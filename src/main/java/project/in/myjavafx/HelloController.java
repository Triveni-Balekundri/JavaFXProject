package project.in.myjavafx;

import java.io.*;
import java.sql.*;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.*;

//This class is responsible to implement the Initializable interface for JavaFX
// This class is responsible for managing:
// the user interface and interactions related to student data. It handles database operations:
// such as inserting, updating, deleting, and viewing student records, as well as importing and
// exporting data to and from Excel files. Additionally, it manages user input from various
// GUI components like text fields, checkboxes, radio buttons, and choice boxes.
public class HelloController implements Initializable {

    //Connection object initialized as NULL
    Connection myCon=null;

    //String variable to store the gender of the user
    String gender;

    //String variable to track the participation
    String participation="";

    //@FXML
    //private ImageView collegeImg;

    //fxml variable for NCC checkbox
    @FXML
    private CheckBox ncc;
    //fxml variable  for NSS checkbox
    @FXML
    private CheckBox nss;
    //fxml variable  for Rotray Club checkbox
    @FXML
    private CheckBox rc;

    //fxml variable  for displaying welcome text
    @FXML
    private Label welcomeText;

    //boolean value intialized as true for first time initializing the table
    Boolean firstTime=true;

    //fxml variable for the add student button
    @FXML
    private Button addStudent;

    //fxml variable for displaying the data status
    @FXML
    private Label dataStatus;

    //fxml variable for displaying messages
    @FXML
    private Label msg;


    //fxml variable for student USN number
    @FXML
    private TextField stdUsn;

    //fxml variable for gender radioButtons here i.e radioButton1 (Male)
    @FXML
    private RadioButton rb1;

    //fxml variable for gender radioButtons here i.e radioButton2 (Female)
    @FXML
    private RadioButton rb2;

    // FXML button to write data to Excel
    @FXML
    private Button writeToExcel;

    // FXML text fields for student Name
    @FXML
    private TextField stdName;

    // FXML text fields for student Semester
    @FXML
    private TextField stdSem;

    // FXML text fields for student Branch
    @FXML
    private TextField stdBranch;

    // FXML TableView to display all students
    @FXML
    private TableView<Student> allStudents;

    // FXML Button to view all Students Data
    @FXML
    private Button viewStudent;


    //FXML choice boxes for selecting semester and branch
    @FXML
    private ChoiceBox<Integer> mySemComboBox;
    @FXML
    private ChoiceBox<String> myBranchComboBox;

    //FXML button for logout functionality
    @FXML
    private Button logout;

    //Variables to hold selected semester and branch
    int mysem;
    String mybranch;

    //This method is responsible for Logging out from the Main application
    public void onLogout(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login-page.fxml"));
        Parent mainView = loader.load();
        Stage stage = (Stage) stdUsn.getScene().getWindow();

        Scene myscene=new Scene(mainView);
        String css = this.getClass().getResource("/application.css").toExternalForm();
        myscene.getStylesheets().add(css);
        stage.setScene(myscene);

        stage.setFullScreenExitHint("");
        stage.setFullScreen(true);
        //stage.setMaximized(true);


        dataStatus.setText("Logging Out");
    }

    //This method is responsible to set up the GUI components and database connection
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //This is an array which shows the available semesters
        Integer semesters[]={1,2,3,4,5,6};
        //This is an array which shows the available branches
        String branches[]={"CSE","MCA","BCA","MECH","CIVIL","EC","EE"};

        //Here we are populating semester choice box
        mySemComboBox.getItems().addAll(semesters);
        //Here we are populating branch choice box
        myBranchComboBox.getItems().addAll(branches);

        //Here we are setting the action for semester selection for SEM Combo-Box
        mySemComboBox.setOnAction(this::getSemester);
        //Here we are setting the action for Branch selection for Branch Combo-Box
        myBranchComboBox.setOnAction(this::getBranch);
        //  collegeImage.getImage(Boolean.parseBoolean("../../../images/GSS.jpg"));

        //collegeImg.setImage(myImage);

        //Default gender selection
        gender="Male";

        //Here we are establishing the databas connection
        try {
            myCon = DataBaseConnect.getConnection();
        } catch (SQLException e) {
            //Handling the SQL Exception
            throw new RuntimeException(e);
        }
    }

    //This method is responsible to update the student data in the database
    public void upDateData() throws SQLException {
        //Here,we are getting the value from the Student USN text box and storing it in a String
        String usn=stdUsn.getText();
        //Here,we are getting the value from the Student Name text box and storing it in a String
        String name=stdName.getText();

        int sem=mysem;
        String branch=mybranch;


        String gender="Male";

        //If condition to set the correct gender option opted by the user for male/female
        if(rb1.isSelected())
            gender="Male";
        else if(rb2.isSelected())
            gender="Female";

        String otherActivity="";

        //If condition to check the user input and opt the checkBox according to it.
        if(nss.isSelected())
            otherActivity+=" NSS ";
        if(ncc.isSelected())
            otherActivity+=" NCC ";
        if(rc.isSelected())
            otherActivity+="Rotary Club";

        //Initialized the PreparedStatement to execute the code
        PreparedStatement statement=null;
        //This is the query to update the the data:(USN,Student_name,gender,semester,branch,participation where USN is the key) in the student1 table
        String sqlStatement="update student1 set usn=?, sname=?, gender=?, sem=?, branch=?, participation=? where usn=?";
        //Initializing the query/sql statement to the statement object
        statement=myCon.prepareStatement(sqlStatement);

        //Here we are setting the statement according to the respective data-types of the inputed data
        statement.setString(1,usn);
        statement.setString(2,name);
        statement.setString(3,gender);
        //Used setInt() as SEM is of data-Type 'Integer' / contains numeric values
        statement.setInt(4,sem);
        statement.setString(5,branch);
        statement.setString(6,otherActivity);
        statement.setString(7,usn);

        //Here we executeUpdate() the statement and in turn it returns the number of row which has been updated or changed
        //And in turn we store it in a integer type variable n
        int rowsAffected=statement.executeUpdate();

        //setting the data status indicating the 'n' number of rows are updated
        dataStatus.setText(rowsAffected+" Row is Updated");

    }

    public void importFromExcel() {

            //Create an instance of ExcelToPostgresApp
            ExcelToPostgresApp excelToPostgresApp;
            excelToPostgresApp = new ExcelToPostgresApp();

            //Set text in dataStatus label to indicate that data import process has started
            dataStatus.setText("Data imported");

            //Create a new Stage (window) for the ExcelToPostgresApp
            Stage primaryStage = new Stage();

            //Start the ExcelToPostgresApp by calling its start method with the primaryStage
            excelToPostgresApp.start(primaryStage);

    }

    //This method is responsible for writing the student data to an Excel file
    public void writeExcel(){

        System.out.println("Writing To Excel File");
        try {
            //creating a Statement object
            Statement statement=myCon.createStatement();

            //Here we are executing SELECT query to Fetch all student records and then store it in ResultObject
            ResultSet rs = statement.executeQuery("select * from student1");

            //Creating a new Excel workbook
            XSSFWorkbook workbook = new XSSFWorkbook();
            // Creating a new sheet
            XSSFSheet sheet = workbook.createSheet("Student Info");
            // Defining the Excel file path where the data will be stored
            String excelFilePath = "studentData.xlsx";
            // Writing the header to Excel sheet
            writeHeaderLine(sheet);
            // Writing the student data to Excel sheet
            writeDataLines(rs, workbook, sheet);

            //Here we are Writing the workbook to the given file using FileOutputStream
            FileOutputStream outputStream = new FileOutputStream(excelFilePath);
            workbook.write(outputStream);
            //Closing the workbook
            workbook.close();
            //setting the data status indicating the data is written to excel sheet
            dataStatus.setText("Data Written to Excel Sheet");


        }catch (SQLException e){
            //Handlingt the SQL Exception
            System.out.println(e.toString());
        } catch (FileNotFoundException e) {
            //Handling FileNotFound Exception
            throw new RuntimeException(e);
        } catch (IOException e) {
            //Handling the IO exception
            throw new RuntimeException(e);
        }

    }
    //This Method is used to determine selected gender to their respective radio button objects/ Buttons
    public void getGender(ActionEvent event){
        if(rb1.isSelected())
            gender="Male";
        else if(rb2.isSelected())
            gender="Female";
    }

    //This Method is to show student data based on entered USN by the user
    public void showStudentData(KeyEvent event) throws SQLException {
        // Get the KeyCode from the KeyEvent
        KeyCode code=event.getCode();
        //setting myUsn to null as String
        String myUsn="";
        //if condition to check if the ENTER key is pressed
        if(code==KeyCode.ENTER)
        {
            //Getting the student USN from text field
            myUsn=stdUsn.getText();
            // Creating the SQL statement
            Statement statement=myCon.createStatement();
            //Executing the SELECT query to display the record of the Student based on his/her USN
            ResultSet rs=statement.executeQuery("select * from student1 where usn='"+myUsn+"'");

            //
            if(!rs.isBeforeFirst() && rs.getRow() == 0) {
                //setting the data status to the message indicating below
                dataStatus.setText("No such Student");
            }
            else {
                //Checking if there is any other data
                rs.next();
                //setting the data status that the student is found
                dataStatus.setText("Student Found");
                //setting the name of the student which we got from the result-Object set
                stdName.setText(rs.getString("sname"));
            }
        }

    }

    //This ActionEvent method handles the Event for choosing the Participation in NSS
    public void getNssParticipation(ActionEvent event){
        System.out.println("I am here...");
        if(nss.isSelected())

            //Here it is same as that of participation = participation+"NSS";
            //Here we are concatinating the string as of to show the result for click event for checkBox
            participation+=" NSS ";
        System.out.println(participation);
    }
    //This ActionEvent method handles the Event for choosing the Participation in NCC
    public  void getNccParticipation(ActionEvent event){
        if(ncc.isSelected())
            participation+=" NCC ";
    }
    //This ActionEvent method handles the Event for choosing the Participation in Rotary Club
    public  void getRotaryParticipation(ActionEvent event){
        if(rc.isSelected())
            participation+=" Rotary Club";
    }
    //This ActionEvent method handles the Event for choosing the Semester value from the Sem combo-Box
    public void getSemester(ActionEvent event){
        //Here we are getting the selected value from the Sem Combo-Box and assigning it to the mysem variable
        mysem=mySemComboBox.getValue();
        System.out.println(mysem);
    }
    //This ActionEvent method handles the Event for choosing the Branch value from the Branch combo-Box
    public void getBranch(ActionEvent event){
        //Here we are getting the selected value from the Branch Combo-Box and assigning it to the mybranch variable
          mybranch=myBranchComboBox.getValue();
          System.out.println(mybranch);
         // welcomeText.setText(mybranch);
    }

    //This method is responsible for handling the event related to the delete Student Button with the following:
    //1. Showing alert box before finally deleting the particular student data
    //2.Executing the DELETE query for the selected student based on their USN
    @FXML
    public void deleteStudent() throws SQLException {
        //Here we are initializing the button object for the option for deleting the student data 'YES' /'NO'
        ButtonType yes = new ButtonType("YES",ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("NO", ButtonBar.ButtonData.CANCEL_CLOSE);
        //Here we are intializing the Alert Object and then specifying a warning for the both the Buttons: YES/NO
        Alert alert = new Alert(Alert.AlertType.WARNING,
                "Do You really want to delete?",
                yes,
                no);
        //Exception handling to handle the SQLExceptions as in this we are
        try {

            //setting the title of the alert box
            alert.setTitle("Student Info Delete Warning!");
            //
            Optional<ButtonType> result = alert.showAndWait();

            //Executing the DELETE query by check if the result object = yes
            if (result.get() == yes) {
                Statement stmt = myCon.createStatement();
                stmt.execute("delete from student1 where usn='" + stdUsn.getText() + "'");
                //setting the data status indicating the below message
                dataStatus.setText("One Row Deleted");
            }
        }catch (SQLException e){
            //Handling the SQL Exception
            System.out.println(e.toString());
        }
    }

    //This method is responsible for handling the event related to the view Student Button
    @FXML
    protected void viewAllStudents() {
        try {
            //establishing the connection
            myCon = DataBaseConnect.getConnection();
            System.out.println("Connected Succesfully");
        }catch (SQLException e){
            System.out.println(e.toString());
        }
        //Clears the items in the allStudents TableView
        allStudents.getItems().clear();

        // Checks if it's the first time loading data to avoid duplicating columns
        if(firstTime) {
            //Defining the columns for TableView

            // Defining a new TableColumn for Student USN (University Serial Number)
            //Here we have set the column header to "USN"
            TableColumn<Student, String> usnCol = new TableColumn<>("USN");
            /*
             This connects the 'usn' property of the Student class to this column,
             so that each cell in this column will display the 'usn' property of the
             corresponding Student object.
             Similarly we have done this to every column element below:
             */
            usnCol.setCellValueFactory(new PropertyValueFactory<>("usn"));

            //Here we have set the column header to "NAME"
            TableColumn<Student, String> nameCol = new TableColumn<>("NAME");
            nameCol.setCellValueFactory(new PropertyValueFactory<>("sname"));

            //Here we have set the column header to "SEM"
            TableColumn<Student, Integer> semCol = new TableColumn<>("SEM");
            semCol.setCellValueFactory(new PropertyValueFactory<>("sem"));

            // //Here we have set the column header to "BRANCH"
            TableColumn<Student, String> branchCol = new TableColumn<>("BRANCH");
            branchCol.setCellValueFactory(new PropertyValueFactory<>("branch"));

            //Here we have set the column header to "GENDER"
            TableColumn<Student, String> genderCol = new TableColumn<>("GENDER");
            genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));

            //Here we have set the column header to "PARTICIPATION"
            TableColumn<Student, String> partCol = new TableColumn<>("PARTICIPATION");
            partCol.setCellValueFactory(new PropertyValueFactory<>("participation"));

            //Here we Adding all the columns to the TableView
            allStudents.getColumns().addAll(usnCol,nameCol,semCol,branchCol,genderCol,partCol);
            //We are marking the firstTime flag to false after columns are added
            firstTime=false;
        }


        //Create an ObservableList to hold Student objects
        ObservableList<Student> allStds=FXCollections.observableArrayList();

        try {
            //Here we are creating a SQL Statement
            Statement statement=myCon.createStatement();
            //Here we are Executing the SELECT query to retrieve data from 'student1' table and then storing in the result object
            ResultSet rs1=statement.executeQuery("select * from student1");

            //Here we Iterate through the ResultSet and populate allStds list with Student objects
            while(rs1.next()){
                //Here we Retrieve all the data from ResultSet object for each column and store it in its respectively described variables
                String usn=rs1.getString("usn");    //Student usn
                String sname=rs1.getString("sname"); //Student name
                Integer sem=rs1.getInt("sem");       //Student Semester
                String branch=rs1.getString("branch"); //Student Branch
                String gend=rs1.getString("gender");   //Student gender
                String part=rs1.getString("participation"); //Student Participation
                allStds.add(new Student(usn,sname,sem,branch,gend,part)); //Here we add a new Student object to allStds list
            }
            //Setting the items in the TableView to display data from allStds list
            allStudents.setItems(allStds);
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        //// Set text for welcomeText label
        welcomeText.setText("JAVAFX-PROJECT");
        // Setting the text for dataStatus label
        dataStatus.setText("VIEWING TABLE DATA");
    }

    //This Method is responsible to write data from ResultSet to Excel sheet
    private void writeDataLines(ResultSet result, XSSFWorkbook workbook,
                                XSSFSheet sheet) throws SQLException {
        //Here we are Initializing row count for starting from the second row (index 1)
        int rowCount = 1;

        //Here we are Iterating through the ResultSet to process each row of student data
        while (result.next()) {
            //Here we are retrieving data for each column from the current row in the ResultSet
            String studentUsn = result.getString("usn");
            String studentName = result.getString("sname");
            int studentSem = result.getInt("sem");
            String studentBranch = result.getString("branch");
            String studentGender = result.getString("gender");
            String studentParticipation = result.getString("participation");

            // Create a new row in the Excel sheet
            Row row = sheet.createRow(rowCount++);

            // Initialize column count for starting from the first column (index 0)
            int columnCount = 0;

            //Now we create cells in the row for each column and set cell values

            //Now we create cell for USN (University Serial Number) and set its value
            Cell cell = row.createCell(columnCount++);
            cell.setCellValue(studentUsn);

            //Now we create cell for StudentName and set its value
            cell = row.createCell(columnCount++);
            cell.setCellValue(studentName);

            //Now we create cell for StudentSem and set its value
            cell = row.createCell(columnCount++);
            cell.setCellValue(studentSem);

            //Now we create cell for StudentBranch and set its value
            cell = row.createCell(columnCount++);
            cell.setCellValue(studentBranch);

            //Now we create cell for StudentGender and set its value
            cell = row.createCell(columnCount++);
            cell.setCellValue(studentGender);

            //Now we create cell for StudentParticipation and set its value
            cell = row.createCell(columnCount++);
            cell.setCellValue(studentParticipation);
        }
    }


    //This Method is responsible to write header line (column names) in the Excel sheet
    private void writeHeaderLine(XSSFSheet sheet) {
        // Create the header row in the Excel sheet at index 0 (first row)
        Row headerRow = sheet.createRow(0);

        // Create cells in the header row for each column and set header names

        // Create cell for USN column header and set its value
        Cell headerCell = headerRow.createCell(0);
        headerCell.setCellValue("USN");

        // Create cell for Student Name column header and set its value
        headerCell = headerRow.createCell(1);
        headerCell.setCellValue("Student Name");

        // Create cell for Semester column header and set its value
        headerCell = headerRow.createCell(2);
        headerCell.setCellValue("Semester");

        // Create cell for Branch column header and set its value
        headerCell = headerRow.createCell(3);
        headerCell.setCellValue("Bracnh");

        // Create cell for Gender column header and set its value
        headerCell = headerRow.createCell(4);
        headerCell.setCellValue("Gender");

        // Create cell for Participation column header and set its value
        headerCell = headerRow.createCell(5);
        headerCell.setCellValue("Participation");
    }


    //This method is responsible for inserting the Student Data
    @FXML
    protected void insertStudentClick(){

        // Establish a database connection using a custom method
        try {
            myCon = DataBaseConnect.getConnection();
            System.out.println("Connected Succesfully");
        }catch (SQLException e){
            // Print SQL exception details if connection fails
            System.out.println(e.toString());
        }
        // Retrieve input values from JavaFX text fields

        //Retrieving Student USN
        String usn=stdUsn.getText();
        //Retrieving Student Name
        String sname=stdName.getText();

        //Here we Checking if either of the input fields is empty
        if(usn.equals("") || sname.equals("")) {
            // If input fields are empty,then we dsiplay warning alert window
            Alert a=new Alert(Alert.AlertType.WARNING);

            //Here we are Defining event handler for the alert
            EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent e)
                        {
                            // Setting the alert type to error
                            a.setAlertType(Alert.AlertType.ERROR);
                            a.setTitle("Inputs Cant be Blank");
                            //Here we show the alert dialog
                            a.show();
                        }
                    };

            //Setting the event handler to the addStudent button
            addStudent.setOnAction(event);
            System.out.println("Data cant be blank");
        }else {
            // If input fields are not empty, then we proceed with inserting data into the database

            //Here we retrieve the values for Semester and Branch
            int Sem = mysem;  //Integer.parseInt(stdSem.getText());
            String Branch = mybranch;     //stdBranch.getText();
            System.out.println(usn + " " + sname + "  " + Sem + "  " + Branch + " " + participation.toString());

            //Defining INSERT SQL statement for inserting data into 'student1' table
            String insertSQL = "INSERT INTO student1(usn, sname, sem, branch,gender,participation) VALUES (?, ?, ?, ?,?,?)";
            try {
                // Prepare a PreparedStatement to execute the insert SQL statement.
                PreparedStatement preparedStatement = myCon.prepareStatement(insertSQL);

                //Setting the values for each parameter in the insert SQL statement
                preparedStatement.setString(1, usn); // id
                preparedStatement.setString(2, sname); // name
                preparedStatement.setInt(3, Sem); // age
                preparedStatement.setString(4, Branch); // grade
                preparedStatement.setString(5, gender);
                preparedStatement.setString(6, participation.toString());

                // Execute the insert SQL statement and retrieve the number of rows affected
                int rowsInserted = preparedStatement.executeUpdate();
                System.out.println(rowsInserted + " rows inserted");

                // Update the dataStatus label to indicate one row was successfully inserted
                dataStatus.setText("One Row Inserted");

                // Sleep for 1 second, just to take some time to display
                Thread.sleep(1000);

                // Update the dataStatus label again to maintain the flow
                dataStatus.setText("One Row Inserted");

                //After the rows were successfully inserted, then we print a success message to the console
                if (rowsInserted > 0) {
                    System.out.println("A new student was inserted successfully!");
                }

            } catch (SQLException e) {
                //If any SQLException occurs during the database operation, print the error details
                System.out.println(e.toString());

                //Here we Update the dataStatus label to display the last 15 characters of the error message in uppercase
                dataStatus.setText(e.toString().substring(e.toString().length()-15,e.toString().length()).toUpperCase());
                //e.printStackTrace();
            } catch (InterruptedException e) {
                //If any InterruptedException occurs, then we throw a runtime exception
                throw new RuntimeException(e);
            } finally {

            }
        }

    }



}