/*
 * CS2852 – 031
 * Spring 2017
 * Lab 1 - Dot 2 Dot Generator
 * Name: Stuart Enters
 * Created: 3/8/2018
 */

package enterss;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * Controller for the Dot 2 Dot application
 * @author enterss
 * @version 1.0
 */
public class Dot2DotController {

    /**
     * The canvas to draw pictures to for the application
     */
    @FXML
    public Canvas canvas;

    /**
     *The edit menu that toggles the ability to draw lines or dots
     */
    @FXML
    public Menu editMenu;
    /**
     * TODO
     */
    @FXML
    public TextArea nodeNumberTextArea;

    private Picture picture = null;


    @FXML private void handleOpen(){
        FileChooser chooser = new FileChooser();
        File file;
        try{
            chooser.setTitle("Choose an image to load");
            chooser.setInitialDirectory(new File(System.getProperty("user.dir")));

            if (chooser.getExtensionFilters().isEmpty()){
                FileChooser.ExtensionFilter dotFilter = new FileChooser.ExtensionFilter(
                        "DOT Image (*.dot)", "*.dot");
                chooser.getExtensionFilters().add(dotFilter);
            }

            file = chooser.showOpenDialog(new Stage());
            if (file != null){
                picture = new Picture(canvas);
                picture.load(file);
                editMenu.setDisable(false);
            }
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.valueOf("ERROR"));
            alert.setHeaderText("Null Pointer Exception");
            alert.setContentText("Error with opening: No file selected");
            alert.showAndWait();
        }
    }

    @FXML private void handleClose(){
        Platform.exit();
    }

    @FXML private void handleLines(){
        GraphicsContext context = canvas.getGraphicsContext2D();
        context.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        picture.drawLines(canvas);
    }

    @FXML private void handleDots(){
        GraphicsContext context = canvas.getGraphicsContext2D();
        context.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        picture.drawDots(canvas);
    }

    public void handleSave() {
    }

    public void checkEnter(KeyEvent keyEvent) {
        if (keyEvent.getCharacter().equalsIgnoreCase("Enter")){
            picture.setNodes(Integer.parseInt(nodeNumberTextArea.getText()));
        }
    }
}
