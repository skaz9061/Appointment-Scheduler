package scheduler.viewcontroller.reports;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import scheduler.util.Reports;
import scheduler.util.Router;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Controller for the Report View screen.
 * */
public class ReportViewController {
    public TableView table;
    public Label reportNameLabel;
    private List<String> fieldNames = new ArrayList<>();

    /**
     * Event Handler for export button.
     * @param actionEvent the event that triggered the method
     * */
    public void onExport(ActionEvent actionEvent) {
        Reports.export(getReportName(), table, fieldNames, Router.getStage(actionEvent));
    }

    /**
     * Parses the report name label text to an appropriate raw filename.
     * @return the raw filename
     * */
    private String getReportName() {
        String value = reportNameLabel.getText().replace(", ", " ").toLowerCase();
        return String.join("_", value.split(" "));
    }

    /**
     * Event handler for the back button. It returns to the previous route.
     * @param actionEvent the event that triggered the method
     * */
    public void onBack(ActionEvent actionEvent) {
        Router.prevRoute(Router.getStage(actionEvent));
    }

    /**
     * Loads the report data into the TableView.
     * @param <T> the type of object in the list of report data
     * @param data the report data
     * @param colFieldMap the column name / field name map
     * @param reportName the report name
     * */
    public <T> void load(List<T> data, Map<String, String> colFieldMap, String reportName) {
        load(data, colFieldMap, reportName, PropertyValueFactory::new);
    }

    /**
     * Loads the report data into the TableView.
     * @param <T> the type of object in the list of report data
     * @param data the report data
     * @param colFieldMap the column name / field name map
     * @param reportName the report name
     * @param factoryFunction function for generating the CellValueFactory
     * */
    public <T> void load(List<T> data, Map<String, String> colFieldMap, String reportName, Function<String, Callback> factoryFunction) {
        reportNameLabel.setText(reportName);

        for (var e : colFieldMap.entrySet()) {
            TableColumn<T, String> tc = new TableColumn<>(e.getKey());
            tc.setCellValueFactory(factoryFunction.apply(e.getValue()));

            table.getColumns().add(tc);
            fieldNames.add(e.getValue());
        }

        table.setItems(FXCollections.observableList(data));
    }
}
