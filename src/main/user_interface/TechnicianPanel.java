package main.user_interface;

import java.util.*;

import javax.swing.*;

import main.system.*;

/*
 * technicianPanel: a table of all appointments, uses getAssignedAppointments()
 * - each row is an appointment and contains a button to view the appointment
 */
class TechnicianPanel extends AppointmentsPanel {
    public TechnicianPanel(UserInterface userInterface, AHHASCSystem system) {
        super(system, "Your appointments");

        // Logout button
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            system.logout();
            userInterface.showPanel("login");
        });
        titleButtonPanel.add(logoutButton, 0);

        JButton changePasswordButton = new JButton("Change password");
        changePasswordButton.addActionListener(e -> {
            new ChangePasswordWindow(system);
        });
        titleButtonPanel.add(changePasswordButton, 1);
    }

    @Override
    public void refreshItemsTable() {
        assignedAppointments = system.getAssignedAppointments((Technician) system.getCurrentUser());
        appointmentsTableModel.setAppointments(new ArrayList<>(assignedAppointments.values()));
    }

    @Override
    protected void createItemWindow(Appointment appointment) {
        AppointmentWindow window = new TechnicianAppointmentWindow(this, appointment, system);
        window.setVisible(true);
    }
}

class TechnicianAppointmentWindow extends AppointmentWindow {
    public TechnicianAppointmentWindow(TechnicianPanel technicianPanel, Appointment appointment,
            AHHASCSystem system) {
        super(technicianPanel, appointment, system);

        paymentStatusBox.setEnabled(true);
        feedbackTextField.setEditable(true);
    }
}
