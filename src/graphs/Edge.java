package graphs;

import java.util.Set;

/**
 * An edge that points from a node, to a node, and contains a value
 * 
 * @author kevinlee
 * 
 */
public class Edge extends Plex {

    /**
     * Constructs an edge with the given value from fromNode to toNode. Nodes
     * must be on the same graph.
     * 
     * @param fromNode
     * @param value
     * @param toNode
     */

    /**
     * Edge constructor
     * 
     * @param fromNode
     *            The origin node
     * @param value
     *            The value of the edge
     * @param toNode
     *            The destination node
     */
    public Edge(Node fromNode, Object value, Node toNode) {
        super(value);
        if (fromNode == null || toNode == null)
            throw new NullPointerException("Null nodes");
        this.addOrigin(fromNode);
        this.addDestination(toNode);
    }

    /**
     * Deletes this edge
     */
    public void delete() {

        for (Plex plex : this.origins) { // Delete edges from origin node
            Node fromNode = (Node) plex;
            this.removeOrigin(fromNode);
        }

        for (Plex plex : this.destinations) { // Delete edges from origin node
            Node toNode = (Node) plex;
            this.removeDestination(toNode);
        }

    }

    /**
     * Returns the graph that this edge is on.
     * 
     * @return the Graph that this edge is on
     */
    public Graph getGraph() {
        Node node = (Node) Plex.getOne(this.origins);
        Graph graph = (Graph) Plex.getOne(node.containers);
        return (Graph) graph;

    }

    /**
     * Returns the node that this edge points out of
     * 
     * @return the Origin node
     */
    public Node getOrigin() {
        return (Node) Edge.getOne(this.origins);
    }

    /**
     * Returns the node that this edge points to
     * 
     * @return the Destination node
     */
    public Node getDestination() {
        return (Node) Edge.getOne(this.destinations);
    }

    /**
     * Returns a printable representation of this edge.
     */
    @Override
    public String toString() {
        return " -- " + this.getValue().toString() + " --> ";
    }

}
