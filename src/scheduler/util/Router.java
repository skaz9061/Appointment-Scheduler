package scheduler.util;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import scheduler.model.Appointment;
import scheduler.model.Customer;
import scheduler.viewcontroller.appointments.AddAppointmentController;
import scheduler.viewcontroller.appointments.AppointmentsController;
import scheduler.viewcontroller.appointments.EditAppointmentController;
import scheduler.viewcontroller.customers.AddCustomerController;
import scheduler.viewcontroller.customers.CustomersController;
import scheduler.viewcontroller.customers.EditCustomerController;
import scheduler.viewcontroller.login.LoginController;
import scheduler.viewcontroller.main.MainController;
import scheduler.viewcontroller.reports.ReportMenuController;
import scheduler.viewcontroller.reports.ReportViewController;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A utility that assists in routing the application to different screens.
 * @author Steven Kazmierkiewicz
 * */
public class Router {

    // tracks users path so they can go back to previous screens
    private static Deque<Class> routeStack = new ArrayDeque<>();

    // associates controllers with the methods that navigate to them
    private static Map<Class, Consumer<Stage>> controllerNav= new HashMap<>();

    /**
     * Performs initialization upon the first reference to the class.
     * */
    static {
        // Initialize controllerNav with each controller and method.
        // Can exclude create and update routes as these are terminal routes and will never be navigated back to.
        controllerNav.put(MainController.class, Router::goToMain);
        controllerNav.put(CustomersController.class, Router::goToCustomers);
        controllerNav.put(AppointmentsController.class, Router::goToAppointments);
        controllerNav.put(LoginController.class, Router::goToLogin);
        controllerNav.put(ReportMenuController.class, Router::goToReportMenu);
    }

    /**
     * Get the Stage object associated with an Event object.
     * @param event the Event to pull the Stage object from
     * @return the Stage object associated with the event
     * */
    public static Stage getStage(Event event) {
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }

    /**
     * Add controller to the route stack. If a controller that does not exist in the controller nav map
     * is attempted to be added, then the route stack is cleared and the main controller is pushed on to
     * the stack.
     * @param controller the controller to add
     * */
    public static void addRoute(Class<?> controller) {
        if (controllerNav.containsKey(controller)) {
            routeStack.push(controller);
        } else {
            routeStack.clear();
            routeStack.push(MainController.class);
        }
    }

    /**
     * Navigate to most recent route in the route stack.
     * @param stage the stage to display the screen
     * */
    public static void prevRoute(Stage stage) {
        controllerNav.get(routeStack.pop()).accept(stage);
    }

    /**
     * Clears the route stack.*/
    public static void clearRoutes() {
        routeStack.clear();
    }

    /**
     * Navigate to the login screen.
     * @param stage the stage to display the screen
     * */
    public static void goToLogin(Stage stage) {
        Parent root = null;
        try {
            root = FXMLLoader.load(Router.class.getResource("../viewcontroller/login/login.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(new Scene(root));
    }

    /**
     * Navigate to the main screen.
     * @param stage the stage to display the screen
     * */
    public static void goToMain(Stage stage) {
        Parent root = null;
        try {
            root = FXMLLoader.load(Router.class.getResource("../viewcontroller/main/main.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(new Scene(root));
    }

    /**
     * Navigate to the customers screen.
     * @param stage the stage to display the screen
     * */
    public static void goToCustomers(Stage stage) {
        Parent root = null;

        try {
            root = FXMLLoader.load(Router.class.getResource("../viewcontroller/customers/customers.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        stage.setScene(new Scene(root));
    }

    /**
     * Navigate to the addCustomer screen.
     * @param stage the stage to display the screen
     * */
    public static void goToAddCustomer(Stage stage) {
        FXMLLoader loader = new FXMLLoader(Router.class.getResource("../viewcontroller/customers/AddEditCustomer.fxml"));
        loader.setController(new AddCustomerController());
        Parent root = null;
        try {
            root = loader.load();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Navigate to the editCustomer screen.
     * @param stage the stage to display the screen
     * @param customer the customer to edit
     * */
    public static void goToEditCustomer(Stage stage, Customer customer) {
        FXMLLoader loader = new FXMLLoader(Router.class.getResource("../viewcontroller/customers/AddEditCustomer.fxml"));
        loader.setController(new EditCustomerController(customer));
        try {
            Parent root = loader.load();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Navigate to the appointments screen.
     * @param stage the stage to display the screen
     * */
    public static void goToAppointments(Stage stage) {
        Parent root = null;
        try {
            root = FXMLLoader.load(Router.class.getResource("../viewcontroller/appointments/appointments.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(new Scene(root));
    }

    /**
     * Navigate to the appointments screen for a specific customer.
     * @param stage the stage to display the screen
     * @param customer the customer
     * */
    public static void goToAppointments(Stage stage, Customer customer) {
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(Router.class.getResource("../viewcontroller/appointments/appointments.fxml"));
            root = loader.load();
            AppointmentsController controller = loader.getController();
            controller.loadCustomer(customer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(new Scene(root));
    }

    /**
     * Navigate to the addAppointment screen.
     * @param stage the stage to display the screen
     * */
    public static void goToAddAppointment(Stage stage) {
        FXMLLoader loader = new FXMLLoader(Router.class.getResource("../viewcontroller/appointments/addEditAppointment.fxml"));
        loader.setController(new AddAppointmentController());
        try {
            Parent root = loader.load();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Navigate to the editAppointment screen.
     * @param stage the stage to display the screen
     * @param appointment the appointment to edit
     * */
    public static void goToEditAppointment(Stage stage, Appointment appointment) {
        FXMLLoader loader = new FXMLLoader(Router.class.getResource("../viewcontroller/appointments/addEditAppointment.fxml"));
        loader.setController(new EditAppointmentController(appointment));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(new Scene(root));
    }

    /**
     * Navigate to the reports menu screen.
     * @param stage the stage to display the screen
     * */
    public static void goToReportMenu(Stage stage) {
        try {
            Parent root = FXMLLoader.load(Router.class.getResource("../viewcontroller/reports/reportMenu.fxml"));
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Navigate to the report view screen.
     * @param stage the stage to display the screen
     * @param data the data for the table
     * @param colFieldMap the map of column title to field name
     * @param reportName the report name
     * @param factoryFunction the lambda expression to generate the factory for the reports columns
     * */
    public static void goToReportView(Stage stage, List data, Map<String, String> colFieldMap, String reportName, Function<String, Callback> factoryFunction) {
        try {
            FXMLLoader loader = new FXMLLoader(Router.class.getResource("../viewcontroller/reports/reportView.fxml"));
            Parent root = loader.load();
            ReportViewController controller = loader.getController();
            controller.load(data, colFieldMap, reportName, factoryFunction);
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
