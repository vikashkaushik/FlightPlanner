// --== CS400 File Header Information ==--
// Name: Pratyush Shanbhag
// Email: pshanbhag@wisc.edu
// Group and Team: BI Red
// Group TA: Naman Gupta
// Lecturer: Gary Dahl
// Notes to Grader: <optional extra notes>

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

public class FlightBackendBD implements FlightBackendInterface {
    private List<RouteInterface> routes = new ArrayList<RouteInterface>();
    private FlightGraphInterface<AirportInterface, RouteDW> graph = new FlightGraphAE<>();
    private Hashtable<String, AirportInterface> airports = new Hashtable<String, AirportInterface>();
    private Set<String> airlines = new HashSet<String>();
    
    /**
     * Loads in the flight route data from a specified file into the flight graph data structure
     * 
     * @param filename name of the file containing flight route data
     * @throws FileNotFoundException thrown if specified file does not exist
     */
    @Override
    public void loadData(String filename) throws FileNotFoundException {
        RouteReaderInterface routeReader = new RouteReaderDW();
        routes = routeReader.readFromFile(filename);
        for(RouteInterface route: routes) {
            airlines.add(route.getAirline());

            if(!airports.containsKey(route.getStartingLocation().getName())) {
                graph.insertNode(route.getStartingLocation());
                airports.put(route.getStartingLocation().getName(), route.getStartingLocation());
            }
            if(!airports.containsKey(route.getEndingLocation().getName())) {
                graph.insertNode(route.getEndingLocation());
                airports.put(route.getEndingLocation().getName(), route.getEndingLocation());
            }
            
            graph.insertEdge(route.getStartingLocation(), route.getEndingLocation(), (RouteDW) route);
        }
    }

    /**
     * Finds the shortest flight route from a specified origin to a specified destination
     * 
     * @param departure flight route's origin
     * @param destination flight route's destination
     * @return list of airports in the shortest flight route
     */
    @Override
    public List<String> findShortestRoute(String departure, String destination) {
        if(!airports.containsKey(departure) || !airports.containsKey(destination))
            return new ArrayList<String>();
        
        List<AirportInterface> route = graph.shortestPathData(airports.get(departure), airports.get(destination));
        List<String> routeStrings = new ArrayList<String>();
        for(AirportInterface routeAirport: route) {
            routeStrings.add(routeAirport.getName());
        }

        return routeStrings;
    }

    /**
     * Finds the shortest flight route from a specified origin to a specified destination through a specified airline
     * 
     * @param departure flight route's origin
     * @param destination flight route's destination
     * @param airline flight route's airline
     * @return list of airports in the shortest flight route
     */
    @Override
    public List<String> findShortestRouteForAirline(String departure, String destination, String airline) {
        if(!airports.containsKey(departure) || !airports.containsKey(destination) || !airlines.contains(airline))
            return new ArrayList<String>();

        FlightGraphInterface<AirportInterface, RouteDW> airlineGraph = new FlightGraphAE<>();
        for(RouteInterface airlineRoute: routes) {
            if(airlineRoute.getAirline().equals(airline)) {
                airlineGraph.insertNode(airlineRoute.getStartingLocation());
                airlineGraph.insertNode(airlineRoute.getEndingLocation());
                airlineGraph.insertEdge(airlineRoute.getStartingLocation(), airlineRoute.getEndingLocation(), (RouteDW) airlineRoute);
            }
        }

        List<AirportInterface> route = null;
        try {
            route = airlineGraph.shortestPathData(airports.get(departure), airports.get(destination));
        }
        catch(NoSuchElementException nsee) {
            return new ArrayList<String>();
        }

        List<String> routeStrings = new ArrayList<String>();
        for(AirportInterface routeAirport: route) {
            routeStrings.add(routeAirport.getName());
        }

        return routeStrings;
    }

    /**
     * Finds the shortest flight route from a specified origin to a specified destination going through a specified airport
     * 
     * @param departure flight route's origin
     * @param destination flight route's destination
     * @param airport flight route's required airport
     * @return list of airports in the shortest flight route
     */
    @Override
    public List<String> findShortestRouteWithAirport(String departure, String destination, String airport) {
        if(!airports.containsKey(departure) || !airports.containsKey(destination) || !airports.containsKey(airport))
            return new ArrayList<String>();
        
        List<AirportInterface> route = null;
        try {
            route = graph.filterWithAirport(airports.get(departure), airports.get(destination), airports.get(airport));
        }
        catch(NoSuchElementException nsee) {
            return new ArrayList<String>();
        }

        List<String> routeStrings = new ArrayList<String>();
        for(AirportInterface routeAirport: route) {
            routeStrings.add(routeAirport.getName());
        }

        return routeStrings;
    }

    /**
     * Finds the shortest flight route from a specified origin to a specified destination excluding a specified airport
     * 
     * @param departure flight route's origin
     * @param destination flight route's destination
     * @param airport flight route's excluded airport
     * @return list of airports in the shortest flight route
     */
    @Override
    public List<String> findShortestRouteWithoutAirport(String departure, String destination, String airport) {
        if(!airports.containsKey(departure) || !airports.containsKey(destination) || !airports.containsKey(airport))
            return new ArrayList<String>();
        
        List<AirportInterface> route = null;
        try {
            route = graph.filterWithoutAirport(airports.get(departure), airports.get(destination), airports.get(airport));
        }
        catch(NoSuchElementException nsee) {
            return new ArrayList<String>();
        }

        List<String> routeStrings = new ArrayList<String>();
        for(AirportInterface routeAirport: route) {
            routeStrings.add(routeAirport.getName());
        }

        return routeStrings;
    }

    /**
     * Returns a String of flight route statistics. More specifically, returns a string contains the name
     * of every airport and airline in the flight routes.
     * 
     * @return String containing every airport and airline in the flight routes
     */
    @Override
    public String getStatisticsString() {
        return "Number of Airports: " + airports.size() + "\n\nNumber of Airlines: " + airlines.size();
    }   
}