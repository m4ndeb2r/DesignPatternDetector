/**
 * 
 */
package nl.ou.dpd.data.template;

import nl.ou.dpd.domain.edge.Edge;
import nl.ou.dpd.domain.edge.EdgeType;
import nl.ou.dpd.domain.rule.EdgeRule;
import nl.ou.dpd.domain.rule.Rule;
import nl.ou.dpd.domain.rule.RuleException;

/**
 * @author Peter Vansweevelt
 *
 */
public class ApplyEdgeRule extends ApplyRule<Edge> {
	private Edge edge;

	/**
	 * @param rule
	 * @param value
	 */
	public ApplyEdgeRule(Rule<Edge> rule, String value) {
		super(rule, value);
		edge = rule.getMould();
	}
	
	   /**
     * Applies this {@link EdgeRule} on a given {@ink Edge}.
     *
     * @param systemEdge the edge that has to be evaluated
     * @return {@code true} if {@code systemEdge} meets the conditions set in the {@link EdgeRule}, or {@code false}
     * otherwise.
     */
    public void apply() {
    	switch (rule.getScope()) {
    		case RELATION:
    			applyRelation(edge);
    			break;
    		case ATTRIBUTE:
    			applyAttribute(edge);
    			break;
    		default:
    			error("Unexpected scope: " + rule.getScope() + ".");
    	}
    }

	private void applyRelation(Edge edge) {
        switch (rule.getTopic()) {
            case TYPE:
                applyRelationType(edge);
    			break;
            case CARDINALITY:
                applyRelationCardinality(edge);
    			break;
            case CARDINALITY_LEFT:
                applyRelationCardinalityLeft(edge);
    			break;
            case CARDINALITY_RIGHT:
                applyRelationCardinalityRight(edge);
    			break;
           default:
                error("Unexpected topic while applying RELATION: " + rule.getTopic() + ".");
        }
    }

    private void applyRelationCardinality(Edge edge) {
        switch (rule.getOperator()) {
            case EXISTS:
//                applyCardinalityExists(edge);
    			break;
            case NOT_EXISTS:
//                applyCardinalityExists(edge);
    			break;
            case EQUALS:
 //               applyCardinalityEquals(edge);
    			break;
            case NOT_EQUALS:
//                applyCardinalityEquals(edge);
    			break;
            default:
                error("Unexpected operator while applying CARDINALITY: " + rule.getOperator() + ".");
        }
    }

    private void applyRelationCardinalityLeft(Edge edge) {
        switch (rule.getOperator()) {
            case EXISTS:
//                applyCardinalityLeftExists(edge);
    			break;
            case NOT_EXISTS:
//                applyCardinalityLeftExists(edge);
    			break;
            case EQUALS:
                applyCardinalityLeftEquals(edge);
    			break;
            case NOT_EQUALS:
 //               applyCardinalityLeftEquals(edge);
    			break;
            default:
                error("Unexpected operator while applying CARDINALITY: " + rule.getOperator() + ".");
        }
    } 
    
    private void applyRelationCardinalityRight(Edge edge) {
        switch (rule.getOperator()) {
        case EXISTS:
//            applyCardinalityRightExists(edge);
			break;
        case NOT_EXISTS:
//            applyCardinalityRightExists(edge);
			break;
        case EQUALS:
            applyCardinalityRightEquals(edge);
			break;
        case NOT_EQUALS:
//            !applyCardinalityRightEquals(edge);
			break;
        default:
            error("Unexpected operator while applying CARDINALITY: " + rule.getOperator() + ".");
    }
}
    private void applyRelationType(Edge edge) {
        switch (rule.getOperator()) {
            case EQUALS:
                edge.setRelationType(findEdgeTypeByName(value));
    			break;
            default:
                error("Unexpected operator while applying TYPE: " + rule.getOperator() + ".");

        }
    }
/*
    private void applyCardinalityExists(Edge edge) {
        final void leftOkay = edge.getCardinalityLeft() != null;
        final void rightOkay = edge.getCardinalityRight() != null;
        return leftOkay && rightOkay;
    }
*/
/*    private void applyCardinalityLeftExists(Edge edge) {
        return edge.getCardinalityLeft() != null;
    }
*/    
/*    private void applyCardinalityRightExists(Edge edge) {
        return edge.getCardinalityRight() != null;
    }
*/
    private void applyCardinalityLeftEquals(Edge edge) {
    	int lower = findLowerCardinality(value);
    	int upper = findUpperCardinality(value);
        edge.setCardinalityLeft(lower, upper);
    }

    private void applyCardinalityRightEquals(Edge edge) {
    	int lower = findLowerCardinality(value);
    	int upper = findUpperCardinality(value);
        edge.setCardinalityRight(lower, upper);
    }    
/*    
    private void applyCardinalityEquals(Edge edge) {
        if (!applyCardinalityExists(edge)) {
            error("Either one or both cardinalities are not set.");
        }
        final Cardinality ruleLeft = rule.getMould().getCardinalityLeft();
        final Cardinality ruleRight = rule.getMould().getCardinalityRight();
        final void leftOkay = ruleLeft.equals(edge.getCardinalityLeft());
        final void rightOkay = ruleRight.equals(edge.getCardinalityRight());
        return leftOkay && rightOkay;
    }
*/
    private void error(String message) {
//        LOGGER.error(message);
        throw new RuleException(message);
    }
    
	private void applyAttribute(Edge edge) {
        switch (rule.getTopic()) {
	        case TYPE:
	            applyAttributeType(edge);
	        default:
	            error("Unexpected topic while applying ATTRIBUTE: " + rule.getTopic() + ".");
        }
	}

	private void applyAttributeType(Edge edge) {
        switch (rule.getOperator()) {
        case EXISTS:
 //           applyAttributeTypeExists(edge);
        default:
            error("Unexpected operator while applying ATTRIBUTE.TYPE: " + rule.getOperator() + ".");
        }
	}

	/** Find an attribute in the left node which has the type of the right node.
	 * Which means: an attribute of that type exists.
	 * @param edge
	 * @return
	 */
/*	private void applyAttributeTypeExists(Edge edge) {
		List<Attribute> systemAttributes = edge.getLeftNode().getAttributes();
		for(Attribute attribute : systemAttributes) {
			if (attribute.getType().getName().equals(edge.getRightNode().getName())) {
				return true;
			}
		}
		return false;
	}
*/
	private EdgeType findEdgeTypeByName(String edgeTypeName) {
		for (EdgeType edgeType : EdgeType.values()) {
		  if (edgeType.getName().contains(edgeTypeName)) {
			  return edgeType;
		  }
		}
		return null;
	}
	
	/**
	 * handles different possibilities to find a lower cardinality value.
	 * If format = "m..n": take m; if format = "m, n" : take m; if format = "m" take m.
	 * @param value
	 * @return
	 */
	private int findLowerCardinality(String value) {
		String[] values = value.split("\\.\\.");
		String lower = "";
		if (values.length == 1) {
			values = value.split(",");
			if (values.length == 1) {
				lower = value;
			}
			lower = values[0];
		} else {
			lower = values[0];
		}
		return Integer.parseInt(lower);		
	}
	
	/**
	 * handles different possibilities to find an upper cardinality value.
	 * If format = "m..n": take n; if format = "m, n" : take n; if format = "m" take m.
	 * If n = * return -1 (infinity)
	 * @param value
	 * @return
	 */
	private int findUpperCardinality(String value) {
		String[] values = value.split("\\.\\.");
		String upper = "";
		if (values.length == 1) {
			values = value.split(",");
			if (values.length == 1) {
				upper = value;
			}
			upper = values[values.length - 1];
		} else {
			upper = values[values.length - 1];
		}
		if (upper.equals("*")) {
			upper = "-1";
		}
		return Integer.parseInt(upper);		
	}

}
