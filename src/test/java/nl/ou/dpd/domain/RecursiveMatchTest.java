package nl.ou.dpd.domain;

import nl.ou.dpd.utils.TestHelper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Temporary test to help refactor the method {@link FourTupleArray#recursiveMatch(FourTupleArray, int, int, MatchedNames)}.
 * <p>
 * TODO: finish this test before refactoring the recursive match method!!!
 * TODO: After that, this test is obsolete: throw it away
 *
 * @author Martin de Boer
 */
public class RecursiveMatchTest {

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();
    private List<FourTuple> fourTuples; // The tuples of the design pattern to match
    private String dpName;              // The design pattern name
    private Solutions solutions;

    @Before
    public void setUp() {
    }

    @Test
    public void testRecursiveMatches() {

        solutions = new Solutions();
        dpName = "ChainOfResponsibility";
        fourTuples = TestHelper.createChainOfResponsibilityPattern().getFourTuples();
        SystemUnderConsideration system = TestHelper.createComplexSystemUnderConsideration();
        MatchedNames matchedClassNames = fillMatchedNames(system);
        int startIndex = 0;
        int maxNotMatchable = 0;
        order(fourTuples);
        recursiveMatch(system, maxNotMatchable, startIndex, matchedClassNames);
        String outputNew = systemOutRule.getLog();
        systemOutRule.clearLog();

        solutions = new Solutions();
        dpName = "ChainOfResponsibility";
        fourTuples = TestHelper.createChainOfResponsibilityPattern().getFourTuples();
        system = TestHelper.createComplexSystemUnderConsideration();
        matchedClassNames = fillMatchedNames(system);
        startIndex = 0;
        maxNotMatchable = 0;
        order(fourTuples);
        recursiveMatchOld(system, maxNotMatchable, startIndex, matchedClassNames);
        String outputOld = systemOutRule.getLog();
        systemOutRule.clearLog();

        assertThat(outputOld, is(outputNew));
    }


    /**
     * For a particular edge (fourtuple.get(index) in the design pattern, try to find the corresponding edges in
     * system.
     *
     * @param system            the "system under consideration"
     * @param maxNotMatchable   the number of unmatchable edges
     * @param index             the index tof the current edge
     * @param matchedClassNames the class names that were matched so far in the recursion
     * @return
     */
    private boolean recursiveMatch(
            final SystemUnderConsideration system,
            final int maxNotMatchable,
            final int index,
            final MatchedNames matchedClassNames) {
        // --this-- should contain the edges of the design pattern !!
        // For a particular edge (fourtuple.get(index) in the design pattern
        // try to find the corresponding edge(s) in system.


        if (index >= fourTuples.size()) {
            // We are done. Let's round up and stop the recursion
            return roundUp(system, maxNotMatchable, matchedClassNames);
        }

        // We're not done yet.
        final FourTuple currentDesignPatternEdge = fourTuples.get(index);
        boolean found = false;

        // For this (index) edge in DP, find matching edges in SE
        // For all edges in SE
        for (int i = 0; i < system.getFourTuples().size(); i++) {

            final FourTuple currentSystemEdge = system.getFourTuples().get(i);

            if (canMakeMatch(matchedClassNames, currentDesignPatternEdge, currentSystemEdge)) {
                makeMatch(currentDesignPatternEdge, currentSystemEdge, matchedClassNames);

                final ArrayList<Integer> extraMatched = new ArrayList<Integer>();
                if (currentDesignPatternEdge.getTypeRelation() == EdgeType.INHERITANCE_MULTI) {

                    // There may be more system edges containing an inheritance to the
                    // same parent and having unmatched children
                    matchChildren(
                            system.getFourTuples(),
                            matchedClassNames,
                            currentDesignPatternEdge,
                            i + 1,
                            currentSystemEdge,
                            extraMatched);
                }

                found |= recursiveMatch(system, maxNotMatchable, index + 1, matchedClassNames);

                currentDesignPatternEdge.setMatched(false);
                currentSystemEdge.setMatched(false);
                extraMatched.forEach(extra -> system.getFourTuples().get(extra).setMatched(false));

                // In case of inheritance with multiple children, all matching edges have been found.
                // Therefore searching for more edges may be stopped.
                if (found && extraMatched.size() > 0) {
                    break;
                }
            }
        }

        if (!found) {
            // is the number of not matched edges acceptable?
            if (maxNotMatchable - 1 >= 0) {
                return recursiveMatch(
                        system,
                        maxNotMatchable - 1,
                        index + 1,
                        matchedClassNames);
            }
            return false;
        }

        return found; //  == true !!
    }

    private void matchChildren(
            List<FourTuple> children,
            MatchedNames matchedClassNames,
            FourTuple designPatternEdge,
            int startIndex,
            FourTuple parent,
            ArrayList<Integer> extraMatched) {
        for (int i = startIndex; i < children.size(); i++) {
            final FourTuple child = children.get(i);
            if (canMakeMatch(matchedClassNames, designPatternEdge, child) && inheritsFrom(child, parent)) {
                makeMatch(designPatternEdge, child, matchedClassNames);
                extraMatched.add(i);
            }
        }
    }

    private boolean inheritsFrom(FourTuple child, FourTuple parent) {
        return (child.getTypeRelation().equals(EdgeType.INHERITANCE) || child.getTypeRelation().equals(EdgeType.INHERITANCE_MULTI))
                && child.getClassName2().equals(parent.getClassName2());
    }

    private boolean canMakeMatch(MatchedNames matchedClassNames, FourTuple designPatternEdge, FourTuple systemEdge) {
        return !systemEdge.isMatched() && systemEdge.isMatch(designPatternEdge, matchedClassNames);
    }

    private void makeMatch(FourTuple currentDesignPatternEdge, FourTuple currentSystemEdge, MatchedNames copyMatchedClassNames) {
        currentDesignPatternEdge.makeMatch(currentSystemEdge, copyMatchedClassNames);
        currentDesignPatternEdge.setMatched(true);
        currentSystemEdge.setMatched(true);
    }

    private boolean roundUp(SystemUnderConsideration system, int maxNotMatchable, MatchedNames matchedClassNames) {
        if (maxNotMatchable >= 0) {
            // This is an acceptable solution.
            if (solutions.isUniq(matchedClassNames.getBoundedSortedKeySet())) {
                solutions.add(new Solution(matchedClassNames.getBoundedSortedKeySet()));

                // Show the matching edges as well as the supplementary edges (if they exist)
                matchedClassNames.show(dpName);
                showSupplementaryEdges(getSupplementaryEdges(system, matchedClassNames));
            }
            return true;
        }

        // Should not occur. The search should be stopped before.
        System.out.println("Should not be shown 1");
        return false;
    }

    /**
     * Gets all edges in system which both classes are matched in this design pattern but ther is no coressponding edge
     * in the design pattern.
     *
     * @param system
     * @param matchedClassNames
     */
    List<FourTuple> getSupplementaryEdges(SystemUnderConsideration system, MatchedNames matchedClassNames) {
        final List<FourTuple> supplementaryEdges = new ArrayList<>();

        for (FourTuple edge : system.getFourTuples()) {
            final String className1 = edge.getClassName1();
            final String className2 = edge.getClassName2();
            final boolean class1IsBounded = matchedClassNames.keyIsBounded(className1);
            final boolean class2IsBounded = matchedClassNames.keyIsBounded(className2);
            if (class1IsBounded && class2IsBounded && !edge.isMatched()) {
                supplementaryEdges.add(edge);
            }
        }

        return supplementaryEdges;
    }

    void showSupplementaryEdges(List<FourTuple> supplementaryEdges) {
        if (supplementaryEdges.size() > 0) {
            System.out.println("Edges which do not belong to this design pattern:");
            for (FourTuple edge : supplementaryEdges) {
                edge.showSimple();
            }
            System.out.println("==================================================");
            System.out.println();
        }
    }


    //==============================================================================
    //
    // Old stuff below ...
    //
    //==============================================================================


    private boolean recursiveMatchOld(SystemUnderConsideration system, int maxNotMatchable,
                                      int startIndex, MatchedNames matchedClassNames) {
        // --this-- should contain the edges of the design pattern !!
        // For a particular edge (fourtuple.get(startIndex) in the design pattern
        // try to find the corresponding edge(s) in system.

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
                    showSupplementaryEdgesOld(system, matchedClassNames);
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
        for (int j = 0; j < system.getFourTuples().size(); j++) {
            // For all edges in SE

            FourTuple seFt = system.getFourTuples().get(j);
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
                    for (k = j + 1; k < system.getFourTuples().size(); k++) {
                        FourTuple sekFt = system.getFourTuples().get(k);
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

                hulp = recursiveMatch(system, maxNotMatchable,
                        startIndex + 1, copyMatchedClassNames);

                found = found || hulp;

                dpFt.setMatched(false);
                seFt.setMatched(false);

                // undo multiple matched edges.
                for (Integer getal : extraMatched) {
                    system.getFourTuples().get(getal.intValue()).setMatched(false);
                }

                if (found && extraMatched.size() > 0) {
                    // In case of inheritance with multiple childs
                    // all matching  edges has been found.
                    // Therefore searching for more edges may be stopped.

                    j = system.getFourTuples().size();
                }
            }
        }

        if (!found) {

            // is the number of not matched edges acceptable?
            if (--maxNotMatchable >= 0) {
                return recursiveMatch(system, maxNotMatchable,
                        startIndex + 1, matchedClassNames);
            }

            return false;
        }

        return found; //  == true !!
    }


    void showSupplementaryEdgesOld(SystemUnderConsideration system, MatchedNames matchedClassNames) {
        // Show all edges in system which both classes are matched in this
        // design pattern but ther is no coressponding edge in the design
        // pattern.

        boolean found;

        found = false;

        for (FourTuple edge : system.getFourTuples()) {
            if (matchedClassNames.keyIsBounded(edge.getClassName1())
                    && matchedClassNames.keyIsBounded(edge.getClassName2())) {
                if (!edge.isMatched()) {
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


    private MatchedNames fillMatchedNames(SystemUnderConsideration system) {
        // No classname will be matched
        MatchedNames names = new MatchedNames();

        for (FourTuple ft : system.getFourTuples()) {
            names.add(ft.getClassName1());
            names.add(ft.getClassName2());
        }

        return names;
    }


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
}
