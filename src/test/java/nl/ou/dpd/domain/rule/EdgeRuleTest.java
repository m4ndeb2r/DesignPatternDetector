package nl.ou.dpd.domain.rule;

import nl.ou.dpd.domain.edge.Cardinality;
import nl.ou.dpd.domain.edge.Edge;
import nl.ou.dpd.domain.edge.EdgeType;
import nl.ou.dpd.domain.node.Attribute;
import nl.ou.dpd.domain.node.Clazz;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests the {@link EdgeRule} class.
 *
 * @author Martin de Boer
 * @author Peter Vansweevelt
 */
public class EdgeRuleTest {

    /**
     * Exception rule.
     */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Test if the {@link EdgeRule} throws an exception when an unexpected {@link Scope} is provided.
     */
    @Test
    public void testEdgeRuleWithUnknownScope() {
        final Edge mould = new Edge(new Clazz("node1", null), new Clazz("node2", null), EdgeType.ASSOCIATION);
        final Edge systemEdge = new Edge(new Clazz("myClass1", null), new Clazz("myClass2", null), EdgeType.ASSOCIATION);
        final EdgeRule failingScopeRule = new EdgeRule(mould, Scope.OBJECT, Topic.TYPE, Operator.EQUALS);

        thrown.expect(RuleException.class);
        thrown.expectMessage("Unexpected scope: OBJECT.");
        failingScopeRule.process(systemEdge);
    }
    
    /**
     * Test processing an attribute.
     */
    @Test
    public void testEdgeRuleAttribute() {
    	Clazz node1 = new Clazz("node1", null);
    	Clazz node2 = new Clazz("node2", null);
    	node1.getAttributes().add(new Attribute("attr", node2));
        final Edge mould = new Edge(node1, node2, EdgeType.ASSOCIATION);
        Clazz myClazz1 = new Clazz("myClass1", null);
        Clazz myClazz2 = new Clazz("myClass2", null);
    	myClazz1.getAttributes().add(new Attribute("attr", myClazz2));
        final Edge systemEdge = new Edge(myClazz1, myClazz2, EdgeType.ASSOCIATION);
        final EdgeRule attributeRule = new EdgeRule(mould, Scope.ATTRIBUTE, Topic.TYPE, Operator.EXISTS);

        assertTrue(attributeRule.process(systemEdge));
    }


    /**
     * Test the {@link EdgeRule} class for type checking.
     */
    @Test
    public void testEdgeForRelationType() {
        final Edge mould = new Edge(new Clazz("node1", null), new Clazz("node2", null), EdgeType.ASSOCIATION);
        final Edge systemEdge = new Edge(new Clazz("myClass1a", null), new Clazz("myClass2a", null), EdgeType.ASSOCIATION);
        final Edge systemEdge2 = new Edge(new Clazz("myClass1b", null), new Clazz("myClass2b", null), EdgeType.DEPENDENCY);

        final EdgeRule edgeRule = new EdgeRule(mould, Scope.RELATION, Topic.TYPE, Operator.EQUALS);
        assertTrue(edgeRule.process(systemEdge));
        assertFalse(edgeRule.process(systemEdge2));

        final EdgeRule failingTopicRule = new EdgeRule(mould, Scope.RELATION, Topic.CARDINALITY, Operator.EQUALS);
        thrown.expect(RuleException.class);
        thrown.expectMessage("Either one or both cardinalities are not set.");
        failingTopicRule.process(systemEdge);
    }

    /**
     * Test if the {@link EdgeRule} throws an exception when an unexpected {@link Topic} is provided.
     */
    @Test
    public void testEdgeRuleWithUnknownTopic() {
        final Edge mould = new Edge(new Clazz("node1", "node1"), new Clazz("node2", "node2"), EdgeType.ASSOCIATION);
        mould.setCardinalityLeft(1, 1);
        mould.setCardinalityRight(0, Cardinality.UNLIMITED);

        final Edge systemEdge = new Edge(new Clazz("myClass1", "myClass1"), new Clazz("myClass2", "myClass2"), EdgeType.ASSOCIATION);
        systemEdge.setCardinalityLeft(1, 1);
        systemEdge.setCardinalityRight(0, Cardinality.UNLIMITED);
     	
        final EdgeRule failingTopicRule = new EdgeRule(mould, Scope.RELATION, Topic.VISIBILITY, Operator.EQUALS);
        thrown.expect(RuleException.class);
        thrown.expectMessage("Unexpected topic while processing RELATION: VISIBILITY");
        failingTopicRule.process(systemEdge);
    }
    
    /**
     * Test the {@link EdgeRule} class for cardinality checking, existing cardinality and equals.
     * Test exception when one or both cardinalities are not set.
     */
    @Test
    public void testEdgeForCardinality() {
        final Edge mould = new Edge(new Clazz("node1", null), new Clazz("node2", null), EdgeType.ASSOCIATION);
        mould.setCardinalityLeft(1, 1);
        mould.setCardinalityRight(0, Cardinality.UNLIMITED);

        final Edge systemEdge = new Edge(new Clazz("myClass1a", null), new Clazz("myClass2a", null), EdgeType.ASSOCIATION);
        systemEdge.setCardinalityLeft(1, 1);
        systemEdge.setCardinalityRight(0, Cardinality.UNLIMITED);

        final Edge systemEdgeWrongCardinalityLeft = new Edge(new Clazz("myClass1b", null), new Clazz("myClass2b", null), EdgeType.ASSOCIATION);
        systemEdgeWrongCardinalityLeft.setCardinalityLeft(0, 1);
        systemEdgeWrongCardinalityLeft.setCardinalityRight(0, Cardinality.UNLIMITED);

        final Edge systemEdgeWrongCardinalityRight = new Edge(new Clazz("myClass1b", null), new Clazz("myClass2b", null), EdgeType.ASSOCIATION);
        systemEdgeWrongCardinalityRight.setCardinalityLeft(1, 1);
        systemEdgeWrongCardinalityRight.setCardinalityRight(1, 1);

        final Edge systemEdgeWrongCardinalities = new Edge(new Clazz("myClass1b", null), new Clazz("myClass2b", null), EdgeType.ASSOCIATION);
        systemEdgeWrongCardinalities.setCardinalityLeft(0, 1);
        systemEdgeWrongCardinalities.setCardinalityRight(1, Cardinality.UNLIMITED);

          // Cardinalities exist
        final EdgeRule edgeRuleExists = new EdgeRule(mould, Scope.RELATION, Topic.CARDINALITY, Operator.EXISTS);
        assertTrue(edgeRuleExists.process(systemEdge));
        assertTrue(edgeRuleExists.process(systemEdgeWrongCardinalityLeft));
        assertTrue(edgeRuleExists.process(systemEdgeWrongCardinalityRight));

        // CardinalityLeft exist
        final EdgeRule cardLeftExists = new EdgeRule(mould, Scope.RELATION, Topic.CARDINALITY_LEFT, Operator.EXISTS);
        final EdgeRule cardLeftNotExists = new EdgeRule(mould, Scope.RELATION, Topic.CARDINALITY_LEFT, Operator.NOT_EXISTS);
        assertTrue(cardLeftExists.process(systemEdge));
        assertTrue(cardLeftExists.process(systemEdgeWrongCardinalityLeft));
        assertFalse(cardLeftNotExists.process(systemEdgeWrongCardinalityRight));
        
        // CardinalityRight exist
        final EdgeRule cardRightExists = new EdgeRule(mould, Scope.RELATION, Topic.CARDINALITY_RIGHT, Operator.EXISTS);
        final EdgeRule cardRightNotExists = new EdgeRule(mould, Scope.RELATION, Topic.CARDINALITY_RIGHT, Operator.NOT_EXISTS);
        assertTrue(cardRightExists.process(systemEdge));
        assertTrue(cardRightExists.process(systemEdgeWrongCardinalityRight));
        assertFalse(cardRightNotExists.process(systemEdgeWrongCardinalityLeft));

        // Cardinality equals, or not
        final EdgeRule edgeRuleEquals = new EdgeRule(mould, Scope.RELATION, Topic.CARDINALITY, Operator.EQUALS);
        assertTrue(edgeRuleEquals.process(systemEdge));
        assertFalse(edgeRuleEquals.process(systemEdgeWrongCardinalityRight));
        assertFalse(edgeRuleEquals.process(systemEdgeWrongCardinalityLeft));
        assertFalse(edgeRuleEquals.process(systemEdgeWrongCardinalities));
        
        // CardinalityLeft equals
        final EdgeRule cardLeftEquals = new EdgeRule(mould, Scope.RELATION, Topic.CARDINALITY_LEFT, Operator.EQUALS);
        final EdgeRule cardLeftNotEquals = new EdgeRule(mould, Scope.RELATION, Topic.CARDINALITY_LEFT, Operator.NOT_EQUALS);
        assertTrue(cardLeftEquals.process(systemEdge));
        assertFalse(cardLeftEquals.process(systemEdgeWrongCardinalities));
        assertTrue(cardLeftNotEquals.process(systemEdgeWrongCardinalities));
        assertFalse(cardLeftEquals.process(systemEdgeWrongCardinalityLeft));
        assertTrue(cardLeftNotEquals.process(systemEdgeWrongCardinalityLeft));
        assertTrue(cardLeftNotEquals.process(systemEdgeWrongCardinalityLeft));
        assertTrue(cardLeftEquals.process(systemEdgeWrongCardinalityRight));
        
        // CardinalityRight equals
        final EdgeRule cardRightEquals = new EdgeRule(mould, Scope.RELATION, Topic.CARDINALITY_RIGHT, Operator.EQUALS);
        final EdgeRule cardRightNotEquals = new EdgeRule(mould, Scope.RELATION, Topic.CARDINALITY_RIGHT, Operator.NOT_EQUALS);
        assertTrue(cardRightEquals.process(systemEdge));
        assertFalse(cardRightEquals.process(systemEdgeWrongCardinalities));
        assertTrue(cardRightNotEquals.process(systemEdgeWrongCardinalities));
        assertFalse(cardRightEquals.process(systemEdgeWrongCardinalityRight));
        assertTrue(cardRightNotEquals.process(systemEdgeWrongCardinalityRight));
        assertTrue(cardRightNotEquals.process(systemEdgeWrongCardinalityRight));
        assertTrue(cardRightEquals.process(systemEdgeWrongCardinalityLeft));
        
        // Cardinality does not exist
        systemEdgeWrongCardinalities.removeCardinalityLeft();
        assertFalse(edgeRuleExists.process(systemEdgeWrongCardinalities));
        assertTrue(cardRightExists.process(systemEdgeWrongCardinalities));
        assertFalse(cardLeftExists.process(systemEdgeWrongCardinalities));

        // Cardinality cannot equal when one or both are not set.
        final EdgeRule failingCardinalityRule = new EdgeRule(mould, Scope.RELATION, Topic.CARDINALITY_LEFT, Operator.EQUALS);
        thrown.expect(RuleException.class);
        thrown.expectMessage("Left cardinality is not set.");
        failingCardinalityRule.process(systemEdgeWrongCardinalities);
    }

}
