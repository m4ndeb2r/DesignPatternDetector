package nl.ou.dpd.parsing.template;

import nl.ou.dpd.domain.edge.Edge;
import nl.ou.dpd.domain.edge.EdgeType;
import nl.ou.dpd.domain.node.Attribute;
import nl.ou.dpd.domain.node.Clazz;
import nl.ou.dpd.domain.node.Interface;
import nl.ou.dpd.domain.node.Node;
import nl.ou.dpd.domain.node.Visibility;
import nl.ou.dpd.domain.rule.AttributeRule;
import nl.ou.dpd.domain.rule.EdgeRule;
import nl.ou.dpd.domain.rule.NodeRule;
import nl.ou.dpd.domain.rule.Operation;
import nl.ou.dpd.domain.rule.RuleException;
import nl.ou.dpd.domain.rule.Scope;
import nl.ou.dpd.domain.rule.Topic;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests the {@link TemplatesParser} class.
 *
 * @author Martin de Boer
 * @author Peter Vansweevelt
 */
public class RuleElementApplicatorTest {

    private EdgeRuleElementApplicator edgeRuleElementApplicator;
    private NodeRuleElementApplicator nodeRuleElementApplicator;
    private AttributeRuleElementApplicator attributeRuleElementApplicator;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Tests the applyEdgeRules and exception handling in case of a topic that cannot be applied.
     */
    @Test
    public void testApplyEdgeRule() {
        Edge mould = new Edge("id1", "node1node2", new Clazz("nodeId1", "node1"), new Clazz("nodeId2", "node2"));
        EdgeRule relationRule = new EdgeRule(mould, Scope.RELATION, Topic.TYPE, Operation.EQUALS);
        EdgeRule cardinalityLeftRule = new EdgeRule(mould, Scope.RELATION, Topic.CARDINALITY_LEFT, Operation.EQUALS);
        EdgeRule cardinalityRightRule = new EdgeRule(mould, Scope.RELATION, Topic.CARDINALITY_RIGHT, Operation.EQUALS);

        edgeRuleElementApplicator = new EdgeRuleElementApplicator(relationRule, "Association_directed");
        edgeRuleElementApplicator.apply();
        assertEquals(EdgeType.ASSOCIATION_DIRECTED, mould.getRelationType());

        //test different cardinality formats
        edgeRuleElementApplicator = new EdgeRuleElementApplicator(cardinalityLeftRule, "0..*");
        edgeRuleElementApplicator.apply();
        assertEquals(0, mould.getCardinalityLeft().getLower());
        assertEquals(-1, mould.getCardinalityLeft().getUpper());

        edgeRuleElementApplicator = new EdgeRuleElementApplicator(cardinalityRightRule, "1");
        edgeRuleElementApplicator.apply();
        assertEquals(1, mould.getCardinalityRight().getLower());
        assertEquals(1, mould.getCardinalityRight().getUpper());

        edgeRuleElementApplicator = new EdgeRuleElementApplicator(cardinalityRightRule, "1,5");
        edgeRuleElementApplicator.apply();
        assertEquals(1, mould.getCardinalityRight().getLower());
        assertEquals(5, mould.getCardinalityRight().getUpper());

        edgeRuleElementApplicator = new EdgeRuleElementApplicator(cardinalityRightRule, "0,*");
        edgeRuleElementApplicator.apply();
        assertEquals(0, mould.getCardinalityRight().getLower());
        assertEquals(-1, mould.getCardinalityRight().getUpper());

        edgeRuleElementApplicator = new EdgeRuleElementApplicator(cardinalityRightRule, "1..1");
        edgeRuleElementApplicator.apply();
        assertEquals(1, mould.getCardinalityRight().getLower());
        assertEquals(1, mould.getCardinalityRight().getUpper());

        thrown.expect(RuleException.class);
        thrown.expectMessage("Unexpected topic while applying RELATION: 'MODIFIER_ABSTRACT'.");

        EdgeRule wrongTopicRule = new EdgeRule(mould, Scope.RELATION, Topic.MODIFIER_ABSTRACT, Operation.EQUALS);
        edgeRuleElementApplicator = new EdgeRuleElementApplicator(wrongTopicRule, "1..1");
        edgeRuleElementApplicator.apply();
    }

    /**
     * Tests the applyNodeRules for a Clazz and exception handling in case of a topic that cannot be applied.
     */
    @Test
    public void testApplyClazzRule() {
        Node mould = new Clazz("id1", "clazz1");
        NodeRule visibility = new NodeRule(mould, Scope.OBJECT, Topic.VISIBILITY, Operation.EQUALS);
        NodeRule modifierAbstract = new NodeRule(mould, Scope.OBJECT, Topic.MODIFIER_ABSTRACT, Operation.EQUALS);
        NodeRule wrongTopic = new NodeRule(mould, Scope.OBJECT, Topic.TYPE, Operation.EQUALS);

        assertNull(mould.getVisibility());

        nodeRuleElementApplicator = new NodeRuleElementApplicator(visibility, "private");
        nodeRuleElementApplicator.apply();
        assertEquals(Visibility.PRIVATE, mould.getVisibility());

        nodeRuleElementApplicator = new NodeRuleElementApplicator(visibility, "public");
        nodeRuleElementApplicator.apply();
        assertEquals(Visibility.PUBLIC, mould.getVisibility());

        nodeRuleElementApplicator = new NodeRuleElementApplicator(visibility, "protected");
        nodeRuleElementApplicator.apply();
        assertEquals(Visibility.PROTECTED, mould.getVisibility());

        nodeRuleElementApplicator = new NodeRuleElementApplicator(visibility, "package");
        nodeRuleElementApplicator.apply();
        assertEquals(Visibility.PACKAGE, mould.getVisibility());

        assertNull(mould.isAbstract());

        nodeRuleElementApplicator = new NodeRuleElementApplicator(modifierAbstract, "true");
        nodeRuleElementApplicator.apply();
        assertTrue(mould.isAbstract());

        thrown.expect(RuleException.class);
        thrown.expectMessage("Unexpected topic while applying OBJECT: 'TYPE'.");

        nodeRuleElementApplicator = new NodeRuleElementApplicator(wrongTopic, "dummie");
        nodeRuleElementApplicator.apply();
    }

    /**
     * Tests the applyNodeRules for an Interface and exception handling in case of a scope that cannot be applied.
     */
    @Test
    public void testApplyInterfaceRule() {
        Node mould = new Interface("id1", "interface1");
        NodeRule wrongScope = new NodeRule(mould, Scope.RELATION, Topic.TYPE, Operation.EQUALS);

        thrown.expect(RuleException.class);
        thrown.expectMessage("Unexpected scope: 'RELATION'.");

        nodeRuleElementApplicator = new NodeRuleElementApplicator(wrongScope, "Inheritance");
        nodeRuleElementApplicator.apply();
    }

    /**
     * Tests the applyAttributeRules and exception handling in case of an operation that cannot be applied.
     */
    @Test
    public void testApplyAttributeRule() {
        Node node = new Clazz("id1", "attribute1");
        Attribute mould = new Attribute("id1", "attr1", node);
        AttributeRule visibility = new AttributeRule(mould, Scope.OBJECT, Topic.VISIBILITY, Operation.EQUALS);
        AttributeRule wrongOperation = new AttributeRule(mould, Scope.OBJECT, Topic.VISIBILITY, Operation.EXISTS);

        assertNull(mould.getVisibility());

        attributeRuleElementApplicator = new AttributeRuleElementApplicator(visibility, "private");
        attributeRuleElementApplicator.apply();
        assertEquals(Visibility.PRIVATE, mould.getVisibility());

        attributeRuleElementApplicator = new AttributeRuleElementApplicator(visibility, "public");
        attributeRuleElementApplicator.apply();
        assertEquals(Visibility.PUBLIC, mould.getVisibility());

        attributeRuleElementApplicator = new AttributeRuleElementApplicator(visibility, "protected");
        attributeRuleElementApplicator.apply();
        assertEquals(Visibility.PROTECTED, mould.getVisibility());

        attributeRuleElementApplicator = new AttributeRuleElementApplicator(visibility, "package");
        attributeRuleElementApplicator.apply();
        assertEquals(Visibility.PACKAGE, mould.getVisibility());

        thrown.expect(RuleException.class);
        thrown.expectMessage("Unexpected operation while applying TOPIC: 'EXISTS'");

        attributeRuleElementApplicator = new AttributeRuleElementApplicator(wrongOperation, "Inheritance");
        attributeRuleElementApplicator.apply();
    }
}
