package graphs;

import java.util.HashSet;
import java.util.Set;

/**
 * A building block for graphs and hypergraphs.
 * @author David Matuszek
 */
public class Plex {
    /** The things that contain this Plex. */
    public Set<Plex> containers   = new HashSet<Plex>();
    
    /** The things that are in this Plex. */
    public Set<Plex> contents     = new HashSet<Plex>();
    
    /** The places this Plex comes from. */
    public Set<Plex> origins      = new HashSet<Plex>();
    
    /** The places this Plex goes to. */
    public Set<Plex> destinations = new HashSet<Plex>();
    
    /** The value in this Plex. */
    private  Object value;
    
    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * Constructs a Plex with the given value.
     * 
     * @param value The data to be put into this Plex.
     */
    public Plex(Object value) {
        this.value = value;
    }

    /**
     * Adds the argument to this Plex's containers, <i>and</i><br>
     * adds this Plex to the argument's contents.
     * this.addContainer(that) is equivalent to that.addContent(this).
     * 
     * @param that The Plex to be put in this Plex's containers.
     */
    public void addContainer(Plex that) {
        this.containers.add(that);
        that.contents.add(this);
    }

    /**
     * Removes the argument from this Plex's containers, <i>and</i><br>
     * removes this Plex from the argument's contents.
     * this.removeContainer(that) is equivalent to that.removeContent(this).
     * 
     * @param that The Plex to be removed from this Plex's containers.
     */    
    public void removeContainer(Plex that) {
        this.containers.remove(that);
        that.contents.remove(this);
    }

    /**
     * Adds the argument to this Plex's contents, <i>and</i><br>
     * adds this Plex to the argument's containers.
     * this.addContent(that) is equivalent to that.addContainer(this).
     * 
     * @param that The Plex to be put in this Plex's contents.
     */
    public void addContent(Plex that) {
        this.contents.add(that);
        that.containers.add(this);
    }

    /**
     * Removes the argument from this Plex's contents, <i>and</i><br>
     * removes this Plex from the argument's containers.
     * this.removeContent(that) is equivalent to that.removeContainer(this).
     * 
     * @param that The Plex to be removed from this Plex's contents.
     */   
    public void removeContent(Plex that) {
        this.contents.remove(that);
        that.containers.remove(this);
    }

    /**
     * Adds the argument to this Plex's origins, <i>and</i><br>
     * adds this Plex to the argument's destinations.
     * this.addOrigin(that) is equivalent to that.addDestination(this).
     * 
     * @param that The Plex to be put in this Plex's origins.
     */
    public void addOrigin(Plex that) {
        this.origins.add(that);
        that.destinations.add(this);
    }
    
    /**
     * Removes the argument from this Plex's origins, <i>and</i><br>
     * removes this Plex from the argument's destinations.
     * this.removeOrigin(that) is equivalent to that.removeDestination(this).
     * 
     * @param that The Plex to be removed from this Plex's origins.
     */   
    public void removeOrigin(Plex that) {
        this.origins.remove(that);
        that.destinations.remove(this);
    }

    /**
     * Adds the argument to this Plex's destinations, <i>and</i><br>
     * adds this Plex to the argument's origins.
     * addDestination(that) is equivalent to that.addOrigin(this).
     * 
     * @param that The Plex to be put in this Plex's destinations.
     */
    public void addDestination(Plex that) {
        this.destinations.add(that);
        that.origins.add(this);
    }
    
    /**
     * Removes the argument from this Plex's destinations, <i>and</i><br>
     * removes this Plex from the argument's origins.
     * this.removeDestination(that) is equivalent to that.removeOrigin(this).
     * 
     * @param that The Plex to be removed from this Plex's destinations.
     */   
    public void removeDestination(Plex that) {
        this.destinations.remove(that);
        that.origins.remove(this);
    }
    
    
    /**
     * Gets the member of a single-element set
     * @param set The single-element plex set
     * @return The member of the single-element plex set
     */
    public static Plex getOne(Set<Plex> set) {
        for (Plex plex : set) { 
            return plex;
        }
        return null; }
}