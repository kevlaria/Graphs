package tests;

import static org.junit.Assert.*;

import java.io.StringReader;

import graphs.Edge;
import graphs.Graph;
import graphs.Node;

import org.junit.Before;
import org.junit.Test;

public class GraphTest {

    Graph g1;
    Node n1, n2, n3, n4;
    Edge e1, e2, e3, e4, e5, e6;
    
    @Before
    public void setUp() throws Exception {
        g1 = new Graph("G1");
        n1 = new Node("N1", g1);
        n2 = new Node("N2", g1);
        n3 = new Node("N3", g1);
        n4 = new Node("N4", g1);
        e1 = new Edge(n1, "E1", n2);
        e2 = new Edge(n2, "E2", n1);
        e3 = new Edge(n2, "E3", n2);
        e4 = new Edge(n2, "E4", n3);  
    }
    
    /******
     * Graph tests
     * ******
     */
    
    @Test
    public void testGraphDeleteNode(){
        assertTrue(g1.contents.contains(n1));
        g1.delete(n1);
        assertFalse(g1.contents.contains(n1));
        assertFalse(g1.contents.contains(e1)); // also deletes incoming and outgoing edges
        assertFalse(g1.contents.contains(e2));
        assertFalse(n2.getOutpointingEdges().contains(e2));
        assertTrue(n2.getOutpointingEdges().contains(e3));
        g1.delete(n4);
        assertFalse(g1.contents.contains(n4));
    }
    
    @Test
    public void testGraphDeleteEdge(){
        assertTrue(n2.getInpointingEdges().contains(e1));
        assertTrue(n1.getOutpointingEdges().contains(e1));
        g1.delete(e1);
        assertFalse(n2.getInpointingEdges().contains(e1));
        assertFalse(n1.getOutpointingEdges().contains(e1));
    }
    
    @Test
    public void testGetNodes(){
        assertTrue(g1.getNodes().contains(n1));
        assertTrue(g1.getNodes().contains(n2));
        assertTrue(g1.getNodes().contains(n3));
        assertTrue(g1.getNodes().contains(n4));
        assertTrue(g1.getNodes().size() == 4);
        assertFalse(g1.getNodes().contains(e1));
    }
    
    @Test
    public void testReadSimple(){
        Graph g2 = Graph.read(new StringReader("graphName {\n fromNode edge -> toNode\n}"));
        assertTrue(g2.getValue().toString().equals("graphName"));
        assertTrue(g2.getNodes().size() == 2);
        assertEquals("graphName {\nfromNode -- edge --> toNode\n}", g2.toString());
    }
    
    @Test
    public void testReadSimpleWithSpacedArrow(){
        Graph g2 = Graph.read(new StringReader("graphName {\n fromNode edge - > toNode\n}"));
        assertTrue(g2.getValue().toString().equals("graphName"));
        assertTrue(g2.getNodes().size() == 2);
        assertEquals("graphName {\nfromNode -- edge --> toNode\n}", g2.toString());
    }
    
    @Test
    public void testReadEmptyEdgeName(){
        Graph g2 = Graph.read(new StringReader("graphName {\n fromNode -> toNode\n}"));
        assertTrue(g2.getValue().toString().equals("graphName"));
        assertTrue(g2.getNodes().size() == 2);
        assertEquals("graphName {\nfromNode --  --> toNode\n}", g2.toString());
    }
    
    @Test
    public void testReadNumericNodeName(){
        Graph g2 = Graph.read(new StringReader("graphName {\n 1231 edge -> toNode\n}"));
        assertTrue(g2.getValue().toString().equals("graphName"));
        assertTrue(g2.getNodes().size() == 2);
        assertEquals("graphName {\n1231 -- edge --> toNode\n}", g2.toString());
    }
    
    @Test
    public void testReadNegativeNumericNodeName(){
        Graph g2 = Graph.read(new StringReader("graphName {\n -1231 edge -> toNode\n}"));
        assertTrue(g2.getValue().toString().equals("graphName"));
        assertTrue(g2.getNodes().size() == 2);
        assertEquals("graphName {\n-1231 -- edge --> toNode\n}", g2.toString());
    }
    
    
    @Test
    public void testReadNegativeNumericNodeNameWithMultipleEdges(){
        Graph g2 = Graph.read(new StringReader("graphName {\n -1231 edge -> toNode\n-1231 anotherEdge -> bob}"));
        assertTrue(g2.getValue().toString().equals("graphName"));
        assertTrue(g2.getNodes().size() == 3);
    }
    
    @Test
    public void testReadNegativeNumericToNodeName(){
        Graph g2 = Graph.read(new StringReader("graphName {\n fromNode edge -> -1231\n}"));
        assertTrue(g2.getValue().toString().equals("graphName"));
        assertTrue(g2.getNodes().size() == 2);
        assertEquals("graphName {\nfromNode -- edge --> -1231\n}", g2.toString());
    }
    
    @Test
    public void testReadNegativeNumericToNodeNameWithMultipleEdges(){
        Graph g2 = Graph.read(new StringReader("graphName {\n fromNode edge -> -1231\n anotherFromNode edge2 -> -1231}"));
        assertTrue(g2.getValue().toString().equals("graphName"));
        assertTrue(g2.getNodes().size() == 3);
    }
    
    @Test
    public void testReadNegativeNumericToNodeWithIdenticalFromNodeName(){
        Graph g2 = Graph.read(new StringReader("graphName {\n fromNode edge -> -1231\n -1231 edge2 -> toNode}"));
        assertTrue(g2.getValue().toString().equals("graphName"));
        assertTrue(g2.getNodes().size() == 3);
    }
    
    @Test
    public void testReadNegativeNumericEdgeName(){
        Graph g2 = Graph.read(new StringReader("graphName {\n fromNode -123 -> toNode\n}"));
        assertTrue(g2.getValue().toString().equals("graphName"));
        assertTrue(g2.getNodes().size() == 2);
        assertEquals("graphName {\nfromNode -- -123 --> toNode\n}", g2.toString());
    }
    
    @Test
    public void testReadNegativeNumericFromToNodesAndEdgeName(){
        Graph g2 = Graph.read(new StringReader("graphName {\n -34 -123 -> -1\n}"));
        assertTrue(g2.getValue().toString().equals("graphName"));
        assertTrue(g2.getNodes().size() == 2);
        assertEquals("graphName {\n-34 -- -123 --> -1\n}", g2.toString());
    }
    
    @Test
    public void testReadDashEdgeName(){
        Graph g2 = Graph.read(new StringReader("graphName {\n fromNode - -> toNode\n}"));
        assertTrue(g2.getValue().toString().equals("graphName"));
        assertTrue(g2.getNodes().size() == 2);
        assertEquals("graphName {\nfromNode -- - --> toNode\n}", g2.toString());        
    }
    
    @Test
    public void testReadNegativeNodeWithSpaceName(){
        Graph g2 = Graph.read(new StringReader("graphName {\n - 134 -> toNode\n}"));
        assertTrue(g2.getValue().toString().equals("graphName"));
        assertTrue(g2.getNodes().size() == 2);
        assertEquals("graphName {\n-134 --  --> toNode\n}", g2.toString());        
    }
    
    @Test
    public void testReadDashFromNode(){
        Graph g2 = Graph.read(new StringReader("graphName {\n - Edge -> toNode\n}"));
        assertTrue(g2.getValue().toString().equals("graphName"));
        assertTrue(g2.getNodes().size() == 2);
        assertEquals("graphName {\n- -- Edge --> toNode\n}", g2.toString());        
    }
    
    @Test
    public void testReadDashToNode(){
        Graph g2 = Graph.read(new StringReader("graphName {\n fromNode Edge -> -\n}"));
        assertTrue(g2.getValue().toString().equals("graphName"));
        assertTrue(g2.getNodes().size() == 2);
        assertEquals("graphName {\nfromNode -- Edge --> -\n}", g2.toString());        
    }
    
    @Test
    public void testReadDashAll(){
        Graph g2 = Graph.read(new StringReader("graphName {\n - - -> -\n}"));
        assertTrue(g2.getValue().toString().equals("graphName"));
        assertTrue(g2.getNodes().size() == 1);
        assertEquals("graphName {\n- -- - --> -\n}", g2.toString());        
    }
    
    @Test
    public void testReadIsolatedNode(){
        Graph g2 = Graph.read(new StringReader("graphName {\n fromNode\n}"));
        assertTrue(g2.getValue().toString().equals("graphName"));
        assertTrue(g2.contents.size() == 1);
        assertTrue(g2.getNodes().size() == 1);
        assertEquals("graphName {\nfromNode\n}", g2.toString());
    }
    
    @Test
    public void testReadIsolatedNodeWithOtherNodes(){
        Graph g2 = Graph.read(new StringReader("graphName {\n fromNode1 edge1 -> toNode1\nfromNode2 edge2 -> toNode2\nisolatedNode}"));
        assertTrue(g2.getValue().toString().equals("graphName"));
        assertTrue(g2.getNodes().size() == 5);
    }
    
    @Test
    public void testReadMultipleLines(){
        Graph g2 = Graph.read(new StringReader("graphName {\n fromNode1 edge1 -> toNode1\nfromNode2 edge2 -> toNode2}"));
        assertTrue(g2.getValue().toString().equals("graphName"));
        assertTrue(g2.getNodes().size() == 4);
        assertEquals("graphName {\nfromNode1 -- edge1 --> toNode1\nfromNode2 -- edge2 --> toNode2\n}", g2.toString());
    }
    
    

    /******
     * Edge tests
     * ******
     */
    @Test
    public void testEdgeGetGraph(){
        assertTrue(e1.getGraph() == g1);
        assertTrue(e3.getGraph() == g1);
    }
    
    @Test
    public void testGetOrigin(){
        assertTrue(e1.getOrigin() == n1);
        assertTrue(e3.getOrigin() == n2);
        assertFalse(e1.getOrigin() == n2);
    }
   
    @Test
    public void testGetDestination(){
        assertTrue(e1.getDestination() == n2);
        assertTrue(e3.getDestination() == n2);
        assertFalse(e1.getDestination() == n1);    
    }
    
    @Test
    public void testEdgeToString(){
        assertEquals(" -- E1 --> ", e1.toString());
    }
    
    @Test
    public void testEdgeDelete(){
        e3.delete();
        assertFalse(n2.getInpointingEdges().contains(e3));
        assertFalse(n2.getOutpointingEdges().contains(e3));
        e4.delete();
        assertTrue(n3.getInpointingEdges().isEmpty());
        assertTrue(n3.getOutpointingEdges().isEmpty());
    }
    
    /******
     * Node tests
     * ******
     */
    
    @Test
    public void testNodeGetGraph(){
        assertTrue(n1.getGraph() == g1);
        assertTrue(n4.getGraph() == g1);
    }
    
    @Test
    public void testGetOutpointingEdges(){
        assertTrue(n2.getOutpointingEdges().contains(e3));
        assertTrue(n2.getOutpointingEdges().contains(e4));
        assertTrue(n2.getOutpointingEdges().contains(e2));
        assertFalse(n2.getOutpointingEdges().contains(e1));
        assertTrue(n4.getOutpointingEdges().isEmpty());
    }
    
    @Test
    public void testGetInpointingEdges(){
        assertTrue(n2.getInpointingEdges().contains(e3));
        assertTrue(n2.getInpointingEdges().contains(e1));
        assertFalse(n2.getInpointingEdges().contains(e2));
        assertTrue(n1.getInpointingEdges().contains(e2));
        assertTrue(n4.getInpointingEdges().isEmpty());
    }
    
    @Test
    public void testNodeToString(){
        assertEquals("N1", n1.toString());
    }
    
    @Test
    public void testNodeDelete(){
        n2.delete();
        assertTrue(n1.getInpointingEdges().isEmpty());
        assertTrue(n1.getOutpointingEdges().isEmpty());
        assertTrue(n3.getOutpointingEdges().isEmpty());
        assertFalse(g1.contents.contains(n2));
    }

}
