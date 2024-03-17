package main;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import java.time.LocalDate;

class DataAccess {
    // TODO it appends insteads of overwriting
    static public <T extends BaseItem> void saveObjectsToCSV(Map<String, T> objects, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            if (!objects.isEmpty()) {
                for (Map.Entry<String, T> entry : objects.entrySet()) {
                    T object = entry.getValue();
                    writer.write(object.toString());
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readUserDataFromCSV(Map<String, User> users, String filePath, Class<User> userClass)
            throws Exception {
        BufferedReader reader = getReaderOrInitializeFile(filePath);
        if (reader == null) {
            return;
        }

        String line;
        User user = null;
        while ((line = reader.readLine()) != null) {
            String[] values = line.split(",");
            if (values[2].equals("CENTRE_MANAGER")) {
                user = new CentreManager(values[0], values[1]);
            } else if (values[2].equals("TECHNICIAN")) {
                user = new Technician(values[0], values[1]);
            }
            users.put(user.getUsername(), user);
        }
        reader.close();
    }

    public static void readCustomerDataFromCSV(Map<String, Customer> customers, String filePath,
            Class<Customer> customerClass) throws Exception {
        BufferedReader reader = getReaderOrInitializeFile(filePath);
        if (reader == null) {
            return;
        }

        String line;
        while ((line = reader.readLine()) != null) {
            String[] values = line.split(",", -1);
            Customer customer = new Customer(values[0], values[1], values[2], values[3]);
            customers.put(customer.getId(), customer);
        }
        reader.close();
    }

    public static void readAppointmentDataFromCSV(Map<String, Appointment> appointments, String filePath,
            Map<String, User> users, Map<String, Customer> customers) throws Exception {
        BufferedReader reader = getReaderOrInitializeFile(filePath);
        if (reader == null) {
            return;
        }

        String line;
        while ((line = reader.readLine()) != null) {
            String[] values = line.split(",");
            String id = values[0];
            Customer customer = customers.get(values[1]);
            Technician technician = (Technician) users.get(values[2]);
            LocalDate creationDate = LocalDate.parse(values[3]);
            LocalDate appointmentDate = LocalDate.parse(values[4]);
            BigDecimal paymentAmount = new BigDecimal(values[5]);
            boolean paymentStatus = Boolean.parseBoolean(values[6]);
            String feedback = values[7];

            if (customer == null) {
                throw new IllegalArgumentException("No matching customer found for ID: " + values[1]);
            }
            if (technician == null) {
                throw new IllegalArgumentException("No matching technician found for ID: " + values[2]);
            }

            Appointment appointment = new Appointment(id, customer, technician, creationDate, appointmentDate,
                    paymentAmount, paymentStatus, feedback);
            appointments.put(appointment.getId(), appointment);
        }
        reader.close();
    }

    public static BufferedReader getReaderOrInitializeFile(String filePath) throws IOException {
        File file = new File(filePath);
        if (file.exists()) {
            return new BufferedReader(new FileReader(filePath));
        } else {
            file.createNewFile(); // Create the file if it doesn't exist
            return null;
        }
    }
}
