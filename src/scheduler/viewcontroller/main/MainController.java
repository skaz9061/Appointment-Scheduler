package scheduler.viewcontroller.main;

import javafx.event.ActionEvent;
import scheduler.model.User;
import scheduler.mysql.DBConnection;
import scheduler.util.Router;

/**
 * Controller for the Main Menu screen.
 * */
public class MainController {
    /**
     * Event handler for the customers button.
     * @param actionEvent the event that triggered the method
     * */
    public void goToCustomers(ActionEvent actionEvent) {
        Router.addRoute(getClass());
        Router.goToCustomers(Router.getStage(actionEvent));
    }

    /**
     * Event handler for the appointments button.
     * @param actionEvent the event that triggered the method
     * */
    public void goToAppointments(ActionEvent actionEvent) {
        Router.addRoute(getClass());
        Router.goToAppointments(Router.getStage(actionEvent));
    }

    /**
     * Event handler for the logout button.
     * @param actionEvent the event that triggered the method
     * */
    public void logout(ActionEvent actionEvent) {
        User.setCurrentUser(null);
        Router.clearRoutes();
        Router.goToLogin(Router.getStage(actionEvent));
    }

    /**
     * Event handler for the reports button.
     * @param actionEvent the event that triggered the method
     * */
    public void goToReports(ActionEvent actionEvent) {
        Router.addRoute(getClass());
        Router.goToReportMenu(Router.getStage(actionEvent));
    }

    /**
     * Event handler for the exit button.
     * @param actionEvent the event that triggered the method
     * */
    public void onExit(ActionEvent actionEvent) {
        DBConnection.closeConnection();
        System.exit(0);
    }
}
