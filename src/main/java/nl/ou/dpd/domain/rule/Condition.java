package nl.ou.dpd.domain.rule;

import nl.ou.dpd.domain.edge.Edge;
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
     */
    public boolean process(Edge edge) {
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
     * Processes the all rules of this {@link Condition}, the edge rules as well as the node rules for either node.
     *
     * @return the accumulated result of processed {@link Rule}s: {@code true} if all the rules succeed, or
     * {@code false} otherwise.
     */
    private boolean processRules(Edge edge) {
        boolean result = true;
        for (Rule rule : this.edgeRules) {
            result = result && rule.process(edge);
            if (result == false) {
                return false;
            }
        }
        for (Rule rule : this.leftNodeRules) {
            result = result && rule.process(edge.getLeftNode());
            if (result == false) {
                return false;
            }
        }
        for (Rule rule : this.rightNodeRules) {
            result = result && rule.process(edge.getRightNode());
            if (result == false) {
                return false;
            }
        }
        return result;
    }

    private boolean error(String message) {
        LOGGER.error(message);
        throw new RuleException(message);
    }

}
