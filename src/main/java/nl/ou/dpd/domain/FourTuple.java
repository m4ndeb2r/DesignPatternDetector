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

    private String classInterface1, classInterface2;
    private EdgeType typeRelation;
    private boolean selfRef, matched, virtual;

    /**
     * This constructor has protected access because it is only available within subclasses.
     *
     * @param cl1
     * @param cl2
     * @param type
     */
    protected FourTuple(String cl1, String cl2, EdgeType type) {
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
        String tmp;

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
            final String sr = selfRef ? "ja" : "nee";
            final String ma = matched ? "ja" : "nee";
            System.out.printf("(%15s, %15s, type relatie %2d, self ref: %3s, matched: %3s)\n", classInterface1,
                    classInterface2, typeRelation.getCode(), sr, ma);
        }

    }

    /**
     * @deprecated All show methods must go. No more printing to System.out very soon.
     */
    void showSimple() {
        if (!virtual) {
            System.out.println(classInterface1 + " --> " + classInterface2);
        }
    }

    boolean equals(FourTuple ft) {
        return ft.getClassName1().equals(classInterface1)
                && ft.getClassName2().equals(classInterface2)
                && ft.getTypeRelation() == typeRelation
                && ft.selfRef == selfRef;
    }

    boolean isMatched() {
        return matched;
    }

    void setMatched(boolean value) {
        matched = value;
    }

    String getClassName1() {
        return classInterface1;
    }

    String getClassName2() {
        return classInterface2;
    }

    EdgeType getTypeRelation() {
        return typeRelation;
    }

    boolean getSelfRef() {
        return selfRef;
    }
}
