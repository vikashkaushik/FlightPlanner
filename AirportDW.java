import java.util.ArrayList;
import java.util.List;

/**
 * An airport
 */
public class AirportDW implements AirportInterface {
    String name;
    List<RouteInterface> routes;

    public AirportDW(String name) {
        this.name = name;
        this.routes = new ArrayList<>();
    }

    /**
     * Returns the name of the airport
     */
	public String getName() {
        return name;
    }

    /**
     * Returns the routes from this airport
     */
	public List<RouteInterface> getRoutes() {
        return null;
    }
}
