package nl.ou.dpd.parsing.template;

import nl.ou.dpd.domain.node.Node;
import nl.ou.dpd.domain.node.Visibility;
import nl.ou.dpd.domain.rule.NodeRule;
import nl.ou.dpd.domain.rule.Rule;
import nl.ou.dpd.domain.rule.RuleException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class that applies a NodeRule using a specified value.
 * Results in the Node, given in the Rule, implementing the Rule in the Node itself.
 *
 * @author Peter Vansweevelt
 */
public class ApplyNodeRule extends ApplyRule<Node> {

    private static final Logger LOGGER = LogManager.getLogger(ApplyNodeRule.class);

    private Node node;

    /**
     * @param rule
     * @param value
     */
    public ApplyNodeRule(Rule<Node> rule, String value) {
        super(rule, value);
        node = rule.getMould();
    }

    /**
     * Applies this {@link NodeRule} on a given {@ink Node}.
     * Results in the Node, given in the Rule, implementing the Rule in the Node itself.
     */
    public void apply() {
        switch (rule.getScope()) {
            case OBJECT:
                applyObject();
                break;
            default:
                error("Unexpected scope: " + rule.getScope() + ".");
        }
    }

    private void applyObject() {
        switch (rule.getTopic()) {
            case VISIBILITY:
                applyObjectVisibility();
                break;
            case MODIFIER_ROOT:
                applyObjectModifierRoot();
                break;
            case MODIFIER_LEAF:
                applyObjectModifierLeaf();
                node.setLeaf(Boolean.valueOf(value));
                break;
            case MODIFIER_ABSTRACT:
                applyObjectModifierAbstract();
                node.setAbstract(Boolean.valueOf(value));
                break;
            case MODIFIER_ACTIVE:
                applyObjectModifierActive();
                node.setActive(Boolean.valueOf(value));
                break;
            default:
                error("Unexpected topic while applying OBJECT: " + rule.getTopic() + ".");
        }
    }

    private void applyObjectVisibility() {
        switch (rule.getOperator()) {
            case EQUALS:
                applyObjectVisibilityEquals();
                break;
            default:
                error("Unexpected operator while applying CARDINALITY: " + rule.getOperator() + ".");

        }
    }

    private void applyObjectModifierRoot() {
        switch (rule.getOperator()) {
            case EQUALS:
                applyObjectModifierRootEquals();
                break;
            default:
                error("Unexpected operator while applying MODIFIER_ROOT: " + rule.getOperator() + ".");
        }
    }

    private void applyObjectModifierActive() {
        switch (rule.getOperator()) {
            case EQUALS:
                applyObjectModifierActiveEquals();
                break;
            default:
                error("Unexpected operator while applying MODIFIER_ACTIVE: " + rule.getOperator() + ".");
        }
    }

    private void applyObjectModifierLeaf() {
        switch (rule.getOperator()) {
            case EQUALS:
                applyObjectModifierLeafEquals();
                break;
            default:
                error("Unexpected operator while applying MODIFIER_LEAF: " + rule.getOperator() + ".");
        }
    }

    private void applyObjectModifierAbstract() {
        switch (rule.getOperator()) {
            case EQUALS:
                applyObjectModifierAbstractEquals();
                break;
            default:
                error("Unexpected operator while applying MODIFIER_ABSTRACT: " + rule.getOperator() + ".");
        }
    }

    private void applyObjectModifierActiveEquals() {
        node.setActive(Boolean.valueOf(value));
    }


    private void applyObjectModifierAbstractEquals() {
        node.setAbstract(Boolean.valueOf(value));
    }

    private void applyObjectModifierLeafEquals() {
        node.setLeaf(Boolean.valueOf(value));
    }


    private void applyObjectModifierRootEquals() {
        node.setRoot(Boolean.valueOf(value));
    }


    private void applyObjectVisibilityEquals() {
        node.setVisibility(findVisibilityByName(value));
    }

    private Visibility findVisibilityByName(String visibilityName) {
        for (Visibility visibility : Visibility.values()) {
            if (visibility.toString().contains(visibilityName.toUpperCase())) {
                return visibility;
            }
        }
        return null;
    }

    private void error(String message) {
        LOGGER.error(message);
        throw new RuleException(message);
    }
}
