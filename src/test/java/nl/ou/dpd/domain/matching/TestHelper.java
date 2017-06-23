package nl.ou.dpd.domain.matching;

import nl.ou.dpd.domain.DesignPattern;
import nl.ou.dpd.domain.SystemUnderConsideration;
import nl.ou.dpd.domain.node.Node;
import nl.ou.dpd.domain.node.NodeType;
import nl.ou.dpd.domain.relation.Cardinality;
import nl.ou.dpd.domain.relation.RelationProperty;
import nl.ou.dpd.domain.relation.RelationType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A helper class for testing purposes.
 *
 * @author Martin de Boer
 */
public class TestHelper {

    /**
     * Temporary method to print feedback. TODO: should eventually be removed....
     *
     * @param designPattern
     * @param system
     * @param patternInspector
     * @param solutions
     */
    public static void printFeedback(DesignPattern designPattern, SystemUnderConsideration system, PatternInspector patternInspector, List<Solution> solutions) {
        solutions.forEach(solution -> {
            System.out.println("---------------------------------------");
            System.out.println(solution.getDesignPatternName());
            System.out.println("---------------------------------------");
            solution.getMatchingNodeNames().forEach(names -> System.out.println(names[0] + " matches " + names[1]));
        });

        System.out.println("\n---------------------------------------");
        System.out.println(designPattern.getName() + " feedback");
        System.out.println("---------------------------------------");
        final Feedback feedback = patternInspector.getFeedback();

        System.out.println("Node feedback:");
        final Set<Node> nodes = new HashSet<>();
        system.edgeSet().iterator().forEachRemaining(r -> {
            nodes.add(system.getEdgeSource(r));
            nodes.add(system.getEdgeTarget(r));
        });
        nodes.iterator().forEachRemaining(node -> {
            System.out.println("\n\tNode: " + node.getName());
            feedback.getFeedbackMessages(node, FeedbackType.INFO).forEach(s -> System.out.println("\t- " + s));
            feedback.getFeedbackMessages(node, FeedbackType.MATCH).forEach(s -> System.out.println("\t- " + s));
            feedback.getFeedbackMessages(node, FeedbackType.MISMATCH).forEach(s -> System.out.println("\t- " + s));
            feedback.getFeedbackMessages(node, FeedbackType.NOT_ANALYSED).forEach(s -> System.out.println("\t- " + s));
        });

        System.out.println("\nRelation feedback:");
        system.edgeSet().iterator().forEachRemaining(r -> {
            System.out.println("\n\tRelation: " + r.getName());
            feedback.getFeedbackMessages(r, FeedbackType.INFO).forEach(s -> System.out.println("\t- " + s));
            feedback.getFeedbackMessages(r, FeedbackType.MATCH).forEach(s -> System.out.println("\t- " + s));
            feedback.getFeedbackMessages(r, FeedbackType.MISMATCH).forEach(s -> System.out.println("\t- " + s));
            feedback.getFeedbackMessages(r, FeedbackType.NOT_ANALYSED).forEach(s -> System.out.println("\t- " + s));
        });
    }


}
