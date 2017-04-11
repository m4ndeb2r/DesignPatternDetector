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
    private Node leftNode, rightNode;
    private EdgeType relationType;
    private boolean selfRef, locked, virtual;
    private Cardinality cardinalityLeft; //cardinality on leftNode
    private Cardinality cardinalityRight; //cardinality on rightNode

    /**
     * Constructs an instance of a {@link Edge} with the specified class names and edge type. The classes
     * represent the vertices in a graph (when the design pattern is viewed as a graph), and the edge type represents
     * the relation type between the classes.
     *
     * @param leftNode  the "left" class or interface in the relation
     * @param rightNode the "right" class or interface in the relation
     * @param type      the type of relation
     */
    public Edge(Node leftNode, Node rightNode, EdgeType type) {
        this.leftNode = leftNode;
        this.rightNode = rightNode;
        this.relationType = type;
        this.selfRef = leftNode.equals(rightNode);
        this.locked = false;
        this.virtual = false;
        this.cardinalityLeft = null;
        this.cardinalityRight = null;
    }


    /**
     * Constructs an instance of a {@link Edge} with the specified class names, edge type and a name of the edge. The
     * nodes represent the vertices in a graph (when the design pattern is viewed as a graph), and the edge type
     * represents the relation type between the nodes.
     *
     * @param leftNode  the "left" class or interface in the relation
     * @param rightNode the "right" class or interface in the relation
     * @param type      the type of relation
     * @param name      the name of the edge. Recommended as being unique (in a pattern).
     */
    public Edge(Node leftNode, Node rightNode, EdgeType type, String name) {
        this(leftNode, rightNode, type);
        this.name = name;
    }

    /**
     * This constructor returns a duplicate of the specified {@link Edge}.
     *
     * @param edge an {@link Edge} to construct a duplicate of.
     */
    public Edge(Edge edge) {
        this(edge.leftNode, edge.rightNode, edge.relationType, edge.name);
        this.cardinalityLeft = edge.cardinalityLeft;
        this.cardinalityRight = edge.cardinalityRight;
        this.locked = edge.locked;
        this.virtual = edge.virtual;
    }

    /**
     * Creates a virtual (none-visible) counterpart of a {@link Edge}.
     */
    public void makeVirtual() {
        Node tmp;

        tmp = leftNode;
        leftNode = rightNode;
        rightNode = tmp;

        virtual = true;
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
     * Set the name of this edge.
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
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
    public Node getLeftNode() {
        return leftNode;
    }

    /**
     * Returns the second {@link Node}.
     *
     * @return the second {@link Node}.
     */
    public Node getRightNode() {
        return rightNode;
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
     * Set the cardinality of {@link #leftNode}.
     *
     * @param lower the lower cardinality value
     * @param upper the upper cardinality value
     */
    public void setCardinalityLeft(int lower, int upper) {
        this.cardinalityLeft = new Cardinality(lower, upper);
    }

    /**
     * Set the cardinality of {@link #rightNode}.
     *
     * @param lower the lower cardinality value
     * @param upper the upper cardinality value
     */
    public void setCardinalityRight(int lower, int upper) {
        this.cardinalityRight = new Cardinality(lower, upper);
    }

    /**
     * Get the cardinality of the left node.
     *
     * @return the cardinality
     */
    public Cardinality getCardinalityLeft() {
        return cardinalityLeft;
    }

    /**
     * Get the cardinality of the right node.
     *
     * @return the cardinality
     */
    public Cardinality getCardinalityRight() {
        return cardinalityRight;
    }

    /**
     * Remove the cardinality of {@link #leftNode}.
     */
    public void removeCardinalityLeft() {
        cardinalityLeft = null;
    }

    /**
     * Remove the cardinality of {@link #rightNode}.
     */
    public void removeCardinalityRight() {
        cardinalityRight = null;
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
                Objects.equals(leftNode, edge.leftNode) &&
                Objects.equals(rightNode, edge.rightNode) &&
                relationType == edge.relationType &&
                Objects.equals(cardinalityLeft, edge.cardinalityLeft) &&
                Objects.equals(cardinalityRight, edge.cardinalityRight);
    }

    /**
     * Returns a hashcode for this object, based on all the attributes, except {@link #locked}.
     *
     * @return the generated hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, leftNode, rightNode, relationType, selfRef, virtual, cardinalityLeft, cardinalityRight);
    }

}
