package scheduler.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;

import java.util.Optional;

/**
 * Shows different types of alerts and retrieves feedback from the user.
 * */
public class Alerts {
    /**
     * Displays an Alert with an AlertType of ERROR.
     * @param title the title of the Alert
     * @param header the header of the Alert
     * @param message the message of the Alert
     * */
    public static void error(String title, String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert(alert, title, header, message);
    }

    /**
     * Displays an Alert with an AlertType of CONFIRMATION.
     * @param title the title of the Alert
     * @param header the header of the Alert
     * @param message the message of the Alert
     * @return true if the OK button was clicked
     * */
    public static boolean confirm(String title, String header, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        Optional<ButtonType> buttonType = alert(alert, title, header, message);

        return buttonType.isPresent() && buttonType.get() == ButtonType.OK;
    }

    /**
     * Displays an Alert with an AlertType of WARNING. It uses a Confirmation Alert with a
     * Warning graphic.
     * @param title the title of the Alert
     * @param header the header of the Alert
     * @param message the message of the Alert
     * @return true if the OK button was clicked
     * */
    public static boolean warn(String title, String header, String message) {
        ImageView icon = new ImageView("/scheduler/util/warn.png");
        icon.setPreserveRatio(true);
        icon.setFitWidth(50);

        // Using confirmation to have an OK and CANCEL button
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        // Change graphic to the Warning graphic
        alert.setGraphic(icon);
        Optional<ButtonType> buttonType = alert(alert, title, header, message);

        return buttonType.isPresent() && buttonType.get() == ButtonType.OK;
    }

    /**
     * Displays an Alert with an AlertType of INFORMATION.
     * @param title the title of the Alert
     * @param header the header of the Alert
     * @param message the message of the Alert
     * */
    public static void info(String title, String header, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert(alert, title, header, message);
    }

    /**
     * Helper method for showing an alert.
     * @param alert the alert
     * @param title the title
     * @param header the header
     * @param message the message
     * @return the button chosen by the user
     * */
    private static Optional<ButtonType> alert(Alert alert, String title, String header, String message) {
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        return alert.showAndWait();
    }
}
