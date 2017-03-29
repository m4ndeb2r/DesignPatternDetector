package nl.ou.dpd.domain;

import java.util.Objects;

/**
 * A {@link Edge} represents an edge in a {@link DesignPattern} or a {@link SystemUnderConsideration}.
 * <p>
 * If an Edge (A, B, type) represents a bi-directional association, an Edge (B, A, type) will also be made. This
 * second Edge will have attribute virtual = true. Edges with virtual == true will not be shown (hidden).
 *
 * @author E.M. van Doorn
 * @author Martin de Boer
 * @author Peter Vansweevelt
 */

public class Edge {

	private String name;
    private Clazz classInterface1, classInterface2;
    private EdgeType relationType;
    private boolean selfRef, locked, virtual;
    private Cardinality cardinality_front = null; //cardinality on classInterface1
    private Cardinality cardinality_end = null; //cardinality on classInterface2

    /**
     * Constructs an instance of a {@link Edge} with the specified class names and edge type. The classes
     * represent the vertices in a graph (when the design pattern is viewed as a graph), and the edge type represents
     * the relation type between the classes.
     *
     * @param cl1  the "left" class or interface in the relation
     * @param cl2  the "right" class or interface in the relation
     * @param type the type of relation
     */
    public Edge(Clazz cl1, Clazz cl2, EdgeType type) {
        classInterface1 = cl1;
        classInterface2 = cl2;
        relationType = type;
        selfRef = cl1.equals(cl2);
        locked = false;
        virtual = false;
    }


    /**
     * Constructs an instance of a {@link Edge} with the specified class names, edge type and a name of the edge. The classes
     * represent the vertices in a graph (when the design pattern is viewed as a graph), and the edge type represents
     * the relation type between the classes.
     *
     * @param cl1  the "left" class or interface in the relation
     * @param cl2  the "right" class or interface in the relation
     * @param type the type of relation
     * @param name the name of the edge. Recommended as being unique (in a pattern). 
     */
    public Edge(Clazz cl1, Clazz cl2, EdgeType type, String name) {
    	this.name = name;
        classInterface1 = cl1;
        classInterface2 = cl2;
        relationType = type;
        selfRef = cl1.equals(cl2);
        locked = false;
        virtual = false;
    }

/**
     * This constructor returns a duplicate of the specified {@link Edge}.
     *
     * @param edge an {@link Edge} to construct a duplicate of.
     */
    public Edge(Edge edge) {
        this(edge.classInterface1, edge.classInterface2, edge.relationType);
        locked = edge.locked;
    }

    /**
     * Creates a virtual (none-visible) counterpart of a {@link Edge}.
     */
    void makeVirtual() {
        Clazz tmp;

        tmp = classInterface1;
        classInterface1 = classInterface2;
        classInterface2 = tmp;

        virtual = true;
    }

    /**
     * Set the name of this edge.
     * @param name
     */
    public void setName(String name) {
    	this.name = name;
    }
    
    /**
     * Set the name of this edge.
     * @return name
     */
    public String getName() {
    	return name;
    }    
    
    /**
     * Indicates whether or not this {@link Edge} is virtual (non-visible).
     *
     * @return {@code true} if virtual, or {@code false} otherwise.
     */
    public boolean isVirtual() {
        return virtual;
    }

    /**
     * Locks a {@link Edge} to prevent it from being matched (again).
     *
     * @return {@code true} if the lock succeeded, or {@code false} otherwise.
     */
    boolean lock() {
        this.locked = true;
        return isLocked();
    }

    /**
     * Unlocks a {@link Edge} so it may be matched again.
     *
     * @return {@code true} if unlocking succeeded, or {@code false} otherwise.
     */
    boolean unlock() {
        this.locked = false;
        return !isLocked();
    }

    /**
     * Returns whether or not this {@link Edge} is locked.
     *
     * @return {@code true} when it is locked, or {@code false} if it is not.
     */
    public boolean isLocked() {
        return this.locked;
    }

    /**
     * Returns the first class/interface.
     *
     * @return the first class/interface.
     */
    public Clazz getClass1() {
        return classInterface1;
    }

    /**
     * Returns the second class/interface.
     *
     * @return the second class/interface.
     */
    public Clazz getClass2() {
        return classInterface2;
    }

    /**
     * Returns the type of relation between the first and the second class/interface.
     *
     * @return the edge type.
     */
    public EdgeType getRelationType() {
        return relationType;
    }

    /**
     * Returns whether this edge references to itself. In that case the first and the second class/inteface are the
     * same.
     *
     * @return {@code true} if this edge references itself, or {@code false} otherwise.
     */
    public boolean isSelfRef() {
        return selfRef;
    }

    /**
     * Set the cardinality of classinterface1. 
     * @param lower
     * @param upper
     */
    public void setCardinality_front(int lower, int upper) {
    	if (cardinality_front == null) {
            cardinality_front = new Cardinality(lower, upper);
    	} else {
    		cardinality_front.lower = lower;
    		cardinality_front.upper = upper;    		
    	}
    }
    
    /**
     * Set the cardinality of classinterface1. 
     * @param lower
     * @param upper
     */
    public void setCardinality_end(int lower, int upper) {
    	if (cardinality_end == null) {
            cardinality_end = new Cardinality(lower, upper);
    	} else {
    		cardinality_end.lower = lower;
    		cardinality_end.upper = upper;    		
    	}
    }
 
    /**
     * Get the cardinality of classinterface1. 
     * @return the cardinality
     */
    public Cardinality getCardinality_front() {
    	return cardinality_front;
    }
    
    /**
     * Get the cardinality of classinterface1. 
     * @return the cardinality
     */
    public Cardinality getCardinality_end() {
    	return cardinality_end;
    }
    
    /**
     * Remove the cardinality of classinterface1. 
     * @return the cardinality
     */
    public void removeCardinality_front() {
    	cardinality_front = null;
    }
    
    /**
     * Remove the cardinality of classinterface1. 
     * @return the cardinality
     */
    public void removeCardinality_end() {
    	cardinality_end = null;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Edge edge = (Edge) o;
        return selfRef == edge.selfRef
                && virtual == edge.virtual
                && Objects.equals(classInterface1, edge.classInterface1)
                && Objects.equals(classInterface2, edge.classInterface2)
                && relationType == edge.relationType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(classInterface1, classInterface2, relationType, selfRef, virtual);
    }
}
