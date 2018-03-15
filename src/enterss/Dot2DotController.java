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
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

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
     *The edit menu that contains the ability to draw lines or dots
     */
    @FXML
    public Menu editMenu;

    /**
     * The label for the number of nodes showing
     */
    @FXML
    public Label nodeLabel;

    /**
     * The user input for changing the number of nodes showing
     */
    @FXML
    public TextField nodeText;

    private File file;
    private Picture picture = null;
    private ArrayList<Dot> nodeList = new ArrayList<>();


    @FXML private void handleOpen(){
        FileChooser chooser = new FileChooser();

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
                picture = new Picture(nodeList);
                load(file);

                clear();

                picture.drawDots(canvas);
                picture.drawLines(canvas);

                editMenu.setDisable(false);
                nodeText.setDisable(false);

                nodeLabel.setText("Nodes: " + nodeList.size());
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
        clear();
        picture.drawLines(canvas);
    }

    @FXML private void handleDots(){
        clear();
        picture.drawDots(canvas);
    }

    @FXML private void handleNodeInput(){
        try {
            picture.removeDots(Integer.parseInt(nodeText.getText()));
        } catch (IllegalArgumentException e){
            Alert alert = new Alert(Alert.AlertType.valueOf("ERROR"));
            alert.setHeaderText("Illegal Argument Exception");
            alert.setContentText("Error with removing dots: Too few dots selected");
            alert.showAndWait();
        }
    }

    @FXML private void handleReload() {
        clear();
        load(file);
    }

    private void clear() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void load(File file) {
        try {
            picture.load(file);
        } catch (FileNotFoundException e){
            Alert alert = new Alert(Alert.AlertType.valueOf("ERROR"));
            alert.setHeaderText("File Not Found Exception");
            alert.setContentText("Error with loading: No file selected");
            alert.showAndWait();
        } catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.valueOf("ERROR"));
            alert.setHeaderText("Number Format Exception");
            alert.setContentText("Error with loading: Illegal file selected");
            alert.showAndWait();
        } catch (IOException e){
            Alert alert = new Alert(Alert.AlertType.valueOf("ERROR"));
            alert.setHeaderText("Input / Output Exception");
            alert.setContentText("Error with loading: Unknown filepath");
            alert.showAndWait();
        }
    }

    public void handleSave() {
        //TODO
    }

    public void checkEnter(KeyEvent keyEvent) {
        //TODO

    }
}
