package graphs;

import java.io.Reader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A graph containing nodes and edges
 * 
 * @author kevinlee
 * 
 */
public class Graph extends Plex {

    /**
     * Creates a graph with the given value
     * 
     * @param value
     *            The name of the graph
     */
    public Graph(Object value) {
        super(value);
    }

    /**
     * Deletes a node (and all its associated edges) from this graph.
     * 
     * @param node
     *            The node to be deleted
     */
    public void delete(Node node) {
        node.delete();
    }

    /**
     * Deletes an edge from this graph
     * 
     * @param edge
     *            The edge to be deleted
     */
    public void delete(Edge edge) {
        edge.delete();
    }

    /**
     * Returns the nodes in this graph
     * 
     * @return A set of Nodes in the graph
     */
    public Set<Node> getNodes() {
        Set<Node> nodes = new HashSet<Node>();
        for (Plex plex : this.contents) {
            if (plex instanceof Node) {
                nodes.add((Node) plex);
            }
        }
        return nodes;
    }

    /**
     * Returns a printable representation of this graph
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(this.getValue().toString() + " {\n");

        Set<Node> nodes = this.getNodes();
        Iterator<Node> it = nodes.iterator();
        while (it.hasNext()) {
            Node node = it.next();
            if (node.getOutpointingEdges().isEmpty() && node.getInpointingEdges().isEmpty()) {
                str.append(node.toString() + '\n');
            } else {
                for (Edge edge : node.getOutpointingEdges()) {
                    str.append(getOne(edge.origins).toString()); // Origin node
                    str.append(edge.toString()); // Edge
                    str.append(getOne(edge.destinations).toString() + '\n'); // Destination
                                                                             // node
                }
            }
        }
        str.append("}");
        return str.toString();
    }

    /**
     * Prints this graph, using the same syntax as read
     */
    public void print() {
        System.out.print(this);
    }

    /**
     * Reads in a graph
     * 
     * @param reader
     *            The reader object from which graph is read
     * @return The resulting graph
     */
    public static Graph read(Reader reader) {

        String previousPlex = "EOL"; // indicates whether the previous plex was
                                     // a Node, an Edge, an Arrow (the ->
                                     // symbol), or an EOL. Start as EOL
        String edgeName = ""; // what the name of the current edge is
        Node previousNode = null;

        Set<String> keywords = new HashSet<String>();
        keywords.add("-");
        Tokenizer tokenizer = new Tokenizer(reader, keywords);
        Graph g = initializeGraph(tokenizer); // Initialises graph and extracts
                                              // graph name

        // 2nd token = "{"
        Token brace = tokenizer.next();
        if (!brace.getValue().equals("{"))
            throw new RuntimeException("Invalid graph syntax");

        while (tokenizer.hasNext()) {
            Token token = tokenizer.next();

            // Scenario 1: Brace to end graph input
            if (token.getValue().equals("}"))
                break;

            // Scenario 2: next token is EOI = invalid input
            else if (token.getType() == TokenType.EOI)
                throw new RuntimeException("Invalid graph syntax (2)");

            // Scenario 3: Token includes a '-'
            else if (token.getType() == TokenType.KEYWORD) {

                // Scenario 3a: Previous token was a edge, so we should be
                // expecting a "->"
                if (previousPlex.equals("Edge")) {

                    if (tokenizer.next().getValue().equals(">")) {
                        previousPlex = "Arrow";
                    }
                    // Scenario 3ai: Syntax error
                    else
                        throw new RuntimeException("Invalid graph syntax (3ai)");

                }

                // Scenario 3b: Previous token was a Node
                else if (previousPlex.equals("Node")) {
                    edgeName = token.getValue();

                    Token nextToken = tokenizer.next();

                    // Scenario 3bi: Next token is a ">", so it's an Edge with
                    // an empty value
                    if (nextToken.getValue().equals(">")) {
                        edgeName = "";
                        previousPlex = "Arrow";
                    }

                    // Scenario 3bii: Next token is a number, so it's an Edge
                    // with a negative number value
                    else if (nextToken.getType() == TokenType.NUMBER) {
                        edgeName = edgeName + nextToken.getValue();
                        previousPlex = "Edge";
                    }

                    // Scenario 3biii: Next token is also a '-', which would be
                    // for a "->"
                    else if (nextToken.getType() == TokenType.KEYWORD) {
                        tokenizer.pushBack();
                        previousPlex = "Edge";
                    }

                    // Scenario 3biv: Syntax error
                    else
                        throw new RuntimeException("Invalid graph syntax (3biv)");
                }

                // Scenario 3c: Previous token was an EOL or an arrow, so token
                // is a Node
                else if (previousPlex.equals("EOL") || previousPlex.equals("Arrow")) {
                    String tempValue = token.getValue();
                    Token nextToken = tokenizer.next();

                    // Scenario 3ci: Next token is a number, so the Node is a
                    // negative number
                    if (nextToken.getType() == TokenType.NUMBER) {
                        tempValue = tempValue + nextToken.getValue();
                        Node node = createNode(tempValue, g);

                        // Scenario 3cia: previousPlex = Arrow, hence the Node
                        // is a toNode
                        if (previousPlex.equals("Arrow")) {
                            Edge edge = new Edge(previousNode, edgeName, node);
                            previousPlex = "Node";
                            edgeName = "";
                            previousNode = null;
                        }

                        // Scenario 3cib: previousPlex != Arrow, hence the Node
                        // is a fromNode
                        else {
                            previousNode = node;
                            previousPlex = "Node";

                        }
                    }

                    // Scenario 3cii: Next token is not a number, so Node = "-"
                    else {
                        Node node = createNode(tempValue, g);
                        tokenizer.pushBack();

                        // Scenario 3ciia: previousPlex = Arrow, hence the Node
                        // is a toNode
                        if (previousPlex.equals("Arrow")) {

                            Edge edge = new Edge(previousNode, edgeName, node);
                            previousPlex = "Node";
                            edgeName = "";
                            previousNode = null;
                        }

                        // Scenario 3ciib: previousPlex != Arrow, hence the Node
                        // is a fromNode
                        else {
                            previousNode = node;
                            previousPlex = "Node";

                        }
                    }

                }

                // Scenario 3d: Syntax error
                else
                    throw new RuntimeException("Invalid graph syntax (3d)");

            }

            // Scenario 4: Token = EOL

            else if (token.getType() == TokenType.EOL) {

                previousPlex = "EOL";
                previousNode = null;
                edgeName = "";
            }

            // Scenario 5: Token = normal string

            else {
                // Scenario 5a: previousNode == null & previousPlex == EOL ie
                // token = fromNode

                if (previousNode == null && previousPlex.equals("EOL")) {
                    Node node = createNode(token.getValue(), g);
                    previousNode = node;
                    previousPlex = "Node";
                }

                // Scenario 5b: previousNode != null && previousPlex = "Node" ie
                // token = Edge
                else if (previousNode != null && previousPlex.equals("Node")) {
                    edgeName = token.getValue();
                    previousPlex = "Edge";
                }

                // Scenario 5c: previousNode != null && previousPlex = "Arrow"
                // ie token = toNode
                else if (previousNode != null && previousPlex.equals("Arrow")) {
                    Node node = createNode(token.getValue(), g);
                    Edge edge = new Edge(previousNode, edgeName, node);
                    previousPlex = "Node";
                    edgeName = "";
                    previousNode = null;
                }

                // Scenario 5d: Syntax error
                else
                    throw new RuntimeException("Invalid graph syntax (5d)");

            }
        }

        // Check if final token is a "}"

        tokenizer.pushBack();
        if (!tokenizer.next().getValue().equals("}"))
            throw new RuntimeException("Invalid graph syntax (final token not })");

        return g;
    }

    /**
     * Initialises graph and extracts graph name
     * 
     * @param tokenizer
     *            The tokenizer object by which the graph is generated
     * @return Graph with a graph name
     */
    private static Graph initializeGraph(Tokenizer tokenizer) {
        if (!tokenizer.hasNext())
            throw new RuntimeException("Unable to read from reader");

        // 1st token = graph name

        Token graphName = tokenizer.next();
        if (graphName.getType().equals(TokenType.EOI) || graphName.getType().equals(TokenType.EOL)
                || graphName.getType().equals(TokenType.ERROR)) {
            throw new RuntimeException("Invalid graph syntax");
        }
        return new Graph(graphName.getValue());
    }

    /**
     * Returns a node that has the same value as the value parameter, or return
     * a new node
     * 
     * @param value
     *            The token's value
     * @param g
     *            The existing graph
     * @return A node with the token's value
     */
    private static Node createNode(String value, Graph g) {
        Set<Node> graphNodes = g.getNodes();
        Node newNode = null;
        for (Node node : graphNodes) {
            if (value.equals(node.toString())) {
                newNode = node;
                break;
            }
        }
        if (newNode == null) {
            newNode = new Node(value, g);
        }
        return newNode;
    }
}
