/**
 * A route between two airports
 */
public class RouteDW extends Number implements RouteInterface {
    AirportInterface starting;
    AirportInterface ending;
    String airline;
    double distance;

    public RouteDW(AirportInterface starting, AirportInterface ending, String airline, double distance) {
        this.starting = starting;
        this.ending = ending;
        this.airline = airline;
        this.distance = distance;
    }

    /**
     * Returns the starting location of the route
     */
    public AirportInterface getStartingLocation() {
        return starting;
    }

    /**
     * Returns the ending location of the route
     */
    public AirportInterface getEndingLocation() {
        return ending;
    }

    /**
     * Returns the airline of the route
     */
    public String getAirline() {
        return airline;
    }

    /**
     * Returns the distance of the route
     */
    public double getDistance() {
        return distance;
    }

    /**
     * Returns the route's distance as an int
     * 
     * @return the route's distance as an int
     */
    @Override
    public int intValue() {
        return (int) distance;
    }

    /**
     * Returns the route's distance as a long
     * 
     * @return the route's distance as a long
     */
    @Override
    public long longValue() {
        return (long) distance;
    }

    /**
     * Returns the route's distance as a float
     * 
     * @return the route's distance as a float
     */
    @Override
    public float floatValue() {
        return (float) distance;
    }

    /**
     * Returns the route's distance as a double
     * 
     * @return the route's distance as a double
     */
    @Override
    public double doubleValue() {
        return (double) distance;
    }

}