package main.user_interface;

import javax.swing.*;
import java.awt.event.*;
import javax.swing.table.*;

import java.awt.*;
import java.util.*;
import java.util.List;
import main.system.*;

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
        panel.setLayout(new BorderLayout());

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
        panel.add(titlePanel, BorderLayout.NORTH);

        // Table of appointments
        JTable appointmentsTable = new JTable();
        appointmentsTableModel = new AppointmentsTableModel();
        appointmentsTable.setModel(appointmentsTableModel);

        Action goToAppointment = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                int modelRow = Integer.valueOf(e.getActionCommand());
                Appointment appointment = appointmentsTableModel.appointments.get(modelRow);
                createTechnicianAppointmentWindow(appointment);
            }
        };

        ButtonColumn buttonColumn = new ButtonColumn(appointmentsTable, goToAppointment, 8);
        buttonColumn.setMnemonic(KeyEvent.VK_D);

        JScrollPane scrollPane = new JScrollPane(appointmentsTable);
        appointmentsTable.setFillsViewportHeight(true);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Logout button
        JPanel logoutPanel = new JPanel(new BorderLayout());
        JButton logoutButton = new LogoutButton(userInterface, system).getLogoutButton();
        logoutPanel.add(logoutButton, BorderLayout.EAST);
        panel.add(logoutPanel, BorderLayout.SOUTH);
    }

    public void updateAppointmentsTable() {
        assignedAppointments = system.getAssignedAppointments((Technician) system.getCurrentUser());
        appointmentsTableModel.setAppointments(new ArrayList<>(assignedAppointments.values()));
    }

    private void createTechnicianAppointmentWindow(Appointment appointment) {
        // Option 1: Using a separate window/frame
        AppointmentWindow window = new AppointmentWindow(appointment);
        window.setVisible(true);

        // Option 2: Programmatic navigation within the same window (if applicable)
        // Replace with your specific code to update the UI with appointment details
        // yourPanel.updateAppointmentDetails(appointment);
    }

    public JPanel getPanel() {
        return panel;
    }
}

class AppointmentsTableModel extends AbstractTableModel {
    protected List<Appointment> appointments = new ArrayList<>();

    @Override
    public int getRowCount() {
        return appointments.size();
    }

    @Override
    public int getColumnCount() {
        return 9;
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
            case 8:
                return "View";
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
        switch (column) {
            case 8:
                return true;
            default:
                return false;
        }
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }
}

class AppointmentWindow {
    private JFrame frame;
    private Appointment appointment;

    public AppointmentWindow(Appointment appointment) {
        this.appointment = appointment;

        frame = new JFrame("Appointment Details");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);

        // TODO
    }

    public void setVisible(boolean visible) {
        frame.setVisible(visible);
    }
}
