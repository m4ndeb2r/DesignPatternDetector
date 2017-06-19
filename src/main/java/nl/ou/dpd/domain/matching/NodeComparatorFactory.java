package nl.ou.dpd.domain.matching;


import nl.ou.dpd.domain.node.Node;
import nl.ou.dpd.domain.node.NodeType;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * A factory for {@link FeedbackEnabledComparator}s used for matching {@link Node}s of system designs with
 * {@link Node}s of design patterns.
 *
 * @author Martin de Boer
 */
public class NodeComparatorFactory {

    /**
     * Creates a {@link CompoundNodeComparator} containing all the available sub comparators for {@link Node}s.
     *
     * @return the created {@link CompoundNodeComparator}.
     */
    public static CompoundComparator<Node> createCompoundNodeComparator() {
        final CompoundNodeComparator compoundNodeComparator = new CompoundNodeComparator();
        compoundNodeComparator.addComparator(createNodeTypeComparator());
        return compoundNodeComparator;
    }

    /**
     * Creates a {@link CompoundNodeComparator} containing the specified {@code subComparators}.
     *
     * @param subComparators the sub comparators to be contained in the created {@link CompoundNodeComparator}.
     * @return the created {@link CompoundNodeComparator}.
     */
    public static CompoundComparator<Node> createCompoundNodeComparator(FeedbackEnabledComparator<Node>... subComparators) {
        final CompoundNodeComparator compoundNodeComparator = new CompoundNodeComparator();
        for (FeedbackEnabledComparator<Node> subComparator : subComparators) {
            compoundNodeComparator.addComparator(subComparator);
        }
        return compoundNodeComparator;
    }

    public static FeedbackEnabledComparator<Node> createNodeTypeComparator() {
        return new NodeTypeComparator();
    }

    private static class CompoundNodeComparator extends CompoundComparator<Node> {
        private final Feedback feedback = new Feedback();

        @Override
        public int compare(Node systemNode, Node patternNode) {
            final int result = super.compare(systemNode, patternNode);
            if (result == 0) {
                final String feedbackMsg = String.format("Matched with '%s'.", patternNode.getName());
                feedback.addFeedbackMessage(systemNode, FeedbackType.MATCH, feedbackMsg);
            } else {
                final String feedbackMsg = String.format("Match failed with '%s'.", patternNode.getName());
                feedback.addFeedbackMessage(systemNode, FeedbackType.MISMATCH, feedbackMsg);
            }
            return result;
        }

        @Override
        public Feedback getFeedback() {
            return super.getFeedback().merge(feedback);
        }
    }

    private static class NodeTypeComparator implements FeedbackEnabledComparator<Node> {
        private final Feedback feedback = new Feedback();

        @Override
        public Feedback getFeedback() {
            return feedback;
        }

        @Override
        public int compare(Node systemNode, Node patternNode) {
            final Set<NodeType> sysTypes = systemNode.getTypes();
            final Set<NodeType> dpTypes = patternNode.getTypes();
            final Set<NodeType> sysDisjunction = getLeftDisjunction(sysTypes, dpTypes);
            final Set<NodeType> dpDisjunction = getLeftDisjunction(dpTypes, sysTypes);

            feedback.addFeedbackMessage(systemNode, FeedbackType.INFO, "Node type(s) analysed.");

            final int result = sysDisjunction.size() + dpDisjunction.size();
            if (result != 0) {
                createFeedback(systemNode, patternNode, sysDisjunction, dpDisjunction);
            }
            return result;
        }

        private void createFeedback(Node systemNode, Node patternNode, Set<NodeType> sysDisjunction, Set<NodeType> dpDisjunction) {
            sysDisjunction.forEach(nodeType -> {
                final String feedbackMsg = String.format(
                        "Mismatch with '%s': unexpected node type '%s' in '%s'.",
                        patternNode.getName(),
                        nodeType,
                        systemNode.getName());
                feedback.addFeedbackMessage(systemNode, FeedbackType.MISMATCH, feedbackMsg);
            });

            dpDisjunction.forEach(nodeType -> {
                final String feedbackMsg = String.format(
                        "Mismatch with '%s': missing node type '%s' in '%s'.",
                        patternNode.getName(),
                        nodeType,
                        systemNode.getName());
                feedback.addFeedbackMessage(systemNode, FeedbackType.MISMATCH, feedbackMsg);
            });
        }

        private Set<NodeType> getLeftDisjunction(Set<NodeType> set1, Set<NodeType> set2) {
            return set1.stream()
                    .filter(type -> !set2.contains(type))
                    .collect(Collectors.toSet());
        }


    }
}
