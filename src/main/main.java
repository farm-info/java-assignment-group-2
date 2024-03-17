package main;

import javax.swing.SwingUtilities;

import main.user_interface.UserInterface;

public class main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new UserInterface();
        });
    }
}
