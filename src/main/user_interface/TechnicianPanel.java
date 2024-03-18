package main.user_interface;

import javax.swing.*;

import java.util.*;

import main.system.*;

/*
 * technicianPanel: a table of all appointments, uses getAssignedAppointments()
 * - each row is an appointment and contains a button to view the appointment
 */
class TechnicianPanel extends AppointmentsPanel {

    public TechnicianPanel(UserInterface userInterface, AHHASCSystem system) {
        super(userInterface, system, "Your appointments");
    }

    @Override
    public void updateAppointmentsTable() {
        assignedAppointments = system.getAssignedAppointments((Technician) system.getCurrentUser());
        appointmentsTableModel.setAppointments(new ArrayList<>(assignedAppointments.values()));
    }

    @Override
    protected void createTechnicianAppointmentWindow(Appointment appointment) {
        // TODO split appointment window to technician and centre manager
        AppointmentWindow window = new AppointmentWindow(appointment, system);
        window.setVisible(true);
    }

    public JPanel getPanel() {
        return panel;
    }
}
