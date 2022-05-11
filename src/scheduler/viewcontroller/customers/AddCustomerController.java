package scheduler.viewcontroller.customers;

import javafx.event.ActionEvent;
import scheduler.dao.CustomerDAO;
import scheduler.mysql.DBCustomer;
import scheduler.util.Alerts;
import scheduler.util.Router;
import scheduler.util.Validators;

/**
 * Controller for the Add Customer screen.
 * */
public class AddCustomerController extends CustomerChange {
    CustomerDAO dbCust = new DBCustomer();

    @Override
    public void onSave(ActionEvent actionEvent) {
        errorLabel.setText("");

        Validators.ValidationResult result = validate();

        if (result.isValid())
            if (dbCust.createCustomer(
                    nameField.getText(),
                    addressField.getText(),
                    postalCodeField.getText(),
                    phoneField.getText(),
                    divisionBox.getValue())) {
                Router.prevRoute(Router.getStage(actionEvent));
            } else {
                Alerts.error("Database Error", "Database Error", "There was a problem creating the customer in the database.");
            }
        else
            errorLabel.setText(result.getError());
    }
}
