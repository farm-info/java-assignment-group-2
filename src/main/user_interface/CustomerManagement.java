package main.user_interface;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.event.*;

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
            AddCustomerWindow window = new AddCustomerWindow(this, system);
            window.setVisible(true);
        });
        titleButtonPanel.add(addCustomerButton);

        // Book appointment button for each customer
        Action bookAppointmentForCustomer = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                int modelRow = Integer.valueOf(e.getActionCommand());
                Customer customer = CustomerTableModel.customers.get(modelRow);
                // TODO windows should not set themselves visible
                BookAppointment window = new BookAppointment(system, customer);
                window.setVisible(true);
            }
        };
        ButtonColumn buttonColumn = new ButtonColumn(itemsTable, bookAppointmentForCustomer, 5);
        buttonColumn.setMnemonic(KeyEvent.VK_D);
    }

    @Override
    public void setModel(JTable customersTable) {
        CustomerTableModel = new CustomerTableModel();
        customersTable.setModel(CustomerTableModel);
    }

    @Override
    public void goToItem(int modelRow) {
        Customer customer = CustomerTableModel.customers.get(modelRow);
        EditCustomerWindow window = new EditCustomerWindow(this, system, customer);
        window.setVisible(true);
    }

    public void refreshItemsTable() {
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
        return 6;
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
            case 5:
                return "Book";
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
            case 5:
                return "Book appointment";
            default:
                return null;
        }
    }

    public boolean isCellEditable(int row, int column) {
        switch (column) {
            case 4:
                return true;
            case 5:
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
