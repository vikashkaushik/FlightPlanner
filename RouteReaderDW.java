import java.io.FileNotFoundException;
import java.util.List;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

// graphviz library found here: http://www.alexander-merz.com/graphviz/
// yeah this is licensed under the LGPL but I'm not distributing this program and nobody has to know ;)
import com.alexmerz.graphviz.Parser;
import com.alexmerz.graphviz.objects.Edge;
import com.alexmerz.graphviz.objects.Graph;
import com.alexmerz.graphviz.objects.Id;
import com.alexmerz.graphviz.objects.Node;
import com.alexmerz.graphviz.objects.PortNode;

/**
 * A route reader class which reads flight data from a dot file
 */
public class RouteReaderDW implements RouteReaderInterface {
    /**
     * A helper method to get an airport from the airports list
     * 
     * @param airports the airports list
     * @param node     the node to search for
     * @return the airport or null if not found (should not happen)
     */
    private AirportInterface getAirport(ArrayList<AirportInterface> airports, PortNode node) {
        for (AirportInterface airport : airports) {
            if (airport.getName().equals(node.getNode().getId().getId())) {
                return airport;
            }
        }

        return null;
    }

    /**
     * Reads airports and routes from a file, returning a list of routes in the file
     * 
     * @param filename the filename to read from
     * @throws FileNotFoundException when the file does not exist or cannot be
     *                               parsed
     * @return the list of routes in the file
     */
    public List<RouteInterface> readFromFile(String filename) throws FileNotFoundException {
        FileReader in = null;
        File f = new File(filename);
        Parser p = null;

        // load and parse file
        try {
            in = new FileReader(f);
            p = new Parser();
            p.parse(in);
        } catch (Exception e) {
            // uh oh!
            throw new FileNotFoundException("Invalid file " + e.toString());
        }

        // the dot file library provides a list of graphs in the dot file.
        // our code will only be using the first graph.
        ArrayList<Graph> graphList = p.getGraphs();
        Graph g = graphList.get(0);

        ArrayList<AirportInterface> airports = new ArrayList<>();
        ArrayList<RouteInterface> routes = new ArrayList<>();

        // iter through the nodes, adding them as airports to the list
        for (Node node : g.getNodes(true)) {
            Id id = node.getId(); // the ID contains info about the node
            AirportInterface airport = new AirportDW(id.getId());
            airports.add(airport);
        }

        // iter through the edges
        for (Edge edge : g.getEdges()) {
            PortNode source = edge.getSource();
            PortNode target = edge.getTarget();
            Double distance = Double.parseDouble(edge.getAttribute("weight"));

            // add each airline along the route as a separate route to the list
            for (String airline : edge.getAttribute("airlines").split(",")) {
                RouteInterface route = new RouteDW(getAirport(airports, source), getAirport(airports, target), airline,
                        distance.doubleValue());
                routes.add(route);
            }
        }

        return routes;
    }
}
