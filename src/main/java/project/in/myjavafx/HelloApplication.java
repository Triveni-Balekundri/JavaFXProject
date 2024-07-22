package project.in.myjavafx;

//Have imported JavaFX Application class
import javafx.application.Application;
//Have imported FXMLLoader for loading FXML files
import javafx.fxml.FXMLLoader;
//Have imported Scene class for UI
import javafx.scene.Scene;
// Have imported Color class for colors
import javafx.scene.paint.Color;
// Have imported CycleMethod for gradient cycling options
import javafx.scene.paint.CycleMethod;
// Have imported RadialGradient for background effects
import javafx.scene.paint.RadialGradient;
// Have imported Stop class for gradient stops
import javafx.scene.paint.Stop;
//Have imported Stage class for application windows
import javafx.stage.Stage;
//Have imported IOException class to handle the exceptions
import java.io.IOException;

//This is the main class and is responsible for setting the first scene.It also extends the Application class
public class HelloApplication extends Application {

    //This method is the entry point for any JavaFX application. We used it to set up the application's scene,stage etc
    @Override
    public void start(Stage stage) throws IOException {
        try {


            // Load the FXML file for the login page
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-page.fxml"));
            // Create a new scene with the loaded FXML, setting width and height and Adjusted height for usability
            Scene scene = new Scene(fxmlLoader.load(), 686, 38);
            // Set the window title
            stage.setTitle("Student Information System");

            // Load and apply the CSS stylesheet
            String css = this.getClass().getResource("/application.css").toExternalForm();
            scene.getStylesheets().add(css);

            // Set a radial gradient background for the scene
            scene.setFill(new RadialGradient(
                    0, 0, 0, 0, 1, true,   // Sizing parameters for the gradient
                    CycleMethod.NO_CYCLE,                      // No cycling of colors
                    new Stop(0, Color.web("#81c483")),   // Start color
                    new Stop(1, Color.web("#fcc200")))   // End color

            );

            // Set the stage to full-screen mode and customize the exit hint
            stage.setFullScreenExitHint("");
            stage.setFullScreen(true);
            // Set the scene to the stage
            stage.setScene(scene);
            // Display the stage
            stage.show();
        }
        catch (IOException e){
            // Print stack trace for debugging
            e.printStackTrace();
        }
    }

    // Main method to launch the application by using the launch()
    public static void main(String[] args) {
        // Launch the JavaFX application
        launch();
    }
}