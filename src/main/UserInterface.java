package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UserInterface {
    private JFrame frame;
    private LoginPanel loginPanel;
    private RegisterPanel registerPanel;
    private AHHASCSystem system;

    public UserInterface() {
        system = new AHHASCSystem();
        createAndShowUI();
    }

    private void createAndShowUI() {
        frame = new JFrame("AHHASC System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        loginPanel = new LoginPanel(this, system, frame);
        registerPanel = new RegisterPanel(this, system, frame);

        showLoginPanel();
    }

    public void showLoginPanel() {
        frame.setContentPane(loginPanel.getPanel());
        frame.pack();
        frame.setVisible(true);
    }

    public void showRegisterPanel() {
        frame.setContentPane(registerPanel.getPanel());
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new UserInterface();
        });
    }
}

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
            Boolean success = system.login(username, password);
            if (success) {
                JOptionPane.showMessageDialog(frame, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "Login failed: Invalid username or password", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(loginButton);

        // Register redirect button
        registerRedirectButton = new JButton("Go to Register page");
        registerRedirectButton.addActionListener(e -> {
            userInterface.showRegisterPanel();
        });
        panel.add(registerRedirectButton);
    }

    public JPanel getPanel() {
        return panel;
    }
}

class RegisterPanel {
    private JPanel panel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton registerButton;

    public RegisterPanel(UserInterface userInterface, AHHASCSystem system, JFrame frame) {
        panel = new JPanel();
        panel.setLayout(new FlowLayout());

        // Registration fields
        JLabel registerUsernameLabel = new JLabel("Username:");
        usernameField = new JTextField(20);
        panel.add(registerUsernameLabel);
        panel.add(usernameField);

        JLabel registerPasswordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);
        panel.add(registerPasswordLabel);
        panel.add(passwordField);

        // Register button
        registerButton = new JButton("Register");
        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = String.valueOf(passwordField.getPassword());

            User success = system.addUser(username, password, User.Role.TECHNICIAN);
            if (success != null) {
                JOptionPane.showMessageDialog(frame, "Registration successful!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                userInterface.showRegisterPanel();
            } else {
                JOptionPane.showMessageDialog(frame, "Registration failed: ", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(registerButton);

        // Redirect to login page
        JButton loginRedirectButton = new JButton("Go to Login page");
        loginRedirectButton.addActionListener(e -> {
            userInterface.showLoginPanel();
        });
        panel.add(loginRedirectButton);
    }

    public JPanel getPanel() {
        return panel;
    }
}
