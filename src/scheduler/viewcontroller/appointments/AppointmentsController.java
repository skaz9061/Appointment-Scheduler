package scheduler.viewcontroller.appointments;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import scheduler.dao.AppointmentDAO;
import scheduler.dao.ContactDAO;
import scheduler.dao.CustomerDAO;
import scheduler.model.Appointment;
import scheduler.model.Contact;
import scheduler.model.Customer;
import scheduler.mysql.DBAppointment;
import scheduler.mysql.DBContact;
import scheduler.mysql.DBCustomer;
import scheduler.util.*;
import scheduler.util.time.Week;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

/**
 * Controller for the Appointments screen.
 * */
public class AppointmentsController implements Initializable {
    public TableView<Appointment> allDatesTable;
    public TableView<Appointment> monthlyTable;
    public Label monthLabel;
    public TableView<Appointment> weeklyTable;
    public Label weekLabel;
    public ComboBox accountComboBox;
    public Label accountLabel;
    public RadioButton allRadio;
    public RadioButton contactRadio;
    public RadioButton customerRadio;
    public Button prevMonthBtn;
    public Button nextMonthBtn;
    public Button prevWeekBtn;
    public Button nextWeekBtn;
    public Spinner<Integer> yearChoice;
    public ChoiceBox<Month> monthChoice;

    private final List<String> fieldNames = new ArrayList<>();
    public TabPane appointmentsTabPane;
    private ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
    private List<Customer> allCustomers;
    private List<Contact> allContacts;
    private ViewOption viewOption = ViewOption.ALL;
    private final AppointmentDAO dbAppt = new DBAppointment();
    private final CustomerDAO dbCust = new DBCustomer();
    private final ContactDAO dbContact = new DBContact();
    private final List<Month> monthList = Arrays.asList(Month.values());
    private int currMonthIndex = 0;
    private int currYear;
    private Week currWeek;

    /**
     * Called when the controller is loaded, it initializes values for the controller.
     * @param url not used
     * @param resourceBundle not used
     * */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // field names for the table columns
        fieldNames.addAll(List.of("id", "title", "description", "location", "contact", "type", "startFormatted", "endFormatted", "customerId", "customerName"));

        // setting up the columns
        for (TableView tv : new TableView[]{allDatesTable, monthlyTable, weeklyTable}) {
            var cols = tv.getColumns();

            for (int i = 0; i < cols.size(); i++) {
                ((TableColumn) cols.get(i)).setCellValueFactory(new PropertyValueFactory<>(fieldNames.get(i)));
            }
        }

        // configure toggle group for view options
        setToggleGroup();
        allRadio.setSelected(true);

        // retrieve data from db
        allAppointments = FXCollections.observableList(dbAppt.getAllAppointments());
        allCustomers = dbCust.getAllCustomers();
        allContacts = dbContact.getAllContacts();

        // get current month, week, and year
        currMonthIndex = monthList.indexOf(LocalDateTime.now().getMonth());
        currYear = LocalDateTime.now().getYear();
        currWeek = Week.of(LocalDateTime.now());

        // set up controls to pick month
        monthChoice.setItems(FXCollections.observableList(monthList));
        monthChoice.getSelectionModel().select(monthList.get(currMonthIndex));
        yearChoice.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1950, 3000, currYear));

        // Set the month and week label
        setMonthLabel();
        setWeekLabel();

        // Set the data in the tables
        showAll();
    }

    /**
     * Event handler for the back button. Goes back to the previous route.
     * @param actionEvent the event that triggered the method
     * */
    public void goBack(ActionEvent actionEvent) {
        Router.prevRoute(Router.getStage(actionEvent));
    }

    /**
     * Gets all appointments filtered based on current options chosen in the UI. The lambda expressions are used to filter
     * the appointments by either the customer or the contact.
     * @return the filtered list of Appointments
     * */
    private ObservableList<Appointment> getAppointments() {
        switch (viewOption) {
            case ALL:
                return allAppointments;
            case CONTACT:
                return allAppointments.filtered(a -> a.getContact().equals(accountComboBox.getSelectionModel().getSelectedItem()));
            case CUSTOMER:
                return allAppointments.filtered(a -> a.getCustomer().equals(accountComboBox.getSelectionModel().getSelectedItem()));
            default:
                return FXCollections.observableArrayList();
        }
    }

    /**
     * Event handler fo the previous month button.
     * @param actionEvent the event that triggered the method
     * */
    public void prevMonth(ActionEvent actionEvent) {
        currMonthIndex--;
        monthCheck();
        setMonthLabel();
        setMonthlyTable(getAppointments());
    }

    /**
     * Event handler fo the next month button.
     * @param actionEvent the event that triggered the method
     * */
    public void nextMonth(ActionEvent actionEvent) {
        currMonthIndex++;
        monthCheck();
        setMonthLabel();
        setMonthlyTable(getAppointments());
    }

    /**
     * Event handler fo the previous week button.
     * @param actionEvent the event that triggered the method
     * */
    public void prevWeek(ActionEvent actionEvent) {
        currWeek = currWeek.prevWeek();
        setWeekLabel();
        setWeeklyTable(getAppointments());
    }

    /**
     * Event handler fo the next week button.
     * @param actionEvent the event that triggered the method
     * */
    public void nextWeek(ActionEvent actionEvent) {
        currWeek = currWeek.nextWeek();
        setWeekLabel();
        setWeeklyTable(getAppointments());
    }

    /**
     * Event handler for the account combo box.
     * @param actionEvent the event that triggered the method
     * */
    public void onAccountSelect(ActionEvent actionEvent) {
        resetTables();
    }

    /**
     * Resets the view to show appointments for all accounts.
     * */
    public void showAll() {
        viewOption = ViewOption.ALL;
        resetTables();

        accountLabel.setDisable(true);
        accountComboBox.setDisable(true);
    }

    /**
     * Event handler for the all radio button.
     * @param actionEvent the event that triggered the method
     * */
    public void showAll(ActionEvent actionEvent) {
        showAll();
    }

    /**
     * Resets the view to show appointments by contact.
     * */
    public void byContact() {
        viewOption = ViewOption.CONTACT;
        resetTables();

        accountLabel.setDisable(false);
        accountLabel.setText("Contact");
        accountComboBox.setDisable(false);
        accountComboBox.setItems(FXCollections.observableList(allContacts));
    }

    /**
     * Event handler for the byContact radio button.
     * @param actionEvent the event that triggered the method
     * */
    public void byContact(ActionEvent actionEvent) {
        byContact();
    }

    /**
     * Resets the view to show appointments by customer.
     * */
    public void byCustomer() {
        viewOption = ViewOption.CUSTOMER;
        resetTables();

        accountLabel.setDisable(false);
        accountLabel.setText("Customer");
        accountComboBox.setDisable(false);
        accountComboBox.setItems(FXCollections.observableList(allCustomers));
    }

    /**
     * Event handler for the byCustomer radio button.
     * @param actionEvent the event that triggered the method
     * */
    public void byCustomer(ActionEvent actionEvent) {
        byCustomer();
    }

    /**
     * Resets the values of all table views with the appropriate list of appointments filtered by account.
     * */
    private void resetTables() {
        var appts = getAppointments();

        allDatesTable.setItems(appts);
        setMonthlyTable(appts);
        setWeeklyTable(appts);
    }

    /**
     * Resets the monthly table with appointments filtered by month. The lambda expression is used to filter the
     * appointments by month and year.
     * @param list the appointments
     * */
    private void setMonthlyTable(ObservableList<Appointment> list) {
        monthlyTable.setItems(list.filtered(a -> {
            var start = a.getStart();
            var month = monthList.get(currMonthIndex);
            return start.getMonth().equals(month) && start.getYear() == currYear;
        }));
    }

    /**
     * Resets the weekly table with appointments filtered by week. The lambda expression is used to filter the
     * appointments by week.
     * @param list the appointments
     * */
    private void setWeeklyTable(ObservableList<Appointment> list) {
        weeklyTable.setItems(list.filtered(a -> currWeek.contains(a.getStart())));
    }

    /**
     * Checks if the months have reached the beginning or end of the year, then cycles to beginning ofmnext year
     * or end of last year accordingly.
     * */
    private void monthCheck() {
        if (currMonthIndex < 0) {
            currMonthIndex = 11;
            currYear--;
        } else if (currMonthIndex > 11) {
            currMonthIndex = 0;
            currYear++;
        }
    }

    /**
     * Sets the month label with the current month and year the user is viewing.
     * */
    private void setMonthLabel() {
        monthLabel.setText(monthList.get(currMonthIndex).toString() + " " + currYear);
    }

    /**
     * Sets the week label with the current week the user is viewing.
     * */
    private void setWeekLabel() {
        weekLabel.setText(currWeek.toString());
    }

    /**
     * Sets the toggle group for the account view radio buttons.
     * */
    private void setToggleGroup() {
        ToggleGroup tg = new ToggleGroup();
        allRadio.setToggleGroup(tg);
        customerRadio.setToggleGroup(tg);
        contactRadio.setToggleGroup(tg);
    }

    /**
     * Event handler for the goToWeek date picker.
     * @param actionEvent the event that triggered the method
     * */
    public void onGoToWeekOf(ActionEvent actionEvent) {
        DatePicker control = (DatePicker) actionEvent.getSource();
        goToWeekOf(control.getValue());
    }

    /**
     * Event handler for the weekly today button.
     * @param actionEvent the event that triggered the method
     * */
    public void onGoToTodayWeek(ActionEvent actionEvent) {
        goToWeekOf(LocalDateTime.now());
    }

    /**
     * Sets the weekly view to the week of the datetime.
     * @param dateTime the datetime
     * */
    private void goToWeekOf(LocalDateTime dateTime) {
        goToWeekOf(LocalDate.from(dateTime));
    }

    /**
     * Sets the weekly view to the week of the date.
     * @param date the date
     * */
    private void goToWeekOf(LocalDate date) {
        currWeek = Week.of(date);
        setWeekLabel();
        setWeeklyTable(getAppointments());
    }

    /**
     * Event handler for the goToMonth dropdown.
     * @param actionEvent the event that triggered the method
     * */
    public void onGoToMonth(ActionEvent actionEvent) {
        currMonthIndex = monthList.indexOf((Month) monthChoice.getSelectionModel().getSelectedItem());
        currYear = (Integer) yearChoice.getValue();

        setMonthLabel();
        setMonthlyTable(getAppointments());
    }

    /**
     * Event handler for the weekly today button.
     * @param actionEvent the event that triggered the method
     * */
    public void onGoToTodayMonth(ActionEvent actionEvent) {
        goToMonth(LocalDateTime.now());
    }

    /**
     * Sets the monthly view to the month of the datetime.
     * @param dateTime the datetime
     * */
    private void goToMonth(LocalDateTime dateTime) {
        goToMonth(LocalDate.from(dateTime));
    }

    /**
     * Sets the monthly view to the month of the date.
     * @param date the datetime
     * */
    private void goToMonth(LocalDate date) {
        currMonthIndex = monthList.indexOf(date.getMonth());
        currYear = date.getYear();
        setMonthLabel();
        setMonthlyTable(getAppointments());
    }

    /**
     * Event handler for a spinner to check if it contains an integer. If not, it clears the field to the last committed value.
     * @param keyEvent the event that triggered the method
     * */
    public void checkForNum(KeyEvent keyEvent) {
        Spinner spinner = (Spinner) keyEvent.getSource();
        String text = keyEvent.getText();

        try {
            Integer.parseInt(text);
        } catch (NumberFormatException | NullPointerException e) {
            spinner.cancelEdit();
        }
    }

    /**
     * Initializes the view to be By Customer for a specific customer. Used when navigating from the customers screen.
     * @param customer the customer to load
     * */
    public void loadCustomer(Customer customer) {
        customerRadio.setSelected(true);
        byCustomer();
        accountComboBox.setValue(customer);
        resetTables();
    }

    /**
     * Event handler for the Add button.
     * @param actionEvent the event that triggered the method
     * */
    public void onAdd(ActionEvent actionEvent) {
        Router.addRoute(getClass());
        Router.goToAddAppointment(Router.getStage(actionEvent));
    }

    /**
     * Event handler for the Edit button.
     * @param actionEvent the event that triggered the method
     * */
    public void onEdit(ActionEvent actionEvent) {
        Appointment appointment = getSelected();

        if (appointment == null) {
            Alerts.error("Appointment Error", "Appointment Error",
                    "You must select an appointment to edit.");
        }
        else {
            Router.addRoute(getClass());
            Router.goToEditAppointment(Router.getStage(actionEvent), appointment);
        }
    }

    /**
     * Event handler for the Delete button.
     * @param actionEvent the event that triggered the method
     * */
    public void onDelete(ActionEvent actionEvent) {
        Appointment appointment = getSelected();

        if (appointment == null) {
            Alerts.error("Appointment Error", "Appointment Error", "You must select an appointment to delete.");
        } else {
            boolean result = Alerts.confirm("Confirm Delete", "Are you sure?",
                    "Are you sure you want to delete Appointment " + appointment.getId() + "?");
            if (result) {
                if (dbAppt.deleteAppointment(appointment)) {
                    allAppointments.remove(appointment);
                    resetTables();

                    Alerts.info("Success", "Successfully Deleted",
                            "Appointment " + appointment.getId() + " was successfully deleted from the database.");
                } else {
                    Alerts.error("Database Error", "Database Error", "There was an error deleting appointment from the database.");
                }
            }
        }
    }

    /**
     * Gets the table view object of the current tab.
     * @return the table view
     * */
    private TableView getActiveTableView() {
        Node tabContent = NodeUtils.getTabPaneContent(appointmentsTabPane);
        return NodeUtils.findChild( ((Parent) tabContent), TableView.class);
    }

    /**
     * Gets the selected appointment.
     * @return the selected appointment
     * */
    private Appointment getSelected() {
        return (Appointment) getActiveTableView().getSelectionModel().getSelectedItem();
    }

    /**
     * Gets the file name for the report based on the current view options.
     * @return the filename for the report
     * */
    private String getReportName() {
        String reportName = "appt";

        switch (viewOption) {
            case CUSTOMER:
                reportName += "_cust_" + ((Customer) accountComboBox.getValue()).getId();
                break;
            case CONTACT:
                reportName += "_con_" + ((Contact) accountComboBox.getValue()).getId();
                break;
            default:
                reportName += "_all";
        }

        String tab = appointmentsTabPane.getSelectionModel().getSelectedItem().getText();
        switch (tab) {
            case "Monthly":
                reportName += "_" + monthList.get(currMonthIndex);
                break;
            case "Weekly":
                reportName += "_" + String.join("", currWeek.toISO().replace(" - ", "_").replace("-", ""));
                break;
            default:
                break;
        }

        return reportName;
    }

    /**
     * Event handler for the Generate Report Button.
     * @param actionEvent the event that triggered the method
     * */
    public void onGenReport(ActionEvent actionEvent) {
        Reports.export(getReportName(), getActiveTableView(), fieldNames, Router.getStage(actionEvent));
    }

    /**
     * An Enum representing the current account view option.
     * */
    private enum ViewOption {
        ALL, CUSTOMER, CONTACT
    }
}
