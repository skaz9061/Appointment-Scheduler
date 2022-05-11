package scheduler.viewcontroller.customers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import scheduler.dao.CountryDAO;
import scheduler.dao.CustomerDAO;
import scheduler.dao.DivisionDAO;
import scheduler.model.Country;
import scheduler.model.Division;
import scheduler.mysql.DBCountry;
import scheduler.mysql.DBCustomer;
import scheduler.mysql.DBDivision;
import scheduler.util.Router;
import scheduler.util.NodeUtils;
import scheduler.util.Validators;
import scheduler.util.WordUtils;

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Abstract class for the controllers of a Customer change screen, either adding or editing.
 * */
public abstract class CustomerChange implements Initializable {
    public Label titleLabel;
    public ComboBox<Country> countryBox;
    public ComboBox<Division> divisionBox;
    public TextField idField;
    public TextField nameField;
    public TextField addressField;
    public TextField postalCodeField;
    public TextField phoneField;
    public Label errorLabel;

    private ObservableList<Division> allDivisions = FXCollections.observableArrayList();
    private final DivisionDAO dbDivision = new DBDivision();
    private final CountryDAO dbCountry = new DBCountry();
    private final CustomerDAO dbCustomer = new DBCustomer();

    /**
     * Called when the controller is loaded, it initializes values for the controller.
     * @param url not used
     * @param resourceBundle not used
     * */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        allDivisions = FXCollections.observableList(dbDivision.getAllDivisions());
        ObservableList<Country> allCountries = FXCollections.observableList(dbCountry.getAllCountries());

        divisionBox.setItems(allDivisions);
        countryBox.setItems(allCountries);

    }

    /**
     * Event handler for the save button.
     * @param actionEvent the event that triggered the method
     * */
    public abstract void onSave(ActionEvent actionEvent);

    /**
     * Event handler for the clear button.
     * @param actionEvent the event that triggered the method
     * */
    public void onClear(ActionEvent actionEvent) {
        clear();
    }

    /**
     * Event handler for the cancel button. It returns to the previous route.
     * @param actionEvent the event that triggered the method
     * */
    public void onCancel(ActionEvent actionEvent) {
        Router.prevRoute(Router.getStage(actionEvent));
    }

    /**
     * Clears the form to its default state.
     * */
    protected void clear() {
        NodeUtils.clear(nameField);
        NodeUtils.clear(addressField);
        NodeUtils.clear(phoneField);
        NodeUtils.clear(postalCodeField);
        NodeUtils.clear(divisionBox);
        NodeUtils.clear(countryBox);
        errorLabel.setText("");
        divisionBox.setItems(allDivisions);
    }

    /**
     * Event handler for the country combo box. The lambda expression filters the divisions by the selected
     * country.
     * @param actionEvent the event that triggered the method
     * */
    public void onSelectCountry(ActionEvent actionEvent) {
        Country country = countryBox.getValue();
        divisionBox.setItems(allDivisions.filtered(d -> d.getCountry().equals(country)));

        if (divisionBox.getValue() == null)
            divisionBox.getSelectionModel().selectFirst();
    }

    /**
     * Event handler for the division combo box.
     * @param actionEvent the event that triggered the method
     * */
    public void onSelectDivision(ActionEvent actionEvent) {
        Division division = divisionBox.getValue();
        if (division != null)
            countryBox.setValue(division.getCountry());
    }

    /**
     * Validates all the fields on the form.
     * @return the result of the validation
     * */
    protected Validators.ValidationResult validate() {
        StringBuilder error = new StringBuilder();
        Map<TextField, String> textFieldStringMap = new LinkedHashMap<>();
        textFieldStringMap.put(nameField, "name");
        textFieldStringMap.put(addressField, "address");
        textFieldStringMap.put(postalCodeField, "postal code");
        textFieldStringMap.put(phoneField, "phone number");

        Map<ComboBox, String> comboBoxStringMap = new LinkedHashMap<>();
        comboBoxStringMap.put(divisionBox, "division");
        comboBoxStringMap.put(countryBox, "country");

        boolean valid = true;

        NodeUtils.clearBorder(textFieldStringMap.keySet().toArray(new TextField[0]));
        NodeUtils.clearBorder(comboBoxStringMap.keySet().toArray(new ComboBox[0]));

        for (Map.Entry<TextField, String> entry : textFieldStringMap.entrySet()) {
            if (!Validators.notEmpty(entry.getKey().getText())) {
                valid = false;
                NodeUtils.setBorder(entry.getKey(), "red");
                error.append(WordUtils.capitalize(entry.getValue())).append(" must have a value.\n");
            }
        }

        for (Map.Entry<ComboBox, String> entry : comboBoxStringMap.entrySet()) {
            if (!Validators.notNull(entry.getKey().getValue())) {
                valid = false;
                NodeUtils.setBorder(entry.getKey(), "red");
                error.append(WordUtils.capitalize(entry.getValue())).append(" must have a value.\n");
            }
        }

        return new Validators.ValidationResult(valid, error.toString().strip());
    }
}
