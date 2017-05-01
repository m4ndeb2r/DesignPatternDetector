package nl.ou.dpd.domain.rule;

import nl.ou.dpd.domain.edge.Cardinality;
import nl.ou.dpd.domain.edge.Edge;
import nl.ou.dpd.domain.edge.EdgeType;
import nl.ou.dpd.domain.node.Clazz;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests the {@link EdgeRule} class.
 *
 * @author Martin de Boer
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
        final Edge mould = new Edge(new Clazz("node1"), new Clazz("node2"), EdgeType.ASSOCIATION);
        final Edge systemEdge = new Edge(new Clazz("myClass1"), new Clazz("myClass2"), EdgeType.ASSOCIATION);
        final EdgeRule failingTargetRule = new EdgeRule(mould, Scope.ATTRIBUTE, Topic.TYPE, Operator.EQUALS);

        thrown.expect(RuleException.class);
        thrown.expectMessage("Unexpected operator while processing ATTRIBUTE.TYPE: EQUALS");
        failingTargetRule.process(systemEdge);
    }

    /**
     * Test the {@link EdgeRule} class for type checking.
     */
    @Test
    public void testEdgeForRelationType() {
        final Edge mould = new Edge(new Clazz("node1"), new Clazz("node2"), EdgeType.ASSOCIATION);
        final Edge systemEdge = new Edge(new Clazz("myClass1a"), new Clazz("myClass2a"), EdgeType.ASSOCIATION);
        final Edge systemEdge2 = new Edge(new Clazz("myClass1b"), new Clazz("myClass2b"), EdgeType.DEPENDENCY);

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
        final Edge mould = new Edge(new Clazz("node1"), new Clazz("node2"), EdgeType.ASSOCIATION);
        mould.setCardinalityLeft(1, 1);
        mould.setCardinalityRight(0, Cardinality.UNLIMITED);

        final Edge systemEdge = new Edge(new Clazz("myClass1"), new Clazz("myClass2"), EdgeType.ASSOCIATION);
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
        final Edge mould = new Edge(new Clazz("node1"), new Clazz("node2"), EdgeType.ASSOCIATION);
        mould.setCardinalityLeft(1, 1);
        mould.setCardinalityRight(0, Cardinality.UNLIMITED);

        final Edge systemEdge = new Edge(new Clazz("myClass1a"), new Clazz("myClass2a"), EdgeType.ASSOCIATION);
        systemEdge.setCardinalityLeft(1, 1);
        systemEdge.setCardinalityRight(0, Cardinality.UNLIMITED);

        final Edge systemEdge2 = new Edge(new Clazz("myClass1b"), new Clazz("myClass2b"), EdgeType.ASSOCIATION);
        systemEdge2.setCardinalityLeft(1, 1);
        systemEdge2.setCardinalityRight(1, Cardinality.UNLIMITED);

        // Cardinalities exist
        final EdgeRule edgeRuleExists = new EdgeRule(mould, Scope.RELATION, Topic.CARDINALITY, Operator.EXISTS);
        assertTrue(edgeRuleExists.process(systemEdge));
        assertTrue(edgeRuleExists.process(systemEdge2));

        // Cardinality equals, or not
        final EdgeRule edgeRuleEquals = new EdgeRule(mould, Scope.RELATION, Topic.CARDINALITY, Operator.EQUALS);
        assertTrue(edgeRuleEquals.process(systemEdge));
        assertFalse(edgeRuleEquals.process(systemEdge2));

        // Cardinality does not exist
        systemEdge2.removeCardinalityLeft();
        assertFalse(edgeRuleExists.process(systemEdge2));

        // Cardinality cannot equal when one or both are not set.
        final EdgeRule failingCardinalityRule = new EdgeRule(mould, Scope.RELATION, Topic.CARDINALITY, Operator.EQUALS);
        thrown.expect(RuleException.class);
        thrown.expectMessage("Either one or both cardinalities are not set.");
        failingCardinalityRule.process(systemEdge2);
    }

}
