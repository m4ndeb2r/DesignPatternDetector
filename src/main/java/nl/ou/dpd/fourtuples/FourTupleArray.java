package nl.ou.dpd.fourtuples;

import java.util.ArrayList;
import java.util.List;

/**
 * @author E.M. van Doorn
 */
public class FourTupleArray {

    protected List<FourTuple> fourTuples; // protected for testing purposes; TODO: turn back to private again
    private String dpName;
    private Solutions solutions;

    /**
     * The default constructor. This constructor initialises all the attributes with default values.
     */
    public FourTupleArray() {
        fourTuples = new ArrayList();
        dpName = FT_constants.EMPTY;
        solutions = new Solutions();
    }

    /**
     * A constructor based on the name of the design pattern this {@link FourTupleArray} represents.
     *
     * @param name a design pattern name.
     */
    public FourTupleArray(String name) {
        this();
        dpName = name;
    }

    /**
     * Check if a specified {@link FourTupleArray} matches this {@link FourTupleArray}. At most {@code maxNotMatchable}
     * may be missing in the specified {@link FourTupleArray}.
     *
     * @param fta             the {@link FourTupleArray} to match with this {@link FourTupleArray}. The argument
     *                        {@code fta} represents (part of) the "system under consideration".
     * @param maxNotMatchable the maximum allowed number of edges missing.
     * @return {@code true} if there is a match, or {@code false} otherwise.
     */
    public boolean match(final FourTupleArray fta, final int maxNotMatchable) {
        // Put every classname occuring in a 4-tuple in fta in MatchedNames with value = EMPTY.
        MatchedNames matchedClassNames = fillMatchedNames(fta);

        // Order the fourTuples
        fourTuples = order(fourTuples);

        // Do the matching recursively
        return recursiveMatch(fta, maxNotMatchable, 0, matchedClassNames);
    }

    private boolean recursiveMatch(FourTupleArray fta, int maxNotMatchable,
                                   int startIndex, MatchedNames matchedClassNames) {
        // --this-- should contain the edges of the design pattern !!
        // For a particular edge (fourtuple.get(startIndex) in the design pattern
        // try to find the corresponding edge(s) in fta.

        //int nNotMatchable;
        boolean found;
        FourTuple dpFt;

        if (startIndex >= fourTuples.size()) {
            if (maxNotMatchable >= 0) {
                // This is an acceptable solution.
                if (solutions.isUniq(matchedClassNames.getBoundedSortedKeySet())) {
                    solutions.add(new Solution(matchedClassNames.getBoundedSortedKeySet()));

                    matchedClassNames.show(dpName);

                    // Does the detected design pattern have more edges than the design pattern?
                    // If so, show those edges.
                    showSupplementaryEdges(fta, matchedClassNames);
                    System.out.println();
                }

                return true;
            }

            System.out.println("Should not be shown 1");

            // Should not occur. The search should be stopped before.

            return false;
        }

        dpFt = fourTuples.get(startIndex); // improves readability

        found = false; // makes compiler happy;;

        // For this (startIndex) edge in DP, find matching edges in SE
        for (int j = 0; j < fta.fourTuples.size(); j++) {
            // For all edges in SE

            FourTuple seFt = fta.fourTuples.get(j);
            // improves readablity

            MatchedNames copyMatchedClassNames = new MatchedNames(matchedClassNames);
            ArrayList<Integer> extraMatched = new ArrayList<Integer>();

            if (!seFt.getMatched()
                    && seFt.isMatch(dpFt, matchedClassNames)) {
                boolean hulp;

                dpFt.makeMatch(seFt, copyMatchedClassNames);
                dpFt.setMatched(true);
                seFt.setMatched(true);

                if (dpFt.getTypeRelation() == FT_constants.INHERITANCE_MULTI) {
                    int k;

                    // There may be more edges of se which
                    // contains an inheritance to the same parent and
                    // have unmatched children
                    for (k = j + 1; k < fta.fourTuples.size(); k++) {
                        FourTuple sekFt = fta.fourTuples.get(k);
                        // for readablity;
                        if (!sekFt.getMatched()
                                && sekFt.getClassName2().equals(seFt.getClassName2())
                                && sekFt.isMatch(dpFt, matchedClassNames)) {
                            dpFt.makeMatch(sekFt, copyMatchedClassNames);
                            extraMatched.add(new Integer(k));
                            sekFt.setMatched(true);
                        }
                    }
                }

                hulp = recursiveMatch(fta, maxNotMatchable,
                        startIndex + 1, copyMatchedClassNames);

                found = found || hulp;

                dpFt.setMatched(false);
                seFt.setMatched(false);

                // undo multiple matched edges.
                for (Integer getal : extraMatched) {
                    fta.fourTuples.get(getal.intValue()).setMatched(false);
                }

                if (found && extraMatched.size() > 0) {
                    // In case of inheritance with multiple childs
                    // all matching  edges has been found.
                    // Therefore searching for more edges may be stopped.

                    j = fta.fourTuples.size();
                }
            }
        }

        if (!found) {

            // is the number of not matched edges acceptable?
            if (--maxNotMatchable >= 0) {
                return recursiveMatch(fta, maxNotMatchable,
                        startIndex + 1, matchedClassNames);
            }

            return false;
        }

        return found; //  == true !!
    }

    /**
     * This method orders the array of {@link FourTuple}'s. It guarantees that every edge in the graph has at least one
     * vertex that is also present in one or more preceding edges. One exception to this rule is the first edge in the
     * graph, obviously because it has no preceding edge. In other words: for every edge E(v1 -> v2) in the graph
     * (except the first one), a previous edge E(v1 -> x2) or E(x1 -> v2) is present. This way every edge is connected
     * to a vertex of a preceding edge.
     * <p/>
     * Example: A->B, C->D, A->C becomes A->B, A->C, C->D.
     *
     * @param graph the graph to order
     * @return the ordered graph
     */
    private List<FourTuple> order(final List<FourTuple> graph) {
        // Skip the first element. It stays where it is. Start with i = 1.
        for (int i = 1; i < graph.size(); i++) {

            boolean found = false;

            // The i-element of fourTuple should have a classname that occurs in the elements 0.. (i-1)
            for (int j = i; j < graph.size() && !found; j++) {
                for (int k = 0; k < i && !found; k++) {
                    found = areEdgesConnected(graph.get(j), graph.get(k));
                    if (found && (j != i)) {
                        // Switch elements
                        final FourTuple temp = graph.get(j);
                        graph.set(j, graph.get(i));
                        graph.set(i, temp);
                    }
                }
            }

            if (!found) {
                System.out.println("Warning: Template is not a connected graph.");
            }
        }
        return graph;
    }

    private boolean areEdgesConnected(FourTuple edge1, FourTuple edge2) {
        final String v1 = edge1.getClassName1();
        final String v2 = edge1.getClassName2();
        final String v3 = edge2.getClassName1();
        final String v4 = edge2.getClassName2();
        return v1.equals(v3) || v1.equals(v4) || v2.equals(v3) || v2.equals(v4);
    }

    private MatchedNames fillMatchedNames() {
        MatchedNames names = new MatchedNames();

        for (FourTuple ft : fourTuples) {
            names.add(ft.getClassName1());
            names.add(ft.getClassName2());
        }

        return names;
    }

    private MatchedNames fillMatchedNames(FourTupleArray fta) {
        // No classname will be matched
        MatchedNames names = new MatchedNames();

        for (FourTuple ft : fta.fourTuples) {
            names.add(ft.getClassName1());
            names.add(ft.getClassName2());
        }

        return names;
    }

    public void add(FourTuple ft) {
        fourTuples.add(new FourTuple(ft));

        if (ft.getTypeRelation() == FT_constants.ASSOCIATION)
        // For edge (A, B, ....) a second but virtual edge (B, A, ...)
        // will be added.
        {
            FourTuple tmp;

            tmp = new FourTuple(ft);
            tmp.makeVirtual();
            fourTuples.add(tmp);
        }
    }

    public void show() {
        if (!dpName.equals(FT_constants.EMPTY)) {
            System.out.println("Design pattern: " + dpName);
        }

        for (FourTuple ft : fourTuples) {
            ft.show();
        }

        System.out.println();
    }

    void showSupplementaryEdges(FourTupleArray fta, MatchedNames matchedClassNames) {
        // Show all edges in fta which both classes are matched in this
        // design pattern but ther is no coressponding edge in the design
        // pattern.

        boolean found;

        found = false;

        for (FourTuple edge : fta.fourTuples) {
            if (matchedClassNames.keyIsBounded(edge.getClassName1())
                    && matchedClassNames.keyIsBounded(edge.getClassName2())) {
                if (!edge.getMatched()) {
                    if (!found) {
                        System.out.println("Edges which do not belong to this design pattern:");
                        found = true;
                    }

                    edge.showSimple();
                }
            }
        }

        if (found) {
            System.out.println("==================================================");
        }
    }
}
