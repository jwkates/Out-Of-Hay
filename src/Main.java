import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by jackkates on 2/21/16.
 */
public class Main {

    public static void main(String[] args) throws IOException {
        new Main().run();
    }

    public void run() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String[] numbers = reader.readLine().split(" ");
        int numFarms = Integer.parseInt(numbers[0]);
        int numRoads = Integer.parseInt(numbers[1]);

        List<Edge> edges = new ArrayList<Edge>();
        DisjointSet disjointSet = new DisjointSet(numFarms);

        // Add each road to the edge set.
        for (int i = 0; i < numRoads; i++) {
            numbers = reader.readLine().split(" ");
            int sourceFarm = Integer.parseInt(numbers[0]) - 1; // -1 to make farm numbers 0-based.
            int destinationFarm = Integer.parseInt(numbers[1]) - 1;
            int length = Integer.parseInt(numbers[2]);
            edges.add(new Edge(sourceFarm, destinationFarm, length));
        }

        // Sort the edges in ascending order of length.
        Collections.sort(edges);

        // For each edge, union the sets that contain them.
        // If the graph connects, the first removable edge is
        // the length of the edge we just added.
        for (Edge e : edges) {
            disjointSet.union(e.firstVertex, e.secondVertex);
            if (disjointSet.isConnected()) {
                System.out.println(e.length);
                return;
            }
        }
    }
    private class DisjointSet {

        int numOfDisjointSets;
        Node[] nodes;

        public DisjointSet(int numberOfVertices) {
            numOfDisjointSets = numberOfVertices;
            nodes = new Node[numberOfVertices];

            for (int i = 0; i < numberOfVertices; i++) {
                nodes[i] = new Node();
            }
        }

        public Node find(int i) {
            return find(nodes[i]);
        }

        public boolean union(int a, int b) {
            return union(nodes[a], nodes[b]);
        }

        private Node find(Node v) {
            if (v.parent != v) {
                v.parent = find(v.parent);
            }

            return v.parent;
        }

        private boolean union(Node a, Node b) {
            Node aRoot = find(a);
            Node bRoot = find(b);

            aRoot.parent = bRoot;

            if (aRoot != bRoot) {
                numOfDisjointSets--;
                return true;
            }

            return false;
        }

        public boolean isConnected() {
            return numOfDisjointSets == 1;
        }


        public class Node {
            Node parent;
            int rank;

            public Node() {
                parent = this;
                rank = 0;
            }
        }
    }

    public static class Edge implements Comparable<Edge> {
        int firstVertex;
        int secondVertex;
        int length;

        public Edge(int firstVertex, int secondVertex, int weight) {
            this.firstVertex = firstVertex;
            this.secondVertex = secondVertex;
            this.length = weight;
        }

        public int compareTo(Edge edge) {
            return Integer.valueOf(length).compareTo(edge.length);
        }
    }
}
