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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests the {@link TemplatesParser} class.
 *
 * @author Martin de Boer
 * @author Peter Vansweevelt
 */
public class RuleElementApplicatorTest {
	
    private EdgeRuleElementApplicator aer;
    private NodeRuleElementApplicator anr;
    private AttributeRuleElementApplicator aar;

    // A test file containing valid XML.
    private static final String ADAPTERTEMPLATES_XML = "/template_adapters.xml";
    // A test file containing invalid XML.
    private static final String INVALID_XML = "/invalid.xml";
    // A test file containing two nodes with the same name.
    private static final String DOUBLE_NODE_XML = "/template_adapters_test_doubleNode.xml";
    // A test file containing two nodes with the same name.
    private static final String DOUBLE_EDGE_XML = "/template_adapters_test_doubleEdge.xml";
    // A test file containing an invalid edge tag.
    private static final String MISSING_EDGE_XML = "/template_adapters_test_missingEdge.xml";
    // A test file containing an invalid edge tag.
    private static final String INVALID_TAG_XML = "/template_adapters_test_invalidTag.xml";
	
    // A test file containing invalid XML.

    /**
     * Exception rule.
     */
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

        aer = new EdgeRuleElementApplicator(relationRule, "Association_directed");
        aer.apply();
        assertEquals(EdgeType.ASSOCIATION_DIRECTED, mould.getRelationType());

        //test different cardinality formats
        aer = new EdgeRuleElementApplicator(cardinalityLeftRule, "0..*");
        aer.apply();
        assertEquals(0, mould.getCardinalityLeft().getLower());
        assertEquals(-1, mould.getCardinalityLeft().getUpper());

        aer = new EdgeRuleElementApplicator(cardinalityRightRule, "1");
        aer.apply();
        assertEquals(1, mould.getCardinalityRight().getLower());
        assertEquals(1, mould.getCardinalityRight().getUpper());

        aer = new EdgeRuleElementApplicator(cardinalityRightRule, "1,5");
        aer.apply();
        assertEquals(1, mould.getCardinalityRight().getLower());
        assertEquals(5, mould.getCardinalityRight().getUpper());
        
        aer = new EdgeRuleElementApplicator(cardinalityRightRule, "0,*");
        aer.apply();
        assertEquals(0, mould.getCardinalityRight().getLower());
        assertEquals(-1, mould.getCardinalityRight().getUpper());

        aer = new EdgeRuleElementApplicator(cardinalityRightRule, "1..1");
        aer.apply();
        assertEquals(1, mould.getCardinalityRight().getLower());
        assertEquals(1, mould.getCardinalityRight().getUpper());

        thrown.expect(RuleException.class);
        thrown.expectMessage("Unexpected topic while applying RELATION: 'MODIFIER_ABSTRACT'.");

        EdgeRule wrongTopicRule = new EdgeRule(mould, Scope.RELATION, Topic.MODIFIER_ABSTRACT, Operation.EQUALS);
        aer = new EdgeRuleElementApplicator(wrongTopicRule, "1..1");
        aer.apply();
    }

    /**
     * Tests the applyNodeRules for a Clazz and exception handling in case of a topic that cannot be applied.
     */
    @Test
    public void testApplyClazzRule() {
        Node mould = new Clazz("id1", "clazz1");
        NodeRule visibility = new NodeRule(mould, Scope.OBJECT, Topic.VISIBILITY, Operation.EQUALS);
        NodeRule modifierAbstract = new NodeRule(mould, Scope.OBJECT, Topic.MODIFIER_ABSTRACT, Operation.EQUALS);
        NodeRule modifierActive = new NodeRule(mould, Scope.OBJECT, Topic.MODIFIER_ACTIVE, Operation.EQUALS);
        NodeRule modifierLeaf = new NodeRule(mould, Scope.OBJECT, Topic.MODIFIER_LEAF, Operation.EQUALS);
        NodeRule modifierRoot = new NodeRule(mould, Scope.OBJECT, Topic.MODIFIER_ROOT, Operation.EQUALS);
        NodeRule wrongTopic = new NodeRule(mould, Scope.OBJECT, Topic.TYPE, Operation.EQUALS);

        assertNull(mould.getVisibility());
        
        anr = new NodeRuleElementApplicator(visibility, "private");
        anr.apply();
        assertEquals(Visibility.PRIVATE, mould.getVisibility());

        anr = new NodeRuleElementApplicator(visibility, "public");
        anr.apply();
        assertEquals(Visibility.PUBLIC, mould.getVisibility());

        anr = new NodeRuleElementApplicator(visibility, "protected");
        anr.apply();
        assertEquals(Visibility.PROTECTED, mould.getVisibility());
        
        anr = new NodeRuleElementApplicator(visibility, "package");
        anr.apply();
        assertEquals(Visibility.PACKAGE, mould.getVisibility());
        
        assertNull(mould.isAbstract());
        
        anr = new NodeRuleElementApplicator(modifierAbstract, "true");
        anr.apply();
        assertTrue(mould.isAbstract());
        
        assertNull(mould.isActive());
        
        anr = new NodeRuleElementApplicator(modifierActive, "false");
        anr.apply();
        assertFalse(mould.isActive());
        
        assertNull(mould.isRoot());

        anr = new NodeRuleElementApplicator(modifierRoot, "true");
        anr.apply();
        assertTrue(mould.isRoot());
        
        assertNull(mould.isLeaf());
        
        anr = new NodeRuleElementApplicator(modifierLeaf, "false");
        anr.apply();
        assertFalse(mould.isLeaf());

        //false if boolean-translation to 'true' has not succeeded.
        anr = new NodeRuleElementApplicator(modifierLeaf, "dummie");
        anr.apply();
        assertFalse(mould.isLeaf());

        thrown.expect(RuleException.class);
        thrown.expectMessage("Unexpected topic while applying OBJECT: 'TYPE'.");

        anr = new NodeRuleElementApplicator(wrongTopic, "dummie");
        anr.apply();
    }

    /**
     * Tests the applyNodeRules for an Interface and exception handling in case of a scope that cannot be applied.
     */
    @Test
    public void testApplyInterfaceRule() {
        Node mould = new Interface("id1", "interface1");
        NodeRule modifierActive = new NodeRule(mould, Scope.OBJECT, Topic.MODIFIER_ACTIVE, Operation.EQUALS);
        NodeRule modifierLeaf = new NodeRule(mould, Scope.OBJECT, Topic.MODIFIER_LEAF, Operation.EQUALS);
        NodeRule modifierRoot = new NodeRule(mould, Scope.OBJECT, Topic.MODIFIER_ROOT, Operation.EQUALS);
        NodeRule wrongScope = new NodeRule(mould, Scope.RELATION, Topic.TYPE, Operation.EQUALS);

        assertNull(mould.isActive());
        
        anr = new NodeRuleElementApplicator(modifierActive, "false");
        anr.apply();
        assertFalse(mould.isActive());
        
        assertNull(mould.isRoot());

        anr = new NodeRuleElementApplicator(modifierRoot, "true");
        anr.apply();
        assertTrue(mould.isRoot());
        
        assertNull(mould.isLeaf());
        
        anr = new NodeRuleElementApplicator(modifierLeaf, "false");
        anr.apply();
        assertFalse(mould.isLeaf());

        //false if boolean-translation to 'true' has not succeeded.
        anr = new NodeRuleElementApplicator(modifierLeaf, "dummie");
        anr.apply();
        assertFalse(mould.isLeaf());

        thrown.expect(RuleException.class);
        thrown.expectMessage("Unexpected scope: 'RELATION'.");

        anr = new NodeRuleElementApplicator(wrongScope, "Inheritance");
        anr.apply();
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
        
        aar = new AttributeRuleElementApplicator(visibility, "private");
        aar.apply();
        assertEquals(Visibility.PRIVATE, mould.getVisibility());

        aar = new AttributeRuleElementApplicator(visibility, "public");
        aar.apply();
        assertEquals(Visibility.PUBLIC, mould.getVisibility());

        aar = new AttributeRuleElementApplicator(visibility, "protected");
        aar.apply();
        assertEquals(Visibility.PROTECTED, mould.getVisibility());
        
        aar = new AttributeRuleElementApplicator(visibility, "package");
        aar.apply();
        assertEquals(Visibility.PACKAGE, mould.getVisibility());
        
        thrown.expect(RuleException.class);
        thrown.expectMessage("Unexpected operation while applying TOPIC: 'EXISTS'");

        aar = new AttributeRuleElementApplicator(wrongOperation, "Inheritance");
        aar.apply();
    }
 }
