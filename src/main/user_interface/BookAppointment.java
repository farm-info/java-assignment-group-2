package main.user_interface;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;

import java.io.IOException;

import java.math.BigDecimal;
import java.text.*;
import java.time.*;
import java.time.format.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.List;

import main.data_access.ItemNotFoundException;
import main.system.*;

public class BookAppointment extends JFrame implements ActionListener {
    protected DecimalFormat decimalFormat = new DecimalFormat("#0.00");

    private JComboBox<String> customerComboBox, technicianComboBox;
    private JFormattedTextField paymentAmountField, datePicker;

    private AHHASCSystem system;
    private Map<String, Customer> customers;
    private List<Technician> technicians;

    public BookAppointment(AHHASCSystem system, Customer customer) {
        // TODO constructor where the combo box is pre-filled with the customer's name
        this(system);
    }

    public BookAppointment(AHHASCSystem system) {
        this.system = system;

        setTitle("Book Appointment");
        setSize(800, 700);
        setLayout(null);

        JLabel titleLabel = new JLabel("Book an Appointment");
        titleLabel.setBounds(300, 90, 300, 30);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel);

        JLabel nameLabel = new JLabel("Customer:");
        nameLabel.setBounds(50, 190, 150, 25);
        add(nameLabel);

        customers = system.getAllCustomers();
        String[] customerNames = new String[customers.size()];
        customerComboBox = new JComboBox<>(customers.keySet().toArray(customerNames));
        customerComboBox.setBounds(170, 190, getWidth() - 200, 25);
        add(customerComboBox);

        JLabel technicianLabel = new JLabel("Technician:");
        technicianLabel.setBounds(50, 230, 150, 25);
        add(technicianLabel);

        Map<String, User> users = system.getUsers();
        technicians = users.values().stream().filter(user -> user.getRole() == User.Role.TECHNICIAN)
                .map(user -> (Technician) user).collect(Collectors.toList());
        String[] technicianNames = new String[technicians.size()];
        technicianComboBox = new JComboBox<>(
                technicians.stream().map(Technician::getUsername).collect(Collectors.toList())
                        .toArray(technicianNames));
        technicianComboBox.setBounds(170, 230, getWidth() - 200, 25);
        add(technicianComboBox);

        JLabel nameLabel2 = new JLabel("Payment amount:");
        nameLabel2.setBounds(50, 270, 150, 25);
        add(nameLabel2);

        // idk what do put for the default payment amount
        paymentAmountField = new JFormattedTextField(10);
        DefaultFormatter fmt = new NumberFormatter(decimalFormat);
        fmt.setValueClass(paymentAmountField.getValue().getClass());
        DefaultFormatterFactory fmtFactory = new DefaultFormatterFactory(fmt, fmt, fmt);
        paymentAmountField.setFormatterFactory(fmtFactory);
        paymentAmountField.setBounds(170, 270, getWidth() - 200, 25);
        add(paymentAmountField);

        JLabel dateLabel = new JLabel("Appointment Date:");
        dateLabel.setBounds(50, 310, 150, 25);
        add(dateLabel);

        JLabel dateLabel2 = new JLabel("(dd/mm/yyyy)");
        dateLabel2.setBounds(50, 330, 150, 25);
        add(dateLabel2);

        // FIXME this uses the old java.util.Date class, which is deprecated
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        datePicker = new JFormattedTextField(sdf);
        datePicker.setBounds(170, 310, getWidth() - 200, 40);
        datePicker.setValue(new Date());
        add(datePicker);

        JButton bookButton = new JButton("Book Appointment");
        bookButton.setBounds(300, 390, 200, 25);
        bookButton.addActionListener(this);
        add(bookButton);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String paymentAmount = paymentAmountField.getText();
        String dateString = datePicker.getText();

        // Validation
        // Check if any field is empty
        if (customerComboBox.getSelectedIndex() == -1
                || technicianComboBox.getSelectedIndex() == -1
                || paymentAmount.isEmpty()
                || dateString == null) {
            JOptionPane.showMessageDialog(this, "Please fill in all the queries");
            return;
        }

        LocalDate date;
        try {
            date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Invalid date input! Please enter a valid date.");
            return;
        }

        Customer customer = customers.get(customerComboBox.getSelectedItem());
        Technician technicianId = technicians.get(technicianComboBox.getSelectedIndex());

        // Interact with backend
        system.addAppointment(customer, technicianId, date, new BigDecimal(paymentAmount));
        system.saveData();

        // Success message
        String message = "Your appointment has been booked successfully!";
        JOptionPane.showMessageDialog(this, message);

        // Clear the text after submitting
        customerComboBox.setSelectedIndex(0);
        technicianComboBox.setSelectedIndex(0);
        paymentAmountField.setText("");
        datePicker.setText("");
    }

    public static void main(String[] args) throws IOException, ItemNotFoundException {
        new BookAppointment(new AHHASCSystem());
    }
}
