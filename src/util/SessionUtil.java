package util;

import model.User;

/**
 * Session manager sederhana — menyimpan user yang sedang login
 * secara global (static) selama aplikasi berjalan.
 */
public class SessionUtil {

    private static User currentUser = null;

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void clearSession() {
        currentUser = null;
    }

    /** Kembalikan nama role. Contoh: "Admin", "Petugas", "Dokter" */
    public static String getCurrentRole() {
        if (currentUser == null) return "";
        return currentUser.getRoleName();
    }

    public static boolean isAdmin() {
        return "Admin".equalsIgnoreCase(getCurrentRole());
    }

    public static boolean isPetugas() {
        return "Petugas".equalsIgnoreCase(getCurrentRole());
    }

    public static boolean isDokter() {
        return "Dokter".equalsIgnoreCase(getCurrentRole());
    }

    public static Integer getCurrentDoctorId() {
        return currentUser == null ? null : currentUser.getIdDokter();
    }

    public static boolean hasRole(String... allowedRoles) {
        String role = getCurrentRole();
        for (String r : allowedRoles) {
            if (r.equalsIgnoreCase(role)) return true;
        }
        return false;
    }

    /** Tolak aksi (dengan alert) jika role saat ini bukan salah satu dari allowedRoles. */
    public static boolean requireRole(String... allowedRoles) {
        if (hasRole(allowedRoles)) return true;
        util.AlertUtil.warning("Anda tidak memiliki akses untuk melakukan aksi ini.");
        return false;
    }
}
