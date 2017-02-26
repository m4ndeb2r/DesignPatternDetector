package nl.ou.dpd.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an design pattern.
 *
 * @author Martin de Boer
 */
public class DesignPattern extends FourTupleArray {

    private String name;
    private Solutions solutions;

    /**
     * Constructs a {@link DesignPattern} instance with the specified name.
     *
     * @param name the name of this design pattern
     */
    public DesignPattern(String name) {
        this.name = name;
        this.solutions = new Solutions();
    }

    /**
     * @deprecated All show methods must go. No more printing to System.out very soon.
     */
    public void show() {
        if (!name.equals(EdgeType.EMPTY.getName())) {
            System.out.println("Design pattern: " + name);
        }

        for (FourTuple ft : getFourTuples()) {
            ft.show();
        }

        System.out.println();
    }

    /**
     * Attempts to match this {@link DesignPattern} with the specified {@link SystemUnderConsideration}.
     *
     * @param system          the system design to analyse and find the design pattern
     * @param maxNotMatchable the maximum number of edges allowed that do not match.
     * @return {@code true} if there was a match, of {@code false} otherwise.
     */
    public boolean match(SystemUnderConsideration system, int maxNotMatchable) {
        // Put every classname occuring in a 4-tuple in system in MatchedNames with value = EMPTY.
        MatchedNames matchedClassNames = fillMatchedNames(system);

        // Order the fourTuples
        setFourTuples(order(getFourTuples()));

        // Do the matching recursively
        return recursiveMatch(system, maxNotMatchable, 0, matchedClassNames);
    }

    // TODO: this method is large and hard to maintain. Refactor this (very carefully)....
    private boolean recursiveMatch(SystemUnderConsideration systemUnderConsideration, int maxNotMatchable,
                                   int startIndex, MatchedNames matchedClassNames) {
        // --this-- should contain the edges of the design pattern !!
        // For a particular edge (fourtuple.get(startIndex) in the design pattern
        // try to find the corresponding edge(s) in systemUnderConsideration.

        //int nNotMatchable;
        boolean found;
        FourTuple dpFt;

        if (startIndex >= getFourTuples().size()) {
            if (maxNotMatchable >= 0) {
                // This is an acceptable solution.
                if (solutions.isUniq(matchedClassNames.getBoundedSortedKeySet())) {
                    solutions.add(new Solution(matchedClassNames.getBoundedSortedKeySet()));

                    matchedClassNames.show(name);

                    // Does the detected design pattern have more edges than the design pattern?
                    // If so, show those edges.
                    systemUnderConsideration.showSupplementaryEdges(matchedClassNames);
                    System.out.println();
                }

                return true;
            }

            System.out.println("Should not be shown 1");

            // Should not occur. The search should be stopped before.

            return false;
        }

        dpFt = getFourTuples().get(startIndex); // improves readability

        found = false; // makes compiler happy;;

        // For this (startIndex) edge in DP, find matching edges in SE
        for (int j = 0; j < systemUnderConsideration.getFourTuples().size(); j++) {
            // For all edges in SE

            FourTuple seFt = systemUnderConsideration.getFourTuples().get(j);
            // improves readablity

            MatchedNames copyMatchedClassNames = new MatchedNames(matchedClassNames);
            ArrayList<Integer> extraMatched = new ArrayList<Integer>();

            if (!seFt.isMatched()
                    && seFt.isMatch(dpFt, matchedClassNames)) {
                boolean hulp;

                dpFt.makeMatch(seFt, copyMatchedClassNames);
                dpFt.setMatched(true);
                seFt.setMatched(true);

                if (dpFt.getTypeRelation() == EdgeType.INHERITANCE_MULTI) {
                    int k;

                    // There may be more edges of se which
                    // contains an inheritance to the same parent and
                    // have unmatched children
                    for (k = j + 1; k < systemUnderConsideration.getFourTuples().size(); k++) {
                        FourTuple sekFt = systemUnderConsideration.getFourTuples().get(k);
                        // for readablity;
                        if (!sekFt.isMatched()
                                && sekFt.getClassName2().equals(seFt.getClassName2())
                                && sekFt.isMatch(dpFt, matchedClassNames)) {
                            dpFt.makeMatch(sekFt, copyMatchedClassNames);
                            extraMatched.add(new Integer(k));
                            sekFt.setMatched(true);
                        }
                    }
                }

                hulp = recursiveMatch(systemUnderConsideration, maxNotMatchable,
                        startIndex + 1, copyMatchedClassNames);

                found = found || hulp;

                dpFt.setMatched(false);
                seFt.setMatched(false);

                // undo multiple matched edges.
                for (Integer getal : extraMatched) {
                    systemUnderConsideration.getFourTuples().get(getal.intValue()).setMatched(false);
                }

                if (found && extraMatched.size() > 0) {
                    // In case of inheritance with multiple childs
                    // all matching  edges has been found.
                    // Therefore searching for more edges may be stopped.

                    j = systemUnderConsideration.getFourTuples().size();
                }
            }
        }

        if (!found) {

            // is the number of not matched edges acceptable?
            if (--maxNotMatchable >= 0) {
                return recursiveMatch(systemUnderConsideration, maxNotMatchable,
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

    /**
     * Determines if two edges are connected. They are connected if one or more vertices in one have the same name as
     * one or more vertices in the other.
     *
     * @param edge1 the edge to compare to {@code edge2}
     * @param edge2 the edge to compare to {@code edge1}
     * @return {@code true} if the edges are connected, or {@code false} otherwise
     */
    private boolean areEdgesConnected(FourTuple edge1, FourTuple edge2) {
        final String v1 = edge1.getClassName1();
        final String v2 = edge1.getClassName2();
        final String v3 = edge2.getClassName1();
        final String v4 = edge2.getClassName2();
        return v1.equals(v3) || v1.equals(v4) || v2.equals(v3) || v2.equals(v4);
    }

    private MatchedNames fillMatchedNames() {
        MatchedNames names = new MatchedNames();

        for (FourTuple ft : getFourTuples()) {
            names.add(ft.getClassName1());
            names.add(ft.getClassName2());
        }

        return names;
    }

    private MatchedNames fillMatchedNames(SystemUnderConsideration system) {
        // No classname will be matched
        MatchedNames names = new MatchedNames();

        for (FourTuple ft : system.getFourTuples()) {
            names.add(ft.getClassName1());
            names.add(ft.getClassName2());
        }

        return names;
    }
}
