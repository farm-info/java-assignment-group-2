package main.user_interface;

import java.util.*;

import javax.swing.*;
import java.awt.*;

import main.system.*;

class CentreManagerAppointmentPanel extends AppointmentsPanel {
    public CentreManagerAppointmentPanel(UserInterface userInterface, AHHASCSystem system) {
        super(userInterface, system, "All appointments");

        // Book appointment
        JButton createAppointmentButton = new JButton("Book appointment");
        createAppointmentButton.addActionListener(e -> {
            BookAppointment window = new BookAppointment(system);
            window.setVisible(true);
        });
        titleButtonPanel.add(createAppointmentButton);
    }

    @Override
    public void updateAppointmentsTable() {
        assignedAppointments = system.getAllAppointments();
        appointmentsTableModel.setAppointments(new ArrayList<>(assignedAppointments.values()));
    }

    @Override
    protected void createTechnicianAppointmentWindow(Appointment appointment) {
        AppointmentWindow window = new CentreManagerAppointmentWindow(appointment, system);
        window.setVisible(true);
    }

    public JPanel getPanel() {
        return panel;
    }
}

/*
 * Window for each appointment
 * - displays all appointment details
 * - modify appointment info
 * - delete appointment
 */
class CentreManagerAppointmentWindow extends AppointmentWindow {
    public CentreManagerAppointmentWindow(Appointment appointment, AHHASCSystem system) {
        super(appointment, system);
    }

    @Override
    protected void createBottomPanels() {
        createDeletePanel();
    }

    private void createDeletePanel() {
        JPanel deletePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton deleteButton = new JButton("Delete appointment");
        deleteButton.addActionListener(e -> {
            createDeleteComfirmationWindow();
        });
        deletePanel.add(deleteButton);
        nestedPanel.add(deletePanel, BorderLayout.SOUTH);
    }

    private void createDeleteComfirmationWindow() {
        JFrame confirmationFrame = new JFrame("Delete appointment?");
        confirmationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        confirmationFrame.setSize(300, 100);
        JPanel confirmationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        confirmationFrame.setContentPane(confirmationPanel);

        JLabel confirmationLabel = new JLabel("Are you sure you want to delete this appointment?");
        confirmationPanel.add(confirmationLabel);

        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(e -> {
            system.removeAppointment(appointment.getId());
            system.saveData();
            frame.dispose();
            confirmationFrame.dispose();
        });
        confirmationPanel.add(confirmButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> confirmationFrame.dispose());
        confirmationPanel.add(cancelButton);

        confirmationFrame.setVisible(true);
    }
}
