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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.ListIterator;
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
     * @throws FileNotFoundException if the file is invalid
     * @throws NumberFormatException if the file is incorrectly formatted
     * @throws IOException if the file is inaccessible
     */
    public void load(File file) throws FileNotFoundException, NumberFormatException, IOException {
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
            gc.fillOval((d.getX() * canvas.getWidth()) - ADJUSTMENT,
                    (d.getY() * canvas.getHeight()) - ADJUSTMENT, DOT_DIAMETER, DOT_DIAMETER);
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
            gc.strokeLine(dots.get(i - 1).getX() * width, dots.get(i - 1).getY() * height,
                    dots.get(i).getX() * width, dots.get(i).getY() * height);
        }
        gc.strokeLine(dots.get(last).getX() * width, dots.get(last).getY() * height,
                dots.get(0).getX() * width, dots.get(0).getY() * height);

    }

    /**
     * Logic for removing a number of nodes from the picture
     * @param numberDesired the number of nodes to remove
     * @throws IllegalArgumentException if the number of nodes is less than 3
     */
    public void removeDots(int numberDesired) throws IllegalArgumentException {
        if (numberDesired < 3){
            throw new IllegalArgumentException("Too few dots");
        }
        ListIterator<Dot> iterator = dots.listIterator();
        while (numberDesired != dots.size()){
            for (Dot dot : dots) {
                if (!iterator.hasPrevious()){
                    dot.calculateCriticalValue(dots.get(dots.size() - 1), iterator.next());
                }
                if (!iterator.hasNext()){
                    dot.calculateCriticalValue(iterator.previous(), dots.get(0));
                }

                dot.calculateCriticalValue(iterator.previous(), iterator.next());
            }

            dots.stream().min(Comparator.comparing(Dot::getCriticalValue)).ifPresent(dots::remove);
        }
    }
}
