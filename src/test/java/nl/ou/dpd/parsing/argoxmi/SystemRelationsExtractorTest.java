package nl.ou.dpd.parsing.argoxmi;

import nl.ou.dpd.domain.SystemUnderConsideration;
import nl.ou.dpd.domain.node.Attribute;
import nl.ou.dpd.domain.node.Node;
import nl.ou.dpd.domain.node.NodeType;
import nl.ou.dpd.domain.node.Operation;
import nl.ou.dpd.domain.node.Visibility;
import nl.ou.dpd.domain.relation.Cardinality;
import nl.ou.dpd.domain.relation.Relation;
import nl.ou.dpd.domain.relation.RelationProperty;
import nl.ou.dpd.domain.relation.RelationType;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests the {@link SystemRelationsExtractor} class.
 *
 * @author Martin de Boer
 * @author Peter Vansweevelt
 */
public class SystemRelationsExtractorTest {

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

    /**
     * Exception rule.
     */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Map<String, Node> nodes;
    private Relation relation;
    private Node source;
    private Node target;
    private Attribute attr;

    /**
     * Test the happy flow of parsing an XMI input file by the {@link ArgoUMLRelationParser}.
     */
    @Test
    public void testParse1() {
        final URL url = SystemRelationsExtractorTest.class.getResource(VALID_ADAPTER);

        final ArgoUMLParser parser = new ArgoUMLParser();
        final SystemUnderConsideration suc = parser.parse(url);

        assertEquals("Adapters", suc.getName());

        //number of vertices and edges there is an undirected association, which has two edges (client-target, target-client)
        assertEquals(4, suc.edgeSet().size());
        assertEquals(4, suc.vertexSet().size());
        //get nodes
        Node client = findVerticesByName(suc, "MyClient").get(0);
        Node target = findVerticesByName(suc, "MyTarget").get(0);
        Node adapter = findVerticesByName(suc, "MyAdapter").get(0);
        Node adaptee = findVerticesByName(suc, "MyAdaptee").get(0);
        //node types
        assertTrue(containsType(target, NodeType.INTERFACE));
        assertTrue(containsType(client, NodeType.CONCRETE_CLASS));
        assertTrue(containsType(adapter, NodeType.CONCRETE_CLASS));
        assertTrue(containsType(adaptee, NodeType.CONCRETE_CLASS));
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
        //get edges
        Relation clientTarget = suc.getEdge(client, target);
        Relation adapterTarget = suc.getEdge(adapter, target);
        Relation adapterAdaptee = suc.getEdge(adapter, adaptee);
        Relation targetClient = suc.getEdge(target, client);
        //check relationtypes
        assertTrue(containsType(clientTarget, RelationType.ASSOCIATES_WITH));
        assertTrue(containsType(clientTarget, RelationType.HAS_ATTRIBUTE_OF));
        assertTrue(containsType(targetClient, RelationType.ASSOCIATES_WITH));
        assertTrue(containsType(adapterTarget, RelationType.IMPLEMENTS));
        assertTrue(containsType(adapterAdaptee, RelationType.ASSOCIATES_WITH));
        assertTrue(containsType(adapterAdaptee, RelationType.HAS_ATTRIBUTE_OF));
        //check cardinalities
        assertEquals(0, getCardinalityLeft(adapterAdaptee, RelationType.ASSOCIATES_WITH).getLower());
        assertEquals(-1, getCardinalityLeft(adapterAdaptee, RelationType.ASSOCIATES_WITH).getUpper());
        assertEquals(1, getCardinalityRight(adapterAdaptee, RelationType.ASSOCIATES_WITH).getLower());
        assertEquals(1, getCardinalityRight(adapterAdaptee, RelationType.ASSOCIATES_WITH).getUpper());

        //check attribute types        
/*        assertEquals("adaptee", adapter.getAttributes().get(0).getName());
        assertEquals(adaptee, adapter.getAttributes().get(0).getType());
        assertEquals(Visibility.PRIVATE, adapter.getAttributes().get(0).getVisibility());
        assertEquals("adapter", client.getAttributes().get(0).getName());
        assertEquals(target, client.getAttributes().get(0).getType());
        assertEquals(Visibility.PUBLIC, client.getAttributes().get(0).getVisibility());
*/        //check method existence
/*        Method method = findOperationByName(client, "adapter.request");
        assertNotNull(method);
        method = findOperationByName(adapter, "request");
        assertNotNull(method);
	    method = findOperationByName(adaptee, "specificRequest");
	    assertNotNull(method);
*/
    }

    /**
     * Test the happy flow of parsing a more complicated XMI input file by the {@link ArgoUMLRelationParser}.
     */
    @Test
    public void testParse2() {
        final ArgoUMLNodeParser nodeParser = new ArgoUMLNodeParser();
        final ArgoUMLRelationParser relationParser = new ArgoUMLRelationParser();
        final String path = getPath(VALID_ADAPTERS);

        nodes = nodeParser.parse(path);
        final SystemUnderConsideration suc = relationParser.parse(path, nodes);
        final SystemRelationsExtractor relationExtractor = new SystemRelationsExtractor();
        relationExtractor.execute(suc);
        assertEquals("Adapters", suc.getName());

//34       assertEquals(35, suc.edgeSet().size());
//40        assertEquals(38, suc.vertexSet().size());        

        //Examine edges in order of appearing in the .xmi
        source = findVerticesByName(suc, "SquarePegAdapter").get(0);
        target = findVerticesByName(suc, "SquarePeg").get(0);
        assertTrue(containsType(source, NodeType.CONCRETE_CLASS));
        assertTrue(containsType(target, NodeType.CONCRETE_CLASS));
        assertEquals(3, suc.edgesOf(source).size());
        assertEquals(1, suc.incomingEdgesOf(source).size());
        assertEquals(2, suc.outgoingEdgesOf(source).size());
        assertEquals(2, suc.edgesOf(target).size()); //edge to Double
        assertEquals(1, suc.incomingEdgesOf(target).size());
        assertEquals(1, suc.outgoingEdgesOf(target).size()); //edge to Double
        relation = suc.getEdge(source, target);
        assertEquals(2, relation.getRelationProperties().size());
        assertTrue(relationTypeExists(relation, RelationType.ASSOCIATES_WITH));
        assertTrue(relationTypeExists(relation, RelationType.HAS_ATTRIBUTE_OF));
        assertNotNull(findAttributeByName(source, "sp"));
        assertEquals(target, findAttributeByName(source, "sp").getType());
        assertEquals(Visibility.PRIVATE, findAttributeByName(source, "sp").getVisibility());
        assertNotNull(findAttributeByName(target, "width"));
        assertEquals("Double", findAttributeByName(target, "width").getType().getName());
        assertEquals(Visibility.PUBLIC, findAttributeByName(target, "width").getVisibility());
        assertNotNull(findOperationByName(source, "makeFit"));
        assertNotNull(findOperationByName(target, "getWidth"));
        assertNotNull(findOperationByName(target, "setWidth"));

        source = findVerticesByName(suc, "AdapterDemoSquarePeg").get(0);
        target = findVerticesByName(suc, "SquarePegAdapter").get(0);
        assertTrue(containsType(source, NodeType.CONCRETE_CLASS));
        assertTrue(containsType(target, NodeType.CONCRETE_CLASS));
        assertEquals(2, suc.edgesOf(source).size());
        assertEquals(0, suc.incomingEdgesOf(source).size());
        assertEquals(2, suc.outgoingEdgesOf(source).size());
        assertEquals(3, suc.edgesOf(target).size());
        assertEquals(1, suc.incomingEdgesOf(target).size());
        assertEquals(2, suc.outgoingEdgesOf(source).size());
        relation = suc.getEdge(source, target);
        assertEquals(2, relation.getRelationProperties().size());
        assertTrue(relationTypeExists(relation, RelationType.ASSOCIATES_WITH));
        assertTrue(relationTypeExists(relation, RelationType.HAS_ATTRIBUTE_OF));
        attr = findAttributeByName(source, "spa");
        assertNotNull(attr);
        assertEquals(target, attr.getType());
        assertEquals(Visibility.PUBLIC, attr.getVisibility());

        source = findVerticesByName(suc, "AdapterDemoSquarePeg").get(0);
        target = findVerticesByName(suc, "RoundHole").get(0);
        assertTrue(containsType(source, NodeType.CONCRETE_CLASS));
        assertTrue(containsType(target, NodeType.CONCRETE_CLASS));
        assertEquals(2, suc.edgesOf(source).size());
        assertEquals(0, suc.incomingEdgesOf(source).size());
        assertEquals(2, suc.outgoingEdgesOf(source).size());
        assertEquals(3, suc.edgesOf(target).size());
        assertEquals(2, suc.incomingEdgesOf(target).size());
        assertEquals(1, suc.outgoingEdgesOf(target).size()); //to datatype Integer
        relation = suc.getEdge(source, target);
        assertEquals(2, relation.getRelationProperties().size());
        assertTrue(relationTypeExists(relation, RelationType.ASSOCIATES_WITH));
        assertTrue(relationTypeExists(relation, RelationType.HAS_ATTRIBUTE_OF));
        attr = findAttributeByName(source, "rh");
        assertNotNull(attr);
        assertEquals(target, attr.getType());
        assertEquals(Visibility.PUBLIC, attr.getVisibility());
        assertNotNull(findOperationByName(target, "getRadius"));

        source = findVerticesByName(suc, "SquarePegAdapter").get(0);
        target = findVerticesByName(suc, "RoundHole").get(0);
        assertTrue(containsType(source, NodeType.CONCRETE_CLASS));
        assertTrue(containsType(target, NodeType.CONCRETE_CLASS));
        assertEquals(3, suc.edgesOf(source).size());
        assertEquals(1, suc.incomingEdgesOf(source).size());
        assertEquals(2, suc.outgoingEdgesOf(source).size());
        relation = suc.getEdge(source, target);
        assertEquals(2, relation.getRelationProperties().size());
        assertTrue(relationTypeExists(relation, RelationType.ASSOCIATES_WITH));
        assertTrue(relationTypeExists(relation, RelationType.HAS_METHOD_PARAMETER_OF_TYPE));

        source = findVerticesByName(suc, "SquarePeg").get(0);
        target = findVerticesByName(suc, "Double").get(0);
        assertTrue(containsType(source, NodeType.CONCRETE_CLASS));
        assertTrue(containsType(target, NodeType.CONCRETE_CLASS));
        assertEquals(2, suc.edgesOf(source).size());
        assertEquals(1, suc.incomingEdgesOf(source).size());
        assertEquals(1, suc.outgoingEdgesOf(source).size());
        assertEquals(1, suc.edgesOf(target).size());
        assertEquals(1, suc.incomingEdgesOf(target).size());
        assertEquals(0, suc.outgoingEdgesOf(target).size());
        relation = suc.getEdge(source, target);
        assertEquals(3, relation.getRelationProperties().size());
        assertTrue(relationTypeExists(relation, RelationType.HAS_METHOD_RETURNTYPE));
        assertTrue(relationTypeExists(relation, RelationType.HAS_ATTRIBUTE_OF));
        assertTrue(relationTypeExists(relation, RelationType.HAS_METHOD_PARAMETER_OF_TYPE));
        attr = findAttributeByName(source, "width");
        assertNotNull(attr);
        assertEquals(target, attr.getType());
        assertEquals(Visibility.PUBLIC, attr.getVisibility());

        source = findVerticesByName(suc, "DrawingEditor").get(0);
        target = findVerticesByName(suc, "Shape").get(0); //there is an interface Shape and an abstract Class Shape
        assertTrue(containsType(source, NodeType.CONCRETE_CLASS));
        assertTrue(containsType(target, NodeType.ABSTRACT_CLASS));
//        assertFalse(target.isAbstract()); //interfaces has to be set abstract explicitly in ArgoUML
        assertEquals(1, suc.edgesOf(source).size());
        assertEquals(0, suc.incomingEdgesOf(source).size());
        assertEquals(1, suc.outgoingEdgesOf(source).size());
        assertEquals(5, suc.edgesOf(target).size()); //2 relations of parameters (Point, Manipulator)
        assertEquals(3, suc.incomingEdgesOf(target).size());
        assertEquals(2, suc.outgoingEdgesOf(target).size());
        relation = suc.getEdge(source, target);
        assertNotNull(relation);
        assertEquals(1, relation.getRelationProperties().size());
        assertTrue(relationTypeExists(relation, RelationType.ASSOCIATES_WITH));
        assertNotNull(findOperationByName(target, "BoundingBox"));
        assertNotNull(findOperationByName(target, "CreateManipulator"));

        source = findVerticesByName(suc, "Line").get(0);
        target = findVerticesByName(suc, "Shape").get(1); //there is an interface Shape and an abstract Class Shape
        assertTrue(containsType(source, NodeType.CONCRETE_CLASS));
        assertTrue(containsType(target, NodeType.INTERFACE));
//        assertFalse(target.isAbstract()); //interfaces has to be set abstract explicitly in ArgoUML
        assertEquals(2, suc.edgesOf(source).size()); //hidden association with interface Shape
        assertEquals(0, suc.incomingEdgesOf(source).size());
        assertEquals(2, suc.outgoingEdgesOf(source).size()); //hidden association with interface Shape
        assertEquals(2, suc.edgesOf(target).size());
        assertEquals(2, suc.incomingEdgesOf(target).size());
        assertEquals(0, suc.outgoingEdgesOf(target).size());
        relation = suc.getEdge(source, target);
        assertNotNull(relation);
//1        assertEquals(2, relation.getRelationProperties().size());
        assertTrue(relationTypeExists(relation, RelationType.DEPENDS_ON));
        assertTrue(relationTypeExists(relation, RelationType.OVERRIDES_METHOD_OF));
        assertNotNull(findOperationByName(source, "BoundingBox"));
        assertNotNull(findOperationByName(source, "CreateManipulator"));

        assertTrue(containsType(findVerticesByName(suc, "Shape").get(0), NodeType.ABSTRACT_CLASS));
        relation = suc.getEdge(source, findVerticesByName(suc, "Shape").get(0)); //hidden inheritance with abstract class Shape
        assertTrue(relationTypeExists(relation, RelationType.INHERITS_FROM));

        source = findVerticesByName(suc, "TextShape").get(0);
        target = findVerticesByName(suc, "Shape").get(0); //there is an interface Shape and an abstract Class Shape
        assertTrue(containsType(source, NodeType.CONCRETE_CLASS));
        assertTrue(containsType(target, NodeType.ABSTRACT_CLASS));
//        assertFalse(target.isAbstract());
        assertEquals(5, suc.edgesOf(source).size()); //hidden dependency with interface Shape + return relation to Point
        assertEquals(1, suc.incomingEdgesOf(source).size());
        assertEquals(4, suc.outgoingEdgesOf(source).size()); //hidden dependency with interface Shape + return relation to Point
        relation = suc.getEdge(source, target);
        assertNotNull(relation);
        assertEquals(1, relation.getRelationProperties().size());
        assertTrue(relationTypeExists(relation, RelationType.INHERITS_FROM));
        assertNotNull(findOperationByName(source, "BoundingBox"));
        assertNotNull(findOperationByName(source, "CreateManipulator"));

        assertTrue(containsType(findVerticesByName(suc, "Shape").get(1), NodeType.INTERFACE));
        relation = suc.getEdge(source, findVerticesByName(suc, "Shape").get(1)); //hidden dependency with interface Shape
        assertEquals(2, relation.getRelationProperties().size());
        assertTrue(relationTypeExists(relation, RelationType.DEPENDS_ON));
        assertTrue(relationTypeExists(relation, RelationType.OVERRIDES_METHOD_OF));

        source = findVerticesByName(suc, "TextShape").get(0);
        target = findVerticesByName(suc, "TextView").get(0);
        assertTrue(containsType(source, NodeType.CONCRETE_CLASS));
        assertTrue(containsType(target, NodeType.CONCRETE_CLASS));
        assertEquals(1, suc.edgesOf(target).size());
        assertEquals(1, suc.incomingEdgesOf(target).size());
        assertEquals(0, suc.outgoingEdgesOf(target).size());
        relation = suc.getEdge(source, target);
        assertNotNull(relation);
        assertEquals(2, relation.getRelationProperties().size());
        assertTrue(relationTypeExists(relation, RelationType.ASSOCIATES_WITH));
        assertTrue(relationTypeExists(relation, RelationType.HAS_ATTRIBUTE_OF));
        attr = findAttributeByName(source, "textview");
        assertNotNull(attr);
        assertEquals(target, attr.getType());
        assertEquals(Visibility.PRIVATE, attr.getVisibility());
        assertNotNull(findOperationByName(target, "getExtent"));

        source = findVerticesByName(suc, "textShapeDemo").get(0);
        target = findVerticesByName(suc, "TextShape").get(0);
        assertTrue(containsType(source, NodeType.CONCRETE_CLASS));
        assertTrue(containsType(target, NodeType.CONCRETE_CLASS));
        assertEquals(1, suc.edgesOf(source).size());
        assertEquals(0, suc.incomingEdgesOf(source).size());
        assertEquals(1, suc.outgoingEdgesOf(source).size());
        relation = suc.getEdge(source, target);
        assertNotNull(relation);
        assertEquals(2, relation.getRelationProperties().size());
        assertTrue(relationTypeExists(relation, RelationType.ASSOCIATES_WITH));
        assertTrue(relationTypeExists(relation, RelationType.HAS_ATTRIBUTE_OF));
        attr = findAttributeByName(source, "textShape");
        assertNotNull(attr);
        assertEquals(target, attr.getType());
        assertEquals(Visibility.PUBLIC, attr.getVisibility());

        source = findVerticesByName(suc, "TextShape").get(0);
        target = findVerticesByName(suc, "Point").get(1);
        relation = suc.getEdge(source, target);
        assertNotNull(relation);
        assertEquals(1, relation.getRelationProperties().size());
        assertTrue(relationTypeExists(relation, RelationType.HAS_METHOD_RETURNTYPE));

        source = findVerticesByName(suc, "Shape").get(0);
        target = findVerticesByName(suc, "Point").get(1);
        relation = suc.getEdge(source, target);
        assertNotNull(relation);
        assertEquals(1, relation.getRelationProperties().size());
        assertTrue(relationTypeExists(relation, RelationType.HAS_METHOD_PARAMETER_OF_TYPE));

        source = findVerticesByName(suc, "MediaPlayerAdapterDemo").get(0);
        target = findVerticesByName(suc, "AudioPlayer").get(0);
        assertTrue(containsType(source, NodeType.CONCRETE_CLASS));
        assertTrue(containsType(target, NodeType.CONCRETE_CLASS));
        assertEquals(1, suc.edgesOf(source).size());
        assertEquals(0, suc.incomingEdgesOf(source).size());
        assertEquals(1, suc.outgoingEdgesOf(source).size());
        assertEquals(3, suc.edgesOf(target).size());
        assertEquals(1, suc.incomingEdgesOf(target).size());
        assertEquals(2, suc.outgoingEdgesOf(target).size());
        relation = suc.getEdge(source, target);
        assertNotNull(relation);
        assertEquals(2, relation.getRelationProperties().size());
        assertTrue(relationTypeExists(relation, RelationType.ASSOCIATES_WITH));
        assertTrue(relationTypeExists(relation, RelationType.HAS_ATTRIBUTE_OF));

        source = findVerticesByName(suc, "AudioPlayer").get(0);
        target = findVerticesByName(suc, "MediaPlayer").get(0);
        assertTrue(containsType(source, NodeType.CONCRETE_CLASS));
        assertTrue(containsType(target, NodeType.INTERFACE));
        assertEquals(3, suc.edgesOf(target).size());
        assertEquals(2, suc.incomingEdgesOf(target).size()); //extra Relation to String
        assertEquals(1, suc.outgoingEdgesOf(target).size());
        relation = suc.getEdge(source, target);
        assertNotNull(relation);
        assertEquals(1, relation.getRelationProperties().size());
        assertTrue(relationTypeExists(relation, RelationType.IMPLEMENTS));

        source = findVerticesByName(suc, "MediaAdapter").get(0);
        target = findVerticesByName(suc, "MediaPlayer").get(0);
        assertTrue(containsType(source, NodeType.CONCRETE_CLASS));
        assertTrue(containsType(target, NodeType.INTERFACE));
        assertEquals(4, suc.edgesOf(source).size());
        assertEquals(1, suc.incomingEdgesOf(source).size());
        assertEquals(3, suc.outgoingEdgesOf(source).size());
        relation = suc.getEdge(source, target);
        assertNotNull(relation);
        assertEquals(2, relation.getRelationProperties().size());
        assertTrue(relationTypeExists(relation, RelationType.IMPLEMENTS));
        assertTrue(relationTypeExists(relation, RelationType.OVERRIDES_METHOD_OF));

        source = findVerticesByName(suc, "MediaAdapter").get(0);
        target = findVerticesByName(suc, "AdvancedMediaPlayer").get(0);
        assertTrue(containsType(source, NodeType.CONCRETE_CLASS));
        assertTrue(containsType(target, NodeType.INTERFACE));
        assertEquals(4, suc.edgesOf(target).size());
        assertEquals(3, suc.incomingEdgesOf(target).size());
        assertEquals(1, suc.outgoingEdgesOf(target).size());
        relation = suc.getEdge(source, target);
        assertNotNull(relation);
        assertEquals(2, relation.getRelationProperties().size());
        assertTrue(relationTypeExists(relation, RelationType.ASSOCIATES_WITH));
        assertTrue(relationTypeExists(relation, RelationType.HAS_ATTRIBUTE_OF));

        source = findVerticesByName(suc, "Mp4Player").get(0);
        target = findVerticesByName(suc, "AdvancedMediaPlayer").get(0);
        assertTrue(containsType(source, NodeType.CONCRETE_CLASS));
        assertTrue(containsType(target, NodeType.INTERFACE));
        assertEquals(2, suc.edgesOf(source).size());
        assertEquals(0, suc.incomingEdgesOf(source).size());
        assertEquals(2, suc.outgoingEdgesOf(source).size());
        relation = suc.getEdge(source, target);
        assertNotNull(relation);
        assertEquals(2, relation.getRelationProperties().size());
        assertTrue(relationTypeExists(relation, RelationType.IMPLEMENTS));
        assertTrue(relationTypeExists(relation, RelationType.OVERRIDES_METHOD_OF));

        source = findVerticesByName(suc, "VlcPlayer").get(0);
        target = findVerticesByName(suc, "AdvancedMediaPlayer").get(0);
        assertTrue(containsType(source, NodeType.CONCRETE_CLASS));
        assertTrue(containsType(target, NodeType.INTERFACE));
        assertEquals(2, suc.edgesOf(source).size());
        assertEquals(0, suc.incomingEdgesOf(source).size());
        assertEquals(2, suc.outgoingEdgesOf(source).size());
        relation = suc.getEdge(source, target);
        assertNotNull(relation);
        assertEquals(2, relation.getRelationProperties().size());
        assertTrue(relationTypeExists(relation, RelationType.IMPLEMENTS));
        assertTrue(relationTypeExists(relation, RelationType.OVERRIDES_METHOD_OF));
    }

    /**
     * Test the happy flow of parsing an XMI input file by the {@link ArgoUMLRelationParser}.
     */
    @Test
    public void testAbstractFactory() {
        final ArgoUMLNodeParser nodeParser = new ArgoUMLNodeParser();
        final ArgoUMLRelationParser relationParser = new ArgoUMLRelationParser();
        final String path = getPath(ABSTRACT_FACTORY);

        nodes = nodeParser.parse(path);
        final SystemUnderConsideration suc = relationParser.parse(path, nodes);
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
        assertTrue(containsType(AbstrFact, NodeType.INTERFACE));
        assertTrue(containsType(User, NodeType.CONCRETE_CLASS));
        assertTrue(containsType(ConcFact2, NodeType.CONCRETE_CLASS));
        assertTrue(containsType(ConcFact1, NodeType.CONCRETE_CLASS));
        assertTrue(containsType(AbstrProdA, NodeType.INTERFACE));
        assertTrue(containsType(Prod2A, NodeType.CONCRETE_CLASS));
        assertTrue(containsType(Prod1A, NodeType.CONCRETE_CLASS));
        assertTrue(containsType(AbstrProdB, NodeType.INTERFACE));
        assertTrue(containsType(AbstrProdC, NodeType.INTERFACE));
        assertTrue(containsType(Prod1B, NodeType.CONCRETE_CLASS));
        assertTrue(containsType(Prod2B, NodeType.CONCRETE_CLASS));
        assertTrue(containsType(Prod1C, NodeType.CONCRETE_CLASS));
        assertTrue(containsType(Prod2C, NodeType.CONCRETE_CLASS));
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
        assertTrue(relationTypeExists(userAbstrFact, RelationType.ASSOCIATES_WITH));
        assertTrue(relationTypeExists(userAbstrProdA, RelationType.ASSOCIATES_WITH));
        assertTrue(relationTypeExists(userAbstrProdB, RelationType.ASSOCIATES_WITH));
        assertTrue(relationTypeExists(userAbstrProdC, RelationType.ASSOCIATES_WITH));
        assertTrue(relationTypeExists(concFact1AbstrFact, RelationType.IMPLEMENTS));
        assertTrue(relationTypeExists(concFact2AbstrFact, RelationType.IMPLEMENTS));
        assertTrue(relationTypeExists(concFact1Prod1A, RelationType.DEPENDS_ON));
        assertTrue(relationTypeExists(concFact1Prod1B, RelationType.DEPENDS_ON));
        assertTrue(relationTypeExists(concFact1Prod1C, RelationType.DEPENDS_ON));
        assertTrue(relationTypeExists(concFact2Prod2A, RelationType.DEPENDS_ON));
        assertTrue(relationTypeExists(concFact2Prod2B, RelationType.DEPENDS_ON));
        assertTrue(relationTypeExists(concFact2Prod2C, RelationType.DEPENDS_ON));
        assertTrue(relationTypeExists(prod1AAbstrProdA, RelationType.IMPLEMENTS));
        assertTrue(relationTypeExists(prod1BAbstrProdB, RelationType.IMPLEMENTS));
        assertTrue(relationTypeExists(prod1CAbstrProdC, RelationType.IMPLEMENTS));
        assertTrue(relationTypeExists(prod2AAbstrProdA, RelationType.IMPLEMENTS));
        assertTrue(relationTypeExists(prod2BAbstrProdB, RelationType.IMPLEMENTS));
        assertTrue(relationTypeExists(prod2CAbstrProdC, RelationType.IMPLEMENTS));
    }

    /**
     * @param adaptertemplatesXml
     * @return
     */
    private String getPath(String resourceName) {
        return this.getClass().getResource(resourceName).getPath();
    }

    private ArrayList<Node> findVerticesByName(SystemUnderConsideration suc, String name) {
        ArrayList<Node> vertices = new ArrayList<>();
        for (Node n : suc.vertexSet()) {
            if (n.getName().equals(name)) {
                vertices.add(n);
            }
        }
        return vertices;
    }

    private Boolean relationTypeExists(Relation relation, RelationType relationType) {
        for (RelationProperty rp : relation.getRelationProperties()) {
            if (rp.getRelationType().equals(relationType)) {
                return true;
            }
        }
        return false;
    }

    private Attribute findAttributeByName(Node node, String name) {
        for (Attribute attr : node.getAttributes()) {
            if (attr.getName().equals(name)) {
                return attr;
            }
        }
        return null;
    }

    private Operation findOperationByName(Node node, String name) {
        for (Operation op : node.getOperations()) {
            if (op.getName().equals(name)) {
                return op;
            }
        }
        return null;
    }

    private Cardinality getCardinalityLeft(Relation relation, RelationType relationtype) {
        for (RelationProperty rp : relation.getRelationProperties()) {
            if (relationtype.equals(rp.getRelationType())) {
                return rp.getCardinalityLeft();
            }
        }
        return null;
    }

    private Cardinality getCardinalityRight(Relation relation, RelationType relationtype) {
        for (RelationProperty rp : relation.getRelationProperties()) {
            if (relationtype.equals(rp.getRelationType())) {
                return rp.getCardinalityRight();
            }
        }
        return null;
    }

    private Boolean containsType(Node node, NodeType nodetype) {
        for (NodeType nt : node.getTypes()) {
            if (nodetype.equals(nt)) {
                return true;
            }
        }
        return false;
    }

    private Boolean containsType(Relation relation, RelationType relationtype) {
        for (RelationProperty rp : relation.getRelationProperties()) {
            if (relationtype.equals(rp.getRelationType())) {
                return true;
            }
        }
        return false;
    }
/* 
    private Attribute findAttributeByName(Node node, String name) {
    	for (Attribute a : node.getAttributes()) {
    		if (a.getName().equals(name)) {
    			return a;
    		}
    	}
    	return null;
    }
*/    
/*    private Method findOperationByName(Node node, String name) {
        for (Method m : node.getMethods()) {
    		if (m.getName().equals(name)) {
    			return m;
    		}
    	}
    	return null;
    }
*/


}
