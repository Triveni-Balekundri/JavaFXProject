package project.in.myjavafx;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.*;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;


//This class is responsible to help us browser through the file system in our pc(system) in order to choose our desired files.
public class ExcelToPostgresApp extends Application {

    //This will be used to set any messages on the main Application
    @FXML
    private Label dataStatus;

    @Override
    public void start(Stage primaryStage) {
        //Creating a file chooser dialog
        FileChooser fileChooser = new FileChooser();
        //Setting the title of the dialog box
        fileChooser.setTitle("Open Excel File");
        //Setting the filter for Excel files
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        //Showing/Displaying the file chooser dialog
        File selectedFile = fileChooser.showOpenDialog(primaryStage);

        // Checking if a file was selected.
        if (selectedFile != null) {
            System.out.println("Selected file: " + selectedFile.getAbsolutePath());
            //Call the method to import data from Excel to PostgreSQL
            importDataFromExcel(selectedFile);
        }
    }

    public static void main(String[] args) {
        // Launch the JavaFX application
        launch(args);
    }

    private void importDataFromExcel(File file) {
        // These variables store the following PostgreSQL database connection details
            String jdbcURL = "jdbc:postgresql://localhost:5432/KLSGit";
            String username = "postgres";
            String password = "triveni";

            //The DataFormatter helps in reading and formatting the cell values into Java-friendly formats.
            DataFormatter formatter = new DataFormatter();
            //Here we are retrieving the AbsolutePath of the file and storing it in a String excelFilePath
            String excelFilePath = file.getAbsolutePath();

            //Here we are passing the excel file path into the FILE INPUT STREAM
            try (FileInputStream inputStream = new FileInputStream(excelFilePath);

                 //Here we are Opening an Excel XSSFworkbook from an input stream
                 Workbook workbook = new XSSFWorkbook(inputStream);
                 // Establishing a connection to a PostgreSQL database
                 Connection connection = DriverManager.getConnection(jdbcURL, username, password)) {
                System.out.println("Connected to POSTGRES SQL");

                //Here we are accessing the first sheet of the workbook
                Sheet sheet = workbook.getSheetAt(0);

                //Here we are iterating through each row of the sheet
                for (Row row : sheet) {

                    //Here we are getting/extracting cells from the current row [0]
                    Cell cell1 = row.getCell(0);
                    //Here we are getting/extracting cells from the current row [1]
                    Cell cell2 = row.getCell(1);
                    //Here we are getting/extracting cells from the current row [2]
                    Cell cell3 = row.getCell(2);
                    //Here we are getting/extracting cells from the current row [3]
                    Cell cell4 = row.getCell(3);
                    //Here we are getting/extracting cells from the current row [4]
                    Cell cell5 = row.getCell(4);
                    //Here we are getting/extracting cells from the current row [5]
                    Cell cell6 = row.getCell(5);

                    // Assuming two columns in Excel: col1 and col2

                    //Here we are getting/extracting values from cells assuming they are strings or formatted values
                    String col1 = cell1.getStringCellValue();
                    String col2 = formatter.formatCellValue(cell2);

                    String col3 = formatter.formatCellValue(cell3); //.getStringCellValue();
                    String col4 = cell4.getStringCellValue();

                    String col5 = cell5.getStringCellValue();
                    String col6 = cell6.getStringCellValue();

                    //Here we are creating a SQL statement to insert data into a table named 'student1'
                    String sql = "INSERT INTO student1(usn, sname,sem,branch,gender,participation) VALUES (?,?,?,?,?,?)";

                    //Here we are executing the SQL statement with the help of prepared statement
                    try (PreparedStatement statement = connection.prepareStatement(sql)) {
                        //Here we are setting the parameters for the prepared statement according to the columns
                        statement.setString(1, col1);
                        statement.setString(2, col2);
                        statement.setInt(3, Integer.parseInt(col3));
                        statement.setString(4, col4);
                        statement.setString(5, col5);
                        statement.setString(6, col6);
                        //Here we will be executing the SQL statement
                        statement.executeUpdate();
                        System.out.println("Data is imported...");
                        //dataStatus.setText("Data imported Succesfully...");
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                //dataStatus.setText("Already Exists..");
            }
        }
        // Implement the method to read Excel and import data to PostgreSQL
}
