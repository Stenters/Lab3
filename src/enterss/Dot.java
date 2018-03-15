/*
 * CS2852 – 031
 * Spring 2017
 * Lab 1 - Dot 2 Dot Generator
 * Name: Stuart Enters
 * Created: 3/8/2018
 */

package enterss;

/**
 * A Dot read from file to form picture
 * @author enterss
 * @version 1.0
 */
public class Dot {

    private double x;
    private double y;

    Dot(double x, double y){
        this.x = x;
        this.y = y;
    }


    /**
     * Method for calculating importance of the dot
     * @param previous dot in the picture
     * @param next dot in the picture
     * @return the critical value of dot
     */
    public double calculateCriticalValue(Dot previous, Dot next){

        return 0;

    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
