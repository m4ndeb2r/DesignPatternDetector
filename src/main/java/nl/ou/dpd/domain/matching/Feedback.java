package nl.ou.dpd.domain.matching;

import nl.ou.dpd.domain.SystemUnderConsideration;
import nl.ou.dpd.domain.node.Node;
import nl.ou.dpd.domain.relation.Relation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A container for feedback messages that are generated during the matching process.
 *
 * @author Martin de Boer
 */
public class Feedback {

    private static final String MSG_NOT_ANALYSED = "Not analysed.";

    private final Map<Node, Map<FeedbackType, List<String>>> nodeRelatedFeedbackMessages = new HashMap<>();
    private final Map<Relation, Map<FeedbackType, List<String>>> relationRelatedFeedbackMessages = new HashMap<>();

    public Feedback() {
    }

    public Feedback(SystemUnderConsideration systemUnderConsideration) {
        systemUnderConsideration.edgeSet().forEach(relation -> {
            addFeedbackMessage(relation, FeedbackType.NOT_ANALYSED, MSG_NOT_ANALYSED);
            addFeedbackMessage(systemUnderConsideration.getEdgeSource(relation), FeedbackType.NOT_ANALYSED, MSG_NOT_ANALYSED);
            addFeedbackMessage(systemUnderConsideration.getEdgeTarget(relation), FeedbackType.NOT_ANALYSED, MSG_NOT_ANALYSED);
        });
    }

    public Set<Node> getNodeSet() {
        return nodeRelatedFeedbackMessages.keySet();
    }

    public Set<Relation> getRelationSet() {
        return relationRelatedFeedbackMessages.keySet();
    }

    public List<String> getFeedbackMessages(Node node, FeedbackType type) {
        if (nodeRelatedFeedbackMessages.get(node) == null) {
            return new ArrayList<>();
        }
        final List<String> messages = nodeRelatedFeedbackMessages.get(node).get(type);
        if (messages == null) {
            return new ArrayList<>();
        }
        return messages;
    }

    public List<String> getFeedbackMessages(Relation relation, FeedbackType type) {
        if (relationRelatedFeedbackMessages.get(relation) == null) {
            return new ArrayList<>();
        }
        final List<String> messages = relationRelatedFeedbackMessages.get(relation).get(type);
        if (messages == null) {
            return new ArrayList<>();
        }
        return messages;
    }

    public Feedback addFeedbackMessage(Node node, FeedbackType type, String message) {
        nodeRelatedFeedbackMessages.computeIfAbsent(node, k -> new HashMap<>());
        nodeRelatedFeedbackMessages.get(node).remove(FeedbackType.NOT_ANALYSED);
        nodeRelatedFeedbackMessages.get(node).computeIfAbsent(type, k -> new ArrayList<>());
        return addUniqueMessage(nodeRelatedFeedbackMessages.get(node).get(type), message);
    }

    public Feedback addFeedbackMessage(Relation relation, FeedbackType type, String message) {
        relationRelatedFeedbackMessages.computeIfAbsent(relation, k -> new HashMap<>());
        relationRelatedFeedbackMessages.get(relation).remove(FeedbackType.NOT_ANALYSED);
        relationRelatedFeedbackMessages.get(relation).computeIfAbsent(type, k -> new ArrayList<>());
        return addUniqueMessage(relationRelatedFeedbackMessages.get(relation).get(type), message);
    }

    private Feedback addUniqueMessage(List<String> messages, String message) {
        if (messages.stream().filter(s -> s.equals(message)).count() == 0) {
            messages.add(message);
        }
        return this;
    }

    public Feedback merge(Feedback other) {
        if (other == null) {
            return this;
        }
        if (other.nodeRelatedFeedbackMessages != null) {
            other.nodeRelatedFeedbackMessages.keySet()
                    .forEach(node -> other.nodeRelatedFeedbackMessages.get(node)
                            .forEach((type, message) -> other.nodeRelatedFeedbackMessages.get(node).get(type)
                                    .forEach(msg -> addFeedbackMessage(node, type, msg))));
        }
        if (other.relationRelatedFeedbackMessages != null) {
            other.relationRelatedFeedbackMessages.keySet()
                    .forEach(relation -> other.relationRelatedFeedbackMessages.get(relation)
                            .forEach((type, message) -> other.relationRelatedFeedbackMessages.get(relation).get(type)
                                    .forEach(msg -> addFeedbackMessage(relation, type, msg))));
        }
        return this;
    }

}
