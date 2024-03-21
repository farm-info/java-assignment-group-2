package main.user_interface;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import main.system.*;

public class UserManagement extends TablePanel {
    private Map<String, User> users;
    private UserTableModel userTableModel;

    public UserManagement(CentreManagerCard centreManagerCard, AHHASCSystem system) {
        super(system, "Users", 2);

        // Back button
        JButton createBackButton = new JButton("Back");
        createBackButton.addActionListener(e -> {
            centreManagerCard.showPanel("centreManager");
        });
        titleButtonPanel.add(createBackButton, 0);
    }

    @Override
    public void setModel(JTable itemsTable) {
        userTableModel = new UserTableModel();
        itemsTable.setModel(userTableModel);
    }

    @Override
    public void updateItemsTable() {
        users = system.getAllUsers();
        userTableModel.setUsers(new ArrayList<>(users.values()));
    }

    /*
     * method for the button column
     * despite the name, you can do whatever you want
     * in this case, it will delete the user
     */
    @Override
    public void goToItem(int modelRow) {
        User user = userTableModel.users.get(modelRow);
        system.removeUser(user.getId());
        updateItemsTable();
    }
}

class UserTableModel extends AbstractTableModel {
    protected List<User> users = new ArrayList<>();

    @Override
    public int getRowCount() {
        return users.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    public Object getValueAt(int row, int column) {
        User user = users.get(row);
        switch (column) {
            case 0:
                return user.getUsername();
            case 1:
                return user.getRole();
            case 2:
                return "Delete";
            default:
                return null;
        }
    }

    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "User Name";
            case 1:
                return "Role";
            case 2:
                return "Delete user";
            default:
                return null;
        }
    }

    public boolean isCellEditable(int row, int column) {
        switch (column) {
            case 2:
                return true;
            default:
                return false;
        }
    }

    public void setUsers(List<User> users) {
        this.users = users;
        this.fireTableDataChanged();
    }
}
