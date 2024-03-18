package main.user_interface;

import java.awt.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;

import javax.swing.*;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

import main.system.AHHASCSystem;
import main.system.Appointment;

/*
 * Window for each appointment
 * - displays information about the appointment
 * - collectPayment() from customers
 * - enterFeedback() for customers to enter feedback after each appointment
 */
class AppointmentWindow {
    // TODO decimal format add to all
    private DecimalFormat decimalFormat = new DecimalFormat("#0.00");
    private AHHASCSystem system;
    private Appointment appointment;

    private JFrame frame;
    private JPanel panel, nestedPanel;
    private Boolean paymentStatus;
    private BigDecimal paymentAmount;
    private JLabel paymentStatusLabel, paymentAmountLabel;

    public AppointmentWindow(Appointment appointment, AHHASCSystem system) {
        this.appointment = appointment;
        this.system = system;

        createFrameAndPanel();
        createTitlePanel();
        createDetailsPanel();
        // for technicians
        createPaymentPanel();
        createFeedbackPanel();

        frame.setContentPane(panel);
    }

    private void createFrameAndPanel() {
        frame = new JFrame("Appointment Details");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);
        this.nestedPanel = new JPanel(new BorderLayout());
        this.panel = new JPanel(new BorderLayout());
        this.panel.add(nestedPanel, BorderLayout.CENTER);
    }

    private void createTitlePanel() {
        JPanel titlePanel = new TitlePanel("Appointment Details").getTitlePanel();

        // Back button
        JButton backButton = new JButton("Close");
        backButton.addActionListener(e -> {
            frame.dispose();
        });
        titlePanel.add(backButton, BorderLayout.EAST);

        nestedPanel.add(titlePanel, BorderLayout.NORTH);
    }

    private void createDetailsPanel() {
        JPanel detailsPanel = new JPanel(new GridLayout(0, 2));
        detailsPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        detailsPanel.add(new JLabel("Customer Name:"));
        detailsPanel.add(new JLabel(appointment.getCustomer().getName()));
        detailsPanel.add(new JLabel("Customer Email:"));
        detailsPanel.add(new JLabel(appointment.getCustomer().getContactEmail()));
        detailsPanel.add(new JLabel("Customer Phone:"));
        detailsPanel.add(new JLabel(appointment.getCustomer().getContactNumber()));
        detailsPanel.add(new JLabel("Appointment Date:"));
        detailsPanel.add(new JLabel(appointment.getAppointmentDate().toString()));
        detailsPanel.add(new JLabel("Creation Date:"));
        detailsPanel.add(new JLabel(appointment.getCreationDate().toString()));
        detailsPanel.add(new JLabel("Payment Status:"));

        this.paymentStatus = appointment.getPaymentStatus();
        this.paymentStatusLabel = new JLabel(paymentStatus ? "Paid" : "Unpaid");
        detailsPanel.add(paymentStatusLabel);

        detailsPanel.add(new JLabel("Payment Amount:"));
        this.paymentAmount = appointment.getPaymentAmount();
        this.paymentAmountLabel = new JLabel(decimalFormat.format(paymentAmount));

        detailsPanel.add(paymentAmountLabel);
        detailsPanel.add(new JLabel("Feedback:"));
        detailsPanel.add(new JLabel(appointment.getFeedback()));
        nestedPanel.add(detailsPanel, BorderLayout.CENTER);
    }

    private void createPaymentPanel() {
        JPanel collectPaymentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
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

    public void setVisible(boolean visible) {
        frame.setVisible(visible);
    }
}
