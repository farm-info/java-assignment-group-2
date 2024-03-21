package main.user_interface;

import java.util.*;
import java.math.BigDecimal;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

import main.system.*;

/*
 * technicianPanel: a table of all appointments, uses getAssignedAppointments()
 * - each row is an appointment and contains a button to view the appointment
 */
class TechnicianPanel extends AppointmentsPanel {
    public TechnicianPanel(UserInterface userInterface, AHHASCSystem system) {
        super(system, "Your appointments");
    }

    @Override
    public void updateItemsTable() {
        assignedAppointments = system.getAssignedAppointments((Technician) system.getCurrentUser());
        appointmentsTableModel.setAppointments(new ArrayList<>(assignedAppointments.values()));
    }

    @Override
    protected void createItemWindow(Appointment appointment) {
        AppointmentWindow window = new TechnicianAppointmentWindow(this, appointment, system);
        window.setVisible(true);
    }

    public JPanel getPanel() {
        return panel;
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
