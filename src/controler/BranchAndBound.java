package controler;

import model.Path;
import model.Tour;

import java.util.*;

public class BranchAndBound {
    // Java program to solve Traveling Salesman Problem
    private int numberOfStep = 0;
    private model.Map map;

    // final_path stores the final solution ie, the
    // path of the salesman.
    private Tour final_path;

    // visited[] keeps track of the already visited nodes
    // in a particular path
    private HashMap<Long,Boolean> visited;

    // Stores the final minimum weight of shortest tour.
    private int final_res = Integer.MAX_VALUE;

    public BranchAndBound(model.Map newMap, HashMap<Long, List<Path>> mapSmallestPaths) {
        numberOfStep = mapSmallestPaths.size();
        map = newMap;
        visited = new HashMap<Long,Boolean>();
        for(Map.Entry<Long,List<Path>> mapentry : mapSmallestPaths.entrySet()) {
            visited.put(mapentry.getKey(),false);
        }
    }


    // Function to copy temporary solution to
    // the final solution
    private void copyToFinal(Tour curr_path) {
        final_path.getListPaths().clear();
        for (Path path: curr_path.getListPaths()) {
            final_path.addPath(path);
        }
        final_path.addPath(curr_path.getListPaths().get(0));
    }

    // Function to find the minimum edge cost
    // having an end at the vertex i
    private int firstMin(HashMap<Long, List<Path>> mapSmallestPaths, long id) {
        int min = Integer.MAX_VALUE;
        for(Path currentPath : mapSmallestPaths.get(id)) {
            int pathlength = currentPath.getPathLength();
            if ( pathlength < min ) {
                min = pathlength;
            }
        }
        return min;
    }

    // function to find the second minimum edge cost
    // having an end at the vertex i
    private int secondMin(HashMap<Long, List<Path>> mapSmallestPaths, long id) {
        int first = Integer.MAX_VALUE, second = Integer.MAX_VALUE;
        for(Path currentPath : mapSmallestPaths.get(id)) {
            int pathlength = currentPath.getPathLength();
            if (pathlength <= first) {
                second = first;
                first = pathlength;
            } else if (pathlength <= second &&
                    pathlength != first) {
                second = pathlength;
            }
        }
        return second;
    }


    // function that takes as arguments:
    // curr_bound -> lower bound of the root node
    // curr_weight-> stores the weight of the path so far
    // level-> current level while moving in the search
    //         space tree
    // curr_path[] -> where the solution is being stored which
    //             would later be copied to final_path[]
    private void TSPRec(HashMap<Long, List<Path>> mapSmallestPaths, int curr_bound, int curr_weight, int level, Tour curr_tour) {
        // base case is when we have reached level N which
        // means we have covered all the nodes once
        if (level == numberOfStep) {
            // check if there is an edge from last vertex in
            // path back to the first vertex
            //for the last point of curr_path, find the way to the initial point (the depot)
            for(Path pathStatedFromIndex : mapSmallestPaths.get(curr_tour.getListPaths().get(level-1).getIdArrival())) {
                int sizePathStatedFromIndex = pathStatedFromIndex.getListSegments().size();
                if (pathStatedFromIndex.getListSegments().get(sizePathStatedFromIndex - 1).getDestination() == map.getDepot().getId()) {
                    // curr_res has the total weight of the
                    // solution we got
                    int curr_res = curr_weight + pathStatedFromIndex.getPathLength();

                    // Update final result and final path if
                    // current result is better.
                    if (curr_res < final_res) {
                        copyToFinal(curr_tour);
                        final_res = curr_res;
                    }
                }
            }
            return;
        }
        //je comprends plus rien à partir d'ici
        // for any other level iterate for all vertices to
        // build the search space tree recursively
        for(Path pathStatedFromIndex : mapSmallestPaths.get(curr_tour.getListPaths().get(level-1).getIdArrival())) {
            // Consider next vertex if it is not same (diagonal
            // entry in adjacency matrix and not visited
            // already)
            if (pathStatedFromIndex.getPathLength() != 0 && visited.get(pathStatedFromIndex.getIdArrival()) == false) {
                int temp = curr_bound;
                curr_weight += pathStatedFromIndex.getPathLength();

                // different computation of curr_bound for
                // level 2 from the other levels
                if (level == 1)
                    curr_bound -= ((firstMin(mapSmallestPaths, curr_tour.getListPaths().get(level - 1).getIdArrival()) +
                            firstMin(mapSmallestPaths, pathStatedFromIndex.getIdArrival())) / 2);
                else
                    curr_bound -= ((secondMin(mapSmallestPaths, curr_tour.getListPaths().get(level - 1).getIdArrival()) +
                            firstMin(mapSmallestPaths, pathStatedFromIndex.getIdArrival())) / 2);

                // curr_bound + curr_weight is the actual lower bound
                // for the node that we have arrived on
                // If current lower bound < final_res, we need to explore
                // the node further
                if (curr_bound + curr_weight < final_res) {
                    curr_tour.addPath(pathStatedFromIndex);
                    visited.put(pathStatedFromIndex.getIdArrival(),true);

                    // call TSPRec for the next level
                    TSPRec(mapSmallestPaths, curr_bound, curr_weight, level + 1,
                            curr_tour);
                }

                // Else we have to prune the node by resetting
                // all changes to curr_weight and curr_bound
                curr_weight -= pathStatedFromIndex.getPathLength();
                curr_bound = temp;

                // Also reset the visited array
                for(Map.Entry<Long,List<Path>> mapentry : mapSmallestPaths.entrySet()) {
                    visited.put(mapentry.getKey(),false);
                }
                for (Path visitedPath : curr_tour.getListPaths())
                {
                    visited.put(visitedPath.getIdArrival(),true);
                }
                visited.put(map.getDepot().getId(),true);
            }
        }
    }
    /*
    // This function sets up final_path[]
    private void TSP(HashMap<Long, List<Path>> mapSmallestPaths) {
        Tour curr_tour = new Tour();

        // Calculate initial lower bound for the root node
        // using the formula 1/2 * (sum of first min +
        // second min) for all edges.
        // Also initialize the curr_path and visited array
        int curr_bound = 0;

        // Compute initial bound
        for (int i = 0; i < N; i++)
            curr_bound += (firstMin(adj, i) +
                    secondMin(adj, i));

        // Rounding off the lower bound to an integer
        curr_bound = (curr_bound == 1) ? curr_bound / 2 + 1 :
                curr_bound / 2;

        // We start at vertex 1 so the first vertex
        // in curr_path[] is 0
        visited.put(map.getDepot().getId(),true);
        curr_tour.addPath(mapSmallestPaths.get(map.getDepot().getId()).get(0));

        // Call to TSPRec for curr_weight equal to
        // 0 and level 1
        TSPRec(mapSmallestPaths, curr_bound, 0, 1, curr_tour);
    }

    // Driver code
    public static void main(String[] args) {
        //Adjacency matrix for the given graph
        int adj[][] = {{0, 10, 15, 20},
                {10, 0, 35, 25},
                {15, 35, 0, 30},
                {20, 25, 30, 0}};

        TSP(adj);

        System.out.printf("Minimum cost : %d\n", final_res);
        System.out.printf("Path Taken : ");
        for (int i = 0; i <= N; i++) {
            System.out.printf("%d ", final_path[i]);
        }
    }
 */
}
