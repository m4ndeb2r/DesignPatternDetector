package nl.ou.dpd.domain.rule;

import nl.ou.dpd.domain.edge.Cardinality;
import nl.ou.dpd.domain.edge.Edge;

/**
 * A {@link EdgeRule} represents a part of a condition.
 *
 * @author Peter Vansweevelt
 * @author Martin de Boer
 */
public class EdgeRule implements Rule<Edge> {

    private final Edge ruleEdge;
    private final Topic topic;
    private final Target target;
    private final Operator operator;

    /**
     * Creates a rule with the {@link Edge} edge that implements the features needed to apply to this rule.
     *
     * @param edge     the {@link Edge} containing the rule's features
     * @param topic    holds the feature to be evaluated (type, visibility, ...)
     * @param target   the target of the evaluation (object, relation, ...)
     * @param operator the evaluation operator (equals, exists, ...)
     */
    public EdgeRule(Edge edge, Topic topic, Target target, Operator operator) {
        this.ruleEdge = edge;
        this.topic = topic;
        this.target = target;
        this.operator = operator;
    }
    
    /**
     * Get the ruleEdge of this {@link Rule}. 
     * @return the ruleEdge
     */
    public Edge getRuleEdge() {
    	return ruleEdge;
    }

    /**
     * Applies the rule on a given {@ink Edge}.
     *
     * @param the class under consideration
     * @return {@code true} if the node meets the conditions set in the {@link EdgeRule}, or {@code false} otherwise.
     */
    public boolean process(Edge systemEdge) {
        if (target == Target.RELATION) {
            return processRelationTarget(systemEdge);
        }
        throw new RuleException("Unexpected target: " + this.target + ".");
    }

    private boolean processRelationTarget(Edge systemEdge) {
        if (topic == Topic.TYPE) {
            return systemEdge.getRelationType() == ruleEdge.getRelationType();
        }
        if (topic == Topic.CARDINALITY) {
        	if (operator == Operator.EXISTS) {
        		return processCardinalityTopicExists(systemEdge);
        	}       	
        	if (operator == Operator.EQUALS) {
        		return processCardinalityTopicEquals(systemEdge);
        	}
        }
        throw new RuleException("Unexpected topic while processing RELATION target: " + this.topic + ".");
    }

    private boolean processCardinalityTopicExists(Edge systemEdge) {
        final boolean frontOkay = systemEdge.getCardinalityFront() != null;
        final boolean endOkay = systemEdge.getCardinalityEnd() != null;
        return frontOkay && endOkay;
    }

    private boolean processCardinalityTopicEquals(Edge systemEdge) {
    	if (!processCardinalityTopicExists(systemEdge)) {
            throw new RuleException("Either one or both cardinalities are not set.");
    	}
        final Cardinality ruleFront = ruleEdge.getCardinalityFront();
        final Cardinality ruleEnd = ruleEdge.getCardinalityEnd();
        final boolean frontOkay = ruleFront.equals(systemEdge.getCardinalityFront());
        final boolean endOkay = ruleEnd.equals(systemEdge.getCardinalityEnd());
        return frontOkay && endOkay;
    }
}
