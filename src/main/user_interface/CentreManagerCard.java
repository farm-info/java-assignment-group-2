package main.user_interface;

import javax.swing.*;

import java.awt.CardLayout;
import main.system.AHHASCSystem;

public class CentreManagerCard extends JPanel {
    private CardLayout cardLayout;
    private CentreManagerAppointmentPanel appointmentPanel;
    private CustomerManagement customerManagement;
    private UserManagement userManagement;

    public CentreManagerCard(UserInterface userInterface, AHHASCSystem system) {
        this.cardLayout = new CardLayout();
        setLayout(cardLayout);

        CentreManagerPanel centreManagerPanel = new CentreManagerPanel(this, userInterface, system);
        appointmentPanel = new CentreManagerAppointmentPanel(this, system);
        customerManagement = new CustomerManagement(this, system);
        userManagement = new UserManagement(this, system);
        this.add(centreManagerPanel, "centreManager");
        this.add(customerManagement, "customerManagement");
        this.add(userManagement, "userManagement");
        this.add(appointmentPanel, "appointments");
    }

    public void showPanel(String layoutName) {
        cardLayout.show(this, layoutName);
        if (layoutName.equals("appointments")) {
            appointmentPanel.updateItemsTable();
        } else if (layoutName.equals("userManagement")) {
            userManagement.updateItemsTable();
        } else if (layoutName.equals("customerManagement")) {
            customerManagement.updateItemsTable();
        }
    }

    public void updateAppointmentsTable() {
        appointmentPanel.updateItemsTable();
    }
}
