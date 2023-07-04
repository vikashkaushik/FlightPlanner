// --== CS400 Spring 2023 File Header Information ==--
// Name: Vikash Kaushik
// Email: vkaushik3@wisc.edu
// Group and Team: BI
// Group TA: Naman Gupta
// Lecturer: Gary Dahl
// Notes to Grader: <optional extra notes>

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.layout.HBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;

/**
 * This class represents the Frontend Developer class that creates the JavaFX web page and calls
 * Backend Developer methods to display them on the screen. This class will display the shortest
 * route to the user.
 *
 */

public class FlightFrontendFD extends Application implements FlightFrontendInterface {
  // public FlightFrontendXX(Scanner userInput, FlightBackendInterface backend);
  private String source; // source
  private String destination; // destination
  private String airline; // airline
  private String airportToInclude; // aiport to include
  private String airportToExclude; // airport to exclude
  private BorderPane borderPane; // create a border pane
  private FlightBackendBD backend;

  /**
   * This method contains the main coding section: contains the labels, textboxes, buttons that user
   * can see on the screen. Also calls backend methods for its functionality.
   * 
   * @param takes in a Stage object
   */
  @Override
  public void start(final Stage stage) throws FileNotFoundException {
    // start by loading the data
    backend = new FlightBackendBD();
    loadData();
    // intialize the border pane
    borderPane = new BorderPane();
    // set ID for border pane for testing
    borderPane.setId("borderPane");
    // set the title for the stage
    stage.setTitle("Flight Planner");

    // create a text field for every single instance variable, set an Id for testing purposes, and
    // set its dimensions
    TextField textFieldSource = new TextField();
    textFieldSource.setLayoutX(200);
    textFieldSource.setLayoutY(100);
    textFieldSource.setId("textFieldSource");

    TextField textFieldDestination = new TextField();
    textFieldDestination.setLayoutX(200);
    textFieldDestination.setLayoutY(400);
    textFieldDestination.setId("textFieldDestination");


    TextField textFieldAirline = new TextField();
    textFieldAirline.setLayoutX(200);
    textFieldAirline.setLayoutY(500);
    textFieldAirline.setId("textFieldAirline");



    TextField textFieldAirportInclude = new TextField();
    textFieldAirportInclude.setLayoutX(200);
    textFieldAirportInclude.setLayoutY(600);
    textFieldAirportInclude.setId("textFieldAirportInclude");

    TextField textFieldAirportExclude = new TextField();
    textFieldAirportExclude.setLayoutX(200);
    textFieldAirportExclude.setLayoutY(700);
    textFieldAirportExclude.setId("textFieldAirportExclude");



    // create a Vboc that contains the text fields.
    VBox vbox2 = new VBox();
    vbox2.setPadding(new Insets(120));
    vbox2.setSpacing(25);
    vbox2.getChildren().add(textFieldSource);
    vbox2.getChildren().add(textFieldDestination);
    vbox2.getChildren().add(textFieldAirline);
    vbox2.getChildren().add(textFieldAirportInclude);
    vbox2.getChildren().add(textFieldAirportExclude);

    // add button that will display shortest path
    Button shortestPathButton = new Button("search path");
    // set ID for testing
    shortestPathButton.setId("shortestPathButton");
    // add the button to vbox2
    vbox2.getChildren().add(shortestPathButton);
    // when the button is pressed:
    shortestPathButton.setOnAction(event -> {
      // retrieve all the information from the text boxes and update the instance variables.
      source = textFieldSource.getText();
      destination = textFieldDestination.getText();
      airline = textFieldAirline.getText();
      airportToInclude = textFieldAirportInclude.getText();
      airportToExclude = textFieldAirportExclude.getText();

      // if the source or destination are blank
      if ((source.isBlank()) || (destination.isBlank())) {
        // create a label and display error message in the bottom of the border pane
        Label errorMessageLabel = new Label("source or destination is blank");
        errorMessageLabel.setMinWidth(550);
        errorMessageLabel.setMinHeight(550);
        errorMessageLabel.setFont(new Font("Arial", 25));
        errorMessageLabel.setTextFill(Color.color(0, 0, 1));

        borderPane.setBottom(errorMessageLabel);

        // set ID for testing
        errorMessageLabel.setId("errorMessageLabel");
      }

      // only one of 3 options can be selected at once
      // if airline and airport to include are both chosen
      if ((!airline.isBlank()) && (!airportToInclude.isBlank()) && (airportToExclude.isBlank())) {
        // create a label and display error message in the bottom of the border pane
        Label errorMessageLabel =
            new Label("can't choose an airline and include aiport at the same time");
        errorMessageLabel.setMinWidth(550);
        errorMessageLabel.setMinHeight(550);
        errorMessageLabel.setFont(new Font("Arial", 25));
        errorMessageLabel.setTextFill(Color.color(0, 0, 1));

        borderPane.setBottom(errorMessageLabel);
      }
      // if airline and airport and both chosen
      if ((!airline.isBlank()) && (airportToInclude.isBlank()) && (!airportToExclude.isBlank())) {
        // create a label and display error message in the bottom of the border pane
        Label errorMessageLabel =
            new Label("can't choose an airline and exclude aiport at the same time");
        errorMessageLabel.setMinWidth(550);
        errorMessageLabel.setMinHeight(550);
        errorMessageLabel.setFont(new Font("Arial", 25));
        errorMessageLabel.setTextFill(Color.color(0, 0, 1));
        borderPane.setBottom(errorMessageLabel);
      }
      // if airport to include and airport to exclude are both chosen
      if ((airline.isBlank()) && (!airportToInclude.isBlank()) && (!airportToExclude.isBlank())) {
        // create a label and display error message in the bottom of the border pane
        Label errorMessageLabel = new Label("can't include and exclude aiport at the same time");
        errorMessageLabel.setMinWidth(550);
        errorMessageLabel.setMinHeight(550);
        errorMessageLabel.setFont(new Font("Arial", 25));
        errorMessageLabel.setTextFill(Color.color(0, 0, 1));
        borderPane.setBottom(errorMessageLabel);
      }
      // if all 3 are chosen at the same time
      if ((!airline.isBlank()) && (!airportToInclude.isBlank()) && (!airportToExclude.isBlank())) {
        // create a label and display error message in the bottom of the border pane
        Label errorMessageLabel =
            new Label("can't choose an airline, include, and exclude an aiport at the same time");
        errorMessageLabel.setMinWidth(550);
        errorMessageLabel.setMinHeight(550);
        errorMessageLabel.setFont(new Font("Arial", 25));
        errorMessageLabel.setTextFill(Color.color(0, 0, 1));
        borderPane.setBottom(errorMessageLabel);
      }



      // just source and destination filled
      if ((!source.isBlank()) && (!destination.isBlank()) && (airline.isBlank())
          && (airportToInclude.isBlank()) && (airportToExclude.isBlank())) {
        // run search and dislay the stats
        search();
        statistics();
      }
      // search with airline
      if ((!source.isBlank()) && (!destination.isBlank()) && (!airline.isBlank())
          && (airportToInclude.isBlank()) && (airportToExclude.isBlank())) {
        // call search by airline and display the stats
        searchByAirline(airline);
        statistics();
      }
      // ssearch with airport
      if ((!source.isBlank()) && (!destination.isBlank()) && (airline.isBlank())
          && (!airportToInclude.isBlank()) && (airportToExclude.isBlank())) {
        // call search with airport and display the stats
        searchWithAirport(airportToInclude);
        statistics();
      }
      // search without airport
      if ((!source.isBlank()) && (!destination.isBlank()) && (airline.isBlank())
          && (airportToInclude.isBlank()) && (!airportToExclude.isBlank())) {
        // call search without airport and display the stats
        searchWithoutAirport(airportToExclude);
        statistics();
      }

      // reset all instance variables
      source = null;
      destination = null;
      airline = null;
      airportToInclude = null;
      airportToExclude = null;
    });

    // create labels on the screen that go along with what the user needs to type in the text boxes

    // create a label that displays the title
    Label title = new Label("Flight Planner");
    title.setMinWidth(100);
    title.setMinHeight(100);
    title.setFont(new Font("Arial", 30));
    title.setId("title");

    // create a label that says type in source name
    Label source = new Label("Name of Source");
    source.setMinWidth(50);
    source.setMinHeight(50);
    source.setFont(new Font("Arial", 20));
    source.setId("source");

    // create a label that says type in destination name
    Label destination = new Label("Name of Destination");
    destination.setMinWidth(50);
    destination.setMinHeight(50);
    destination.setFont(new Font("Arial", 20));
    destination.setId("destination");

    // create a label that says type in airline name
    Label airline = new Label("Name of Airline");
    airline.setMinWidth(50);
    airline.setMinHeight(50);
    airline.setFont(new Font("Arial", 20));
    airline.setId("airline");

    // create a label that says type in airport to include name
    Label airportInclude = new Label("Name of Airport to Include");
    airportInclude.setMinWidth(50);
    airportInclude.setMinHeight(50);
    airportInclude.setFont(new Font("Arial", 20));
    airportInclude.setId("airportInclude");

    // create a label that says type in airport to exclude name
    Label airportExclude = new Label("Name of Airport to Exclude");
    airportExclude.setMinWidth(50);
    airportExclude.setMinHeight(50);
    airportExclude.setFont(new Font("Arial", 20));
    airportExclude.setId("airportExclude");

    // create a label that shows that the shortest route will be displayed
    Label shortestRoute = new Label("The shortest route");
    shortestRoute.setMinWidth(50);
    shortestRoute.setMinHeight(50);
    shortestRoute.setFont(new Font("Arial", 20));
    shortestRoute.setId("shortestRoute");


    // create a vbox that contains lal the labels
    VBox vbox = new VBox(title, source, destination, airline, airportInclude, airportExclude,
        shortestRoute);
    // all the labels will be in a vbox on the left side and all the textboxes will be in a vbox on
    // the right side
    borderPane.setLeft(vbox);
    borderPane.setCenter(vbox2);
    // put the border pane in the scene
    Scene scene = new Scene(borderPane, 1000, 1000);

    // set the scene and show it
    stage.setScene(scene);
    stage.show();

  }


  /**
   * This method is used to call the BD's method loadData().
   * 
   * @throws FileNotFoundException
   */
  public void loadData() throws FileNotFoundException {
    // calls BD's loadData()
    backend.loadData("flights.gv");
  }

  /**
   * This method is used to call the BD's method findShortestRouteForAirline().
   * 
   * @param takes in an airline that is inputted by the user
   */
  public void searchByAirline(String airline) {
    // calls BD's findShortestRouteForAirline()

    // stores the list returned back and creates a string that shows the aiports with arrows
    // representing the path taken
    ArrayList<String> a1 =
        (ArrayList<String>) backend.findShortestRouteForAirline(source, destination, airline);
    if (a1.size() == 0) {
      Label outputLabel1 = new Label("There is no route");
      outputLabel1.setId("outputLabelNoRoute");
      outputLabel1.setMinWidth(550);
      outputLabel1.setMinHeight(550);
      outputLabel1.setFont(new Font("Arial", 25));
      outputLabel1.setTextFill(Color.color(0, 0, 1));
      borderPane.setBottom(outputLabel1);
    } else {
      String outputString = "Here is the shortest route!" + "\n";
      for (int i = 0; i < a1.size(); i++) {
        if (i < a1.size() - 1) {
          outputString += a1.get(i) + "-> ";
        } else {
          outputString += a1.get(i);
        }
      }
      // display the output on the bottom of the border pane
      Label outputLabel = new Label(outputString);
      outputLabel.setId("outputLabelRegular");
      outputLabel.setMinWidth(550);
      outputLabel.setMinHeight(550);
      outputLabel.setFont(new Font("Arial", 25));
      outputLabel.setTextFill(Color.color(0, 0, 1));
      borderPane.setBottom(outputLabel);
    }
  }

  /**
   * This method is used to call the BD's method findShortestRouteWithAirport().
   * 
   * @param takes in an aiport that the user wants to include
   */
  public void searchWithAirport(String airport) {
    // calls BD's findShortestRouteWithAirport()

    // stores the list returned back and creates a string that shows the aiports with arrows
    // representing the path taken
    ArrayList<String> a1 = (ArrayList<String>) backend.findShortestRouteWithAirport(source,
        destination, airportToInclude);

    if (a1.size() == 0) {
      Label outputLabel1 = new Label("There is no route");
      outputLabel1.setMinWidth(550);
      outputLabel1.setMinHeight(550);
      outputLabel1.setFont(new Font("Arial", 25));
      outputLabel1.setTextFill(Color.color(0, 0, 1));
      borderPane.setBottom(outputLabel1);
    } else {

      String outputString = "Here is the shortest route!" + "\n";
      for (int i = 0; i < a1.size(); i++) {
        if (i < a1.size() - 1) {
          outputString += a1.get(i) + "-> ";
        } else {
          outputString += a1.get(i);
        }
      }
      // display the output on the bottom of the border pane
      Label outputLabel = new Label(outputString);
      outputLabel.setId("outputLabelRegular");
      outputLabel.setMinWidth(550);
      outputLabel.setMinHeight(550);
      outputLabel.setFont(new Font("Arial", 25));
      outputLabel.setTextFill(Color.color(0, 0, 1));
      borderPane.setBottom(outputLabel);
    }
  }

  /**
   * This method is used to call the BD's method findShortestRouteWithoutAirport().
   * 
   * @param takes in an aiport that the user wants to exclude
   */
  public void searchWithoutAirport(String airport) {
    // calls BD's findShortestRouteWithoutAirport()

    // stores the list returned back and creates a string that shows the aiports with arrows
    // representing the path taken
    ArrayList<String> a1 = (ArrayList<String>) backend.findShortestRouteWithoutAirport(source,
        destination, airportToExclude);

    if (a1.size() == 0) {
      Label outputLabel1 = new Label("There is no route");
      outputLabel1.setMinWidth(550);
      outputLabel1.setMinHeight(550);
      outputLabel1.setFont(new Font("Arial", 25));
      outputLabel1.setTextFill(Color.color(0, 0, 1));
      borderPane.setBottom(outputLabel1);
    } else {

      String outputString = "Here is the shortest route!" + "\n";
      for (int i = 0; i < a1.size(); i++) {
        if (i < a1.size() - 1) {
          outputString += a1.get(i) + "-> ";
        } else {
          outputString += a1.get(i);
        }
      }
      // display the output on the bottom of the border pane
      Label outputLabel = new Label(outputString);
      outputLabel.setId("outputLabelRegular");
      outputLabel.setMinWidth(550);
      outputLabel.setMinHeight(550);
      outputLabel.setFont(new Font("Arial", 25));
      outputLabel.setTextFill(Color.color(0, 0, 1));
      borderPane.setBottom(outputLabel);
    }
  }

  /**
   * This method is used to call the BD's method findShortestRoute(). The method just needs a source
   * and destination.
   */
  public void search() {
    // calls BD's findShortestRoute()

    // stores the list returned back and creates a string that shows the aiports with arrows
    // representing the path taken
    ArrayList<String> a1 = (ArrayList<String>) backend.findShortestRoute(source, destination);

    if (a1.size() == 0) {
      Label outputLabel1 = new Label("There is no route");
      outputLabel1.setMinWidth(550);
      outputLabel1.setMinHeight(550);
      outputLabel1.setFont(new Font("Arial", 25));
      outputLabel1.setTextFill(Color.color(0, 0, 1));
      borderPane.setBottom(outputLabel1);
    } else {

      String outputString = "Here is the shortest route!" + "\n";
      for (int i = 0; i < a1.size(); i++) {
        if (i < a1.size() - 1) {
          outputString += a1.get(i) + "-> ";
        } else {
          outputString += a1.get(i);
        }
      }
      // display the output on the bottom of the border pane
      Label outputLabel = new Label(outputString);
      outputLabel.setId("outputLabelRegular");
      outputLabel.setMinWidth(550);
      outputLabel.setMinHeight(550);
      outputLabel.setFont(new Font("Arial", 25));
      outputLabel.setTextFill(Color.color(0, 0, 1));
      borderPane.setBottom(outputLabel);
    }
  }

  /**
   * This method is used to call the BD's getStatisticsString() method. Shows the stats on the
   * screen when called.
   */
  public void statistics() {
    // calls BD's getStatisticsString()

    // Stores the string and displays in using a label
    String statistics = "Here are the statistics!" + "\n" + backend.getStatisticsString();

    Label outputLabel = new Label(statistics);
    // set ID for testing
    outputLabel.setId("statisticsOutput");

    // display the output on the bottom of the border pane
    outputLabel.setMinWidth(200);
    outputLabel.setMinHeight(200);
    outputLabel.setPadding(new Insets(20));
    outputLabel.setFont(new Font("Arial", 15));
    outputLabel.setTextFill(Color.color(1, 0, 0));
    borderPane.setRight(outputLabel);
  }

  /**
   * This method is a setter method for instance variable source
   * 
   * @param a source
   */
  public void setDeparture(String departure) {
    this.source = departure;
  }

  /**
   * This method is a setter method for instance variable destination
   * 
   * @param a destination
   */
  public void setDestination(String destination) {
    this.destination = destination;
  }



  public static void main(String[] args) {
    Application.launch();
  }

}

