package controler;
import model.Map;
import model.Path;
import model.Request;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Algorithme {

    protected Map map;
    protected List<Request> listRequests;
    protected final long timeZero;
    protected final long TIMEOUT = 20000;

    public Algorithme(Map map, List<Request> listRequests) {
        this.map = map;
        this.listRequests = listRequests;
        this.timeZero = System.currentTimeMillis();

        HashMap<Long, List<Path>> mapSmallestPaths = this.computeSmallestPaths();
        LinkedList<Path> optimalTour = this.computeOptimalTour(mapSmallestPaths);
    }

    public HashMap<Long, List<Path>> computeSmallestPaths() {
        System.out.println("Computing the smallest paths...");
        HashMap<Long, List<Path>> mapSmallestPaths = new HashMap<>();
        while (System.currentTimeMillis() - this.timeZero < this.TIMEOUT) {
            /*
             * Algo
             */
        }
        System.out.println("Smallest paths computed.");
        return mapSmallestPaths;
    }

    public LinkedList<Path> computeOptimalTour(HashMap<Long, List<Path>> mapSmallestPaths) {
        System.out.println("Computing the optimal tour...");
        LinkedList<Path> optimalTour = new LinkedList<>();
        while (System.currentTimeMillis() - this.timeZero < this.TIMEOUT) {
            /*
             * Algo
             */
        }
        System.out.println("Optimal tour computed.");
        return optimalTour;
    }
}
