/*
 * CS2852 – 031
 * Spring 2017
 * Lab 1 - Dot 2 Dot Generator
 * Name: Stuart Enters
 * Created: 3/8/2018
 */

package enterss;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * Starting point for the Dot2Dot application
 * @author enterss
 * @version 1.0
 */
public class Dot2Dot extends Application {

    /**
     * Width for the application
     */
    public static final int PREF_WIDTH = 600;
    /**
     * Height for the application
     */
    public static final int PREF_HEIGHT = 600;
    private static final int MENU_HEIGHT = 50;

    /**
     * The entry point to the application
     * @param primaryStage the stage for the application
     * @throws Exception if exception occurs
     */
    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("Dot2DotController.fxml"));
        primaryStage.setTitle("Dot to Dot");
        primaryStage.setScene(new Scene(root, PREF_WIDTH, PREF_HEIGHT + MENU_HEIGHT));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }


}
