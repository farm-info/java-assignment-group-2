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

abstract class TablePanel extends JPanel {
    protected JPanel panel;
    protected AHHASCSystem system;
    protected JPanel titleButtonPanel;
    protected int buttonColumnIndex;

    public TablePanel(AHHASCSystem system, String title, int buttonColumnIndex) {
        this.system = system;
        this.buttonColumnIndex = buttonColumnIndex;

        setLayout(new BorderLayout());
        add(new JPanel(), BorderLayout.WEST);
        add(new JPanel(), BorderLayout.EAST);
        add(new JPanel(), BorderLayout.SOUTH);
        add(new JPanel(), BorderLayout.NORTH);

        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);

        // Title
        JPanel titlePanel = new TitlePanel(title);

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

        ButtonColumn buttonColumn = new ButtonColumn(itemsTable, goToItem, buttonColumnIndex);
        buttonColumn.setMnemonic(KeyEvent.VK_D);

        JScrollPane scrollPane = new JScrollPane(itemsTable);
        itemsTable.setFillsViewportHeight(true);
        panel.add(scrollPane, BorderLayout.CENTER);
    }

    abstract public void setModel(JTable itemTable);

    abstract public void goToItem(int modelRow);

    abstract public void updateItemsTable();
}
