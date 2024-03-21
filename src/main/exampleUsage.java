package main;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

import main.data_access.ItemNotFoundException;
import main.system.*;

public class exampleUsage {
    public static void main(String[] args) throws UserNameAlreadyExistsException {
        // Example usage of the AHHASC system
        AHHASCSystem system;
        try {
            system = new AHHASCSystem();
        } catch (IOException | ItemNotFoundException e) {
            e.printStackTrace();
            return;
        }

        system.login("manager1", "password123");
        CentreManager c = (CentreManager) system.addUser("manager2", "password123", User.Role.CENTRE_MANAGER);
        Technician t = (Technician) system.addUser(
                "tech1",
                "password789",
                User.Role.TECHNICIAN);
        Customer cu = (Customer) system.addCustomer("customer idk", "", "");

        if (system.getCurrentUser().getRole().equals(User.Role.CENTRE_MANAGER)) {
            // this is broken
            // system.addAppointment(cu, t, LocalDate.now(), BigDecimal.valueOf(100));
            system.getAllAppointments().forEach((k, v) -> {
                System.out.println(v);
            });

        } else if (system.getCurrentUser().getRole().equals(User.Role.TECHNICIAN)) {
            // idk
        }
        system.saveData();
    }
}
