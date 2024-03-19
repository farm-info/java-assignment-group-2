package main.user_interface;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

import java.time.*;

// unused for now
public class BasicDatePicker {
    JPanel panel;
    private JTextField dayTextField, yearTextField;
    private JComboBox<String> monthComboBox;
    Boolean inputValid = false;

    public BasicDatePicker() {
        // Create Panel
        panel = new JPanel();

        // Create components for the date picker
        dayTextField = new JTextField(2);
        monthComboBox = new JComboBox<>(
                new String[] { "Jan", "Feb", "Mar", "Apl", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" });
        yearTextField = new JTextField(4);

        // Set default value
        yearTextField.setText(String.valueOf(LocalDate.now().getYear()));

        // validation
        dayTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                validateInput(dayTextField);
            }
        });

        panel.add(dayTextField);
        panel.add(monthComboBox);
        panel.add(yearTextField);
    }

    private void validateInput(JTextField textField) {
        String text = textField.getText();

        if (text.isEmpty()) {
            return;
        }

        // check if text matches numbers only
        if (!text.matches("^\\d+$")) {
            setInvalid(textField, "Invalid input! Please enter numbers only.");
            return;
        }

        int value = Integer.parseInt(text);

        // Check if the other fields are empty (for date only)
        int monthNumber = monthComboBox.getSelectedIndex();
        if (yearTextField.getText().isEmpty() || monthNumber == -1) {
            return;
        }
        int yearNumber = Integer.parseInt(yearTextField.getText());

        // Validate based on the JTextField (using if-else statements)
        if (textField == dayTextField) {
            int maxDay = getMonthDayLimit(monthNumber, yearNumber);
            if (value < 1 || value > maxDay) {
                setInvalid(textField, "Invalid day! Enter a value between 1 and " + maxDay);
            } else {
                setValid(textField);
            }
        } else if (textField == yearTextField) {
            if (value < 1) {
                setInvalid(textField, "Invalid year! Enter a value greater than 0");
            } else {
                setValid(textField);
            }
            if (value < 1000 || value > 9999) {
                setInvalid(textField, "Invalid year! Enter a 4-digit year");
            } else {
                setValid(textField);
            }
        }
    }

    // Helper methods for validation and error handling
    private void setInvalid(JTextField field, String errorMessage) {
        setInvalid();
        field.setBackground(Color.RED);
        field.setToolTipText(errorMessage);
    }

    private void setInvalid() {
        inputValid = false;
    }

    private void setValid(JTextField field) {
        inputValid = true;
        field.setBackground(Color.WHITE);
        field.setToolTipText("");
    }

    // Helper method to get maximum day limit based on month and year
    private int getMonthDayLimit(int month, int year) {
        if (month == 2) {
            if (year % 4 == 0) {
                return 29;
            } else {
                return 28;
            }
        } else if (month == 4 || month == 6 || month == 9 || month == 11) {
            return 30;
        } else {
            return 31;
        }
    }

    public void clear() {
        dayTextField.setText("");
        monthComboBox.setSelectedIndex(-1);
        yearTextField.setText("");
    }

    public LocalDate getDate() {
        validateInput(dayTextField);
        validateInput(yearTextField);
        if (!inputValid) {
            return null;
        }

        int day = Integer.parseInt(dayTextField.getText());
        int month = monthComboBox.getSelectedIndex() + 1;
        int year = Integer.parseInt(yearTextField.getText());

        return LocalDate.of(year, month, day);
    }

    public JPanel getPanel() {
        return panel;
    }
}
