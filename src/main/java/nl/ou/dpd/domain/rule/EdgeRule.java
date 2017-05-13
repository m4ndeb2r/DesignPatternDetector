package nl.ou.dpd.domain.rule;

import nl.ou.dpd.domain.edge.Cardinality;
import nl.ou.dpd.domain.edge.Edge;
import nl.ou.dpd.domain.node.Attribute;
import nl.ou.dpd.domain.node.Node;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * An {@link EdgeRule} is a {@link Rule} that applies to {@link Edge}s specificly. It contains two internal
 * {@link NodeRule}s
 *
 * @author Peter Vansweevelt
 * @author Martin de Boer
 * @see NodeRule
 * @see AttributeRule
 */
public class EdgeRule extends Rule<Edge> {

    private static final Logger LOGGER = LogManager.getLogger(EdgeRule.class);

    /**
     * Creates a rule with the {@link Edge} mould that implements the features needed to apply to this rule.
     *
     * @param mould    the {@link Edge} containing the rule's features
     * @param topic    holds the feature to be evaluated (type, visibility, ...)
     * @param scope    the scope of the evaluation (object, relation, ...)
     * @param operation the evaluation operation (equals, exists, ...)
     */
    public EdgeRule(Edge mould, Scope scope, Topic topic, Operation operation) {
        super(mould, scope, topic, operation);
    }

    /**
     * Applies this {@link EdgeRule} on a given {@link Edge}.
     *
     * @param systemEdge the edge that has to be evaluated
     * @return {@code true} if {@code systemEdge} meets the conditions set in the {@link EdgeRule}, or {@code false}
     * otherwise.
     */
    public boolean process(Edge systemEdge) {
        switch (getScope()) {
            case RELATION:
                return processRelation(systemEdge);
            case ATTRIBUTE:
                return processAttribute(systemEdge);
            default:
                return error(String.format("Unexpected scope: '%s'.", getScope()));
        }
    }

    private boolean processRelation(Edge systemEdge) {
        switch (getTopic()) {
            case TYPE:
                return processRelationType(systemEdge);
            case CARDINALITY:
                return processRelationCardinality(systemEdge);
            case CARDINALITY_LEFT:
                return processRelationCardinalityLeft(systemEdge);
            case CARDINALITY_RIGHT:
                return processRelationCardinalityRight(systemEdge);
            default:
                return error(String.format("Unexpected topic '%s' while processing scope 'RELATION'." , getTopic()));
        }
    }

    private boolean processRelationCardinality(Edge systemEdge) {
        switch (getOperation()) {
            case EXISTS:
                return processCardinalityExists(systemEdge);
            case NOT_EXISTS:
                return !processCardinalityExists(systemEdge);
            case EQUALS:
                return processCardinalityEquals(systemEdge);
            case NOT_EQUALS:
                return !processCardinalityEquals(systemEdge);
            default:
                return error(String.format("Unexpected operation '%s' while processing topic 'CARDINALITY'." , getOperation()));
        }
    }

    private boolean processRelationCardinalityLeft(Edge systemEdge) {
        switch (getOperation()) {
            case EXISTS:
                return processCardinalityLeftExists(systemEdge);
            case NOT_EXISTS:
                return !processCardinalityLeftExists(systemEdge);
            case EQUALS:
                return processCardinalityLeftEquals(systemEdge);
            case NOT_EQUALS:
                return !processCardinalityLeftEquals(systemEdge);
            default:
                return error(String.format("Unexpected operation '%s' while processing topic 'CARDINALITY_LEFT'.", getOperation()));
        }
    }

    private boolean processRelationCardinalityRight(Edge systemEdge) {
        switch (getOperation()) {
            case EXISTS:
                return processCardinalityRightExists(systemEdge);
            case NOT_EXISTS:
                return !processCardinalityRightExists(systemEdge);
            case EQUALS:
                return processCardinalityRightEquals(systemEdge);
            case NOT_EQUALS:
                return !processCardinalityRightEquals(systemEdge);
            default:
                return error(String.format("Unexpected operation '%s' while processing topic 'CARDINALITY_RIGHT'.", getOperation()));
        }
    }

    private boolean processRelationType(Edge systemEdge) {
        switch (getOperation()) {
            case EQUALS:
                return systemEdge.getRelationType() == getMould().getRelationType();
            default:
                return error(String.format("Unexpected operation '%s' while processing topic 'TYPE'.", getOperation()));
        }
    }

    private boolean processCardinalityExists(Edge systemEdge) {
        final boolean leftOkay = systemEdge.getCardinalityLeft() != null;
        final boolean rightOkay = systemEdge.getCardinalityRight() != null;
        return leftOkay && rightOkay;
    }

    private boolean processCardinalityLeftExists(Edge systemEdge) {
        return systemEdge.getCardinalityLeft() != null;
    }

    private boolean processCardinalityRightExists(Edge systemEdge) {
        return systemEdge.getCardinalityRight() != null;
    }

    private boolean processCardinalityLeftEquals(Edge systemEdge) {
        if (!processCardinalityLeftExists(systemEdge)) {
            error("Left cardinality is not set.");
        }
        final Cardinality ruleLeft = getMould().getCardinalityLeft();
        final boolean leftOkay = ruleLeft.equals(systemEdge.getCardinalityLeft());
        return leftOkay;
    }

    private boolean processCardinalityRightEquals(Edge systemEdge) {
        if (!processCardinalityRightExists(systemEdge)) {
            error("Right cardinality is not set.");
        }
        final Cardinality ruleRight = getMould().getCardinalityRight();
        final boolean rightOkay = ruleRight.equals(systemEdge.getCardinalityRight());
        return rightOkay;
    }

    private boolean processCardinalityEquals(Edge systemEdge) {
        if (!processCardinalityExists(systemEdge)) {
            error("Either one or both cardinalities are not set.");
        }
        final Cardinality ruleLeft = getMould().getCardinalityLeft();
        final Cardinality ruleRight = getMould().getCardinalityRight();
        final boolean leftOkay = ruleLeft.equals(systemEdge.getCardinalityLeft());
        final boolean rightOkay = ruleRight.equals(systemEdge.getCardinalityRight());
        return leftOkay && rightOkay;
    }

    private boolean processAttribute(Edge systemEdge) {
        switch (getTopic()) {
            case TYPE:
                return processAttributeType(systemEdge);
            default:
                return error(String.format("Unexpected topic '%s' while processing scope 'ATTRIBUTE'.", getTopic()));
        }
    }

    private boolean processAttributeType(Edge systemEdge) {
        switch (getOperation()) {
            case EXISTS:
                return processAttributeTypeExists(systemEdge);
            default:
                return error(String.format("Unexpected operation '%s' while processing topic 'TYPE'.", getOperation()));
        }
    }

    /**
     * Determine whether an attribute exists in the left node of an edge, that is of the type of the right node.
     *
     * @param systemEdge the edge to examine
     * @return {@code true} if the left node of {@code systemEdge} contains an attribute of the type of the right node
     * of {@code systemEdge}, ofr {@code false} otherwise.
     */
    private boolean processAttributeTypeExists(Edge systemEdge) {
        final Node rightNode = systemEdge.getRightNode();
        final List<Attribute> systemAttributes = systemEdge.getLeftNode().getAttributes();
        return systemAttributes.stream().anyMatch(attr -> attr.getType().equals(rightNode));
    }

    private boolean error(String message) {
        LOGGER.error(message);
        throw new RuleException(message);
    }

}
