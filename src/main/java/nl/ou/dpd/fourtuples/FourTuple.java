package nl.ou.dpd.fourtuples;

/**
 *
 * @author E.M. van Doorn
 */

public class FourTuple {

    private String classInterface1, classInterface2;
    private int typeRelation;
    private boolean selfRef, matched, virtual;
    // If a FourTuple (A, B, type) represents a bi-directional association,
    // a FourTuple(B, A, type) will be made.
    // This second FourTuple will have attribute virtual = true;
    // FourTuples with virtual = true will not be shown.

    public FourTuple() {
        classInterface1 = "";
        classInterface2 = "";
        typeRelation = FT_constants.ASSOCIATION;
        selfRef = false;
        matched = false;
        virtual = false;
    }

    public FourTuple(String cl1, String cl2, int type) {
        classInterface1 = cl1;
        classInterface2 = cl2;
        typeRelation = type;
        selfRef = cl1.equals(cl2);

        matched = false;
        virtual = false;
    }

    FourTuple(FourTuple ft) {
        this(ft.classInterface1, ft.classInterface2, ft.typeRelation);
        matched = ft.matched;
    }

    void makeVirtual() {
        String tmp;

        tmp = classInterface1;
        classInterface1 = classInterface2;
        classInterface2 = tmp;

        virtual = true;
    }

    boolean isMatch(FourTuple dp, MatchedNames matchedNames) // dp must be an edge of the design pattern
    {
        if (dp.typeRelation != typeRelation)
        {
            if (dp.typeRelation == FT_constants.INHERITANCE_MULTI
                    && typeRelation == FT_constants.INHERITANCE)
                ; // break; generates a warning.
            else
            {
                return false;
            }
        }

        if (dp.selfRef != selfRef)
        {
            return false;
        }

        // two empty names
        if (matchedNames.isEmpty(classInterface1)
                && matchedNames.isEmpty(classInterface2)
                && !matchedNames.valueIsBounded(dp.classInterface1)
                && !matchedNames.valueIsBounded(dp.classInterface2))
        {
            return true;
        }

        // first name matched, second name empty
        if (matchedNames.equals(classInterface1, dp.classInterface1)
                && matchedNames.isEmpty(classInterface2)
                && !matchedNames.valueIsBounded(dp.classInterface2))
        {
            return true;
        }

        // first name empty, second name matched
        if (matchedNames.isEmpty(classInterface1)
                && !matchedNames.valueIsBounded(dp.classInterface1)
                && matchedNames.equals(classInterface2, dp.classInterface2))
        {
            return true;
        }

        // both names are already matched.
        if (matchedNames.equals(classInterface1, dp.classInterface1)
                && matchedNames.equals(classInterface2, dp.classInterface2))
        {
            return true;
        }

        return false;
    }

    void makeMatch(FourTuple se, MatchedNames matchedNames) {
        matchedNames.add(se.classInterface1, classInterface1);
        matchedNames.add(se.classInterface2, classInterface2);
    }

    void show() {
        String sr, ma;

        if (virtual)
        {
            return;
        }

        sr = selfRef ? "ja" : "nee";
        ma = matched ? "ja" : "nee";

        System.out.printf("(%15s, %15s, type relatie %2d, self ref: %3s, matched: %3s)\n", classInterface1,
                classInterface2, typeRelation, sr, ma);
    }

    void showSimple() {
        if (virtual)
        {
            return;
        }

        System.out.println(classInterface1 + " --> " + classInterface2);
    }

    boolean equals(FourTuple ft) {
        return ft.getClassName1().equals(classInterface1)
                && ft.getClassName2().equals(classInterface2)
                && ft.getTypeRelation() == typeRelation
                && ft.selfRef == selfRef;
    }

    void setMatched(boolean value) {
        matched = value;
    }

    boolean getMatched() {
        return matched;
    }

    String getClassName1() {
        return classInterface1;
    }

    String getClassName2() {
        return classInterface2;
    }

    int getTypeRelation() {
        return typeRelation;
    }
}
