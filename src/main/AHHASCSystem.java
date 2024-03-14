package main;

import java.math.BigDecimal;
import java.util.*;

import java.time.LocalDate;

abstract class BaseItem {
    protected String Id;

    public BaseItem() {
        this.Id = UUID.randomUUID().toString();
    }

    public String getId() {
        return Id;
    }

    protected void setId(String id) {
        this.Id = id;
    }
}

abstract class User extends BaseItem {
    private String password;
    private Role role;

    public enum Role {
        CENTRE_MANAGER,
        TECHNICIAN,
    }

    public User(String username, String password, Role role) {
        setId(username);
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return getId();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public String toString() {
        return getId() + "," + password + "," + role;
    }
}

class CentreManager extends User {
    public CentreManager(String username, String password) {
        super(username, password, Role.CENTRE_MANAGER);
    }
}

class Technician extends User {
    public Technician(String username, String password) {
        super(username, password, Role.TECHNICIAN);
    }
}

class Customer extends BaseItem {
    private String name;
    private String contact_number;
    private String contact_email;

    public Customer(String name, String contactDetails, String contact_email) {
        this.name = name;
        this.contact_number = contactDetails;
        this.contact_email = contact_email;
    }

    public String getName() {
        return name;
    }

    public String getContact_number() {
        return contact_number;
    }

    public String getContact_email() {
        return contact_email;
    }

    public String toString() {
        return getId() + "," + name + "," + contact_number + "," + contact_email;
    }
}

class Appointment extends BaseItem {
    // TODO migrate to storing IDs and utilizing hashmaps
    private Customer customer;
    private Technician technician;
    private LocalDate creationDate;
    private LocalDate appointmentDate;
    private BigDecimal paymentAmount;
    private Boolean paymentStatus;
    private String feedback;

    public Appointment(Customer customer, Technician technician, LocalDate appointmentDate, BigDecimal paymentAmount) {
        this.customer = customer;
        this.technician = technician;
        this.creationDate = LocalDate.now();
        this.appointmentDate = appointmentDate;
        this.paymentAmount = paymentAmount;
        this.paymentStatus = false;
    }

    // currently unused
    @Override
    public Appointment clone() {
        try {
            return (Appointment) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    // getter and setters
    public Customer getCustomer() {
        return customer;
    }

    public Technician getTechnician() {
        return technician;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public boolean getPaymentStatus() {
        return paymentStatus;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public String getFeedback() {
        return feedback;
    }

    void setCustomer(Customer customer) {
        this.customer = customer;
    }

    void setTechnician(Technician technician) {
        this.technician = technician;
    }

    void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    void setPaymentStatus(boolean paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    void setPaymentStatus(Boolean paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String toString() {
        return getId() + "," + customer.getId() + "," + technician.getId() + "," + creationDate + "," + paymentStatus;
    }
}

public class AHHASCSystem {
    private Map<String, User> users;
    private Map<String, Customer> customers;
    private Map<String, Appointment> appointments;
    private String userFilePath = "users.csv";
    private String customerFilePath = "customers.csv";
    private String appointmentFilePath = "appointments.csv";
    private User currentUser;

    public AHHASCSystem() {
        users = new HashMap<>();
        customers = new HashMap<>();
        appointments = new HashMap<>();
        DataAccess.readUserDataFromCSV(users, userFilePath, User.class);
        DataAccess.readCustomerDataFromCSV(customers, customerFilePath, Customer.class);
        DataAccess.readAppointmentDataFromCSV(appointments, appointmentFilePath, users, customers);
    }

    // permission checks
    private boolean hasCurrentUserPermission(User.Role requiredRole) {
        return currentUser != null && currentUser.getRole() == requiredRole;
    }

    // account management
    public Boolean login(String username, String password) {
        if (users.containsKey(username)) {
            User user = users.get(username);
            if (user.getPassword().equals(password)) {
                currentUser = user;
                System.out.println("Login successful.");
                return true;
            }
        } else {
            System.out.println("Username not found.");
        }
        return false;
    }

    public User getCurrentUser() {
        return currentUser;
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
    public Map<String, Appointment> checkAssignedAppointments(Technician technician) {
        if (!hasCurrentUserPermission(User.Role.TECHNICIAN)) {
            System.out.println("Permission denied: Check assigned appointments");
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

    public Boolean collectPayment(Appointment appointment, BigDecimal paymentAmount, Boolean paymentStatus) {
        if (!hasCurrentUserPermission(User.Role.TECHNICIAN)) {
            System.out.println("Permission denied: Collect payment");
            return false;
        }

        appointment.setPaymentAmount(paymentAmount);
        appointment.setPaymentStatus(paymentStatus);
        System.out.println("Payment collected for appointment.");
        return true;
    }

    public void enterFeedback(Appointment appointment, String feedback) {
        if (!(hasCurrentUserPermission(User.Role.TECHNICIAN) || hasCurrentUserPermission(User.Role.CENTRE_MANAGER))) {
            System.out.println("Permission denied: Enter feedback");
        }

        appointment.setFeedback(feedback);
        System.out.println("Feedback entered for appointment.");
    }
}
