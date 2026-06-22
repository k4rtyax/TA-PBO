package model;

public class User {

    private int idUser;
    private String username;
    private String password;
    private String role;

    public User(int idUser, String username,
                String password, String role) {

        this.idUser = idUser;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public boolean login(String user, String pass) {

        return username.equals(user)
                && password.equals(pass);
    }

    public void logout() {
        System.out.println("Logout berhasil");
    }

    public String getRole() {
        return role;
    }
}