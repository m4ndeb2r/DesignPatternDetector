package nl.ou.dpd.domain;

import java.util.Map;

/**
 * A {@link Rule} represents a part of a condition.
 * 
 * @author Peter Vansweevelt
 *
 */
public class Rule {
	
	private final Clazz rulenode;
	private final Edge ruleedge;
	private ClazzConstant topic;
//	private ClazzConstant rulenodetype;
	private ClazzConstant operator;
	private ClazzConstant target;
	
	 /**
     * Creates a rule with the {@link Clazz} node that implements the features needed to apply the rule.
     *
     * @param node the {@link Clazz} containing the rule's features; 
     */	
	public Rule(Clazz node) {
		this.rulenode = node;
		this.ruleedge = null;
		setTopic(ClazzConstant.OP_NOTSET);
		setOperator(ClazzConstant.OP_NOTSET);
		setTarget(ClazzConstant.TARGET_NOTSET); //holds the feature to be evaluated
		//		rulenodetype = node.getType();
	}

	public Rule(Edge edge) {
		this.ruleedge = edge;
		this.rulenode = null;
		setTopic(ClazzConstant.OP_NOTSET);
		setOperator(ClazzConstant.OP_NOTSET);
		setTarget(ClazzConstant.TARGET_NOTSET); //holds the feature to be evaluated
		//		rulenodetype = node.getType();
	}

	/**
	 * Sets the topic of the {@link Rule}.
	 * @param topic the target of the rule. 
	 */
	private void setTopic(ClazzConstant topic) {
		this.topic = topic;
	}
	
	/**
	 * Sets the operator of the {@link Rule}.
	 * @param operator the desired operation of the rule. 
	 */
	private void setOperator(ClazzConstant operator) {
		this.operator = operator;
	}
	
	/**
	 * Sets the target of the {@link Rule}.
	 * @param target of the rule. 
	 */
	private void setTarget(ClazzConstant target) {
		this.target = target;
	}
	
	/**
	 * Applies the rule on a given {@ink Clazz}.
	 * @param the class under consideration
	 * @return <code>True</code> if the node meets the conditions set in the {@link Rule}. <code> False</code> otherwise.
	 * >>>or return: feedback?
	 */
	public boolean applyRule(Clazz systemnode) {
		boolean ruleFulfilled = false;
		/*TODO*****************
		 * The system class must be bound with a dp-class as it does in MatchedClasses. Now the classtype is still compared by using the name.
		 * Maybe a decoratorpattern on the system class can be used to dynamically add the associated dp class to it?
		 */
		//we always inspect a node of a given class-type
		if (systemnode.getName().equalsIgnoreCase(rulenode.getName()) ) {
			if (topic == ClazzConstant.TOPIC_OBJECT) {
				if (target == ClazzConstant.TARGET_TYPE) {
					return systemnode.getObjecttype() == rulenode.getObjecttype();
				}
				if (target == ClazzConstant.TARGET_VISIBILITY) {
					return systemnode.getVisibility() == rulenode.getVisibility();
				}
				if (target == ClazzConstant.TARGET_MODIFIER) {
					for (Map.Entry<ClazzConstant, Boolean> me : rulenode.getModifiers().entrySet()) {
						if (me.getValue() != null) {					//the value is set
							if (systemnode.getModifierStatus(me.getKey()) != me.getValue()) {
								return false;
							} else {
								ruleFulfilled = true;
							}
						}
					}
				}
			}
		}
		return ruleFulfilled;		
	}
	
	/**
	 * Applies the rule on a given {@ink Edge}.
	 * @param the class under consideration
	 * @return <code>True</code> if the node meets the conditions set in the {@link Rule}. <code> False</code> otherwise.
	 */
	public boolean applyRule(Edge systemedge) {
		boolean ruleFulfilled = false;
/* this condition supposes an name-attribute in the edge-class as well as a cardinality	*/
		if (systemedge.getName().equalsIgnoreCase(ruleedge.getName()) ) {
			if (topic == ClazzConstant.TOPIC_RELATION) {
				if (target == ClazzConstant.TARGET_TYPE) {
					return systemedge.getRelationType() == ruleedge.getRelationType();
				}
			}
			if (topic == ClazzConstant.TOPIC_OBJECT) {
				if (target == ClazzConstant.TARGET_CARDINALITY) {
				}
					boolean front = systemedge.getCardinality_front() == ruleedge.getCardinality_front();
					boolean end = systemedge.getCardinality_end() == ruleedge.getCardinality_end();
					return front && end;
				}
		}
		return ruleFulfilled;		
	}

}
