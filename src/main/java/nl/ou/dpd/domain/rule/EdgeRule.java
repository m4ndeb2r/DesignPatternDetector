package nl.ou.dpd.domain.rule;

import nl.ou.dpd.domain.edge.Cardinality;
import nl.ou.dpd.domain.edge.Edge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * An {@link EdgeRule} is a {@link Rule} that applies to {@link Edge}s specificly. It contains two internal
 * {@link NodeRule}s
 *
 * @author Peter Vansweevelt
 * @author Martin de Boer
 */
public class EdgeRule extends Rule<Edge> {

    private static final Logger LOGGER = LogManager.getLogger(EdgeRule.class);

    /**
     * Creates a rule with the {@link Edge} mould that implements the features needed to apply to this rule.
     *
     * @param mould     the {@link Edge} containing the rule's features
     * @param topic     holds the feature to be evaluated (type, visibility, ...)
     * @param scope     the scope of the evaluation (object, relation, ...)
     * @param operator  the evaluation operator (equals, exists, ...)
     */
    public EdgeRule(Edge mould, Topic topic, Scope scope, Operator operator) {
        super(mould, topic, scope, operator);
    }

    /**
     * Applies this {@link EdgeRule} on a given {@ink Edge}.
     *
     * @param systemEdge the edge that has to be evaluated
     * @return {@code true} if {@code systemEdge} meets the conditions set in the {@link EdgeRule}, or {@code false}
     * otherwise.
     */
    public boolean process(Edge systemEdge) {
        if (getScope() == Scope.RELATION) {
            return processRelation(systemEdge);
        }
        return error("Unexpected scope: " + getScope() + ".");
    }

    private boolean processRelation(Edge systemEdge) {
        switch (getTopic()) {
            case TYPE:
                return processRelationType(systemEdge);
            case CARDINALITY:
                return processRelationCardinality(systemEdge);
            default:
                return error("Unexpected topic while processing RELATION: " + getTopic() + ".");
        }
    }

    private boolean processRelationCardinality(Edge systemEdge) {
        switch (getOperator()) {
            case EXISTS:
                return processCardinalityExists(systemEdge);
            case NOT_EXISTS:
                return !processCardinalityExists(systemEdge);
            case EQUALS:
                return processCardinalityEquals(systemEdge);
            case NOT_EQUALS:
                return !processCardinalityEquals(systemEdge);
            default:
                return error("Unexpected operator while processing CARDINALITY: " + getOperator() + ".");
        }
    }

    private boolean processRelationType(Edge systemEdge) {
        switch (getOperator()) {
            case EQUALS:
                return systemEdge.getRelationType() == getMould().getRelationType();
            default:
                return error("Unexpected operator while processing TYPE: " + getOperator() + ".");

        }
    }

    private boolean processCardinalityExists(Edge systemEdge) {
        final boolean frontOkay = systemEdge.getCardinalityFront() != null;
        final boolean endOkay = systemEdge.getCardinalityEnd() != null;
        return frontOkay && endOkay;
    }

    private boolean processCardinalityEquals(Edge systemEdge) {
        if (!processCardinalityExists(systemEdge)) {
            error("Either one or both cardinalities are not set.");
        }
        final Cardinality ruleFront = getMould().getCardinalityFront();
        final Cardinality ruleEnd = getMould().getCardinalityEnd();
        final boolean frontOkay = ruleFront.equals(systemEdge.getCardinalityFront());
        final boolean endOkay = ruleEnd.equals(systemEdge.getCardinalityEnd());
        return frontOkay && endOkay;
    }

    private boolean error(String message) {
        LOGGER.error(message);
        throw new RuleException(message);
    }

}
