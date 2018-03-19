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
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
import java.util.stream.Stream;

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
    private ArrayList<Dot> dots;

    /**
     * Constructor for picture class
     * @param original the original picture
     * @param emptylist the initial arrayList
     */
    public Picture(Picture original, ArrayList<Dot> emptylist) {
        emptylist.addAll(original.dots);
        this.dots = emptylist;
    }

    /**
     * Constructor for picture class
     * @param emptylist the initial list for the picture
     */
    public Picture(ArrayList<Dot> emptylist){
        this.dots = emptylist;
    }

    /**
     * Method to load a picture from file
     * @param file the file to load from
     * @throws NumberFormatException if the file is incorrectly formatted
     * @throws IOException if the file is inaccessible
     */
    public void load(File file) throws NumberFormatException, IOException {
        Stream<String> lines = Files.lines(Paths.get(file.getAbsolutePath()));
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
    }

    /**
     * Method for drawing the points of the image
     * @param canvas the canvas to draw on
     */
    public void drawDots(Canvas canvas){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        for (Dot d: dots) {
            gc.fillOval(d.getX() - ADJUSTMENT,
                    d.getY() - ADJUSTMENT, DOT_DIAMETER, DOT_DIAMETER);
        }
    }

    /**
     * Method for drawing lines between points of the image
     * @param canvas the canvas to draw on
     */
    public void drawLines(Canvas canvas){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        double width = canvas.getWidth();
        double height = canvas.getHeight();
        int last = dots.size() - 1;
        for (int i = 1; i <= last; i++) {
            gc.strokeLine(dots.get(i - 1).getX(), dots.get(i - 1).getY(),
                    dots.get(i).getX(), dots.get(i).getY());
        }
        gc.strokeLine(dots.get(last).getX(), dots.get(last).getY(),
                dots.get(0).getX(), dots.get(0).getY());

    }

    /**
     * Logic for removing a number of nodes from the picture
     * @param numberDesired the number of nodes to remove
     * @throws IllegalArgumentException if the number of nodes is less than 3
     */
    public void removeDots(double numberDesired) throws IllegalArgumentException {
        if (numberDesired < 3){
            throw new IllegalArgumentException("Too few dots");
        }


        while (numberDesired < dots.size()){

            int last = dots.size() - 1;

            dots.get(0).calculateCriticalValue(dots.get(last), dots.get(1));

            for (int i = 1; i < dots.size() - 1; i++) {

                dots.get(i).calculateCriticalValue(dots.get(i - 1), dots.get(i + 1));
            }

            dots.get(last).calculateCriticalValue(dots.get(last - 1), dots.get(0));

            dots.stream().min(Comparator.comparing(Dot::getCriticalValue)).ifPresent(dots::remove);
        }
    }

    /**
     * Logic for saving picture
     * @param file the file to save to
     * @throws IOException if file doesn't exist
     *          (Should never get thrown)
     */
    public void save(File file) throws IOException {
        FileWriter writer = new FileWriter(file);
        for (Dot d :dots) {
            writer.write(String.format("%s, %s", d.getX(), d.getY()));
        }
    }
}
