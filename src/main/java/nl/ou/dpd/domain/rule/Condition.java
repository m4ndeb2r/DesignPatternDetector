package nl.ou.dpd.domain.rule;

import nl.ou.dpd.domain.edge.Edge;
import nl.ou.dpd.domain.node.Attribute;
import nl.ou.dpd.domain.node.Node;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A Design Pattern template (defined in a templates xml-file) is defined by way of a number of conditions. This class
 * represents these conditions. Just like in the xml-file, a {@link Condition} comprises a number of {@link Rules}s,
 * some of which apply to edges, and some of which apply to either the left node or the right node of an edge. There
 * can be several {@link EdgeRule}s and {@link NodeRule}s (for either or both nodes) present in this {@link Condition}.
 * <p>
 * An edge and its nodes in a system under consideration can be compared to an edge and its nodes in a design pattern
 * by processing the {@link Condition}s. Processing a {@link Condition} comes down to processing all its
 * {@link EdgeRule}s and {@link NodeRule}s.
 * <p>
 * A {@link Condition} class's {@link Purview} defines the interest of a {@link Condition}: some {@link Condition}s are
 * mandatory, some can be ignored, and some are used for feedback purposes only )depending on the user's preference).
 *
 * @author Peter Vansweevelt
 * @author Martin de Boer
 */
public class Condition {

    private static final Logger LOGGER = LogManager.getLogger(Condition.class);

    private final String id;
    private final String description;

    private List<EdgeRule> edgeRules;
    private List<NodeRule> leftNodeRules;
    private List<NodeRule> rightNodeRules;
    private List<NodeRule> nodeRules;
    private List<AttributeRule> attributeRules;

    private Purview purview;

    private boolean processResult;
    private boolean processed;

    /**
     * Constructor of the {@link Condition} class, bases on its id and a short description.
     *
     * @param id          the id of the {@link Condition}
     * @param description a short description of the {@link Condition}
     */
    public Condition(String id, String description) {
        this.id = id;
        this.description = description;

        edgeRules = new ArrayList<>();
        leftNodeRules = new ArrayList<>();
        rightNodeRules = new ArrayList<>();
        nodeRules = new ArrayList<>();
        attributeRules = new ArrayList<>();

        // Default value: ignore
        purview = Purview.IGNORE;

        processResult = false;
        processed = false;
    }

    /**
     * Get the id of the {@link Condition}.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Get the description of the {@link Condition}.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    public List<EdgeRule> getEdgeRules() {
        return edgeRules;
    }

    public void setEdgeRules(List<EdgeRule> edgeRules) {
        this.edgeRules = edgeRules;
    }

    public List<NodeRule> getLeftNodeRules() {
        return leftNodeRules;
    }

    public void setLeftNodeRules(List<NodeRule> leftNodeRules) {
        this.leftNodeRules = leftNodeRules;
    }

    public List<NodeRule> getRightNodeRules() {
        return rightNodeRules;
    }

    public void setRightNodeRules(List<NodeRule> rightNodeRules) {
        this.rightNodeRules = rightNodeRules;
    }

    public List<NodeRule> getNodeRules() {
        return nodeRules;
    }

    public void setNodeRules(List<NodeRule> nodeRules) {
        this.nodeRules = nodeRules;
    }

    public List<AttributeRule> getAttributeRules() {
        return attributeRules;
    }

    public void setAtributeRules(List<AttributeRule> attributeRules) {
        this.attributeRules = attributeRules;
    }
    
    /**
     * Get the purview.
     *
     * @return the purview
     */
    public Purview getPurview() {
        return purview;
    }

    /**
     * Set the purview.
     *
     * @param purview the new {@link Purview} for this {@link Condition}.
     */
    public void setPurview(Purview purview) {
        this.purview = purview;
    }

    /**
     * Indicates whether this {@link Condition} is processed, and was processed successfully.
     *
     * @return {@code true} if the process method was executed and returned {@code true}.
     */
    public boolean isProcessedSuccessfully() {
        return isProcessed() && processResult == true;
    }

    /**
     * Indicates whether this {@link Condition} is processed, and was processed unsuccessfully.
     *
     * @return {@code true} if the process method was executed, but returned {@code false}.
     */
    public boolean isProcessedUnsuccessfully() {
        return isProcessed() && processResult == false;
    }

    /**
     * Indicates whether this {@link Condition} was processed.
     *
     * @return {@code false} if the condition has not been processed yet. {@code true} otherwise.
     */
    public boolean isProcessed() {
        return processed;
    }

    /**
     * Reset the processed attribute to {@code false}.
     */
    public void clearProcessed() {
        this.processed = false;
    }

    /**
     * Process the {@link Condition}. A {@link Condition} is only processed once. To force reprocessing of a
     * {@link Condition}, please call {@link Condition#clearProcessed()} beforehand.
     *
     * @param edge an edge of the system under consideration.
     * @return {@code true} if all the {@link Rule}s have been met, or {@code null} if the {@link Purview} is set to
     * {@link Purview#IGNORE}, or {@code false} in all other cases.
     */    public boolean process(Edge edge) {
        if (processed == true) {
            return processResult;
        }
        switch (purview) {
            case IGNORE:
                this.processed = false;
                return true;
            case FEEDBACK_ONLY:
            case MANDATORY:
                this.processResult = processRules(edge);
                this.processed = true;
                return this.processResult;
            default:
                return error("Unexpected purview: " + purview + ".");
        }
    }


    /**
     * Process the {@link Condition}. A {@link Condition} is only processed once. To force reprocessing of a
     * {@link Condition}, please call {@link Condition#clearProcessed()} beforehand.
     *
     * @param the systemEdge to be processed
     * @param patternEdge the mould holding the desired values of this edge
     * @return {@code true} if all the {@link Rule}s have been met, or {@code null} if the {@link Purview} is set to
     * {@link Purview#IGNORE}, or {@code false} in all other cases.
     */
    public boolean process(Edge systemEdge, Edge patternEdge) {
        if (processed == true) {
            return processResult;
        }
        switch (purview) {
            case IGNORE:
                this.processed = false;
                return true;
            case FEEDBACK_ONLY:
            case MANDATORY:
                this.processResult = processRules(systemEdge, patternEdge);
                this.processed = true;
                return this.processResult;
            default:
                return error("Unexpected purview: " + purview + ".");
        }
    }
    
    /*NEW 12 april 2017*/
    /**
     * Process the {@link Condition}. A {@link Condition} is only processed once. To force reprocessing of a
     * {@link Condition}, please call {@link Condition#clearProcessed()} beforehand.
     *
     * @param the systemEdge to be processed
     * @param patternEdge the mould holding the desired values of this edge
     * @return {@code true} if all the {@link Rule}s have been met, or {@code null} if the {@link Purview} is set to
     * {@link Purview#IGNORE}, or {@code false} in all other cases.
     */
    public boolean process(Map<Node, Node> matchedNodes) {
        if (processed == true) {
            return processResult;
        }
        switch (purview) {
            case IGNORE:
                this.processed = false;
                return true;
            case FEEDBACK_ONLY:
            case MANDATORY:
                this.processResult = processRules(matchedNodes);
                this.processed = true;
                return this.processResult;
            default:
                return error("Unexpected purview: " + purview + ".");
        }
    }
    
    /**
     * Processes the all rules of this {@link Condition}, the edge rules as well as the node rules for either node.
     *
     * @return the accumulated result of processed {@link Rule}s: {@code true} if all the rules succeed, or
     * {@code false} otherwise.
     */
    private boolean processRules(Edge edge) {
        boolean result = true;
        for (Rule<Edge> rule : this.edgeRules) {
            result = result && rule.process(edge);
            if (result == false) {
                return false;
            }
        }
        for (Rule<Node> rule : this.leftNodeRules) {
            result = result && rule.process(edge.getLeftNode());
            if (result == false) {
                return false;
            }
        }
        for (Rule<Node> rule : this.rightNodeRules) {
            result = result && rule.process(edge.getRightNode());
            if (result == false) {
                return false;
            }
        }
        return result;
    }


    /**
     * Processes all the rules of this {@link Condition} for either edge, adhering nodes and their attributes.
     *
     * @return the accumulated result of processed {@link Rule}s: {@code true} if all the rules succeed, or
     * {@code false} otherwise.
     */
    private boolean processRules(Edge systemEdge, Edge patternEdge) {
        boolean result = true;
        
        //evaluate all edge rules matching the given edge
        for (Rule<Edge> rule : this.edgeRules) {
        	if (rule.getMould() == patternEdge) {
	            result = result && rule.process(systemEdge);
	            if (result == false) {
	                return false;
	            }
        	}
        }

        //evaluate all node rules matching the given left node
        for (Rule<Node> rule : this.nodeRules) {
        	if (rule.getMould() == patternEdge.getLeftNode()) {
	            result = result && rule.process(systemEdge.getLeftNode());
	            if (result == false) {
	                return false;
	            }
        	}
        }

        //evaluate all node rules matching the given right node
        for (Rule<Node> rule : this.nodeRules) {
        	if (rule.getMould() == patternEdge.getRightNode()) {
	            result = result && rule.process(systemEdge.getRightNode());
	            if (result == false) {
	                return false;
	            }
        	}
        }

        //evaluate all attribute rules matching the left node attributes with the type of the right node
        //(other attributes must be handled in the 'Map'-version which gives us an overview of the whole system)
        for (Rule<Attribute> rule : this.attributeRules) {
        	boolean partialResult = false;
        	boolean ruleProcessed = false;
        	for (Attribute systemAttribute : findAttributesOfRightNodeType(systemEdge)) {
        		if (rule.getMould().getType().getName() == patternEdge.getRightNode().getName()) {
		            partialResult = partialResult || rule.process(systemAttribute);
		            ruleProcessed = true;
        		}
        	}
        	partialResult = partialResult || !ruleProcessed;
            result = result && partialResult;
            if (result == false) {
                return false;
            }
        }
     return result;
    }
    
	/** Find all attributes in the left node which has the type of the right node.
	 * (other attributes must be handled in the 'Map'-version which gives us an overview of the whole system)
	 * @param systemEdge
	 * @return
	 */
	private List<Attribute> findAttributesOfRightNodeType(Edge systemEdge) {
		List<Attribute> systemAttributes = systemEdge.getLeftNode().getAttributes();
		List<Attribute> foundAttributes = new ArrayList<Attribute>();
		for(Attribute attribute : systemAttributes) {
			if (attribute.getType().getName().equals(systemEdge.getRightNode().getName())) {
				foundAttributes.add(attribute);
			}
		}
		return foundAttributes;
	}
     
    /*NEW 12 april 2017*/
    /**
     * Processes all the noderules of this {@link Condition}, which have a matched node in the matchedNodes-map.
     * If the node on which the rule applies does not exist or the rule is an edgerule, return is {@code true}.
     * 
     * @param a map with system edges as keys and matched pattern edges as values
     * @return the accumulated result of processed node{@link Rule}s: {@code true} if all the rules succeed, or the node does not exist in the map.
     * {@code false} otherwise.
     */
    private boolean processRules(Map<Node, Node> matchedNodes) {
        boolean result = true;
        
        for (Rule<Node> rule : this.nodeRules) {
        	if (matchedNodes.containsValue(rule.getMould())) {
	            result = result && rule.process(getMapKey(matchedNodes, rule.getMould()));
	            if (result == false) {
	                return false;
	            }
        	}
        }
        return result;
    }
    
	/**
	 * Returns the key of the value in the given map.
	 * @param map
	 * @param value
	 * @return
	 */
     private Node getMapKey(Map<Node, Node> map, Node value) {
    	for (Entry<Node, Node> e : map.entrySet()) {
    		if (e.getValue() == value) {
    			return e.getKey();
    		}
    	}
    	return null;
    }

    private boolean error(String message) {
        LOGGER.error(message);
        throw new RuleException(message);
    }

}
