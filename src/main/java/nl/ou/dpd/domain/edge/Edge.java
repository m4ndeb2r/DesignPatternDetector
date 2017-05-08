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

    private String id;
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
     * @param name      the name of the edge. Recommended as being unique (in a pattern).
     * @param leftNode  the "left" class or interface in the relation
     * @param rightNode the "right" class or interface in the relation
     */
    public Edge(Node leftNode, Node rightNode, EdgeType type, String name) {
        this(leftNode, rightNode, type);
        this.name = name;
    }

    /*18/04/17*/

    /**
     * Constructs an instance of a {@link Edge} with the specified nodes and a name of the edge. The
     * nodes represent the vertices in a graph (when the design pattern is viewed as a graph).
     *
     * @param leftNode  the "left" class or interface in the relation
     * @param rightNode the "right" class or interface in the relation
     * @param type      the type of relation
     * @param name      the name of the edge. Recommended as being unique (in a pattern).
     */
    public Edge(String id, String name, Node leftNode, Node rightNode) {
        this.id = id;
        this.name = name;
        this.leftNode = leftNode;
        this.rightNode = rightNode;
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

    /*18/04/17*/

    /**
     * This method returns a duplicate of this {@link Edge}.
     *
     * @return a duplicate of this {@link Edge}.
     */
    public Edge duplicate() {
        Edge newEdge = new Edge(this.getId(), this.getName(), this.getLeftNode(), this.getRightNode());
        newEdge.cardinalityLeft = this.cardinalityLeft;
        newEdge.cardinalityRight = this.cardinalityRight;
        newEdge.locked = this.locked;
        newEdge.virtual = this.virtual;
        return newEdge;
    }

    /**
     * Turns this {@link Edge} into a virtual (none-visible) {@link Edge}. It reverses the left and right {@link Node},
     * and sets the the {@code virtual} flag to {@code true}.
     */
    public void makeVirtual() {
        final Node tmp = leftNode;
        leftNode = rightNode;
        rightNode = tmp;
        virtual = true;
    }

    /**
     * Get the name of this edge.
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

    /*18/04/17*/

    /**
     * Get the id of this edge.
     *
     * @return id
     */
    public String getId() {
        return id;
    }

    /*18/04/17*/

    /**
     * Set the id of this edge.
     *
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    } 
    
    /*27/04/17*/

    /**
     * Indicates whether or not this {@link Edge} is virtual (non-visible).
     *
     * @return {@code true} if virtual, or {@code false} otherwise.
     */
    public boolean isVirtual() {
        return virtual;
    }
    
   
    /*27/04/17*/

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
     * Set the left node of this edge.
     *
     * @param Node
     */
    public void setLeftNode(Node node) {
        this.leftNode = node;
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
     * Set the right node of this edge.
     *
     * @param Node
     */
    public void setRightNode(Node node) {
        this.rightNode = node;
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
     * Sets the type of relation between the first and the second {@link Node}.
     *
     * @return the edge type.
     */
    public void setRelationType(EdgeType relationType) {
        this.relationType = relationType;
        //TODO make a virtual edge when relation type is ASSOCIATION
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

    public void setCardinalityLeft(Cardinality cardinality) {
        this.cardinalityLeft = cardinality;
    }

    public void setCardinalityRight(Cardinality cardinality) {
        this.cardinalityRight = cardinality;
    }

    public Cardinality getCardinalityLeft() {
        return cardinalityLeft;
    }

    public Cardinality getCardinalityRight() {
        return cardinalityRight;
    }

    public void removeCardinalityLeft() {
        cardinalityLeft = null;
    }

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
