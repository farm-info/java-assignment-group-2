package main.user_interface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BookAppointment extends JFrame implements ActionListener {
    private JTextField nameField, emailField, phoneField, dateField, feedbackField;

    public BookAppointment() {
        setTitle("Book Appointment");
        setSize(800, 700);
        setLayout(null);

        JLabel titleLabel = new JLabel("Book an Appointment");
        titleLabel.setBounds(300, 90, 300, 30);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel);

        JLabel nameLabel = new JLabel("Customer Name:");
        nameLabel.setBounds(50, 190, 100, 25);
        add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(170, 190, getWidth() - 200, 25);
        add(nameField);

        JLabel emailLabel = new JLabel("Customer Email:");
        emailLabel.setBounds(50, 230, 100, 25);
        add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(170, 230, getWidth() - 200, 25);
        add(emailField);

        JLabel phoneLabel = new JLabel("Customer Phone:");
        phoneLabel.setBounds(50, 270, 100, 25);
        add(phoneLabel);

        phoneField = new JTextField();
        phoneField.setBounds(170, 270, getWidth() - 200, 25);
        add(phoneField);

        JLabel dateLabel = new JLabel("Appointment Date:");
        dateLabel.setBounds(50, 310, 120, 25);
        add(dateLabel);

        dateField = new JTextField();
        dateField.setBounds(170, 310, getWidth() - 200, 25);
        add(dateField);

        JLabel feedbackLabel = new JLabel("Feedback:");
        feedbackLabel.setBounds(50, 350, 80, 25);
        add(feedbackLabel);

        feedbackField = new JTextField();
        feedbackField.setBounds(170, 350, getWidth() - 200, 25);
        add(feedbackField);

        JButton bookButton = new JButton("Book Appointment");
        bookButton.setBounds(300, 390, 200, 25);
        bookButton.addActionListener(this);
        add(bookButton);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String name = nameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String date = dateField.getText();
        String feedback = feedbackField.getText();

        // Check if any field is empty
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || date.isEmpty() || feedback.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all the queries");
            return;
        }

        // Check if phone number is a real phone number
        try {
            long phoneNumber = Long.parseLong(phone);
            // If the number is negative, not a real phone number
            if (phoneNumber < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid Phone Number");
            return;
        }

        // Check if date given is a real date
        if (!date.matches("\\d{2}/\\d{2}/\\d{4}")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid date in dd/mm/yyyy format");
            return;
        }

        // TODO Interact with backend

        // success message
        String message = "Your appointment has been booked successfully!";
        JOptionPane.showMessageDialog(this, message);

        // this to clear the text after submitting
        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        dateField.setText("");
        feedbackField.setText("");
    }
}
