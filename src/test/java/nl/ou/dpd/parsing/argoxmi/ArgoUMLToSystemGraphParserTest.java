package nl.ou.dpd.parsing.argoxmi;

import nl.ou.dpd.domain.SystemUnderConsideration;
import nl.ou.dpd.domain.node.Attribute;
import nl.ou.dpd.domain.node.Method;
import nl.ou.dpd.domain.SystemUnderConsiderationGraph;
import nl.ou.dpd.domain.edge.Edge;
import nl.ou.dpd.domain.edge.EdgeType;
import nl.ou.dpd.domain.edge.Relation;
import nl.ou.dpd.domain.edge.RelationType;
import nl.ou.dpd.domain.node.Node;
import nl.ou.dpd.domain.node.NodeType;
import nl.ou.dpd.domain.node.Visibility;
import nl.ou.dpd.parsing.ParseException;
import nl.ou.dpd.utils.TestHelper;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Tests the {@link ArgoUMLSystemParser} class.
 *
 * @author Martin de Boer
 * @author Peter Vansweevelt
 */
public class ArgoUMLToSystemGraphParserTest {

    // A test file containing valid XML.
    private static final String VALID_ADAPTER = "/adapters_structures_association.xmi";
    // A  more complicated test file containing valid XML.
    private static final String VALID_ADAPTERS = "/adapters.xmi";
    //all 17 Patterns of van Doorn and Ba_brahem
    private static final String ABSTRACT_FACTORY = "/AbstractFactory.xmi";
    private static final String BA_BRAHEM = "/Ba_Brahem.xmi";
    private static final String BRIDGE = "/Bridge.xmi";
    private static final String BUILDER = "/Builder.xmi";
    private static final String CHAIN_OF_RESPONSIBILITY = "/ChainOfResponsibility.xmi";
    private static final String COMMAND = "/Command.xmi";
    private static final String COMPOSITE = "/Composite.xmi";
    private static final String DECORATOR = "/Decorator.xmi";
    private static final String FACTORY_METHOD = "/FactoryMethod.xmi";
    private static final String FLYWEIGHT = "/Flyweight.xmi";
    private static final String INTERPRETER = "/Interpreter.xmi";
    private static final String ITERATOR = "/Iterator.xmi";
    private static final String MEDIATOR = "/Mediator.xmi";
    private static final String MEMENTO = "/Memento.xmi";
    private static final String OBSERVER = "/Observer.xmi";
    private static final String PROXY = "/Proxy.xmi";
    private static final String STRATEGY = "/Strategy.xmi";
    // A test file containing invalid XML.
    private static final String INVALID_XML = "/invalid.xml";

    Relation relation;
    Node source;
    Node target;
    Attribute attr;
    
    /**
     * Exception rule.
     */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Tests the exception handling in case of document which could not be parsed, resulting in a {@link XMLStreamException} during
     * parsing a template file by a {@link TemplatesParserWithConditions}.
     */
    @Test
    public void testXMLStreamException() {
        final String path = getPath(INVALID_XML);
        final ArgoUMLToSystemGraphParser parser = new ArgoUMLToSystemGraphParser();

        thrown.expect(ParseException.class);
        thrown.expectCause(is(XMLStreamException.class));
        thrown.expectMessage("The XMI file " + path + " could not be parsed.");

        parser.parse(path);
    }

    /**
     * Tests the exception handling in case of a {@link IOException} during parsing a template file by a
     * {@link TemplatesParserWithConditions}.
     */
    @Test
    public void testFileNotFoundException() {
        final ArgoUMLToSystemGraphParser parser = new ArgoUMLToSystemGraphParser();

        thrown.expect(ParseException.class);
        thrown.expectCause(is(FileNotFoundException.class));
        thrown.expectMessage("The XMI file missing.xml could not be parsed.");

        parser.parse("missing.xml");
    }

    /**
     * Test the happy flow of parsing an XMI input file by the {@link ArgoUMLToSystemGraphParser}.
     */
    @Test
    public void testParse1() {
        final ArgoUMLToSystemGraphParser parser = new ArgoUMLToSystemGraphParser();
        final String path = getPath(VALID_ADAPTER);

        final SystemUnderConsiderationGraph suc = parser.parse(path);
        assertEquals("Adapters", suc.getName());

        //number of vertices and edges there is an undirected association, which has two edges (client-target, target-client)
        assertEquals(4, suc.edgeSet().size());
        assertEquals(4, suc.vertexSet().size());        
        //get nodes
        Node client = findVerticesByName(suc, "Client").get(0);
        Node target = findVerticesByName(suc, "Target").get(0);
        Node adapter = findVerticesByName(suc, "Adapter").get(0);
        Node adaptee = findVerticesByName(suc, "Adaptee").get(0);
        //node types
        assertEquals(NodeType.INTERFACE, target.getType());
        assertEquals(NodeType.CLASS, client.getType());
        assertEquals(NodeType.CLASS, adapter.getType());
        assertEquals(NodeType.CLASS, adaptee.getType());
        //number of edges per Node
        assertEquals(2, suc.edgesOf(client).size());
        assertEquals(3, suc.edgesOf(target).size());
        assertEquals(2, suc.edgesOf(adapter).size());
        assertEquals(1, suc.edgesOf(adaptee).size());
        //number of incoming edges per Node
        assertEquals(1, suc.incomingEdgesOf(client).size());
        assertEquals(2, suc.incomingEdgesOf(target).size());
        assertEquals(0, suc.incomingEdgesOf(adapter).size());
        assertEquals(1, suc.incomingEdgesOf(adaptee).size());
        //number of outgoing edges per Node
        assertEquals(1, suc.outgoingEdgesOf(client).size());
        assertEquals(1, suc.outgoingEdgesOf(target).size());
        assertEquals(2, suc.outgoingEdgesOf(adapter).size());
        assertEquals(0, suc.outgoingEdgesOf(adaptee).size());
        //node types
        assertEquals(NodeType.CLASS, client.getType());
        assertEquals(NodeType.INTERFACE, target.getType());
        assertEquals(NodeType.CLASS, adapter.getType());
        assertEquals(NodeType.CLASS, adaptee.getType());
        //get edges
        Relation clientTarget = suc.getEdge(client, target);
        Relation adapterTarget = suc.getEdge(adapter, target);
        Relation adapterAdaptee = suc.getEdge(adapter, adaptee);
        Relation targetClient = suc.getEdge(target, client);
        //check relationtypes
        assertTrue(relationTypeExists(clientTarget.getRelationTypes(),"S"));
        assertTrue(relationTypeExists(clientTarget.getRelationTypes(),"A"));
        assertTrue(relationTypeExists(targetClient.getRelationTypes(),"S"));
        assertTrue(relationTypeExists(adapterTarget.getRelationTypes(),"I"));
        assertTrue(relationTypeExists(adapterAdaptee.getRelationTypes(),"S"));
        assertTrue(relationTypeExists(adapterAdaptee.getRelationTypes(),"A"));
        //check cardinalities
        assertEquals(0, adapterAdaptee.getCardinalityLeft().getLower());
        assertEquals(-1, adapterAdaptee.getCardinalityLeft().getUpper());
        assertEquals(1, adapterAdaptee.getCardinalityRight().getLower());
        assertEquals(1, adapterAdaptee.getCardinalityRight().getUpper());
        //check attribute types
        assertEquals("adaptee", adapter.getAttributes().get(0).getName());
        assertEquals(adaptee, adapter.getAttributes().get(0).getType());
        assertEquals(Visibility.PRIVATE, adapter.getAttributes().get(0).getVisibility());
        assertEquals("adapter", client.getAttributes().get(0).getName());
        assertEquals(target, client.getAttributes().get(0).getType());
        assertEquals(Visibility.PUBLIC, client.getAttributes().get(0).getVisibility());
        //check method existence
        Method method = findMethodByName(client, "adapter.request");
        assertNotNull(method);
        method = findMethodByName(adapter, "request");
        assertNotNull(method);
	    method = findMethodByName(adaptee, "specificRequest");
	    assertNotNull(method);
	}

 	/**
     * Test the happy flow of parsing a more complicated XMI input file by the {@link ArgoUMLToSystemGraphParser}.
     */
    @Test
    public void testParse2() {
        final ArgoUMLToSystemGraphParser parser = new ArgoUMLToSystemGraphParser();
        final String path = getPath(VALID_ADAPTERS);

        final SystemUnderConsiderationGraph suc = parser.parse(path);
        assertEquals("Adapters", suc.getName());

        assertEquals(35, suc.edgeSet().size());
        assertEquals(38, suc.vertexSet().size());        

        //Examine edges in order of appearing in the .xmi
        source = findVerticesByName(suc, "SquarePegAdapter").get(0);
        target = findVerticesByName(suc, "SquarePeg").get(0);
        assertEquals(NodeType.CLASS, source.getType());
        assertEquals(NodeType.CLASS, target.getType());
        assertEquals(3, suc.edgesOf(source).size());
        assertEquals(1, suc.incomingEdgesOf(source).size());
        assertEquals(2, suc.outgoingEdgesOf(source).size());
        assertEquals(2, suc.edgesOf(target).size()); //edge to Double
        assertEquals(1, suc.incomingEdgesOf(target).size());
        assertEquals(1, suc.outgoingEdgesOf(target).size()); //edge to Double
        relation = suc.getEdge(source, target);
        assertEquals(2, relation.getRelationTypes().size());
        assertTrue(relationTypeExists(relation.getRelationTypes(),"S"));
        assertTrue(relationTypeExists(relation.getRelationTypes(),"A"));
        assertEquals("sp", source.getAttributes().get(0).getName());
        assertEquals(target, source.getAttributes().get(0).getType());
        assertEquals(Visibility.PRIVATE, source.getAttributes().get(0).getVisibility());       
        assertEquals("width", target.getAttributes().get(0).getName());
        assertEquals("Double", target.getAttributes().get(0).getType().getName());
        assertEquals(Visibility.PUBLIC, target.getAttributes().get(0).getVisibility());
        assertNotNull(findMethodByName(source,"makeFit"));
        assertNotNull(findMethodByName(target,"getWidth"));
        assertNotNull(findMethodByName(target,"setWidth"));

        source = findVerticesByName(suc, "AdapterDemoSquarePeg").get(0);
        target = findVerticesByName(suc, "SquarePegAdapter").get(0);
        assertEquals(NodeType.CLASS, source.getType());
        assertEquals(NodeType.CLASS, target.getType());
        assertEquals(2, suc.edgesOf(source).size());
        assertEquals(0, suc.incomingEdgesOf(source).size());
        assertEquals(2, suc.outgoingEdgesOf(source).size());
        assertEquals(3, suc.edgesOf(target).size());
        assertEquals(1, suc.incomingEdgesOf(target).size());
        assertEquals(2, suc.outgoingEdgesOf(source).size());
        relation = suc.getEdge(source, target);
        assertEquals(2, relation.getRelationTypes().size());
        assertTrue(relationTypeExists(relation.getRelationTypes(),"S"));
        assertTrue(relationTypeExists(relation.getRelationTypes(),"A"));
        attr = findAttributeByName(source, "spa");
		assertNotNull(attr);
        assertEquals(target, attr.getType());
        assertEquals(Visibility.PUBLIC, attr.getVisibility());
       
        source = findVerticesByName(suc, "AdapterDemoSquarePeg").get(0);
        target = findVerticesByName(suc, "RoundHole").get(0);
        assertEquals(NodeType.CLASS, source.getType());
        assertEquals(NodeType.CLASS, target.getType());
        assertEquals(2, suc.edgesOf(source).size());
        assertEquals(0, suc.incomingEdgesOf(source).size());
        assertEquals(2, suc.outgoingEdgesOf(source).size());
        assertEquals(2, suc.edgesOf(target).size());
        assertEquals(2, suc.incomingEdgesOf(target).size());
        assertEquals(0, suc.outgoingEdgesOf(target).size());
        relation = suc.getEdge(source, target);
        assertEquals(2, relation.getRelationTypes().size());
        assertTrue(relationTypeExists(relation.getRelationTypes(),"S"));
        assertTrue(relationTypeExists(relation.getRelationTypes(),"A"));
        attr = findAttributeByName(source, "rh");
		assertNotNull(attr);
        assertEquals(target, attr.getType());
        assertEquals(Visibility.PUBLIC, attr.getVisibility());
        assertNotNull(findMethodByName(target,"getRadius"));

        source = findVerticesByName(suc, "SquarePegAdapter").get(0);
        target = findVerticesByName(suc, "RoundHole").get(0);
        assertEquals(NodeType.CLASS, source.getType());
        assertEquals(NodeType.CLASS, target.getType());
        assertEquals(3, suc.edgesOf(source).size());
        assertEquals(1, suc.incomingEdgesOf(source).size());
        assertEquals(2, suc.outgoingEdgesOf(source).size());
        assertEquals(2, suc.edgesOf(target).size());
        assertEquals(2, suc.incomingEdgesOf(target).size());
        assertEquals(0, suc.outgoingEdgesOf(target).size());
        relation = suc.getEdge(source, target);
        assertEquals(2, relation.getRelationTypes().size());
        assertTrue(relationTypeExists(relation.getRelationTypes(),"S"));
        assertTrue(relationTypeExists(relation.getRelationTypes(),"P"));
       
        source = findVerticesByName(suc, "SquarePeg").get(0);
        target = findVerticesByName(suc, "Double").get(0);
        assertEquals(NodeType.CLASS, source.getType());
        assertEquals(NodeType.CLASS, target.getType());
        assertEquals(2, suc.edgesOf(source).size());
        assertEquals(1, suc.incomingEdgesOf(source).size());
        assertEquals(1, suc.outgoingEdgesOf(source).size());
        assertEquals(1, suc.edgesOf(target).size());
        assertEquals(1, suc.incomingEdgesOf(target).size());
        assertEquals(0, suc.outgoingEdgesOf(target).size());
        relation = suc.getEdge(source, target);
        assertEquals(3, relation.getRelationTypes().size());
        assertTrue(relationTypeExists(relation.getRelationTypes(),"R"));
        assertTrue(relationTypeExists(relation.getRelationTypes(),"A"));
        assertTrue(relationTypeExists(relation.getRelationTypes(),"P"));
        attr = findAttributeByName(source, "width");
 		assertNotNull(attr);
        assertEquals(target, attr.getType());
        assertEquals(Visibility.PUBLIC, attr.getVisibility());
        
        source = findVerticesByName(suc, "DrawingEditor").get(0);
        target = findVerticesByName(suc, "Shape").get(1); //there is an interface Shape and an abstract Class Shape
        assertEquals(NodeType.CLASS, source.getType());
        assertEquals(NodeType.CLASS, target.getType());
        assertTrue(target.isAbstract());
        assertEquals(1, suc.edgesOf(source).size());
        assertEquals(0, suc.incomingEdgesOf(source).size());
        assertEquals(1, suc.outgoingEdgesOf(source).size());
        assertEquals(5, suc.edgesOf(target).size()); //2 relations of parameters (Point, Manipulator)
        assertEquals(3, suc.incomingEdgesOf(target).size());
        assertEquals(2, suc.outgoingEdgesOf(target).size());
        relation = suc.getEdge(source, target);
        assertNotNull(relation);
        assertEquals(1, relation.getRelationTypes().size());
        assertTrue(relationTypeExists(relation.getRelationTypes(),"S"));
        assertNotNull(findMethodByName(target,"BoundingBox"));
        assertNotNull(findMethodByName(target,"CreateManipulator"));

        source = findVerticesByName(suc, "Line").get(0);
        target = findVerticesByName(suc, "Shape").get(1); //there is an interface Shape and an abstract Class Shape
        assertEquals(NodeType.CLASS, source.getType());
        assertEquals(NodeType.CLASS, target.getType());
        assertTrue(target.isAbstract());
        assertEquals(2, suc.edgesOf(source).size()); //hidden association with interface Shape
        assertEquals(0, suc.incomingEdgesOf(source).size());
        assertEquals(2, suc.outgoingEdgesOf(source).size()); //hidden association with interface Shape
        assertEquals(5, suc.edgesOf(target).size());
        assertEquals(3, suc.incomingEdgesOf(target).size());
        assertEquals(2, suc.outgoingEdgesOf(target).size());
        relation = suc.getEdge(source, target);
        assertNotNull(relation);
        assertEquals(1, relation.getRelationTypes().size());
        assertTrue(relationTypeExists(relation.getRelationTypes(),"X"));
        assertNotNull(findMethodByName(source,"BoundingBox"));
        assertNotNull(findMethodByName(source,"CreateManipulator"));
        relation = suc.getEdge(source, findVerticesByName(suc, "Shape").get(0)); //hidden dependency with interface Shape
        assertTrue(relationTypeExists(relation.getRelationTypes(),"D"));
        
        source = findVerticesByName(suc, "TextShape").get(0);
        target = findVerticesByName(suc, "Shape").get(1); //there is an interface Shape and an abstract Class Shape
        assertEquals(NodeType.CLASS, source.getType());
        assertEquals(NodeType.CLASS, target.getType());
        assertTrue(target.isAbstract());
        assertEquals(5, suc.edgesOf(source).size()); //hidden dependency with interface Shape + return relation to Point
        assertEquals(1, suc.incomingEdgesOf(source).size());
        assertEquals(4, suc.outgoingEdgesOf(source).size()); //hidden dependency with interface Shape + return relation to Point
        assertEquals(5, suc.edgesOf(target).size());
        assertEquals(3, suc.incomingEdgesOf(target).size());
        assertEquals(2, suc.outgoingEdgesOf(target).size());
        relation = suc.getEdge(source, target);
        assertNotNull(relation);
        assertEquals(1, relation.getRelationTypes().size());
        assertTrue(relationTypeExists(relation.getRelationTypes(),"X"));
        assertNotNull(findMethodByName(source,"BoundingBox"));
        assertNotNull(findMethodByName(source,"CreateManipulator"));
        relation = suc.getEdge(source, findVerticesByName(suc, "Shape").get(0)); //hidden dependency with interface Shape
        assertTrue(relationTypeExists(relation.getRelationTypes(),"D"));
  
        source = findVerticesByName(suc, "TextShape").get(0);
        target = findVerticesByName(suc, "TextView").get(0); 
        assertEquals(NodeType.CLASS, source.getType());
        assertEquals(NodeType.CLASS, target.getType());
        assertEquals(5, suc.edgesOf(source).size()); //hidden dependency with interface Shape + return relation to Point
        assertEquals(1, suc.incomingEdgesOf(source).size());
        assertEquals(4, suc.outgoingEdgesOf(source).size()); //hidden dependency with interface Shape + return relation to Point
        assertEquals(1, suc.edgesOf(target).size());
        assertEquals(1, suc.incomingEdgesOf(target).size());
        assertEquals(0, suc.outgoingEdgesOf(target).size());
        relation = suc.getEdge(source, target);
        assertNotNull(relation);
        assertEquals(2, relation.getRelationTypes().size());
        assertTrue(relationTypeExists(relation.getRelationTypes(),"S"));
        assertTrue(relationTypeExists(relation.getRelationTypes(),"A"));
        attr = findAttributeByName(source, "textview");
 		assertNotNull(attr);
        assertEquals(target, attr.getType());
        assertEquals(Visibility.PRIVATE, attr.getVisibility());
        assertNotNull(findMethodByName(target,"getExtent"));
         
        source = findVerticesByName(suc, "textShapeDemo").get(0);
        target = findVerticesByName(suc, "TextShape").get(0); 
        assertEquals(NodeType.CLASS, source.getType());
        assertEquals(NodeType.CLASS, target.getType());
        assertEquals(1, suc.edgesOf(source).size());
        assertEquals(0, suc.incomingEdgesOf(source).size());
        assertEquals(1, suc.outgoingEdgesOf(source).size());
        assertEquals(5, suc.edgesOf(target).size());  //hidden dependency with interface Shape + return relation to Point
        assertEquals(1, suc.incomingEdgesOf(target).size());
        assertEquals(4, suc.outgoingEdgesOf(target).size()); //hidden dependency with interface Shape + return relation to Point
        relation = suc.getEdge(source, target);
        assertNotNull(relation);
        assertEquals(2, relation.getRelationTypes().size());
        assertTrue(relationTypeExists(relation.getRelationTypes(),"S"));
        assertTrue(relationTypeExists(relation.getRelationTypes(),"A"));
        attr = findAttributeByName(source, "textShape");
 		assertNotNull(attr);
        assertEquals(target, attr.getType());
        assertEquals(Visibility.PUBLIC, attr.getVisibility());

        source = findVerticesByName(suc, "TextShape").get(0);
        target = findVerticesByName(suc, "Point").get(0); 
        relation = suc.getEdge(source, target);
        assertNotNull(relation);
        assertEquals(1, relation.getRelationTypes().size());
        assertTrue(relationTypeExists(relation.getRelationTypes(),"R"));
 
        source = findVerticesByName(suc, "Shape").get(1);
        target = findVerticesByName(suc, "Point").get(0); 
        relation = suc.getEdge(source, target);
        assertNotNull(relation);
        assertEquals(1, relation.getRelationTypes().size());
        assertTrue(relationTypeExists(relation.getRelationTypes(),"P"));

        source = findVerticesByName(suc, "MediaPlayerAdapterDemo").get(0);
        target = findVerticesByName(suc, "AudioPlayer").get(0); 
        assertEquals(NodeType.CLASS, source.getType());
        assertEquals(NodeType.CLASS, target.getType());
        assertEquals(1, suc.edgesOf(source).size());
        assertEquals(0, suc.incomingEdgesOf(source).size());
        assertEquals(1, suc.outgoingEdgesOf(source).size());
        assertEquals(3, suc.edgesOf(target).size());  //hidden dependency with interface Shape + return relation to Point
        assertEquals(1, suc.incomingEdgesOf(target).size());
        assertEquals(2, suc.outgoingEdgesOf(target).size()); //hidden dependency with interface Shape + return relation to Point
        relation = suc.getEdge(source, target);
        assertNotNull(relation);
        assertEquals(2, relation.getRelationTypes().size());
        assertTrue(relationTypeExists(relation.getRelationTypes(),"S"));
        assertTrue(relationTypeExists(relation.getRelationTypes(),"A"));
    }

    /**
     * Test the happy flow of parsing an XMI input file by the {@link ArgoUMLToSystemGraphParser}.
     */
    @Test
    public void testAbstractFactory() {
        final ArgoUMLToSystemGraphParser parser = new ArgoUMLToSystemGraphParser();
        final String path = getPath(ABSTRACT_FACTORY);

        final SystemUnderConsiderationGraph suc = parser.parse(path);
        assertEquals("untitledModel", suc.getName());

        //number of vertices and edges there is an undirected association, which has two edges (client-target, target-client)
        assertEquals(18, suc.edgeSet().size());
        assertEquals(13, suc.vertexSet().size());        
        //get nodes
        Node AbstrFact = findVerticesByName(suc, "AbstrFact").get(0);
        Node User = findVerticesByName(suc, "User").get(0);
        Node ConcFact2 = findVerticesByName(suc, "ConcFact2").get(0);
        Node ConcFact1 = findVerticesByName(suc, "ConcFact1").get(0);
        Node AbstrProdA = findVerticesByName(suc, "AbstrProdA").get(0);
        Node Prod2A = findVerticesByName(suc, "Prod2A").get(0);
        Node Prod1A = findVerticesByName(suc, "Prod1A").get(0);
        Node AbstrProdB = findVerticesByName(suc, "AbstrProdB").get(0);
        Node AbstrProdC = findVerticesByName(suc, "AbstrProdC").get(0);
        Node Prod1B = findVerticesByName(suc, "Prod1B").get(0);
        Node Prod2B = findVerticesByName(suc, "Prod2B").get(0);
        Node Prod1C = findVerticesByName(suc, "Prod1C").get(0);
        Node Prod2C = findVerticesByName(suc, "Prod2C").get(0);
        //node types
        assertEquals(NodeType.INTERFACE, AbstrFact.getType());
        assertEquals(NodeType.CLASS, User.getType());
        assertEquals(NodeType.CLASS, ConcFact2.getType());
        assertEquals(NodeType.CLASS, ConcFact1.getType());
        assertEquals(NodeType.INTERFACE, AbstrProdA.getType());
        assertEquals(NodeType.CLASS, Prod2A.getType());
        assertEquals(NodeType.CLASS, Prod1A.getType());
        assertEquals(NodeType.INTERFACE, AbstrProdB.getType());
        assertEquals(NodeType.INTERFACE, AbstrProdC.getType());
        assertEquals(NodeType.CLASS, Prod1B.getType());
        assertEquals(NodeType.CLASS, Prod2B.getType());
        assertEquals(NodeType.CLASS, Prod1C.getType());
        assertEquals(NodeType.CLASS, Prod2C.getType());
        //number of edges per Node
        assertEquals(3, suc.edgesOf(AbstrFact).size());
        assertEquals(4, suc.edgesOf(User).size());
        assertEquals(4, suc.edgesOf(ConcFact2).size());
        assertEquals(4, suc.edgesOf(ConcFact1).size());
        assertEquals(3, suc.edgesOf(AbstrProdA).size());
        assertEquals(2, suc.edgesOf(Prod2A).size());
        assertEquals(2, suc.edgesOf(Prod1A).size());
        assertEquals(3, suc.edgesOf(AbstrProdB).size());
        assertEquals(3, suc.edgesOf(AbstrProdC).size());
        assertEquals(2, suc.edgesOf(Prod1B).size());
        assertEquals(2, suc.edgesOf(Prod2B).size());
        assertEquals(2, suc.edgesOf(Prod1C).size());
        assertEquals(2, suc.edgesOf(Prod2C).size());
        //number of incoming edges per Node
        assertEquals(3, suc.incomingEdgesOf(AbstrFact).size());
        assertEquals(0, suc.incomingEdgesOf(User).size());
        assertEquals(0, suc.incomingEdgesOf(ConcFact2).size());
        assertEquals(0, suc.incomingEdgesOf(ConcFact1).size());
        assertEquals(3, suc.incomingEdgesOf(AbstrProdA).size());
        assertEquals(1, suc.incomingEdgesOf(Prod2A).size());
        assertEquals(1, suc.incomingEdgesOf(Prod1A).size());
        assertEquals(3, suc.incomingEdgesOf(AbstrProdB).size());
        assertEquals(3, suc.incomingEdgesOf(AbstrProdC).size());
        assertEquals(1, suc.incomingEdgesOf(Prod1B).size());
        assertEquals(1, suc.incomingEdgesOf(Prod2B).size());
        assertEquals(1, suc.incomingEdgesOf(Prod1C).size());
        assertEquals(1, suc.incomingEdgesOf(Prod2C).size());
       //number of outgoing edges per Node
        assertEquals(0, suc.outgoingEdgesOf(AbstrFact).size());
        assertEquals(4, suc.outgoingEdgesOf(User).size());
        assertEquals(4, suc.outgoingEdgesOf(ConcFact2).size());
        assertEquals(4, suc.outgoingEdgesOf(ConcFact1).size());
        assertEquals(0, suc.outgoingEdgesOf(AbstrProdA).size());
        assertEquals(1, suc.outgoingEdgesOf(Prod2A).size());
        assertEquals(1, suc.outgoingEdgesOf(Prod1A).size());
        assertEquals(0, suc.outgoingEdgesOf(AbstrProdB).size());
        assertEquals(0, suc.outgoingEdgesOf(AbstrProdC).size());
        assertEquals(1, suc.outgoingEdgesOf(Prod1B).size());
        assertEquals(1, suc.outgoingEdgesOf(Prod2B).size());
        assertEquals(1, suc.outgoingEdgesOf(Prod1C).size());
        assertEquals(1, suc.outgoingEdgesOf(Prod2C).size());
        //get edges
        Relation userAbstrFact = suc.getEdge(User, AbstrFact);
        Relation userAbstrProdA = suc.getEdge(User, AbstrProdA);
        Relation userAbstrProdB = suc.getEdge(User, AbstrProdB);
        Relation userAbstrProdC = suc.getEdge(User, AbstrProdC);
        Relation concFact1AbstrFact = suc.getEdge(ConcFact1, AbstrFact);
        Relation concFact2AbstrFact = suc.getEdge(ConcFact2, AbstrFact);
        Relation concFact1Prod1A = suc.getEdge(ConcFact1, Prod1A);
        Relation concFact1Prod1B = suc.getEdge(ConcFact1, Prod1B);
        Relation concFact1Prod1C = suc.getEdge(ConcFact1, Prod1C);
        Relation concFact2Prod2A = suc.getEdge(ConcFact2, Prod2A);
        Relation concFact2Prod2B = suc.getEdge(ConcFact2, Prod2B);
        Relation concFact2Prod2C = suc.getEdge(ConcFact2, Prod2C);
        Relation prod1AAbstrProdA = suc.getEdge(Prod1A, AbstrProdA);
        Relation prod1BAbstrProdB = suc.getEdge(Prod1B, AbstrProdB);
        Relation prod1CAbstrProdC = suc.getEdge(Prod1C, AbstrProdC);
        Relation prod2AAbstrProdA = suc.getEdge(Prod2A, AbstrProdA);
        Relation prod2BAbstrProdB = suc.getEdge(Prod2B, AbstrProdB);
        Relation prod2CAbstrProdC = suc.getEdge(Prod2C, AbstrProdC);
        //check relationtypes
        assertTrue(relationTypeExists(userAbstrFact.getRelationTypes(),"S"));
        assertTrue(relationTypeExists(userAbstrProdA.getRelationTypes(),"S"));
        assertTrue(relationTypeExists(userAbstrProdB.getRelationTypes(),"S"));
        assertTrue(relationTypeExists(userAbstrProdC.getRelationTypes(),"S"));
        assertTrue(relationTypeExists(concFact1AbstrFact.getRelationTypes(),"I"));
        assertTrue(relationTypeExists(concFact2AbstrFact.getRelationTypes(),"I"));
        assertTrue(relationTypeExists(concFact1Prod1A.getRelationTypes(),"D"));
        assertTrue(relationTypeExists(concFact1Prod1B.getRelationTypes(),"D"));
        assertTrue(relationTypeExists(concFact1Prod1C.getRelationTypes(),"D"));
        assertTrue(relationTypeExists(concFact2Prod2A.getRelationTypes(),"D"));
        assertTrue(relationTypeExists(concFact2Prod2B.getRelationTypes(),"D"));
        assertTrue(relationTypeExists(concFact2Prod2C.getRelationTypes(),"D"));
        assertTrue(relationTypeExists(prod1AAbstrProdA.getRelationTypes(),"I"));
        assertTrue(relationTypeExists(prod1BAbstrProdB.getRelationTypes(),"I"));
        assertTrue(relationTypeExists(prod1CAbstrProdC.getRelationTypes(),"I"));
        assertTrue(relationTypeExists(prod2AAbstrProdA.getRelationTypes(),"I"));
        assertTrue(relationTypeExists(prod2BAbstrProdB.getRelationTypes(),"I"));
        assertTrue(relationTypeExists(prod2CAbstrProdC.getRelationTypes(),"I"));
    }

    /**
     * Test the happy flow of parsing an XMI input file by the {@link ArgoUMLSystemParser}.
     */
    @Test
    public void testParseBaBrahem() {
        final ArgoUMLToSystemGraphParser parser = new ArgoUMLToSystemGraphParser();
        final String path = getPath(BA_BRAHEM);

        final SystemUnderConsiderationGraph suc = parser.parse(path);
        assertEquals("untitledModel", suc.getName());

        assertEquals(6, suc.edgeSet().size());
        assertEquals(5, suc.vertexSet().size());        

        source = findVerticesByName(suc, "A").get(0);
        target = findVerticesByName(suc, "B").get(0);
        assertEquals(NodeType.CLASS, source.getType());
        assertEquals(NodeType.CLASS, target.getType());
        assertEquals(2, suc.edgesOf(source).size());
        assertEquals(0, suc.incomingEdgesOf(source).size());
        assertEquals(2, suc.outgoingEdgesOf(source).size());
        assertEquals(3, suc.edgesOf(target).size()); //edge to Double
        assertEquals(3, suc.incomingEdgesOf(target).size());
        assertEquals(0, suc.outgoingEdgesOf(target).size()); //edge to Double
        relation = suc.getEdge(source, target);
        assertEquals(1, relation.getRelationTypes().size());
        assertTrue(relationTypeExists(relation.getRelationTypes(),"S"));
 
        source = findVerticesByName(suc, "A").get(0);
        target = findVerticesByName(suc, "C").get(0);
        assertEquals(NodeType.CLASS, target.getType());
        assertEquals(3, suc.edgesOf(target).size()); //edge to Double
        assertEquals(2, suc.incomingEdgesOf(target).size());
        assertEquals(1, suc.outgoingEdgesOf(target).size()); //edge to Double
        relation = suc.getEdge(source, target);
        assertEquals(1, relation.getRelationTypes().size());
        assertTrue(relationTypeExists(relation.getRelationTypes(),"S"));

        source = findVerticesByName(suc, "D").get(0);
        target = findVerticesByName(suc, "B").get(0);
        assertEquals(NodeType.CLASS, source.getType());
        assertEquals(2, suc.edgesOf(source).size()); //edge to Double
        assertEquals(0, suc.incomingEdgesOf(source).size());
        assertEquals(2, suc.outgoingEdgesOf(source).size()); //edge to Double
        relation = suc.getEdge(source, target);
        assertEquals(1, relation.getRelationTypes().size());
        assertTrue(relationTypeExists(relation.getRelationTypes(),"X"));

        source = findVerticesByName(suc, "D").get(0);
        target = findVerticesByName(suc, "E").get(0);
        assertEquals(NodeType.CLASS, target.getType());
        assertEquals(2, suc.edgesOf(target).size()); //edge to Double
        assertEquals(1, suc.incomingEdgesOf(target).size());
        assertEquals(1, suc.outgoingEdgesOf(target).size()); //edge to Double
        relation = suc.getEdge(source, target);
        assertEquals(1, relation.getRelationTypes().size());
        assertTrue(relationTypeExists(relation.getRelationTypes(),"D"));

        source = findVerticesByName(suc, "E").get(0);
        target = findVerticesByName(suc, "C").get(0);
        relation = suc.getEdge(source, target);
        assertEquals(1, relation.getRelationTypes().size());
        assertTrue(relationTypeExists(relation.getRelationTypes(),"X"));
    }

    /**
     * Test the happy flow of parsing an XMI input file by the {@link ArgoUMLSystemParser}.
     */
    @Test
    public void testParseBridge() {
        final ArgoUMLToSystemGraphParser parser = new ArgoUMLToSystemGraphParser();
        final String path = getPath(BRIDGE);

        final SystemUnderConsiderationGraph suc = parser.parse(path);
        assertEquals("untitledModel", suc.getName());

        assertEquals(8, suc.edgeSet().size());
        assertEquals(8, suc.vertexSet().size());        

        source = findVerticesByName(suc, "Client").get(0);
        target = findVerticesByName(suc, "Ab").get(0);
        assertEquals(NodeType.CLASS, source.getType());
        assertEquals(NodeType.INTERFACE, target.getType());
        assertEquals(1, suc.edgesOf(source).size());
        assertEquals(0, suc.incomingEdgesOf(source).size());
        assertEquals(1, suc.outgoingEdgesOf(source).size());
        assertEquals(4, suc.edgesOf(target).size());
        assertEquals(3, suc.incomingEdgesOf(target).size());
        assertEquals(1, suc.outgoingEdgesOf(target).size());
        relation = suc.getEdge(source, target);
        assertEquals(1, relation.getRelationTypes().size());
        assertTrue(relationTypeExists(relation.getRelationTypes(),"S"));
 
        source = findVerticesByName(suc, "ConcAbstr1").get(0);
        target = findVerticesByName(suc, "Ab").get(0);
        assertEquals(NodeType.CLASS, source.getType());
        assertEquals(NodeType.INTERFACE, target.getType());
        assertEquals(1, suc.edgesOf(source).size());
        assertEquals(0, suc.incomingEdgesOf(source).size());
        assertEquals(1, suc.outgoingEdgesOf(source).size());
        relation = suc.getEdge(source, target);
        assertEquals(1, relation.getRelationTypes().size());
        assertTrue(relationTypeExists(relation.getRelationTypes(),"I"));
 
        source = findVerticesByName(suc, "ConcAbstr2").get(0);
        target = findVerticesByName(suc, "Ab").get(0);
        assertEquals(NodeType.CLASS, source.getType());
        assertEquals(NodeType.INTERFACE, target.getType());
        assertEquals(2, suc.edgesOf(source).size());
        assertEquals(0, suc.incomingEdgesOf(source).size());
        assertEquals(2, suc.outgoingEdgesOf(source).size());
        relation = suc.getEdge(source, target);
        assertEquals(1, relation.getRelationTypes().size());
        assertTrue(relationTypeExists(relation.getRelationTypes(),"I"));
 
        source = findVerticesByName(suc, "ConcAbstr2").get(0);
        target = findVerticesByName(suc, "ConcImpl1").get(0);
        assertEquals(NodeType.CLASS, source.getType());
        assertEquals(NodeType.CLASS, target.getType());
        assertEquals(2, suc.edgesOf(target).size());
        assertEquals(1, suc.incomingEdgesOf(target).size());
        assertEquals(1, suc.outgoingEdgesOf(target).size());
        relation = suc.getEdge(source, target);
        assertEquals(1, relation.getRelationTypes().size());
        assertTrue(relationTypeExists(relation.getRelationTypes(),"S"));
 
        source = findVerticesByName(suc, "ConcImpl1").get(0);
        target = findVerticesByName(suc, "Impl").get(0);
        assertEquals(NodeType.CLASS, source.getType());
        assertEquals(NodeType.INTERFACE, target.getType());
        assertEquals(4, suc.edgesOf(target).size());
        assertEquals(4, suc.incomingEdgesOf(target).size());
        assertEquals(0, suc.outgoingEdgesOf(target).size());
        relation = suc.getEdge(source, target);
        assertEquals(1, relation.getRelationTypes().size());
        assertTrue(relationTypeExists(relation.getRelationTypes(),"I"));
 
        source = findVerticesByName(suc, "ConcImpl2").get(0);
        target = findVerticesByName(suc, "Impl").get(0);
        assertEquals(NodeType.CLASS, source.getType());
        assertEquals(NodeType.INTERFACE, target.getType());
        relation = suc.getEdge(source, target);
        assertEquals(1, relation.getRelationTypes().size());
        assertTrue(relationTypeExists(relation.getRelationTypes(),"I"));
 
        source = findVerticesByName(suc, "ConcImpl3").get(0);
        target = findVerticesByName(suc, "Impl").get(0);
        assertEquals(NodeType.CLASS, source.getType());
        assertEquals(NodeType.INTERFACE, target.getType());
        relation = suc.getEdge(source, target);
        assertEquals(1, relation.getRelationTypes().size());
        assertTrue(relationTypeExists(relation.getRelationTypes(),"I"));
 
        source = findVerticesByName(suc, "Ab").get(0);
        target = findVerticesByName(suc, "Impl").get(0);
        assertEquals(NodeType.INTERFACE, source.getType());
        assertEquals(NodeType.INTERFACE, target.getType());
        relation = suc.getEdge(source, target);
        assertEquals(1, relation.getRelationTypes().size());
        assertTrue(relationTypeExists(relation.getRelationTypes(),"S"));
 
    }

    /**
     * Test the happy flow of parsing an XMI input file by the {@link ArgoUMLSystemParser}.
     */
    @Test
    public void testParseBuilder() {
        final ArgoUMLToSystemGraphParser parser = new ArgoUMLToSystemGraphParser();
        final String path = getPath(BUILDER);

        final SystemUnderConsiderationGraph suc = parser.parse(path);

        assertEquals("untitledModel", suc.getName());

        assertEquals(3, suc.edgeSet().size());
        assertEquals(4, suc.vertexSet().size());        

        source = findVerticesByName(suc, "ConcrMaker").get(0);
        target = findVerticesByName(suc, "Prod").get(0);
        assertEquals(NodeType.CLASS, source.getType());
        assertEquals(NodeType.CLASS, target.getType());
        assertEquals(2, suc.edgesOf(source).size());
        assertEquals(0, suc.incomingEdgesOf(source).size());
        assertEquals(2, suc.outgoingEdgesOf(source).size());
        assertEquals(1, suc.edgesOf(target).size());
        assertEquals(1, suc.incomingEdgesOf(target).size());
        assertEquals(0, suc.outgoingEdgesOf(target).size());
        relation = suc.getEdge(source, target);
        assertEquals(1, relation.getRelationTypes().size());
        assertTrue(relationTypeExists(relation.getRelationTypes(),"D"));
 
        source = findVerticesByName(suc, "ConcrMaker").get(0);
        target = findVerticesByName(suc, "Maker").get(0);
        assertEquals(NodeType.CLASS, source.getType());
        assertEquals(NodeType.CLASS, target.getType());
        assertEquals(2, suc.edgesOf(target).size());
        assertEquals(2, suc.incomingEdgesOf(target).size());
        assertEquals(0, suc.outgoingEdgesOf(target).size());
        relation = suc.getEdge(source, target);
        assertEquals(1, relation.getRelationTypes().size());
        assertTrue(relationTypeExists(relation.getRelationTypes(),"X"));
 
        source = findVerticesByName(suc, "Direc").get(0);
        target = findVerticesByName(suc, "Maker").get(0);
        assertEquals(NodeType.CLASS, source.getType());
        assertEquals(NodeType.CLASS, target.getType());
        relation = suc.getEdge(source, target);
        assertEquals(1, relation.getRelationTypes().size());
        assertTrue(relationTypeExists(relation.getRelationTypes(),"S"));
     }

    /**
     * Test the happy flow of parsing an XMI input file by the {@link ArgoUMLSystemParser}.
     */
    @Test
    public void testParseChainOfResponsibility() {
        final ArgoUMLToSystemGraphParser parser = new ArgoUMLToSystemGraphParser();
        final String path = getPath(CHAIN_OF_RESPONSIBILITY);

        final SystemUnderConsiderationGraph suc = parser.parse(path);

        assertEquals("untitledModel", suc.getName());

        assertEquals(4, suc.edgeSet().size());
        assertEquals(4, suc.vertexSet().size());        

        source = findVerticesByName(suc, "User").get(0);
        target = findVerticesByName(suc, "Processor").get(0);
        assertEquals(NodeType.CLASS, source.getType());
        assertEquals(NodeType.INTERFACE, target.getType());
        assertEquals(1, suc.edgesOf(source).size());
        assertEquals(0, suc.incomingEdgesOf(source).size());
        assertEquals(1, suc.outgoingEdgesOf(source).size());
        assertEquals(4, suc.edgesOf(target).size());
        assertEquals(4, suc.incomingEdgesOf(target).size());
        assertEquals(1, suc.outgoingEdgesOf(target).size());
        relation = suc.getEdge(source, target);
        assertEquals(1, relation.getRelationTypes().size());
        assertTrue(relationTypeExists(relation.getRelationTypes(),"S"));
 
        source = findVerticesByName(suc, "Processor").get(0);
        target = findVerticesByName(suc, "Processor").get(0);
        assertEquals(NodeType.INTERFACE, source.getType());
        assertEquals(NodeType.INTERFACE, target.getType());
        relation = suc.getEdge(source, target);
        assertEquals(1, relation.getRelationTypes().size());
        assertTrue(relationTypeExists(relation.getRelationTypes(),"S"));
 
        source = findVerticesByName(suc, "ConcProcess1").get(0);
        target = findVerticesByName(suc, "Processor").get(0);
        assertEquals(NodeType.CLASS, source.getType());
        assertEquals(NodeType.INTERFACE, target.getType());
        assertEquals(1, suc.edgesOf(source).size());
        assertEquals(0, suc.incomingEdgesOf(source).size());
        assertEquals(1, suc.outgoingEdgesOf(source).size());
        relation = suc.getEdge(source, target);
        assertEquals(1, relation.getRelationTypes().size());
        assertTrue(relationTypeExists(relation.getRelationTypes(),"I"));
 
        source = findVerticesByName(suc, "ConcProcess2").get(0);
        target = findVerticesByName(suc, "Processor").get(0);
        assertEquals(NodeType.CLASS, source.getType());
        assertEquals(NodeType.INTERFACE, target.getType());
        assertEquals(1, suc.edgesOf(source).size());
        assertEquals(0, suc.incomingEdgesOf(source).size());
        assertEquals(1, suc.outgoingEdgesOf(source).size());
        relation = suc.getEdge(source, target);
        assertEquals(1, relation.getRelationTypes().size());
        assertTrue(relationTypeExists(relation.getRelationTypes(),"I"));
     }

    /**
     * Test the happy flow of parsing an XMI input file by the {@link ArgoUMLSystemParser}.
     */
    @Test
    public void testParseCommand() {
        final ArgoUMLToSystemGraphParser parser = new ArgoUMLToSystemGraphParser();
        final String path = getPath(COMMAND);

        final SystemUnderConsiderationGraph suc = parser.parse(path);

        assertEquals("untitledModel", suc.getName());

        assertEquals(5, suc.edgeSet().size());
        assertEquals(5, suc.vertexSet().size());        

        source = findVerticesByName(suc, "User").get(0);
        target = findVerticesByName(suc, "Rec").get(0);
        assertEquals(NodeType.CLASS, source.getType());
        assertEquals(NodeType.CLASS, target.getType());
        assertEquals(2, suc.edgesOf(source).size());
        assertEquals(0, suc.incomingEdgesOf(source).size());
        assertEquals(2, suc.outgoingEdgesOf(source).size());
        assertEquals(2, suc.edgesOf(target).size());
        assertEquals(2, suc.incomingEdgesOf(target).size());
        assertEquals(0, suc.outgoingEdgesOf(target).size());
        relation = suc.getEdge(source, target);
        assertEquals(1, relation.getRelationTypes().size());
        assertTrue(relationTypeExists(relation.getRelationTypes(),"S"));

        source = findVerticesByName(suc, "User").get(0);
        target = findVerticesByName(suc, "ConcTask").get(0);
        assertEquals(NodeType.CLASS, source.getType());
        assertEquals(NodeType.CLASS, target.getType());
        assertEquals(3, suc.edgesOf(target).size());
        assertEquals(1, suc.incomingEdgesOf(target).size());
        assertEquals(2, suc.outgoingEdgesOf(target).size());
        relation = suc.getEdge(source, target);
        assertEquals(1, relation.getRelationTypes().size());
        assertTrue(relationTypeExists(relation.getRelationTypes(),"D"));

        source = findVerticesByName(suc, "ConcTask").get(0);
        target = findVerticesByName(suc, "Task").get(0);
        assertEquals(NodeType.CLASS, source.getType());
        assertEquals(NodeType.INTERFACE, target.getType());
        assertEquals(2, suc.edgesOf(target).size());
        assertEquals(2, suc.incomingEdgesOf(target).size());
        assertEquals(0, suc.outgoingEdgesOf(target).size());
        relation = suc.getEdge(source, target);
        assertEquals(1, relation.getRelationTypes().size());
        assertTrue(relationTypeExists(relation.getRelationTypes(),"I"));

        source = findVerticesByName(suc, "Caller").get(0);
        target = findVerticesByName(suc, "Task").get(0);
        assertEquals(NodeType.CLASS, source.getType());
        assertEquals(NodeType.INTERFACE, target.getType());
        assertEquals(1, suc.edgesOf(source).size());
        assertEquals(0, suc.incomingEdgesOf(source).size());
        assertEquals(1, suc.outgoingEdgesOf(source).size());
        relation = suc.getEdge(source, target);
        assertEquals(1, relation.getRelationTypes().size());
        assertTrue(relationTypeExists(relation.getRelationTypes(),"S"));
 
        source = findVerticesByName(suc, "ConcTask").get(0);
        target = findVerticesByName(suc, "Rec").get(0);
        assertEquals(NodeType.CLASS, source.getType());
        assertEquals(NodeType.CLASS, target.getType());
        relation = suc.getEdge(source, target);
        assertEquals(1, relation.getRelationTypes().size());
        assertTrue(relationTypeExists(relation.getRelationTypes(),"S"));
    }

    /**
     * Test the happy flow of parsing an XMI input file by the {@link ArgoUMLSystemParser}.
     */
    @Test
    public void testParseComposite() {
        final ArgoUMLToSystemGraphParser parser = new ArgoUMLToSystemGraphParser();
        final String path = getPath(COMPOSITE);

        final SystemUnderConsiderationGraph suc = parser.parse(path);

        assertEquals("Composite", suc.getName());

        assertEquals(3, suc.edgeSet().size());
        assertEquals(4, suc.vertexSet().size());        

        source = findVerticesByName(suc, "User").get(0);
        target = findVerticesByName(suc, "Part").get(0);
        assertEquals(NodeType.CLASS, source.getType());
        assertEquals(NodeType.INTERFACE, target.getType());
        assertEquals(1, suc.edgesOf(source).size());
        assertEquals(0, suc.incomingEdgesOf(source).size());
        assertEquals(1, suc.outgoingEdgesOf(source).size());
        assertEquals(3, suc.edgesOf(target).size());
        assertEquals(3, suc.incomingEdgesOf(target).size());
        assertEquals(0, suc.outgoingEdgesOf(target).size());
        relation = suc.getEdge(source, target);
        assertEquals(1, relation.getRelationTypes().size());
        assertTrue(relationTypeExists(relation.getRelationTypes(),"S"));

        source = findVerticesByName(suc, "Union").get(0);
        target = findVerticesByName(suc, "Part").get(0);
        assertEquals(NodeType.CLASS, source.getType());
        assertEquals(NodeType.INTERFACE, target.getType());
        assertEquals(1, suc.edgesOf(source).size());
        assertEquals(0, suc.incomingEdgesOf(source).size());
        assertEquals(1, suc.outgoingEdgesOf(source).size());
        relation = suc.getEdge(source, target);
        assertEquals(2, relation.getRelationTypes().size());
        assertTrue(relationTypeExists(relation.getRelationTypes(),"S"));
        assertTrue(relationTypeExists(relation.getRelationTypes(),"I"));

        source = findVerticesByName(suc, "End").get(0);
        target = findVerticesByName(suc, "Part").get(0);
        assertEquals(NodeType.CLASS, source.getType());
        assertEquals(NodeType.INTERFACE, target.getType());
        assertEquals(1, suc.edgesOf(source).size());
        assertEquals(0, suc.incomingEdgesOf(source).size());
        assertEquals(1, suc.outgoingEdgesOf(source).size());
        relation = suc.getEdge(source, target);
        assertEquals(1, relation.getRelationTypes().size());
        assertTrue(relationTypeExists(relation.getRelationTypes(),"I"));
    }

    /**
     * Test the happy flow of parsing an XMI input file by the {@link ArgoUMLSystemParser}.
     */
    @Test
    public void testParseDecorator() {
        final ArgoUMLToSystemGraphParser parser = new ArgoUMLToSystemGraphParser();
        final String path = getPath(DECORATOR);

        final SystemUnderConsiderationGraph suc = parser.parse(path);

        assertEquals("untitledProfile", suc.getName());

        assertEquals(4, suc.edgeSet().size());
        assertEquals(5, suc.vertexSet().size());        

        source = findVerticesByName(suc, "Dec").get(0);
        target = findVerticesByName(suc, "Part").get(0);
        assertEquals(NodeType.CLASS, source.getType());
        assertEquals(NodeType.INTERFACE, target.getType());
        assertEquals(3, suc.edgesOf(source).size());
        assertEquals(2, suc.incomingEdgesOf(source).size());
        assertEquals(1, suc.outgoingEdgesOf(source).size());
        assertEquals(2, suc.edgesOf(target).size());
        assertEquals(2, suc.incomingEdgesOf(target).size());
        assertEquals(0, suc.outgoingEdgesOf(target).size());
        relation = suc.getEdge(source, target);
        assertEquals(2, relation.getRelationTypes().size());
        assertTrue(relationTypeExists(relation.getRelationTypes(),"S"));
        assertTrue(relationTypeExists(relation.getRelationTypes(),"I"));

        source = findVerticesByName(suc, "ConcretePart").get(0);
        target = findVerticesByName(suc, "Part").get(0);
        assertEquals(NodeType.CLASS, source.getType());
        assertEquals(NodeType.INTERFACE, target.getType());
        assertEquals(1, suc.edgesOf(source).size());
        assertEquals(0, suc.incomingEdgesOf(source).size());
        assertEquals(1, suc.outgoingEdgesOf(source).size());
        relation = suc.getEdge(source, target);
        assertEquals(1, relation.getRelationTypes().size());
        assertTrue(relationTypeExists(relation.getRelationTypes(),"I"));

        source = findVerticesByName(suc, "ConcDecA").get(0);
        target = findVerticesByName(suc, "Dec").get(0);
        assertEquals(NodeType.CLASS, source.getType());
        assertEquals(NodeType.CLASS, target.getType());
        assertEquals(1, suc.edgesOf(source).size());
        assertEquals(0, suc.incomingEdgesOf(source).size());
        assertEquals(1, suc.outgoingEdgesOf(source).size());
        relation = suc.getEdge(source, target);
        assertEquals(1, relation.getRelationTypes().size());
        assertTrue(relationTypeExists(relation.getRelationTypes(),"X"));

        source = findVerticesByName(suc, "ConcDecB").get(0);
        target = findVerticesByName(suc, "Dec").get(0);
        assertEquals(NodeType.CLASS, source.getType());
        assertEquals(NodeType.CLASS, target.getType());
        assertEquals(1, suc.edgesOf(source).size());
        assertEquals(0, suc.incomingEdgesOf(source).size());
        assertEquals(1, suc.outgoingEdgesOf(source).size());
        relation = suc.getEdge(source, target);
        assertEquals(1, relation.getRelationTypes().size());
        assertTrue(relationTypeExists(relation.getRelationTypes(),"X"));

    }

    /**
     * Test the happy flow of parsing an XMI input file by the {@link ArgoUMLSystemParser}.
     */
    @Test
    public void testParseFactoryMethod() {
        final ArgoUMLSystemParser parser = new ArgoUMLSystemParser();
        final String path = getPath(FACTORY_METHOD);
        Edge edge;

        final SystemUnderConsideration parseResult = parser.parse(path);

        assertEquals(4, TestHelper.countUniqueNodes(parseResult.getEdges()));
        assertEquals(3, parseResult.getEdges().size());

        //Examine edges in order of appearing in the .xmi
        edge = parseResult.getEdges().get(0);
        assertEquals(EdgeType.DEPENDENCY, edge.getRelationType());
        assertEquals("ConcreteCreator", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("ConcreteProduct", edge.getRightNode().getName());
        assertEquals(NodeType.CLASS, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(1);
        assertEquals(EdgeType.INHERITANCE, edge.getRelationType());
        assertEquals("ConcreteProduct", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("Product", edge.getRightNode().getName());
        assertEquals(NodeType.CLASS, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(2);
        assertEquals(EdgeType.INHERITANCE, edge.getRelationType());
        assertEquals("ConcreteCreator", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("Creator", edge.getRightNode().getName());
        assertEquals(NodeType.CLASS, edge.getRightNode().getType());
    }

    /**
     * Test the happy flow of parsing an XMI input file by the {@link ArgoUMLSystemParser}.
     */
    @Test
    public void testParseFlyweight() {
        final ArgoUMLSystemParser parser = new ArgoUMLSystemParser();
        final String path = getPath(FLYWEIGHT);
        Edge edge;

        final SystemUnderConsideration parseResult = parser.parse(path);

        assertEquals(5, TestHelper.countUniqueNodes(parseResult.getEdges()));
        assertEquals(6, parseResult.getEdges().size());

        //Examine edges in order of appearing in the .xmi
        edge = parseResult.getEdges().get(0);
        assertEquals(EdgeType.ASSOCIATION_DIRECTED, edge.getRelationType());
        assertEquals("Client", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("FlyWFac", edge.getRightNode().getName());
        assertEquals(NodeType.CLASS, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(1);
        assertEquals(EdgeType.ASSOCIATION_DIRECTED, edge.getRelationType());
        assertEquals("Client", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("ConcrFlyW", edge.getRightNode().getName());
        assertEquals(NodeType.CLASS, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(2);
        assertEquals(EdgeType.ASSOCIATION_DIRECTED, edge.getRelationType());
        assertEquals("Client", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("UnshConcrFlyw", edge.getRightNode().getName());
        assertEquals(NodeType.CLASS, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(3);
        assertEquals(EdgeType.INHERITANCE, edge.getRelationType());
        assertEquals("ConcrFlyW", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("FlyW", edge.getRightNode().getName());
        assertEquals(NodeType.CLASS, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(4);
        assertEquals(EdgeType.INHERITANCE, edge.getRelationType());
        assertEquals("UnshConcrFlyw", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("FlyW", edge.getRightNode().getName());
        assertEquals(NodeType.CLASS, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(5);
        assertEquals(EdgeType.ASSOCIATION_DIRECTED, edge.getRelationType());
        assertEquals("FlyWFac", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("FlyW", edge.getRightNode().getName());
        assertEquals(NodeType.CLASS, edge.getRightNode().getType());
    }

    /**
     * Test the happy flow of parsing an XMI input file by the {@link ArgoUMLSystemParser}.
     */
    @Test
    public void testParseInterpreter() {
        final ArgoUMLSystemParser parser = new ArgoUMLSystemParser();
        final String path = getPath(INTERPRETER);
        Edge edge;

        final SystemUnderConsideration parseResult = parser.parse(path);

        assertEquals(5, TestHelper.countUniqueNodes(parseResult.getEdges()));
        assertEquals(5, parseResult.getEdges().size());

        //Examine edges in order of appearing in the .xmi
        edge = parseResult.getEdges().get(0);
        assertEquals(EdgeType.REALIZATION, edge.getRelationType());
        assertEquals("NonTermExpres", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("AbstrExpres", edge.getRightNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(1);
        assertEquals(EdgeType.REALIZATION, edge.getRelationType());
        assertEquals("TermExpres", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("AbstrExpres", edge.getRightNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(2);
        assertEquals(EdgeType.ASSOCIATION_DIRECTED, edge.getRelationType());
        assertEquals("User", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("Cont", edge.getRightNode().getName());
        assertEquals(NodeType.CLASS, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(3);
        assertEquals(EdgeType.ASSOCIATION_DIRECTED, edge.getRelationType());
        assertEquals("User", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("AbstrExpres", edge.getRightNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(4);
        assertEquals(EdgeType.ASSOCIATION_DIRECTED, edge.getRelationType());
        assertEquals("TermExpres", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("AbstrExpres", edge.getRightNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getRightNode().getType());
    }

    /**
     * Test the happy flow of parsing an XMI input file by the {@link ArgoUMLSystemParser}.
     */
    @Test
    public void testParseIterator() {
        final ArgoUMLSystemParser parser = new ArgoUMLSystemParser();
        final String path = getPath(ITERATOR);
        Edge edge;

        final SystemUnderConsideration parseResult = parser.parse(path);

        assertEquals(5, TestHelper.countUniqueNodes(parseResult.getEdges()));
        assertEquals(6, parseResult.getEdges().size());

        //Examine edges in order of appearing in the .xmi
        edge = parseResult.getEdges().get(0);
        assertEquals(EdgeType.DEPENDENCY, edge.getRelationType());
        assertEquals("ConcAgg", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("ConcIter", edge.getRightNode().getName());
        assertEquals(NodeType.CLASS, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(1);
        assertEquals(EdgeType.ASSOCIATION_DIRECTED, edge.getRelationType());
        assertEquals("User", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("Iter", edge.getRightNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(2);
        assertEquals(EdgeType.ASSOCIATION_DIRECTED, edge.getRelationType());
        assertEquals("User", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("Agg", edge.getRightNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(3);
        assertEquals(EdgeType.REALIZATION, edge.getRelationType());
        assertEquals("ConcAgg", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("Agg", edge.getRightNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(4);
        assertEquals(EdgeType.REALIZATION, edge.getRelationType());
        assertEquals("ConcIter", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("Iter", edge.getRightNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(5);
        assertEquals(EdgeType.ASSOCIATION_DIRECTED, edge.getRelationType());
        assertEquals("ConcIter", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("ConcAgg", edge.getRightNode().getName());
        assertEquals(NodeType.CLASS, edge.getRightNode().getType());
    }

    /**
     * Test the happy flow of parsing an XMI input file by the {@link ArgoUMLSystemParser}.
     */
    @Test
    public void testParseMediator() {
        final ArgoUMLSystemParser parser = new ArgoUMLSystemParser();
        final String path = getPath(MEDIATOR);
        Edge edge;

        final SystemUnderConsideration parseResult = parser.parse(path);

        assertEquals(5, TestHelper.countUniqueNodes(parseResult.getEdges()));
        assertEquals(6, parseResult.getEdges().size());

        //Examine edges in order of appearing in the .xmi
        edge = parseResult.getEdges().get(0);
        assertEquals(EdgeType.REALIZATION, edge.getRelationType());
        assertEquals("ConcMed", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("Med", edge.getRightNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(1);
        assertEquals(EdgeType.ASSOCIATION_DIRECTED, edge.getRelationType());
        assertEquals("Coll", edge.getLeftNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getLeftNode().getType());
        assertEquals("Med", edge.getRightNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(2);
        assertEquals(EdgeType.REALIZATION, edge.getRelationType());
        assertEquals("ConcColl_1", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("Coll", edge.getRightNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(3);
        assertEquals(EdgeType.REALIZATION, edge.getRelationType());
        assertEquals("ConcColl_2", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("Coll", edge.getRightNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(4);
        assertEquals(EdgeType.ASSOCIATION_DIRECTED, edge.getRelationType());
        assertEquals("ConcMed", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("ConcColl_1", edge.getRightNode().getName());
        assertEquals(NodeType.CLASS, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(5);
        assertEquals(EdgeType.ASSOCIATION_DIRECTED, edge.getRelationType());
        assertEquals("ConcMed", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("ConcColl_2", edge.getRightNode().getName());
        assertEquals(NodeType.CLASS, edge.getRightNode().getType());
    }

    /**
     * Test the happy flow of parsing an XMI input file by the {@link ArgoUMLSystemParser}.
     */
    @Test
    public void testParseMemento() {
        final ArgoUMLSystemParser parser = new ArgoUMLSystemParser();
        final String path = getPath(MEMENTO);
        Edge edge;

        final SystemUnderConsideration parseResult = parser.parse(path);

        assertEquals(3, TestHelper.countUniqueNodes(parseResult.getEdges()));
        assertEquals(2, parseResult.getEdges().size());

        //Examine edges in order of appearing in the .xmi
        edge = parseResult.getEdges().get(0);
        assertEquals(EdgeType.DEPENDENCY, edge.getRelationType());
        assertEquals("Source", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("Package", edge.getRightNode().getName());
        assertEquals(NodeType.CLASS, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(1);
        assertEquals(EdgeType.ASSOCIATION_DIRECTED, edge.getRelationType());
        assertEquals("Keeper", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("Package", edge.getRightNode().getName());
        assertEquals(NodeType.CLASS, edge.getRightNode().getType());
    }

    /**
     * Test the happy flow of parsing an XMI input file by the {@link ArgoUMLSystemParser}.
     */
    @Test
    public void testParseObserver() {
        final ArgoUMLSystemParser parser = new ArgoUMLSystemParser();
        final String path = getPath(OBSERVER);
        Edge edge;

        final SystemUnderConsideration parseResult = parser.parse(path);

        assertEquals(4, TestHelper.countUniqueNodes(parseResult.getEdges()));
        assertEquals(4, parseResult.getEdges().size());

        //Examine edges in order of appearing in the .xmi
        edge = parseResult.getEdges().get(0);
        assertEquals(EdgeType.ASSOCIATION_DIRECTED, edge.getRelationType());
        assertEquals("Subj", edge.getLeftNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getLeftNode().getType());
        assertEquals("Observ", edge.getRightNode().getName());
        assertEquals(NodeType.CLASS, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(1);
        assertEquals(EdgeType.INHERITANCE, edge.getRelationType());
        assertEquals("ConcrObs", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("Observ", edge.getRightNode().getName());
        assertEquals(NodeType.CLASS, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(2);
        assertEquals(EdgeType.ASSOCIATION_DIRECTED, edge.getRelationType());
        assertEquals("ConcrObs", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("ConcrSubj", edge.getRightNode().getName());
        assertEquals(NodeType.CLASS, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(3);
        assertEquals(EdgeType.REALIZATION, edge.getRelationType());
        assertEquals("ConcrSubj", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("Subj", edge.getRightNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getRightNode().getType());
    }

    /**
     * Test the happy flow of parsing an XMI input file by the {@link ArgoUMLSystemParser}.
     */
    @Test
    public void testParseProxy() {
        final ArgoUMLSystemParser parser = new ArgoUMLSystemParser();
        final String path = getPath(PROXY);
        Edge edge;

        final SystemUnderConsideration parseResult = parser.parse(path);

        assertEquals(4, TestHelper.countUniqueNodes(parseResult.getEdges()));
        assertEquals(4, parseResult.getEdges().size());

        //Examine edges in order of appearing in the .xmi
        edge = parseResult.getEdges().get(0);
        assertEquals(EdgeType.ASSOCIATION_DIRECTED, edge.getRelationType());
        assertEquals("User", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("Subj", edge.getRightNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(1);
        assertEquals(EdgeType.REALIZATION, edge.getRelationType());
        assertEquals("RealSubj", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("Subj", edge.getRightNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(2);
        assertEquals(EdgeType.REALIZATION, edge.getRelationType());
        assertEquals("Proxy", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("Subj", edge.getRightNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(3);
        assertEquals(EdgeType.ASSOCIATION_DIRECTED, edge.getRelationType());
        assertEquals("Proxy", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("RealSubj", edge.getRightNode().getName());
        assertEquals(NodeType.CLASS, edge.getRightNode().getType());
    }

    /**
     * Test the happy flow of parsing an XMI input file by the {@link ArgoUMLSystemParser}.
     */
    @Test
    public void testParseStrategy() {
        final ArgoUMLSystemParser parser = new ArgoUMLSystemParser();
        final String path = getPath(STRATEGY);
        Edge edge;

        final SystemUnderConsideration parseResult = parser.parse(path);

        assertEquals(5, TestHelper.countUniqueNodes(parseResult.getEdges()));
        assertEquals(4, parseResult.getEdges().size());

        //Examine edges in order af appearing in the .xmi
        edge = parseResult.getEdges().get(0);
        assertEquals(EdgeType.ASSOCIATION_DIRECTED, edge.getRelationType());
        assertEquals("Cont", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("Strat", edge.getRightNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(1);
        assertEquals(EdgeType.REALIZATION, edge.getRelationType());
        assertEquals("ConcrStratB", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("Strat", edge.getRightNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(2);
        assertEquals(EdgeType.REALIZATION, edge.getRelationType());
        assertEquals("ConcrStratC", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("Strat", edge.getRightNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(3);
        assertEquals(EdgeType.REALIZATION, edge.getRelationType());
        assertEquals("ConcrStratA", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("Strat", edge.getRightNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getRightNode().getType());
    }

    /**
     * @param adaptertemplatesXml
     * @return
     */
    private String getPath(String resourceName) {
        return this.getClass().getResource(resourceName).getPath();
    }
    
    private ArrayList<Node> findVerticesByName(SystemUnderConsiderationGraph suc, String name) {
    	ArrayList<Node> vertices = new ArrayList<>();
    	for (Node n : suc.vertexSet()) {
    		if (n.getName().equals(name)) {
    			vertices.add(n);
    		}
    	}
    	return vertices;
    }

    private Boolean relationTypeExists(Set<RelationType> relationTypes, String name) {
        return relationTypes.contains(RelationType.valueOf(name));
    }
    
    private Attribute findAttributeByName(Node node, String name) {
    	for (Attribute a : node.getAttributes()) {
    		if (a.getName().equals(name)) {
    			return a;
    		}
    	}
    	return null;
    }
    
    private Method findMethodByName(Node node, String name) {
    	for (Method m : node.getMethods()) {
    		if (m.getName().equals(name)) {
    			return m;
    		}
    	}
    	return null;
    }
 


}
