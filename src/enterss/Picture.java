/*
 * CS2852 – 031
 * Spring 2017
 * Lab 1 - Dot 2 Dot Generator
 * Name: Stuart Enters
 * Created: 3/8/2018
 */

package enterss;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import static enterss.Dot2Dot.PREF_HEIGHT;
import static enterss.Dot2Dot.PREF_WIDTH;

/**
 * Class for reading a .dot image from file
 * and drawing it to canvas
 * @author enterss
 * @version 1.0
 */
public class Picture {

    private static final int DOT_DIAMETER = 10;
    private static final int ADJUSTMENT = 5;
    private ArrayList<Dot> dots = new ArrayList<>();
    private Canvas canvas;

    /**
     * Constructor for picture class
     * @param canvas the canvas to draw the picture on
     */
    public Picture(Canvas canvas){
        this.canvas = canvas;
    }

    /**
     * Method to load a picture from file
     * @param file the file to load from
     */
    public void load(File file){
        try{
            Scanner reader = new Scanner(file);
            double width = PREF_WIDTH;
            double height = PREF_HEIGHT;
            while (reader.hasNextLine()){
                String input = reader.nextLine();
                String[] coords = input.split(",");
                double x = width - (Double.parseDouble(coords[0]) * width);
                double y = height - (Double.parseDouble(coords[1]) * height);
                dots.add(new Dot(x, y));
            }

            GraphicsContext context = canvas.getGraphicsContext2D();
            context.clearRect(0, 0, width, height);
            drawDots(canvas);
            drawLines(canvas);

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
        }
    }

    /**
     * Method for drawing the points of the image
     * @param canvas the canvas to draw on
     */
    public void drawDots(Canvas canvas){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        for (Dot d: dots) {
            gc.fillOval(d.getX() - ADJUSTMENT, d.getY() - ADJUSTMENT, DOT_DIAMETER, DOT_DIAMETER);
        }
    }

    /**
     * Method for drawing lines between points of the image
     * @param canvas the canvas to draw on
     */
    public void drawLines(Canvas canvas){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        for (int i = 1; i < dots.size(); i++) {
            gc.strokeLine(dots.get(i - 1).getX(), dots.get(i - 1).getY(),
                    dots.get(i).getX(), dots.get(i).getY());
        }
        gc.strokeLine(dots.get(dots.size() - 1).getX(), dots.get(dots.size() - 1).getY(),
                dots.get(0).getX(), dots.get(0).getY());

    }

    /**
     * Set the number of nodes in the picture to that of the one set by the user
     * @param number the number of nodes to show
     */
    public void setNodes(int number) {
        ArrayList<Double> criticalValues = new ArrayList<>();
        double critValue;
        for (int i = 1; i < dots.size() - 1; i++) {
            critValue = dots.get(i).calculateCriticalValue(
                    dots.get(i - 1), dots.get(i + 1));
            criticalValues.add(critValue);
        }
        Collections.sort(criticalValues);
        Collections.reverse(criticalValues);

        while (number < criticalValues.size()){
            criticalValues.remove(number);
        }
    }
}
