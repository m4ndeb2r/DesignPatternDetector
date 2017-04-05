
package nl.ou.dpd.domain.rule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import nl.ou.dpd.domain.node.*;
import nl.ou.dpd.domain.edge.*;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.*;

/**
 * Tests the {@link Condition} class.
 *
 * @author Peter Vansweevelt
 *
 */
public class ConditionTest {
    /**
     * Exception rule.
     */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Clazz ruleClass1: id="class1", name="class1", visibility=public, isAbstract=true
     * Clazz ruleClass2: id="class2", name="class2", visibility=public, isAbstract=false
     * Edge ruleEdge1:  id="edge1", name="edge1", node1=class1, node2=class2, relationtype=association
     * 
     * EdgeRule edgeRule1: relationtype must be association
     * NodeRule nodeRule1: node1.Modifier_abstract must be true
     * NodeRule nodeRule2: node2.visibility must be public
     * NodeRule nodeRule3: node1 must be of type Clazz.
     * 
     * Interface systemInterface1: id="class1", name="class1", visibility=public, isAbstract=true
     * Clazz systemClass1t: id="class1", name="class1", visibility=public, isAbstract=true
     * Clazz systemClass1f: id="class1", name="class1", visibility=public, isAbstract=FALSE
     * Clazz systemClass2t: id="class2", name="class2", visibility:=PRIVATE, isAbstract:NOT SET;
     * Clazz systemClass2f: id="class2", name="CLASS3", visibility:=PRIVATE, isAbstract:NOT SET;
     * Edge systemEdge1t: id="edge1, name="edge1", node1=systemClass1t, node2= systemClass2, relationtype=association: all true
     * Edge systemEdge1f: id="edge1, name="edge1", node1=systemClass1f, node2= systemClass2, relationtype=association: isAbstract = false
     * Edge systemEdge2: id="edge2", name="edge1", node1=SystemClass1, node2= systemClass2, relationtype=DEPENDENCY
     * Edge systemEdge3: id="edge1, name="EDGE3", node1=systemClass1, node2= systemClass2, relationtype=association
     * 
     * Clazz(String id, String name, Visibility visibility, List<Attribute> attributes, Boolean isRoot, Boolean isLeaf, Boolean isAbstract, Boolean isActive)
     * Edge(Node node1, Node node2, EdgeType type, String name)
     */
    
    Clazz ruleClass1 = new Clazz("class1","class1",Visibility.PUBLIC, null, null, null, true, null);
    Clazz ruleClass2 = new Clazz("class2","class2",Visibility.PUBLIC, null, null, null, false, null);
    Edge ruleEdge1 = new Edge(ruleClass1, ruleClass2, EdgeType.ASSOCIATION, "edge1");
    
    EdgeRule edgerule1 = new EdgeRule(ruleEdge1, Topic.TYPE, Target.RELATION, Operator.EQUALS);
    NodeRule noderule1 = new NodeRule(ruleClass1, Topic.MODIFIER_ABSTRACT, Target.OBJECT, Operator.EQUALS);
    NodeRule noderule2 = new NodeRule(ruleClass2, Topic.VISIBILITY, Target.OBJECT, Operator.EQUALS);
    NodeRule noderule3 = new NodeRule(ruleClass1, Topic.TYPE, Target.OBJECT, Operator.EQUALS);

    Interface systemInterface1 = new Interface("class1","class1");
    Clazz systemClass1 = new Clazz("class1","class1",Visibility.PUBLIC, null, null, null, true, null);
    Clazz systemClass1f = new Clazz("class1","class1",Visibility.PUBLIC, null, null, null, false, null);
    Clazz systemClass2 = new Clazz("class2","class2",Visibility.PUBLIC, null, null, null, null, null);
    Clazz systemClass2f = new Clazz("class2","class2",Visibility.PRIVATE, null, null, null, null, null);
    Clazz systemClass3 = new Clazz("class2","class3",Visibility.PUBLIC, null, null, null, null, null);
    
    Edge edgeTrue = new Edge(systemClass1, systemClass2, EdgeType.ASSOCIATION, "edge1"); //true
    Edge edgeNotAbstract = new Edge(systemClass1f, systemClass2, EdgeType.ASSOCIATION, "edge1"); //node1 is not abstract 
    Edge edgeWrongVisibility = new Edge(systemClass1, systemClass2f, EdgeType.ASSOCIATION, "edge1"); //node2 has wrong visibility 
    Edge edgeWrongRelation = new Edge(systemClass1, systemClass2, EdgeType.DEPENDENCY, "edge1"); //wrong relationship
    Edge edgeWrongName = new Edge(systemClass1, systemClass3, EdgeType.ASSOCIATION, "edge2"); //wrong name of node2
    Edge edgeWrongClass = new Edge(systemInterface1, systemClass2, EdgeType.ASSOCIATION, "edge1"); //node1 has a wrong classtype
    

    Condition condition1 = new Condition("c1", "test condition1");
    Condition condition2 = new Condition("c2", "test condition2");
    Condition condition3 = new Condition("c3", "test condition3");
    
    Conditions conditions = new Conditions();
    
    /**
     * Test basic operations.
     */
    @Test
    public void testConditionBasicOperations() {
    	condition1.addRule(edgerule1);
    	condition2.addRule(noderule1);
    	condition3.addRule(noderule2);
    	
        assertEquals(1, condition1.getRules().size());
        //a same rule cannot be added twice
        condition1.addRule(edgerule1);
        assertEquals(1, condition1.getRules().size());
        condition2.addRule(noderule3);
        assertEquals(2, condition2.getRules().size());
        condition2.removeRule(noderule3);
        assertEquals(1, condition2.getRules().size());
        condition2.clearRules();
        assertEquals(0, condition2.getRules().size());       
    }

    /**
     * Test Purview.IGNORE and Purview.FEEDBACK_ONLY
     */
    @Test
    public void testPurview() {
    	condition1.addRule(edgerule1);  	
    	condition1.setPurview(Purview.IGNORE);
    	
    	//return must always be null
    	assertNull(condition1.process(edgeTrue));
    	assertNull(condition1.process(edgeNotAbstract));
    	assertNull(condition1.process(edgeWrongVisibility));
    	assertNull(condition1.process(edgeWrongRelation));
    	assertNull(condition1.process(edgeWrongName));
    	assertNull(condition1.process(edgeWrongClass));
    	
    	condition1.setPurview(Purview.FEEDBACK_ONLY);
    	condition1.clearHandled();
    	//return must be the same as Obligated
    	assertTrue(condition1.process(edgeTrue));
    	condition1.clearHandled();
    	assertTrue(condition1.process(edgeNotAbstract));
    	condition1.clearHandled();
    	assertTrue(condition1.process(edgeWrongVisibility));
    	condition1.clearHandled();
    	assertFalse(condition1.process(edgeWrongRelation));
    	condition1.clearHandled();
    	assertNull(condition1.process(edgeWrongName));
    	condition1.clearHandled();
    	assertTrue(condition1.process(edgeWrongClass));
    }
 
    /**
     * Test case EdgeType
     */
    @Test
    public void testCaseEdgeType() {
    	//conditions must be set separately because handler is set after processing
        Condition condition1 = new Condition("c1", "test condition1");
        condition1.addRule(edgerule1);  	
    	condition1.setPurview(Purview.OBLIGATED);

    	condition1.clearHandled();
    	assertTrue(condition1.process(edgeTrue));
    	condition1.clearHandled();
    	assertTrue(condition1.process(edgeNotAbstract));
    	condition1.clearHandled();
    	assertTrue(condition1.process(edgeWrongVisibility));
    	condition1.clearHandled();
    	assertFalse(condition1.process(edgeWrongRelation));
    	condition1.clearHandled();
    	assertNull(condition1.process(edgeWrongName));
    	condition1.clearHandled();
    	assertTrue(condition1.process(edgeWrongClass));
    }
    
    /**
     * Test case NodeType node1
     */
    @Test
    public void testCaseNodeTypeNode1() {
        Condition condition1 = new Condition("c1", "test condition1");
        condition1.addRule(noderule1);  	
    	condition1.setPurview(Purview.OBLIGATED);
    	   	
    	condition1.clearHandled();
    	assertTrue(condition1.process(edgeTrue));
    	condition1.clearHandled();
    	assertFalse(condition1.process(edgeNotAbstract));
    	condition1.clearHandled();
    	assertTrue(condition1.process(edgeWrongVisibility));
    	condition1.clearHandled();
    	assertTrue(condition1.process(edgeWrongRelation));
    	condition1.clearHandled();
    	assertTrue(condition1.process(edgeWrongName)); //condition about node1 and wrong name of node2
    	condition1.clearHandled();
    	assertTrue(condition1.process(edgeWrongClass));
    }
    
    /**
     * Test case NodeType node2
     */
    @Test
    public void testCaseNodeTypeNode2() {
        Condition condition1 = new Condition("c1", "test condition1");
        condition1.addRule(noderule2);  	
    	condition1.setPurview(Purview.OBLIGATED);

    	condition1.clearHandled();
    	assertTrue(condition1.process(edgeTrue));
    	condition1.clearHandled();
    	assertTrue(condition1.process(edgeNotAbstract));
    	condition1.clearHandled();
    	assertFalse(condition1.process(edgeWrongVisibility));
    	condition1.clearHandled();
    	assertTrue(condition1.process(edgeWrongRelation));
    	condition1.clearHandled();
    	assertNull(condition1.process(edgeWrongName)); //condition about node1 and wrong name of node2
    	condition1.clearHandled();
    	assertTrue(condition1.process(edgeWrongClass));   	
    }
    
    /**
     * Test multiple rules. All rules must be met.
     */
    @Test
    public void testMultipleRules() {
        Condition condition1 = new Condition("c1", "test condition1");
        condition1.addRule(edgerule1);
        condition1.addRule(noderule2);  	
    	condition1.setPurview(Purview.OBLIGATED);

    	condition1.clearHandled();
    	assertTrue(condition1.process(edgeTrue));
    	condition1.clearHandled();
    	assertTrue(condition1.process(edgeNotAbstract));
    	condition1.clearHandled();
    	assertFalse(condition1.process(edgeWrongVisibility));
    	condition1.clearHandled();
    	assertFalse(condition1.process(edgeWrongRelation));
    	condition1.clearHandled();
    	assertNull(condition1.process(edgeWrongName)); //condition about node1 and wrong name of node2
    	condition1.clearHandled();
    	assertTrue(condition1.process(edgeWrongClass));   	
    }    	
}
