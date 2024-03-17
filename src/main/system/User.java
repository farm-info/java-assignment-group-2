package main.system;

public abstract class User extends BaseItem {
    private String password;
    private Role role;

    public enum Role {
        CENTRE_MANAGER,
        TECHNICIAN,
    }

    public User(String username, String password, Role role) {
        setId(username);
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return getId();
    }

    String getPassword() {
        return password;
    }

    void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public String toString() {
        return getId() + "," + password + "," + role;
    }
}
