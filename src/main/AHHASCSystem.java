package main;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

enum Role {
    CENTRE_MANAGER,
    TECHNICIAN,
}

enum TechnicianSpecialization {
    APPLIANCE_REPAIR,
    ELECTRICAL,
    PLUMBING,
    CARPENTRY,
}

class User {
    private String userId;
    private String username;
    private String password;
    private Role role;

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
    UserManager userManager;
    CustomerManager customerManager;

    public CentreManager(String username, String password) {
        super(username, password, Role.CENTRE_MANAGER);
        this.userManager = new UserManager();
        this.customerManager = new CustomerManager();
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public CustomerManager getCustomerManager() {
        return customerManager;
    }

    public void setCustomerManager(CustomerManager customerManager) {
        this.customerManager = customerManager;
    }
}

class Technician extends User {
    private String specialization;
    // TODO did the doc mention specilaization or issit a chatgpt hallucination

    public Technician(String username, String password, String specialization) {
        super(username, password, Role.TECHNICIAN);
        this.specialization = specialization;
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
                getRole() + "," +
                specialization;
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

    public Appointment bookAppointment(String technicianId, String appointmentDate) {
        Appointment appointment = new Appointment(customerID, technicianId, appointmentDate);
        return appointment;
    }

    public String toString() {
        return customerID + "," + name + "," + contact_number + "," + contact_email;
    }
}

class Appointment {
    private String appointmentId;
    private String customerId;
    private String technicianId;
    private String appointmentDate;
    private boolean paymentStatus;

    public Appointment(String customerId, String technicianId, String appointmentDate) {
        this.appointmentId = generateAppointmentId();
        this.customerId = customerId;
        this.technicianId = technicianId;
        this.appointmentDate = appointmentDate;
        this.paymentStatus = false;
    }

    private static String generateAppointmentId() {
        // Generate a unique appointment ID
        // Implementation logic...
        return "APPT123";
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getTechnicianId() {
        return technicianId;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public boolean getPaymentStatus() {
        return paymentStatus;
    }

    public String toString() {
        return appointmentId + "," + customerId + "," + technicianId + "," + appointmentDate + "," + paymentStatus;
    }
}

// TODO better save to file
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

    static public <T> void readObjectsFromCSV(List<T> objects, String filePath) {
        try (BufferedReader writer = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = writer.readLine()) != null) {
                @SuppressWarnings("unchecked")
                T object = parseObjectFromLine(line, (Class<T>) objects.get(0).getClass());
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

class UserManager {
    // TODO
}

class CustomerManager {
    // TODO
}

public class AHHASCSystem {
    List<User> users;
    List<Customer> customers;
    List<Appointment> appointments;
    User currentUser;

    public static void main(String[] args) {
    }

    void login(String username, String password) {
        // TODO check in users list for matching username
        // then check password
    }

    class UserExistsException extends Exception {
        public UserExistsException(String message) {
            super(message);
        }
    }

    void createUser(String username, String password, Role role) throws UserExistsException {
        boolean usernameExists = users.stream().anyMatch(user -> user.getUsername().equals(username));
        if (usernameExists) {
            throw new UserExistsException("Username already exists. Please choose a different username.");
        }

        if (role == Role.CENTRE_MANAGER) {
            CentreManager manager = new CentreManager(username, password);
            users.add(manager);
            System.out.println("Centre Manager created successfully.");

        } else if (role == Role.TECHNICIAN) {
            throw new IllegalArgumentException("Invalid user type. Technician specialization is required.");

        } else {
            throw new IllegalArgumentException("Invalid user type.");
        }

        System.out.println("User created successfully.");
    }

    void createUser(String username, String password, Role role, TechnicianSpecialization specialization)
            throws UserExistsException {
        // WONTFIX duplicate code
        boolean usernameExists = users.stream().anyMatch(user -> user.getUsername().equals(username));
        if (usernameExists) {
            throw new UserExistsException("Username already exists. Please choose a different username.");
        }

        if (role == Role.TECHNICIAN) {
            Technician technician = new Technician(username, password,
                    "Appliance Repair");
            users.add(technician);
            System.out.println("Technician created successfully.");

        } else {
            throw new IllegalArgumentException("Invalid user type.");
        }

        System.out.println("User created successfully.");
    }
}
