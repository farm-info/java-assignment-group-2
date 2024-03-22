package main.user_interface;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import main.system.*;

abstract class TablePanel extends JPanel {
    protected AHHASCSystem system;

    protected JPanel panel, titleButtonPanel;
    protected JTable itemsTable;
    protected int buttonColumnIndex;

    // Creates the UI
    public TablePanel(AHHASCSystem system, String title, int buttonColumnIndex) {
        this.system = system;
        this.buttonColumnIndex = buttonColumnIndex;
        setLayout(new BorderLayout());

        // Title
        JPanel titlePanel = new TitlePanel(title);

        // Title button panel
        titleButtonPanel = new JPanel(new FlowLayout());

        // Refresh table button
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> {
            refreshItemsTable();
        });
        titleButtonPanel.add(refreshButton, BorderLayout.EAST);
        titlePanel.add(titleButtonPanel, BorderLayout.EAST);
        add(titlePanel, BorderLayout.NORTH);

        // Table of items
        itemsTable = new JTable();
        setModel(itemsTable);
        itemsTable.setRowHeight(25);

        Action goToItem = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                int modelRow = Integer.valueOf(e.getActionCommand());
                goToItem(modelRow);
            }
        };

        ButtonColumn buttonColumn = new ButtonColumn(itemsTable, goToItem, buttonColumnIndex);
        buttonColumn.setMnemonic(KeyEvent.VK_D);

        JScrollPane scrollPane = new JScrollPane(itemsTable);
        itemsTable.setFillsViewportHeight(true);
        add(scrollPane, BorderLayout.CENTER);
    }

    // Abstract methods
    abstract public void setModel(JTable itemTable);

    abstract public void goToItem(int modelRow);

    abstract public void refreshItemsTable();
}
