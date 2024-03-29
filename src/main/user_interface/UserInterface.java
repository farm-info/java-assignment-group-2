package main.user_interface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import main.data_access.ItemNotFoundException;
import main.system.*;

public class UserInterface {
    private JFrame frame;
    private CardLayout cardLayout;

    private JPanel centerPanel;
    private LoginPanel loginPanel;
    private RegisterPanel registerPanel;
    private TechnicianPanel technicianPanel;
    private CentreManagerCard centreManagerCard;

    private AHHASCSystem system;

    public UserInterface() {
        initializeSystem();
        createAndShowUI();
    }

    private void initializeSystem() {
        try {
            system = new AHHASCSystem();
        } catch (ItemNotFoundException | IOException e) {
            JOptionPane.showMessageDialog(
                    frame,
                    "Error initializing system. Data may be corrupted.\n" + e,
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void createAndShowUI() {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException
                | IllegalAccessException e) {
            e.printStackTrace();
        }

        cardLayout = new CardLayout();
        frame = new JFrame("AHHASC System");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                system.saveData();
                System.exit(0);
            }
        });
        frame.setSize(1100, 500);

        centerPanel = new JPanel();
        centerPanel.setLayout(cardLayout);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        loginPanel = new LoginPanel(this, system, frame);
        registerPanel = new RegisterPanel(this, system, frame);
        technicianPanel = new TechnicianPanel(this, system);
        centreManagerCard = new CentreManagerCard(this, system);
        centerPanel.add(loginPanel, "login");
        centerPanel.add(registerPanel, "register");
        centerPanel.add(technicianPanel, "technician");
        centerPanel.add(centreManagerCard, "centreManager");

        frame.setContentPane(centerPanel);

        showPanel("login");
    }

    public void showPanel(String panelName) {
        if (panelName.equals("technician")) {
            technicianPanel.refreshItemsTable();
        }
        if (panelName.equals("centreManager")) {
            centreManagerCard.showPanel("centreManager");
            centreManagerCard.refreshAppointmentsTable();
        }
        cardLayout.show(centerPanel, panelName);
        // frame.pack();
        frame.setVisible(true);
    }
}
