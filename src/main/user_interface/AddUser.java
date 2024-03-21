package main.user_interface;

import javax.swing.*;
import java.awt.*;

import main.system.*;

public class AddUser extends JFrame {
    private AHHASCSystem system;
    private UserManagement userManagement;

    private JPanel panel;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;
    private JButton submitButton;

    public AddUser(UserManagement userManagement, AHHASCSystem system) {
        this.system = system;
        this.userManagement = userManagement;
        setTitle("AddUser");
        setSize(300, 180);

        panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(panel, BorderLayout.CENTER);

        TitlePanel titlePanel = new TitlePanel("Add User");
        panel.add(titlePanel, BorderLayout.NORTH);

        addFormPanel();
    }

    private void addFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(0, 2));
        panel.add(formPanel, BorderLayout.CENTER);

        JLabel usernameLabel = new JLabel("Username:");
        formPanel.add(usernameLabel);
        usernameField = new JTextField();
        formPanel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        formPanel.add(passwordLabel);
        passwordField = new JPasswordField();
        formPanel.add(passwordField);

        JLabel roleLabel = new JLabel("Role:");
        formPanel.add(roleLabel);
        String[] roles = { "Technician", "Centre Manager" };
        roleComboBox = new JComboBox<>(roles);
        formPanel.add(roleComboBox);

        submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            submit();
        });
        panel.add(submitButton, BorderLayout.SOUTH);
    }

    private void submit() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String roleString = (String) roleComboBox.getSelectedItem();

        // Validation
        if (username.isEmpty() || password.isEmpty() || roleString == null) {
            JFrame frame = new JFrame();
            JOptionPane.showMessageDialog(frame, "Please fill in all the fields.");
            return;
        }

        // Convert string to Role enum
        User.Role role;
        if (roleString.equals("Technician")) {
            role = User.Role.TECHNICIAN;
        } else {
            role = User.Role.CENTRE_MANAGER;
        }

        User newUser;
        try {
            newUser = system.addUser(username, password, role);

        } catch (UserNameAlreadyExistsException e) {
            JFrame frame = new JFrame();
            JOptionPane.showMessageDialog(frame, "Username already exists.");
            return;
        }

        if (newUser == null) {
            JFrame frame = new JFrame();
            JOptionPane.showMessageDialog(frame, "Failed to add user.");
            return;
        }

        JFrame frame = new JFrame();
        JOptionPane.showMessageDialog(frame, "User added successfully.");
        userManagement.updateItemsTable();
        dispose();
    }
}
