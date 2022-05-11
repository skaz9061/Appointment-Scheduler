package scheduler.viewcontroller.reports;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import scheduler.dao.AppointmentDAO;
import scheduler.dao.ContactDAO;
import scheduler.dao.CustomerDAO;
import scheduler.dao.UserDAO;
import scheduler.model.Appointment;
import scheduler.model.Contact;
import scheduler.model.Customer;
import scheduler.model.User;
import scheduler.mysql.DBAppointment;
import scheduler.mysql.DBContact;
import scheduler.mysql.DBCustomer;
import scheduler.mysql.DBUser;
import scheduler.util.Alerts;
import scheduler.util.NodeUtils;
import scheduler.util.Router;

import java.net.URL;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controller for the report menu screen.
 * */
public class ReportMenuController implements Initializable {
    public ComboBox<Customer> customerComboBox;
    public ComboBox<Contact> contactComboBox;
    public ComboBox<User> userComboBox;
    public Label errorLabel;

    private final CustomerDAO dbCust = new DBCustomer();
    private final ContactDAO dbCon = new DBContact();
    private final UserDAO dbUser = new DBUser();
    private final AppointmentDAO dbAppt = new DBAppointment();

    /**
     * Called when the controller is loaded, it initializes values for the controller.
     * @param url not used
     * @param resourceBundle not used
     * */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customerComboBox.setItems(FXCollections.observableList(dbCust.getAllCustomers()));
        contactComboBox.setItems(FXCollections.observableList(dbCon.getAllContacts()));
        userComboBox.setItems(FXCollections.observableList(dbUser.getAllUsers()));
    }

    /**
     * Event handler for the back button. Returns to the previous route.
     * @param actionEvent the event that triggered the method
     * */
    public void onBack(ActionEvent actionEvent) {
        Router.prevRoute(Router.getStage(actionEvent));
    }

    /**
     * Event handler for the customer schedule button.
     * @param actionEvent the event that triggered the method
     * */
    public void onCustomerSchedule(ActionEvent actionEvent) {
        if (validate(customerComboBox)) {
            Customer customer = customerComboBox.getValue();
            List<Appointment> appts = dbAppt.getAppointmentsFor(customer);
            appts.sort(ReportMenuController::sortAppt);
            var map = getApptColumnFieldMap();
            String reportName = "Customer Schedule, " + customer.getName();
            Router.addRoute(getClass());
            Router.goToReportView(Router.getStage(actionEvent), appts, map, reportName, PropertyValueFactory::new);
        } else {
            Alerts.error("Customer Error", "Nothing Selected", "You must choose a customer to run this report.");
        }
    }

    /**
     * Event handler for the contact schedule button.
     * @param actionEvent the event that triggered the method
     * */
    public void onContactSchedule(ActionEvent actionEvent) {
        if (validate(contactComboBox)) {
            Contact contact = contactComboBox.getValue();
            List<Appointment> appts = dbAppt.getAppointmentsFor(contact);
            appts.sort(ReportMenuController::sortAppt);
            var map = getApptColumnFieldMap();
            String reportName = "Contact Schedule, " + contact.getName();

            Router.addRoute(getClass());
            Router.goToReportView(Router.getStage(actionEvent), appts, map, reportName, PropertyValueFactory::new);
        } else {
            Alerts.error("Contact Error", "Nothing Selected", "You must choose a contact to run this report.");
        }
    }

    /**
     * Event handler for the user schedule button. The lambda expression filters the appointments by user.
     * @param actionEvent the event that triggered the method
     * */
    public void onUserSchedule(ActionEvent actionEvent) {
        if (validate(userComboBox)) {
            User user = userComboBox.getValue();
            List<Appointment> appts = dbAppt.getAllAppointments()
                    .stream()
                    .filter(a -> a.getUser().equals(user))
                    .sorted(ReportMenuController::sortAppt)
                    .collect(Collectors.toList());
            var map = getApptColumnFieldMap();
            String reportName = "User Schedule, " + user.getName();

            Router.addRoute(getClass());
            Router.goToReportView(Router.getStage(actionEvent), appts, map, reportName, PropertyValueFactory::new);
        } else {
            Alerts.error("User Error", "Nothing Selected", "You must choose a user to run this report.");
        }
    }

    /**
     * Event handler for the month analysis button. The first lambda expression maps the appointments to their month value, which is then
     * grouped and counted with the stream. The second lambda expression maps each month/count entry to a list where the
     * first element is the month string and the second element is the count string.
     * @param actionEvent the event that triggered the method
     * */
    public void onMonthAnalysis(ActionEvent actionEvent) {
        Map<Month, Long> data = dbAppt.getAllAppointments()
                .stream()
                .map(a -> a.getStart().getMonth()).sorted(Comparator.naturalOrder())
                .collect(Collectors.groupingBy(a -> a,Collectors.counting()));

        Map<Month, Long> dataAllMonths = new LinkedHashMap<>();

        for(Month month: Month.values()) {
            dataAllMonths.put(month, data.getOrDefault(month, 0L));
        }

        Map<String, String> map = new LinkedHashMap<>();
        map.put("Month", "0");
        map.put("Count", "1");

        var dataEntryList = new ArrayList<>(dataAllMonths.entrySet());

        List<List<String>> list = dataEntryList.stream()
                .map(e -> {
                    List<String> l = new ArrayList<>();
                    l.add(e.getKey().toString());
                    l.add(e.getValue().toString());
                    return l;
                }).collect(Collectors.toList());

        String reportName = "Month Analysis";
        Router.addRoute(getClass());
        Router.goToReportView(Router.getStage(actionEvent), list, map, reportName, NodeUtils.ListValueFactory);
    }

    /**
     * Event handler for the type analysis button. The lambda expression maps the type/count entry to a list where the
     * first element is the type string and the second element is the count string.
     * @param actionEvent the event that triggered the method
     * */
    public void onTypeAnalysis(ActionEvent actionEvent) {
        var data = dbAppt.appointmentsByType();
        Map<Appointment.Type, Long> dataAllTypes = new LinkedHashMap<>();

        for(var type: Appointment.Type.values()) {
            dataAllTypes.put(type, data.getOrDefault(type, 0L));
        }

        var dataEntryList = new ArrayList<>(dataAllTypes.entrySet());

        List<List<String>> list = dataEntryList.stream()
                .map(e -> {
                    List<String> l = new ArrayList<>();
                    l.add(e.getKey().toString());
                    l.add(e.getValue().toString());
                    return l;
                }).collect(Collectors.toList());

        Map<String,String> map = new LinkedHashMap<>();
        map.put("Type", "0");
        map.put("Count", "1");

        String reportName = "Type Analysis";
        Router.addRoute(getClass());
        Router.goToReportView(Router.getStage(actionEvent), list, map, reportName, NodeUtils.ListValueFactory);
    }

    /**
     * Checks if a combo box is not null.
     * @param cmb the combo box
     * @return true if its not null
     * */
    private boolean validate(ComboBox<?> cmb) {
        return cmb.getValue() != null;
    }

    /**
     * Provides the column-field map for a report of appointments.
     * @return the column-field map
     * */
    private Map<String, String> getApptColumnFieldMap() {
        List<String> fieldNames = new ArrayList<>();
        List<String> columnNames = new ArrayList<>();
        Map<String, String> columnFieldMap = new LinkedHashMap<>();
        fieldNames.addAll(List.of("id", "title", "description", "location", "contact", "type", "startFormatted", "endFormatted", "customerId", "customerName"));
        columnNames.addAll(List.of("Appt ID", "Title", "Description", "Location", "Contact", "Type", "Start", "End", "Cust ID", "Customer Name"));

        for(int i = 0; i < fieldNames.size(); i++)
            columnFieldMap.put(columnNames.get(i), fieldNames.get(i));

        return columnFieldMap;
    }

    /**
     * Comparator function for sorting appointments.
     * @param a first appointment
     * @param b second appointment
     * @return -1 for less than, 0 for equal, and 1 for greater than
     * */
    private static int sortAppt(Appointment a, Appointment b) {
        if (a.getStart().equals(b.getStart()))
            return 0;
        else if (a.getStart().isBefore(b.getStart()))
            return -1;
        else
            return 1;
    }
}
