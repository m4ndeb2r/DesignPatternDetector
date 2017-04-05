package nl.ou.dpd.domain.rule;

import nl.ou.dpd.domain.edge.Edge;
import nl.ou.dpd.domain.edge.EdgeType;
import nl.ou.dpd.domain.node.Attribute;
import nl.ou.dpd.domain.node.Clazz;
import nl.ou.dpd.domain.node.Interface;
import nl.ou.dpd.domain.node.Node;
import nl.ou.dpd.domain.node.Visibility;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;

/**
 * Tests the {@link NodeRule} class.
 *
 * @author Martin de Boer
 */
public class NodeRuleTest {

    /**
     * Exception rule.
     */
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    /**
     * Test the {@link NodeRule} class for node name checking.
     */
    
    //this checking has been moved to the condition class
/*    @Test
    public void testNodeForName() {
        final NodeRule nodeRule = new NodeRule(new Interface("id1", "Interface"), Topic.TYPE, Target.OBJECT, Operator.EQUALS);
        assertTrue(nodeRule.process(new Interface("id2", "Interface")));
        assertFalse(nodeRule.process(new Interface("id3", "OtherInterface")));
    }
*/
    /**
     * Test if the {@link NodeRule} throws an exception when an unexpected {@link Target} is provided.
     */
    @Test
    public void testNodeRuleWithUnknownTarget() {
       final Node nothingSetNode = new Clazz("id1", "ClassName");
       final NodeRule failingTargetRule = new NodeRule(nothingSetNode, Topic.TYPE, Target.RELATION, null);

        thrown.expect(RuleException.class);
        thrown.expectMessage("Unexpected target: RELATION");
        failingTargetRule.process(new Clazz("id2", "ClassName"));
    }
 
    /**
     * Test if the {@link NodeRule} throws an exception when an unexpected {@link Topic} is provided.
     */
    @Test
    public void testNodeRuleWithUnknownTopic() {
        final Node everythingSetNode = new Clazz("id1", "ClassName", Visibility.PRIVATE, null, true, false, false, true);
        final NodeRule failingTargetRule = new NodeRule(everythingSetNode, Topic.CARDINALITY, Target.OBJECT, Operator.EQUALS);

         thrown.expect(RuleException.class);
         thrown.expectMessage("Unexpected topic while processing OBJECT target: CARDINALITY");
         failingTargetRule.process(new Clazz("id2", "ClassName"));
     }
   
    /**
     * Test if the {@link NodeRule} throws an exception when {@link Topic} VISIBILITY is not set.
     */
    @Test
    public void testNodeRuleWithVisibilityNotSet() {
        final Node everythingSetNode = new Clazz("id1", "ClassName", Visibility.PUBLIC, null, true, false, false, true);
        final NodeRule failingTargetRule = new NodeRule(everythingSetNode, Topic.VISIBILITY, Target.OBJECT, Operator.EQUALS);

         thrown.expect(RuleException.class);
         thrown.expectMessage("Unexpected error. The topic VISIBILITY is not set.");
         failingTargetRule.process(new Clazz("id2", "ClassName", null, null, false, false, true, false));
    }
    
    /**
     * Test if the {@link NodeRule} throws an exception when {@link Topic} MODIFIER_ROOT is not set.
     */
    @Test
    public void testNodeRuleWithRootModifierNotSet() {
        final Node everythingSetNode = new Clazz("id1", "ClassName", Visibility.PUBLIC, null, true, false, false, true);
        final NodeRule failingTargetRule = new NodeRule(everythingSetNode, Topic.MODIFIER_ROOT, Target.OBJECT, Operator.EQUALS);

         thrown.expect(RuleException.class);
         thrown.expectMessage("Unexpected error. The topic MODIFIER_ROOT is not set.");
         failingTargetRule.process(new Clazz("id2", "ClassName", Visibility.PUBLIC, null, null, false, true, false));
    }

    /**
     * Test if the {@link NodeRule} throws an exception when {@link Topic} MODIFIER_LEAF is not set.
     */
    @Test
    public void testNodeRuleWithLeafModifierNotSet() {
        final Node everythingSetNode = new Clazz("id1", "ClassName", Visibility.PUBLIC, null, true, false, false, true);
        final NodeRule failingTargetRule = new NodeRule(everythingSetNode, Topic.MODIFIER_LEAF, Target.OBJECT, Operator.EQUALS);

         thrown.expect(RuleException.class);
         thrown.expectMessage("Unexpected error. The topic MODIFIER_LEAF is not set.");
         failingTargetRule.process(new Clazz("id2", "ClassName", Visibility.PUBLIC, null, true, null, true, false));
    }

    /**
     * Test if the {@link NodeRule} throws an exception when {@link Topic} MODIFIER_ABSTRACT is not set.
     */
    @Test
    public void testNodeRuleWithAbstractModifierNotSet() {
        final Node everythingSetNode = new Clazz("id1", "ClassName", Visibility.PUBLIC, null, true, false, false, true);
        final NodeRule failingTargetRule = new NodeRule(everythingSetNode, Topic.MODIFIER_ABSTRACT, Target.OBJECT, Operator.EQUALS);

         thrown.expect(RuleException.class);
         thrown.expectMessage("Unexpected error. The topic MODIFIER_ABSTRACT is not set.");
         failingTargetRule.process(new Clazz("id2", "ClassName", Visibility.PUBLIC, null, true, false, null, false));
    }

    /**
     * Test if the {@link NodeRule} throws an exception when {@link Topic} MODIFIER_ACTIVE is not set.
     */
    @Test
    public void testNodeRuleWithActiveModifierNotSet() {
        final Node everythingSetNode = new Clazz("id1", "ClassName", Visibility.PUBLIC, null, true, false, false, true);
        final NodeRule failingTargetRule = new NodeRule(everythingSetNode, Topic.MODIFIER_ACTIVE, Target.OBJECT, Operator.EQUALS);

         thrown.expect(RuleException.class);
         thrown.expectMessage("Unexpected error. The topic MODIFIER_ACTIVE is not set.");
         failingTargetRule.process(new Clazz("id2", "ClassName", Visibility.PUBLIC, null, true, false, false, null));
    }

   /**
     * Test the existence of the targets in the systemNode (whatever the rulenode demands).
     */
    @Test
    public void testNodeForTopicExistence() {
    	final List<Attribute> attributes = new ArrayList<Attribute>();
    	attributes.add(new Attribute("Class1", new Clazz("id0a", "Class")));
    	attributes.add(new Attribute("Interface1", new Clazz("id0b", "Interface")));
    	
        final Node nothingSetNode = new Clazz("id1a", "ClassName");
        final Node everythingSetNode = new Clazz("id1b", "ClassName", Visibility.PRIVATE, attributes, true, false, false, true);

        //type: is always set by Constructor
        final NodeRule typeNotSetRule = new NodeRule(nothingSetNode, Topic.TYPE, Target.OBJECT, Operator.EXISTS);
        final NodeRule typeSetRule = new NodeRule(everythingSetNode, Topic.TYPE, Target.OBJECT, Operator.EXISTS);
        assertTrue(typeNotSetRule.process(new Clazz("id2", "ClassName", Visibility.PUBLIC, null, false, false, true, false)));
        assertTrue(typeNotSetRule.process(new Clazz("id3", "ClassName", null, null, false, false, true, false)));
        assertTrue(typeSetRule.process(new Clazz("id4", "ClassName", Visibility.PROTECTED, null, false, false, true, false)));
        assertTrue(typeSetRule.process(new Clazz("id5", "ClassName", null, null, false, false, true, false)));
        assertTrue(typeNotSetRule.process(new Interface("id6", "ClassName")));
        
        final NodeRule notTypeNotSetRule = new NodeRule(everythingSetNode, Topic.TYPE, Target.OBJECT, Operator.EXISTS, true);
        assertFalse(notTypeNotSetRule.process(new Clazz("id2", "ClassName", Visibility.PUBLIC, null, false, false, true, false)));
        assertFalse(notTypeNotSetRule.process(new Clazz("id3", "ClassName", null, null, false, false, true, false)));
        assertFalse(notTypeNotSetRule.process(new Clazz("id4", "ClassName", Visibility.PROTECTED, null, false, false, true, false)));
        assertFalse(notTypeNotSetRule.process(new Clazz("id5", "ClassName", null, null, false, false, true, false)));
        assertFalse(notTypeNotSetRule.process(new Interface("id6", "ClassName")));
                
        //visibility
        final NodeRule visibilityNotSetRule = new NodeRule(nothingSetNode, Topic.VISIBILITY, Target.OBJECT, Operator.EXISTS);
        final NodeRule visibilitySetRule = new NodeRule(everythingSetNode, Topic.VISIBILITY, Target.OBJECT, Operator.EXISTS);
        assertTrue(visibilityNotSetRule.process(new Clazz("id2", "ClassName", Visibility.PUBLIC, null, false, false, true, false)));
        assertFalse(visibilityNotSetRule.process(new Clazz("id3", "ClassName", null, null, null, null, null, null)));
        assertTrue(visibilitySetRule.process(new Clazz("id4", "ClassName", Visibility.PROTECTED, null, true, true, false, true)));
        assertFalse(visibilitySetRule.process(new Clazz("id5", "ClassName", null, null, null, null, null, null)));
        //visibility is always set (public) in an interface 
        assertTrue(visibilityNotSetRule.process(new Interface("id6", "ClassName", true, false, false)));
        assertTrue(visibilityNotSetRule.process(new Interface("id6", "ClassName")));

        //rootModifier
        final NodeRule rootModifierNotSetRule = new NodeRule(nothingSetNode, Topic.MODIFIER_ROOT, Target.OBJECT, Operator.EXISTS);
        final NodeRule rootModifierSetRule = new NodeRule(everythingSetNode, Topic.MODIFIER_ROOT, Target.OBJECT, Operator.EXISTS);
        assertTrue(rootModifierNotSetRule.process(new Clazz("id2", "ClassName", Visibility.PUBLIC, null, false, false, true, false)));
        assertFalse(rootModifierNotSetRule.process(new Clazz("id3", "ClassName")));
        assertTrue(rootModifierSetRule.process(new Clazz("id4", "ClassName", Visibility.PROTECTED, null, true, true, false, true)));
        assertFalse(rootModifierSetRule.process(new Clazz("id5", "ClassName")));

        assertTrue(rootModifierNotSetRule.process(new Interface("id6", "ClassName", true, false, false)));
        assertFalse(rootModifierNotSetRule.process(new Interface("id6", "ClassName")));

        //leafModifier
        final NodeRule leafModifierNotSetRule = new NodeRule(nothingSetNode, Topic.MODIFIER_LEAF, Target.OBJECT, Operator.EXISTS);
        final NodeRule leafModifierSetRule = new NodeRule(everythingSetNode, Topic.MODIFIER_LEAF, Target.OBJECT, Operator.EXISTS);
        assertTrue(leafModifierNotSetRule.process(new Clazz("id2", "ClassName", Visibility.PUBLIC, null, false, false, true, false)));
        assertFalse(leafModifierNotSetRule.process(new Clazz("id3", "ClassName")));
        assertTrue(leafModifierSetRule.process(new Clazz("id4", "ClassName", Visibility.PROTECTED, null, true, true, false, true)));
        assertFalse(leafModifierSetRule.process(new Clazz("id5", "ClassName")));

        assertTrue(leafModifierNotSetRule.process(new Interface("id6", "ClassName", true, false, false)));
        assertFalse(leafModifierNotSetRule.process(new Interface("id6", "ClassName")));

        //abstractModifier
        final NodeRule abstractModifierNotSetRule = new NodeRule(nothingSetNode, Topic.MODIFIER_ABSTRACT, Target.OBJECT, Operator.EXISTS);
        final NodeRule abstractModifierSetRule = new NodeRule(everythingSetNode, Topic.MODIFIER_ABSTRACT, Target.OBJECT, Operator.EXISTS);
        assertTrue(abstractModifierNotSetRule.process(new Clazz("id2", "ClassName", Visibility.PUBLIC, null, false, false, true, false)));
        assertFalse(abstractModifierNotSetRule.process(new Clazz("id3", "ClassName")));
        assertTrue(abstractModifierSetRule.process(new Clazz("id4", "ClassName", Visibility.PROTECTED, null, true, true, false, true)));
        assertFalse(abstractModifierSetRule.process(new Clazz("id5", "ClassName")));
        //abstractModifier is always set true in an interface 
        assertTrue(abstractModifierNotSetRule.process(new Interface("id6", "ClassName", true, false, false)));
        assertTrue(abstractModifierNotSetRule.process(new Interface("id6", "ClassName")));

        //activeModifier
        final NodeRule activeModifierNotSetRule = new NodeRule(nothingSetNode, Topic.MODIFIER_ACTIVE, Target.OBJECT, Operator.EXISTS);
        final NodeRule activeModifierSetRule = new NodeRule(everythingSetNode, Topic.MODIFIER_ACTIVE, Target.OBJECT, Operator.EXISTS);
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
    public void testNodeForType() {
        final Node clazz = new Clazz("id1", "ClassName");
        final NodeRule clazzRule = new NodeRule(clazz, Topic.TYPE, Target.OBJECT, Operator.EQUALS);
        assertTrue(clazzRule.process(new Clazz("id2", "ClassName")));
        assertFalse(clazzRule.process(new Interface("id3", "ClassName")));

        final Node inferfaze = new Interface("id1", "InterfaceName");
        final NodeRule interfazeRule = new NodeRule(inferfaze, Topic.TYPE, Target.OBJECT, Operator.EQUALS);
        assertFalse(interfazeRule.process(new Clazz("id2", "ClassName")));
        assertTrue(interfazeRule.process(new Interface("id3", "InterfaceName")));
    }

   /**
     * Test the {@link NodeRule} class for {@link Topic#VISIBILITY} checking.
     */
    @Test
    public void testNodeForVisibility() {
        final Node visibilityNotSetNode = new Clazz("id1", "ClassName");
        final NodeRule visibilityNotSetRule = new NodeRule(visibilityNotSetNode, Topic.VISIBILITY, Target.OBJECT, Operator.EQUALS);
        assertTrue(visibilityNotSetRule.process(new Clazz("id2", "ClassName", Visibility.PUBLIC, null, false, false, true, false)));
        assertTrue(visibilityNotSetRule.process(new Interface("id3", "ClassName")));
        assertTrue(visibilityNotSetRule.process(new Clazz("id4", "ClassName", Visibility.PROTECTED, null, false, false, true, false)));
        assertTrue(visibilityNotSetRule.process(new Clazz("id5", "ClassName", Visibility.PRIVATE, null, false, false, true, false)));
        assertTrue(visibilityNotSetRule.process(new Clazz("id6", "ClassName", Visibility.PACKAGE, null, false, false, true, false)));

        final Node publicNode = new Clazz("id1", "ClassName", Visibility.PUBLIC, null, null, null, null, null);
        final NodeRule visibilityRule = new NodeRule(publicNode, Topic.VISIBILITY, Target.OBJECT, Operator.EQUALS);
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
        final NodeRule rootModifierNotSetRule = new NodeRule(rootModifierNotSetNode, Topic.MODIFIER_ROOT, Target.OBJECT, Operator.EQUALS);
        assertTrue(rootModifierNotSetRule.process(new Clazz("id2", "ClassName", Visibility.PRIVATE, null, true, false, false, false)));
        assertTrue(rootModifierNotSetRule.process(new Clazz("id3", "ClassName", Visibility.PRIVATE, null, true, true, false, false)));
        assertTrue(rootModifierNotSetRule.process(new Clazz("id4", "ClassName", Visibility.PRIVATE, null, true, false, true, false)));
        assertTrue(rootModifierNotSetRule.process(new Clazz("id5", "ClassName", Visibility.PRIVATE, null, true, false, false, true)));
        assertTrue(rootModifierNotSetRule.process(new Clazz("id6", "ClassName", Visibility.PRIVATE, null, false, false, false, false)));

        final Node rootNode = new Clazz("id1", "ClassName", Visibility.PUBLIC, null, true, false, false, false);
        final NodeRule rootRule = new NodeRule(rootNode, Topic.MODIFIER_ROOT, Target.OBJECT, Operator.EQUALS);
        assertTrue(rootRule.process(new Clazz("id2", "ClassName", Visibility.PRIVATE, null, true, false, false, false)));
        assertTrue(rootRule.process(new Clazz("id3", "ClassName", Visibility.PRIVATE, null, true, true, false, false)));
        assertTrue(rootRule.process(new Clazz("id4", "ClassName", Visibility.PRIVATE, null, true, false, true, false)));
        assertTrue(rootRule.process(new Clazz("id5", "ClassName", Visibility.PRIVATE, null, true, false, false, true)));
        assertFalse(rootRule.process(new Clazz("id6", "ClassName", Visibility.PRIVATE, null, false, false, false, false)));
    }

    /**
     * Test the {@link NodeRule} class for {@link Topic#MODIFIER_LEAF} checking.
     */
    @Test
    public void testNodeForLeafModifier() {
        final Node leafModifierNotSetNode = new Clazz("id1", "ClassName", Visibility.PUBLIC, null, true, null, false, false);
        final NodeRule leafModifierNotSetRule = new NodeRule(leafModifierNotSetNode, Topic.MODIFIER_LEAF, Target.OBJECT, Operator.EQUALS);
        assertTrue(leafModifierNotSetRule.process(new Clazz("id2", "ClassName", Visibility.PRIVATE, null, false, true, false, false)));
        assertTrue(leafModifierNotSetRule.process(new Clazz("id3", "ClassName", Visibility.PRIVATE, null, true, true, false, false)));
        assertTrue(leafModifierNotSetRule.process(new Clazz("id4", "ClassName", Visibility.PRIVATE, null, false, true, true, false)));
        assertTrue(leafModifierNotSetRule.process(new Clazz("id5", "ClassName", Visibility.PRIVATE, null, false, true, false, true)));
        assertTrue(leafModifierNotSetRule.process(new Clazz("id6", "ClassName", Visibility.PRIVATE, null, false, false, false, false)));
 
        final Node leafNode = new Clazz("id1", "ClassName", Visibility.PUBLIC, null, false, true, false, false);
        final NodeRule leafRule = new NodeRule(leafNode, Topic.MODIFIER_LEAF, Target.OBJECT, Operator.EQUALS);
        assertTrue(leafRule.process(new Clazz("id2", "ClassName", Visibility.PRIVATE, null, false, true, false, false)));
        assertTrue(leafRule.process(new Clazz("id3", "ClassName", Visibility.PRIVATE, null, true, true, false, false)));
        assertTrue(leafRule.process(new Clazz("id4", "ClassName", Visibility.PRIVATE, null, false, true, true, false)));
        assertTrue(leafRule.process(new Clazz("id5", "ClassName", Visibility.PRIVATE, null, false, true, false, true)));
        assertFalse(leafRule.process(new Clazz("id6", "ClassName", Visibility.PRIVATE, null, false, false, false, false)));
    }

    /**
     * Test the {@link NodeRule} class for {@link Topic#MODIFIER_ABSTRACT} checking.
     */
    @Test
    public void testNodeForAbstractModifier() {
        final Node abstractModifierNotSetNode = new Clazz("id1", "ClassName", Visibility.PUBLIC, null, true, false, null, false);
        final NodeRule abstractModifierNotSetRule = new NodeRule(abstractModifierNotSetNode, Topic.MODIFIER_ABSTRACT, Target.OBJECT, Operator.EQUALS);
        assertTrue(abstractModifierNotSetRule.process(new Interface("id2", "ClassName", false, false, false)));
        assertTrue(abstractModifierNotSetRule.process(new Clazz("id3", "ClassName", Visibility.PRIVATE, null, false, false, true, false)));
        assertTrue(abstractModifierNotSetRule.process(new Clazz("id4", "ClassName", Visibility.PRIVATE, null, true, false, true, false)));
        assertTrue(abstractModifierNotSetRule.process(new Clazz("id5", "ClassName", Visibility.PRIVATE, null, false, true, true, false)));
        assertTrue(abstractModifierNotSetRule.process(new Clazz("id6", "ClassName", Visibility.PRIVATE, null, false, false, true, true)));
        assertTrue(abstractModifierNotSetRule.process(new Clazz("id7", "ClassName", Visibility.PRIVATE, null, false, false, false, false)));
 
        final Node abstractNode = new Clazz("id1", "ClassName", Visibility.PUBLIC, null, false, false, true, false);
        final NodeRule abstractRule = new NodeRule(abstractNode, Topic.MODIFIER_ABSTRACT, Target.OBJECT, Operator.EQUALS);
        assertTrue(abstractRule.process(new Interface("id2", "ClassName", false, false, false)));
        assertTrue(abstractRule.process(new Clazz("id3", "ClassName", Visibility.PRIVATE, null, false, false, true, false)));
        assertTrue(abstractRule.process(new Clazz("id4", "ClassName", Visibility.PRIVATE, null, true, false, true, false)));
        assertTrue(abstractRule.process(new Clazz("id5", "ClassName", Visibility.PRIVATE, null, false, true, true, false)));
        assertTrue(abstractRule.process(new Clazz("id6", "ClassName", Visibility.PRIVATE, null, false, false, true, true)));
        assertFalse(abstractRule.process(new Clazz("id7", "ClassName", Visibility.PRIVATE, null, false, false, false, false)));
    }

    /**
     * Test the {@link NodeRule} class for {@link Topic#MODIFIER_ACTIVE} checking.
     */
    @Test
    public void testNodeForActiveModifier() {
        final Node activeModifierNotSetNode = new Clazz("id1", "ClassName", Visibility.PUBLIC, null, null, false, false, null);
        final NodeRule activeModifierNotSetRule = new NodeRule(activeModifierNotSetNode, Topic.MODIFIER_ACTIVE, Target.OBJECT, Operator.EQUALS);
        assertTrue(activeModifierNotSetRule.process(new Clazz("id2", "ClassName", Visibility.PRIVATE, null, false, false, false, true)));
        assertTrue(activeModifierNotSetRule.process(new Clazz("id3", "ClassName", Visibility.PRIVATE, null, true, false, false, true)));
        assertTrue(activeModifierNotSetRule.process(new Clazz("id4", "ClassName", Visibility.PRIVATE, null, false, true, false, true)));
        assertTrue(activeModifierNotSetRule.process(new Clazz("id5", "ClassName", Visibility.PRIVATE, null, false, false, true, true)));
        assertTrue(activeModifierNotSetRule.process(new Clazz("id6", "ClassName", Visibility.PRIVATE, null, false, false, false, false)));
 
        // Test active modifier
        final Node activeNode = new Clazz("id1", "ClassName", Visibility.PUBLIC, null, false, false, false, true);
        final NodeRule activeRule = new NodeRule(activeNode, Topic.MODIFIER_ACTIVE, Target.OBJECT, Operator.EQUALS);
        assertTrue(activeRule.process(new Clazz("id2", "ClassName", Visibility.PRIVATE, null, false, false, false, true)));
        assertTrue(activeRule.process(new Clazz("id3", "ClassName", Visibility.PRIVATE, null, true, false, false, true)));
        assertTrue(activeRule.process(new Clazz("id4", "ClassName", Visibility.PRIVATE, null, false, true, false, true)));
        assertTrue(activeRule.process(new Clazz("id5", "ClassName", Visibility.PRIVATE, null, false, false, true, true)));
        assertFalse(activeRule.process(new Clazz("id6", "ClassName", Visibility.PRIVATE, null, false, false, false, false)));
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
