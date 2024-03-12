package main;

import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import java.time.LocalDate;

abstract class User {
    private String userId;
    private String username;
    private String password;
    private Role role;

    public enum Role {
        CENTRE_MANAGER,
        TECHNICIAN,
    }

    public User(String username, String password, Role role) {
        this.userId = UUID.randomUUID().toString();
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
        return userId + "," + username + "," + password + "," + role;
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

    public void checkAppointments() {
        // Display technician's upcoming appointments
        // Read and display appointments from the file...
    }

    public void collectPayment(String appointmentId) {
        // Collect payment for an appointment
        // Update payment status in the file...
    }

    public void provideFeedback(String appointmentId, String feedback) {
        // Provide feedback for an appointment
        // Save feedback to the file...
    }

    @Override
    public String toString() {
        return getUserId() + "," +
                getUsername() + "," +
                getPassword() + "," +
                getRole() + ",";
    }
}

class Customer {
    private String customerID;
    private String name;
    private String contact_number;
    private String contact_email;

    public Customer(String name, String contactDetails, String contact_email) {
        this.customerID = UUID.randomUUID().toString();
        this.name = name;
        this.contact_number = contactDetails;
        this.contact_email = contact_email;
    }

    public String getCustomerID() {
        return customerID;
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
        return customerID + "," + name + "," + contact_number + "," + contact_email;
    }
}

class Appointment {
    private String appointmentId;
    private Customer customer;
    private Technician technician;
    private LocalDate creationDate;
    private LocalDate appointmentDate;
    private BigDecimal paymentAmount;
    private Boolean paymentStatus;
    private String feedback;

    public Appointment(Customer customer, Technician technician, LocalDate appointmentDate, BigDecimal paymentAmount) {
        this.appointmentId = UUID.randomUUID().toString();
        this.customer = customer;
        this.technician = technician;
        this.creationDate = LocalDate.now();
        this.appointmentDate = appointmentDate;
        this.paymentAmount = paymentAmount;
        this.paymentStatus = false;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

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
        return appointmentId + "," + customer + "," + technician + "," + creationDate + "," + paymentStatus;
    }
}

class AppointmentManager {
    private List<Appointment> appointments;

    public AppointmentManager() {
    }

    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
    }

    public void removeAppointment(Appointment appointment) {
        appointments.remove(appointment);
    }

    public void updateAppointment(String appointmentID, String customerId, String technicianId, Boolean paymentStatus) {
        // Update appointment data
    }

    public void displayAppointments() {
        // Display appointments from the file...
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }
}

class DataAccess {
    static public <T> void saveObjectsToCSV(List<T> objects, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            for (T object : objects) {
                writer.write(object.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static public <T> void readObjectsFromCSV(List<T> objects, String filePath, Class<T> clazz) {
        try (BufferedReader writer = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = writer.readLine()) != null) {
                T object = parseObjectFromLine(line, clazz);
                objects.add(object);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // TODO test
    private static <T> T parseObjectFromLine(String line, Class<T> objectType) {
        String[] values = line.split(",");
        try {
            T object = objectType.getDeclaredConstructor().newInstance();
            int index = 0;
            for (Field field : objectType.getDeclaredFields()) {
                field.setAccessible(true);
                Class<?> fieldType = field.getType();
                if (fieldType == String.class) {
                    field.set(object, values[index]);
                } else if (fieldType == int.class || fieldType == Integer.class) {
                    field.set(object, Integer.parseInt(values[index]));
                } else if (fieldType == boolean.class || fieldType == Boolean.class) {
                    field.set(object, Boolean.parseBoolean(values[index]));
                }
                index++;
            }
            return object;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

public class AHHASCSystem {
    private List<User> users;
    private List<Customer> customers;
    private List<Appointment> appointments;
    private String userFilePath = "users.csv";
    private String customerFilePath = "customers.csv";
    private String appointmentFilePath = "appointments.csv";
    private User currentUser;

    public AHHASCSystem() {
        users = new ArrayList<>();
        customers = new ArrayList<>();
        appointments = new ArrayList<>();
        DataAccess.readObjectsFromCSV(users, userFilePath, User.class);
        DataAccess.readObjectsFromCSV(customers, customerFilePath, Customer.class);
        DataAccess.readObjectsFromCSV(appointments, appointmentFilePath, Appointment.class);
    }

    // permission checks
    private boolean hasCurrentUserPermission(User.Role requiredRole) {
        return currentUser != null && currentUser.getRole() == requiredRole;
    }

    // account management
    public Boolean login(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                currentUser = user;
                System.out.println("Login successful.");
                return true;
            }
        }
        return false;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    // user management
    public User createUser(String username, String password, User.Role role) {
        if (!hasCurrentUserPermission(User.Role.CENTRE_MANAGER)) {
            System.out.println("Permission denied: Get user list");
            return null;
        }

        boolean usernameExists = users.stream().anyMatch(user -> user.getUsername().equals(username));
        if (usernameExists) {
            System.out.println("Username already exists.");
            return null;
        }

        User newUser = null;

        if (role == User.Role.CENTRE_MANAGER) {
            newUser = new CentreManager(username, password);
            users.add(newUser);
            System.out.println("Centre Manager created successfully.");

        } else if (role == User.Role.TECHNICIAN) {
            newUser = new Technician(username, password);
            users.add(newUser);
            System.out.println("Technician created successfully.");

        } else {
            throw new IllegalArgumentException("Invalid user type.");
        }

        saveUsers();
        System.out.println("User created successfully.");
        return newUser;
    }

    public List<User> getUsers() {
        if (!hasCurrentUserPermission(User.Role.CENTRE_MANAGER)) {
            System.out.println("Permission denied: Get user list");
            return null;
        }

        return users;
    }

    public void saveUsers() {
        DataAccess.saveObjectsToCSV(users, "users.csv");
    }

    // appointment management
    public Appointment bookAppointment(Customer customer, Technician technician, LocalDate appointmentDate,
            BigDecimal paymentAmount) {
        if (!hasCurrentUserPermission(User.Role.CENTRE_MANAGER)) {
            System.out.println("Permission denied: Book appointment");
            return null;
        }

        Appointment appointment = new Appointment(customer, technician, appointmentDate, paymentAmount);
        appointments.add(appointment);
        return appointment;
    }

    public Boolean cancelAppointment(Appointment appointment) {
        if (!hasCurrentUserPermission(User.Role.CENTRE_MANAGER)) {
            System.out.println("Permission denied: Cancel appointment");
            return false;
        }

        appointments.remove(appointment);
        return true;
    }

    // there's nothing to ensure anyone else can edit appointments, like,
    // technicians can edit stuff from their list, which is a big no
    // TODO implement setters for them

    public List<Appointment> getTechnicianAppointments() {
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
        customers.add(newCustomer);
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

    public List<Customer> getCustomers() {
        if (!hasCurrentUserPermission(User.Role.CENTRE_MANAGER)) {
            System.out.println("Permission denied: Get customer list");
            return null;
        }

        return customers;
    }

    // technician features
    public List<Appointment> checkAssignedAppointments(Technician technician) {
        if (!hasCurrentUserPermission(User.Role.TECHNICIAN)) {
            System.out.println("Permission denied: Check assigned appointments");
        }

        List<Appointment> technicianAppointments = new ArrayList<Appointment>();
        String technicianId = technician.getUserId();
        for (Appointment appointment : appointments) {
            if (appointment.getTechnician().getUserId().equals(technicianId)) {
                technicianAppointments.add(appointment);
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

        // TODO
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
