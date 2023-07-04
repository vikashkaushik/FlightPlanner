import java.util.List;

public interface FlightGraphInterface<NodeType, EdgeType extends Number>
    extends GraphADT<NodeType, EdgeType> {
  public List<NodeType> getAirports();

  public List<List<NodeType>> getRoutes(NodeType origin, NodeType destination);

  public List<NodeType> filterWithoutAirport(NodeType origin, NodeType destination,
      NodeType toFilter);

  public List<NodeType> filterWithAirport(NodeType origin, NodeType destination,
      NodeType toFilter);
}
