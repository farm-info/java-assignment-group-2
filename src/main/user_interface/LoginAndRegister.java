package main.user_interface;

import javax.swing.*;
import java.awt.*;
import main.system.AHHASCSystem;
import main.system.User;
import main.system.UserNameAlreadyExistsException;

class LoginPanel extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, registerRedirectButton;

    public LoginPanel(UserInterface userInterface, AHHASCSystem system, JFrame frame) {
        setLayout(new FlowLayout());

        // Username label and field
        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(20);
        add(usernameLabel);
        add(usernameField);

        // Password label and field
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);
        add(passwordLabel);
        add(passwordField);

        // Login button
        loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = String.valueOf(passwordField.getPassword());

            User.Role userRole = system.login(username, password);
            if (userRole != null) {
                JOptionPane.showMessageDialog(frame, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);

                if (userRole == User.Role.CENTRE_MANAGER) {
                    userInterface.showPanel("centreManager");
                } else if (userRole == User.Role.TECHNICIAN) {
                    userInterface.showPanel("technician");
                }

                // clear username and password fields
                usernameField.setText("");
                passwordField.setText("");

            } else {
                JOptionPane.showMessageDialog(frame, "Login failed: Invalid username or password", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        add(loginButton);

        // Register redirect button
        registerRedirectButton = new JButton("Go to Register page");
        registerRedirectButton.addActionListener(e -> {
            userInterface.showPanel("register");
        });
        add(registerRedirectButton);
    }
}

class RegisterPanel extends JPanel {

    public RegisterPanel(UserInterface userInterface, AHHASCSystem system, JFrame frame) {
        setLayout(new FlowLayout());

        // Registration fields
        JLabel registerUsernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField(20);
        add(registerUsernameLabel);
        add(usernameField);

        JLabel registerPasswordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(20);
        add(registerPasswordLabel);
        add(passwordField);

        // Register button
        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = String.valueOf(passwordField.getPassword());

            User newUser;
            try {
                newUser = system.addUser(username, password, User.Role.TECHNICIAN);

            } catch (UserNameAlreadyExistsException e1) {
                JOptionPane.showMessageDialog(
                        frame,
                        "User already exists",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (newUser == null) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Registration failed",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);

            } else {
                JOptionPane.showMessageDialog(
                        frame,
                        "Registration successful!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                userInterface.showPanel("login");
                // clear username and password fields
                usernameField.setText("");
                passwordField.setText("");
            }
        });
        add(registerButton);

        // Redirect to login page
        JButton loginRedirectButton = new JButton("Go to Login page");
        loginRedirectButton.addActionListener(e -> {
            userInterface.showPanel("login");
        });
        add(loginRedirectButton);
    }
}

class LogoutButton {
    private JButton logoutButton;
    private AHHASCSystem system;
    private UserInterface userInterface;

    public LogoutButton(UserInterface userInterface, AHHASCSystem system) {
        this.system = system;
        this.userInterface = userInterface;
        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            this.system.logout();
            this.userInterface.showPanel("login");
        });
    }

    public JButton getLogoutButton() {
        return logoutButton;
    }
}
