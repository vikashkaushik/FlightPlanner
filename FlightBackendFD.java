// --== CS400 Spring 2023 File Header Information ==--
// Name: Vikash Kaushik
// Email: vkaushik3@wisc.edu
// Group and Team: BI
// Group TA: Naman Gupta
// Lecturer: Gary Dahl
// Notes to Grader: <optional extra notes>

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the backend class that uses AE and DW to find shortest paths between two
 * airports. These methods are called by the FD.
 *
 */

public class FlightBackendFD implements FlightBackendInterface {
  // public BackendXX( DijkstraInterface<String, > Dijkstra, AirportInterface destination)

  public void loadData(String filename) throws FileNotFoundException {

  }

  public List<String> findShortestRoute(String departure, String destination) {
    ArrayList<String> output = new ArrayList<String>();
    output.add(departure);
    output.add("Seattle (SEA)");
    output.add("Boston (BOS)");
    output.add(destination);
    return output;
  }

  public List<String> findShortestRouteForAirline(String departure, String destination,
      String airline) {
    ArrayList<String> output = new ArrayList<String>();
    output.add(departure);
    output.add("Chicago (ORD)");
    output.add("Denver (DEN)");
    output.add(destination);
    return output;
  }

  public List<String> findShortestRouteWithAirport(String departure, String destination,
      String airport) {
    ArrayList<String> output = new ArrayList<String>();
    output.add(departure);
    output.add("Orlando (MCO)");
    output.add("Dallas (DFW)");
    output.add(destination);
    return output;
  }

  public List<String> findShortestRouteWithoutAirport(String departure, String destination,
      String airport) {
    ArrayList<String> output = new ArrayList<String>();
    output.add(departure);
    output.add("San Francisco (SFO)");
    output.add("Atlanta (ATL)");
    output.add(destination);
    return output;
  }

  public String getStatisticsString() {
    String output =
        "Airports:San Francisco (SFO);Los Angeles (LAX);,Airlines:United Airlines;American Airlines";
    return output;
  }
}
