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
}
