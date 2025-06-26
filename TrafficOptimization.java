//CSC4202 - DESIGN AND ANALYSIS OF ALGORITHM
//AYA, AZYAN, FATIN, NAJMA
//GROUP 4

import java.util.*;

public class TrafficOptimization {

    static class RoadNetwork {
        Map<String, List<Road>> adjacencyList = new HashMap<>();

        public void addLocation(String locationId) {
            adjacencyList.putIfAbsent(locationId, new ArrayList<>());
        }

        public void addRoad(String source, String destination, double distance, double tollCost, double baseTime) {
            adjacencyList.get(source).add(new Road(destination, distance, tollCost, baseTime));
            adjacencyList.get(destination).add(new Road(source, distance, tollCost, baseTime));
        }

        public void updateTrafficConditions(String source, String destination, double congestionFactor) {
            for (Road road : adjacencyList.get(source)) {
                if (road.destination.equals(destination)) {
                    road.congestionFactor = congestionFactor;
                    break;
                }
            }
        }
    }

    static class Road {
        String destination;
        double distance;
        double tollCost;
        double baseTime;
        double congestionFactor = 1.0;

        public Road(String destination, double distance, double tollCost, double baseTime) {
            this.destination = destination;
            this.distance = distance;
            this.tollCost = tollCost;
            this.baseTime = baseTime;
        }

        public double getCurrentTime() {
            return baseTime * congestionFactor;
        }

        public double getFuelCost() {
            return distance / 10.0 * 2.05;
        }

        public double getTotalCost() {
            return tollCost + getFuelCost();
        }
    }

    static class PathResult {
        List<String> path;
        double totalTime;
        double totalCost;
        double totalDistance;

        public PathResult(List<String> path, double totalTime, double totalCost, double totalDistance) {
            this.path = path;
            this.totalTime = totalTime;
            this.totalCost = totalCost;
            this.totalDistance = totalDistance;
        }
    }

    static class Node {
        String location;
        double cost;
        double time;
        double monetaryCost;

        public Node(String location, double cost, double time, double monetaryCost) {
            this.location = location;
            this.cost = cost;
            this.time = time;
            this.monetaryCost = monetaryCost;
        }
    }

    public static PathResult dijkstra(RoadNetwork network, String start, String end,
                                      boolean prioritizeTime, boolean prioritizeCost) {
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingDouble(a -> a.cost));

        Map<String, Double> dist = new HashMap<>();
        Map<String, String> prev = new HashMap<>();
        Map<String, Double> time = new HashMap<>();
        Map<String, Double> monetaryCost = new HashMap<>();

        for (String location : network.adjacencyList.keySet()) {
            dist.put(location, Double.POSITIVE_INFINITY);
            time.put(location, Double.POSITIVE_INFINITY);
            monetaryCost.put(location, Double.POSITIVE_INFINITY);
        }

        dist.put(start, 0.0);
        time.put(start, 0.0);
        monetaryCost.put(start, 0.0);
        pq.add(new Node(start, 0.0, 0.0, 0.0));

        while (!pq.isEmpty()) {
            Node current = pq.poll();

            if (current.location.equals(end)) break;

            for (Road road : network.adjacencyList.get(current.location)) {
                double newTime = time.get(current.location) + road.getCurrentTime();
                double newCost = monetaryCost.get(current.location) + road.getTotalCost();

                double combinedCost = (prioritizeTime && prioritizeCost) ? newTime * 0.5 + newCost * 0.5
                        : prioritizeTime ? newTime : newCost;

                if (combinedCost < dist.get(road.destination)) {
                    dist.put(road.destination, combinedCost);
                    time.put(road.destination, newTime);
                    monetaryCost.put(road.destination, newCost);
                    prev.put(road.destination, current.location);
                    pq.add(new Node(road.destination, combinedCost, newTime, newCost));
                }
            }
        }

        return buildPath(prev, network, start, end, time.get(end), monetaryCost.get(end));
    }

    public static PathResult aStar(RoadNetwork network, String start, String end,
                                   boolean prioritizeTime, boolean prioritizeCost,
                                   Map<String, Double> heuristicValues) {
        PriorityQueue<Node> pq = new PriorityQueue<>(
                Comparator.comparingDouble(a -> a.cost + heuristicValues.getOrDefault(a.location, 300.0)));

        Map<String, Double> gScore = new HashMap<>();
        Map<String, String> cameFrom = new HashMap<>();
        Map<String, Double> time = new HashMap<>();
        Map<String, Double> monetaryCost = new HashMap<>();

        for (String location : network.adjacencyList.keySet()) {
            gScore.put(location, Double.POSITIVE_INFINITY);
            time.put(location, Double.POSITIVE_INFINITY);
            monetaryCost.put(location, Double.POSITIVE_INFINITY);
        }

        gScore.put(start, 0.0);
        time.put(start, 0.0);
        monetaryCost.put(start, 0.0);
        pq.add(new Node(start, 0.0, 0.0, 0.0));

        while (!pq.isEmpty()) {
            Node current = pq.poll();

            if (current.location.equals(end)) break;

            for (Road road : network.adjacencyList.get(current.location)) {
                double newTime = time.get(current.location) + road.getCurrentTime();
                double newCost = monetaryCost.get(current.location) + road.getTotalCost();

                double combined = (prioritizeTime && prioritizeCost) ? road.getCurrentTime() * 0.5 + road.getTotalCost() * 0.5
                        : prioritizeTime ? road.getCurrentTime() : road.getTotalCost();

                double tentativeG = gScore.get(current.location) + combined;

                if (tentativeG < gScore.get(road.destination)) {
                    gScore.put(road.destination, tentativeG);
                    cameFrom.put(road.destination, current.location);
                    time.put(road.destination, newTime);
                    monetaryCost.put(road.destination, newCost);
                    pq.add(new Node(road.destination, tentativeG, newTime, newCost));
                }
            }
        }

        return buildPath(cameFrom, network, start, end, time.get(end), monetaryCost.get(end));
    }

    private static PathResult buildPath(Map<String, String> cameFrom, RoadNetwork network,
                                        String start, String end, double totalTime, double totalCost) {
        List<String> path = new ArrayList<>();
        String current = end;
        double totalDistance = 0.0;

        while (current != null) {
            path.add(0, current);
            String prev = cameFrom.get(current);
            if (prev != null) {
                for (Road road : network.adjacencyList.get(prev)) {
                    if (road.destination.equals(current)) {
                        totalDistance += road.distance;
                        break;
                    }
                }
            }
            current = prev;
        }

        return new PathResult(path, totalTime, totalCost, totalDistance);
    }

    public static Map<String, Double> createHeuristic() {
        Map<String, Double> heuristic = new HashMap<>();
        heuristic.put("UPM", 400.0);
        heuristic.put("KL", 390.0);
        heuristic.put("Karak", 350.0);
        heuristic.put("Kuantan", 250.0);
        heuristic.put("Kuala Terengganu", 100.0);
        heuristic.put("Besut", 0.0);
        heuristic.put("Alternative1", 270.0);
        heuristic.put("Alternative2", 200.0);
        heuristic.put("CoastalRoute", 50.0);
        return heuristic;
    }

    public static void simulateFestiveTraffic(RoadNetwork network) {
        Random rand = new Random(42); // Fixed seed for consistency
        String[][] congestedRoads = {
            {"UPM", "KL"}, {"KL", "Karak"}, {"Karak", "Kuantan"},
            {"Kuantan", "Kuala Terengganu"}, {"Kuala Terengganu", "Besut"}
        };
        for (String[] pair : congestedRoads) {
            double congestion = 1.5 + rand.nextDouble() * 2.0;
            network.updateTrafficConditions(pair[0], pair[1], congestion);
        }
    }

    public static void main(String[] args) {
        RoadNetwork network = new RoadNetwork();
        String[] locations = {"UPM", "KL", "Karak", "Kuantan", "Kuala Terengganu", "Besut",
                              "Alternative1", "Alternative2", "CoastalRoute"};
        for (String loc : locations) network.addLocation(loc);

        network.addRoad("UPM", "KL", 25, 5.00, 30);
        network.addRoad("KL", "Karak", 60, 15.00, 45);
        network.addRoad("Karak", "Kuantan", 150, 25.00, 120);
        network.addRoad("Kuantan", "Kuala Terengganu", 200, 35.00, 150);
        network.addRoad("Kuala Terengganu", "Besut", 60, 10.00, 50);
        network.addRoad("KL", "Alternative1", 40, 8.00, 140);
        network.addRoad("Alternative1", "Kuantan", 170, 20.00, 140);
        network.addRoad("Karak", "Alternative2", 80, 12.00, 70);
        network.addRoad("Alternative2", "Kuala Terengganu", 220, 30.00, 180);
        network.addRoad("Kuantan", "CoastalRoute", 180, 25.00, 160);
        network.addRoad("CoastalRoute", "Besut", 70, 15.00, 60);

        simulateFestiveTraffic(network);
        Map<String, Double> heuristic = createHeuristic();

        System.out.println("=== Time-Optimized Path ===");
        printComparison(
            dijkstra(network, "UPM", "Besut", true, false),
            aStar(network, "UPM", "Besut", true, false, heuristic));

        System.out.println("\n=== Cost-Optimized Path ===");
        printComparison(
            dijkstra(network, "UPM", "Besut", false, true),
            aStar(network, "UPM", "Besut", false, true, heuristic));

        System.out.println("\n=== Balanced Path (Time and Cost) ===");
        printComparison(
            dijkstra(network, "UPM", "Besut", true, true),
            aStar(network, "UPM", "Besut", true, true, heuristic));
    }

    public static void printComparison(PathResult dijkstraResult, PathResult aStarResult) {
        System.out.println("Dijkstra's Algorithm:");
        printPathDetails(dijkstraResult);
        System.out.println("\nA* Algorithm:");
        printPathDetails(aStarResult);
    }

    public static void printPathDetails(PathResult result) {
        if (result == null) {
            System.out.println("No path found!");
            return;
        }
        System.out.println("Path: " + String.join(" -> ", result.path));
        System.out.printf("Total Time: %.2f minutes\n", result.totalTime);
        System.out.printf("Total Cost: RM%.2f\n", result.totalCost);
        System.out.printf("Total Distance: %.2f km\n", result.totalDistance);
    }
}
