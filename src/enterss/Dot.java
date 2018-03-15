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
    private double criticalValue;

    Dot(double x, double y){
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    /**
     * Logic for finding the 'critical value'
     * or importance of the node
     * @param previous the previous node in the picture
     * @param next the next node in the picture
     * @return the critical value of the node
     */
    public double calculateCriticalValue(Dot previous, Dot next){
        double d12;
        double d23;
        double d13;

        d12 = Math.sqrt(Math.pow(this.x - previous.x, 2) + Math.pow(this.y - previous.y, 2));
        d23 = Math.sqrt(Math.pow(next.x - this.x, 2) + Math.pow(next.y - this.y, 2));
        d13 = Math.sqrt(Math.pow(next.x - previous.x, 2) + Math.pow(next.y - previous.y, 2));

        criticalValue = d12 + d23 - d13;
        return getCriticalValue();
    }

    public double getCriticalValue() {
        return criticalValue;
    }
}
