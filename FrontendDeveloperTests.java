// --== CS400 Spring 2023 File Header Information ==--
// Name: Vikash Kaushik
// Email: vkaushik3@wisc.edu
// Group and Team: BI
// Group TA: Naman Gupta
// Lecturer: Gary Dahl
// Notes to Grader: <optional extra notes>

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.BeforeEach;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.TextField;

import edu.wisc.cs.cs400.JavaFXTester;

/**
 * This class represents a tester class that tests the methods of the FlightFrontendFD.java class.
 */

public class FrontendDeveloperTests extends JavaFXTester {
  /**
   * Constructor for this class that makes a call to the super class that we are testing
   */
  public FrontendDeveloperTests() {
    super(FlightFrontendFD.class);
  }

  /**
   * This method tests that if you put no input into the text boxes, that it displays the error
   * message on the bottom.
   */
  @Test
  void test1() {
    // test no input
    // click the shortest path button
    clickOn("#shortestPathButton");
    // look at what the label says
    Label label = lookup("#errorMessageLabel").query();

    // should display error message on bottom to type in a source and destination.
    assertEquals("source or destination is blank", label.getText());


  }

  /**
   * This method tests that if you put input a source and destination, then statistics are displayed
   * on the screen when you press the button.
   */
  @Test
  void test2() {
    // statistics work
    // find the textbox and set its text to the source "SF"
    TextField source = lookup("#textFieldSource").query();
    source.setText("DEN");

    // find the textbox and set its text to the destination "LA"
    TextField destination = lookup("#textFieldDestination").query();
    destination.setText("DEA");


    // click the shortest path button
    clickOn("#shortestPathButton");


    // find what the statistics label says
    Label label = lookup("#statisticsOutput").query();

    // should return this output string of statistics
    String outputString = "Here are the statistics!" + "\n" + "Number of Airports: " + "3257"
        + "\n\nNumber of Airlines: " + "565";
    assertEquals(true, label.getText().equals(outputString));

  }

  /**
   * The method checks that all the Labels are displayed on the screen.
   */
  @Test
  void test3() {
    // test all labels are there
    // find all the labels and what they store
    Label label = lookup("#title").query();
    Label label2 = lookup("#source").query();
    Label label3 = lookup("#destination").query();
    Label label4 = lookup("#airline").query();
    Label label5 = lookup("#airportInclude").query();
    Label label6 = lookup("#airportExclude").query();
    Label label7 = lookup("#shortestRoute").query();



    // they should all be equal to their respective values that are displayed on the screen
    assertEquals("Flight Planner", label.getText());
    assertEquals("Name of Source", label2.getText());
    assertEquals("Name of Destination", label3.getText());
    assertEquals("Name of Airline", label4.getText());
    assertEquals("Name of Airport to Include", label5.getText());
    assertEquals("Name of Airport to Exclude", label6.getText());
    assertEquals("The shortest route", label7.getText());

  }

  /**
   * This method tests that if you type into the text box, then the text retrieved will infact be
   * the text the user typed in after they click the button.
   */
  @Test
  void test4() {
    // typing into text field works
    // find the text box and set its text to be "SF"
    TextField source = lookup("#textFieldSource").query();

    source.setText("DEN");
    // when you click the shortest path button, the text should be saved
    clickOn("#shortestPathButton");

    // the text should be equal to SF after the button is pressed.
    assertEquals("DEN", source.getText());

  }

  /**
   * This method tests that if you type in a source and destination and click the button, then a
   * shortest path is displayed on the screen.
   */
  @Test
  void test5() {
    // check that shortest path prints out
    // find the text field and set the source text field to be "SF"
    TextField source = lookup("#textFieldSource").query();
    source.setText("DEN");

    // find the text field and set the destination text field to be "LA"
    TextField destination = lookup("#textFieldDestination").query();
    destination.setText("DEA");

    // click on the shortest path button
    clickOn("#shortestPathButton");

    // create an output string that represents the right output
    String outputString = "Here is the shortest route!" + "\n" + "DEN-> " + "KEF-> " + "OSL-> "
        + "ISB-> " + "LHE-> " + "DEA";

    // find the output label and what it displays
    Label outputLabel = lookup("#outputLabelRegular").query();


    // the label should display the output message that represents the shortest route
    assertEquals(true, outputLabel.getText().equals(outputString));

  }

  /**
   * This method tests the backend developer's methods getStatisticsString() and loadData().
   */
  @Test
  void testCodeReviewOfBackendDeveloper1() {
    // creates a backend object
    FlightBackendBD b1 = new FlightBackendBD();
    // creates variable to see if it went in catch when it shouldn't
    boolean wentInCatch = false;
    try {
      // load the data
      b1.loadData("flights.gv");
    } catch (FileNotFoundException f) {
      // exception shouldn't be caught
      wentInCatch = true;
    }
    // the boolean should reamin false
    assertEquals(false, wentInCatch);

    // create a string that holds what getStatisticsString() returns
    String s1 = b1.getStatisticsString();

    // the output string here should be what the method returns
    String outputString = "Number of Airports: " + "3257" + "\n\nNumber of Airlines: " + "565";
    assertEquals(outputString, s1);

  }

  /**
   * This method tests the backend developer's method findShortestRoute().
   */
  @Test
  void testCodeReviewOfBackendDeveloper2() {
    // creates a backend object
    FlightBackendBD b1 = new FlightBackendBD();
    // creates variable to see if it went in catch when it shouldn't
    boolean wentInCatch = false;
    try {
      // load the data
      b1.loadData("flights.gv");
    } catch (FileNotFoundException f) {
      // exception shouldn't be caught
      wentInCatch = true;
    }
    // the boolean should reamin false
    assertEquals(false, wentInCatch);

    // create an array list that holds what the method returns.
    ArrayList<String> s1 = (ArrayList<String>) b1.findShortestRoute("DEN", "DEA");

    // check the contents of the array list and check its size
    assertEquals("DEN", s1.get(0));
    assertEquals("KEF", s1.get(1));
    assertEquals("OSL", s1.get(2));
    assertEquals("ISB", s1.get(3));
    assertEquals("LHE", s1.get(4));
    assertEquals("DEA", s1.get(5));
    assertEquals(6, s1.size());

  }

  /**
   * This method tests the tests BE, FE, AE, DW by checking that it displays the shortest path for
   * when excluding an airport.
   */
  @Test
  void testIntegration1() {
    // check that shortest path prints out
    // find the text field and set the source text field to be "DEN"
    TextField source = lookup("#textFieldSource").query();
    source.setText("DEN");

    // find the text field and set the destination text field to be "DEA"
    TextField destination = lookup("#textFieldDestination").query();
    destination.setText("DEA");

    // find the text field and set the airportExclude text field to be "OSL"
    TextField airportExclude = lookup("#textFieldAirportExclude").query();
    airportExclude.setText("OSL");

    // click on the shortest path button
    clickOn("#shortestPathButton");

    // create an output string that represents the right output
    String outputString =
        "Here is the shortest route!" + "\n" + "DEN-> " + "KEF-> " + "CPH-> " + "LHE-> " + "DEA";

    // find the output label and what it displays
    Label outputLabel = lookup("#outputLabelRegular").query();


    // the label should display the output message that represents the shortest route
    assertEquals(true, outputLabel.getText().equals(outputString));

  }

  /**
   * This method tests the tests BE, FE, AE, DW by checking that it displays the shortest path for
   * when including an airport.
   */
  @Test
  void testIntegration2() {
    // check that shortest path prints out
    // find the text field and set the source text field to be "DEN"
    TextField source = lookup("#textFieldSource").query();
    source.setText("DEN");

    // find the text field and set the destination text field to be "DEA"
    TextField destination = lookup("#textFieldDestination").query();
    destination.setText("DEA");

    // find the text field and set the airportInclude text field to be "ORD"
    TextField airportInclude = lookup("#textFieldAirportInclude").query();
    airportInclude.setText("ORD");

    // click on the shortest path button
    clickOn("#shortestPathButton");

    // create an output string that represents the right output
    String outputString =
        "Here is the shortest route!" + "\n" + "DEN-> " + "ORD-> " + "YYZ-> " + "LHE-> " + "DEA";

    // find the output label and what it displays
    Label outputLabel = lookup("#outputLabelRegular").query();


    // the label should display the output message that represents the shortest route
    assertEquals(true, outputLabel.getText().equals(outputString));

  }


}

