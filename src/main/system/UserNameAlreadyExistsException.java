package main.system;

public class UserNameAlreadyExistsException extends Exception {
    public UserNameAlreadyExistsException(String message) {
        super(message);
    }
}
