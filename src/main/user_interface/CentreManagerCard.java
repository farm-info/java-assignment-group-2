package main.user_interface;

import java.awt.CardLayout;
import main.system.AHHASCSystem;

public class CentreManagerCard extends javax.swing.JPanel {
    private AHHASCSystem system;
    private UserInterface userInterface;
    private CardLayout cardLayout;
    private CentreManagerAppointmentPanel appointmentPanel;

    public CentreManagerCard(UserInterface userInterface, AHHASCSystem system) {
        this.system = system;
        this.userInterface = userInterface;
        this.cardLayout = new CardLayout();
        setLayout(cardLayout);

        // TODO add the other two panels
        CentreManagerPanel centreManagerPanel = new CentreManagerPanel(this, userInterface, system);
        appointmentPanel = new CentreManagerAppointmentPanel(this, system);
        this.add(appointmentPanel.getPanel(), "appointments");
        this.add(centreManagerPanel, "centreManager");
    }

    public void showPanel(String layoutName) {
        cardLayout.show(this, layoutName);
        if (layoutName.equals("appointments")) {
            appointmentPanel.updateItemsTable();
        }
    }

    public void updateAppointmentsTable() {
        appointmentPanel.updateItemsTable();
    }
}
