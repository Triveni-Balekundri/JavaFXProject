package project.in.myjavafx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

//This class is responsible to Authenticate the user-name and password given as an input by the user
public class AuthenticateController implements Initializable {
   // @FXML
   // private ImageView collegeImageView;

    //FXML annotation indicates that the following field or method is linked to an element defined in the FXML file.
    @FXML
    // TextField for user login input
    private TextField login;

    // PasswordField for user password input
    @FXML
    private PasswordField password1;

    // Label for displaying messages to the user
    @FXML
   private Label message;
//Image myImage;

    // Method to authenticate the user
public void authenticateUser() throws IOException{

    //This 'if' statement Checks if the entered login and password values match with the hard-coded values in the below given condition
    if(login.getText().equals("Triveni") && password1.getText().equals("triveni")) {

        // Print welcome message to console to check weather the code is working properly till this line
        System.out.println("Welcome User");
        // Display welcome message on the UI
        message.setText("Welcome User"+login.getText());

        //Here we are loading the next view that is: (hello-view.fxml)
        FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        // Load the FXML file
        Parent mainView = loader.load();

        // Get the current stage (window) from the login text field.
        // Getting the current stage (window) is useful because:
        // 1. It lets us change things like the window size, title, and fullscreen mode.
        // 2. We can switch the view or scene displayed in the window.
        // 3. It helps us handle user actions, like closing the window or changing screens.
        // 4. We can apply styles or behaviors that depend on the window's state.
        // Here, we're getting the stage with the 'login' TextField so we can update the window after the user logs in.
        Stage stage = (Stage) login.getScene().getWindow();

        // Set the stage to full screen
        stage.setFullScreen(true);

        // Create a new scene with the loaded main view
        Scene myscene=new Scene(mainView);
        // Set a radial gradient as the background fill of the scene.
        myscene.setFill(new RadialGradient(
                0, 0, 0, 0, 1, true,                  //sizing
                CycleMethod.NO_CYCLE,                 //cycling
                new Stop(0, Color.web("#81c483")),    //colors
                new Stop(1, Color.web("#fcc200")))
        );
        // Set the scene with the main view

        // Load and apply external CSS for styling
        String css = this.getClass().getResource("/application.css").toExternalForm();
        myscene.getStylesheets().add(css);

        // Create and set another radial gradient (currently commented out)
        RadialGradient gradient1 = new RadialGradient(0,
                .1,
                100,
                100,
                200,
                false,
                CycleMethod.NO_CYCLE,
                new Stop(0, Color.YELLOW),
                new Stop(1, Color.RED));
       // myscene.setFill(gradient1);

        //setting the scene on the stage
        stage.setScene(myscene);
        //setting the scene to Full Screen
        stage.setFullScreen(true);
        //setting the Exit hint for the Full Screen view
        stage.setFullScreenExitHint("");
        //setting the title for the stage
        stage.setTitle("Main Application");


    }
        else {
         //Here we print/show the message if the username and password are wrong
        System.out.println("Access Denied");
        message.setText("Access Denied");
    }
}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
            //setting the 'login' Text-box text to the user-name
            login.setText("Triveni");
//         myImage=new Image(getClass().getResourceAsStream("/images/gssCollege.jpg"));
//         collegeImageView.setImage(myImage);

    }
}
