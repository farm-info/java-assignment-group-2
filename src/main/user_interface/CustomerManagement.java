package main.user_interface;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import main.system.*;

public class CustomerManagement extends TablePanel {
    private Map<String, Customer> customers;
    private CustomerTableModel CustomerTableModel;

    public CustomerManagement(CentreManagerCard centreManagerCard, AHHASCSystem system) {
        super(system, "Customers", 4);

        // Back button
        JButton createBackButton = new JButton("Back");
        createBackButton.addActionListener(e -> {
            centreManagerCard.showPanel("centreManager");
        });
        titleButtonPanel.add(createBackButton, 0);

        // Add customer button
        JButton addCustomerButton = new JButton("Add customer");
        addCustomerButton.addActionListener(e -> {
            NewCustomer window = new NewCustomer(this, system);
            window.setVisible(true);
        });
        titleButtonPanel.add(addCustomerButton);
    }

    @Override
    public void setModel(JTable customersTable) {
        CustomerTableModel = new CustomerTableModel();
        customersTable.setModel(CustomerTableModel);
    }

    @Override
    public void goToItem(int modelRow) {
        Customer customer = CustomerTableModel.customers.get(modelRow);
        CustomerWindow window = new CustomerWindow(this, system, customer);
        window.setVisible(true);
    }

    public void updateItemsTable() {
        customers = system.getAllCustomers();
        CustomerTableModel.setCustomers(new ArrayList<>(customers.values()));
    }
}

class CustomerTableModel extends AbstractTableModel {
    protected List<Customer> customers = new ArrayList<>();

    @Override
    public int getRowCount() {
        return customers.size();
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    public Object getValueAt(int row, int column) {
        Customer customer = customers.get(row);
        switch (column) {
            case 0:
                return customer.getId();
            case 1:
                return customer.getName();
            case 2:
                return customer.getContactNumber();
            case 3:
                return customer.getContactEmail();
            case 4:
                return "Edit";
            default:
                return null;
        }
    }

    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "ID";
            case 1:
                return "Name";
            case 2:
                return "Contact number";
            case 3:
                return "Contact email";
            case 4:
                return "Edit customer";
            default:
                return null;
        }
    }

    public boolean isCellEditable(int row, int column) {
        switch (column) {
            case 4:
                return true;
            default:
                return false;
        }
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
        this.fireTableDataChanged();
    }
}

/*
 * A window to view a specific customer
 * This window will display the customer's details
 * and allow the user to edit them
 * there's also a button to delete the customer
 */
class CustomerWindow extends JFrame {
    private CustomerManagement customerManagement;
    private AHHASCSystem system;
    private Customer customer;

    private JPanel panel;

    private JTextField nameField;
    private JTextField contactNumberField;
    private JTextField contactEmailField;

    public CustomerWindow(CustomerManagement customerManagement, AHHASCSystem system, Customer customer) {
        this.customerManagement = customerManagement;
        this.system = system;
        this.customer = customer;

        initFrameAndPanel();
        createTitlePanel();
        createDetailsPanel();
        createBottomPanel();
    }

    private void initFrameAndPanel() {
        setTitle("Edit customer");
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
        JPanel bottomPanel = new JPanel(new FlowLayout());

        // Back button
        JButton backButton = new JButton("Close");
        backButton.addActionListener(e -> {
            dispose();
        });
        bottomPanel.add(backButton, BorderLayout.EAST);

        JButton saveButton = new JButton("Save and Close");
        saveButton.addActionListener(e -> {
            // WONTFIX we should have had a method to update the customer
            customer.setName(nameField.getText());
            customer.setContactNumber(contactNumberField.getText());
            customer.setContactEmail(contactEmailField.getText());
            system.saveData();

            customerManagement.updateItemsTable();
            dispose();
        });
        bottomPanel.add(saveButton);

        JButton deleteButton = new JButton("Delete customer");
        deleteButton.addActionListener(e -> {
            createDeleteComfirmationWindow();
        });
        bottomPanel.add(deleteButton);

        panel.add(bottomPanel, BorderLayout.SOUTH);
    }

    private void createDeleteComfirmationWindow() {
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
            customerManagement.updateItemsTable();
            dispose();
        });
        confirmationPanel.add(confirmButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> confirmationFrame.dispose());
        confirmationPanel.add(cancelButton);

        confirmationFrame.setVisible(true);
    }
}

class NewCustomer extends JFrame {
    public NewCustomer(CustomerManagement customerManagement_temp, AHHASCSystem system) {
        // TODO
    }
}
