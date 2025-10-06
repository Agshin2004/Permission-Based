package az.qala.permissionbased.filter;

import az.qala.permissionbased.config.CustomUserDetails;
import az.qala.permissionbased.context.TenantContext;
import az.qala.permissionbased.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class TenantFilter extends OncePerRequestFilter {
    public static final String TENANT_HEADER = "X-Tenant-Id";

    private final UserRepository userRepository;

    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {
        try {
            String tenantId = request.getHeader(TENANT_HEADER);
            if (tenantId == null || tenantId.isBlank()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Missing tenant id header: " + TENANT_HEADER);
                return;
            }

            // getting current user from spring sec
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "NOT AUTHNETICATEDD");
                return;
            }
            CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();

            // checking if user has access to tenant
            boolean allowed = userRepository.userHasTenant(user.getUser().getId(), Long.valueOf(tenantId));
            if (!allowed) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "No access to tenant " + tenantId);
                return;
            }

            TenantContext.setTenantId(tenantId.trim());
            chain.doFilter(request, response);
        } finally {
            // cleaning context since threads can be reused for different request (from threadpool)
            TenantContext.clear();
        }
    }

}
