package main.system;

public class CentreManager extends User {
    public CentreManager(String username, String password) {
        super(username, password, Role.CENTRE_MANAGER);
    }
}
