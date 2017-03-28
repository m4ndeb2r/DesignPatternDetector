package nl.ou.dpd.domain;

/**
 * A {@link Rule} represents a part of a condition.
 * 
 * @author Peter Vansweevelt
 *
 */
public class Rule {
	
	private final Clazz rulenode;
	
	 /**
     * Creates a rule with the {@link Clazz} node, implementing the features needed to apply the rule.
     *
     * @param node the {@link Clazz} conatining the rule's features; 
     */	
	public Rule(Clazz node) {
		this.rulenode = node;
	}
	
	

}
