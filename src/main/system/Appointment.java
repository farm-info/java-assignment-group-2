package main.system;

import java.time.LocalDate;
import java.math.BigDecimal;

public class Appointment extends BaseItem {
    /*
     * WONTFIX We might want to consider storing IDs and utilizing hashmaps
     * instead of storing the whole object
     * But honestly, i'm not sure which one is the better solution
     */
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

    // overloaded constructor for reading from file
    public Appointment(String Id, Customer customer, Technician technician, LocalDate creationDate,
            LocalDate appointmentDate, BigDecimal paymentAmount, Boolean paymentStatus, String feedback) {
        this.setId(Id);
        this.customer = customer;
        this.technician = technician;
        this.creationDate = creationDate;
        this.appointmentDate = appointmentDate;
        this.paymentAmount = paymentAmount;
        this.paymentStatus = paymentStatus;
        this.feedback = feedback;
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
        return getId()
                + "," + customer.getId()
                + "," + technician.getId()
                + "," + creationDate
                + "," + appointmentDate
                + "," + paymentAmount
                + "," + paymentStatus
                + "," + feedback;
    }
}
