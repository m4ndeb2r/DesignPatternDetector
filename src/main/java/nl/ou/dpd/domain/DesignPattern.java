package nl.ou.dpd.domain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an design pattern.
 *
 * @author Martin de Boer
 */
public class DesignPattern extends FourTupleArray<DesignPatternEdge, DesignPatternEdgeFactory> {

    private static final Logger LOGGER = LogManager.getLogger(DesignPattern.class);

    private String name;
    private Solutions solutions;

    /**
     * Constructs a {@link DesignPattern} instance with the specified name.
     *
     * @param name the name of this design pattern
     */
    public DesignPattern(String name) {
        super(new DesignPatternEdgeFactory());
        this.name = name;
        this.solutions = new Solutions();
    }

    /**
     * @deprecated All show methods must go. No more printing to System.out very soon.
     */
    public void show() {
        if (!name.isEmpty()) {
            System.out.println("Design pattern: " + name);
        }

        for (DesignPatternEdge edge : getFourTuples()) {
            edge.show();
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
        // Put every classname occuring in a 4-tuple in system in MatchedClasses with value = EMPTY.
        MatchedClasses matchedClasses = fillMatchedClasses(system);

        // Order the fourTuples
        setFourTuples(order(getFourTuples()));

        // Do the matching recursively
        return recursiveMatch(system, maxNotMatchable, 0, matchedClasses);
    }

    // TODO: this method is large and hard to maintain. Refactor this (very carefully)....
    private boolean recursiveMatch(SystemUnderConsideration systemUnderConsideration, int maxNotMatchable,
                                   int startIndex, MatchedClasses matchedClasses) {
        // --this-- should contain the edges of the design pattern !!
        // For a particular edge (fourtuple.get(startIndex) in the design pattern
        // try to find the corresponding edge(s) in systemUnderConsideration.

        //int nNotMatchable;
        boolean found;

        if (startIndex >= getFourTuples().size()) {
            if (maxNotMatchable >= 0) {
                // This is an acceptable solution.
                if (solutions.isUniq(matchedClasses.getBoundedSortedKeySet())) {
                    solutions.add(new Solution(matchedClasses.getBoundedSortedKeySet()));

                    matchedClasses.show(name);

                    // Does the detected design pattern have more edges than the design pattern?
                    // If so, show those edges.
                    systemUnderConsideration.showSupplementaryEdges(matchedClasses);
                    System.out.println();
                }

                return true;
            }

            LOGGER.warn("Unexpected situation in DesignPattern#recursiveMatch(). " +
                    "Value of maxNotMatchable = " + maxNotMatchable);

            // Should not occur. The search should be stopped before.

            return false;
        }

        DesignPatternEdge dpEdge = getFourTuples().get(startIndex); // improves readability

        found = false; // makes compiler happy;;

        // For this (startIndex) edge in DP, find matching edges in SE
        for (int j = 0; j < systemUnderConsideration.getFourTuples().size(); j++) {
            // For all edges in SE

            SystemUnderConsiderationEdge sysEdge = systemUnderConsideration.getFourTuples().get(j);
            // improves readablity

            MatchedClasses copyMatchedClasses = new MatchedClasses(matchedClasses);
            ArrayList<Integer> extraMatched = new ArrayList<Integer>();

            if (!sysEdge.isMatched()
                    && sysEdge.isMatch(dpEdge, matchedClasses)) {
                boolean hulp;

                dpEdge.makeMatch(sysEdge, copyMatchedClasses);
                dpEdge.setMatched(true);
                sysEdge.setMatched(true);

                if (dpEdge.getTypeRelation() == EdgeType.INHERITANCE_MULTI) {
                    int k;

                    // There may be more edges of se which
                    // contains an inheritance to the same parent and
                    // have unmatched children
                    for (k = j + 1; k < systemUnderConsideration.getFourTuples().size(); k++) {
                        SystemUnderConsiderationEdge skEdge = systemUnderConsideration.getFourTuples().get(k);
                        // for readablity;
                        if (!skEdge.isMatched()
                                && skEdge.getClass2().equals(sysEdge.getClass2())
                                && skEdge.isMatch(dpEdge, matchedClasses)) {
                            dpEdge.makeMatch(skEdge, copyMatchedClasses);
                            extraMatched.add(new Integer(k));
                            skEdge.setMatched(true);
                        }
                    }
                }

                hulp = recursiveMatch(systemUnderConsideration, maxNotMatchable,
                        startIndex + 1, copyMatchedClasses);

                found = found || hulp;

                dpEdge.setMatched(false);
                sysEdge.setMatched(false);

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
                        startIndex + 1, matchedClasses);
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
    private List<DesignPatternEdge> order(final List<DesignPatternEdge> graph) {
        // Skip the first element. It stays where it is. Start with i = 1.
        for (int i = 1; i < graph.size(); i++) {

            boolean found = false;

            // The i-element of fourTuple should have a classname that occurs in the elements 0.. (i-1)
            for (int j = i; j < graph.size() && !found; j++) {
                for (int k = 0; k < i && !found; k++) {
                    found = areEdgesConnected(graph.get(j), graph.get(k));
                    if (found && (j != i)) {
                        // Switch elements
                        final DesignPatternEdge temp = graph.get(j);
                        graph.set(j, graph.get(i));
                        graph.set(i, temp);
                    }
                }
            }

            if (!found) {
                LOGGER.warn("Template is not a connected graph.");
            }
        }
        return graph;
    }

    /**
     * Determines if two edges are connected. They are connected if one or more vertices in one are equal to one or
     * more vertices in the other.
     *
     * @param edge1 the edge to compare to {@code edge2}
     * @param edge2 the edge to compare to {@code edge1}
     * @return {@code true} if the edges are connected, or {@code false} otherwise
     */
    private boolean areEdgesConnected(DesignPatternEdge edge1, DesignPatternEdge edge2) {
        final Clazz v1 = edge1.getClass1();
        final Clazz v2 = edge1.getClass2();
        final Clazz v3 = edge2.getClass1();
        final Clazz v4 = edge2.getClass2();
        return v1.equals(v3) || v1.equals(v4) || v2.equals(v3) || v2.equals(v4);
    }

    private MatchedClasses fillMatchedClasses(SystemUnderConsideration system) {
        // No classname will be matched
        MatchedClasses matchedClasses = new MatchedClasses();

        for (SystemUnderConsiderationEdge edge : system.getFourTuples()) {
            matchedClasses.add(edge.getClass1());
            matchedClasses.add(edge.getClass2());
        }

        return matchedClasses;
    }
}
