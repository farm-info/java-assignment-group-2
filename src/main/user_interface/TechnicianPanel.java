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
        super(userInterface, system, "Your appointments");
    }

    @Override
    public void updateAppointmentsTable() {
        assignedAppointments = system.getAssignedAppointments((Technician) system.getCurrentUser());
        appointmentsTableModel.setAppointments(new ArrayList<>(assignedAppointments.values()));
    }

    @Override
    protected void createTechnicianAppointmentWindow(Appointment appointment) {
        AppointmentWindow window = new TechnicianAppointmentWindow(appointment, system);
        window.setVisible(true);
    }

    public JPanel getPanel() {
        return panel;
    }
}

class TechnicianAppointmentWindow extends AppointmentWindow {
    public TechnicianAppointmentWindow(Appointment appointment, AHHASCSystem system) {
        super(appointment, system);
    }

    @Override
    protected void createBottomPanels() {
        createPaymentPanel();
        createFeedbackPanel();
    }

    private void createPaymentPanel() {
        JPanel collectPaymentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        // TODO wait... is this supposed to be modified by the technician?
        JFormattedTextField paymentAmountField = new JFormattedTextField(paymentAmount);
        DefaultFormatter fmt = new NumberFormatter(decimalFormat);
        fmt.setValueClass(paymentAmountField.getValue().getClass());
        DefaultFormatterFactory fmtFactory = new DefaultFormatterFactory(fmt, fmt, fmt);
        paymentAmountField.setFormatterFactory(fmtFactory);

        JCheckBox paymentStatusCheckbox = new JCheckBox("Paid", paymentStatus);
        JButton collectPaymentButton = new JButton("Modify payment status");
        collectPaymentButton.addActionListener(e -> {
            try {
                BigDecimal newPaymentAmount = new BigDecimal(paymentAmountField.getText());
                system.setAppointmentPayment(appointment.getId(), newPaymentAmount, paymentStatusCheckbox.isSelected());
                // Update data // Payment panel

                paymentStatusLabel.setText(paymentStatusCheckbox.isSelected() ? "Paid" : "Unpaid");
                paymentAmountLabel.setText(decimalFormat.format(newPaymentAmount));
                JOptionPane.showMessageDialog(frame, "Payment updated successfully.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid payment amount.");
            }
        });
        collectPaymentPanel.add(new JLabel("Payment amount:"));
        collectPaymentPanel.add(paymentAmountField);
        collectPaymentPanel.add(paymentStatusCheckbox);
        collectPaymentPanel.add(collectPaymentButton);
        nestedPanel.add(collectPaymentPanel, BorderLayout.SOUTH);
    }

    private void createFeedbackPanel() {
        JPanel feedbackPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField feedbackField = new JTextField(appointment.getFeedback());
        JButton enterFeedbackButton = new JButton("Enter feedback");
        enterFeedbackButton.addActionListener(e -> {
            system.setAppointmentFeedback(appointment.getId(), feedbackField.getText());
            JOptionPane.showMessageDialog(frame, "Feedback updated successfully.");
        });
        feedbackPanel.add(new JLabel("Feedback:"));
        feedbackPanel.add(feedbackField);
        feedbackPanel.add(enterFeedbackButton);
        panel.add(feedbackPanel, BorderLayout.SOUTH);
    }
}
