package nl.ou.dpd.domain;

/**
 * A {@link FourTuple} represents an edge in a {@link DesignPattern} or a {@link SystemUnderConsideration}.
 * <p>
 * If a FourTuple (A, B, type) represents a bi-directional association, a FourTuple (B, A, type) will be made. This
 * second FourTuple will have attribute virtual = true. FourTuples with virtual == true will not be shown.
 *
 * @author E.M. van Doorn
 * @author Martin de Boer
 */

public abstract class FourTuple {

    private Clazz classInterface1, classInterface2;
    private EdgeType typeRelation;
    private boolean selfRef, locked, virtual;

    /**
     * This constructor has protected access because it is only available within subclasses.
     *
     * @param cl1  the "left" class or interface in the relation
     * @param cl2  the "right" class or interface in the relation
     * @param type the type of relation
     */
    protected FourTuple(Clazz cl1, Clazz cl2, EdgeType type) {
        classInterface1 = cl1;
        classInterface2 = cl2;
        typeRelation = type;
        selfRef = cl1.equals(cl2);

        locked = false;
        virtual = false;
    }

    /**
     * This constructor has protected access because it is only available within subclasses.
     *
     * @param ft a {@link FourTuple} to construct a copy of.
     */
    protected FourTuple(FourTuple ft) {
        this(ft.classInterface1, ft.classInterface2, ft.typeRelation);
        locked = ft.locked;
    }

    /**
     * Creates a virtual (none visible) counterpart of a {@link FourTuple}.
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
     * Determines whether the specified {@link FourTuple} has the same classes/interfaces, type of relation and
     * self reference. In that case they are being consideren equal.
     *
     * @param ft the {@link FourTuple} to compare {@code this} with
     * @return {@code true} if {@code this} equals {@code ft}, {@code false} otherwise
     */
    boolean equals(FourTuple ft) {
        return ft.getClass1().equals(classInterface1)
                && ft.getClass2().equals(classInterface2)
                && ft.getTypeRelation() == typeRelation
                && ft.getSelfRef() == selfRef;
    }

    /**
     * Locks a {@link FourTuple} to prevent it from being matched.
     *
     * @return {@code true} if the lock succeeded, or {@code false} otherwise.
     */
    boolean lock() {
        this.locked = true;
        return isLocked();
    }

    /**
     * Unlocks a {@link FourTuple} so it may be matched again.
     *
     * @return {@code true} if unlocking succeeded, or {@code false} otherwise.
     */
    boolean unlock() {
        this.locked = false;
        return !isLocked();
    }

    /**
     * Returns whether or not this {@link FourTuple} is locked.
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
