package nl.ou.dpd.domain.matching;

import nl.ou.dpd.domain.DesignPattern;
import nl.ou.dpd.domain.SystemUnderConsideration;
import nl.ou.dpd.domain.node.Node;
import nl.ou.dpd.domain.relation.Relation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jgrapht.GraphMapping;
import org.jgrapht.alg.isomorphism.VF2SubgraphIsomorphismInspector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Inspects a system under consideration for design patterns. Both objects (the system under control as well as the
 * design pattern) must be implemented as {@link org.jgrapht.DirectedGraph}s with {@link Node}s as vertices and
 * {@link Relation}s as edges.
 *
 * @author Martin de Boer
 */
public class PatternInspector extends VF2SubgraphIsomorphismInspector<Node, Relation> {

    private static final Logger LOGGER = LogManager.getLogger(PatternInspector.class);

    private SystemUnderConsideration system;
    private DesignPattern designPattern;

    /**
     * Construct a new {@link PatternInspector}.
     *
     * @param system        a system under consideration
     * @param designPattern a designPattern (possible subgraph of system)
     */
    public PatternInspector(SystemUnderConsideration system, DesignPattern designPattern) {
        super(system, designPattern, designPattern.getNodeComparator(), designPattern.getRelationComparator());
        LOGGER.info(String.format("Analysing system design '%s' for design pattern '%s'.",
                system.getName(),
                designPattern.getName()));
        this.system = system;
        this.designPattern = designPattern;
    }

    public MatchingResult getMatchingResult() {
        return new MatchingResult(getSolutions(), getFeedback());
    }

    /**
     * Initializes a new {@link Feedback} object (all relations and nodes are set to {@link FeedbackType#NOT_ANALYSED})
     * and merges all feedback messages from the comparators into the newly created feedback object.
     *
     * @return the resulting {@link Feedback} object containing all the feedback messages from the comparators, or
     * a message "not analysed" and {@link FeedbackType#NOT_ANALYSED} when no feedback has been generated by the
     * comparators.
     */
    private Feedback getFeedback() {
        return new Feedback(this.system)
                .addNotes(designPattern.getNotes())
                .merge(designPattern.getNodeComparator().getFeedback())
                .merge(designPattern.getRelationComparator().getFeedback());
    }

    /**
     * Gathers the solutions, one for every instance of a detected design pattern.
     *
     * @return a {@link List} of {@link Solution} objects, one for every design pattern instance that was detected.
     */
    private List<Solution> getSolutions() {
        LOGGER.info(String.format("Found %d solutions for pattern '%s' (before filtering out duplicates).",
                asList(getMappings()).size(),
                designPattern.getName()));
        return asList(getMappings())
                .stream()
                .map(graphMapping -> getSolutionFromGraphMapping(graphMapping))
                .collect(Collectors.toList());
    }

    private Solution getSolutionFromGraphMapping(GraphMapping<Node, Relation> mapping) {
        final Solution solution = new Solution(designPattern.getName(), designPattern.getFamily());
        for (Relation relation : system.edgeSet()) {
            final Node edgeSource = system.getEdgeSource(relation);
            final Node edgeTarget = system.getEdgeTarget(relation);
            addMatchingNodesToSolution(solution, edgeSource, mapping);
            addMatchingNodesToSolution(solution, edgeTarget, mapping);
            addMatchingRelationsToSolution(solution, relation, mapping);
        }
        return solution;
    }

    private void addMatchingRelationsToSolution(Solution solution, Relation systemRelation, GraphMapping<Node, Relation> mapping) {
        final Relation patternRelation = mapping.getEdgeCorrespondence(systemRelation, true);
        solution.addMatchingRelations(systemRelation, patternRelation);
    }

    private void addMatchingNodesToSolution(Solution solution, Node systemNode, GraphMapping<Node, Relation> mapping) {
        final Node patternNode = mapping.getVertexCorrespondence(systemNode, true);
        if (patternNode != null) {
            solution.addMatchingNodes(systemNode, patternNode);
        }
    }

    private List<GraphMapping<Node, Relation>> asList(Iterator<GraphMapping<Node, Relation>> iter) {
        List<GraphMapping<Node, Relation>> graphMappings = new ArrayList<>();
        iter.forEachRemaining(graphMapping -> graphMappings.add(graphMapping));
        return graphMappings;
    }

    /**
     * A {@link MatchingResult} object contains the solutions and the feedback for a specific design pattern.
     */
    public class MatchingResult implements FeedbackEnabled {
        private final List<Solution> solutions;
        private final Feedback feedback;

        private MatchingResult(List<Solution> solutions, Feedback feedback) {
            this.solutions = solutions;
            this.feedback = feedback;
        }

        public List<Solution> getSolutions() {
            return getSolutions(false);
        }

        public List<Solution> getSolutions(boolean allowSimilarSolutions) {
            if (allowSimilarSolutions) {
                return solutions;
            }
            // Filter out similar solutions
            List<Solution> result = new ArrayList<>();
            solutions.forEach(solution -> {
                if (!containsSimilarSolution(result, solution)) {
                    result.add(solution);
                }
            });
            LOGGER.info(String.format("Found %d solutions for pattern '%s' (after filtering out duplicates).",
                    result.size(),
                    designPattern.getName()));
            return result;
        }

        private boolean containsSimilarSolution(List<Solution> list, Solution solution) {
            return list.stream().anyMatch(sol -> sol.isSimilar(solution));
        }

        public Feedback getFeedback() {
            return feedback;
        }
    }
}
