package main.user_interface;

import javax.swing.*;
import java.awt.*;

import main.system.*;

public class ChangePasswordWindow extends JFrame {
    private AHHASCSystem system;

    private JPanel formPanel;

    private JPasswordField oldPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;

    public ChangePasswordWindow(AHHASCSystem system) {
        this.system = system;

        initFrame();
        createTitle();
        createForm();

        setVisible(true);
    }

    private void initFrame() {
        setTitle("Change Password");
        setSize(340, 150);
        setLayout(new BorderLayout());
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void createTitle() {
        TitlePanel titlePanel = new TitlePanel("Change Password");
        add(titlePanel, BorderLayout.NORTH);
    }

    private void createForm() {
        formPanel = new JPanel(new GridLayout(0, 2));

        formPanel.add(new JLabel("Old Password:"));
        oldPasswordField = new JPasswordField(20);
        formPanel.add(oldPasswordField);

        formPanel.add(new JLabel("New Password:"));
        newPasswordField = new JPasswordField(20);
        formPanel.add(newPasswordField);

        formPanel.add(new JLabel("Confirm New Password:"));
        confirmPasswordField = new JPasswordField(20);
        formPanel.add(confirmPasswordField);

        add(formPanel, BorderLayout.CENTER);

        JButton changePasswordButton = new JButton("Comfirm");
        changePasswordButton.addActionListener(changePassword -> changePassword());
        add(changePasswordButton, BorderLayout.SOUTH);
    }

    private void changePassword() {
        String oldPassword = new String(oldPasswordField.getPassword());
        String newPassword = new String(newPasswordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (!newPassword.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(null, "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Boolean passwordChanged = system.changePassword(oldPassword, newPassword);

        if (!passwordChanged) {
            JOptionPane.showMessageDialog(null, "Old password is incorrect", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(null, "Password changed successfully!", "Success",
                JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
}
