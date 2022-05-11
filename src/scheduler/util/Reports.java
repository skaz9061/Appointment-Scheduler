package scheduler.util;

import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.stage.Window;
import scheduler.model.Appointment;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides methods to help in generating reports.
 * */
public class Reports {
    /**
     * Gets a formatted date prefix for a file name.
     * @return the formatted date
     * */
    private static String getTimeStampString() {
        return LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE).replace('-', '_');
    }

    /**
     * Creates the file object of the report. Appends an appropriate number to the file name if the file already exists.
     * @param dir the directory to save the file
     * @param report the raw filename for the report
     * @param ext the file extension
     * @return the File object
     * */
    public static File generateFile(File dir, String report, String ext) {
        String base = getTimeStampString() + "_" + report;
        File file = dir.toPath().resolve(base + "." + ext).toFile();
        int i = 0;

        while (file.exists())
            file = dir.toPath().resolve(base + ++i + "." + ext).toFile();

        return file;
    }

    /**
     * Prepares the data for a file with a table format by converting the list of objects into a list of rows
     * containing a list of cell values. The first lambda expression maps the helps convert the strings to ints in the list.
     * The second lambda expression maps each object to a list of string values of its fields.
     * @param list the list of objects
     * @param fieldNames the field names for each column
     * @param <T> the type of object in the list
     * @return the rows of the report
     * */
    public static <T> List<List<String>> fromObject(List<T> list, List<String> fieldNames) {
        if (Validators.isInteger(fieldNames.get(0))) {
            // first lambda
            List<Integer> indexOrder = fieldNames.stream().map(s -> Integer.parseInt(s)).collect(Collectors.toList());
            return fromList((List<List<T>>) list, indexOrder);
        }

        // second lambda
        return list.stream().map(a -> {
            List<String> row = new ArrayList<>();
            for(var field : fieldNames) {
                try {
                    Method method = ReflectUtils.getter(a, field);
                    row.add(method.invoke(a).toString());
                } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            return row;
        }).collect(Collectors.toList());
    }

    /**
     * Prepares the data for a file with a table format by converting the list of lists into a list of rows
     * containing a list of cell values. The lambda expression converts each list object into a list of string values of
     * its elements, in the order specified by indexOrder.
     * @param list the list of objects
     * @param indexOrder the index of the value for each column
     * @param <T> the type of object in the list
     * @return the rows of the report
     * */
    public static <T> List<List<String>> fromList(List<List<T>> list, List<Integer> indexOrder) {
        return list.stream().map(a -> {
            List<String> row = new ArrayList<>();
            for(var index : indexOrder) {
                row.add(a.get(index).toString());
            }

            return row;
        }).collect(Collectors.toList());
    }

    /**
     * Exports the contents of a TableView as a report in a csv file. The lambda expression converts the list of table columns
     * into a list of the text value of all the table columns.
     * @param reportName the raw filename of the report
     * @param tv the TableView
     * @param fields the list of field names
     * @param stage the current stage
     * */
    public static void export(String reportName, TableView tv, List<String> fields, Stage stage) {
        ObservableList<Appointment> showing = tv.getItems();

        if (showing.size() == 0) {
            Alerts.info("Report Canceled", "No Data to Report",
                    "There is no data that meets the criteria for the report. The report has been canceled.");

            return;
        }

        var cols = ((ObservableList<TableColumn>) tv.getColumns());
        List<String> headers = cols.stream().map(tc -> tc.getText()).collect(Collectors.toList());
        List<List<String>> rows = Reports.fromObject(showing, fields);
        Alerts.info("Choose Directory","Choose Directory","Choose the directory to save your report.");
        File dir = FileUtils.chooseDir(stage);
        if (dir != null) {
            File file = Reports.generateFile(dir, reportName, "csv");
            if (FileUtils.createCSV(file, headers, rows))
                Alerts.info("Success", "Report Successful", "The report was generated successfully.");
            else
                Alerts.error("Error", "Report Error", "There was a problem generating the report.");
        }
    }
}
