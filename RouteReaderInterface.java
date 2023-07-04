import java.io.FileNotFoundException;
import java.util.List;

public interface RouteReaderInterface {
    public List<RouteInterface> readFromFile(String filename) throws FileNotFoundException;
}
