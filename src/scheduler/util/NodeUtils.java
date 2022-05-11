package scheduler.util;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * Provides methods for working with JavaFX Node objects.
 * */
public class NodeUtils {
    /**
     * There are two lambda expressions here: ListValueFactory is a lambda expression that creates a CellValueFactory
     * for a TableColumn of a List object. This lambda expression allows the use of a List object as a row for a
     * TableView. It accepts the index of the desired value as a String and returns the CellValueFactory, which
     * itself is a lambda expression to get the value to display in the cell.
     * */
    public static final Function<String, Callback> ListValueFactory = s -> {
        Callback<TableColumn.CellDataFeatures<List<String>,String>, ObservableValue<String>> factory = (TableColumn.CellDataFeatures<List<String>,String> e) -> {
            return new ReadOnlyStringWrapper(e.getValue().get(Integer.parseInt(s)));
        };
        return factory;
    };

    /**
     * Sets the border of a node to a specific color.
     * @param color the color
     * @param node the node
     * */
    public static void setBorder(Node node, String color) {
        node.setStyle(String.format("-fx-border-color: %s;", color));
    }

    /**
     * Sets the border of all nodes in an array to a specific color.
     * @param color the color
     * @param nodes the array of nodes
     * */
    public static void setBorder(Node[] nodes, String color) {
        setBorder(Arrays.asList(nodes), color);
    }

    /**
     * Sets the border of all nodes in a list to a specific color.
     * @param color the color
     * @param nodeList the list of nodes
     * */
    public static void setBorder(List<Node> nodeList, String color) {
        for(var node : nodeList)
            setBorder(node, color);
    }

    /**
     * Removes the border from a node.
     * @param node the node
     * */
    public static void clearBorder(Node node) {
        node.setStyle("-fx-border-color: none");
    }

    /**
     * Removes the border from multiple nodes.
     * @param nodes the nodes
     * */
    public static void clearBorder(Node[] nodes) {
        clearBorder(Arrays.asList(nodes));
    }

    /**
     * Removes the border from multiple nodes.
     * @param nodeList the nodes
     * */
    public static void clearBorder(List<Node> nodeList) {
        for(var node: nodeList)
            clearBorder(node);
    }

    /**
     * Removes the border and clears the text input from the TextInputControl.
     * @param node the TextInputControl
     * */
    public static void clear(TextInputControl node) {
        clearBorder(node);
        node.clear();
    }

    /**
     * Removes the border and clears the selection from the ComboBox.
     * @param node the ComboBox
     * */
    public static void clear(ComboBox node) {
        clearBorder(node);
        node.getSelectionModel().clearSelection();
    }

    /**
     * Removes the border and sets the Spinner to a specified value.
     * @param <T> the type of the objects in the Spinner
     * @param node the Spinner
     * @param clearVal the value to set
     * */
    public static <T> void clear(Spinner<T> node, T clearVal) {
        clearBorder(node);
        node.getValueFactory().setValue(clearVal);
    }

    /**
     * Removes the border and sets the value of the DatePicker to today's date.
     * @param node the DatePicker
     * */
    public static void clear(DatePicker node) {
        clearBorder(node);
        node.setValue(LocalDate.from(LocalDateTime.now()));
    }

    /**
     * Get the content of a tab pane.
     * @param tabPane the tab pane
     * @return the content
     * */
    public static Node getTabPaneContent(TabPane tabPane) {
        return tabPane.getSelectionModel().getSelectedItem().getContent();
    }

    /**
     * Find the first child of a parent with the specified class.
     * @param parent the parent
     * @param clazz the class
     * @param <T> the type of the Class object, should be the same as the class
     * @return the child
     * */
    @SuppressWarnings("unchecked")
    public static <T> T findChild(Parent parent, Class<T> clazz) {
        for(Node child : parent.getChildrenUnmodifiable())
            if(child.getClass().equals(clazz))
                return (T) child;

        return null;
    }
}
