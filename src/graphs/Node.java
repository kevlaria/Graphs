package graphs;

import java.util.HashSet;
import java.util.Set;

/**
 * A node in a graph
 * 
 * @author kevinlee
 * 
 */
public class Node extends Plex {

    /**
     * Constructs a node on graph g with the given value
     * 
     * @param value
     *            The value of the node
     * @param g
     *            The graph on which the node exists
     */
    public Node(Object value, Graph g) {
        super(value);
        this.addContainer(g);
    }

    /**
     * Deletes this node (and all its associated edges) from the graph it is on
     */
    public void delete() {
        Set<Edge> incomingEdges = this.getInpointingEdges(); // Delete incoming
                                                             // edges
        for (Edge edge : incomingEdges) {
            edge.delete();
        }
        Set<Edge> outpointingEdges = this.getOutpointingEdges();
        for (Edge edge : outpointingEdges) {
            edge.delete();
        }
        this.removeContainer(this.getGraph());
    }

    /**
     * Returns the graph that this node is on
     * 
     * @return the graph that this node is on
     */
    public Graph getGraph() {
        Plex container = Plex.getOne(containers);
        return (Graph) container;
    }

    /**
     * Returns the edges pointing out from this node
     * 
     * @return Set of outpointing edges from this node
     */
    public Set<Edge> getOutpointingEdges() {
        Set<Plex> plexDestinations = this.destinations;
        Set<Edge> edgeDestination = new HashSet<Edge>();
        for (Plex plex : plexDestinations) {
            edgeDestination.add((Edge) plex);
        }
        return edgeDestination;

    }

    /**
     * Returns the edges pointing in to this node
     * 
     * @return Set of inpointing edges to this node
     */
    public Set<Edge> getInpointingEdges() {
        Set<Plex> plexDestinations = this.origins;
        Set<Edge> edgeOrigins = new HashSet<Edge>();
        for (Plex plex : plexDestinations) {
            edgeOrigins.add((Edge) plex);
        }
        return edgeOrigins;
    }

    /**
     * Returns a printable representation of this node
     */
    @Override
    public String toString() {
        return this.getValue().toString();
    }

}
