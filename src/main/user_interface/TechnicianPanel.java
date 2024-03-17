package main.user_interface;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
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
