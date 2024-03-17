package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UserInterface {
    private JFrame frame;
    private CardLayout cardLayout;

    private JPanel centerPanel;

    private AHHASCSystem system;

    public UserInterface() {
        system = new AHHASCSystem();
        createAndShowUI();
    }

    private void createAndShowUI() {
        cardLayout = new CardLayout();
        frame = new JFrame("AHHASC System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        centerPanel = new JPanel();
        centerPanel.setLayout(cardLayout);

        LoginPanel loginPanel = new LoginPanel(this, system, frame);
        RegisterPanel registerPanel = new RegisterPanel(this, system, frame);
        TechnicianPanel technicianPanel = new TechnicianPanel(this, system, frame);
        centerPanel.add(loginPanel.getPanel(), "login");
        centerPanel.add(registerPanel.getPanel(), "register");
        centerPanel.add(technicianPanel.getPanel(), "technician");

        frame.setContentPane(centerPanel);

        showPanel("login");
    }

    public void showPanel(String panelName) {
        cardLayout.show(centerPanel, panelName);
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
            User.Role userRole = system.login(username, password);
            if (userRole != null) {
                JOptionPane.showMessageDialog(frame, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                // TODO doesn't work until i implement these classes
                if (userRole == User.Role.CENTRE_MANAGER) {
                    userInterface.showPanel("centreManager");
                } else if (userRole == User.Role.TECHNICIAN) {
                    userInterface.showPanel("technician");
                }
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

class TechnicianPanel {
    private JPanel panel;

    public TechnicianPanel(UserInterface userInterface, AHHASCSystem system, JFrame frame) {
        panel = new JPanel();
        panel.setLayout(new FlowLayout());

        // Check assigned appointments
        JButton checkAssignedAppointmentsButton = new JButton("Check assigned appointments");
        checkAssignedAppointmentsButton.addActionListener(e -> {
            // TODO
        });
        panel.add(checkAssignedAppointmentsButton);

        // Collect payment
        JButton collectPaymentButton = new JButton("Collect payment");
        collectPaymentButton.addActionListener(e -> {
            // TODO
        });
        panel.add(collectPaymentButton);

        // Enter feedback
        JButton enterFeedbackButton = new JButton("Enter feedback");
        enterFeedbackButton.addActionListener(e -> {
            // TODO
        });
        panel.add(enterFeedbackButton);

        // Logout button
        // TODO make it reussable?
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            system.logout();
            userInterface.showPanel("login");
        });
        panel.add(logoutButton);
    }

    public JPanel getPanel() {
        return panel;
    }
}
