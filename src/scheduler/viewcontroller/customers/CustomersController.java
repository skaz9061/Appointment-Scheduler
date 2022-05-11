package scheduler.viewcontroller.customers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import scheduler.dao.AppointmentDAO;
import scheduler.dao.CustomerDAO;
import scheduler.model.Customer;
import scheduler.mysql.DBAppointment;
import scheduler.mysql.DBCustomer;
import scheduler.util.Alerts;
import scheduler.util.Router;
import scheduler.util.SearchUtils;
import scheduler.util.WordUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

/**
 * Controller for the customers screen.
 * @author Steven Kazmierkiewicz*/
public class CustomersController implements Initializable {
    public TableView<Customer> customersTable;
    public TableColumn idCol;
    public TableColumn nameCol;
    public TableColumn addressCol;
    public TableColumn postalCodeCol;
    public TableColumn divisionCol;
    public TableColumn countryCol;
    public TableColumn phoneCol;
    public TextField searchField;
    public ChoiceBox searchTypeChoice;

    private ObservableList<Customer> allCustomers;
    private Map<String, String> searchTypeMap = new LinkedHashMap<>();
    private final String searchAllStr = "Search all fields";
    private final CustomerDAO dbCustomer = new DBCustomer();

    /**
     * Called when the controller is loaded, it initializes values for the controller.
     * @param url not used
     * @param resourceBundle not used
     * */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // initialize columns
        idCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("name"));
        addressCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("address"));
        postalCodeCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("postalCode"));
        divisionCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("divisionName"));
        countryCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("countryName"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("phone"));

        allCustomers = getCustomers();

        customersTable.setItems(allCustomers);

        searchTypeMap.put(searchAllStr, "all");
        searchTypeMap.put("ID", "id");
        searchTypeMap.put("Name", "name");
        searchTypeMap.put("Address", "address");
        searchTypeMap.put("Postal Code", "postalCode");
        searchTypeMap.put("Division", "divisionName");
        searchTypeMap.put("Country", "countryName");
        searchTypeMap.put("Phone", "phone");

        searchTypeChoice.setItems(FXCollections.observableList(new ArrayList<>(searchTypeMap.keySet())));
        searchTypeChoice.getSelectionModel().selectFirst();
    }

    /**
     * Navigate to previous route.
     * @param actionEvent the event that triggered this method.
     * */
    public void goBack(ActionEvent actionEvent) {
        Router.prevRoute(Router.getStage(actionEvent));
    }

    /**
     * Retrieve customer list from the database.
     * @return the list of customers
     * */
    private ObservableList<Customer> getCustomers() {
        return FXCollections.observableList(dbCustomer.getAllCustomers());
    }

    /**
     * Event handler for searching the table view.
     * @param event the event that triggered the method
     * */
    public void search(Event event) {
        String text = searchField.getText();
        if(text.equals("")) {
            customersTable.setItems(allCustomers);
        } else {
            if (searchTypeChoice.getSelectionModel().getSelectedItem().equals(searchAllStr)) {
                customersTable.setItems(SearchUtils.search(allCustomers, text));
            } else {
                String param = searchTypeMap.get(searchTypeChoice.getSelectionModel().getSelectedItem());
                customersTable.setItems(SearchUtils.search(allCustomers, text, param));
            }
        }
    }

    /**
     * Event handler for the view appointments button.
     * @param actionEvent the event that triggered the method
     * */
    public void onViewAppointments(ActionEvent actionEvent) {
        Customer customer = (Customer) customersTable.getSelectionModel().getSelectedItem();
        Router.addRoute(getClass());

        if (customer != null) {
            Router.goToAppointments(Router.getStage(actionEvent), customer);
        } else {
            Router.goToAppointments(Router.getStage(actionEvent));
        }

    }

    /**
     * Event handler for the Add button.
     * @param actionEvent the event that triggered the method
     * */
    public void onAdd(ActionEvent actionEvent) {
        Router.addRoute(getClass());
        Router.goToAddCustomer(Router.getStage(actionEvent));
    }

    /**
     * Event handler for the Edit button.
     * @param actionEvent the event that triggered the method
     * */
    public void onEdit(ActionEvent actionEvent) {
        Customer customer = customersTable.getSelectionModel().getSelectedItem();

        if (customer == null) {
            Alerts.error("Customer Error", "Customer Error",
                    "You must select a customer to edit.");
        }
        else {
            Router.addRoute(getClass());
            Router.goToEditCustomer(Router.getStage(actionEvent), customer);
        }
    }

    /**
     * Event handler for the Delete button.
     * @param actionEvent the event that triggered the method
     * */
    public void onDelete(ActionEvent actionEvent) {
        AppointmentDAO dbAppt = new DBAppointment();
        Customer customer = customersTable.getSelectionModel().getSelectedItem();

        // Check if customer is selected *******************************
        if (customer == null) {
            Alerts.error("Customer Error", "Customer Error", "You must select a customer to delete.");
        } else {

            // Confirm deletion of customer ****************************
            boolean result = Alerts.confirm("Confirm Delete", "Are you sure?",
                    "Are you sure you want to delete Customer " + customer.getName() + "?");
            if (result) {
                var appointments = dbAppt.getAppointmentsFor(customer);
                if(appointments.size() > 0) {

                    // Confirm deletion of appointments ***************************
                    result = Alerts.warn("Warning", "Continue?",
                            customer.getName() + " still has appointments scheduled. Deleting this customer will" +
                                    " also delete all their appointments. Do you want to continue? ");
                }

                if (result) {

                    // Delete all customer appointments ***************************
                    for(var a : appointments)
                        if (!dbAppt.deleteAppointment(a))
                            Alerts.error("Database Error", "Database Error",
                                    "There was a problem deleting Appointment " + a.getId() + " from the" +
                                            "database.");

                    // If there are no appointments for the customer, delete the customer **************
                    if (dbAppt.getAppointmentsFor(customer).size() == 0) {
                        if (dbCustomer.deleteCustomer(customer)) {
                            allCustomers.remove(customer);
                            customersTable.getItems().remove(customer);

                            Alerts.info("Success", "Successfully Deleted",
                                    customer.getName() + " was successfully deleted from the database.");
                        } else {
                            Alerts.error("Database Error", "Database Error", "There was an error deleting appointment from the database.");
                        }
                    }
                }
            }
        }
    }
}
