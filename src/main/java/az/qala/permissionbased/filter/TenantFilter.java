package az.qala.permissionbased.filter;

import az.qala.permissionbased.tenant.TenantContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class TenantFilter extends OncePerRequestFilter {
    public static final String TENANT_HEADER = "X-Tenant-Id";

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

            TenantContext.setTenantId(tenantId.trim());
            chain.doFilter(request, response);
        } finally {
            // cleaning context since threads can be reused for different request (from threadpool)
            TenantContext.clear();
        }
    }

}
