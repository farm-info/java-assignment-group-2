package main.user_interface;

import javax.swing.*;
import java.awt.*;

import main.system.*;

/*
 * A window to view a specific customer
 * This window will display the customer's details
 * and allow the user to edit them
 * there's also a button to delete the customer
 */
abstract class CustomerWindow extends JFrame {
    protected CustomerManagement customerManagement;
    protected AHHASCSystem system;
    protected Customer customer;

    protected JPanel panel;
    protected JPanel bottomPanel;

    protected JTextField nameField;
    protected JTextField contactNumberField;
    protected JTextField contactEmailField;

    public CustomerWindow(CustomerManagement customerManagement, AHHASCSystem system, Customer customer, String title) {
        this.customerManagement = customerManagement;
        this.system = system;
        this.customer = customer;

        initFrameAndPanel(title);
        createTitlePanel();
        createDetailsPanel();
        createBottomPanel();
    }

    private void initFrameAndPanel(String title) {
        setTitle(title);
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setContentPane(panel);
    }

    private void createTitlePanel() {
        JPanel titlePanel = new TitlePanel(getTitle());
        panel.add(titlePanel, BorderLayout.NORTH);
    }

    private void createDetailsPanel() {
        JPanel detailsPanel = new JPanel(new GridLayout(0, 2));

        detailsPanel.add(new JLabel("ID:"));
        detailsPanel.add(new JLabel(customer.getId()));

        detailsPanel.add(new JLabel("Name:"));
        nameField = new JTextField(customer.getName());
        detailsPanel.add(nameField);

        detailsPanel.add(new JLabel("Contact number:"));
        contactNumberField = new JTextField(customer.getContactNumber());
        detailsPanel.add(contactNumberField);

        detailsPanel.add(new JLabel("Contact email:"));
        contactEmailField = new JTextField(customer.getContactEmail());
        detailsPanel.add(contactEmailField);

        panel.add(detailsPanel, BorderLayout.CENTER);
    }

    private void createBottomPanel() {
        bottomPanel = new JPanel(new FlowLayout());

        // Back button
        JButton backButton = new JButton("Close");
        backButton.addActionListener(e -> {
            dispose();
        });
        bottomPanel.add(backButton, BorderLayout.EAST);

        JButton saveButton = new JButton("Save and Close");
        saveButton.addActionListener(e -> {
            try {
                validateFields();
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
                return;
            }

            // WONTFIX we should have had a method to update the customer
            customer.setName(nameField.getText());
            customer.setContactNumber(contactNumberField.getText());
            customer.setContactEmail(contactEmailField.getText());
            saveCustomer();

            system.saveData();
            customerManagement.refreshItemsTable();
            dispose();
        });
        bottomPanel.add(saveButton);

        panel.add(bottomPanel, BorderLayout.SOUTH);
    }

    abstract void saveCustomer();

    private void validateFields() {
        if (nameField.getText().isEmpty()
                || (contactNumberField.getText().isEmpty() && contactEmailField.getText().isEmpty())) {
            throw new IllegalArgumentException("Please fill in customer name and at least one contact method");
        }
    }
}

class EditCustomerWindow extends CustomerWindow {
    public EditCustomerWindow(CustomerManagement customerManagement, AHHASCSystem system, Customer customer) {
        super(customerManagement, system, customer, "Edit customer");
        addDeleteButton();
    }

    // There's nothing to do here
    @Override
    void saveCustomer() {
    }

    private void addDeleteButton() {
        JButton deleteButton = new JButton("Delete customer");
        deleteButton.addActionListener(e -> {
            JFrame window = createDeleteComfirmationWindow();
            window.setVisible(true);
        });
        bottomPanel.add(deleteButton);
    }

    private JFrame createDeleteComfirmationWindow() {
        JFrame confirmationFrame = new JFrame("Delete customer?");
        confirmationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        confirmationFrame.setSize(300, 100);
        JPanel confirmationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        confirmationFrame.setContentPane(confirmationPanel);

        JLabel confirmationLabel = new JLabel("Are you sure you want to delete this customer?");
        confirmationPanel.add(confirmationLabel);

        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(e -> {
            system.removeCustomer(customer.getId());
            customerManagement.refreshItemsTable();
            confirmationFrame.dispose();
            dispose();
        });
        confirmationPanel.add(confirmButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> confirmationFrame.dispose());
        confirmationPanel.add(cancelButton);

        return confirmationFrame;
    }
}

class AddCustomerWindow extends CustomerWindow {
    public AddCustomerWindow(CustomerManagement customerManagement, AHHASCSystem system) {
        super(customerManagement, system, new Customer("", "", ""), "Add customer");
    }

    @Override
    void saveCustomer() {
        system.addCustomer(customer);
    }
}
