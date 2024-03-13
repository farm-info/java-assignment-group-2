package main;

import java.math.BigDecimal;
import java.time.LocalDate;

public class exampleUsage {
    public static void main(String[] args) {
        // Example usage of the AHHASC system
        AHHASCSystem system = new AHHASCSystem();

        system.login("manager1", "password123");
        CentreManager c = (CentreManager) system.createUser("manager2", "password123", User.Role.CENTRE_MANAGER);
        Technician t = (Technician) system.createUser("tech1", "password789", User.Role.TECHNICIAN);
        Customer cu = (Customer) system.addCustomer("customer idk", "", "");

        if (system.getCurrentUser().getRole().equals(User.Role.CENTRE_MANAGER)) {
            system.bookAppointment(cu, t, LocalDate.now(), BigDecimal.valueOf(100));

        } else if (system.getCurrentUser().getRole().equals(User.Role.TECHNICIAN)) {
        }
    }
}
