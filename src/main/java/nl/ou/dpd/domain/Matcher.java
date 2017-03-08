package nl.ou.dpd.domain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

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
     * Matches the specified {@link DesignPattern} and {@link SystemUnderConsideration}. This method is called once for
     * the whole detection process.
     *
     * @param pattern         the pattern to detect
     * @param system          the system to search for the {@code pattern}
     * @param maxNotMatchable the maximum number of not matchable edges allowed.
     * @return a {@link Solutions} instance containing feedback information.
     */
    public Solutions match(DesignPattern pattern, SystemUnderConsideration system, int maxNotMatchable) {
        // Order the design pattern's edges
        pattern.order();

        // Put every classname occuring in a 4-tuple in system in MatchedClasses with value = EMPTY.
        MatchedClasses matchedClasses = prepareMatchedClasses(system);

        // Initialise the solutions. These will be populated during the recursive match.
        solutions = new Solutions();

        // Do the matching recursively. Initially regard all pattern edges as missing.
        // We will remove them from the missing edges list as soon as they are matched.
        final HashSet<Edge> missingEdges = new HashSet<>();
        missingEdges.addAll(pattern.getEdges());
        recursiveMatch(pattern, system, maxNotMatchable, 0, matchedClasses, missingEdges);

        // Return feedback
        return solutions;
    }

    /**
     * TODO: simplify this method. It is to long and incomprehensive.
     *
     * @param pattern
     * @param system
     * @param maxNotMatchable
     * @param startIndex
     * @param matchedClasses
     * @return
     */
    private boolean recursiveMatch(
            DesignPattern pattern,
            SystemUnderConsideration system,
            int maxNotMatchable,
            int startIndex,
            MatchedClasses matchedClasses,
            Set<Edge> missingEdges) {

        if (startIndex >= pattern.getEdges().size()) {
            // The detecting process is completed

            if (maxNotMatchable >= 0) {
                // This is an acceptable solution. Let's gather the information and keep it.
                final Solution solution = createSolution(pattern, system, matchedClasses, missingEdges);
                if (!solution.isEmpty() && solutions.isUniq(solution)) {
                    solutions.add(solution);
                }
                return true;
            }

            // Should not occur. The search should be stopped before.
            LOGGER.warn("Unexpected situation in DesignPattern#recursiveMatch(). " +
                    "Value of maxNotMatchable = " + maxNotMatchable);
            return false;
        }

        final Edge patternEdge = pattern.getEdges().get(startIndex);
        boolean found = false;

        // For this (startIndex) edge in DP, find matching edges in SE
        for (int j = 0; j < system.getEdges().size(); j++) {

            final Edge systemEdge = system.getEdges().get(j);
            final MatchedClasses copyMatchedClasses = new MatchedClasses(matchedClasses);
            final List<Integer> extraMatched = new ArrayList<>();

            if (!systemEdge.isLocked() && matchedClasses.canMatch(systemEdge, patternEdge)) {

                // Make match in copyMatchedClasses, and lock the matched edges to prevent them from being matched twice
                copyMatchedClasses.makeMatch(systemEdge, patternEdge);
                missingEdges.remove(patternEdge);
                lockEdges(patternEdge, systemEdge);

                if (patternEdge.getTypeRelation() == EdgeType.INHERITANCE_MULTI) {
                    // There may be more edges of se that contain an inheritance to the same parent and
                    // have unmatched children
                    for (int k = j + 1; k < system.getEdges().size(); k++) {
                        final Edge skEdge = system.getEdges().get(k);
                        if (!skEdge.isLocked()
                                && skEdge.getClass2().equals(systemEdge.getClass2())
                                && matchedClasses.canMatch(skEdge, patternEdge)) {
                            copyMatchedClasses.makeMatch(skEdge, patternEdge);
                            missingEdges.remove(patternEdge);
                            extraMatched.add(new Integer(k));
                            skEdge.lock();
                        }
                    }
                }

                // Recursive matching
                boolean foundRecursively = recursiveMatch(
                        pattern,
                        system,
                        maxNotMatchable,
                        startIndex + 1,
                        copyMatchedClasses, missingEdges);
                found = found || foundRecursively;

                // Unlock all locked edges, including the multiple inherited ones
                unlockEdges(patternEdge, systemEdge);
                for (Integer getal : extraMatched) {
                    system.getEdges().get(getal.intValue()).unlock();
                }

                if (found && extraMatched.size() > 0) {
                    // In case of inheritance with multiple children, all matching edges has been found.
                    // Therefore searching for more edges may be stopped.
                    j = system.getEdges().size();
                }
            }
        }

        if (!found) {

            // Is the number of not matched edges acceptable? Can we proceed recursively?
            if (maxNotMatchable > 0) {

                return recursiveMatch(
                        pattern,
                        system,
                        maxNotMatchable - 1,
                        startIndex + 1,
                        matchedClasses, missingEdges);
            }
            return false;
        }

        return found; // == true !!
    }


    /**
     * Create a {@link Solution} instance containing (feedback) information about a detected design pattern.
     *
     * @param pattern        the detected design pattern
     * @param system         the system under consideration that was analysed
     * @param matchedClasses matched classes
     * @return feedback information about the detected design pattern
     */
    private Solution createSolution(
            DesignPattern pattern,
            SystemUnderConsideration system,
            MatchedClasses matchedClasses,
            Set<Edge> missingEdges) {

        // Pattern name
        final String dpName = pattern.getName();

        // Classes that have been matched
        final SortedSet<Clazz> boundedKeys = matchedClasses.getBoundSystemClassesSorted();
        final MatchedClasses involvedClasses = matchedClasses.filter(boundedKeys);

        // Superfluous classes
        final Set<Edge> superfluousEdges = new HashSet<>();
        for (Edge systemEdge : system.getEdges()) {
            if (matchedClasses.isSystemClassBound(systemEdge.getClass1())
                    && matchedClasses.isSystemClassBound(systemEdge.getClass2())
                    && !systemEdge.isLocked()) {
                superfluousEdges.add(systemEdge);
            }
        }
        return new Solution(dpName, involvedClasses, superfluousEdges, missingEdges);
    }

    /**
     * Prepares a {@link MatchedClasses} for the matching process, based on the "system under consideration".
     *
     * @param system the "system under consideration"
     * @return the prepared {@link MatchedClasses} instance.
     */
    private MatchedClasses prepareMatchedClasses(SystemUnderConsideration system) {
        MatchedClasses matchedClasses = new MatchedClasses();
        system.getEdges().forEach(edge -> matchedClasses.prepareMatch(edge));
        return matchedClasses;
    }

    /**
     * Locks a number of {@link Edge}s.
     *
     * @param edges the {@link Edge}s to lock.
     */
    private void lockEdges(Edge... edges) {
        Arrays.asList(edges).forEach(edge -> edge.lock());
    }

    /**
     * Unlocks a number of {@link Edge}s.
     *
     * @param edges the {@link Edge}s to unlock.
     */
    private void unlockEdges(Edge... edges) {
        Arrays.asList(edges).forEach(edge -> edge.unlock());
    }
}
