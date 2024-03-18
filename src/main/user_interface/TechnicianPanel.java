package main.user_interface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.*;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

import java.util.*;
import java.util.List;
import java.math.BigDecimal;
import java.text.DecimalFormat;

import main.system.*;

/*
 * technicianPanel: a table of all appointments, uses getAssignedAppointments()
 * - each row is an appointment and contains a button to view the appointment
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
        JPanel titlePanel = new TitlePanel("Your appointments").getTitlePanel();

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
        AppointmentWindow window = new AppointmentWindow(appointment, system);
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
    private JPanel nestedPanel;
    private JPanel panel;
    private Boolean paymentStatus;
    private JLabel paymentStatusLabel;
    private BigDecimal paymentAmount;
    private JLabel paymentAmountLabel;

    public AppointmentWindow(Appointment appointment, AHHASCSystem system) {
        this.appointment = appointment;
        this.system = system;

        createFrameAndPanel();
        createTitlePanel();
        createDetailsPanel();
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
