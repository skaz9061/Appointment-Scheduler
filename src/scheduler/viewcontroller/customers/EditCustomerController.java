package scheduler.viewcontroller.customers;

import javafx.event.ActionEvent;
import scheduler.dao.CustomerDAO;
import scheduler.model.Customer;
import scheduler.mysql.DBCustomer;
import scheduler.util.Alerts;
import scheduler.util.Router;
import scheduler.util.Validators;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the edit customer screen.
 * */
public class EditCustomerController extends CustomerChange {
    private Customer origCustomer;
    private final CustomerDAO dbCust = new DBCustomer();

    public EditCustomerController(Customer customer){
        this.origCustomer = customer;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);

        titleLabel.setText("Edit Customer");

        idField.setText(String.valueOf(origCustomer.getId()));
        nameField.setText(origCustomer.getName());
        addressField.setText(origCustomer.getAddress());
        postalCodeField.setText(origCustomer.getPostalCode());
        phoneField.setText(origCustomer.getPhone());

        countryBox.getSelectionModel().select(origCustomer.getDivision().getCountry());
        divisionBox.getSelectionModel().select(origCustomer.getDivision());
    }

    @Override
    public void onSave(ActionEvent actionEvent) {
        errorLabel.setText("");
        Validators.ValidationResult result = validate();


        if (result.isValid()) {
            Customer newCust = new Customer(
                    Integer.parseInt(idField.getText()),
                    nameField.getText(),
                    addressField.getText(),
                    postalCodeField.getText(),
                    phoneField.getText(),
                    divisionBox.getValue());

            if (dbCust.updateCustomer(newCust)) {
                Router.prevRoute(Router.getStage(actionEvent));
            } else {
                Alerts.error("Database Error", "Database Error", "There was a problem creating the customer in the database.");
            }
        } else {
            errorLabel.setText(result.getError());
        }
    }
}
