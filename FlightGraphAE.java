// --== CS400 File Header Information ==--
// Name: <Krishna Kalappareddigari>
// Email: <kalappareddi@wisc.edu>
// Group and Team: <BI Red>
// Group TA: <Naman Gupta>
// Lecturer: <Gary Dahl>
// Notes to Grader: <optional extra notes>

import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class FlightGraphAE<NodeType, EdgeType extends Number> extends
    BaseGraph<NodeType, EdgeType> implements FlightGraphInterface<NodeType, EdgeType> {

  /**
   * While searching for the shortest path between two nodes, a SearchNode contains data about one
   * specific path between the start node and another node in the graph. The final node in this path
   * is stored in it's node field. The total cost of this path is stored in its cost field. And the
   * predecessor SearchNode within this path is referenced by the predecessor field (this field is
   * null within the SearchNode containing the starting node in it's node field).
   *
   * SearchNodes are Comparable and are sorted by cost so that the lowest cost SearchNode has the
   * highest priority within a java.util.PriorityQueue.
   */
  protected class SearchNode implements Comparable<SearchNode> {
    public Node node;
    public double cost;
    public SearchNode predecessor;

    public SearchNode(Node node, double cost, SearchNode predecessor) {
      this.node = node;
      this.cost = cost;
      this.predecessor = predecessor;
    }

    public int compareTo(SearchNode other) {
      if (cost > other.cost)
        return +1;
      if (cost < other.cost)
        return -1;
      return 0;
    }
  }

  /**
   * This helper method creates a network of SearchNodes while computing the shortest path between the
   * provided start and end locations. The SearchNode that is returned by this method is represents
   * the end of the shortest path that is found: it's cost is the cost of that shortest path, and the
   * nodes linked together through predecessor references represent all of the nodes along that
   * shortest path (ordered from end to start).
   *
   * @param start the data item in the starting node for the path
   * @param end   the data item in the destination node for the path
   * @return SearchNode for the final end node within the shortest path
   * @throws NoSuchElementException when no path from start to end is found or when either start or
   *                                end data do not correspond to a graph node
   */
  protected SearchNode computeShortestPath(NodeType start, NodeType end) {
    // TODO: implement in step 6
    // Check if start and end nodes exist in the graph
    if (!containsNode(start) || !containsNode(end)) {
      throw new NoSuchElementException("no node");
    }
    // Create a Hashtable to store SearchNodes for all graph node
    Hashtable<NodeType, SearchNode> hash = new Hashtable<>();
    // Create a PriorityQueue to store SearchNodes in increasing cost
    PriorityQueue<SearchNode> queue = new PriorityQueue<>();

    // Initialize SearchNodes for all graph nodes and set cost to infinity while adding to Hashtable
    for (Node eachNode : nodes.values()) {
      SearchNode add = new SearchNode(eachNode, Double.POSITIVE_INFINITY, null);
      hash.put(eachNode.data, add);
    }

    // Set cost of start node to 0 and add to PriorityQueue
    SearchNode startNode = hash.get(start);
    startNode.cost = 0;
    queue.add(startNode);

    // Loop until PriorityQueue is empty
    while (!queue.isEmpty()) {
      // Get the lowest cost SearchNode from PriorityQueue
      SearchNode current = queue.poll();
      Node currNode = current.node;

      // Check if current SearchNode is the end node and return if true
      if (currNode.data.equals(end)) {
        return current;
      }

      // Loop through each edge leaving the current node
      for (Edge edge : current.node.edgesLeaving) {
        // Get the successor node of the current edge
        SearchNode successorNode = hash.get(edge.successor.data);

        // Calculate the new cost to reach the successor node with current edge
        double newCost = current.cost + edge.data.doubleValue();
        // Update the cost of predecessor and successor node if new cost is less than current cost
        if (newCost < successorNode.cost) {
          successorNode.cost = newCost;
          successorNode.predecessor = current;
          // Add the updated successor node to PriorityQueue to check all visited nodes
          if (!queue.contains(successorNode)) {
            queue.add(successorNode);
          }
        }
      }
    }


    throw new NoSuchElementException("No path found");
  }

  /**
   * Returns the list of data values from nodes along the shortest path from the node with the
   * provided start value through the node with the provided end value. This list of data values
   * starts with the start value, ends with the end value, and contains intermediary values in the
   * order they are encountered while traversing this shorteset path. This method uses Dijkstra's
   * shortest path algorithm to find this solution.
   *
   * @param start the data item in the starting node for the path
   * @param end   the data item in the destination node for the path
   * @return list of data item from node along this shortest path
   */
  public List<NodeType> shortestPathData(NodeType start, NodeType end) {
    // TODO: implement in step 7
    SearchNode endNode = computeShortestPath(start, end);
    // Create an empty path list to store the node data in the shortest path
    LinkedList<NodeType> path = new LinkedList<>();
    SearchNode currNode = endNode;
    // Starting from the end node add each node data to the beginning of path list
    while (currNode != null) {
      path.add(0, currNode.node.data);
      // Follow the predecessor references to traverse nodes in shortest path from end to start
      currNode = currNode.predecessor;
    }
    return path;
  }

  /**
   * Returns the cost of the path (sum over edge weights) of the shortest path freom the node
   * containing the start data to the node containing the end data. This method uses Dijkstra's
   * shortest path algorithm to find this solution.
   *
   * @param start the data item in the starting node for the path
   * @param end   the data item in the destination node for the path
   * @return the cost of the shortest path between these nodes
   */
  public double shortestPathCost(NodeType start, NodeType end) {
    // TODO: implement in step 7
    SearchNode endNode = computeShortestPath(start, end);
    return endNode.cost;
  }

  /**
   * Retrieves a list of all the airport nodes in the graph.
   * @return A list of NodeType objects representing the airport nodes in the graph.
  */
  @Override
  public List<NodeType> getAirports() {
    // TODO Auto-generated method stub
    List<NodeType> list = new ArrayList<NodeType>();
    // loop through each node in the graph
    for (Node eachNode : nodes.values()) {
      // add the data of each node
      list.add(eachNode.data);
    }
    return list;
  }

  /**
   * Retrieves a list of all possible routes between two nodes (airports) in the graph.
   * @param origin the starting airport node
   * @param destination the destination airport node
   * @return A list of lists of NodeType objects representing all possible routes between the origin and destination airports.
  */
  @Override
  public List<List<NodeType>> getRoutes(NodeType origin, NodeType destination) {
    // Create an empty list to store all routes
    List<List<NodeType>> allRoutes = new ArrayList<>();
    // Create an empty list to store the current route being traversed
    List<NodeType> currentRoute = new ArrayList<>();
    // Call the recursive helper method to find all routes
    getRoutesHelper(origin, destination, currentRoute, allRoutes);
    return allRoutes;
  }

  /**
   * Helper method that recursively finds all possible routes between the node with the provided start
   * value and the node with the provided end value. Each route is represented by a list of data
   * values from nodes along that route.
   *
   * @param currNode     the current node being traversed
   * @param end          the data item in the destination node for the routes
   * @param currentRoute the current route being traversed
   * @param allRoutes    the list of all routes found so far
   */
  private void getRoutesHelper(NodeType currNode, NodeType end,
      List<NodeType> currentRoute, List<List<NodeType>> allRoutes) {
    // Add the current node to the current route
    currentRoute.add(currNode);
    // If the current node is the end node, add the current route to the list of all routes
    if (currNode.equals(end)) {
      allRoutes.add(new ArrayList<NodeType>(currentRoute));
    } else {
      // If the current node is not the end node, recursively traverse all its neighbors
      for (Node node : nodes.values()) {
        if (node.data.equals(currNode)) {
          for (Edge edge : node.edgesLeaving) {
            // Check if the neighbor node has already been visited in the current route to avoid cycles
            if (!currentRoute.contains(edge.successor.data)) {
              // Recursively traverse the neighbor node
              getRoutesHelper(edge.successor.data, end, currentRoute, allRoutes);
            }
          }
        }
      }
    }
    // Remove the current node from the current route before returning from the recursion
    currentRoute.remove(currentRoute.size() - 1);
  }


  /**
   * Retrieves the shortest route between two nodes (airports) in the graph that does not pass 
   * through a given airport node.
   * @param origin the starting airport node
   * @param destination the destination airport node
   * @param toFilter the airport node to avoid passing through
   * @return A list of NodeType objects representing the shortest route between the origin
   *  and destination airports that does not pass through the given airport node.
  */
  @Override
  public List<NodeType> filterWithoutAirport(NodeType origin, NodeType destination,
      NodeType toFilter) {
    // get all possible routes between origin and destination airports
    List<List<NodeType>> allRoutes = getRoutes(origin, destination);
    List<List<NodeType>> filteredRoutes = new ArrayList<>();
    
    // filter routes that don't pass through the given airport node
    for(List<NodeType> list: allRoutes) {
      boolean check = true;
      // loop to find routes without filter
      for(int i = 0; i < list.size(); i++) {
        if(list.get(i).equals(toFilter)) {
          check = false;
        }
      }
      // add to filtered list
      if(check == true) {
        filteredRoutes.add(list);
      }
    }
    
    
    // find the shortest route among the filtered routes
    double current = 0.0;
    List<NodeType> shortest = null;
    for(List<NodeType> list: filteredRoutes) {
      double compare = 0.0;
      // loop to find cost of each route
      for(int i = 0; i < list.size() - 1; i++) {
        NodeType node1 = (NodeType) list.get(i);
        NodeType node2 = (NodeType) list.get(i + 1);
        compare += getEdge(node1, node2).doubleValue();
      }
      // compare to check what's the cheapest route
      if(current == 0.0) {
        current = compare;
        shortest = list;
      }
      else if(compare < current) {
        current = compare;
        shortest = list;
      }
    }
    
    return shortest;
  }

  /**
   * Returns the shortest route between two nodes, filtering only those that include a 
   * specific airport.
   * @param origin the starting node
   * @param destination the destination node
   * @param toFilter the airport node to be included in the routes
   * @return the shortest route between the origin and destination that includes the toFilter node
  */
  @Override
  public List<NodeType> filterWithAirport(NodeType origin, NodeType destination,
      NodeType toFilter) {
    // Get all possible routes between origin and destination
    List<List<NodeType>> allRoutes = getRoutes(origin, destination);
    List<List<NodeType>> filteredRoutes = new ArrayList<>();
    
    // Filter out routes that include the toFilter node
    for(List<NodeType> list: allRoutes) {
      boolean check = false;
      // loop to find routes without filter
      for(int i = 0; i < list.size(); i++) {
        if(list.get(i).equals(toFilter)) {
          check = true;
        }
      }
      // add to filtered list
      if(check == true) {
        filteredRoutes.add(list);
      }
    }
    
    // Find the shortest route among the filtered routes
    double current = 0.0;
    List<NodeType> shortest = null;
    for(List<NodeType> list: filteredRoutes) {
      double compare = 0.0;
      // loop to find cost of each route
      for(int i = 0; i < list.size() - 1; i++) {
        NodeType node1 = (NodeType) list.get(i);
        NodeType node2 = (NodeType) list.get(i + 1);
        compare += getEdge(node1, node2).doubleValue();
      }
      // compare to check what's the cheapest route
      if(current == 0.0) {
        current = compare;
        shortest = list;
      }
      else if(compare < current) {
        current = compare;
        shortest = list;
      }
    }
    
    return shortest;
  }

}
