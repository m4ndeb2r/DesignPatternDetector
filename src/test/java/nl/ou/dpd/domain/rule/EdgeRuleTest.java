package nl.ou.dpd.domain.rule;

import nl.ou.dpd.domain.edge.Cardinality;
import nl.ou.dpd.domain.edge.Edge;
import nl.ou.dpd.domain.edge.EdgeType;
import nl.ou.dpd.domain.node.Attribute;
import nl.ou.dpd.domain.node.Clazz;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Tests the {@link EdgeRule} class.
 *
 * @author Martin de Boer
 * @author Peter Vansweevelt
 */
@RunWith(MockitoJUnitRunner.class)
public class EdgeRuleTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private Edge mould;
    @Mock
    private Clazz leftMouldNode;
    @Mock
    private Clazz rightMouldNode;

    @Mock
    private Edge systemAssociation;
    @Mock
    private Clazz leftSystemAssociationNode;
    @Mock
    private Clazz rightSystemAssociationNode;
    @Mock
    private Attribute leftSystemAssociationNodeAttribute;

    @Mock
    private Edge systemDependency;
    @Mock
    private Clazz leftSystemDependencyNode;
    @Mock
    private Clazz rightSystemDependencyNode;

    @Before
    public void defineDefaultMockBehaviour() {
        when(mould.getLeftNode()).thenReturn(leftMouldNode);
        when(mould.getRightNode()).thenReturn(rightMouldNode);
        when(mould.getRelationType()).thenReturn(EdgeType.ASSOCIATION);

        when(systemAssociation.getLeftNode()).thenReturn(leftSystemAssociationNode);
        when(systemAssociation.getRightNode()).thenReturn(rightSystemAssociationNode);
        when(systemAssociation.getRelationType()).thenReturn(EdgeType.ASSOCIATION);
        List<Attribute> leftSystemNodeAttributes = Collections.singletonList(leftSystemAssociationNodeAttribute);
        when(leftSystemAssociationNode.getAttributes()).thenReturn(leftSystemNodeAttributes);
        when(leftSystemAssociationNodeAttribute.getType()).thenReturn(rightSystemAssociationNode);

        when(systemDependency.getLeftNode()).thenReturn(leftSystemDependencyNode);
        when(systemDependency.getRightNode()).thenReturn(rightSystemDependencyNode);
        when(systemDependency.getRelationType()).thenReturn(EdgeType.DEPENDENCY);
    }

    @Test
    public void testEdgeRuleWithUnknownScope() {
        final EdgeRule unexpectedScopeRule = new EdgeRule(mould, Scope.OBJECT, Topic.TYPE, Operation.EQUALS);
        thrown.expect(RuleException.class);
        thrown.expectMessage("Unexpected scope: 'OBJECT'.");
        unexpectedScopeRule.process(systemAssociation);
    }

    @Test
    public void testEdgeRuleAttributeExists() {
        final EdgeRule attributeRule = new EdgeRule(mould, Scope.ATTRIBUTE, Topic.TYPE, Operation.EXISTS);
        assertTrue(attributeRule.process(systemAssociation));
    }

    @Test
    public void testEdgeRuleForRelationWithUnknownOperation() {
        final EdgeRule attributeRule = new EdgeRule(mould, Scope.RELATION, Topic.TYPE, Operation.EXISTS);
        thrown.expect(RuleException.class);
        thrown.expectMessage("Unexpected operation 'EXISTS' while processing topic 'TYPE'");
        assertTrue(attributeRule.process(systemAssociation));
    }

    @Test
    public void testEdgeRuleForAttributeWithUnknownOperation() {
        final EdgeRule attributeRule = new EdgeRule(mould, Scope.ATTRIBUTE, Topic.TYPE, Operation.EQUALS);
        thrown.expect(RuleException.class);
        thrown.expectMessage("Unexpected operation 'EQUALS' while processing topic 'TYPE'");
        assertTrue(attributeRule.process(systemAssociation));
    }

    @Test
    public void testEdgeRuleForAttributeWithUnknownTopic() {
        final EdgeRule attributeRule = new EdgeRule(mould, Scope.ATTRIBUTE, Topic.CARDINALITY, Operation.EQUALS);
        thrown.expect(RuleException.class);
        thrown.expectMessage("Unexpected topic 'CARDINALITY' while processing scope 'ATTRIBUTE'");
        assertTrue(attributeRule.process(systemAssociation));
    }

    /**
     * Test the {@link EdgeRule} class for correct relation type checking.
     */
    @Test
    public void testEdgeForRelationType() {
        final EdgeRule edgeRule = new EdgeRule(mould, Scope.RELATION, Topic.TYPE, Operation.EQUALS);
        assertTrue(edgeRule.process(systemAssociation));
        assertFalse(edgeRule.process(systemDependency));
    }

    @Test
    public void testMissingLeftCardinalityError() {
        final EdgeRule failingTopicRule = new EdgeRule(mould, Scope.RELATION, Topic.CARDINALITY_LEFT, Operation.EQUALS);
        thrown.expect(RuleException.class);
        thrown.expectMessage("Left cardinality is not set.");
        failingTopicRule.process(systemDependency);
    }

    @Test
    public void testMissingRightCardinalityError() {
        final EdgeRule failingTopicRule = new EdgeRule(mould, Scope.RELATION, Topic.CARDINALITY_RIGHT, Operation.EQUALS);
        thrown.expect(RuleException.class);
        thrown.expectMessage("Right cardinality is not set.");
        failingTopicRule.process(systemDependency);
    }

    @Test
    public void testEdgeRuleWithLeftCardinalityEqual() {
        when(mould.getCardinalityLeft()).thenReturn(Cardinality.valueOf("1"));
        when(systemAssociation.getCardinalityLeft()).thenReturn(Cardinality.valueOf("1"));
        final EdgeRule cardinalityEqualsRule = new EdgeRule(mould, Scope.RELATION, Topic.CARDINALITY_LEFT, Operation.EQUALS);
        assertTrue(cardinalityEqualsRule.process(systemAssociation));
    }

    @Test
    public void testEdgeRuleWithLeftCardinalityNotEqual() {
        when(mould.getCardinalityLeft()).thenReturn(Cardinality.valueOf("*"));
        when(systemAssociation.getCardinalityLeft()).thenReturn(Cardinality.valueOf("1"));

        final EdgeRule cardinalityExistsRule = new EdgeRule(mould, Scope.RELATION, Topic.CARDINALITY_LEFT, Operation.EXISTS);
        final EdgeRule cardinalityNotExistsRule = new EdgeRule(mould, Scope.RELATION, Topic.CARDINALITY_LEFT, Operation.NOT_EXISTS);
        final EdgeRule cardinalityEqualsRule = new EdgeRule(mould, Scope.RELATION, Topic.CARDINALITY_LEFT, Operation.EQUALS);
        final EdgeRule cardinalityNotEqualsRule = new EdgeRule(mould, Scope.RELATION, Topic.CARDINALITY_LEFT, Operation.NOT_EQUALS);

        assertTrue(cardinalityExistsRule.process(systemAssociation));
        assertFalse(cardinalityNotExistsRule.process(systemAssociation));
        assertFalse(cardinalityEqualsRule.process(systemAssociation));
        assertTrue(cardinalityNotEqualsRule.process(systemAssociation));
    }

    @Test
    public void testEdgeRuleWithRightCardinalityEqual() {
        when(mould.getCardinalityRight()).thenReturn(Cardinality.valueOf("0..*"));
        when(systemAssociation.getCardinalityRight()).thenReturn(Cardinality.valueOf("0..*"));
        final EdgeRule cardinalityRule = new EdgeRule(mould, Scope.RELATION, Topic.CARDINALITY_RIGHT, Operation.EQUALS);
        assertTrue(cardinalityRule.process(systemAssociation));
    }

    @Test
    public void testEdgeRuleWithRightCardinalityNotEqual() {
        when(mould.getCardinalityRight()).thenReturn(Cardinality.valueOf("0..*"));
        when(systemAssociation.getCardinalityRight()).thenReturn(Cardinality.valueOf("0..5"));

        final EdgeRule cardinalityExistsRule = new EdgeRule(mould, Scope.RELATION, Topic.CARDINALITY_RIGHT, Operation.EXISTS);
        final EdgeRule cardinalityNotExistsRule = new EdgeRule(mould, Scope.RELATION, Topic.CARDINALITY_RIGHT, Operation.NOT_EXISTS);
        final EdgeRule cardinalityEqualsRule = new EdgeRule(mould, Scope.RELATION, Topic.CARDINALITY_RIGHT, Operation.EQUALS);
        final EdgeRule cardinalityNotEqualsRule = new EdgeRule(mould, Scope.RELATION, Topic.CARDINALITY_RIGHT, Operation.NOT_EQUALS);

        assertTrue(cardinalityExistsRule.process(systemAssociation));
        assertFalse(cardinalityNotExistsRule.process(systemAssociation));
        assertFalse(cardinalityEqualsRule.process(systemAssociation));
        assertTrue(cardinalityNotEqualsRule.process(systemAssociation));
    }

    @Test
    public void testEdgeRuleWithCardinalityOkay() {
        when(mould.getCardinalityLeft()).thenReturn(Cardinality.valueOf("1"));
        when(mould.getCardinalityRight()).thenReturn(Cardinality.valueOf("0..*"));
        when(systemAssociation.getCardinalityLeft()).thenReturn(Cardinality.valueOf("1"));
        when(systemAssociation.getCardinalityRight()).thenReturn(Cardinality.valueOf("0..*"));
        final EdgeRule cardinalityRule = new EdgeRule(mould, Scope.RELATION, Topic.CARDINALITY, Operation.EQUALS);
        assertTrue(cardinalityRule.process(systemAssociation));
    }

    @Test
    public void testEdgeRuleWithCardinalityNotEqual() {
        when(mould.getCardinalityLeft()).thenReturn(Cardinality.valueOf("1"));
        when(mould.getCardinalityRight()).thenReturn(Cardinality.valueOf("0..*"));
        when(systemAssociation.getCardinalityLeft()).thenReturn(Cardinality.valueOf("1"));
        when(systemAssociation.getCardinalityRight()).thenReturn(Cardinality.valueOf("0..5"));

        final EdgeRule cardinalityExistsRule = new EdgeRule(mould, Scope.RELATION, Topic.CARDINALITY, Operation.EXISTS);
        final EdgeRule cardinalityNotExistsRule = new EdgeRule(mould, Scope.RELATION, Topic.CARDINALITY, Operation.NOT_EXISTS);
        final EdgeRule cardinalityEqualsRule = new EdgeRule(mould, Scope.RELATION, Topic.CARDINALITY, Operation.EQUALS);
        final EdgeRule cardinalityNotEqualsRule = new EdgeRule(mould, Scope.RELATION, Topic.CARDINALITY, Operation.NOT_EQUALS);

        assertTrue(cardinalityExistsRule.process(systemAssociation));
        assertFalse(cardinalityNotExistsRule.process(systemAssociation));
        assertFalse(cardinalityEqualsRule.process(systemAssociation));
        assertTrue(cardinalityNotEqualsRule.process(systemAssociation));
    }

    @Test
    public void testCardinalitiesExist() {
        final EdgeRule cardinalityExistsRule = new EdgeRule(mould, Scope.RELATION, Topic.CARDINALITY, Operation.EXISTS);
        assertFalse(cardinalityExistsRule.process(systemDependency));
    }

    @Test
    public void testCardinalitiesNotExist() {
        final EdgeRule cardinalityNotExistsRule = new EdgeRule(mould, Scope.RELATION, Topic.CARDINALITY, Operation.NOT_EXISTS);
        assertTrue(cardinalityNotExistsRule.process(systemDependency));
    }

    @Test
    public void testMissingCardinalitiesError() {
        final EdgeRule failingTopicRule = new EdgeRule(mould, Scope.RELATION, Topic.CARDINALITY, Operation.EQUALS);
        thrown.expect(RuleException.class);
        thrown.expectMessage("Either one or both cardinalities are not set.");
        failingTopicRule.process(systemDependency);
    }

    @Test
    public void testEdgeRuleWithUnknownTopic() {
        final EdgeRule failingTopicRule = new EdgeRule(mould, Scope.RELATION, Topic.VISIBILITY, Operation.EQUALS);
        thrown.expect(RuleException.class);
        thrown.expectMessage("Unexpected topic 'VISIBILITY' while processing scope 'RELATION'.");
        failingTopicRule.process(systemAssociation);
    }
    
}
