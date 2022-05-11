ATTENTION: Database has since been disconnected. A new database will need to be setup for the program to function properly.


APPOINTMENT SCHEDULER
~~~~~~~~~~~~~~~~~~~~~

This application allows the users to add, edit, and delete customers and add, edit, and delete appointments.
Appointments are scheduled between customers, contacts, and users. Customers cannot have overlapping appointments,
but contacts and users can. There are also several ways to filter the data and generate reports to analyze appointment
trends.

Javadocs are located in the javadocs folder at the root of the project directory.

Author: Steven Kazmierkiewicz
Email: skazmie@wgu.edu
Version: 1.0.6
Date: 2021-05-24

IDE: IntelliJ IDEA 2021.1 (Community Edition)
JDK: Java SE 11.0.10
JAVAFX: JavaFX-SDK-11.0.2
MYSQL DRIVER: mysql-connector-java-8.1.23


-DIRECTIONS

Customers Screen
----------------
* From the customer screen, the user can use the search box at the top of the page to search through all customer records.
  The dropdown next to the search box lets the user choose which fields of the customer to search through.

* The 'View Appointments' button will bring the user to appointments screen showing the appointments for a specific
  customer. If no customer is selected, it brings the user to the appointments screen for all appointments.

Appointments Screen
-------------------
* There are many options to filter appointments. The radio buttons at the top of the screen allow the user to show
  appointments for all, by customer, or by contact. If filtering by customer or contact, the user must choose either
  a customer or contact in the dropdown to the right of the radio buttons.
* The tabs allow the user to view all appointments, monthly appointments, or weekly appointments. The left and right
  arrow buttons will cycle throw months or weeks sequentially.  The user can go directly to a specific month by
  choosing the month and year when the monthly tab is selected. The user can go directly to a specific week by choosing
  a date within that week from the date picker when the weekly tab is selected.
* The user can generate a report from the Appointments screen at any time by clicking the 'Generate Report' button.
  The appointments that are currently displayed in the table view will be saved to disk as a .csv file.

Add/Edit Appointments
---------------------
* All times are given in the user's default timezone. At the top of the screen, the open hours (8am - 10pm ET) are
  converted to the users time zone. Any appointments must be scheduled between these times.

-REPORTS
* Required Reports
    - Login attempts saved to the login_activity.txt file in the root directory of the project.
    - Schedule for contacts
    - Count of appointments by month
    - Count of appointments by type

* Additional Reports
    - On the Appointments screen
        - Appointments for all, by customer, or by contact
        - Appointments for all dates, by month, by week
        - Any combination of the first two bullets
    - Schedule for customers
    - Schedule for users

**** All reports can be exported to a .csv file ****
----------------------------------------------------
-Use the 'Generate Report' button on the appointments screen.
-Use the 'Export' button on the Report View screen.

**** Back buttons will go to previous screen ****
-------------------------------------------------
If the user navigates Login -> Main -> Appointments
    - back button will bring the user to the Main screen

However, if the user navigates Login -> Main -> Customers -> Appointments
    - back button will bring the user to the Customers screen, not Main
