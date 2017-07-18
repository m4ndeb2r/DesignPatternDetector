package nl.ou.dpd.parsing.argoxmi;

import nl.ou.dpd.IntegrationTest;
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
import nl.ou.dpd.parsing.ParseException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.ExpectedException;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests the {@link ArgoUMLRelationParser} class.
 *
 * @author Martin de Boer
 * @author Peter Vansweevelt
 */
@Category(IntegrationTest.class)
public class ArgoUMLRelationParserTest {

    // A test file containing valid XML.
    private static final String VALID_ADAPTER = "/argoUML/adapters_structures_association.xmi";
    //another pattern
    private static final String ABSTRACT_FACTORY = "/argoUML/AbstractFactory.xmi";

    // A test file containing invalid XML.
    private static final String INVALID_XML = "/patterns/invalid.xml";

    private ArgoUMLNodeParser argoUMLNodeParser;
    private ArgoUMLRelationParser argoUMLRelationParser;

    @Before
    public void initArgoUMLRelationParser() {
        argoUMLNodeParser = new ArgoUMLNodeParser(XMLInputFactory.newInstance());
        argoUMLRelationParser = new ArgoUMLRelationParser(XMLInputFactory.newInstance());
    }

    /**
     * Exception rule.
     */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Map<String, Node> nodes;

    @Test
    public void testXMLStreamException() {
        final String path = getPath(INVALID_XML);

        thrown.expect(ParseException.class);
        thrown.expectCause(is(XMLStreamException.class));
        thrown.expectMessage("The XMI file '" + path + "' could not be parsed.");

        nodes = argoUMLNodeParser.parse(path);
        argoUMLRelationParser.parse(path, nodes);
    }

    /**
     * Tests the exception handling in case of a {@link IOException} during parsing a patterns file.
     */
    @Test
    public void testFileNotFoundException() {
        thrown.expect(ParseException.class);
        thrown.expectCause(is(FileNotFoundException.class));
        thrown.expectMessage("The XMI file 'missing.xml' could not be parsed.");

        nodes = argoUMLNodeParser.parse("missing.xml");
        argoUMLRelationParser.parse("missing.xml", nodes);
    }

    /**
     * Test the happy flow of parsing an XMI input file by the {@link ArgoUMLRelationParser}.
     */
    @Test
    public void testParse1() {
        final String path = getPath(VALID_ADAPTER);

        nodes = argoUMLNodeParser.parse(path);
        final SystemUnderConsideration suc = argoUMLRelationParser.parse(path, nodes);
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
//        assertTrue(containsType(clientTarget, RelationType.HAS_ATTRIBUTE_OF));
        assertTrue(containsType(targetClient, RelationType.ASSOCIATES_WITH));
        assertTrue(containsType(adapterTarget, RelationType.IMPLEMENTS));
        assertTrue(containsType(adapterAdaptee, RelationType.ASSOCIATES_WITH));
//        assertTrue(containsType(adapterAdaptee, RelationType.HAS_ATTRIBUTE_OF));
        //check cardinalities
        assertEquals(0, getCardinalityLeft(adapterAdaptee, RelationType.ASSOCIATES_WITH).getLower());
        assertEquals(-1, getCardinalityLeft(adapterAdaptee, RelationType.ASSOCIATES_WITH).getUpper());
        assertEquals(1, getCardinalityRight(adapterAdaptee, RelationType.ASSOCIATES_WITH).getLower());
        assertEquals(1, getCardinalityRight(adapterAdaptee, RelationType.ASSOCIATES_WITH).getUpper());

        //check attribute types        
        assertNotNull(findAttributeByName(adapter, "adaptee"));
        assertNotNull(findAttributeByName(adapter, "adaptee"));
        assertEquals(adaptee, findAttributeByName(adapter, "adaptee").getType());
        assertEquals(Visibility.PRIVATE, findAttributeByName(adapter, "adaptee").getVisibility());
        assertNotNull(findAttributeByName(client, "adapter"));
        assertEquals(target, findAttributeByName(client, "adapter").getType());
        assertEquals(Visibility.PUBLIC, findAttributeByName(client, "adapter").getVisibility());
        //check method existence
        Operation method = findOperationByName(client, "adapter.request");
        assertNotNull(method);
        method = findOperationByName(adapter, "request");
        assertNotNull(method);
	    method = findOperationByName(adaptee, "specificRequest");
	    assertNotNull(method);
    }

    /**
     * Test the happy flow of parsing an XMI input file by the {@link ArgoUMLRelationParser}.
     */
    @Test
    public void testAbstractFactory() {
        final String path = getPath(ABSTRACT_FACTORY);

        nodes = argoUMLNodeParser.parse(path);
        final SystemUnderConsideration suc = argoUMLRelationParser.parse(path, nodes);
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
