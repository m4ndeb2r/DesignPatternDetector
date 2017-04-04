
package nl.ou.dpd.domain.rule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import nl.ou.dpd.domain.*;
import nl.ou.dpd.domain.node.*;
import nl.ou.dpd.domain.edge.*;
import nl.ou.dpd.domain.rule.*;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.*;

import java.util.List;

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
     * 
     * Clazz systemClass1: id="class1", name="class1", visibility=public, isAbstract=FALSE
     * Clazz systemClass2: id="class2", name="class2", visibility:=PRIVATE, isAbstract:NOT SET;
     * Clazz systemClass3: id="class2", name="CLASS3", visibility:=PRIVATE, isAbstract:NOT SET;
     * Edge systemEdge1: id="edge1, name="edge1", node1=systemClass1, node2= systemClass2, relationtype=association
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
    NodeRule noderule2 = new NodeRule(ruleClass1, Topic.MODIFIER_ABSTRACT, Target.OBJECT, Operator.EQUALS);
    NodeRule noderule3 = new NodeRule(ruleClass2, Topic.VISIBILITY, Target.OBJECT, Operator.EQUALS);

    Clazz systemClass1 = new Clazz("class1","class1",Visibility.PUBLIC, null, null, null, false, null);
    Clazz systemClass2 = new Clazz("class2","class2",Visibility.PRIVATE, null, null, null, null, null);
    Clazz systemClass3 = new Clazz("class2","class3",Visibility.PRIVATE, null, null, null, null, null);
    Edge systemEdge1 = new Edge(systemClass1, systemClass2, EdgeType.ASSOCIATION, "edge1");
    Edge systemEdge2 = new Edge(systemClass1, systemClass2, EdgeType.DEPENDENCY, "edge2");
    Edge systemEdge3 = new Edge(systemClass1, systemClass2, EdgeType.ASSOCIATION, "edge3");

    Condition condition1 = new Condition("c1", "test condition1");
    Condition condition2 = new Condition("c2", "test condition2");
    Condition condition3 = new Condition("c3", "test condition3");
    
    /**
     * Test basic operations.
     */
    @Test
    public void testConditionBasicOperations() {
    	condition1.addRule(edgerule1);
    	condition2.addRule(noderule2);
    	condition3.addRule(noderule3);
    	
        assertEquals(condition1.getRules().size(),1);
        //a same rule cannot be added twice
        condition1.addRule(edgerule1);
        assertEquals(condition1.getRules().size(),1);
        condition2.addRule(noderule3);
        assertEquals(condition2.getRules().size(),2);
        condition2.removeRule(noderule3);
        assertEquals(condition2.getRules().size(),1);
        condition2.clearRules();
        assertEquals(condition2.getRules().size(),0);       
    }

    /**
     * Test Purview.IGNORE and Purview.FEEDBACK_ONLY
     */
    @Test
    public void testPurview() {
    	condition1.addRule(edgerule1);  	
    	condition1.setPurview(Purview.IGNORE);
    	
    	//return must always be true
    	assertTrue(condition1.process(systemEdge1));
    	assertTrue(condition1.process(systemEdge2));
    	assertTrue(condition1.process(systemEdge3));
    	
    	condition1.setPurview(Purview.FEEDBACK_ONLY);
    	//return must always be true
    	assertTrue(condition1.process(systemEdge1));
    	assertTrue(condition1.process(systemEdge2));
    	assertTrue(condition1.process(systemEdge3)); 
    }
 
    /**
     * Test case EdgeType
     */
    @Test
    public void testCaseEdgeType() {
    	condition1.addRule(edgerule1);  	
    	condition1.setPurview(Purview.OBLIGATED);
    	condition2.addRule(edgerule1);  	
    	condition2.setPurview(Purview.OBLIGATED);
    	condition3.addRule(edgerule1);  	
    	condition3.setPurview(Purview.OBLIGATED);
    	
    	assertTrue(condition1.process(systemEdge1));
    }
}
