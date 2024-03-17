package main.system;

public class Technician extends User {
    public Technician(String username, String password) {
        super(username, password, Role.TECHNICIAN);
    }
}
