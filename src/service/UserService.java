package service;

import dao.UserDAO;
import javafx.collections.ObservableList;
import model.User;

public class UserService {
    private UserDAO dao = new UserDAO();

    // =========================
    // GET ALL
    // =========================
    public ObservableList<User> getAll() {
        return dao.getAllUsers();
    }

    // =========================
    // SEARCH
    // =========================
    public ObservableList<User> search(String keyword) {
        return dao.searchUsers(keyword);
    }

    // =========================
    // DELETE
    // =========================
    public void delete(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("ID user tidak valid");
        }
        dao.deleteUser(id);
    }

    // =========================
    // SIMPAN
    // =========================
    public void simpan(User u, boolean modeEdit) throws Exception {
        if (u.getNama() == null || u.getNama().trim().isEmpty()) {
            throw new Exception("Nama wajib diisi");
        }
        if (u.getUsername() == null || u.getUsername().trim().isEmpty()) {
            throw new Exception("Username wajib diisi");
        }
        if (u.getPassword() == null || u.getPassword().trim().isEmpty()) {
            throw new Exception("Password wajib diisi");
        }
        if (u.getIdRole() <= 0) {
            throw new Exception("Role wajib dipilih");
        }

        if (modeEdit) {
            dao.updateUser(u);
        } else {
            dao.insertUser(u);
        }
    }
}
