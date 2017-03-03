package nl.ou.dpd.domain;

/**
 * A {@link Edge} represents an edge in a {@link DesignPattern} or a {@link SystemUnderConsideration}.
 * <p>
 * If an Edge (A, B, type) represents a bi-directional association, an Edge (B, A, type) will also be made. This
 * second Edge will have attribute virtual = true. Edges with virtual == true will not be shown (hidden).
 *
 * @author E.M. van Doorn
 * @author Martin de Boer
 */

public class Edge {

    private Clazz classInterface1, classInterface2;
    private EdgeType typeRelation;
    private boolean selfRef, locked, virtual;

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
        typeRelation = type;
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
        this(edge.classInterface1, edge.classInterface2, edge.typeRelation);
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
     * @deprecated All show methods must go. No more printing to System.out very soon.
     */
    void show() {
        if (!virtual) {
            System.out.printf(
                    "(%15s, %15s, type relatie %2d, self ref: %3s, locked: %3s)\n",
                    classInterface1.getName(),
                    classInterface2.getName(),
                    typeRelation.getCode(),
                    selfRef ? "ja" : "nee",
                    locked ? "ja" : "nee");
        }

    }

    /**
     * @deprecated All show methods must go. No more printing to System.out very soon.
     */
    void showSimple() {
        if (!virtual) {
            System.out.println(classInterface1.getName() + " --> " + classInterface2.getName());
        }
    }

    /**
     * Determines whether the specified {@link Edge} has the same classes/interfaces, type of relation and
     * self reference. In that case they are being considered equal.
     *
     * @param edge the {@link Edge} to compare {@code this} with
     * @return {@code true} if {@code this} equals {@code edge}, {@code false} otherwise
     */
    boolean equals(Edge edge) {
        return edge.getClass1().equals(classInterface1)
                && edge.getClass2().equals(classInterface2)
                && edge.getTypeRelation() == typeRelation
                && edge.getSelfRef() == selfRef;
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
    boolean isLocked() {
        return this.locked;
    }

    /**
     * Returns the first class/interface.
     *
     * @return the first class/interface.
     */
    Clazz getClass1() {
        return classInterface1;
    }

    /**
     * Returns the second class/interface.
     *
     * @return the second class/interface.
     */
    Clazz getClass2() {
        return classInterface2;
    }

    /**
     * Returns the type of relation between the first and the second class/interface.
     *
     * @return the edge type.
     */
    EdgeType getTypeRelation() {
        return typeRelation;
    }

    /**
     * Returns whether this edge references to itself. In that case the first and the second class/inteface are the
     * same.
     *
     * @return {@code true} if this edge references itself, or {@code false} otherwise.
     */
    boolean getSelfRef() {
        return selfRef;
    }

}
