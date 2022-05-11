package scheduler.util;

import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides methods for working with files.
 * */
public class FileUtils {

    /**
     * Writes a file to the file system. If the file exists, the lines are appended to the original file.
     * @param lines the lines to write to the file
     * @param filename the filename, including the path
     * @return true if the operation was successful
     * */
    public static boolean write(String filename, List<String> lines) {
        try (var fos = new FileOutputStream(filename, true);
             var bos = new BufferedOutputStream(fos);
             var pw = new PrintWriter(bos, true)) {
            for( var line : lines)
                pw.println(line);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Writes a file to the file system. If the file exists, the line is appended to the original file.
     * @param line the line to write to the file
     * @param filename the filename, including the path
     * @return true if the operation was successful
     * */
    public static boolean write(String filename, String line) {
        try (var fos = new FileOutputStream(filename, true);
             var bos = new BufferedOutputStream(fos);
             var pw = new PrintWriter(bos, true)) {
            pw.println(line);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Writes a file to the file system. If the file exists, the lines are appended to the original file.
     * @param lines the lines to write to the file
     * @param f the file
     * @return true if the operation was successful
     * */
    public static boolean write(File f, List<String> lines) {
        return write(f.getPath(), lines);
    }

    /**
     * Writes a file to the file system. If the file exists, the line is appended to the original file.
     * @param line the line to write to the file
     * @param f the file
     * @return true if the operation was successful
     * */
    public static boolean write(File f, String line) {
        return write(f.getPath(), line);
    }

    /**
     * Opens the system specific file picker for choosing directories.
     * @param window the current window, which may be a stage
     * @return the chosen directory
     * */
    public static File chooseDir(Window window) {
        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle("Choose the directory to save the file");
        return dc.showDialog(window);
    }

    /**
     * Creates a CSV file in the file system. if the file already exists, the lines are appended to the end of the file.
     * @param file the file
     * @param headers the headers
     * @param rows the values for each cell in each row
     * @return true if the operation was successful
     * */
    public static boolean createCSV(File file, List<String> headers, List<List<String>> rows) {
        List<String> newRows = new ArrayList<>();
        newRows.add(String.join(",", headers));

        for(var row : rows)
            newRows.add(String.join(",", row));

        return write(file, newRows);
    }
}
