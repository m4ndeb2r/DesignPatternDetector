package nl.ou.dpd.domain.rule;

import nl.ou.dpd.domain.edge.Edge;
import nl.ou.dpd.domain.node.Attribute;
import nl.ou.dpd.domain.node.Node;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

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

    public String getId() {
        return id;
    }

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

    public Purview getPurview() {
        return purview;
    }

    public void setPurview(Purview purview) {
        this.purview = purview;
    }

    public boolean isProcessedSuccessfully() {
        return isProcessed() && processResult == true;
    }

    public boolean isProcessedUnsuccessfully() {
        return isProcessed() && processResult == false;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void clearProcessed() {
        this.processed = false;
    }

    /**
     * Process the {@link Condition}. A {@link Condition} is only processed once. To force reprocessing of a
     * {@link Condition}, please call {@link Condition#clearProcessed()} beforehand.
     *
     * @param systemEdge  the systemEdge to be processed
     * @param patternEdge the mould containing the desired values of this edge
     * @return {@code true} if all the {@link Rule}s have been met, or {@code null} if the {@link Purview} is set to
     * {@link Purview#IGNORE}, or {@code false} in all other cases.
     */
    boolean process(Edge systemEdge, Edge patternEdge) {
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
    
    /**
     * Processes all the rules of this {@link Condition} for either edge, adhering nodes and their attributes.
     *
     * @return the accumulated result of processed {@link Rule}s: {@code true} if all the rules succeed, or
     * {@code false} otherwise.
     */
    private boolean processRules(Edge systemEdge, Edge patternEdge) {

        // Evaluate all edge rules matching the given edge
        for (Rule<Edge> rule : this.edgeRules) {
            if (rule.getMould() == patternEdge && !rule.process(systemEdge)) {
                return false;
            }
        }

        // Evaluate all node rules matching the given left node
        for (Rule<Node> rule : this.nodeRules) {
            if (rule.getMould() == patternEdge.getLeftNode() && !rule.process(systemEdge.getLeftNode())) {
                return false;
            }
            if (rule.getMould() == patternEdge.getRightNode() && !rule.process(systemEdge.getRightNode())) {
                return false;
            }
        }

        // Evaluate all attribute rules matching the left node attributes with the type of the right node
        // (other attributes must be handled in the 'Map'-version which gives us an overview of the whole system)
        for (Rule<Attribute> rule : this.attributeRules) {
            for (Attribute systemAttribute : findAttributesOfRightNodeType(systemEdge)) {
                final String mouldTypeName = rule.getMould().getType().getName();
                final String patternRightNodeName = patternEdge.getRightNode().getName();
                if (mouldTypeName == patternRightNodeName && !rule.process(systemAttribute)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Find all attributes in the left node which has the type of the right node.
     * (other attributes must be handled in the 'Map'-version which gives us an overview of the whole system)
     *
     * @param systemEdge
     * @return
     */
    private List<Attribute> findAttributesOfRightNodeType(Edge systemEdge) {
        List<Attribute> systemAttributes = systemEdge.getLeftNode().getAttributes();
        List<Attribute> foundAttributes = new ArrayList<>();
        for (Attribute attribute : systemAttributes) {
            if (attribute.getType().getName().equals(systemEdge.getRightNode().getName())) {
                foundAttributes.add(attribute);
            }
        }
        return foundAttributes;
    }

    private boolean error(String message) {
        LOGGER.error(message);
        throw new RuleException(message);
    }

}
