// --== CS400 Spring 2023 File Header Information ==--
// Name: Vikash Kaushik
// Email: vkaushik3@wisc.edu
// Group and Team: BI
// Group TA: Naman Gupta
// Lecturer: Gary Dahl
// Notes to Grader: <optional extra notes>

import java.io.FileNotFoundException;

/**
 * This interface represents the methods that the FD has to implement and shows how they call the
 * Backend Developer's methods
 */

public interface FlightFrontendInterface {
  // public FlightFrontendXX(Scanner userInput, FlightBackendInterface backend);

  public void loadData() throws FileNotFoundException;

  public void searchByAirline(String airline);

  public void searchWithAirport(String airport);

  public void searchWithoutAirport(String airport);

  public void search();

  public void statistics();

  public void setDeparture(String departure);

  public void setDestination(String destination);


}
