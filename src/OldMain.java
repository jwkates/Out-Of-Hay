import java.util.*;

/**
 * Created by jackkates on 2/14/16.
 */
public class OldMain {

    public static void main(String[] args) {
        new OldMain().run();
    }

    public void run() {
        Graph g = new Graph();

        Scanner sc = new Scanner(System.in);
        int numFarms = sc.nextInt(), numRoads = sc.nextInt();

        for (int i = 0; i < numRoads; i++) {
            int sourceFarm = sc.nextInt();
            int destinationFarm = sc.nextInt();
            int roadLength = sc.nextInt();

            g.addEdge(sourceFarm, destinationFarm, roadLength);
        }

        System.out.println(g.findLongestRequiredEdge());
    }

    public class Graph {
        public Map<Integer, Vertex> vertices;
        public Queue<Edge> edges;

        public Graph() {
            vertices = new HashMap<Integer, Vertex>();
            edges = new PriorityQueue<Edge>(10, Collections.reverseOrder());
        }

        public void addEdge(int v1, int v2, int length) {
            Vertex firstVertex = addVertex(v1);
            Vertex secondVertex = addVertex(v2);

            firstVertex.addNeighbor(secondVertex);
            secondVertex.addNeighbor(firstVertex);

            Edge e = new Edge(firstVertex, secondVertex, length);
            edges.add(e);
        }


        public boolean isConnected() {
            // Find an arbitrary vertex to begin the search from.
            Vertex arbitraryVertex = vertices.values().iterator().next();
            arbitraryVertex.visit();

            int nodesVisited = 1;

            // Perform a Breadth-First Search from that initial vertex.
            Queue<Vertex> queue = new LinkedList<Vertex>();
            queue.add(arbitraryVertex);

            while (!queue.isEmpty()) {
                Vertex currentVertex = queue.poll();

                for (Vertex neighbor : currentVertex.neighbors) {
                    if (!neighbor.visited) {
                        neighbor.visit();
                        queue.add(neighbor);

                        nodesVisited++;
                    }
                }
            }

            for (Vertex vertex : vertices.values()) {
                vertex.visited = false;
            }

            return nodesVisited == vertices.size();
        }

        public int findLongestRequiredEdge() {
            Edge longestEdge = removeLongestEdge();
            while (isConnected()) {
                longestEdge = removeLongestEdge();
            }

            return longestEdge.weight;
        }

        private Edge removeLongestEdge() {
            Edge longestEdge = edges.poll();
            removeEdge(longestEdge);

            return longestEdge;
        }

        private void removeEdge(Edge edge) {
            edge.firstVertex.removeNeighbor(edge.secondVertex);
            edge.secondVertex.removeNeighbor(edge.firstVertex);
        }

        private Vertex addVertex(int vertexID) {
            if (!vertices.containsKey(vertexID)) {
                vertices.put(vertexID, new Vertex());
            }

            return vertices.get(vertexID);
        }
    }

    public class Vertex {
        public boolean visited;
        public List<Vertex> neighbors;

        public Vertex() {
            visited = false;
            neighbors = new ArrayList<Vertex>();
        }

        public void addNeighbor(Vertex neighbor) {
            neighbors.add(neighbor);
        }

        public void removeNeighbor(Vertex neighbor) {
            neighbors.remove(neighbor);
        }

        public void visit() {
            visited = true;
        }
    }

    public class Edge implements Comparable<Edge> {
        Vertex firstVertex;
        Vertex secondVertex;
        int weight;

        public Edge(Vertex firstVertex, Vertex secondVertex, int weight) {
            this.firstVertex = firstVertex;
            this.secondVertex = secondVertex;
            this.weight = weight;
        }

        public int compareTo(Edge edge) {
            return Integer.valueOf(weight).compareTo(edge.weight);
        }
    }

}
