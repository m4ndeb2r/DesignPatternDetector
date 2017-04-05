/**
 * 
 */
package nl.ou.dpd.domain.rule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import nl.ou.dpd.domain.node.*;
import nl.ou.dpd.domain.edge.*;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.*;

/**
 * Tests the {@link Conditions} class.
 */

public class ConditionsTest {
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
    	condition2.addRule(noderule2);
    	condition3.addRule(noderule3);
    	
    	conditions.addCondition(condition1);
    	conditions.addCondition(condition2);
    	conditions.addCondition(condition3);
    	
        assertEquals(3, conditions.getConditions().size());
        //a same condition cannot be added twice
        conditions.addCondition(condition2);
        assertEquals(3, conditions.getConditions().size());
        conditions.removeCondition(condition1);
        assertEquals(2, conditions.getConditions().size());
        conditions.clearConditions();
        assertEquals(0, conditions.getConditions().size());       
    }
    
    /**
     * Test conditions without relations.
     */
    @Test
    public void testConditionsWithoutRelations() {
    	//conditions must be set separately because handler is set after processing
        Condition condition1 = new Condition("c1", "test condition1");
        Condition condition2 = new Condition("c2", "test condition2");
        Condition condition3 = new Condition("c3", "test condition3");
        
        condition1.addRule(edgerule1);  	
        condition1.addRule(noderule3);  	 	
    	condition1.setPurview(Purview.OBLIGATED);  	
        condition2.addRule(noderule1);  	    	
    	condition2.setPurview(Purview.OBLIGATED);  	
    	condition3.addRule(noderule2);    	
    	condition3.setPurview(Purview.OBLIGATED);
    	
    	conditions.clearConditions();
    	conditions.addCondition(condition1);
    	conditions.addCondition(condition2);
    	conditions.addCondition(condition3);
    	
    	//no and-relations
    	assertTrue(conditions.processConditions(edgeTrue));
    	assertFalse(conditions.processConditions(edgeNotAbstract));
    	assertFalse(conditions.processConditions(edgeWrongVisibility));
    	assertFalse(conditions.processConditions(edgeWrongRelation));
    	assertNull(conditions.processConditions(edgeWrongName));
       	assertFalse(conditions.processConditions(edgeWrongClass));
   	
    	//change purview
    	//conditions must be set separately because handler is set after processing
        condition1 = new Condition("c1", "test condition1");
        condition2 = new Condition("c2", "test condition2");
        condition3 = new Condition("c3", "test condition3");
        
        condition1.addRule(edgerule1);  	
        condition1.addRule(noderule3);  	 	
    	condition1.setPurview(Purview.OBLIGATED);  	
        condition2.addRule(noderule1);  	    	
    	condition2.setPurview(Purview.IGNORE);  	
    	condition3.addRule(noderule2);    	
    	condition3.setPurview(Purview.IGNORE);
    	
    	conditions.clearConditions();
    	conditions.addCondition(condition1);
    	conditions.addCondition(condition2);
    	conditions.addCondition(condition3);
    	
       	//no and-relations
    	assertTrue(conditions.processConditions(edgeTrue));
    	assertTrue(conditions.processConditions(edgeNotAbstract));
    	assertTrue(conditions.processConditions(edgeWrongVisibility));
    	assertFalse(conditions.processConditions(edgeWrongRelation));
    	assertNull(conditions.processConditions(edgeWrongName));
       	assertFalse(conditions.processConditions(edgeWrongClass));

    	//change purview
    	//conditions must be set separately because handler is set after processing
        condition1 = new Condition("c1", "test condition1");
        condition2 = new Condition("c2", "test condition2");
        condition3 = new Condition("c3", "test condition3");
        
        condition1.addRule(edgerule1);  	
        condition1.addRule(noderule3);  	 	
    	condition1.setPurview(Purview.IGNORE);  	
        condition2.addRule(noderule1);  	    	
    	condition2.setPurview(Purview.OBLIGATED);  	
    	condition3.addRule(noderule2);    	
    	condition3.setPurview(Purview.FEEDBACK_ONLY);
    	
    	conditions.clearConditions();
    	conditions.addCondition(condition1);
    	conditions.addCondition(condition2);
    	conditions.addCondition(condition3);
    	
       	//no and-relations
    	assertTrue(conditions.processConditions(edgeTrue));
    	assertFalse(conditions.processConditions(edgeNotAbstract));
    	assertTrue(conditions.processConditions(edgeWrongVisibility));
    	assertTrue(conditions.processConditions(edgeWrongRelation));
    	assertTrue(conditions.processConditions(edgeWrongName));
       	assertTrue(conditions.processConditions(edgeWrongClass));

   } 
    
    /**
     * Test conditions with And relations.
     */
    @Test
    public void testConditionsWithAndRelations() {
    	//conditions must be set separately because handler is set after processing
        Condition condition1 = new Condition("c1", "test condition1");
        Condition condition2 = new Condition("c2", "test condition2");
        Condition condition3 = new Condition("c3", "test condition3");
        
        condition1.addRule(edgerule1);  	
        condition1.addRule(noderule3);  	 	
    	condition1.setPurview(Purview.OBLIGATED);  	
        condition2.addRule(noderule1);  	    	
    	condition2.setPurview(Purview.OBLIGATED);  	
    	condition3.addRule(noderule2);    	
    	condition3.setPurview(Purview.OBLIGATED);
    	
    	condition1.addAndCondition(condition3);
    	condition3.addAndCondition(condition1);
    	
    	conditions.clearConditions();
    	conditions.addCondition(condition1);
    	conditions.addCondition(condition2);
    	conditions.addCondition(condition3);
    	
    	//if condition1 is true, condition3 must also be true and vice versa
    	//this gives the same result as without an and-relation because conditions are mutually evaluated with an AND-relation.  
    	assertTrue(conditions.processConditions(edgeTrue));
    	assertFalse(conditions.processConditions(edgeNotAbstract));
    	assertFalse(conditions.processConditions(edgeWrongVisibility));
    	assertFalse(conditions.processConditions(edgeWrongRelation));
    	assertNull(conditions.processConditions(edgeWrongName));
       	assertFalse(conditions.processConditions(edgeWrongClass));
   	
    	//change purview
        condition1 = new Condition("c1", "test condition1");
        condition2 = new Condition("c2", "test condition2");
        condition3 = new Condition("c3", "test condition3");
        
        condition1.addRule(edgerule1);  	
        condition1.addRule(noderule3);  	 	
    	condition1.setPurview(Purview.OBLIGATED);  	
        condition2.addRule(noderule1);  	    	
    	condition2.setPurview(Purview.IGNORE);  	
    	condition3.addRule(noderule2);    	
    	condition3.setPurview(Purview.IGNORE);

    	condition1.addAndCondition(condition2);
    	
    	conditions.clearConditions();
    	conditions.addCondition(condition1);
    	conditions.addCondition(condition2);
    	conditions.addCondition(condition3);
    	
    	//if condition1 is true, condition3 must also be true and vice versa, but condition3 is ignored
    	//This gives the same result because condition3 is ignored in the and-relation too.
    	assertTrue(conditions.processConditions(edgeTrue));
    	assertTrue(conditions.processConditions(edgeNotAbstract));
    	assertTrue(conditions.processConditions(edgeWrongVisibility));
    	assertFalse(conditions.processConditions(edgeWrongRelation));
    	assertNull(conditions.processConditions(edgeWrongName));
       	assertFalse(conditions.processConditions(edgeWrongClass));
     } 

    /**
     * Test conditions with OR relations.
     */
    @Test
    public void testConditionsWithOrRelations() {
    	//conditions must be set separately because handler is set after processing
        Condition condition1 = new Condition("c1", "test condition1");
        Condition condition2 = new Condition("c2", "test condition2");
        Condition condition3 = new Condition("c3", "test condition3");
        
        condition1.addRule(edgerule1);  	
        condition1.addRule(noderule3);  	 	
    	condition1.setPurview(Purview.OBLIGATED);  	
        condition2.addRule(noderule1);  	    	
    	condition2.setPurview(Purview.OBLIGATED);  	
    	condition3.addRule(noderule2);    	
    	condition3.setPurview(Purview.OBLIGATED);
    	
    	condition1.addOrCondition(condition3);
    	
    	conditions.clearConditions();
    	conditions.addCondition(condition1);
    	conditions.addCondition(condition2);
    	conditions.addCondition(condition3);
    	
    	//if either condition1 or condition3 is true, the whole is true. 
    	assertTrue(conditions.processConditions(edgeTrue));
    	assertFalse(conditions.processConditions(edgeNotAbstract));
    	assertTrue(conditions.processConditions(edgeWrongVisibility));
    	assertTrue(conditions.processConditions(edgeWrongRelation));
    	assertNull(conditions.processConditions(edgeWrongName));
       	assertTrue(conditions.processConditions(edgeWrongClass));
   	
    	//change purview
        condition1 = new Condition("c1", "test condition1");
        condition2 = new Condition("c2", "test condition2");
        condition3 = new Condition("c3", "test condition3");
        
        condition1.addRule(edgerule1);  	
        condition1.addRule(noderule3);  	 	
    	condition1.setPurview(Purview.OBLIGATED);  	
        condition2.addRule(noderule1);  	    	
    	condition2.setPurview(Purview.IGNORE);  	
    	condition3.addRule(noderule2);    	
    	condition3.setPurview(Purview.IGNORE);

    	condition1.addOrCondition(condition2);
    	
    	conditions.clearConditions();
    	conditions.addCondition(condition1);
    	conditions.addCondition(condition2);
    	conditions.addCondition(condition3);
    	
    	//if either condition1 or condition3 is true, the whole is true. 
    	//This gives the same result because ignored condition return always true.
    	assertTrue(conditions.processConditions(edgeTrue));
    	assertTrue(conditions.processConditions(edgeNotAbstract));
    	assertTrue(conditions.processConditions(edgeWrongVisibility));
    	assertFalse(conditions.processConditions(edgeWrongRelation));
    	assertNull(conditions.processConditions(edgeWrongName));
       	assertFalse(conditions.processConditions(edgeWrongClass));
     } 

    /**
     * Test conditions with OR and AND relations.
     */
    @Test
    public void testConditionsWithOrAndRelations() {
    	//conditions must be set separately because handler is set after processing
        Condition condition1 = new Condition("c1", "test condition1");
        Condition condition2 = new Condition("c2", "test condition2");
        Condition condition3 = new Condition("c3", "test condition3");
        
        condition1.addRule(edgerule1);  	
        condition1.addRule(noderule3);  	 	
    	condition1.setPurview(Purview.OBLIGATED);  	
        condition2.addRule(noderule1);  	    	
    	condition2.setPurview(Purview.OBLIGATED);  	
    	condition3.addRule(noderule2);    	
    	condition3.setPurview(Purview.OBLIGATED);
    	
    	condition1.addAndCondition(condition3);
    	condition1.addOrCondition(condition2);
    	
    	conditions.clearConditions();
    	conditions.addCondition(condition1);
    	conditions.addCondition(condition2);
    	conditions.addCondition(condition3);
    	
    	//if (condition1 and condition3) or condition2 , the whole is true. 
    	assertTrue(conditions.processConditions(edgeTrue));
    	assertTrue(conditions.processConditions(edgeNotAbstract));
    	assertFalse(conditions.processConditions(edgeWrongVisibility));
    	assertTrue(conditions.processConditions(edgeWrongRelation));
    	assertNull(conditions.processConditions(edgeWrongName));
       	assertTrue(conditions.processConditions(edgeWrongClass));
     } 

}
