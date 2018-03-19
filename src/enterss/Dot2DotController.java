/*
 * CS2852 – 031
 * Spring 2017
 * Lab 1 - Dot 2 Dot Generator
 * Name: Stuart Enters
 * Created: 3/8/2018
 */

package enterss;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
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
    public Label nodeCounter;

    /**
     * The user input for changing the number of nodes showing
     */
    @FXML
    public TextArea nodeText;

    /**
     * The Hbox for editing the number of nodes in the image
     */
    @FXML
    public HBox nodeHBox;

    private File file;
    private Picture picture = null;
    private FileChooser chooser = new FileChooser();
    private ArrayList<Dot> nodeList = new ArrayList<>();


    @FXML private void handleOpen(){
        try{

            chooser.setTitle("Choose an image to load");
            chooser.setInitialDirectory(new File(System.getProperty("user.dir")));

            if (chooser.getExtensionFilters().isEmpty()){
                FileChooser.ExtensionFilter dotFilter = new FileChooser.ExtensionFilter(
                        "DOT Image (*.dot)", "*.dot");
                chooser.getExtensionFilters().add(dotFilter);
            }
            file = chooser.showOpenDialog(new Stage());

            if (file.exists()){
                load(file);

                editMenu.setDisable(false);
                nodeHBox.setDisable(false);
                nodeCounter.setText("Nodes: " + nodeList.size());
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

    @FXML private void handleNodeInput(Double remove){
        try {
            clear();
            picture.removeDots(remove);
            picture.drawDots(canvas);
            picture.drawLines(canvas);
            nodeCounter.setText("Nodes: " + nodeList.size());
        } catch (IllegalArgumentException e){
            Alert alert = new Alert(Alert.AlertType.valueOf("ERROR"));
            alert.setHeaderText("Illegal Argument Exception");
            alert.setContentText("Error with removing dots: Too few dots selected");
            alert.showAndWait();
        }
    }

    @FXML private void handleReload() {
        load(file);
        nodeCounter.setText("Nodes: " + nodeList.size());
    }

    private void clear() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void load(File file) {
        try {
            if(nodeList.size() != 0){
                nodeList = new ArrayList<>();
            }

            clear();
            picture = new Picture(nodeList);
            picture.load(file);
            picture.drawDots(canvas);
            picture.drawLines(canvas);
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

    @FXML private void handleSave() {
        try {
            chooser.setTitle("Choose a file to save to");
            chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
            file = chooser.showSaveDialog(new Stage());

            if (file.exists()){
                picture.save(file);
            }

        } catch (IOException e){
            Alert alert = new Alert(Alert.AlertType.valueOf("ERROR"));
            alert.setHeaderText("Input / Output Exception");
            alert.setContentText("Error with saving: Unknown filepath");
            alert.showAndWait();
        }
    }

    @FXML private void checkEnter(KeyEvent keyEvent) {
        try {

            if(keyEvent.getCode().equals(KeyCode.ENTER)){
                handleReload();
                double remove = Double.parseDouble(nodeText.getText());
                handleNodeInput(remove);
                nodeText.setText("");

            }
        } catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.valueOf("ERROR"));
            alert.setHeaderText("Number Format Exception");
            alert.setContentText("Error with removing dots: Illegal number entered: "
                    + nodeText.getText());

            nodeText.setText("");
            alert.showAndWait();
        }

    }

}
