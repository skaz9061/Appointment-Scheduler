package scheduler.viewcontroller.appointments;

import javafx.event.ActionEvent;
import scheduler.dao.AppointmentDAO;
import scheduler.model.User;
import scheduler.mysql.DBAppointment;
import scheduler.util.Alerts;
import scheduler.util.Router;
import scheduler.util.Validators;

import java.time.LocalDateTime;

/**
 * Controller for the Add Appointment screen.
 * */
public class AddAppointmentController extends AppointmentChange {
    @Override
    public void onSave(ActionEvent actionEvent) {
        AppointmentDAO dbAppt = new DBAppointment();

        errorLabel.setText("");

        Validators.ValidationResult result = validate();

        if (result.isValid()) {
            LocalDateTime start = LocalDateTime.of(startDatePicker.getValue(), startTimeSpinner.getValue().toLocalTime());
            LocalDateTime end = LocalDateTime.of(endDatePicker.getValue(), endTimeSpinner.getValue().toLocalTime());

            if (dbAppt.createAppointment(
                    titleField.getText(),
                    descriptionField.getText(),
                    locationField.getText(),
                    typeComboBox.getValue(),
                    customerComboBox.getValue(),
                    contactComboBox.getValue(),
                    userComboBox.getValue(),
                    start,
                    end)) {
                Router.prevRoute(Router.getStage(actionEvent));
            } else {
                Alerts.error("Database Error", "Database Error", "There was a problem creating the customer in the database.");
            }
        }
        else
            errorLabel.setText(result.getError());
    }
}
