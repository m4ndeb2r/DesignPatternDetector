package nl.ou.dpd.domain.rule;

import nl.ou.dpd.domain.node.Attribute;
import nl.ou.dpd.domain.node.Clazz;
import nl.ou.dpd.domain.node.Interface;
import nl.ou.dpd.domain.node.Node;
import nl.ou.dpd.domain.node.NodeType;
import nl.ou.dpd.domain.node.Visibility;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Tests the {@link NodeRule} class.
 *
 * @author Martin de Boer
 */
@RunWith(MockitoJUnitRunner.class)
public class NodeRuleTest {

    @Mock
    private Clazz clazz;
    @Mock
    private Interface interfaze;
    @Mock
    private Node publicNode;
    @Mock
    private Node privateNode;
    @Mock
    private Node protectedNode;
    @Mock
    private Node packageNode;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void initMocks() {
        when(clazz.getType()).thenReturn(NodeType.CLASS);
        when(interfaze.getType()).thenReturn(NodeType.INTERFACE);
        when(interfaze.getVisibility()).thenReturn(Visibility.PUBLIC);

        when(publicNode.getVisibility()).thenReturn(Visibility.PUBLIC);
        when(privateNode.getVisibility()).thenReturn(Visibility.PRIVATE);
        when(protectedNode.getVisibility()).thenReturn(Visibility.PROTECTED);
        when(packageNode.getVisibility()).thenReturn(Visibility.PACKAGE);
    }

    @Test
    public void testConstructorMissingMould() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("All arguments are mandatory.");
        new NodeRule(null, Scope.OBJECT, Topic.TYPE, Operation.EQUALS);
    }

    @Test
    public void testConstructorMissingScope() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("All arguments are mandatory.");
        new NodeRule(new Interface("", ""), null, Topic.TYPE, Operation.EQUALS);
    }

    @Test
    public void testConstructorMissingTopic() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("All arguments are mandatory.");
        new NodeRule(new Interface("", ""), Scope.ATTRIBUTE, null, Operation.EXISTS);
    }

    @Test
    public void testConstructorMissingOperation() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("All arguments are mandatory.");
        new NodeRule(new Interface("", ""), Scope.OBJECT, Topic.TYPE, null);
    }

    @Test
    public void testNodeRuleWithUnknownScope() {
        final NodeRule failingScopeRule = new NodeRule(clazz, Scope.RELATION, Topic.TYPE, Operation.EQUALS);
        thrown.expect(RuleException.class);
        thrown.expectMessage("Unexpected scope: 'RELATION'");
        failingScopeRule.process(new Clazz("id2", "ClassName2"));
    }

    @Test
    public void testNodeRuleWithUnknownTopicCardinality() {
        final NodeRule failingScopeRule = new NodeRule(clazz, Scope.OBJECT, Topic.CARDINALITY, Operation.EQUALS);
        thrown.expect(RuleException.class);
        thrown.expectMessage("Unexpected topic 'CARDINALITY' while processing scope 'OBJECT'.");
        failingScopeRule.process(new Clazz("id2", "ClassName"));
    }

    @Test
    public void testNodeRuleWithUnknownTopicCardinalityLeft() {
        final NodeRule failingScopeRule = new NodeRule(clazz, Scope.OBJECT, Topic.CARDINALITY_LEFT, Operation.EQUALS);
        thrown.expect(RuleException.class);
        thrown.expectMessage("Unexpected topic 'CARDINALITY_LEFT' while processing scope 'OBJECT'.");
        failingScopeRule.process(new Clazz("id2", "ClassName"));
    }

    @Test
    public void testNodeRuleWithUnknownTopicCardinalityRight() {
        final NodeRule failingScopeRule = new NodeRule(clazz, Scope.OBJECT, Topic.CARDINALITY_RIGHT, Operation.EQUALS);
        thrown.expect(RuleException.class);
        thrown.expectMessage("Unexpected topic 'CARDINALITY_RIGHT' while processing scope 'OBJECT'.");
        failingScopeRule.process(new Clazz("id2", "ClassName"));
    }

    /**
     * Test the existence of the scopes in the systemNode (whatever the mould demands).
     */
    @Test
    public void testNodeForTopicExistence() {
        final List<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("Class1", new Clazz("id0a", "Class")));
        attributes.add(new Attribute("Interface1", new Clazz("id0b", "Interface")));

        final Node nothingSetNode = new Clazz("id1a", "ClassName");
        final Node everythingSetNode = new Clazz("id1b", "ClassName", Visibility.PRIVATE, attributes, true, false, false, true);

        //activeModifier
        final NodeRule activeModifierNotSetRule = new NodeRule(nothingSetNode, Scope.OBJECT, Topic.MODIFIER_ACTIVE, Operation.EXISTS);
        final NodeRule activeModifierSetRule = new NodeRule(everythingSetNode, Scope.OBJECT, Topic.MODIFIER_ACTIVE, Operation.EXISTS);
        assertTrue(activeModifierNotSetRule.process(new Clazz("id2", "ClassName", Visibility.PUBLIC, null, false, false, true, false)));
        assertFalse(activeModifierNotSetRule.process(new Clazz("id3", "ClassName")));
        assertTrue(activeModifierSetRule.process(new Clazz("id4", "ClassName", Visibility.PROTECTED, null, true, true, false, true)));
        assertFalse(activeModifierSetRule.process(new Clazz("id5", "ClassName")));

        assertTrue(activeModifierNotSetRule.process(new Interface("id6", "ClassName", true, false, false)));
        assertFalse(activeModifierNotSetRule.process(new Interface("id6", "ClassName")));
    }

    /**
     * Test the {@link NodeRule} class for {@link Topic#TYPE} checking.
     */
    @Test
    public void testNodeRuleForType() {
        final Clazz testClass = new Clazz("id2", "ClassName");
        final Interface testInterface = new Interface("id3", "InterfaceName");

        // Tests with a Clazz
        NodeRule typeRule = new NodeRule(clazz, Scope.OBJECT, Topic.TYPE, Operation.EQUALS);
        assertTrue(typeRule.process(testClass));
        assertFalse(typeRule.process(testInterface));

        typeRule = new NodeRule(clazz, Scope.OBJECT, Topic.TYPE, Operation.NOT_EQUALS);
        assertFalse(typeRule.process(testClass));
        assertTrue(typeRule.process(testInterface));

        typeRule = new NodeRule(clazz, Scope.OBJECT, Topic.TYPE, Operation.EXISTS);
        assertTrue(typeRule.process(testClass));

        typeRule = new NodeRule(clazz, Scope.OBJECT, Topic.TYPE, Operation.NOT_EXISTS);
        assertFalse(typeRule.process(testClass));

        // Tests with an Interface
        typeRule = new NodeRule(interfaze, Scope.OBJECT, Topic.TYPE, Operation.EQUALS);
        assertFalse(typeRule.process(testClass));
        assertTrue(typeRule.process(testInterface));

        typeRule = new NodeRule(interfaze, Scope.OBJECT, Topic.TYPE, Operation.NOT_EQUALS);
        assertTrue(typeRule.process(testClass));
        assertFalse(typeRule.process(testInterface));

        typeRule = new NodeRule(interfaze, Scope.OBJECT, Topic.TYPE, Operation.EXISTS);
        assertTrue(typeRule.process(testInterface));

        typeRule = new NodeRule(interfaze, Scope.OBJECT, Topic.TYPE, Operation.NOT_EXISTS);
        assertFalse(typeRule.process(testInterface));
    }

    /**
     * Test the {@link NodeRule} class for {@link Topic#VISIBILITY} checking.
     */
    @Test
    public void testNodeRuleForVisibility() {
        NodeRule visibilityRule = new NodeRule(publicNode, Scope.OBJECT, Topic.VISIBILITY, Operation.EQUALS);
        assertTrue(visibilityRule.process(publicNode));
        assertTrue(visibilityRule.process(interfaze));
        assertFalse(visibilityRule.process(protectedNode));
        assertFalse(visibilityRule.process(privateNode));
        assertFalse(visibilityRule.process(packageNode));

        visibilityRule = new NodeRule(protectedNode, Scope.OBJECT, Topic.VISIBILITY, Operation.NOT_EQUALS);
        assertTrue(visibilityRule.process(publicNode));
        assertTrue(visibilityRule.process(interfaze));
        assertFalse(visibilityRule.process(protectedNode));
        assertTrue(visibilityRule.process(privateNode));
        assertTrue(visibilityRule.process(packageNode));

        visibilityRule = new NodeRule(privateNode, Scope.OBJECT, Topic.VISIBILITY, Operation.EXISTS);
        assertTrue(visibilityRule.process(publicNode));
        assertTrue(visibilityRule.process(interfaze));
        assertTrue(visibilityRule.process(protectedNode));
        assertTrue(visibilityRule.process(privateNode));
        assertTrue(visibilityRule.process(packageNode));

        visibilityRule = new NodeRule(packageNode, Scope.OBJECT, Topic.VISIBILITY, Operation.NOT_EXISTS);
        assertFalse(visibilityRule.process(publicNode));
        assertFalse(visibilityRule.process(interfaze));
        assertFalse(visibilityRule.process(protectedNode));
        assertFalse(visibilityRule.process(privateNode));
        assertFalse(visibilityRule.process(packageNode));

        // Test situation in which the visibility is not set in the mould, for all operations.
        for (Operation operation : Operation.values()) {
            final NodeRule visibilityNotSetRule = new NodeRule(clazz, Scope.OBJECT, Topic.VISIBILITY, operation);
            thrown.expect(RuleException.class);
            thrown.expect(RuleException.class);
            thrown.expectMessage("Cannot perform rule on topic 'VISIBILITY'. Unable to detect what to check for.");
            visibilityNotSetRule.process(publicNode);
        }
    }

    /**
     * Test the {@link NodeRule} class for {@link Topic#MODIFIER_ROOT} checking.
     */
    @Test
    public void testNodeRuleForRootModifier() {
        when(clazz.isRoot()).thenReturn(true);
        when(publicNode.isRoot()).thenReturn(true);
        when(protectedNode.isRoot()).thenReturn(false);

        NodeRule rootRule = new NodeRule(clazz, Scope.OBJECT, Topic.MODIFIER_ROOT, Operation.EQUALS);
        assertTrue(rootRule.process(publicNode));
        assertFalse(rootRule.process(protectedNode));
        assertFalse(rootRule.process(privateNode));

        rootRule = new NodeRule(clazz, Scope.OBJECT, Topic.MODIFIER_ROOT, Operation.NOT_EQUALS);
        assertFalse(rootRule.process(publicNode));
        assertTrue(rootRule.process(protectedNode));
        assertTrue(rootRule.process(privateNode));

        rootRule = new NodeRule(clazz, Scope.OBJECT, Topic.MODIFIER_ROOT, Operation.EXISTS);
        assertTrue(rootRule.process(publicNode));
        assertTrue(rootRule.process(protectedNode));
        assertTrue(rootRule.process(privateNode));

        rootRule = new NodeRule(clazz, Scope.OBJECT, Topic.MODIFIER_ROOT, Operation.NOT_EXISTS);
        assertFalse(rootRule.process(publicNode));
        assertFalse(rootRule.process(protectedNode));
        assertFalse(rootRule.process(privateNode));

        // Test situation in which root modifier is not set in the mould, for all operations.
        when(interfaze.isRoot()).thenReturn(null);
        for (Operation operation : Operation.values()) {
            rootRule = new NodeRule(interfaze, Scope.OBJECT, Topic.MODIFIER_ROOT, operation);
            thrown.expect(RuleException.class);
            thrown.expectMessage("Cannot perform rule on topic 'MODIFIER_ROOT'. Unable to detect what to check for.");
            rootRule.process(publicNode);
        }
    }

    /**
     * Test the {@link NodeRule} class for {@link Topic#MODIFIER_LEAF} checking.
     */
    @Test
    public void testNodeRuleForLeafModifier() {
        when(clazz.isLeaf()).thenReturn(true);
        when(publicNode.isLeaf()).thenReturn(true);
        when(protectedNode.isLeaf()).thenReturn(false);

        NodeRule leafRule = new NodeRule(clazz, Scope.OBJECT, Topic.MODIFIER_LEAF, Operation.EQUALS);
        assertTrue(leafRule.process(publicNode));
        assertFalse(leafRule.process(protectedNode));
        assertFalse(leafRule.process(privateNode));

        leafRule = new NodeRule(clazz, Scope.OBJECT, Topic.MODIFIER_LEAF, Operation.NOT_EQUALS);
        assertFalse(leafRule.process(publicNode));
        assertTrue(leafRule.process(protectedNode));
        assertTrue(leafRule.process(privateNode));

        leafRule = new NodeRule(clazz, Scope.OBJECT, Topic.MODIFIER_LEAF, Operation.EXISTS);
        assertTrue(leafRule.process(publicNode));
        assertTrue(leafRule.process(protectedNode));
        assertTrue(leafRule.process(privateNode));

        leafRule = new NodeRule(clazz, Scope.OBJECT, Topic.MODIFIER_LEAF, Operation.NOT_EXISTS);
        assertFalse(leafRule.process(publicNode));
        assertFalse(leafRule.process(protectedNode));
        assertFalse(leafRule.process(privateNode));

        // Test situation in which root modifier is not set in the mould, for all operations.
        when(interfaze.isLeaf()).thenReturn(null);
        for (Operation operation : Operation.values()) {
            leafRule = new NodeRule(interfaze, Scope.OBJECT, Topic.MODIFIER_LEAF, operation);
            thrown.expect(RuleException.class);
            thrown.expectMessage("Cannot perform rule on topic 'MODIFIER_LEAF'. Unable to detect what to check for.");
            leafRule.process(publicNode);
        }
    }

    /**
     * Test the {@link NodeRule} class for {@link Topic#MODIFIER_ABSTRACT} checking.
     */
    @Test
    public void testNodeRuleForAbstractModifier() {
        when(clazz.isAbstract()).thenReturn(true);
        when(publicNode.isAbstract()).thenReturn(true);
        when(protectedNode.isAbstract()).thenReturn(false);

        NodeRule abstractRule = new NodeRule(clazz, Scope.OBJECT, Topic.MODIFIER_ABSTRACT, Operation.EQUALS);
        assertTrue(abstractRule.process(publicNode));
        assertFalse(abstractRule.process(protectedNode));
        assertFalse(abstractRule.process(privateNode));

        abstractRule = new NodeRule(clazz, Scope.OBJECT, Topic.MODIFIER_ABSTRACT, Operation.NOT_EQUALS);
        assertFalse(abstractRule.process(publicNode));
        assertTrue(abstractRule.process(protectedNode));
        assertTrue(abstractRule.process(privateNode));

        abstractRule = new NodeRule(clazz, Scope.OBJECT, Topic.MODIFIER_ABSTRACT, Operation.EXISTS);
        assertTrue(abstractRule.process(publicNode));
        assertTrue(abstractRule.process(protectedNode));
        assertTrue(abstractRule.process(privateNode));

        abstractRule = new NodeRule(clazz, Scope.OBJECT, Topic.MODIFIER_ABSTRACT, Operation.NOT_EXISTS);
        assertFalse(abstractRule.process(publicNode));
        assertFalse(abstractRule.process(protectedNode));
        assertFalse(abstractRule.process(privateNode));

        // Test situation in which root modifier is not set in the mould, for all operations.
        when(clazz.isAbstract()).thenReturn(null);
        for (Operation operation : Operation.values()) {
            abstractRule = new NodeRule(clazz, Scope.OBJECT, Topic.MODIFIER_ABSTRACT, operation);
            thrown.expect(RuleException.class);
            thrown.expectMessage("Cannot perform rule on topic 'MODIFIER_ABSTRACT'. Unable to detect what to check for.");
            abstractRule.process(publicNode);
        }
    }

    /**
     * Test the {@link NodeRule} class for {@link Topic#MODIFIER_ACTIVE} checking.
     */
    @Test
    public void testNodeRuleForActiveModifier() {
        when(clazz.isActive()).thenReturn(true);
        when(publicNode.isActive()).thenReturn(true);
        when(protectedNode.isActive()).thenReturn(false);

        NodeRule activeRule = new NodeRule(clazz, Scope.OBJECT, Topic.MODIFIER_ACTIVE, Operation.EQUALS);
        assertTrue(activeRule.process(publicNode));
        assertFalse(activeRule.process(protectedNode));
        assertFalse(activeRule.process(privateNode));

        activeRule = new NodeRule(clazz, Scope.OBJECT, Topic.MODIFIER_ACTIVE, Operation.NOT_EQUALS);
        assertFalse(activeRule.process(publicNode));
        assertTrue(activeRule.process(protectedNode));
        assertTrue(activeRule.process(privateNode));

        activeRule = new NodeRule(clazz, Scope.OBJECT, Topic.MODIFIER_ACTIVE, Operation.EXISTS);
        assertTrue(activeRule.process(publicNode));
        assertTrue(activeRule.process(protectedNode));
        assertTrue(activeRule.process(privateNode));

        activeRule = new NodeRule(clazz, Scope.OBJECT, Topic.MODIFIER_ACTIVE, Operation.NOT_EXISTS);
        assertFalse(activeRule.process(publicNode));
        assertFalse(activeRule.process(protectedNode));
        assertFalse(activeRule.process(privateNode));

        // Test situation in which root modifier is not set in the mould, for all operations.
        when(clazz.isActive()).thenReturn(null);
        for (Operation operation : Operation.values()) {
            activeRule = new NodeRule(clazz, Scope.OBJECT, Topic.MODIFIER_ACTIVE, operation);
            thrown.expect(RuleException.class);
            thrown.expectMessage("Cannot perform rule on topic 'MODIFIER_ACTIVE'. Unable to detect what to check for.");
            activeRule.process(publicNode);
        }
    }

    /**
     * Tests the {@link NodeRule#process(Node)} for attributes.
     */
    @Test
    public void testNodeRuleForAttributes() {
        final NodeRule rule = new NodeRule(Clazz.EMPTY_NODE, Scope.ATTRIBUTE, Topic.TYPE, Operation.EQUALS);
        // TODO: attributes checking is not yet implemented and always throws an exception.
        thrown.expect(RuleException.class);
        thrown.expect(RuleException.class);
        thrown.expectMessage("Unexpected scope: 'ATTRIBUTE'.");
        rule.process(Clazz.EMPTY_NODE);
    }
}
