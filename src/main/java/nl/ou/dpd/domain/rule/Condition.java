/**
 * 
 */
package nl.ou.dpd.domain.rule;

import java.util.ArrayList;
import java.util.List;

import nl.ou.dpd.domain.edge.Edge;
import nl.ou.dpd.domain.node.Node;

/**
 * This class represents a condition as given by an Design Pattern.
 * <p>
 * The class has an id and a decription of the condition. It can implement one or more rules.
 * The class also administers the combination of this condition with others  by AND and OR relationships.
 * A Condition class sets the purview of the condition: obligated, only feedback or ignored.
 * 
 * @author Peter Vansweevelt
 *
 */
public class Condition {
	
	private final String id;
	private final String description;
	private List<Rule> rules;
	private List<Condition> andConditions;
	private List<Condition> orConditions;
	private Purview purview;
	private Boolean value;
	private boolean handled;
	
	/**
	 * Constructor of the {@link Condition} class.
	 * @param id the id of the {@link Condition}
	 * @param description a short description of the {@link Condition}
	 */
	public Condition(String id, String description) {
		this.id = id;
		this.description = description;
		rules = new ArrayList<Rule>();
		andConditions = new ArrayList<Condition>();
		orConditions = new ArrayList<Condition>();
		purview = null;
		value = null;
		handled = false;
	}
	
	/**
	 * Get the id of the {@link Condition}.
	 * @return the id
	 */	
	public String getId() {
		return id;
	}
	
	/**
	 * Get the description of the {@link Condition}.
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Add a {@link Rule} to the {@link Condition}.
	 * @param a {@link Rule}
	 * @return the index of the newly added element or of the element if it was already in the list.
	 */
	public int addRule(Rule rule) {
		if (!rules.contains(rule)) {
			rules.add(rule);
		}
		return rules.indexOf(rule);
	}
	
	/**
	 * removes a {@link Rule} from the {@link Condition}.
	 * @param rule
	 * @return <code>True</code> if the {@link Rule} has been removed successfully. <code>False</code> if the {@link Rule} was not in the list.
	 */
	public boolean removeRule(Rule rule) {
		return rules.remove(rule);
	}
	
	/**
	 * Removes all the rules from the {@link Condition}.
	 */
	public void clearRules() {
		rules.clear();
	}
	
	/**
	 * Get a list of all the {@link Rule}s of this condition.
	 * @return
	 */
	public List<Rule> getRules() {
		return rules;
	}

	/**
	 * Add a {@link Condition} to the <code>AND</code> Conditions.
	 * @param a {@link Condition}
	 * @return the index of the newly added element or of the element if it was already in the list.
	 */
	public int addAndCondition(Condition condition) {
		if (!andConditions.contains(condition)) {
			andConditions.add(condition);
		}
		return andConditions.indexOf(condition);
	}
	
	/**
	 * removes a {@link Condition} from the <code>AND</code> Conditions.
	 * @param rule
	 * @return <code>True</code> if the {@link Condition} has been removed successfully. <code>False</code> if the {@link Condition} was not in the list.
	 */
	public boolean removeAndCondition(Condition condition) {
		return andConditions.remove(condition);
	}
	
	/**
	 * Removes all the rules from the <code>AND</code> Conditions.
	 */
	public void clearAndConditions() {
		andConditions.clear();
	}
	
	/**
	 * Get a list of all the <code>AND</code> Conditions of this condition.
	 * @return
	 */
	public List<Condition> getAndConditions() {
		return andConditions;
	}

	/**
	 * Add a {@link Condition} to the <code>OR</code> Conditions.
	 * @param a {@link Condition}
	 * @return the index of the newly added element or of the element if it was already in the list.
	 */
	public int addOrCondition(Condition condition) {
		if (!orConditions.contains(condition)) {
			orConditions.add(condition);
		}
		return orConditions.indexOf(condition);
	}
	
	/**
	 * removes a {@link Condition} from the <code>OR</code> Conditions.
	 * @param rule
	 * @return <code>True</code> if the {@link Condition} has been removed successfully. <code>False</code> if the {@link Condition} was not in the list.
	 */
	public boolean removeOrCondition(Condition condition) {
		return orConditions.remove(condition);
	}
	
	/**
	 * Removes all the rules from the <code>OR</code> Conditions.
	 */
	public void clearOrConditions() {
		orConditions.clear();
	}
	
	/**
	 * Get a list of all the <code>OR</code> Conditions of this condition.
	 * @return
	 */
	public List<Condition> getOrConditions() {
		return orConditions;
	}
	
	/**
	 * Set the purview.
	 * @param purview
	 */
	public void setPurview(Purview purview) {
		this.purview = purview;
	}
	
	/**
	 * Get the purview.
	 * @return the purview
	 */
	public Purview getPurview() {
		return purview;
	}
	
	/**
	 * Set value attribute.
	 * Visibility private: the value can only be set by processing the condition.
	 * @param value
	 */
	private void setValue(Boolean value) {
		this.value = value;
	}
	
	/**
	 * Get value attribute.
	 * @return <code>Null</code> if the condition has not been handled yet. The boolean value of the condition otherwise.
	 */
	public Boolean getValue() {		
		return value;
	}
	
	/**
	 * Set handled attribute.
	 * Visibility private: the attribuet handled can only be set by processing the condition.
	 * @param handled
	 */
	private void setHandled(boolean handled) {
		this.handled = handled;
	}
	
	/**
	 * Get handled attribute.
	 * @return <code>False</code> if the condition has not been handled yet. <code>True</code> otherwise.
	 */
	public boolean getHandled() {		
		return handled;
	}
	
	/**
	 * Reset the handled attribute to false. 
	 */
	public void clearHandled() {
		setValue(null);
		setHandled(false);
	}
	
	/**
	 * Process the {@link Condition}.
	 * @param edge an edgeof the system under consideration.
	 * @return <code>True</code> if all the rules have been met or if the purview is set to IGNORE. <code>False</code> otherwise. 
	 */
	public Boolean process(Edge edge) {
		//if condition has been handled, do not process
		if (handled == true) {
			return value;
		}		
		switch (purview) {
		case IGNORE: {
			setHandled(false);
			return value;
		}
		case FEEDBACK_ONLY: {
			setValue(processRules(edge));
			setHandled(true);
			return value;
		}
        default:		
			setValue(processRules(edge));
			setHandled(true);
			return value;
		}
	}

	/**
	 * Processes the rules of this condition.
	 * Inspects whether the rule applies to an Edge or a Node, and selects the appropriate Rule.
	 * Inspects whether the rule has to be applied to this Edge or Node by comparing their names. 
	 * @return the value of the handled attribute; <br>
	 * <ul><li><code>True</code> if all the rules return true.</li>
	 * <li><code>False</code> if at least one of the rules returns false.</li>
	 * <li><code>Null</code> if the condition has not been processed.</li></ul>
	 */
	private Boolean processRules(Edge edge) {
		for (Rule r : rules) {
			Boolean ruleHandled = null;
			//check classes
			if (r.getClass().getName().contains("EdgeRule")) {
				ruleHandled = processEdgeRule((EdgeRule) r, edge);
			} else if (r.getClass().getName().contains("NodeRule")) {
				Node node1 = edge.getNode1();
				ruleHandled = processNodeRule((NodeRule) r, node1);
				//handle node 2 only if node 1 didn't return a result.
				if (ruleHandled == null) {
					Node node2 = edge.getNode2();					
					ruleHandled = processNodeRule((NodeRule) r, node2);
				}
			} else {
				String message = "The Class " + r.getClass() + " has no implementation in any Rule.";
				throw new RuleException("Unexpected exception. " + message);
			}
			
			//rule could not be applied
			if (ruleHandled == null) {
				return null;
			}
			//all rules must be met			
			if (ruleHandled == false) {
				return false;
			}
			//rule has been met: process next rule
		}
		return true;
	}
	
	/**
	 * Processes an edgerule.
	 * @param r the rule
	 * @param edge the edge to be processed
	 */
	private Boolean processEdgeRule(EdgeRule r, Edge edge) {
		//Only process if the rule applies to this edge
		boolean checkNames = nameExists(r.getRuleEdge().getName()) && compareNames(r.getRuleEdge().getName(),edge.getName());
		if (checkNames) {
			return r.process(edge);			
		} else {
		// if names does not correspond: do nothing
			return null;
		}		
	}

	/**
	 * Processes a noderule.
	 * @param r the rule
	 * @param node the node to be processed
	 */
	private Boolean processNodeRule(NodeRule r, Node node) {
		//Only process if the rule applies to this edge
		boolean checkNames = nameExists(r.getRuleNode().getName()) && compareNames(r.getRuleNode().getName(),node.getName());
		if (checkNames) {
			return r.process(node);			
		} else {
		// if names does not correspond: do nothing
			return null;
		}		
	}
	
	/**
	 * Compare the name of the edges/nodes
	 * @param name1 name of the first edge/node
	 * @param name1 name of the second edge/node
	 * @return <code>True</code> if the name correspond (ignoring Case) <code>False</code> otherwise.
	 */
	private boolean compareNames(String name1, String name2) {
		return name1.equalsIgnoreCase(name2);
	}
	
	/**
	 * Check if he name exists
	 * @param name the name to examine
	 * @return <code>True</code> if the name exists. <code>False</code> otherwise.
	 */
	private boolean nameExists(String name) {
		return name != null && !name.equals("");
		
	}

}
