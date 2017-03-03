package nl.ou.dpd.domain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

/**
 * A {@link Matcher} matches a {@link DesignPattern} and a {@link SystemUnderConsideration}, and tries to detect
 * a match. If found, we have detected the occurrence of a design pattern in the "system under consideration".
 *
 * @author Martin de Boer
 */
public class Matcher {

    private static final Logger LOGGER = LogManager.getLogger(Matcher.class);

    private Solutions solutions;

    /**
     * Matches the specified {@link DesignPattern} and {@link SystemUnderConsideration}.
     *
     * @param pattern         the pattern to detect
     * @param system          the system to search for the {@code pattern}
     * @param maxNotMatchable the maximum number of not matchable edges allowed.
     * @return {@code true} if a design pattern was detected, or {@code false} otherwise.
     */
    public boolean match(DesignPattern pattern, SystemUnderConsideration system, int maxNotMatchable) {
        // Order the design pattern's edges
        pattern.order();

        // Put every classname occuring in a 4-tuple in system in MatchedClasses with value = EMPTY.
        MatchedClasses matchedClasses = fillMatchedClasses(system);

        // Initiliase the solutions
        solutions = new Solutions();

        // Do the matching recursively
        return recursiveMatch(pattern, system, maxNotMatchable, 0, matchedClasses);
    }

    private boolean recursiveMatch(
            DesignPattern pattern,
            SystemUnderConsideration system,
            int maxNotMatchable,
            int startIndex,
            MatchedClasses matchedClasses) {

        // --this-- should contain the edges of the design pattern !!
        // For a particular edge (fourtuple.get(startIndex) in the design pattern
        // try to find the corresponding edge(s) in system under consideration.

        //int nNotMatchable;
        boolean found;

        if (startIndex >= pattern.getFourTuples().size()) {
            if (maxNotMatchable >= 0) {
                // This is an acceptable solution.
                if (solutions.isUniq(matchedClasses.getBoundedSortedKeySet())) {
                    solutions.add(new Solution(matchedClasses.getBoundedSortedKeySet()));

                    matchedClasses.show(pattern.getName());

                    // Does the detected design pattern have more edges than the design pattern?
                    // If so, show those edges.
                    system.showSupplementaryEdges(matchedClasses);
                    System.out.println();
                }

                return true;
            }

            LOGGER.warn("Unexpected situation in DesignPattern#recursiveMatch(). " +
                    "Value of maxNotMatchable = " + maxNotMatchable);

            // Should not occur. The search should be stopped before.

            return false;
        }

        DesignPatternEdge dpEdge = pattern.getFourTuples().get(startIndex); // improves readability

        found = false; // makes compiler happy;;

        // For this (startIndex) edge in DP, find matching edges in SE
        for (int j = 0; j < system.getFourTuples().size(); j++) {
            // For all edges in SE

            SystemUnderConsiderationEdge sysEdge = system.getFourTuples().get(j);
            // improves readablity

            MatchedClasses copyMatchedClasses = new MatchedClasses(matchedClasses);
            ArrayList<Integer> extraMatched = new ArrayList<Integer>();

            if (!sysEdge.isLocked() && sysEdge.isMatch(dpEdge, matchedClasses)) {

                // Make match in matched classes
                dpEdge.makeMatch(sysEdge, copyMatchedClasses);

                // Lock edges to prevent them from being matched twice
                dpEdge.lock();
                sysEdge.lock();

                if (dpEdge.getTypeRelation() == EdgeType.INHERITANCE_MULTI) {
                    int k;

                    // There may be more edges of se that contain an inheritance to the same parent and
                    // have unmatched children
                    for (k = j + 1; k < system.getFourTuples().size(); k++) {
                        SystemUnderConsiderationEdge skEdge = system.getFourTuples().get(k);
                        // for readablity;
                        if (!skEdge.isLocked()
                                && skEdge.getClass2().equals(sysEdge.getClass2())
                                && skEdge.isMatch(dpEdge, matchedClasses)) {
                            dpEdge.makeMatch(skEdge, copyMatchedClasses);
                            extraMatched.add(new Integer(k));
                            skEdge.lock();
                        }
                    }
                }

                // Recursive matching
                boolean foundRecursively = recursiveMatch(pattern, system, maxNotMatchable,
                        startIndex + 1, copyMatchedClasses);
                found = found || foundRecursively;

                // Reset match setting in dp en sys and multiple matched edges
                dpEdge.unlock();
                sysEdge.unlock();
                for (Integer getal : extraMatched) {
                    system.getFourTuples().get(getal.intValue()).unlock();
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
                return recursiveMatch(pattern, system, maxNotMatchable,
                        startIndex + 1, matchedClasses);
            }

            return false;
        }

        return found; //  == true !!
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
