package nl.ou.dpd.parsing.template;

import nl.ou.dpd.domain.node.Node;
import nl.ou.dpd.domain.node.Visibility;
import nl.ou.dpd.domain.rule.Rule;
import nl.ou.dpd.domain.rule.RuleException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A {@link RuleElementApplicator} implementation for {@link Node}s.
 *
 * @author Peter Vansweevelt
 * @author Martin de Boer
 * @see EdgeRuleElementApplicator
 * @see AttributeRuleElementApplicator
 */
public class NodeRuleElementApplicator extends RuleElementApplicator<Node> {

    private static final Logger LOGGER = LogManager.getLogger(NodeRuleElementApplicator.class);

    public NodeRuleElementApplicator(Rule<Node> rule, String value) {
        super(rule, value);
    }

    public void apply() {
        switch (getScope()) {
            case OBJECT:
                applyObject();
                break;
            default:
                error(String.format("Unexpected scope: '%s'.", getScope()));
        }
    }

    private void applyObject() {
        switch (getTopic()) {
            case VISIBILITY:
                applyObjectVisibility();
                break;
            case MODIFIER_ROOT:
                applyObjectModifierRoot();
                break;
            case MODIFIER_LEAF:
                applyObjectModifierLeaf();
                break;
            case MODIFIER_ABSTRACT:
                applyObjectModifierAbstract();
                break;
            case MODIFIER_ACTIVE:
                applyObjectModifierActive();
                break;
            default:
                error(String.format("Unexpected topic while applying OBJECT: '%s'.", getTopic()));
        }
    }

    private void applyObjectVisibility() {
        switch (getOperation()) {
            case EQUALS:
                getMould().setVisibility(Visibility.valueOf(getValue().toUpperCase()));
                break;
            default:
                error(String.format("Unexpected operation while applying CARDINALITY: '%s'.", getOperation()));

        }
    }

    private void applyObjectModifierRoot() {
        switch (getOperation()) {
            case EQUALS:
                getMould().setRoot(Boolean.valueOf(getValue()));
                break;
            default:
                error(String.format("Unexpected operation while applying MODIFIER_ROOT: '%s'.", getOperation()));
        }
    }

    private void applyObjectModifierActive() {
        switch (getOperation()) {
            case EQUALS:
                getMould().setActive(Boolean.valueOf(getValue()));
                break;
            default:
                error(String.format("Unexpected operation while applying MODIFIER_ACTIVE: '%s'.", getOperation()));
        }
    }

    private void applyObjectModifierLeaf() {
        switch (getOperation()) {
            case EQUALS:
                getMould().setLeaf(Boolean.valueOf(getValue()));
                break;
            default:
                error(String.format("Unexpected operation while applying MODIFIER_LEAF: '%s'.", getOperation()));
        }
    }

    private void applyObjectModifierAbstract() {
        switch (getOperation()) {
            case EQUALS:
                getMould().setAbstract(Boolean.valueOf(getValue()));
                break;
            default:
                error(String.format("Unexpected operation while applying MODIFIER_ABSTRACT: '%s'.", getOperation()));
        }
    }

    private void error(String message) {
        LOGGER.error(message);
        throw new RuleException(message);
    }
}
