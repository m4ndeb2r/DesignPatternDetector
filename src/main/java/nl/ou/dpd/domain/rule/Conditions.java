/**
 * 
 */
package nl.ou.dpd.domain.rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nl.ou.dpd.domain.edge.Edge;

/**
 * This class manages all the {@link conditions} of a {@link DesignPattern}, including the <code>and</code> and <code>or</code> relationships.
 * 
 * @author Peter Vansweevelt
 *
 */
public class Conditions {
	
    private List<Condition> conditions;

    /**
     * Constructor of the class {@link Conditions}.
     */
    public Conditions() {
        this.conditions = new ArrayList();
    }
    
	/**
	 * Add a {@link Condition} to the {@link Conditions}.
	 * @param a {@link Condition}
	 * @return the index of the newly added element or of the element if it was already in the list.
	 */
	public int addCondition(Condition condition) {
		if (!conditions.contains(condition)) {
			conditions.add(condition);
		}
		return conditions.indexOf(condition);
	}
	
	/**
	 * removes a {@link Condition} from the {@link Conditions}.
	 * @param condition
	 * @return <code>True</code> if the {@link Condition} has been removed successfully. <code>False</code> if the {@link Condition} was not in the list.
	 */
	public boolean removeCondition(Condition condition) {
		return conditions.remove(condition);
	}
	
	/**
	 * Removes all the conditions from the {@link Conditions}.
	 */
	public void clearConditions() {
		conditions.clear();
	}
	
	/**
	 * Get a list of all the {@link Condition}s of this conditions.
	 * @return
	 */
	public List<Condition> getConditions() {
		return conditions;
	}

	/**
	 * Processes all the conditions.
	 * @param the edge to be processed
	 * @return the value of the handled attribute; <br>
	 * <ul><li><code>True</code> if the combination of obligated conditions has been met, taking into account the <code>and</code> and <code>or</code> relationships.</li>
	 * <li><code>False</code> if the combination of obligated conditions has not been met.</li>
	 * <li><code>Null</code> if at least one obligated condition has not been processed.</li></ul>
	 */
	public Boolean processConditions(Edge edge) {
		Boolean tempValue = null;
		
		//Only Obligated and feedback-only conditions will be processed, even if a condition returns false.		
		for (Condition c : conditions) {
			if (c.getPurview() == Purview.OBLIGATED || c.getPurview() == Purview.FEEDBACK_ONLY) {
				c.clearHandled();
				c.process(edge);
			}
		}
		
		//calculate total value
		
		//look for AND and OR relations
		Map<Condition, Boolean> andMap = mapAndConditions();
		Set<Condition> orSet = listOrConditions();
		
		//first calculate the sum of OR-values
		if (orSet.isEmpty()) {
			tempValue = true;
		} else {
			tempValue = false;
		}
		for (Condition c : orSet) {
			if (andMap.get(c) != null && tempValue != null ) {
				tempValue = tempValue || andMap.get(c);
				//condition has been used
				andMap.remove(c);
			} else {
				tempValue = null;
			}
		}

		//then combine remaining values with AND, only if the tempValue is true.
		if (tempValue != null && tempValue == true) {
			for (Boolean b : andMap.values()) {
				if (b != null && tempValue != null) {
					tempValue = tempValue && b;
				} else {
					tempValue = null;
				}
			}
		}
		return tempValue;		
	}

	private Map<Condition, Boolean> mapAndConditions() {
		//Map the first condition with the value of the second condition in a AND-relation (1 false = everything false)
		Map<Condition, Boolean> andMap= new HashMap<Condition, Boolean>();		
		for (Condition c : conditions) {
			if (c.getPurview() == Purview.OBLIGATED) {
				List<Condition> andConditions = c.getAndConditions();
				andMap.put(c, c.getValue());
				for (Condition andC : andConditions) {
					if (c.getValue() != null && andC.getValue() != null) {
						andMap.put(c, c.getValue() && andC.getValue());
					} else {
						andMap.put(c, c.getValue());
					}
				}
			}
		}
		return andMap;
	}
	
	private Set<Condition> listOrConditions() {
		//List all conditions which have a or-relation
		Set<Condition> orSet= new HashSet<Condition>();		
		for (Condition c : conditions) {
			if (c.getPurview() == Purview.OBLIGATED) {
				List<Condition> orConditions = c.getOrConditions();
				for (Condition orC : orConditions) {
					orSet.add(c);
					if (c.getValue() != null && orC.getValue() != null) {
						orSet.add(orC);
					}
				}
			}
		}
		return orSet;
	}	
}
