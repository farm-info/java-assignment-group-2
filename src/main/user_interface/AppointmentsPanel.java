package main.user_interface;

import java.util.*;
import java.util.List;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;

import main.system.*;

abstract class AppointmentsPanel extends TablePanel {
    protected Map<String, Appointment> assignedAppointments;
    protected AppointmentsTableModel appointmentsTableModel;

    public AppointmentsPanel(AHHASCSystem system, String title) {
        super(system, title, 8);
    }

    @Override
    public void setModel(JTable appointmentsTable) {
        appointmentsTableModel = new AppointmentsTableModel();
        appointmentsTable.setModel(appointmentsTableModel);
    }

    @Override
    public void goToItem(int modelRow) {
        Appointment appointment = appointmentsTableModel.appointments.get(modelRow);
        createItemWindow(appointment);
    }

    abstract protected void createItemWindow(Appointment appointment);
}

/*
 * Window for each appointment
 * - displays information about the appointment
 * - collectPayment() from customers
 * - enterFeedback() for customers to enter feedback after each appointment
 */
abstract class AppointmentWindow {
    // TODO decimal format add to all
    protected DecimalFormat decimalFormat = new DecimalFormat("#0.00");

    protected AppointmentsPanel appointmentsPanel;
    protected AHHASCSystem system;
    protected Appointment appointment;

    protected JFrame frame;
    protected JPanel panel, nestedPanel, bottomPanel, bottomSavePanel;
    protected Boolean paymentStatus;
    protected BigDecimal paymentAmount;
    protected ReadOnlyTextField appointmentDateField, feedbackTextField;
    protected ReadOnlyFormattedTextField paymentAmountField;
    protected JCheckBox paymentStatusBox;

    public AppointmentWindow(AppointmentsPanel appointmentsPanel, Appointment appointment, AHHASCSystem system) {
        this.appointmentsPanel = appointmentsPanel;
        this.appointment = appointment;
        this.system = system;

        createFrameAndPanel();
        createTitlePanel();
        createDetailsPanel();
        createBottomPanels();

        frame.setContentPane(panel);
    }

    private void createFrameAndPanel() {
        frame = new JFrame("Appointment Details");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);
        nestedPanel = new JPanel(new BorderLayout());
        panel = new JPanel(new BorderLayout());
        panel.add(nestedPanel, BorderLayout.CENTER);
    }

    private void createTitlePanel() {
        JPanel titlePanel = new TitlePanel("Appointment Details");
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

        detailsPanel.add(new JLabel("Creation Date:"));
        detailsPanel.add(new JLabel(appointment.getCreationDate().toString()));

        detailsPanel.add(new JLabel("Appointment Date:"));
        appointmentDateField = new ReadOnlyTextField(appointment.getAppointmentDate().toString());
        detailsPanel.add(appointmentDateField);

        detailsPanel.add(new JLabel("Payment Status:"));

        paymentStatus = appointment.getPaymentStatus();
        paymentStatusBox = new JCheckBox("Paid", paymentStatus);
        paymentStatusBox.setEnabled(false);
        detailsPanel.add(paymentStatusBox);

        detailsPanel.add(new JLabel("Payment Amount:"));

        paymentAmount = appointment.getPaymentAmount();
        paymentAmountField = new ReadOnlyFormattedTextField(paymentAmount);
        DefaultFormatter fmt = new NumberFormatter(decimalFormat);
        fmt.setValueClass(paymentAmountField.getValue().getClass());
        DefaultFormatterFactory fmtFactory = new DefaultFormatterFactory(fmt, fmt, fmt);
        paymentAmountField.setFormatterFactory(fmtFactory);

        detailsPanel.add(paymentAmountField);

        detailsPanel.add(new JLabel("Feedback:"));
        feedbackTextField = new ReadOnlyTextField(appointment.getFeedback());
        detailsPanel.add(feedbackTextField);

        nestedPanel.add(detailsPanel, BorderLayout.CENTER);
    }

    private void createBottomPanels() {
        bottomPanel = new JPanel(new BorderLayout());
        bottomSavePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        // Back button
        JButton backButton = new JButton("Close");
        backButton.addActionListener(e -> {
            frame.dispose();
        });
        bottomSavePanel.add(backButton);

        // Save button
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            saveAppointment();
        });
        bottomSavePanel.add(saveButton);

        bottomPanel.add(bottomSavePanel, BorderLayout.EAST);
        nestedPanel.add(bottomPanel, BorderLayout.SOUTH);
    }

    private void saveAppointment() {
        try {
            BigDecimal newPaymentAmount = new BigDecimal(paymentAmountField.getText());
            Boolean paymentStatus = paymentStatusBox.isSelected();
            system.setAppointmentPayment(appointment, newPaymentAmount, paymentStatus);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Invalid payment amount.");
            return;
        }

        String feedback = feedbackTextField.getText();
        system.setAppointmentFeedback(appointment, feedback);

        String appointmentDateString = appointmentDateField.getText();
        LocalDate appointmentDate = LocalDate.parse(appointmentDateString);
        system.setAppointmentDate(appointment, appointmentDate);

        appointmentsPanel.refreshItemsTable();
        JOptionPane.showMessageDialog(frame, "Appointment updated successfully.");
        frame.dispose();
    }

    public void setVisible(boolean visible) {
        frame.setVisible(visible);
    }
}

class AppointmentsTableModel extends AbstractTableModel {
    protected List<Appointment> appointments = new ArrayList<>();
    String buttonText = "View";

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
                return buttonText;
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
                return "Paid?";
            case 6:
                return "Payment Amount";
            case 7:
                return "Feedback";
            case 8:
                return buttonText + " Appointment";
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
        this.fireTableDataChanged();
    }
}
