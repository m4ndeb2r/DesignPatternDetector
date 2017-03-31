package nl.ou.dpd.domain.rule;

import nl.ou.dpd.domain.edge.Edge;
import nl.ou.dpd.domain.node.Clazz;

/**
 * A {@link Rule} represents a part of a condition.
 *
 * @author Peter Vansweevelt
 */
public class Rule {

    public enum Operator {EQUALS, EXISTS}

    ;

    public enum Target {TYPE, VISIBILITY, MODIFIER, CARDINALITY}

    ;

    private final Clazz rulenode;
    private final Edge ruleedge;
    private Operator operator;
    private Target target;
    private RuleTopic topic;

    /**
     * Creates a rule with the {@link Clazz} node that implements the features needed to apply the rule.
     *
     * @param node the {@link Clazz} containing the rule's features;
     */
    public Rule(Clazz node) {
        this.rulenode = node;
        this.ruleedge = null;
        setTopic(null);
        setOperator(null);
        setTarget(null); //holds the feature to be evaluated
        //		rulenodetype = node.getType();
    }

    public Rule(Edge edge) {
        this.ruleedge = edge;
        this.rulenode = null;
        setTopic(null);
        setOperator(null);
        setTarget(null); //holds the feature to be evaluated
        //		rulenodetype = node.getType();
    }

    /**
     * Sets the topic of the {@link Rule}.
     *
     * @param topic the target of the rule.
     */
    private void setTopic(RuleTopic topic) {
        this.topic = topic;
    }

    /**
     * Sets the operator of the {@link Rule}.
     *
     * @param operator the desired operation of the rule.
     */
    private void setOperator(Operator operator) {
        this.operator = operator;
    }

    /**
     * Sets the target of the {@link Rule}.
     *
     * @param target of the rule.
     */
    private void setTarget(Target target) {
        this.target = target;
    }

    /**
     * Applies the rule on a given {@ink Clazz}.
     *
     * @param the class under consideration
     * @return <code>True</code> if the node meets the conditions set in the {@link Rule}. <code> False</code> otherwise.
     * >>>or return: feedback?
     */
    public boolean applyRule(Clazz systemnode) {
        /*TODO*****************
		 * The system class must be bound with a dp-class as it does in MatchedNodes. Now the classtype is still compared by using the name.
		 * Maybe a decoratorpattern on the system class can be used to dynamically add the associated dp class to it?
		 */
        //we always inspect a node of a given class-type
        if (systemnode.getName().equalsIgnoreCase(rulenode.getName())) {
            if (topic == RuleTopic.TOPIC_OBJECT) {
                if (target == Target.TYPE) {
                    return systemnode.getType() == rulenode.getType();
                }
                if (target == Target.VISIBILITY) {
                    return systemnode.getVisibility() == rulenode.getVisibility();
                }
                if (target == Target.MODIFIER) {
                    return (systemnode.isAbstract() == rulenode.isAbstract()
                            && systemnode.isRoot() == rulenode.isRoot()
                            && systemnode.isLeaf() == rulenode.isLeaf()
                            && systemnode.isActive() == rulenode.isActive());
                }
            }
        }
        return false;
    }

    /**
     * Applies the rule on a given {@ink Edge}.
     *
     * @param the class under consideration
     * @return <code>True</code> if the node meets the conditions set in the {@link Rule}. <code> False</code> otherwise.
     */
    public boolean applyRule(Edge systemedge) {
        boolean ruleFulfilled = false;
/* this condition supposes an name-attribute in the edge-class as well as a cardinality	*/
        if (systemedge.getName().equalsIgnoreCase(ruleedge.getName())) {
            if (topic == RuleTopic.TOPIC_RELATION) {
                if (target == Target.TYPE) {
                    return systemedge.getRelationType() == ruleedge.getRelationType();
                }
            }
            if (topic == RuleTopic.TOPIC_OBJECT) {
                if (target == Target.CARDINALITY) {
                }
                boolean front = systemedge.getCardinalityFront() == ruleedge.getCardinalityFront();
                boolean end = systemedge.getCardinalityEnd() == ruleedge.getCardinalityEnd();
                return front && end;
            }
        }
        return ruleFulfilled;
    }

}
