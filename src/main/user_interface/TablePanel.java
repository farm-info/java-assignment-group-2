package main.user_interface;

import java.util.*;
import java.util.List;
import java.math.BigDecimal;
import java.text.DecimalFormat;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

import main.system.*;

abstract class TablePanel {
    protected JPanel panel;
    protected AHHASCSystem system;
    protected JPanel titleButtonPanel;

    public TablePanel(AHHASCSystem system, String title) {
        this.system = system;

        panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Title
        JPanel titlePanel = new TitlePanel(title).getTitlePanel();

        // Title button panel
        titleButtonPanel = new JPanel(new FlowLayout());

        // Update table button
        JButton updateButton = new JButton("Update table");
        updateButton.addActionListener(e -> {
            updateItemsTable();
        });
        titleButtonPanel.add(updateButton, BorderLayout.EAST);
        titlePanel.add(titleButtonPanel, BorderLayout.EAST);
        panel.add(titlePanel, BorderLayout.NORTH);

        // Table of items
        JTable itemsTable = new JTable();
        setModel(itemsTable);

        Action goToItem = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                int modelRow = Integer.valueOf(e.getActionCommand());
                goToItem(modelRow);
            }
        };

        ButtonColumn buttonColumn = new ButtonColumn(itemsTable, goToItem, 8);
        buttonColumn.setMnemonic(KeyEvent.VK_D);

        JScrollPane scrollPane = new JScrollPane(itemsTable);
        itemsTable.setFillsViewportHeight(true);
        panel.add(scrollPane, BorderLayout.CENTER);
    }

    abstract public void setModel(JTable itemTable);

    abstract public void goToItem(int modelRow);

    abstract public void updateItemsTable();

    public JPanel getPanel() {
        return panel;
    }
}