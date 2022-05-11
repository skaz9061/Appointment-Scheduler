package scheduler.viewcontroller.appointments;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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
import scheduler.util.NodeUtils;
import scheduler.util.Router;
import scheduler.util.time.Time;
import scheduler.util.time.TimeUtils;
import scheduler.util.Validators;
import scheduler.util.WordUtils;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

/**
 * Abstract class for the controllers of an Appointment change screen, either adding or editing.
 * */
public abstract class AppointmentChange implements Initializable {
    public Label titleLabel;
    public TextField idField;
    public TextField titleField;
    public TextField descriptionField;
    public TextField locationField;
    public ComboBox<Appointment.Type> typeComboBox;
    public ComboBox<Contact> contactComboBox;
    public ComboBox<Customer> customerComboBox;
    public ComboBox<User> userComboBox;
    public DatePicker startDatePicker;
    public Spinner<Time> startTimeSpinner;
    public DatePicker endDatePicker;
    public Spinner<Time> endTimeSpinner;
    public Label errorLabel;
    public Label hoursLabel;

    /**
     * Called when the controller is loaded, it initializes values for the controller.
     * @param url not used
     * @param resourceBundle not used
     * */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ContactDAO dbContact = new DBContact();
        UserDAO dbUser = new DBUser();
        CustomerDAO dbCust = new DBCustomer();

        typeComboBox.setItems(FXCollections.observableList(Arrays.asList(Appointment.Type.values())));
        contactComboBox.setItems(FXCollections.observableList(dbContact.getAllContacts()));
        customerComboBox.setItems(FXCollections.observableList(dbCust.getAllCustomers()));
        userComboBox.setItems(FXCollections.observableList(dbUser.getAllUsers()));
        userComboBox.setValue(User.getCurrentUser());

        LocalDate today = LocalDate.from(LocalDateTime.now());
        startDatePicker.setValue(today);
        endDatePicker.setValue(today);

        ObservableList<Time> startTimes = FXCollections.observableList(Time.allDayIntervals());
        ObservableList<Time> endTimes = FXCollections.observableArrayList(startTimes);

        startTimeSpinner.setValueFactory(new SpinnerValueFactory.ListSpinnerValueFactory<Time>(startTimes));
        endTimeSpinner.setValueFactory(new SpinnerValueFactory.ListSpinnerValueFactory<Time>(endTimes));

        Time closestTime = Time.closestTime();
        startTimeSpinner.getValueFactory().setValue(closestTime);
        endTimeSpinner.getValueFactory().setValue(closestTime.plusHours(1));

        if (endTimeSpinner.getValue().equals(new Time()))
            endDatePicker.setValue(today.plusDays(1));

        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
        StringBuilder hoursStr = new StringBuilder();
        hoursStr.append(TimeUtils.getLocalOpen().format(formatter));
        hoursStr.append(" - ");
        hoursStr.append(TimeUtils.getLocalClose().format(formatter));
        hoursLabel.setText(hoursStr.toString());
    }

    /**
     * Event handler for the save button.
     * @param actionEvent the event that triggered the method
     * */
    public abstract void onSave(ActionEvent actionEvent);

    /**
     * Event handler for the clear button.
     * @param actionEvent the event that triggered the method
     * */
    public void onClear(ActionEvent actionEvent) {
        clear();
    }

    /**
     * Event handler for the cancel button. It returns to the previous route.
     * @param actionEvent the event that triggered the method
     * */
    public void onCancel(ActionEvent actionEvent) {
        Router.prevRoute(Router.getStage(actionEvent));
    }

    /**
     * Clears the form to its default state.
     * */
    protected void clear() {
        NodeUtils.clear(titleField);
        NodeUtils.clear(descriptionField);
        NodeUtils.clear(locationField);
        NodeUtils.clear(typeComboBox);
        NodeUtils.clear(contactComboBox);
        NodeUtils.clear(customerComboBox);
        NodeUtils.clear(userComboBox);
        NodeUtils.clear(startDatePicker);
        NodeUtils.clear(startTimeSpinner, Time.closestTime());
        NodeUtils.clear(endDatePicker);
        NodeUtils.clear(endTimeSpinner, Time.closestTime().plusHours(1));
        errorLabel.setText("");
        userComboBox.setValue(User.getCurrentUser());
    }

    /**
     * Event handler for when the Start date or Time is changed.
     * @param event the event that triggered this method
     * */
    public void onStartTimeChange(Event event) {
        Time start = startTimeSpinner.getValue();
        endTimeSpinner.getValueFactory().setValue(start.plusHours(1));

        LocalDate startDate = startDatePicker.getValue();

        if (start.plusHours(1).isBefore(start))
            endDatePicker.setValue(startDate.plusDays(1));
        else
            endDatePicker.setValue(startDate);

    }

    /**
     * Validates all the fields on the form. The lambda expression is a shortcut for setting the border of all the start
     * and end date time fields to red.
     * @return the result of the validation
     * */
    protected Validators.ValidationResult validate() {
        StringBuilder error = new StringBuilder();
        Map<TextField, String> textFieldStringMap = new LinkedHashMap<>();
        textFieldStringMap.put(titleField, "title");
        textFieldStringMap.put(descriptionField, "description");
        textFieldStringMap.put(locationField, "location");

        Map<ComboBox, String> comboBoxStringMap = new LinkedHashMap<>();
        comboBoxStringMap.put(typeComboBox, "type");
        comboBoxStringMap.put(customerComboBox, "customer");
        comboBoxStringMap.put(contactComboBox, "contact");
        comboBoxStringMap.put(userComboBox, "user");

        boolean valid = true;

        NodeUtils.clearBorder(textFieldStringMap.keySet().toArray(new TextField[0]));
        NodeUtils.clearBorder(comboBoxStringMap.keySet().toArray(new ComboBox[0]));
        NodeUtils.clearBorder(startDatePicker);
        NodeUtils.clearBorder(startTimeSpinner);
        NodeUtils.clearBorder(endDatePicker);
        NodeUtils.clearBorder(endTimeSpinner);

        // **************** Checking for empty or null fields
        for (Map.Entry<TextField, String> entry : textFieldStringMap.entrySet()) {
            if (!Validators.notEmpty(entry.getKey().getText())) {
                valid = false;
                NodeUtils.setBorder(entry.getKey(), "red");
                error.append(WordUtils.capitalize(entry.getValue())).append(" must have a value.\n");
            }
        }

        for (Map.Entry<ComboBox, String> entry : comboBoxStringMap.entrySet()) {
            if (!Validators.notNull(entry.getKey().getValue())) {
                valid = false;
                NodeUtils.setBorder(entry.getKey(), "red");
                error.append(WordUtils.capitalize(entry.getValue())).append(" must have a value.\n");
            }
        }

        // ****************** Checking for valid start and end
        LocalDateTime start = LocalDateTime.of(startDatePicker.getValue(), startTimeSpinner.getValue().toLocalTime());
        LocalDateTime end = LocalDateTime.of(endDatePicker.getValue(), endTimeSpinner.getValue().toLocalTime());

        Runnable setTimeBordersRed = () -> {
            NodeUtils.setBorder(startDatePicker, "red");
            NodeUtils.setBorder(startTimeSpinner, "red");
            NodeUtils.setBorder(endDatePicker, "red");
            NodeUtils.setBorder(endTimeSpinner, "red");
        };


        if ( !start.isBefore(end)) {
            valid = false;
            setTimeBordersRed.run();

            error.append("Start date and time must be before end date and time.\n");
        } else if (!TimeUtils.isInOpenHours(start, end, ZoneId.systemDefault())) {
            valid = false;

            error.append("Appointment timeframe must be within open office hours.\n");
            setTimeBordersRed.run();
        } else if(Validators.notNull(customerComboBox.getValue())) {
            // ******************* Checking if customer already has appointment
            AppointmentDAO dbAppt = new DBAppointment();
            List<Appointment> custAppointments = dbAppt.getAppointmentsFor(customerComboBox.getValue());
            for (Appointment a : custAppointments) {
                if (!String.valueOf(a.getId()).equals(idField.getText()) && a.overlaps(start, end)) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);

                    valid = false;
                    error.append("Customer has overlapping appointment from ");
                    error.append(a.getStart().format(formatter));
                    error.append(" - ");
                    error.append(a.getEnd().format(formatter));
                    error.append(".\n");
                    setTimeBordersRed.run();

                    break;
                }
            }
        }

        return new Validators.ValidationResult(valid, error.toString().strip());
    }
}
