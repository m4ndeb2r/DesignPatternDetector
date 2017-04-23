package nl.ou.dpd.domain.rule;

import nl.ou.dpd.domain.edge.Edge;
import nl.ou.dpd.domain.edge.EdgeType;
import nl.ou.dpd.domain.node.Attribute;
import nl.ou.dpd.domain.node.Clazz;
import nl.ou.dpd.domain.node.Interface;
import nl.ou.dpd.domain.node.Node;
import nl.ou.dpd.domain.node.Visibility;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import static junit.framework.TestCase.assertFalse;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Tests the {@link Conditions} class. In this test class we define a set of {@link Condition}s with {@link EdgeRule}s and/or two
 * {@link NodeRule}s implementing the rules constituting an Adapter Pattern (in its form of an objectdapter, with an abstract class as target).
 * There are three mould-edges, carrying the ideal specifications of the pattern.
 * The systemnodes and edges are based on the GoF-example of a drawingEditor.
 * <p>
 * We construct:
 * <ul>
 * <li>systemedges implementing the full pattern</li>
 * <li>systemedges implementing wrong relationships</li>
 * <li>systemedges with nodes implementing wrong types of classes</li>
 * <li>systemedges with nodes implementing wrong modifiers</li>
 * 
 * This class does not test the functionality of the units, but the behavior of the program, its possibilities, strengths and weaknesses with respect to a given pattern.
 *
 * // TODO: attributes are not checked.
 * 
 * @author Peter Vansweevelt
 */

public class AdapterPatternTest {

	private Node client, target, adapter, adaptee;
	private Edge clientTarget, adapterTarget, adapterAdaptee;
	private Attribute adapteeAttr, adapterAttr;
//	private Conditions conditions;
	private Edge editorShape;
	private Edge textshapeTextview;
	private Edge textshapeShape;
	private Node drawingEditor;
	private Node shape;
	private Node textShape;
	private Node textView;
	
	 /**
     * Exception rule.
     */
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Mock
    private Conditions conditions;
	
    /**
     * Initializes pattern edges.
     */
    @Before
    public void createPatternEdges() {
    	
        // Create Nodes
    	client = new Clazz("client", "Client", Visibility.PUBLIC, null, null, null, null, null);
    	target = new Clazz("target", "Target", Visibility.PUBLIC, null, null, null, true, null); //an abstract class
    	adapter = new Clazz("adapter", "Adapter", Visibility.PUBLIC, null, null, null, null, null);
    	adaptee = new Clazz("adaptee", "Adaptee", Visibility.PUBLIC, null, null, null, null, null);
    	
    	// Create attributes
    	adapteeAttr = new Attribute("adaptee", adaptee);
    	adapteeAttr.setVisibility(Visibility.PRIVATE);
    	adapterAttr = new Attribute("adapter", adapter);
    	
    	// set attributes in classes
    	client.getAttributes().add(adapterAttr);
    	adapter.getAttributes().add(adapteeAttr);
    	
    	// Create edges
        clientTarget = new Edge(client, target, EdgeType.ASSOCIATION_DIRECTED, "Client-Target");
        adapterAdaptee = new Edge(adapter, adaptee, EdgeType.ASSOCIATION_DIRECTED, "Adapter-Adaptee");
        adapterTarget = new Edge(adapter, target, EdgeType.INHERITANCE, "Adapter-Target");
        
        //set cardinality
        adapterAdaptee.setCardinalityLeft(0, -1);
        adapterAdaptee.setCardinalityRight(1, 1);
    }
    
    /**
     * Initializes pattern conditions.
     */
    @Before
    public void createPatternConditions() {
    	
        // Create Conditions
    	Condition cond1 = new Condition("1", "The Client has a directed association with the Target.");
    	cond1.getEdgeRules().add(new EdgeRule(clientTarget, Scope.RELATION, Topic.TYPE, Operator.EQUALS));
    	Condition cond2 = new Condition("2", "The Adapter has a directed association with the Adaptee.");
    	cond2.getEdgeRules().add(new EdgeRule(adapterAdaptee, Scope.RELATION, Topic.TYPE, Operator.EQUALS));
    	Condition cond3 = new Condition("3", "The Adapter has a inheritance relation with the Target (abstract class).");
    	cond3.getEdgeRules().add(new EdgeRule(adapterTarget, Scope.RELATION, Topic.TYPE, Operator.EQUALS));
    	Condition cond4 = new Condition("4", "The Target is an abstract class.");
    	cond4.getNodeRules().add(new NodeRule(target, Scope.OBJECT, Topic.MODIFIER_ABSTRACT, Operator.EQUALS));
    	Condition cond5 = new Condition("5", "The Adapter uses exactly one Adaptee. An Adaptee can be used by none or more Adapters.");
    	cond5.getEdgeRules().add(new EdgeRule(adapterAdaptee, Scope.RELATION, Topic.CARDINALITY, Operator.EQUALS));
    	Condition cond6 = new Condition("6", "The Adapter is a class.");
    	cond6.getNodeRules().add(new NodeRule(adapter, Scope.OBJECT, Topic.TYPE, Operator.EQUALS));    	
    	Condition cond7 = new Condition("7", "The Adaptee is a class.");
    	cond7.getNodeRules().add(new NodeRule(adaptee, Scope.OBJECT, Topic.TYPE, Operator.EQUALS));
    	Condition cond8 = new Condition("8", "The Client is a class.");
    	cond8.getNodeRules().add(new NodeRule(client, Scope.OBJECT, Topic.TYPE, Operator.EQUALS));
    	
    	//condition created by parsing the structure component of the template.xml
    	Condition cond9 = new Condition("9", "The Client has a Target attribute.");
    	cond9.getEdgeRules().add(new EdgeRule(clientTarget, Scope.ATTRIBUTE, Topic.TYPE, Operator.EXISTS));
    	//condition created by parsing the structure component of the template.xml
    	Condition cond10 = new Condition("10", "The Adapter has an Adaptee attribute.");
    	cond10.getEdgeRules().add(new EdgeRule(adapterAdaptee, Scope.ATTRIBUTE, Topic.TYPE, Operator.EXISTS));
    	
    	//This is a genuine attribute-rule
    	Condition cond11 = new Condition("12", "The Adaptee attribute in the Adapter has a private visibility.");
    	cond11.getAttributeRules().add(new AttributeRule(adapteeAttr, Scope.OBJECT, Topic.VISIBILITY, Operator.EQUALS));
    	//this condition needs an 'overview' of all pattern- and systemedges 
/*    	Condition cond12 = new Condition("12", "The Client does not have an Adaptee attribute.");
    	cond11.getNodeRules().add(new NodeRule(client, Scope.ATTRIBUTE, Topic.TYPE, Operator.NOT_EXISTS));
*/
    	
    	
    	conditions = new Conditions();
    	conditions.getConditions().add(cond1);
    	conditions.getConditions().add(cond2);
    	conditions.getConditions().add(cond3);
    	conditions.getConditions().add(cond4);
    	conditions.getConditions().add(cond5);
    	conditions.getConditions().add(cond6);
    	conditions.getConditions().add(cond7);
    	conditions.getConditions().add(cond8);
    	conditions.getConditions().add(cond9);
    	conditions.getConditions().add(cond10);
       	conditions.getConditions().add(cond11);    	   	
//    	conditions.getConditions().add(cond12);
 
    	//change default purview IGNORED to MANDATORY
    	for (Condition c : conditions.getConditions()) {
    		c.setPurview(Purview.MANDATORY);
    	}
    	
    }
    
    @Before
    public void createSystemEdges() {
        // Create ideal systemnodes
    	//Client
    	drawingEditor = new Clazz("drawingEditor", "DrawingEditor", Visibility.PUBLIC, null, null, null, null, null);
    	//Target
    	shape = new Clazz("shape", "Shape", Visibility.PUBLIC, null, null, null, true, null); //an abstract class
    	//Adapter
    	textShape = new Clazz("textShape", "TextShape", Visibility.PUBLIC, null, null, null, null, null);
    	//Adaptee
    	textView = new Clazz("textView", "TextView", Visibility.PUBLIC, null, null, null, null, null);

    	// Create attributes
    	Attribute textShapeAttr = new Attribute("textView", textView);
    	textShapeAttr.setVisibility(Visibility.PRIVATE);
    	Attribute drawingEditorAttr = new Attribute("Shape", shape);
  
    	// set attributes in classes
    	textShape.getAttributes().add(textShapeAttr);
    	drawingEditor.getAttributes().add(drawingEditorAttr);   	
    	
    	// Create systemedges
    	//clientTaget
        editorShape = new Edge(drawingEditor, shape, EdgeType.ASSOCIATION_DIRECTED, "editor-shape");
        //adapterAdaptee
        textshapeTextview = new Edge(textShape, textView, EdgeType.ASSOCIATION_DIRECTED, "textShape-textView");
        //adapterTarget
        textshapeShape = new Edge(textShape, shape, EdgeType.INHERITANCE, "textShape-shape");
        
        //set systemcardinality
        textshapeTextview.setCardinalityLeft(0, -1);
        textshapeTextview.setCardinalityRight(1, 1);    	
    }
    
    /**
     * Tests a system where all conditions are met.
     */
    @Test
    public void testSystemOK() {
  
    	assertTrue(conditions.processConditions(editorShape, clientTarget));
    	assertTrue(conditions.processConditions(textshapeTextview, adapterAdaptee));
    	assertTrue(conditions.processConditions(textshapeShape, adapterTarget));
    	
    	//wrong combinations of systemedges and patternedges
    	//don't evaluate cardinalities and attributes
    	conditions.getConditions().get(4).setPurview(Purview.IGNORE);
    	conditions.getConditions().get(10).setPurview(Purview.IGNORE);
    	//wrong association but all conditions of adapter-adaptee are met (cond2,cond6, cond7)
    	assertTrue(conditions.processConditions(editorShape, adapterAdaptee));
    	assertFalse(conditions.processConditions(editorShape, adapterTarget));
    	assertFalse(conditions.processConditions(textshapeTextview, adapterTarget));
    	assertFalse(conditions.processConditions(textshapeTextview, clientTarget));
    	assertFalse(conditions.processConditions(textshapeShape, clientTarget));
    	assertFalse(conditions.processConditions(textshapeShape, adapterAdaptee));
    	
    	//wrong combination cardinality condition on adapter-adaptee- relation, not set in systemedge
    	//same problem with exception for null-settings of attributes 
    	//evaluate cardinalities
    	conditions.getConditions().get(4).setPurview(Purview.MANDATORY);
    	thrown.expect(RuleException.class);
        thrown.expectMessage("Either one or both cardinalities are not set.");
        conditions.processConditions(editorShape, adapterAdaptee);
   }
    
    /**
     * Tests a system where a relationship is wrong.
     */
    @Test
    public void testWrongRelationship() {
    	//clientTarget has a wrong relationship
        editorShape = new Edge(drawingEditor, shape, EdgeType.DEPENDENCY, "editor-shape");
        
        //run test
    	assertFalse(conditions.processConditions(editorShape, clientTarget));
    	assertTrue(conditions.processConditions(textshapeTextview, adapterAdaptee));
    	assertTrue(conditions.processConditions(textshapeShape, adapterTarget));

    	//set condition1 on IGNORE
        conditions.getConditions().get(0).setPurview(Purview.IGNORE);
    	assertTrue(conditions.processConditions(editorShape, clientTarget));
    	assertTrue(conditions.processConditions(textshapeTextview, adapterAdaptee));
    	assertTrue(conditions.processConditions(textshapeShape, adapterTarget));   	
    } 
    
    /**
     * Tests a system where a nodeType is wrong.
     */
    @Test
    public void testWrongNodeType() {
    	//textView has a wrong nodeType
    	textView = new Interface("textView", "TextView");
    	textshapeTextview = new Edge(textShape, textView, EdgeType.ASSOCIATION_DIRECTED, "textShape-textView");
        textshapeTextview.setCardinalityLeft(0, -1);
        textshapeTextview.setCardinalityRight(1, 1);    	
    	 
        //run test
    	assertTrue(conditions.processConditions(editorShape, clientTarget));
    	assertFalse(conditions.processConditions(textshapeTextview, adapterAdaptee));
    	assertTrue(conditions.processConditions(textshapeShape, adapterTarget));

    	//set condition 7 on IGNORE
        conditions.getConditions().get(6).setPurview(Purview.IGNORE);
    	assertTrue(conditions.processConditions(editorShape, clientTarget));
    	assertTrue(conditions.processConditions(textshapeTextview, adapterAdaptee));
    	assertTrue(conditions.processConditions(textshapeShape, adapterTarget));   	
    }
    
    /**
     * Tests a system where a nodeType is wrong.
     */
    @Test
    public void testWrongModifier() {
    	//shape is not an abstract class
    	shape = new Clazz("shape", "Shape", Visibility.PUBLIC, null, null, null, false, null);
    	textshapeShape = new Edge(textShape, shape, EdgeType.INHERITANCE, "textShape-shape");
    	   	 
        //run test
    	assertTrue(conditions.processConditions(editorShape, clientTarget));
    	assertTrue(conditions.processConditions(textshapeTextview, adapterAdaptee));
    	assertFalse(conditions.processConditions(textshapeShape, adapterTarget));

    	//set condition 4 on IGNORE
        conditions.getConditions().get(3).setPurview(Purview.IGNORE);
    	assertTrue(conditions.processConditions(editorShape, clientTarget));
    	assertTrue(conditions.processConditions(textshapeTextview, adapterAdaptee));
    	assertTrue(conditions.processConditions(textshapeShape, adapterTarget)); 
    	
       	//modifier abstract class is not set
    	shape = new Clazz("shape", "Shape", Visibility.PUBLIC, null, null, null, null, null);
    	textshapeShape = new Edge(textShape, shape, EdgeType.INHERITANCE, "textShape-shape");
    	
        conditions.getConditions().get(3).setPurview(Purview.MANDATORY);
    	assertTrue(conditions.processConditions(editorShape, clientTarget));
    	assertTrue(conditions.processConditions(textshapeTextview, adapterAdaptee));
    	assertFalse(conditions.processConditions(textshapeShape, adapterTarget)); 

    }
    
    /**
     * Test a system with mapped system and patternedges
     * Map<Node, Node> = map<systemedge, patternedge>
     */
    @Test
    public void testMatchedNodesOK() {
    	Map<Node, Node> matchedNodes = new HashMap<>();
    	matchedNodes.put(drawingEditor, client);
    	matchedNodes.put(shape, target);
    	matchedNodes.put(textShape, adapter);
    	matchedNodes.put(textView, adaptee);
 
    	assertTrue(conditions.processConditions(matchedNodes));    	

    	//only node conditions are evaluated
    	for (Condition c : conditions.getConditions()) {
    		assertTrue(c.isProcessed());
    	}
     	
    }
    
    /**
     * Test a system building mapped system and patternedges
     * Map<Node, Node> = map<systemedge, patternedge>
     */
    @Test
    public void testMatchedNodesBuildingOK() {
    	
    	Map<Node, Node> matchedNodes = new HashMap<>();
       	if (conditions.processConditions(editorShape, clientTarget)) {
    		matchedNodes.put(editorShape.getLeftNode(), clientTarget.getLeftNode());
    		matchedNodes.put(editorShape.getRightNode(), clientTarget.getRightNode());
    	}
    	if (conditions.processConditions(textshapeTextview, adapterAdaptee)) {
    		matchedNodes.put(textshapeTextview.getLeftNode(), adapterAdaptee.getLeftNode());
    		matchedNodes.put(textshapeTextview.getRightNode(), adapterAdaptee.getRightNode());
    	}
    	if (conditions.processConditions(textshapeShape, adapterTarget)) {
    		matchedNodes.put(textshapeShape.getLeftNode(), adapterTarget.getLeftNode());
    		matchedNodes.put(textshapeShape.getRightNode(), adapterTarget.getRightNode());
    	}

    	boolean structureOK = conditions.processConditions(editorShape, clientTarget) && conditions.processConditions(textshapeTextview, adapterAdaptee) && conditions.processConditions(textshapeShape, adapterTarget);
    	assertTrue(conditions.processConditions(matchedNodes) && structureOK);


    }
    
    /**
     * Test a system building mapped system and patternedges with a wrong relationship.
     * Map<Node, Node> = map<systemedge, patternedge>
     */
    @Test
    public void testMatchedNodesBuildingWrongRelationship() {
    	
    	Map<Node, Node> matchedNodes = new HashMap<>();
    	//clientTarget has a wrong relationship
        editorShape = new Edge(drawingEditor, shape, EdgeType.DEPENDENCY, "editor-shape");
    	
    	if (conditions.processConditions(editorShape, clientTarget)) {
    		matchedNodes.put(editorShape.getLeftNode(), clientTarget.getLeftNode());
    		matchedNodes.put(editorShape.getRightNode(), clientTarget.getRightNode());
    	}
    	if (conditions.processConditions(textshapeTextview, adapterAdaptee)) {
    		matchedNodes.put(textshapeTextview.getLeftNode(), adapterAdaptee.getLeftNode());
    		matchedNodes.put(textshapeTextview.getRightNode(), adapterAdaptee.getRightNode());
    	}
    	if (conditions.processConditions(textshapeShape, adapterTarget)) {
    		matchedNodes.put(textshapeShape.getLeftNode(), adapterTarget.getLeftNode());
    		matchedNodes.put(textshapeShape.getRightNode(), adapterTarget.getRightNode());
    	}
    	
    	//nodes of a wrong edge are not mentioned in the map, thus not processed with the matchedNodes.
    	//assert all conditions are evaluated
    	for (Condition c : conditions.getConditions()) {
    		assertTrue(c.isProcessed());
    	}
    	
    	assertTrue(conditions.processConditions(matchedNodes)); 
    	
    	//with an extra evaluation of the different edges, structure is not complete.
       	boolean structureOK = conditions.processConditions(editorShape, clientTarget) && conditions.processConditions(textshapeTextview, adapterAdaptee) && conditions.processConditions(textshapeShape, adapterTarget);
    	assertFalse(conditions.processConditions(matchedNodes) && structureOK);    	
     }
}
