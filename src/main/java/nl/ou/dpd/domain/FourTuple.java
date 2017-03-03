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
    private boolean selfRef, matched, virtual;

    /**
     * This constructor has protected access because it is only available within subclasses.
     *
     * @param cl1 the "left" class or interface in the relation
     * @param cl2 the "right" class or interface in the relation
     * @param type the type of relation
     */
    protected FourTuple(Clazz cl1, Clazz cl2, EdgeType type) {
        classInterface1 = cl1;
        classInterface2 = cl2;
        typeRelation = type;
        selfRef = cl1.equals(cl2);

        matched = false;
        virtual = false;
    }

    /**
     * This constructor has protected access because it is only available within subclasses.
     *
     * @param ft a {@link FourTuple} to construct a copy of.
     */
    protected FourTuple(FourTuple ft) {
        this(ft.classInterface1, ft.classInterface2, ft.typeRelation);
        matched = ft.matched;
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
                    "(%15s, %15s, type relatie %2d, self ref: %3s, matched: %3s)\n",
                    classInterface1.getName(),
                    classInterface2.getName(),
                    typeRelation.getCode(),
                    selfRef ? "ja" : "nee",
                    matched ? "ja" : "nee");
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

    boolean equals(FourTuple ft) {
        return ft.getClass1().equals(classInterface1)
                && ft.getClass2().equals(classInterface2)
                && ft.getTypeRelation() == typeRelation
                && ft.getSelfRef() == selfRef;
    }

    boolean isMatched() {
        return matched;
    }

    void setMatched(boolean value) {
        matched = value;
    }

    Clazz getClass1() {
        return classInterface1;
    }

    Clazz getClass2() {
        return classInterface2;
    }

    EdgeType getTypeRelation() {
        return typeRelation;
    }

    boolean getSelfRef() {
        return selfRef;
    }

}
