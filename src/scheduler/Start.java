package scheduler;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import scheduler.mysql.DBConnection;
import scheduler.util.WordUtils;

import java.io.IOException;
import java.util.*;

/**
 * The starting class of the application.
 * */
public class Start extends Application {

    /**
     * Loads the initial screen of the application.
     * @param primaryStage the primary stage
     * @throws IOException if there is a problem loading the resource for the view
     * */
    @Override
    public void start(Stage primaryStage) throws IOException {
        ResourceBundle rb = ResourceBundle.getBundle("scheduler/viewcontroller/login/login", Locale.getDefault());

        Parent root = FXMLLoader.load(getClass().getResource("viewcontroller/login/login.fxml"));
        primaryStage.setTitle(WordUtils.capitalizeAll(rb.getString("appointmentScheduler")));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    /**
     * Entry method of the application.
     * @param args command line arguments, not used
     * */
    public static void main(String[] args) {
        // ************* UNCOMMENT THE FOLLOWING LINES TO TEST DIFFERENT LOCALE OR TIMEZONE
        //Locale.setDefault(Locale.FRANCE);
        //TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("Europe/Paris")));

        DBConnection.startConnection();
        launch(args);
        DBConnection.closeConnection();
    }
}
