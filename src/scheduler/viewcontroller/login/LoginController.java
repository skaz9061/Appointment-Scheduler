package scheduler.viewcontroller.login;

import javafx.event.Event;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import scheduler.dao.AppointmentDAO;
import scheduler.dao.UserDAO;
import scheduler.model.User;
import scheduler.mysql.DBAppointment;
import scheduler.mysql.DBUser;
import scheduler.util.*;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Controller for the Login Screen.
 * */
public class LoginController implements Initializable {
    private static final String LOG_FILENAME = "login_activity.txt";

    public TextField usernameField;
    public PasswordField passwordField;
    public Label zoneLabel;
    public Label errorLabel;
    public Label titleLable;
    public Label usernameLabel;
    public Label passwordLabel;
    public Button clearButton;
    public Button loginButton;
    public Label zoneIdLabel;
    private UserDAO dbUser = new DBUser();
    private final ResourceBundle rb = ResourceBundle.getBundle("scheduler/viewcontroller/login/login", Locale.getDefault());


    /**
     * Attempts to login with given username and password. Routes to main screen if successful.
     * @param event the event that triggered this method
     * */
    public void login(Event event) {
        try {
            User user = dbUser.login(usernameField.getText(), passwordField.getText());
            logAttempt(user.getName(), true);
            User.setCurrentUser(user);
            checkForAppointments();
            Stage stage = Router.getStage(event);
            Router.goToMain(stage);
        } catch (Exceptions.UserException e) {
            //e.printStackTrace();
            logAttempt(usernameField.getText(), false);
            errorLabel.setText(e.getMessage());
        }
    }

    /**
     * Clears all fields and error text.
     * @param event the event that triggered this method
     * */
    public void clear(Event event) {
        usernameField.setText("");
        passwordField.setText("");
        errorLabel.setText("");
    }

    /**
     * Called when the controller is loaded, it initializes values for the controller.
     * @param url not used
     * @param resourceBundle not used
     * */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        zoneLabel.setText(ZoneId.systemDefault().toString());

        if (Locale.getDefault().getLanguage() == "fr") {
            titleLable.setText(WordUtils.capitalizeAll(rb.getString("login")));
            zoneIdLabel.setText(WordUtils.capitalizeAll(rb.getString("zone")));
            usernameLabel.setText(WordUtils.capitalizeAll(rb.getString("username")));
            passwordLabel.setText(WordUtils.capitalizeAll(rb.getString("password")));
            clearButton.setText(WordUtils.capitalizeAll(rb.getString("clear")));
            loginButton.setText(WordUtils.capitalizeAll(rb.getString("login")));
        }

    }

    /**
     * Checks if there are any appointments starting within 15 minutes and displays message to user. Lambda expression
     * is used here to filter the appointments and get only those in the desired timeframe.
     * */
    private void checkForAppointments() {
        AppointmentDAO dbAppt = new DBAppointment();
        var appts = dbAppt.getAllAppointments();
        LocalDateTime now = LocalDateTime.now();

        appts = appts.stream().filter(a -> {
            boolean inFuture = a.getStart().isAfter(now) || a.getStart().equals(now);
            boolean lessThan15 = a.getStart().isBefore(now.plusMinutes(15)) || a.getStart().equals(now.plusMinutes(15));
            return inFuture && lessThan15;
        }).collect(Collectors.toList());

        if (appts.size() == 0) {
            Alerts.info(rb.getString("apptCheckTitle"), rb.getString("noApptHeader"),
                    String.format(rb.getString("noApptMessage"), 15));
        } else {
            StringBuilder apptStr = new StringBuilder();

            for(var appt : appts) {
                apptStr.append("\n");
                apptStr.append(rb.getString("id"));
                apptStr.append(": ");
                apptStr.append(appt.getId());
                apptStr.append("     ");
                apptStr.append(rb.getString("user"));
                apptStr.append(": ");
                apptStr.append(appt.getUser().getName());
                apptStr.append("     ");
                apptStr.append(rb.getString("start"));
                apptStr.append(": ");
                apptStr.append(appt.getStart().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT)));
            }

            Alerts.info(rb.getString("apptCheckTitle"), String.format(rb.getString("yesApptHeader"), appts.size()), apptStr.toString().strip());
        }
    }

    /**
     * Adds the login attempt to the log file.
     * @param user the attempted username
     * @param success the result of the login attempt
     * */
    public void logAttempt(String user, boolean success) {
        var now = LocalDateTime.now();
        String successStr = success ? "SUCCESS" : "FAILURE";

        StringBuilder line = new StringBuilder();
        line.append(now).append(" ").append(successStr);
        line.append(" | USER: ").append(user);

        FileUtils.write(LOG_FILENAME, line.toString());
    }
}
