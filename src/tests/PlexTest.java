package tests;
import static org.junit.Assert.*;
import graphs.Plex;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Dave Matuszek
 * @version Feb 27, 2014
 */
public class PlexTest {

    Plex p1, p2, p3;

    @Before
    public void setUp() throws Exception {
        p1 = new Plex("p1");
        p2 = new Plex("p2");
        p3 = new Plex("p3");
    }

    /**
     * Test method for 'Plex.addContainer(Plex)'
     * and 'Plex.removeContainer(Plex)'
     */
    @Test
    public void testAddAndRemoveContainer() {
        p1.addContainer(p2);
        p1.addContainer(p3);
        assertTrue(p1.containers.contains(p2));
        assertTrue(p2.contents.contains(p1));
        assertTrue(p1.containers.contains(p3));
        assertTrue(p3.contents.contains(p1));
        
        p1.removeContainer(p2);
        assertFalse(p1.containers.contains(p2));
        assertFalse(p2.contents.contains(p1));
        assertTrue(p1.containers.contains(p3));
        assertTrue(p3.contents.contains(p1));
    }

    /**
     * Test method for 'Plex.addContent(Plex)'
     * and 'Plex.removeContent(Plex)'
     */
    @Test
    public void testAddAndRemoveContent() {
        p1.addContent(p2);
        p1.addContent(p3);
        assertTrue(p1.contents.contains(p2));
        assertTrue(p2.containers.contains(p1));
        assertTrue(p1.contents.contains(p3));
        assertTrue(p3.containers.contains(p1));
        
        p1.removeContent(p2);
        assertFalse(p1.contents.contains(p2));
        assertFalse(p2.containers.contains(p1));
        assertTrue(p1.contents.contains(p3));
        assertTrue(p3.containers.contains(p1));
        
        p1.addContainer(p2);
        p2.removeContent(p1);
        assertFalse(p1.containers.contains(p2));
        assertFalse(p2.contents.contains(p1));
    }

    /**
     * Test method for 'Plex.addOrigin(Plex)'
     * and 'Plex.removeSource(Plex)'
     */
    @Test
    public void testAddAndRemoveOrigin() {
        p1.addOrigin(p2);
        p1.addOrigin(p3);
        assertTrue(p1.origins.contains(p2));
        assertTrue(p2.destinations.contains(p1));
        assertTrue(p1.origins.contains(p3));
        assertTrue(p3.destinations.contains(p1));
        
        p1.removeOrigin(p2);
        assertFalse(p1.origins.contains(p2));
        assertFalse(p2.destinations.contains(p1));
        assertTrue(p1.origins.contains(p3));
        assertTrue(p3.destinations.contains(p1));
    }

    /**
     * Test method for 'Plex.addDestination(Plex)'
     * and 'Plex.removeDestination(Plex)'
     */
    @Test
    public void testAddAndRemoveDestination() {
        p1.addDestination(p2);
        p1.addDestination(p3);
        assertTrue(p1.destinations.contains(p2));
        assertTrue(p2.origins.contains(p1));
        assertTrue(p1.destinations.contains(p3));
        assertTrue(p3.origins.contains(p1));
        
        p1.removeDestination(p2);
        assertFalse(p1.destinations.contains(p2));
        assertFalse(p2.origins.contains(p1));
        assertTrue(p1.destinations.contains(p3));
        assertTrue(p3.origins.contains(p1));
        
        p1.addOrigin(p2);
        p2.removeDestination(p1);
        assertFalse(p1.origins.contains(p2));
        assertFalse(p2.destinations.contains(p1));
    }
}