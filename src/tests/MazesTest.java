package tests;

import static org.junit.Assert.*;

import java.io.StringReader;

import graphs.Edge;
import graphs.Graph;
import graphs.Node;
import mazes.Mazes;

import org.junit.Before;
import org.junit.Test;

public class MazesTest {

    Graph g1;
    Mazes maze;
    
    @Before
    public void setUp() throws Exception {
        maze = new Mazes();
        g1 = Graph.read(new StringReader("graphName {\n start edge -> toNode\ntoNode edge2 -> finish}"));
    }

    @Test
    public void testValidateGraph() {
        assertTrue(maze.validateGraph(g1));
        g1 = Graph.read(new StringReader("graphName {\n end edge -> toNode\ntoNode edge2 -> finish}"));
        assertFalse(maze.validateGraph(g1));
        g1 = Graph.read(new StringReader("graphName {\n start edge -> toNode\ntoNode edge2 -> fin}"));
        assertFalse(maze.validateGraph(g1));
        g1 = Graph.read(new StringReader("graphName {\n end edge -> toNode\ntoNode edge2 -> fin}"));
        assertFalse(maze.validateGraph(g1));
    }
    
    @Test
    public void testGetStartNode(){
        Node start = maze.getStartNode(g1);
        assertTrue(start.toString().equals("start"));
        g1 = Graph.read(new StringReader("graphName {\n end edge -> toNode\ntoNode edge2 -> fin}"));
        assertNull(maze.getStartNode(g1));
    }
    
    @Test
    public void testTraverseGraph(){
        assertEquals("start -- edge --> toNode\ntoNode -- edge2 --> finish\n", maze.traverseGraph(g1));
        g1 = Graph.read(new StringReader("graphName {\n start edge -> toNode\nfromNode edge2 -> finish}"));
        assertEquals("No path exists", maze.traverseGraph(g1));
        
        // Include dead ends
        g1 = Graph.read(new StringReader("graphName {\n start edge -> toNode\ntoNode edge2 -> fromNode\n toNode 3 -> deadNode\n toNode -> finish}"));
        assertNotEquals("No path exists", maze.traverseGraph(g1));
        
        // Include loops
        g1 = Graph.read(new StringReader("graphName {\n start edge -> toNode\ntoNode edge2 -> fromNode\nfromNode edge3 -> toNode\n fromNode * -> fromNode\n fromNode 3 -> deadNode\n toNode -> finish}"));
        assertNotEquals("No path exists", maze.traverseGraph(g1));
    }

}
