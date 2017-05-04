package nl.ou.dpd.parsing.template;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nl.ou.dpd.domain.edge.Edge;
import nl.ou.dpd.domain.edge.EdgeType;
import nl.ou.dpd.domain.rule.EdgeRule;
import nl.ou.dpd.domain.rule.Rule;
import nl.ou.dpd.domain.rule.RuleException;

/**
 * Class that applies an {@link EdgeRule} using a specified value.
 * Results in the Edge, given in the Rule, implementing the Rule in the Edge itself.
 *  
 * @author Peter Vansweevelt
 *
 */
public class ApplyEdgeRule extends ApplyRule<Edge> {

    private static final Logger LOGGER = LogManager.getLogger(ApplyEdgeRule.class);
    
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
     * Results in the Edge, given in the Rule, implementing the Rule in the Edge itself.
     */
    public void apply() {
    	switch (rule.getScope()) {
    		case RELATION:
    			applyRelation();
    			break;
    		default:
    			error("Unexpected scope while applying: " + rule.getScope() + ".");
    	}
    }

	private void applyRelation() {
        switch (rule.getTopic()) {
            case TYPE:
                applyRelationType();
    			break;
            case CARDINALITY_LEFT:
                applyRelationCardinalityLeft();
    			break;
            case CARDINALITY_RIGHT:
                applyRelationCardinalityRight();
    			break;
           default:
                error("Unexpected topic while applying RELATION: " + rule.getTopic() + ".");
        }
    }

    private void applyRelationCardinalityLeft() {
        switch (rule.getOperator()) {
            case EQUALS:
                applyCardinalityLeftEquals();
    			break;
            default:
                error("Unexpected operator while applying CARDINALITY_LEFT: " + rule.getOperator() + ".");
        }
    } 
    
    private void applyRelationCardinalityRight() {
        switch (rule.getOperator()) {
        case EQUALS:
            applyCardinalityRightEquals();
			break;
        default:
            error("Unexpected operator while applying CARDINALITY_RIGHT: " + rule.getOperator() + ".");
    }
}
    private void applyRelationType() {
        switch (rule.getOperator()) {
            case EQUALS:
                edge.setRelationType(findEdgeTypeByName(value));
    			break;
            default:
                error("Unexpected operator while applying TYPE: " + rule.getOperator() + ".");

        }
    }

    private void applyCardinalityLeftEquals() {
    	int lower = findLowerCardinality(value);
    	int upper = findUpperCardinality(value);
        edge.setCardinalityLeft(lower, upper);
    }

    private void applyCardinalityRightEquals() {
    	int lower = findLowerCardinality(value);
    	int upper = findUpperCardinality(value);
        edge.setCardinalityRight(lower, upper);
    }    

	private EdgeType findEdgeTypeByName(String edgeTypeName) {
		for (EdgeType edgeType : EdgeType.values()) {
		  if (edgeType.getName().contains(edgeTypeName.toUpperCase())) {
			  return edgeType;
		  }
		}
		return null;
	}
	
	/**
	 * Handles different possibilities to find a lower cardinality value out of a String.
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
	
    private void error(String message) {
      LOGGER.error(message);
      throw new RuleException(message);
  }
  


}
