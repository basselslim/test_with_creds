package controler;

import model.*;

import java.util.*;

/**
 * Algorithm that solve Traveling Salesman Problem.
 * Source https://www.geeksforgeeks.org/travelling-salesman-problem-implementation-using-backtracking/
 *
 * @author T-REXANOME
 */
public class TravellingSalesmanProblem {

    protected int numberOfStep = 0;
    protected model.Map map;
    protected HashMap<Long, HashMap<Long, Path>> adjacencyMatrixOfShortestPath;
    protected HashMap<Long, List<Long>> precedenceOrderMatrix;
    // final_path stores the final solution ie, the
    // path of the salesman.
    protected Tour final_tour;

    // visited[] keeps track of the already visited nodes
    // in a particular path
    protected HashMap<Long, Boolean> visited;

    // Stores the final minimum weight of shortest tour.
    protected int final_res = Integer.MAX_VALUE;
    protected long timeZero;
    protected long TIMELIMIT;

    /**
     * Constructor.
     *
     * @param newMap           map object
     * @param mapSmallestPaths map of all smallest paths between intersections of the requests
     * @param t                time limit for the algorithm
     */
    public TravellingSalesmanProblem(model.Map newMap, HashMap<Long, HashMap<Long, Path>> mapSmallestPaths, long t) {
        numberOfStep = mapSmallestPaths.size();
        map = newMap;
        final_tour = new Tour(map);
        adjacencyMatrixOfShortestPath = mapSmallestPaths;
        visited = new HashMap<Long, Boolean>();
        initialisePrecedenceMatrix();
        timeZero = System.currentTimeMillis();
        TIMELIMIT = t;
    }

    /**
     *
     */
    private void initialisePrecedenceMatrix() {
        precedenceOrderMatrix = new HashMap<Long, List<Long>>();
        for (Request r : map.getListRequests()) {
            if (precedenceOrderMatrix.containsKey(r.getDeliveryPoint().getId())) {
                precedenceOrderMatrix.get(r.getDeliveryPoint().getId()).add(r.getPickUpPoint().getId());
            } else {
                List<Long> pickUpList = new LinkedList<Long>();
                pickUpList.add(r.getPickUpPoint().getId());
                precedenceOrderMatrix.put(r.getDeliveryPoint().getId(), pickUpList);
            }

        }
    }

    /**
     * Function to copy temporary solution to the final solution.
     *
     * @param curr_path current path
     */
    protected void copyToFinal(Tour curr_path) {
        final_tour.getListPaths().clear();
        for (Path path : curr_path.getListPaths()) {
            final_tour.addPath(path);
        }
        int numberOfStep = final_tour.getListPaths().size();
        Long lastStepId = final_tour.getListPaths().get(numberOfStep - 1).getIdArrival();
        Long firstStepId = final_tour.getListPaths().get(0).getIdDeparture();
        final_tour.addPath(adjacencyMatrixOfShortestPath.get(lastStepId).get(firstStepId));
    }

    /**
     * Function to find the minimum edge cost having an end at the vertex i.
     *
     * @param id
     * @return minimum edge cost
     */
    protected int firstMin(long id) {
        int min = Integer.MAX_VALUE;
        for (java.util.Map.Entry<Long, Path> pathsStartingAtId : adjacencyMatrixOfShortestPath.get(id).entrySet()) {
            int pathlength = pathsStartingAtId.getValue().getPathLength();
            if (pathlength < min) {
                min = pathlength;
            }
        }
        return min;
    }

    /**
     * Function to find the second minimum edge cost having an end at the vertex i.
     *
     * @param id
     * @return second minimum edge cost
     */
    protected int secondMin(long id) {
        int first = Integer.MAX_VALUE, second = Integer.MAX_VALUE;
        for (java.util.Map.Entry<Long, Path> pathsStartingAtId : adjacencyMatrixOfShortestPath.get(id).entrySet()) {
            int pathlength = pathsStartingAtId.getValue().getPathLength();
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

    /**
     * Set up final_tour.
     */
    public void TSP() {
        Tour curr_tour = new Tour(map);

        // Calculate initial lower bound for the root node
        // using the formula 1/2 * (sum of first min +
        // second min) for all edges.
        // Also initialize the curr_path and visited array
        int curr_bound = 0;

        // Compute initial bound
        for (java.util.Map.Entry<Long, HashMap<Long, Path>> step : adjacencyMatrixOfShortestPath.entrySet()) {
            curr_bound += (firstMin(step.getKey()) + secondMin(step.getKey()));
        }

        // Rounding off the lower bound to an integer
        curr_bound = (curr_bound == 1) ? curr_bound / 2 + 1 :
                curr_bound / 2;
        // We start at vertex 1 so the first vertex
        // in curr_path[] is 0
        visited.put(map.getDepot().getId(), true);

        // Call to TSPRec for curr_weight equal to
        // 0 and level 1
        TSPRec(curr_bound, 0, 1, curr_tour);
        map.setDeliveryTour(final_tour);
    }

    /**
     * Function that takes as arguments:
     *
     * @param curr_bound  lower bound of the current node to the end
     * @param curr_weight stores the weight of the path so far
     * @param level       current level while moving in the search space tree
     * @param curr_tour   where the solution is being stored which would later be copied to final_tour
     */
    protected void TSPRec(int curr_bound, int curr_weight, int level, Tour curr_tour) {

        // base case is when we have reached level N which
        // means we have covered all the nodes once
        if (level == numberOfStep) {
            // check if there is an edge from last vertex in
            // path back to the first vertex
            int lengthfromLastStepToFirst = adjacencyMatrixOfShortestPath.get(curr_tour.getListPaths().get(level - 2).getIdArrival()).get(map.getDepot().getId()).getPathLength();
            if (lengthfromLastStepToFirst != 0) {
                // curr_res has the total weight of the
                // solution we got
                int curr_res = curr_weight +
                        lengthfromLastStepToFirst;
                // Update final result and final path if
                // current result is better.

                if (curr_res < final_res) {
                    copyToFinal(curr_tour);
                    final_res = curr_res;
                }
            }
            return;
        }

        // for any other level iterate for all vertices to
        // build the search space tree recursively
        long currentStepId;
        if (level == 1) {
            currentStepId = map.getDepot().getId();
        } else {
            currentStepId = curr_tour.getListPaths().get(level - 2).getIdArrival();
        }

        for (java.util.Map.Entry<Long, Path> pathStartingAtCurrentStep : adjacencyMatrixOfShortestPath.get(currentStepId).entrySet()) {
            // check if not visited already
            boolean precedenceRespected = false;
            if (precedenceOrderMatrix.containsKey(pathStartingAtCurrentStep.getKey())) {
                for (Long id : precedenceOrderMatrix.get(pathStartingAtCurrentStep.getKey())) {
                    if (visited.containsKey(id)) {
                        precedenceRespected = true;
                    } else {
                        precedenceRespected = false;
                        break;
                    }
                }
            } else {
                precedenceRespected = true;
            }
            if (!visited.containsKey(pathStartingAtCurrentStep.getKey()) && precedenceRespected) {
                int temp = curr_bound;
                curr_weight += pathStartingAtCurrentStep.getValue().getPathLength();

                // different computation of curr_bound for
                // level 2 from the other levels
                if (level == 1) {
                    curr_bound -= ((firstMin(currentStepId) + firstMin(pathStartingAtCurrentStep.getKey())) / 2);
                } else {
                    curr_bound -= ((secondMin(currentStepId) + firstMin(pathStartingAtCurrentStep.getKey())) / 2);
                }
                // curr_bound + curr_weight is the actual lower bound
                // for the node that we have arrived on
                // If current lower bound < final_res, we need to explore
                // the node further
                if (curr_bound + curr_weight < final_res && System.currentTimeMillis() - timeZero < TIMELIMIT) {
                    curr_tour.addPath(pathStartingAtCurrentStep.getValue());
                    visited.put(pathStartingAtCurrentStep.getKey(), true);
                    // call TSPRec for the next level
                    TSPRec(curr_bound, curr_weight, level + 1,
                            curr_tour);
                } else {
                    return;
                }

                if (curr_tour.getListPaths().size() == level) {
                    curr_tour.getListPaths().remove(level - 1);
                }
                // Else we have to prune the node by resetting
                // all changes to curr_weight and curr_bound
                curr_weight -= pathStartingAtCurrentStep.getValue().getPathLength();
                curr_bound = temp;

                // Also reset the visited array
                visited.clear();
                visited.put(map.getDepot().getId(), true);
                for (Path p : curr_tour.getListPaths()) {
                    visited.put(p.getIdArrival(), true);
                }
            }
        }
    }
}
