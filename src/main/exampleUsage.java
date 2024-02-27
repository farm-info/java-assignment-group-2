package main;

import main.AHHASCSystem;

public class exampleUsage {
    public static void main(String[] args) {
        // Example usage of the AHHASC system
        AHHASCSystem system = new AHHASCSystem();
        CentreManager manager = new CentreManager("manager1", "password123");
        system.users.add(manager);

        Customer customer = new Customer("John Doe", "123-456-7890", "aaa@mail.com");
        customer.bookAppointment("T001", "2024-01-30");

        Technician technician = new Technician("tech1", "password789", "Appliance Repair");
        technician.checkAppointments();
        technician.collectPayment("APPT123");
        technician.provideFeedback("APPT123", "Great service!");
    }
}
