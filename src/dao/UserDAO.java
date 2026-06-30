package dao;

import database.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.User;
import util.PasswordUtil;
import java.sql.*;

public class UserDAO {
    Connection conn = DBConnection.connect();

    // GET ALL
    public ObservableList<User> getAllUsers() {
        ObservableList<User> list = FXCollections.observableArrayList();
        String sql = """
                SELECT u.*, r.nama_role, d.nama AS nama_dokter
                FROM users u
                LEFT JOIN roles r ON u.id_role = r.id_role
                LEFT JOIN dokter d ON u.id_dokter = d.id_dokter
                """;
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                User u = new User(
                        rs.getInt("id_user"),
                        rs.getString("nama"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getInt("id_role"),
                        rs.getObject("id_dokter") != null ? rs.getInt("id_dokter") : null
                );
                u.setRoleName(rs.getString("nama_role"));
                u.setNamaDokter(rs.getString("nama_dokter") != null ? rs.getString("nama_dokter") : "-");
                list.add(u);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // INSERT
    public void insertUser(User u) {
        String sql = "INSERT INTO users(nama, username, password, id_role, id_dokter) VALUES(?,?,?,?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, u.getNama());
            ps.setString(2, u.getUsername());
            ps.setString(3, PasswordUtil.hash(u.getPassword()));
            ps.setInt(4, u.getIdRole());
            if (u.getIdDokter() != null) {
                ps.setInt(5, u.getIdDokter());
            } else {
                ps.setNull(5, Types.INTEGER);
            }
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // UPDATE (password baru dalam bentuk plaintext, akan di-hash)
    public void updateUser(User u) {
        updateUserRaw(u, PasswordUtil.hash(u.getPassword()));
    }

    // UPDATE tanpa mengubah password (mis. field password dikosongkan saat edit) -
    // u.getPassword() di sini diasumsikan SUDAH berupa hash lama, bukan plaintext.
    public void updateUserKeepHashedPassword(User u) {
        updateUserRaw(u, u.getPassword());
    }

    private void updateUserRaw(User u, String passwordToStore) {
        String sql = "UPDATE users SET nama=?, username=?, password=?, id_role=?, id_dokter=? WHERE id_user=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, u.getNama());
            ps.setString(2, u.getUsername());
            ps.setString(3, passwordToStore);
            ps.setInt(4, u.getIdRole());
            if (u.getIdDokter() != null) {
                ps.setInt(5, u.getIdDokter());
            } else {
                ps.setNull(5, Types.INTEGER);
            }
            ps.setInt(6, u.getIdUser());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // DELETE
    public void deleteUser(int id) {
        try {
            String sql = "DELETE FROM users WHERE id_user=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // LOGIN - Cari user berdasarkan username, lalu verifikasi password hash di Java
    public User findByCredentials(String username, String password) {
        String sql = """
                SELECT u.*, r.nama_role
                FROM users u
                LEFT JOIN roles r ON u.id_role = r.id_role
                WHERE u.username = ?
                """;
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String storedHash = rs.getString("password");
                if (!PasswordUtil.verify(password, storedHash)) {
                    return null; // password salah
                }
                User u = new User(
                        rs.getInt("id_user"),
                        rs.getString("nama"),
                        rs.getString("username"),
                        storedHash,
                        rs.getInt("id_role"),
                        rs.getObject("id_dokter") != null ? rs.getInt("id_dokter") : null
                );
                u.setRoleName(rs.getString("nama_role"));
                return u;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // null = credentials salah
    }

    // Ambil hash password tersimpan untuk satu user (single-row, bukan full-table fetch)
    public String getPasswordById(int idUser) throws SQLException {
        String sql = "SELECT password FROM users WHERE id_user=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, idUser);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getString("password");
        }
        return null;
    }

    // SEARCH
    public ObservableList<User> searchUsers(String keyword) {
        ObservableList<User> list = FXCollections.observableArrayList();
        String sql = """
                SELECT u.*, r.nama_role, d.nama AS nama_dokter
                FROM users u
                LEFT JOIN roles r ON u.id_role = r.id_role
                LEFT JOIN dokter d ON u.id_dokter = d.id_dokter
                WHERE u.nama LIKE ? OR u.username LIKE ? OR r.nama_role LIKE ?
                """;
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            String searchStr = "%" + keyword + "%";
            ps.setString(1, searchStr);
            ps.setString(2, searchStr);
            ps.setString(3, searchStr);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User u = new User(
                        rs.getInt("id_user"),
                        rs.getString("nama"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getInt("id_role"),
                        rs.getObject("id_dokter") != null ? rs.getInt("id_dokter") : null
                );
                u.setRoleName(rs.getString("nama_role"));
                u.setNamaDokter(rs.getString("nama_dokter") != null ? rs.getString("nama_dokter") : "-");
                list.add(u);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
