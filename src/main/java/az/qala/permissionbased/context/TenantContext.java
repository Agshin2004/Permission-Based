package az.qala.permissionbased.context;


/**
 * implementing this class so every thread will get its own tenantId
 */
public final class TenantContext {
    // current thread
    private static final ThreadLocal<String> CURRENT = new ThreadLocal<>();

    private TenantContext() {
    }

    public static void setTenantId(String tenantId) {
        CURRENT.set(tenantId);
    }

    public static String getTenantid() {
        return CURRENT.get();
    }

    public static void clear() {
        CURRENT.remove();
    }
}
