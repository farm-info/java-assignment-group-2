package main.user_interface;

import java.util.*;

import javax.swing.*;
import java.awt.*;

import main.system.*;

class CentreManagerAppointmentPanel extends AppointmentsPanel {
    public CentreManagerAppointmentPanel(CentreManagerCard centreManagerCard, AHHASCSystem system) {
        super(system, "All appointments");

        // Change table button text
        appointmentsTableModel.buttonText = "Edit";

        // Back button
        JButton createBackButton = new JButton("Back");
        createBackButton.addActionListener(e -> {
            centreManagerCard.showPanel("centreManager");
        });
        titleButtonPanel.add(createBackButton, 0);

        // Book appointment
        JButton createAppointmentButton = new JButton("Book appointment");
        createAppointmentButton.addActionListener(e -> {
            BookAppointment window = new BookAppointment(this, system);
            window.setVisible(true);
        });
        titleButtonPanel.add(createAppointmentButton);
    }

    @Override
    public void refreshItemsTable() {
        assignedAppointments = system.getAllAppointments();
        appointmentsTableModel.setAppointments(new ArrayList<>(assignedAppointments.values()));
    }

    @Override
    protected void createItemWindow(Appointment appointment) {
        AppointmentWindow window = new CentreManagerAppointmentWindow(this, appointment, system);
        window.setVisible(true);
    }
}

/*
 * Window for each appointment
 * - displays all appointment details
 * - modify appointment info
 * - delete appointment
 */
class CentreManagerAppointmentWindow extends AppointmentWindow {
    private CentreManagerAppointmentPanel centreManagerAppointmentPanel;

    public CentreManagerAppointmentWindow(CentreManagerAppointmentPanel centreManagerAppointmentPanel,
            Appointment appointment,
            AHHASCSystem system) {

        super(centreManagerAppointmentPanel, appointment, system);
        this.centreManagerAppointmentPanel = centreManagerAppointmentPanel;

        appointmentDateField.setEditable(true);
        paymentAmountField.setEditable(true);
        paymentStatusBox.setEnabled(true);
        feedbackTextField.setEditable(true);

        JButton deleteButton = new JButton("Delete appointment");
        deleteButton.addActionListener(e -> {
            JFrame window = createDeleteComfirmationWindow();
            window.setVisible(true);
        });
        bottomPanel.add(deleteButton);
        nestedPanel.add(bottomSavePanel, BorderLayout.SOUTH);
    }

    private JFrame createDeleteComfirmationWindow() {
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
            centreManagerAppointmentPanel.refreshItemsTable();
            dispose();
            confirmationFrame.dispose();
        });
        confirmationPanel.add(confirmButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> confirmationFrame.dispose());
        confirmationPanel.add(cancelButton);

        return confirmationFrame;
    }
}
