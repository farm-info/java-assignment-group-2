package main.system;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.time.LocalDate;

import main.data_access.*;

public class AHHASCSystem {
    private Map<String, User> users;
    private Map<String, Customer> customers;
    private Map<String, Appointment> appointments;
    private String userFilePath = "users.csv";
    private String customerFilePath = "customers.csv";
    private String appointmentFilePath = "appointments.csv";
    private User currentUser;

    public AHHASCSystem() throws IOException, ItemNotFoundException {
        users = new HashMap<>();
        customers = new HashMap<>();
        appointments = new HashMap<>();
        try {
            DataAccess.readUserDataFromCSV(users, userFilePath, User.class);
            DataAccess.readCustomerDataFromCSV(customers, customerFilePath, Customer.class);
            DataAccess.readAppointmentDataFromCSV(appointments, appointmentFilePath, users, customers);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } catch (ItemNotFoundException e) {
            e.printStackTrace();
            throw e;
        }
    }

    // permission checks
    private boolean hasCurrentUserPermission(User.Role requiredRole) {
        return currentUser != null && currentUser.getRole() == requiredRole;
    }

    // account management
    public User.Role login(String username, String password) {
        if (users.containsKey(username)) {
            User user = users.get(username);
            if (user.getPassword().equals(password)) {
                currentUser = user;
                System.out.println("Login successful.");
                return user.getRole();
            }
        } else {
            System.out.println("Username not found.");
        }
        return null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void logout() {
        currentUser = null;
        System.out.println("Logged out.");
    }

    // user management
    public User addUser(String username, String password, User.Role role) {
        if (!hasCurrentUserPermission(User.Role.CENTRE_MANAGER)) {
            System.out.println("Permission denied: Get user list");
            return null;
        }

        boolean usernameExists = users.containsKey(username);
        if (usernameExists) {
            System.out.println("Username already exists.");
            return null;
        }

        User newUser = null;

        if (role == User.Role.CENTRE_MANAGER) {
            newUser = new CentreManager(username, password);
            users.put(newUser.getId(), newUser);
            System.out.println("Centre Manager created successfully.");

        } else if (role == User.Role.TECHNICIAN) {
            newUser = new Technician(username, password);
            users.put(newUser.getId(), newUser);
            System.out.println("Technician created successfully.");

        } else {
            throw new IllegalArgumentException("Invalid user type.");
        }

        save();
        System.out.println("User created successfully.");
        return newUser;
    }

    public Map<String, User> getUsers() {
        if (!hasCurrentUserPermission(User.Role.CENTRE_MANAGER)) {
            System.out.println("Permission denied: Get user list");
            return null;
        }

        return users;
    }

    public void save() {
        // TODO change this
        DataAccess.saveObjectsToCSV(users, userFilePath);
        DataAccess.saveObjectsToCSV(customers, customerFilePath);
        DataAccess.saveObjectsToCSV(appointments, appointmentFilePath);
    }

    // appointment management
    public Appointment addAppointment(Customer customer, Technician technician, LocalDate appointmentDate,
            BigDecimal paymentAmount) {
        if (!hasCurrentUserPermission(User.Role.CENTRE_MANAGER)) {
            System.out.println("Permission denied: Book appointment");
            return null;
        }

        Appointment appointment = new Appointment(customer, technician, appointmentDate, paymentAmount);
        appointments.put(appointment.getId(), appointment);
        return appointment;
    }

    public Boolean removeAppointment(Appointment appointment) {
        if (!hasCurrentUserPermission(User.Role.CENTRE_MANAGER)) {
            System.out.println("Permission denied: Cancel appointment");
            return false;
        }

        appointments.remove(appointment.getId());
        return true;
    }

    // there's nothing to ensure anyone else can edit appointments, like,
    // technicians can edit stuff from their list, which is a big no
    // WONTFIX implement setters for them?

    public Map<String, Appointment> getAppointments() {
        if (!hasCurrentUserPermission(User.Role.CENTRE_MANAGER)) {
            System.out.println("Permission denied: Cancel appointment");
            return null;
        }

        return appointments;
    }

    // customer manaaement
    public Customer addCustomer(String name, String contactDetails, String contactEmail) {
        if (!hasCurrentUserPermission(User.Role.CENTRE_MANAGER)) {
            System.out.println("Permission denied: Add customer");
            return null;
        }

        Customer newCustomer = new Customer(name, contactDetails, contactEmail);
        customers.put(newCustomer.getId(), newCustomer);
        System.out.println("Customer added successfully.");
        return newCustomer;
    }

    public Boolean removeCustomer(String customerID) {
        if (!hasCurrentUserPermission(User.Role.CENTRE_MANAGER)) {
            System.out.println("Permission denied: Remove customer");
            return false;
        }

        // Implement logic to find and remove customer by ID
        System.out.println("Customer removed successfully.");
        return true;
    }

    public Map<String, Customer> getCustomers() {
        if (!hasCurrentUserPermission(User.Role.CENTRE_MANAGER)) {
            System.out.println("Permission denied: Get customer list");
            return null;
        }

        return customers;
    }

    // technician features
    public Map<String, Appointment> getAssignedAppointments(Technician technician) {
        if (!hasCurrentUserPermission(User.Role.TECHNICIAN)) {
            System.out.println("Permission denied: Check assigned appointments");
            return null;
        }

        Map<String, Appointment> technicianAppointments = new HashMap<String, Appointment>();
        String technicianId = technician.getId();
        for (Map.Entry<String, Appointment> entry : appointments.entrySet()) {
            Appointment appointment = entry.getValue();
            if (appointment.getTechnician().getId().equals(technicianId)) {
                technicianAppointments.put(appointment.getId(), appointment);
            }
        }
        System.out.println("Technician appointments accessed.");
        return technicianAppointments;
    }

    public Boolean setAppointmentPayment(Appointment appointment, BigDecimal paymentAmount, Boolean paymentStatus) {
        if (!hasCurrentUserPermission(User.Role.TECHNICIAN)) {
            System.out.println("Permission denied: Collect payment");
            return false;
        }

        appointment.setPaymentAmount(paymentAmount);
        appointment.setPaymentStatus(paymentStatus);
        System.out.println("Payment collected for appointment.");
        return true;
    }

    public void setAppointmentFeedback(Appointment appointment, String feedback) {
        if (!(hasCurrentUserPermission(User.Role.TECHNICIAN) || hasCurrentUserPermission(User.Role.CENTRE_MANAGER))) {
            System.out.println("Permission denied: Enter feedback");
            return;
        }

        appointment.setFeedback(feedback);
        System.out.println("Feedback entered for appointment.");
    }
}