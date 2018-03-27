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
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static enterss.Dot2Dot.PREF_HEIGHT;
import static enterss.Dot2Dot.PREF_WIDTH;

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
     * The edit menu that contains the ability to draw lines or dots
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
    public TextField nodeText;

    /**
     * The button to reload the image
     */
    @FXML
    public Button reloadButton;

    /**
     *Button for selecting list ant iteration types
     */
    @FXML
    public Button arrayIndexButton;

    /**
     *Button for selecting list ant iteration types
     */
    @FXML
    public Button arrayIterButton;

    /**
     *Button for selecting list ant iteration types
     */
    @FXML
    public Button linkedIndexButton;

    /**
     *Button for selecting list ant iteration types
     */
    @FXML
    public Button linkedIterButton;

    /**
     *The label for recording the time algorithms took
     */
    @FXML
    public Label timeLabel;

    private File file;
    private Stage stage = new Stage();
    private static String listType = "";
    private static String iterateType = "";
    private FileChooser chooser = new FileChooser();
    private List<Dot> nodeList = new LinkedList<Dot>();
    private Picture picture = new Picture(nodeList);
    private static final Logger LOG = Logger.getLogger(Dot2Dot.class.getName());

    @FXML
    private void handleOpen() {
        try {

            FileHandler handler = new FileHandler("d2d.txt");
            LOG.setUseParentHandlers(false);
            LOG.addHandler(handler);

            chooser.setTitle("Choose an image to load");
            chooser.setInitialDirectory(new File(System.getProperty("user.dir")));

            if (chooser.getExtensionFilters().isEmpty()) {
                FileChooser.ExtensionFilter dotFilter = new FileChooser.ExtensionFilter(
                        "DOT Image (*.dot)", "*.dot");
                chooser.getExtensionFilters().add(dotFilter);
            }
            file = chooser.showOpenDialog(new Stage());

            if (!getExtension(file).equals(".dot")){
                throw new IOException();
            }

            if (file != null) {
                load();

                editMenu.setDisable(false);
                nodeText.setDisable(false);
                reloadButton.setDisable(false);
                nodeCounter.setText("Nodes: " + nodeList.size());
            }


        } catch (NullPointerException e) {
            LOG.log(Level.SEVERE, "Error with opening: No file selected");

        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Error with opening: Illegal file selected");
        }
    }

    @FXML
    private void handleSave() {
        try {
            chooser.setTitle("Choose a file to save to");
            chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
            file = chooser.showSaveDialog(new Stage());

            if (file != null) {
                picture.save(file);
                LOG.info("File Saved");
            }

        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Error with saving: Illegal filepath");
        }
    }

    @FXML
    private void handleClose() {
        LOG.info("Closing");
        Platform.exit();
    }

    @FXML
    private void handleLines() {
        clear();
        picture.drawLines(canvas);
        LOG.info("Lines only drawn");
    }

    @FXML
    private void handleDots() {
        clear();
        picture.drawDots(canvas);
        LOG.info("Dots only drawn");
    }

    @FXML
    private void checkEnter(KeyEvent keyEvent) {
        try {

            if(keyEvent.getCode().equals(KeyCode.ENTER)){
                LOG.info("Enter pressed");
                load();
                handleNodeInput();
                nodeText.setText("");
            }
        } catch (NumberFormatException e){
            LOG.log(Level.SEVERE, "Error with removing dots: Illegal number entered");
        }
    }


    /**
     * If user wants to use an Array with an Index
     */
    @FXML
    public void selectArrayIndex() {
        stage = (Stage) arrayIndexButton.getScene().getWindow();
        stage.close();
        listType = "Array";
        iterateType = "Index";
    }

    /**
     * If user wants to use an Array with a List Iterator
     */
    @FXML
    public void selectArrayIter() {
        listType = "Array";
        iterateType = "Iterator";
        stage = (Stage) arrayIndexButton.getScene().getWindow();
        stage.close();
    }

    /**
     * If user wants to use a Linked List with an Index
     */
    @FXML
    public void selectLinkedIndex() {
        listType = "Linked";
        iterateType = "Index";
        stage = (Stage) arrayIndexButton.getScene().getWindow();
        stage.close();
    }

    /**
     * If user wants to use a Linked List with a List Iterator
     */
    @FXML
    public void selectLinkedIter() {
        listType = "Linked";
        iterateType = "Iterator";
        stage = (Stage) arrayIndexButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleNodeInput() {
        try {

            getTypes();
            switch (listType) {
                case "Array":
                    clear();
                    picture = new Picture(picture, new ArrayList<>());
                    nodeList = picture.getDotList();
                    break;
                case "Linked":
                    clear();
                    picture = new Picture(picture, new LinkedList<>());
                    nodeList = picture.getDotList();
                    break;
            }

            long millis = picture.removeDots(
                    Integer.parseInt(nodeText.getText()), iterateType);

            String time = String.format("%02d:%02d:%02d.%03d", TimeUnit.NANOSECONDS.toHours(millis),
                    TimeUnit.NANOSECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                    TimeUnit.NANOSECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1),
                    TimeUnit.NANOSECONDS.toMillis(millis) % TimeUnit.SECONDS.toMillis(1));

            picture.drawDots(canvas);
            picture.drawLines(canvas);
            nodeCounter.setText("Nodes: " + nodeList.size());
            timeLabel.setText("Time: " + time);

            LOG.info("Removing with " + listType +
                    " using " + iterateType + " took: " + time);


        } catch (IllegalArgumentException e) {
            LOG.log(Level.SEVERE, "Error with removing dots: Too few dots selected");
        }
    }

    @FXML
    private void load() {
        try {
            if (nodeList.size() != 0) {
                nodeList = new LinkedList<>();
            }
            clear();
            picture = new Picture(nodeList);
            picture.load(file);
            picture.drawDots(canvas);
            picture.drawLines(canvas);
            nodeCounter.setText("Nodes: " + nodeList.size());
            LOG.info("File Loaded");

        } catch (FileNotFoundException e) {
            LOG.log(Level.SEVERE, "Error with loading: No file selected");
        } catch (NumberFormatException e) {
            LOG.log(Level.SEVERE, "Error with loading: File not formatted correctly: "
                    + "\t" + e.getMessage());
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Error with loading: File can't be edited");
        }
    }

    private void clear() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        LOG.info("Canvas cleared");
    }

    private String getExtension(File file) {
        String fileName = file.getName();
        int begin = fileName.indexOf('.');
        return fileName.substring(begin, fileName.length());
    }

    @FXML
    private void getTypes(){
        int prefWidth = PREF_WIDTH / 2;
        int prefHeight = PREF_HEIGHT / 2;
        try{
            Parent root = FXMLLoader.load(getClass().getResource("Alert.fxml"));
            stage.setTitle("Dot to Dot");
            stage.setScene(new Scene(root, prefWidth, prefHeight));
            stage.showAndWait();

        } catch (IOException e){
            LOG.log(Level.SEVERE, "Error with source code: Alert.fxml not found");
        }
    }
}
