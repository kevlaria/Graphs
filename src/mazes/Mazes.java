package mazes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import graphs.Graph;
import graphs.Node;
import graphs.Edge;
import graphs.Plex;

/**
 * Program to determine whether there's a path from start to finish in a graph
 * 
 * @author kevinlee
 * 
 */
public class Mazes {

    /**
     * Launches the program
     * 
     * @param arg
     */
    public static void main(String[] arg) {
        new Mazes().run();
    }

    /**
     * Runs graph traversal program
     */
    public void run() {

        JFileChooser chooser = new JFileChooser();

        while (true) {
            System.out.println("\nEnter 'q' to quit, or any other key to begin maze");
            Scanner scanner = new Scanner(System.in);
            if (scanner.next().equals("q")) {
                System.out.println("Farewell, cruel world...");
                System.exit(1);
            } else {
                Graph g = this.generateGraph(chooser);
                if (g == null) {
                    System.out.println("Invalid graph. Please retry");
                } else {
                    System.out.println("\nThis is the graph:");
                    g.print();
                    System.out.println("\n\nThis is the path:");
                    System.out.println(traverseGraph(g));
                }
            }
        }

    }

    /**
     * Traverses graph using depth-first-search, and seeks the 'finish' node.
     * 
     * @param g
     *            Graph to traverse
     * @return The string representation of the path that is taken to get to the
     *         finish node, or indicate that there isn't one.
     */
    public String traverseGraph(Graph g) {
        // Start with 'start' node
        Node start = getStartNode(g);
        StringBuilder str = new StringBuilder();

        // Create stack of edges for DFS, and a set of edges to mark edges that
        // we have already traversed
        Stack<Edge> edgeStack = new Stack<Edge>();
        Set<Edge> traversedEdgeSet = new HashSet<Edge>();

        for (Edge edge : start.getOutpointingEdges()) {

            if (!traversedEdgeSet.contains(edge)) {
                edgeStack.push(edge);
                traversedEdgeSet.add(edge);
            }
        }

        while (!edgeStack.isEmpty()) {
            Edge edge = edgeStack.pop();
            Node fromNode = edge.getOrigin();
            Node toNode = edge.getDestination();
            str.append(fromNode.toString());
            str.append(edge.toString());
            str.append(toNode.toString() + '\n');

            // Check if it's the goal node
            if (toNode.getValue().toString().toLowerCase().equals("finish")) {
                return str.toString();
            }

            // Put the outpointing edges on the stack, but only if edge isn't
            // already in the set of already-traversed edges
            Set<Edge> edges = toNode.getOutpointingEdges();
            for (Edge newEdge : edges) {
                if (!traversedEdgeSet.contains(newEdge)) {
                    traversedEdgeSet.add(newEdge);
                    edgeStack.push(newEdge);
                }
            }
        }
        return "No path exists";

    }

    /**
     * From a graph, returns the start node.
     * 
     * @param g
     *            Graph with the maze
     * @return the Start node
     */
    public Node getStartNode(Graph g) {
        Set<Node> nodes = g.getNodes();
        Node start;
        for (Node node : nodes) {
            if (node.getValue().toString().toLowerCase().equals("start")) {
                start = node;
                return start;
            }
        }
        return null;
    }

    /**
     * Generates and validates a graph from a file
     * 
     * @return the resulting graph, or null if input is invalid
     */
    private Graph generateGraph(JFileChooser chooser) {
        BufferedReader file = this.openFile(chooser);
        if (file == null) {
            return null;
        } else {
            try {
                Graph g = Graph.read(file);
                if (validateGraph(g))
                    return g;
                return null;
            } catch (RuntimeException e) {
                return null;
            }
        }
    }

    /**
     * Opens JFileChooser for user to select graph
     * 
     * @return the bufferedReader object
     */
    private BufferedReader openFile(JFileChooser chooser) {

        int option = chooser.showOpenDialog(null);

        if (option == JFileChooser.APPROVE_OPTION) {
            try {
                File file = chooser.getSelectedFile();
                if (file != null) {
                    String fileName = file.getCanonicalPath();
                    FileReader fileReader = new FileReader(fileName);
                    BufferedReader reader = new BufferedReader(fileReader);
                    return reader;
                } else
                    return null;
            } catch (IOException e) {
                return null;
            }
        } else
            return null;
    }

    /**
     * Validates graph by determining whether a start and a finish node exist in
     * the graph
     * 
     * @param g
     *            Graph to be validated
     * @return True if both a start and finish node exist in the graph
     */
    public boolean validateGraph(Graph g) {
        Set<Node> nodes = g.getNodes();
        Boolean start = false;
        Boolean finish = false;
        for (Node node : nodes) {
            if (node.getValue().toString().toLowerCase().equals("start"))
                start = true;
            if (node.getValue().toString().toLowerCase().equals("finish"))
                finish = true;
        }
        return (start && finish);
    }

}