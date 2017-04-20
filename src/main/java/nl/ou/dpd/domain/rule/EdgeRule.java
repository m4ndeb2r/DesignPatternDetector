package nl.ou.dpd.domain.rule;

import nl.ou.dpd.domain.edge.Cardinality;
import nl.ou.dpd.domain.edge.Edge;
import nl.ou.dpd.domain.node.Attribute;

import java.util.List;

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
     * @param mould    the {@link Edge} containing the rule's features
     * @param topic    holds the feature to be evaluated (type, visibility, ...)
     * @param scope    the scope of the evaluation (object, relation, ...)
     * @param operator the evaluation operator (equals, exists, ...)
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
    	switch (getScope()) {
    		case RELATION:
    			return processRelation(systemEdge);
    		case ATTRIBUTE:
    			return processAttribute(systemEdge);
    		default:
    			return error("Unexpected scope: " + getScope() + ".");
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

    private boolean processRelationCardinalityLeft(Edge systemEdge) {
        switch (getOperator()) {
            case EXISTS:
                return processCardinalityLeftExists(systemEdge);
            case NOT_EXISTS:
                return !processCardinalityLeftExists(systemEdge);
            case EQUALS:
                return processCardinalityLeftEquals(systemEdge);
            case NOT_EQUALS:
                return !processCardinalityLeftEquals(systemEdge);
            default:
                return error("Unexpected operator while processing CARDINALITY: " + getOperator() + ".");
        }
    } 
    
    private boolean processRelationCardinalityRight(Edge systemEdge) {
        switch (getOperator()) {
        case EXISTS:
            return processCardinalityRightExists(systemEdge);
        case NOT_EXISTS:
            return !processCardinalityRightExists(systemEdge);
        case EQUALS:
            return processCardinalityRightEquals(systemEdge);
        case NOT_EQUALS:
            return !processCardinalityRightEquals(systemEdge);
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
    private boolean error(String message) {
        LOGGER.error(message);
        throw new RuleException(message);
    }
    
	private boolean processAttribute(Edge systemEdge) {
        switch (getTopic()) {
	        case TYPE:
	            return processAttributeType(systemEdge);
	        default:
	            return error("Unexpected topic while processing ATTRIBUTE: " + getTopic() + ".");
        }
	}

	private boolean processAttributeType(Edge systemEdge) {
        switch (getOperator()) {
        case EXISTS:
            return processAttributeTypeExists(systemEdge);
        default:
            return error("Unexpected operator while processing ATTRIBUTE.TYPE: " + getOperator() + ".");
        }
	}

	/** Find an attribute in the left node which has the type of the right node.
	 * Which means: an attribute of that type exists.
	 * @param systemEdge
	 * @return
	 */
	private boolean processAttributeTypeExists(Edge systemEdge) {
		List<Attribute> systemAttributes = systemEdge.getLeftNode().getAttributes();
		for(Attribute attribute : systemAttributes) {
			if (attribute.getType().getName().equals(systemEdge.getRightNode().getName())) {
				return true;
			}
		}
		return false;
	}
}
