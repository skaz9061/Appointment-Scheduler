package scheduler.viewcontroller.appointments;

import javafx.event.ActionEvent;
import scheduler.dao.AppointmentDAO;
import scheduler.model.Appointment;
import scheduler.mysql.DBAppointment;
import scheduler.util.Alerts;
import scheduler.util.Router;
import scheduler.util.time.Time;
import scheduler.util.Validators;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

/**
 * Controller for the Edit Appointment screen.
 * */
public class EditAppointmentController extends AppointmentChange {
    private final Appointment loadAppt;

    public EditAppointmentController(Appointment appointment) {
        loadAppt = appointment;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);

        titleLabel.setText("Edit Appointment");
        idField.setText(String.valueOf(loadAppt.getId()));
        titleField.setText(loadAppt.getTitle());
        descriptionField.setText(loadAppt.getDescription());
        locationField.setText(loadAppt.getLocation());
        typeComboBox.setValue(loadAppt.getType());
        customerComboBox.setValue(loadAppt.getCustomer());
        contactComboBox.setValue(loadAppt.getContact());
        userComboBox.setValue(loadAppt.getUser());
        startDatePicker.setValue(loadAppt.getStart().toLocalDate());
        startTimeSpinner.getValueFactory().setValue(Time.from(loadAppt.getStart()));
        endDatePicker.setValue(loadAppt.getEnd().toLocalDate());
        endTimeSpinner.getValueFactory().setValue(Time.from(loadAppt.getEnd()));
    }

    @Override
    public void onSave(ActionEvent actionEvent) {
        AppointmentDAO dbAppt = new DBAppointment();

        errorLabel.setText("");

        Validators.ValidationResult result = validate();

        if (result.isValid()) {
            LocalDateTime start = LocalDateTime.of(startDatePicker.getValue(), startTimeSpinner.getValue().toLocalTime());
            LocalDateTime end = LocalDateTime.of(endDatePicker.getValue(), endTimeSpinner.getValue().toLocalTime());

            Appointment newAppt = new Appointment(
                    Integer.parseInt(idField.getText()),
                    titleField.getText(),
                    descriptionField.getText(),
                    locationField.getText(),
                    typeComboBox.getValue(),
                    start,
                    end,
                    customerComboBox.getValue(),
                    userComboBox.getValue(),
                    contactComboBox.getValue());

            if (dbAppt.updateAppointment(newAppt)) {
                Router.prevRoute(Router.getStage(actionEvent));
            } else {
                Alerts.error("Database Error", "Database Error", "There was a problem creating the customer in the database.");
            }
        }
        else
            errorLabel.setText(result.getError());
    }
}
