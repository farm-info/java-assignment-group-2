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
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
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
        frame.setSize(400, 300);

        centerPanel = new JPanel();
        centerPanel.setLayout(cardLayout);

        loginPanel = new LoginPanel(this, system, frame);
        registerPanel = new RegisterPanel(this, system, frame);
        technicianPanel = new TechnicianPanel(this, system);
        centerPanel.add(loginPanel.getPanel(), "login");
        centerPanel.add(registerPanel.getPanel(), "register");
        centerPanel.add(technicianPanel.getPanel(), "technician");

        frame.setContentPane(centerPanel);

        showPanel("login");
    }

    public void showPanel(String panelName) {
        if (panelName.equals("technician")) {
            technicianPanel.updateAppointmentsTable();
        }
        cardLayout.show(centerPanel, panelName);
        frame.pack();
        frame.setVisible(true);
    }
}
