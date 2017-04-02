package nl.ou.dpd.domain.rule;

import nl.ou.dpd.domain.node.Clazz;
import nl.ou.dpd.domain.node.Interface;
import nl.ou.dpd.domain.node.Node;
import nl.ou.dpd.domain.node.Visibility;
import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests the {@link NodeRule} class.
 *
 * @author Martin de Boer
 */
public class NodeRuleTest {

    /**
     * Test the {@link NodeRule} class for node name checking.
     */
    @Test
    public void testNodeForName() {
        final NodeRule nodeRule = new NodeRule(new Interface("Interface"), Topic.TYPE, Target.OBJECT, null);
        assertTrue(nodeRule.process(new Interface("Interface")));
        assertFalse(nodeRule.process(new Interface("OtherInterface")));
    }

    /**
     * Test the {@link NodeRule} class for {@link Topic#TYPE} checking.
     */
    @Test
    public void testNodeForType() {
        final Node clazz = new Clazz("id1", "ClassName");
        final NodeRule clazzRule = new NodeRule(clazz, Topic.TYPE, Target.OBJECT, null);
        assertTrue(clazzRule.process(new Clazz("id2", "ClassName")));
        assertFalse(clazzRule.process(new Interface("id3", "ClassName")));

        final Node inferfaze = new Interface("id1", "InterfaceName");
        final NodeRule interfazeRule = new NodeRule(inferfaze, Topic.TYPE, Target.OBJECT, null);
        assertFalse(interfazeRule.process(new Clazz("id2", "InterfaceName")));
        assertTrue(interfazeRule.process(new Interface("id3", "InterfaceName")));
    }

    /**
     * Test the {@link NodeRule} class for {@link Topic#VISIBILITY} checking.
     */
    @Test
    public void testNodeForVisibility() {
        final Node visibilityNotSetNode = new Clazz("id1", "ClassName");
        final NodeRule visibilityNotSetRule = new NodeRule(visibilityNotSetNode, Topic.VISIBILITY, Target.OBJECT, null);
        assertTrue(visibilityNotSetRule.process(new Clazz("id2", "ClassName", Visibility.PUBLIC, null, false, false, true, false)));
        assertTrue(visibilityNotSetRule.process(new Interface("id3", "ClassName")));
        assertTrue(visibilityNotSetRule.process(new Clazz("id4", "ClassName", Visibility.PROTECTED, null, false, false, true, false)));
        assertTrue(visibilityNotSetRule.process(new Clazz("id5", "ClassName", Visibility.PRIVATE, null, false, false, true, false)));
        assertTrue(visibilityNotSetRule.process(new Clazz("id6", "ClassName", Visibility.PACKAGE, null, false, false, true, false)));

        final Node publicNode = new Clazz("id1", "ClassName", Visibility.PUBLIC, null, null, null, null, null);
        final NodeRule visibilityRule = new NodeRule(publicNode, Topic.VISIBILITY, Target.OBJECT, null);
        assertTrue(visibilityRule.process(new Clazz("id2", "ClassName", Visibility.PUBLIC, null, false, false, true, false)));
        assertTrue(visibilityRule.process(new Interface("id3", "ClassName")));
        assertFalse(visibilityRule.process(new Clazz("id4", "ClassName", Visibility.PROTECTED, null, false, false, true, false)));
        assertFalse(visibilityRule.process(new Clazz("id5", "ClassName", Visibility.PRIVATE, null, false, false, true, false)));
        assertFalse(visibilityRule.process(new Clazz("id6", "ClassName", Visibility.PACKAGE, null, false, false, true, false)));
    }

    /**
     * Test the {@link NodeRule} class for {@link Topic#MODIFIER_ROOT} checking.
     */
    @Test
    public void testNodeForRootModifier() {
        final Node rootModifierNotSetNode = new Clazz("id1", "ClassName", Visibility.PUBLIC, null, null, false, false, false);
        final NodeRule rootModifierNotSetRule = new NodeRule(rootModifierNotSetNode, Topic.MODIFIER_ROOT, Target.OBJECT, null);
        assertTrue(rootModifierNotSetRule.process(new Clazz("id2", "ClassName", Visibility.PRIVATE, null, true, false, false, false)));
        assertTrue(rootModifierNotSetRule.process(new Clazz("id3", "ClassName", Visibility.PRIVATE, null, true, true, false, false)));
        assertTrue(rootModifierNotSetRule.process(new Clazz("id4", "ClassName", Visibility.PRIVATE, null, true, false, true, false)));
        assertTrue(rootModifierNotSetRule.process(new Clazz("id5", "ClassName", Visibility.PRIVATE, null, true, false, false, true)));
        assertTrue(rootModifierNotSetRule.process(new Clazz("id6", "ClassName", Visibility.PRIVATE, null, false, false, false, false)));
        assertTrue(rootModifierNotSetRule.process(new Clazz("id7", "ClassName", Visibility.PRIVATE, null, null, false, false, false)));

        final Node rootNode = new Clazz("id1", "ClassName", Visibility.PUBLIC, null, true, false, false, false);
        final NodeRule rootRule = new NodeRule(rootNode, Topic.MODIFIER_ROOT, Target.OBJECT, null);
        assertTrue(rootRule.process(new Clazz("id2", "ClassName", Visibility.PRIVATE, null, true, false, false, false)));
        assertTrue(rootRule.process(new Clazz("id3", "ClassName", Visibility.PRIVATE, null, true, true, false, false)));
        assertTrue(rootRule.process(new Clazz("id4", "ClassName", Visibility.PRIVATE, null, true, false, true, false)));
        assertTrue(rootRule.process(new Clazz("id5", "ClassName", Visibility.PRIVATE, null, true, false, false, true)));
        assertFalse(rootRule.process(new Clazz("id6", "ClassName", Visibility.PRIVATE, null, false, false, false, false)));
        assertFalse(rootRule.process(new Clazz("id7", "ClassName", Visibility.PRIVATE, null, null, false, false, false)));

    }

    /**
     * Test the {@link NodeRule} class for {@link Topic#MODIFIER_LEAF} checking.
     */
    @Test
    public void testNodeForLeafModifier() {
        final Node leafModifierNotSetNode = new Clazz("id1", "ClassName", Visibility.PUBLIC, null, true, null, false, false);
        final NodeRule leafModifierNotSetRule = new NodeRule(leafModifierNotSetNode, Topic.MODIFIER_LEAF, Target.OBJECT, null);
        assertTrue(leafModifierNotSetRule.process(new Clazz("id2", "ClassName", Visibility.PRIVATE, null, false, true, false, false)));
        assertTrue(leafModifierNotSetRule.process(new Clazz("id3", "ClassName", Visibility.PRIVATE, null, true, true, false, false)));
        assertTrue(leafModifierNotSetRule.process(new Clazz("id4", "ClassName", Visibility.PRIVATE, null, false, true, true, false)));
        assertTrue(leafModifierNotSetRule.process(new Clazz("id5", "ClassName", Visibility.PRIVATE, null, false, true, false, true)));
        assertTrue(leafModifierNotSetRule.process(new Clazz("id6", "ClassName", Visibility.PRIVATE, null, false, false, false, false)));
        assertTrue(leafModifierNotSetRule.process(new Clazz("id7", "ClassName", Visibility.PRIVATE, null, false, null, false, false)));

        final Node leafNode = new Clazz("id1", "ClassName", Visibility.PUBLIC, null, false, true, false, false);
        final NodeRule leafRule = new NodeRule(leafNode, Topic.MODIFIER_LEAF, Target.OBJECT, null);
        assertTrue(leafRule.process(new Clazz("id2", "ClassName", Visibility.PRIVATE, null, false, true, false, false)));
        assertTrue(leafRule.process(new Clazz("id3", "ClassName", Visibility.PRIVATE, null, true, true, false, false)));
        assertTrue(leafRule.process(new Clazz("id4", "ClassName", Visibility.PRIVATE, null, false, true, true, false)));
        assertTrue(leafRule.process(new Clazz("id5", "ClassName", Visibility.PRIVATE, null, false, true, false, true)));
        assertFalse(leafRule.process(new Clazz("id6", "ClassName", Visibility.PRIVATE, null, false, false, false, false)));
        assertFalse(leafRule.process(new Clazz("id7", "ClassName", Visibility.PRIVATE, null, false, null, false, false)));
    }

    /**
     * Test the {@link NodeRule} class for {@link Topic#MODIFIER_ABSTRACT} checking.
     */
    @Test
    public void testNodeForAbstractModifier() {
        final Node abstractModifierNotSetNode = new Clazz("id1", "ClassName", Visibility.PUBLIC, null, true, false, null, false);
        final NodeRule abstractModifierNotSetRule = new NodeRule(abstractModifierNotSetNode, Topic.MODIFIER_ABSTRACT, Target.OBJECT, null);
        assertTrue(abstractModifierNotSetRule.process(new Interface("id2", "ClassName", false, false, false)));
        assertTrue(abstractModifierNotSetRule.process(new Clazz("id3", "ClassName", Visibility.PRIVATE, null, false, false, true, false)));
        assertTrue(abstractModifierNotSetRule.process(new Clazz("id4", "ClassName", Visibility.PRIVATE, null, true, false, true, false)));
        assertTrue(abstractModifierNotSetRule.process(new Clazz("id5", "ClassName", Visibility.PRIVATE, null, false, true, true, false)));
        assertTrue(abstractModifierNotSetRule.process(new Clazz("id6", "ClassName", Visibility.PRIVATE, null, false, false, true, true)));
        assertTrue(abstractModifierNotSetRule.process(new Clazz("id7", "ClassName", Visibility.PRIVATE, null, false, false, false, false)));
        assertTrue(abstractModifierNotSetRule.process(new Clazz("id8", "ClassName", Visibility.PRIVATE, null, false, false, null, false)));

        final Node abstractNode = new Clazz("id1", "ClassName", Visibility.PUBLIC, null, false, false, true, false);
        final NodeRule abstractRule = new NodeRule(abstractNode, Topic.MODIFIER_ABSTRACT, Target.OBJECT, null);
        assertTrue(abstractRule.process(new Interface("id2", "ClassName", false, false, false)));
        assertTrue(abstractRule.process(new Clazz("id3", "ClassName", Visibility.PRIVATE, null, false, false, true, false)));
        assertTrue(abstractRule.process(new Clazz("id4", "ClassName", Visibility.PRIVATE, null, true, false, true, false)));
        assertTrue(abstractRule.process(new Clazz("id5", "ClassName", Visibility.PRIVATE, null, false, true, true, false)));
        assertTrue(abstractRule.process(new Clazz("id6", "ClassName", Visibility.PRIVATE, null, false, false, true, true)));
        assertFalse(abstractRule.process(new Clazz("id7", "ClassName", Visibility.PRIVATE, null, false, false, false, false)));
        assertFalse(abstractRule.process(new Clazz("id8", "ClassName", Visibility.PRIVATE, null, false, false, null, false)));
    }

    /**
     * Test the {@link NodeRule} class for {@link Topic#MODIFIER_ACTIVE} checking.
     */
    @Test
    public void testNodeForActiveModifier() {
        final Node activeModifierNotSetNode = new Clazz("id1", "ClassName", Visibility.PUBLIC, null, null, false, false, null);
        final NodeRule activeModifierNotSetRule = new NodeRule(activeModifierNotSetNode, Topic.MODIFIER_ACTIVE, Target.OBJECT, null);
        assertTrue(activeModifierNotSetRule.process(new Clazz("id2", "ClassName", Visibility.PRIVATE, null, false, false, false, true)));
        assertTrue(activeModifierNotSetRule.process(new Clazz("id3", "ClassName", Visibility.PRIVATE, null, true, false, false, true)));
        assertTrue(activeModifierNotSetRule.process(new Clazz("id4", "ClassName", Visibility.PRIVATE, null, false, true, false, true)));
        assertTrue(activeModifierNotSetRule.process(new Clazz("id5", "ClassName", Visibility.PRIVATE, null, false, false, true, true)));
        assertTrue(activeModifierNotSetRule.process(new Clazz("id6", "ClassName", Visibility.PRIVATE, null, false, false, false, false)));
        assertTrue(activeModifierNotSetRule.process(new Clazz("id7", "ClassName", Visibility.PRIVATE, null, false, false, false, null)));

        // Test active modifier
        final Node activeNode = new Clazz("id1", "ClassName", Visibility.PUBLIC, null, false, false, false, true);
        final NodeRule activeRule = new NodeRule(activeNode, Topic.MODIFIER_ACTIVE, Target.OBJECT, null);
        assertTrue(activeRule.process(new Clazz("id2", "ClassName", Visibility.PRIVATE, null, false, false, false, true)));
        assertTrue(activeRule.process(new Clazz("id3", "ClassName", Visibility.PRIVATE, null, true, false, false, true)));
        assertTrue(activeRule.process(new Clazz("id4", "ClassName", Visibility.PRIVATE, null, false, true, false, true)));
        assertTrue(activeRule.process(new Clazz("id5", "ClassName", Visibility.PRIVATE, null, false, false, true, true)));
        assertFalse(activeRule.process(new Clazz("id6", "ClassName", Visibility.PRIVATE, null, false, false, false, false)));
        assertFalse(activeRule.process(new Clazz("id7", "ClassName", Visibility.PRIVATE, null, false, false, false, null)));
    }

    /**
     * Tests the {@link NodeRule#process(Node)} for attributes.
     */
    @Test
    public void testNodeForAttributes() {
        final NodeRule rule = new NodeRule(Clazz.EMPTY_NODE, Topic.TYPE, Target.ATTRIBUTE, null);
        // TODO: attributes checking is not yet implemented and always returns false.
        assertFalse(rule.process(Clazz.EMPTY_NODE));
    }
}
