package az.qala.permissionbased.utils;

import az.qala.permissionbased.constants.AuthenticationConstants;
import az.qala.permissionbased.model.entity.Role;
import az.qala.permissionbased.model.entity.User;
import az.qala.permissionbased.model.enums.UserRoles;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JwtUtils {

    private final SecretKey secretKey;
    private final Long jwtExpirationMs;

    public JwtUtils(@Value("${app.jwt.secret}") String secret,
                    @Value("${app.jwt.refreshExpiration:3600000}") Long jwtExpirationMs) {

        this.secretKey = getSigningKey(secret);
        this.jwtExpirationMs = jwtExpirationMs;
    }

    private SecretKey getSigningKey(String secret) {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(@NotNull User user) {
        Map<String, Object> claims = new HashMap<>();

        claims.put(AuthenticationConstants.USER_ID, user.getId());
        claims.put(AuthenticationConstants.USERNAME, user.getUsername());
        claims.put(AuthenticationConstants.USER_EMAIL, user.getEmail());
        claims.put(AuthenticationConstants.USER_REGISTRATION_STATUS, user.getRegistrationStatus());
        claims.put(AuthenticationConstants.LAST_LOGIN, LocalDateTime.now().toString());

        List<UserRoles> rolesList = user.getRoles().stream()
                .map((role) -> {
                    // note that conversion (meaning we can save to List<UserRoles>)
                    // happens because roleName is enum and when we fetch that jpa converts it automatically to UserRoles enum

                    return role.getRoleName(); // == Role::getRoleName
                })
                .toList();


        claims.put(AuthenticationConstants.ROLE, rolesList);

        return createToken(claims, user.getEmail());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String refreshToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return createToken(claims, claims.getSubject());
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("username", String.class);
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String usernameFromToken = claims.get("username", String.class);
            String usernameFromUserDetails = userDetails.getUsername();


            return (usernameFromToken.equals(usernameFromUserDetails) && !claims.getExpiration().before(new Date()));
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getEmail(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public List<String> getRoles(String token) {
        return getAllClaimsFromToken(token).get(AuthenticationConstants.ROLE, List.class);
    }

    private Claims getAllClaimsFromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

}
