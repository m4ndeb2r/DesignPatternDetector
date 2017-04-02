package nl.ou.dpd.domain.edge;

import nl.ou.dpd.domain.DesignPattern;
import nl.ou.dpd.domain.SystemUnderConsideration;
import nl.ou.dpd.domain.node.Node;

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
    private Node node1, node2;
    private EdgeType relationType;
    private boolean selfRef, locked, virtual;
    private Cardinality cardinalityFront; //cardinality on node1
    private Cardinality cardinalityEnd; //cardinality on node2

    /**
     * Constructs an instance of a {@link Edge} with the specified class names and edge type. The classes
     * represent the vertices in a graph (when the design pattern is viewed as a graph), and the edge type represents
     * the relation type between the classes.
     *
     * @param node1 the "left" class or interface in the relation
     * @param node2 the "right" class or interface in the relation
     * @param type  the type of relation
     */
    public Edge(Node node1, Node node2, EdgeType type) {
        this.node1 = node1;
        this.node2 = node2;
        this.relationType = type;
        this.selfRef = node1.equals(node2);
        this.locked = false;
        this.virtual = false;
        this.cardinalityFront = null;
        this.cardinalityEnd = null;
    }


    /**
     * Constructs an instance of a {@link Edge} with the specified class names, edge type and a name of the edge. The
     * nodes represent the vertices in a graph (when the design pattern is viewed as a graph), and the edge type
     * represents the relation type between the nodes.
     *
     * @param node1 the "left" class or interface in the relation
     * @param node2 the "right" class or interface in the relation
     * @param type  the type of relation
     * @param name  the name of the edge. Recommended as being unique (in a pattern).
     */
    public Edge(Node node1, Node node2, EdgeType type, String name) {
        this(node1, node2, type);
        this.name = name;
    }

    /**
     * This constructor returns a duplicate of the specified {@link Edge}.
     *
     * @param edge an {@link Edge} to construct a duplicate of.
     */
    public Edge(Edge edge) {
        this(edge.node1, edge.node2, edge.relationType, edge.name);
        this.cardinalityFront = edge.cardinalityFront;
        this.cardinalityEnd = edge.cardinalityEnd;
        this.locked = edge.locked;
        this.virtual = edge.virtual;
    }

    /**
     * Creates a virtual (none-visible) counterpart of a {@link Edge}.
     */
    public void makeVirtual() {
        Node tmp;

        tmp = node1;
        node1 = node2;
        node2 = tmp;

        virtual = true;
    }

    /**
     * Set the name of this edge.
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set the name of this edge.
     *
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
    public boolean lock() {
        this.locked = true;
        return isLocked();
    }

    /**
     * Unlocks a {@link Edge} so it may be matched again.
     *
     * @return {@code true} if unlocking succeeded, or {@code false} otherwise.
     */
    public boolean unlock() {
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
     * Returns the first {@link Node}.
     *
     * @return the first {@link Node}.
     */
    public Node getNode1() {
        return node1;
    }

    /**
     * Returns the second {@link Node}.
     *
     * @return the second {@link Node}.
     */
    public Node getNode2() {
        return node2;
    }

    /**
     * Returns the type of relation between the first and the second {@link Node}.
     *
     * @return the edge type.
     */
    public EdgeType getRelationType() {
        return relationType;
    }

    /**
     * Returns whether this edge references to itself. In that case the first and the second {@link Node} are the
     * same.
     *
     * @return {@code true} if this edge references itself, or {@code false} otherwise.
     */
    public boolean isSelfRef() {
        return selfRef;
    }

    /**
     * Set the cardinality of {@link #node1}.
     *
     * @param lower the lower cardinality value
     * @param upper the upper cardinality value
     */
    public void setCardinalityFront(int lower, int upper) {
        this.cardinalityFront = new Cardinality(lower, upper);
    }

    /**
     * Set the cardinality of {@link #node2}.
     *
     * @param lower the lower cardinality value
     * @param upper the upper cardinality value
     */
    public void setCardinalityEnd(int lower, int upper) {
        this.cardinalityEnd = new Cardinality(lower, upper);
    }

    /**
     * Get the cardinality of classinterface1.
     *
     * @return the cardinality
     */
    public Cardinality getCardinalityFront() {
        return cardinalityFront;
    }

    /**
     * Get the cardinality of classinterface1.
     *
     * @return the cardinality
     */
    public Cardinality getCardinalityEnd() {
        return cardinalityEnd;
    }

    /**
     * Remove the cardinality of {@link #node1}.
     */
    public void removeCardinalityFront() {
        cardinalityFront = null;
    }

    /**
     * Remove the cardinality of {@link #node2}.
     */
    public void removeCardinalityEnd() {
        cardinalityEnd = null;
    }


    /**
     * Checks if the specified {@link Object} equals this {@link Edge}. The attrbute {@link #locked} is omitted in the
     * check, and is therefore irrelevant.
     *
     * @param o an {@link Edge} object to comapre with this one
     * @return {@code true} if {@code o} equals this {@link Edge} (omitting the {@link #locked} attribute, or
     * {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return selfRef == edge.selfRef &&
                virtual == edge.virtual &&
                Objects.equals(name, edge.name) &&
                Objects.equals(node1, edge.node1) &&
                Objects.equals(node2, edge.node2) &&
                relationType == edge.relationType &&
                Objects.equals(cardinalityFront, edge.cardinalityFront) &&
                Objects.equals(cardinalityEnd, edge.cardinalityEnd);
    }

    /**
     * Returns a hashcode for this object, based on all the attributes, except {@link #locked}.
     *
     * @return the generated hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, node1, node2, relationType, selfRef, virtual, cardinalityFront, cardinalityEnd);
    }

}
