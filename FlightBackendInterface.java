// --== CS400 File Header Information ==--
// Name: Pratyush Shanbhag
// Email: pshanbhag@wisc.edu
// Group and Team: BI Red
// Group TA: Naman Gupta
// Lecturer: Gary Dahl
// Notes to Grader: <optional extra notes>

import java.io.FileNotFoundException;
import java.util.List;

public interface FlightBackendInterface {
    //public BackendXX( DijkstraInterface<String, > Dijkstra, AirportInterface destination) 
    
    public void loadData(String filename) throws FileNotFoundException;
    public List<String> findShortestRoute(String departure, String destination);
    public List<String> findShortestRouteForAirline(String departure, String destination, String airline);
    public List<String> findShortestRouteWithAirport(String departure, String destination, String airport);
    public List<String> findShortestRouteWithoutAirport(String departure, String destination, String airport);
    public String getStatisticsString();
}
    