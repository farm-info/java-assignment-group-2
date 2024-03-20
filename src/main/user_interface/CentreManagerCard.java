package main.user_interface;

import java.awt.CardLayout;
import main.system.AHHASCSystem;

public class CentreManagerCard extends javax.swing.JPanel {
    private AHHASCSystem system;
    private UserInterface userInterface;
    private CardLayout cardLayout;
    private CentreManagerAppointmentPanel appointmentPanel;
    private CustomerManagement customerManagement;
    private UserManagement userManagement;

    public CentreManagerCard(UserInterface userInterface, AHHASCSystem system) {
        this.system = system;
        this.userInterface = userInterface;
        this.cardLayout = new CardLayout();
        setLayout(cardLayout);

        // TODO add the other two panels
        CentreManagerPanel centreManagerPanel = new CentreManagerPanel(this, userInterface, system);
        appointmentPanel = new CentreManagerAppointmentPanel(this, system);
        customerManagement = new CustomerManagement(this, system);
        userManagement = new UserManagement(system);
        // WONTFIX this is inconsistent
        this.add(centreManagerPanel, "centreManager");
        this.add(customerManagement, "customerManagement");
        this.add(userManagement.getPanel(), "userManagement");
        this.add(appointmentPanel.getPanel(), "appointments");
    }

    public void showPanel(String layoutName) {
        cardLayout.show(this, layoutName);
        if (layoutName.equals("appointments")) {
            appointmentPanel.updateItemsTable();
        } else if (layoutName.equals("userManagement")) {
            userManagement.updateItemsTable();
        } else if (layoutName.equals("customerManagement")) {
            // TODO
            // customerManagement.updateItemsTable();
        }
    }

    public void updateAppointmentsTable() {
        appointmentPanel.updateItemsTable();
    }
}
