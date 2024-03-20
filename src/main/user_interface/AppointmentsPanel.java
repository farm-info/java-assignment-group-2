package main.user_interface;

import java.util.*;
import java.util.List;
import java.math.BigDecimal;
import java.text.DecimalFormat;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

import main.system.AHHASCSystem;
import main.system.Appointment;

abstract class AppointmentsPanel {
    protected JPanel panel;
    protected AHHASCSystem system;
    protected Map<String, Appointment> assignedAppointments;
    protected AppointmentsTableModel appointmentsTableModel;
    protected JPanel titleButtonPanel;

    public AppointmentsPanel(AHHASCSystem system, String title) {
        this.system = system;

        panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Title
        JPanel titlePanel = new TitlePanel(title).getTitlePanel();

        // Title button panel
        titleButtonPanel = new JPanel(new FlowLayout());

        // Update table button
        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(e -> {
            updateAppointmentsTable();
        });
        titleButtonPanel.add(updateButton, BorderLayout.EAST);
        titlePanel.add(titleButtonPanel, BorderLayout.EAST);
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
    }

    abstract public void updateAppointmentsTable();

    abstract protected void createTechnicianAppointmentWindow(Appointment appointment);

    public JPanel getPanel() {
        return panel;
    }
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
    protected AHHASCSystem system;
    protected Appointment appointment;

    protected JFrame frame;
    protected JPanel panel, nestedPanel;
    protected Boolean paymentStatus;
    protected BigDecimal paymentAmount;
    protected JLabel paymentStatusLabel, paymentAmountLabel;

    public AppointmentWindow(Appointment appointment, AHHASCSystem system) {
        this.appointment = appointment;
        this.system = system;

        createFrameAndPanel();
        createTitlePanel();
        createDetailsPanel();
        createBottomPanels();

        frame.setContentPane(panel);
    }

    abstract protected void createBottomPanels();

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

    public void setVisible(boolean visible) {
        frame.setVisible(visible);
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
        this.fireTableDataChanged();
    }
}
