package main.user_interface;

import javax.swing.*;
import java.awt.*;
import main.system.AHHASCSystem;
import main.system.User;

class LoginPanel {
    private JPanel panel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, registerRedirectButton;

    public LoginPanel(UserInterface userInterface, AHHASCSystem system, JFrame frame) {
        panel = new JPanel();
        panel.setLayout(new FlowLayout());

        // Username label and field
        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(20);
        panel.add(usernameLabel);
        panel.add(usernameField);

        // Password label and field
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);
        panel.add(passwordLabel);
        panel.add(passwordField);

        // Login button
        loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = String.valueOf(passwordField.getPassword());

            User.Role userRole = system.login(username, password);
            if (userRole != null) {
                JOptionPane.showMessageDialog(frame, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);

                // TODO implement the panels
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
        panel.add(loginButton);

        // Register redirect button
        registerRedirectButton = new JButton("Go to Register page");
        registerRedirectButton.addActionListener(e -> {
            userInterface.showPanel("register");
        });
        panel.add(registerRedirectButton);
    }

    public JPanel getPanel() {
        return panel;
    }
}

class RegisterPanel {
    private JPanel panel;

    public RegisterPanel(UserInterface userInterface, AHHASCSystem system, JFrame frame) {
        panel = new JPanel();
        panel.setLayout(new FlowLayout());

        // Registration fields
        JLabel registerUsernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField(20);
        panel.add(registerUsernameLabel);
        panel.add(usernameField);

        JLabel registerPasswordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(20);
        panel.add(registerPasswordLabel);
        panel.add(passwordField);

        // Register button
        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = String.valueOf(passwordField.getPassword());

            User success = system.addUser(username, password, User.Role.TECHNICIAN);
            if (success != null) {
                JOptionPane.showMessageDialog(frame, "Registration successful!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                userInterface.showPanel("login");

                // clear username and password fields
                usernameField.setText("");
                passwordField.setText("");

            } else {
                JOptionPane.showMessageDialog(frame, "Registration failed: ", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(registerButton);

        // Redirect to login page
        JButton loginRedirectButton = new JButton("Go to Login page");
        loginRedirectButton.addActionListener(e -> {
            userInterface.showPanel("login");
        });
        panel.add(loginRedirectButton);
    }

    public JPanel getPanel() {
        return panel;
    }
}

class LogoutButton {
    private JButton logoutButton;

    public LogoutButton(UserInterface userInterface, AHHASCSystem system) {
        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            system.logout();
            userInterface.showPanel("login");
        });
    }

    public JButton getLogoutButton() {
        return logoutButton;
    }
}
