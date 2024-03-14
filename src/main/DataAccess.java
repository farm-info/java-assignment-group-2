package main;

import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import java.time.LocalDate;

class DataAccess {
    // TODO modify toString to support nested objects
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

    @SuppressWarnings("unchecked")
    static public <T extends BaseItem> void readObjectsFromCSV(Map<String, T> objects, String filePath) {
        try (BufferedReader writer = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = writer.readLine()) != null) {
                T object = parseObjectFromLine(line, (Class<T>) BaseItem.class);
                objects.put(object.getId(), object);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Map<String, Object> parsedObjects

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
                } else if (fieldType == LocalDate.class) {
                    field.set(object, LocalDate.parse(values[index]));
                } else if (fieldType == BigDecimal.class) {
                    field.set(object, new BigDecimal(values[index]));
                } else if (fieldType.isEnum()) {
                    field.set(object, Enum.valueOf((Class<Enum>) fieldType, values[index]));
                } else {
                    throw new IllegalArgumentException("Unsupported field type: " + fieldType);
                }
                index++;
            }

            return object;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    static public <T extends BaseItem> void readObjectsFromCSV(Map<String, T> objects, String filePath,
            Map<String, User> users, Map<String, Customer> customer) {
        try (BufferedReader writer = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = writer.readLine()) != null) {
                T object = parseObjectFromLine(line, (Class<T>) BaseItem.class, users, customer);
                objects.put(object.getId(), object);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static <T> T parseObjectFromLine(String line, Class<T> objectType, Map<String, User> users,
            Map<String, Customer> customers) {
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
                } else if (fieldType == LocalDate.class) {
                    field.set(object, LocalDate.parse(values[index]));
                } else if (fieldType == BigDecimal.class) {
                    field.set(object, new BigDecimal(values[index]));
                } else if (fieldType.isEnum()) {
                    field.set(object, Enum.valueOf((Class<Enum>) fieldType, values[index]));

                } else if (fieldType == Customer.class) {
                    String customerId = values[index];
                    Customer customer = null;
                    if (customers.containsKey(customerId)) {
                        customer = (Customer) customers.get(customerId);
                    } else {
                        throw new NestedObjectNotFoundException("Customer with id " + customerId + " not found");
                    }
                    field.set(object, customer);

                } else if (fieldType == Technician.class) {
                    String technicianId = values[index];
                    Technician technician = null;
                    if (users.containsKey(technicianId)) {
                        technician = (Technician) users.get(technicianId);
                    } else {
                        throw new NestedObjectNotFoundException("Technician with id " + technicianId + " not found");
                    }
                    field.set(object, technician);

                } else {
                    throw new IllegalArgumentException("Unsupported field type: " + fieldType);
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
