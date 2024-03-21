## pages
- [x] login and register - wenyi
- [ ] edit passwords
- [x] centreManagerPanel - jas, wenyi
  - [x] user management - wenyi
    - [x] remove user
    - [x] addUser()
  - [x] customer management - jas, wenyi
    - [x] addCustomer()
    - [x] CustomerWindow
      - [x] removeCustomer()
      - [x] editCustomer()
      - [-] BookAppointment - jas, wenyi
  - [x] appointment management - wenyi
      - [x] BookAppointment - jas, wenyi
    - [ ] update appointment info
    - [x] cancel appointment
- [x] technicianPanel - wenyi
  - [x] AppointmentWindow
    - [x] collect payment
    - [x] enter feedback

## design
### features
each feature has its own individual page
- for all end users
  - login()
    - set current user to a user class
  - getCurrentUser()
  - edit current user
    - directly through the user object
- for centre managers
  - user management
    - createUser()
    - removeUser()
    - getUsers()
    - edit user
      - directly through the user object
  - customer management
    - addCustomer()
    - removeCustomer()
    - editCustomer()
    - getCustomers()
  - appointment management
    - bookAppointment()
    - cancelAppointment()
    - getAppointments()
    - update appointment
      - directly through the appointment object?
      - or return a clone, and implement setters that only CentreManagers can edit?
- for technicians
  - checkAssignedAppointments()
  - collectPayment()
    - from customers
  - enterFeedback()
    - for customers to enter feedback after each appointment

### classes
- user interface
  - check user type?

- data access
  - read()
  - save()

- system
  - list of:
    - CentreManager
    - Technician
    - Customer
    - Appointment
  - all the features as functions
    - each with a permission check

### classes for the items
- User
  - CentreManager
  - Technician

- Customer
- Appointment
  - contains:
    - Customer
    - Technician
