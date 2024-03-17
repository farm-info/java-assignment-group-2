package main;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class UserInterface {
    private JFrame frame;
    private CardLayout cardLayout;

    private JPanel centerPanel;
    private LoginPanel loginPanel;
    private RegisterPanel registerPanel;
    private TechnicianPanel technicianPanel;

    private AHHASCSystem system;

    public UserInterface() {
        initializeSystem();
        createAndShowUI();
    }

    private void initializeSystem() {
        try {
            system = new AHHASCSystem();
        } catch (ItemNotFoundException | IOException e) {
            JOptionPane.showMessageDialog(
                    frame,
                    "Error initializing system. Data may be corrupted.\n" + e,
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void createAndShowUI() {
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException
                | IllegalAccessException e) {
            e.printStackTrace();
        }

        cardLayout = new CardLayout();
        frame = new JFrame("AHHASC System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        centerPanel = new JPanel();
        centerPanel.setLayout(cardLayout);

        loginPanel = new LoginPanel(this, system, frame);
        registerPanel = new RegisterPanel(this, system, frame);
        technicianPanel = new TechnicianPanel(this, system, frame);
        centerPanel.add(loginPanel.getPanel(), "login");
        centerPanel.add(registerPanel.getPanel(), "register");
        centerPanel.add(technicianPanel.getPanel(), "technician");

        frame.setContentPane(centerPanel);

        showPanel("login");
    }

    public void showPanel(String panelName) {
        if (panelName.equals("technician")) {
            technicianPanel.updateAppointmentsTable();
        }
        cardLayout.show(centerPanel, panelName);
        frame.pack();
        frame.setVisible(true);
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

/*
 * technicianPanel: a table of all appointments, uses getAssignedAppointments()
 * - each row is an appointment and contains a button to view the appointment
 * each appointment has a page, which can use:
 * - collectPayment() from customers
 * - enterFeedback() for customers to enter feedback after each appointment
 * - a back button to return to the table
 */
class TechnicianPanel {
    private JPanel panel;
    private AHHASCSystem system;
    private Map<String, Appointment> assignedAppointments;
    private AppointmentsTableModel appointmentsTableModel;

    public TechnicianPanel(UserInterface userInterface, AHHASCSystem system, JFrame frame) {
        this.system = system;

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Title
        JPanel titlePanel = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Your appointments");
        titlePanel.add(title, BorderLayout.WEST);

        // Update table button
        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(e -> {
            updateAppointmentsTable();
        });
        titlePanel.add(updateButton, BorderLayout.EAST);
        panel.add(titlePanel);

        // Table of appointments
        JTable appointmentsTable = new JTable();
        appointmentsTableModel = new AppointmentsTableModel();
        appointmentsTable.setModel(appointmentsTableModel);
        JScrollPane scrollPane = new JScrollPane(appointmentsTable);
        appointmentsTable.setFillsViewportHeight(true);
        panel.add(scrollPane);

        // Logout button
        JButton logoutButton = new LogoutButton(userInterface, system).getLogoutButton();
        panel.add(logoutButton);
    }

    public void updateAppointmentsTable() {
        assignedAppointments = system.getAssignedAppointments((Technician) system.getCurrentUser());
        appointmentsTableModel.setAppointments(new ArrayList<>(assignedAppointments.values()));
    }

    public JPanel getPanel() {
        return panel;
    }
}

class AppointmentsTableModel extends AbstractTableModel {
    private List<Appointment> appointments = new ArrayList<>();

    @Override
    public int getRowCount() {
        return appointments.size();
    }

    @Override
    public int getColumnCount() {
        return 8;
    }

    public Object getValueAt(int row, int column) {
        Appointment appointment = appointments.get(row);
        switch (column) {
            case 0:
                return appointment.getCustomer().getName();
            case 1:
                return appointment.getCustomer().getContactEmail();
            case 2:
                return appointment.getCustomer().getContactNumber();
            case 3:
                return appointment.getAppointmentDate();
            case 4:
                return appointment.getCreationDate();
            case 5:
                return appointment.getPaymentStatus();
            case 6:
                return appointment.getPaymentAmount();
            case 7:
                return appointment.getFeedback();
            // TODO button to view appointment
            default:
                return null;
        }
    }

    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "Customer Name";
            case 1:
                return "Customer Email";
            case 2:
                return "Customer Phone";
            case 3:
                return "Appointment Date";
            case 4:
                return "Creation Date";
            case 5:
                return "Payment Status";
            case 6:
                return "Payment Amount";
            case 7:
                return "Feedback";
            case 8:
                return "View Appointment";
            default:
                return null;
        }
    }

    public boolean isCellEditable(int row, int column) {
        return false;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }
}
