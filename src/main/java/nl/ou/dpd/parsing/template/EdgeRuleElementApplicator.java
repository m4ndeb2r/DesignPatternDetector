package nl.ou.dpd.parsing.template;

import nl.ou.dpd.domain.edge.Cardinality;
import nl.ou.dpd.domain.edge.Edge;
import nl.ou.dpd.domain.edge.EdgeType;
import nl.ou.dpd.domain.rule.EdgeRule;
import nl.ou.dpd.domain.rule.Rule;
import nl.ou.dpd.domain.rule.RuleException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

/**
 * Class that applies an {@link EdgeRule} using a specified value.
 * Results in the Edge, given in the Rule, implementing the Rule in the Edge itself.
 *
 * @author Peter Vansweevelt
 */
public class EdgeRuleElementApplicator extends RuleElementApplicator<Edge> {

    private static final Logger LOGGER = LogManager.getLogger(EdgeRuleElementApplicator.class);

    private Edge edge;

    /**
     * @param rule
     * @param value
     */
    public EdgeRuleElementApplicator(Rule<Edge> rule, String value) {
        super(rule, value);
        edge = rule.getMould();
    }

    /**
     * Applies this {@link EdgeRule} on a given {@link Edge}.
     * Results in the Edge, given in the Rule, implementing the Rule in the Edge itself.
     */
    public void apply() {
        switch (getScope()) {
            case RELATION:
                applyRelation();
                break;
            default:
                error(String.format("Unexpected scope while applying: '%s'.", getScope()));
        }
    }

    private void applyRelation() {
        switch (getTopic()) {
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
                error(String.format("Unexpected topic while applying RELATION: '%s'.", getTopic()));
        }
    }

    private void applyRelationCardinalityLeft() {
        switch (getOperation()) {
            case EQUALS:
                applyCardinalityLeftEquals();
                break;
            default:
                error(String.format("Unexpected operation while applying CARDINALITY_LEFT: '%s'.", getOperation()));
        }
    }

    private void applyRelationCardinalityRight() {
        switch (getOperation()) {
            case EQUALS:
                applyCardinalityRightEquals();
                break;
            default:
                error(String.format("Unexpected operation while applying CARDINALITY_RIGHT: '%s'.", getOperation()));
        }
    }

    private void applyRelationType() {
        switch (getOperation()) {
            case EQUALS:
                edge.setRelationType(findEdgeTypeByName(getValue()));
                break;
            default:
                error(String.format("Unexpected operation while applying TYPE: '%s'.", getOperation()));

        }
    }

    private void applyCardinalityLeftEquals() {
        edge.setCardinalityLeft(Cardinality.valueOf(getValue()));
    }

    private void applyCardinalityRightEquals() {
        edge.setCardinalityRight(Cardinality.valueOf(getValue()));
    }

    private EdgeType findEdgeTypeByName(String edgeTypeName) {
        return Arrays.stream(EdgeType.values())
                .filter(edgeType -> edgeType.getName().contains(edgeTypeName.toUpperCase()))
                .findFirst()
                .orElse(null);
    }

    private void error(String message) {
        LOGGER.error(message);
        throw new RuleException(message);
    }


}
