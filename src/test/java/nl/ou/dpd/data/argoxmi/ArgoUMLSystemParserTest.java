package nl.ou.dpd.data.argoxmi;

import nl.ou.dpd.domain.DesignPattern;
import nl.ou.dpd.domain.SystemUnderConsideration;
import nl.ou.dpd.domain.edge.Edge;
import nl.ou.dpd.domain.edge.EdgeType;
import nl.ou.dpd.domain.node.Attribute;
import nl.ou.dpd.domain.node.Node;
import nl.ou.dpd.exception.DesignPatternDetectorException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Tests the {@link TemplatesParser} class.
 *
 * @author Martin de Boer
 * @author Peter Vansweevelt
 */
public class ArgoUMLSystemParserTest {

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
    // A test file containing two nodes with the same name.
    private static final String DOUBLE_NODE_XML = "/template_adapters_test_doubleNode.xml";
    // A test file containing two nodes with the same name.
    private static final String DOUBLE_EDGE_XML = "/template_adapters_test_doubleEdge.xml";
    // A test file containing an invalid edge tag.
    private static final String MISSING_EDGE_XML = "/template_adapters_test_missingEdge.xml";
    // A test file containing an invalid edge tag.
    private static final String INVALID_TAG_XML = "/template_adapters_test_invalidTag.xml";
    
    private int numberOfNodes; //used to print the number of (different) nodes
	
    // A test file containing invalid XML.

    /**
     * Exception rule.
     */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Tests the exception handling in case of document which could not be parsed, resulting in a {@link XMLStreamException} during
     * parsing a template file by a {@link TemplatesParserWithConditions}.
     */
/*    @Test
    public void testXMLStreamException() {
        final String path = getPath(INVALID_XML);
        final TemplatesParserWithConditions templatesParser = new TemplatesParserWithConditions();

        thrown.expect(DesignPatternDetectorException.class);
        thrown.expectCause(is(XMLStreamException.class));
        thrown.expectMessage("The pattern template file " + path + " could not be parsed.");

        templatesParser.parse(path);
    }
*/
    /**
     * Tests the exception handling a case of a node id which is not unique, resulting in a {@link XMLStreamException} during
     * parsing a template file by a {@link TemplatesParserWithConditions}.
     */
/*    @Test
    public void testDoubleNodeException() {
        final String path = getPath(DOUBLE_NODE_XML);
        final TemplatesParserWithConditions templatesParser = new TemplatesParserWithConditions();

        thrown.expect(DesignPatternDetectorException.class);
        thrown.expectCause(is(XMLStreamException.class));
        thrown.expectMessage("The node id Adaptee is not unique in this pattern.");

        templatesParser.parse(path);
    }
*/
    /**
     * Tests the exception handling  a case of an edge id which is not unique, resulting in a {@link XMLStreamException} during
     * parsing a template file by a {@link TemplatesParserWithConditions}.
     */
/*    @Test
    public void testDoubleEdgeException() {
        final String path = getPath(DOUBLE_EDGE_XML);
        final TemplatesParserWithConditions templatesParser = new TemplatesParserWithConditions();

        thrown.expect(DesignPatternDetectorException.class);
        thrown.expectCause(is(XMLStreamException.class));
        thrown.expectMessage("The edge id ClientTarget is not unique in this pattern.");

        templatesParser.parse(path);
    }
*/
    /**
     * Tests the exception handling  a case of a missing edge, resulting in a {@link XMLStreamException} during
     * parsing a template file by a {@link TemplatesParserWithConditions}.
     */
/*    @Test
    public void testEdgeNotFoundException() {
        final String path = getPath(MISSING_EDGE_XML);
        final TemplatesParserWithConditions templatesParser = new TemplatesParserWithConditions();

        thrown.expect(DesignPatternDetectorException.class);
        thrown.expectCause(is(XMLStreamException.class));
        thrown.expectMessage("An edge between Adapter and Adaptee could not be found.");

        templatesParser.parse(path);
    }
*/
    /**
     * Tests the exception handling  a case of a missing edge, resulting in a {@link XMLStreamException} during
     * parsing a template file by a {@link TemplatesParserWithConditions}.
     */
/*    @Test
    public void testTagNotFoundException() {
        final String path = getPath(INVALID_TAG_XML);
        final TemplatesParserWithConditions templatesParser = new TemplatesParserWithConditions();

        thrown.expect(DesignPatternDetectorException.class);
        thrown.expectCause(is(XMLStreamException.class));
        thrown.expectMessage("The pattern template tag invalidtag could not be handled.");

        templatesParser.parse(path);
    }
*/
    /**
     * Tests the exception handling in case of a {@link IOException} during parsing a template file by a
     * {@link TemplatesParserWithConditions}.
     */
/*    @Test
    public void testFileNotFoundException() {
        final TemplatesParserWithConditions parser = new TemplatesParserWithConditions();

        thrown.expect(DesignPatternDetectorException.class);
        thrown.expectCause(is(FileNotFoundException.class));
        thrown.expectMessage("The pattern template file missing.xml could not be found.");

        parser.parse("missing.xml");
    }
*/
    /**
     * Test the happy flow of parsing an XMI input file by the {@link ArgoUMLSystemParser}.
     */
    @Test
    public void testParse1() {
        final ArgoUMLSystemParser parser = new ArgoUMLSystemParser();
        final String path = getPath(VALID_ADAPTER);

        final SystemUnderConsideration parseResult = parser.parse(path);
        numberOfNodes = parser.getNumberOfNodes();
        System.out.print(printSystemStructure(parseResult));
        assertEquals("Adapters", parseResult.getName());
    }

    /**
     * Test the happy flow of parsing a complicated XMI input file by the {@link ArgoUMLSystemParser}.
     */
    @Test
    public void testParse2() {
        final ArgoUMLSystemParser parser = new ArgoUMLSystemParser();
        final String path = getPath(VALID_ADAPTERS);

        final SystemUnderConsideration parseResult = parser.parse(path);
        numberOfNodes = parser.getNumberOfNodes();
        System.out.print(printSystemStructure(parseResult));
        assertEquals("Adapters", parseResult.getName());
    }

    /**
     * Test the happy flow of parsing an XMI input file by the {@link ArgoUMLSystemParser}.
     */
    @Test
    public void testAbstractFactory() {
        final ArgoUMLSystemParser parser = new ArgoUMLSystemParser();
        final String path = getPath(ABSTRACT_FACTORY);

        final SystemUnderConsideration parseResult = parser.parse(path);
        numberOfNodes = parser.getNumberOfNodes();
        System.out.print(printSystemStructure(parseResult));
    }
    
    /**
     * Test the happy flow of parsing an XMI input file by the {@link ArgoUMLSystemParser}.
     */
    @Test
    public void testParseBaBrahem() {
        final ArgoUMLSystemParser parser = new ArgoUMLSystemParser();
        final String path = getPath(BA_BRAHEM);

        final SystemUnderConsideration parseResult = parser.parse(path);
        numberOfNodes = parser.getNumberOfNodes();
        System.out.print(printSystemStructure(parseResult));
    }
    
    /**
     * Test the happy flow of parsing an XMI input file by the {@link ArgoUMLSystemParser}.
     */
    @Test
    public void testParseBridge() {
        final ArgoUMLSystemParser parser = new ArgoUMLSystemParser();
        final String path = getPath(BRIDGE);

        final SystemUnderConsideration parseResult = parser.parse(path);
        numberOfNodes = parser.getNumberOfNodes();
        System.out.print(printSystemStructure(parseResult));
    }
    
    /**
     * Test the happy flow of parsing an XMI input file by the {@link ArgoUMLSystemParser}.
     */
    @Test
    public void testParseBuilder() {
        final ArgoUMLSystemParser parser = new ArgoUMLSystemParser();
        final String path = getPath(BUILDER);

        final SystemUnderConsideration parseResult = parser.parse(path);
        numberOfNodes = parser.getNumberOfNodes();
        System.out.print(printSystemStructure(parseResult));
    }
    
    /**
     * Test the happy flow of parsing an XMI input file by the {@link ArgoUMLSystemParser}.
     */
    @Test
    public void testParseChainOfResponsibility() {
        final ArgoUMLSystemParser parser = new ArgoUMLSystemParser();
        final String path = getPath(CHAIN_OF_RESPONSIBILITY);

        final SystemUnderConsideration parseResult = parser.parse(path);
        numberOfNodes = parser.getNumberOfNodes();
        System.out.print(printSystemStructure(parseResult));
    }
    
    /**
     * Test the happy flow of parsing an XMI input file by the {@link ArgoUMLSystemParser}.
     */
    @Test
    public void testParseCommand() {
        final ArgoUMLSystemParser parser = new ArgoUMLSystemParser();
        final String path = getPath(COMMAND);

        final SystemUnderConsideration parseResult = parser.parse(path);
        numberOfNodes = parser.getNumberOfNodes();
        System.out.print(printSystemStructure(parseResult));
    }
    
    /**
     * Test the happy flow of parsing an XMI input file by the {@link ArgoUMLSystemParser}.
     */
    @Test
    public void testParseComposite() {
        final ArgoUMLSystemParser parser = new ArgoUMLSystemParser();
        final String path = getPath(COMPOSITE);

        final SystemUnderConsideration parseResult = parser.parse(path);
        numberOfNodes = parser.getNumberOfNodes();
        System.out.print(printSystemStructure(parseResult));
    }
    
    /**
     * Test the happy flow of parsing an XMI input file by the {@link ArgoUMLSystemParser}.
     */
    @Test
    public void testParseDecorator() {
        final ArgoUMLSystemParser parser = new ArgoUMLSystemParser();
        final String path = getPath(DECORATOR);

        final SystemUnderConsideration parseResult = parser.parse(path);
        numberOfNodes = parser.getNumberOfNodes();
        System.out.print(printSystemStructure(parseResult));
    }
    
    /**
     * Test the happy flow of parsing an XMI input file by the {@link ArgoUMLSystemParser}.
     */
    @Test
    public void testParseFactoryMethod() {
        final ArgoUMLSystemParser parser = new ArgoUMLSystemParser();
        final String path = getPath(FACTORY_METHOD);

        final SystemUnderConsideration parseResult = parser.parse(path);
        numberOfNodes = parser.getNumberOfNodes();
        System.out.print(printSystemStructure(parseResult));
    }
    
    /**
     * Test the happy flow of parsing an XMI input file by the {@link ArgoUMLSystemParser}.
     */
    @Test
    public void testParseFlyweight() {
        final ArgoUMLSystemParser parser = new ArgoUMLSystemParser();
        final String path = getPath(FLYWEIGHT);

        final SystemUnderConsideration parseResult = parser.parse(path);
        numberOfNodes = parser.getNumberOfNodes();
        System.out.print(printSystemStructure(parseResult));
    }
    
    /**
     * Test the happy flow of parsing an XMI input file by the {@link ArgoUMLSystemParser}.
     */
    @Test
    public void testParseInterpreter() {
        final ArgoUMLSystemParser parser = new ArgoUMLSystemParser();
        final String path = getPath(INTERPRETER);

        final SystemUnderConsideration parseResult = parser.parse(path);
        numberOfNodes = parser.getNumberOfNodes();
        System.out.print(printSystemStructure(parseResult));
    }
    
    /**
     * Test the happy flow of parsing an XMI input file by the {@link ArgoUMLSystemParser}.
     */
    @Test
    public void testParseIterator() {
        final ArgoUMLSystemParser parser = new ArgoUMLSystemParser();
        final String path = getPath(ITERATOR);

        final SystemUnderConsideration parseResult = parser.parse(path);
        numberOfNodes = parser.getNumberOfNodes();
        System.out.print(printSystemStructure(parseResult));
    }
    
    /**
     * Test the happy flow of parsing an XMI input file by the {@link ArgoUMLSystemParser}.
     */
    @Test
    public void testParseMediator() {
        final ArgoUMLSystemParser parser = new ArgoUMLSystemParser();
        final String path = getPath(MEDIATOR);

        final SystemUnderConsideration parseResult = parser.parse(path);
        numberOfNodes = parser.getNumberOfNodes();
        System.out.print(printSystemStructure(parseResult));
    }
    
    /**
     * Test the happy flow of parsing an XMI input file by the {@link ArgoUMLSystemParser}.
     */
    @Test
    public void testParseMemento() {
        final ArgoUMLSystemParser parser = new ArgoUMLSystemParser();
        final String path = getPath(MEMENTO);

        final SystemUnderConsideration parseResult = parser.parse(path);
        numberOfNodes = parser.getNumberOfNodes();
        System.out.print(printSystemStructure(parseResult));
    }
    
    /**
     * Test the happy flow of parsing an XMI input file by the {@link ArgoUMLSystemParser}.
     */
    @Test
    public void testParseObserver() {
        final ArgoUMLSystemParser parser = new ArgoUMLSystemParser();
        final String path = getPath(OBSERVER);

        final SystemUnderConsideration parseResult = parser.parse(path);
        numberOfNodes = parser.getNumberOfNodes();
        System.out.print(printSystemStructure(parseResult));
    }
    
    /**
     * Test the happy flow of parsing an XMI input file by the {@link ArgoUMLSystemParser}.
     */
    @Test
    public void testParseProxy() {
        final ArgoUMLSystemParser parser = new ArgoUMLSystemParser();
        final String path = getPath(PROXY);

        final SystemUnderConsideration parseResult = parser.parse(path);
        numberOfNodes = parser.getNumberOfNodes();
        System.out.print(printSystemStructure(parseResult));
    }
    
    /**
     * Test the happy flow of parsing an XMI input file by the {@link ArgoUMLSystemParser}.
     */
    @Test
    public void testParseStrategy() {
        final ArgoUMLSystemParser parser = new ArgoUMLSystemParser();
        final String path = getPath(STRATEGY);

        final SystemUnderConsideration parseResult = parser.parse(path);
        numberOfNodes = parser.getNumberOfNodes();
        System.out.print(printSystemStructure(parseResult));
    }
    
    /**
	 * @param adaptertemplatesXml
	 * @return
	 */
	private String getPath(String resourceName) {
        return this.getClass().getResource(resourceName).getPath();
	}
	
	/**Print the edges and nodes of the SystemUnderConsideration
	 *  
	 */
	
	private String printSystemStructure(SystemUnderConsideration system) {
		String s = "System name: " + system.getName() + "; system id: " + system.getId() + "\n" + "--------------------------------------\n";
		s += "Number of edges = " +  system.getEdges().size() + "\n";
		s += "Number of nodes = " +  numberOfNodes + "\n\n";
		
		for (int i = 0; i < system.getEdges().size(); i++) {
			Edge edge = system.getEdges().get(i);
			Node nodeLeft = edge.getLeftNode();
			Node nodeRight = edge.getRightNode();
			
			s += "Edge " + i + ": id = " + edge.getId() + "; name = " + edge.getName() + "\n";
			s += nodeLeft.getName() + " - " + nodeRight.getName() + "\n";
			s += "RelationType: " + edge.getRelationType().toString() + "\n";
			s += "\tLeft node: name = " + nodeLeft.getName() + "; id = " + nodeLeft.getId() + "\n";
			s += "\tClass = " + nodeLeft.getType().toString() + "\n";
			if (edge.getCardinalityLeft() != null) {
				s += "\tCardinality = " + edge.getCardinalityLeft().toString() + "\n";
			} else {
				s += "\tCardinality not set\n";
			}
			s += "\tVisibility = " + nodeLeft.getVisibility() + "\n";
			s += "\tisAbstract = " + nodeLeft.isAbstract().toString() + "\n";
			s += "\tisActive = " + nodeLeft.isActive().toString() + "\n";
			s += "\tisRoot = " + nodeLeft.isRoot().toString() + "\n";
			s += "\tisLeaf = " + nodeLeft.isLeaf().toString() + "\n";
			s += "\tAttributes:\n";		
			for (Attribute a : nodeLeft.getAttributes()) {
				s += "\tAttribute name = " + a.getName() + "; id = " + a.getId() + "\n";			
				if (a.getType() != null) {
					s += "\t\tType = " + a.getType().getName() + "\n";			
					} else {
						s += "\tType not set\n";
					}			
				if (a.getVisibility() != null) {
					s += "\t\tVisibility = " + a.getVisibility().toString() + "\n";
				} else {
					s += "\tVisibility not set\n";
				}			
			}
			s += "\n";
			s += "\tRight node: name = " + nodeRight.getName() + "; id = " + nodeRight.getId() + "\n";
			s += "\tClass = " + nodeRight.getType().toString() + "\n";			
			if (edge.getCardinalityRight() != null) {
				s += "\tCardinality = " + edge.getCardinalityRight().toString() + "\n";
			} else {
				s += "\tCardinality not set\n";
			}			
			s += "\tVisibility = " + nodeRight.getVisibility() + "\n";
			s += "\tisAbstract = " + nodeRight.isAbstract().toString() + "\n";
			s += "\tisActive = " + nodeRight.isActive().toString() + "\n";
			s += "\tisRoot = " + nodeRight.isRoot().toString() + "\n";
			s += "\tisLeaf = " + nodeRight.isLeaf().toString() + "\n";
			s += "\tAttributes:\n";		
			for (Attribute a : nodeRight.getAttributes()) {
				s += "\tAttribute name = " + a.getName() + "; id = " + a.getId() + "\n";			
				if (a.getType() != null) {
					s += "\t\tType = " + a.getType().getName() + "\n";			
					} else {
						s += "\tType not set\n";
					}			
				if (a.getVisibility() != null) {
					s += "\t\tVisibility = " + a.getVisibility().toString() + "\n";
				} else {
					s += "\tVisibility not set\n";
				}			
			}
			s += "\n";
		}		
		return s;
	}
}
