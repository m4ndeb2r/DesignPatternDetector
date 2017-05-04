package nl.ou.dpd.parsing.argoxmi;

import nl.ou.dpd.domain.SystemUnderConsideration;
import nl.ou.dpd.domain.edge.Edge;
import nl.ou.dpd.domain.edge.EdgeType;
import nl.ou.dpd.domain.node.Attribute;
import nl.ou.dpd.domain.node.Node;
import nl.ou.dpd.domain.node.NodeType;
import nl.ou.dpd.domain.node.Visibility;
import nl.ou.dpd.exception.DesignPatternDetectorException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Tests the {@link ArgoUMLSystemParser} class.
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
   //used to print the number of (different) nodes
    private int numberOfNodes; 

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
        final ArgoUMLSystemParser parser = new ArgoUMLSystemParser();

        thrown.expect(DesignPatternDetectorException.class);
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
        final ArgoUMLSystemParser parser = new ArgoUMLSystemParser();

        thrown.expect(DesignPatternDetectorException.class);
        thrown.expectCause(is(FileNotFoundException.class));
        thrown.expectMessage("The XMI file missing.xml could not be found.");

        parser.parse("missing.xml");
    }

    /**
     * Test the happy flow of parsing an XMI input file by the {@link ArgoUMLSystemParser}.
     */
    @Test
    public void testParse1() {
        final ArgoUMLSystemParser parser = new ArgoUMLSystemParser();
        final String path = getPath(VALID_ADAPTER);
        Edge edge;

        final SystemUnderConsideration parseResult = parser.parse(path);
        numberOfNodes = parser.getNumberOfNodes();
        assertEquals("Adapters", parseResult.getName());
        
        assertEquals(4, parser.getNumberOfNodes());
        assertEquals(3, parseResult.getEdges().size());
 
        edge = parseResult.getEdges().get(0);
        assertEquals(EdgeType.ASSOCIATION_DIRECTED, edge.getRelationType());
        assertEquals("Adapter", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("Adaptee", edge.getRightNode().getName());
        assertEquals(NodeType.CLASS, edge.getRightNode().getType());
        assertEquals(0, edge.getCardinalityLeft().getLower());
        assertEquals(-1, edge.getCardinalityLeft().getUpper());
        assertEquals(1, edge.getCardinalityRight().getLower());
        assertEquals(1, edge.getCardinalityRight().getUpper());

        assertEquals("adaptee", edge.getLeftNode().getAttributes().get(0).getName());
        assertEquals("Adaptee", edge.getLeftNode().getAttributes().get(0).getType().getName());
        assertEquals(Visibility.PRIVATE, edge.getLeftNode().getAttributes().get(0).getVisibility());

        edge = parseResult.getEdges().get(1);
        assertEquals(EdgeType.REALIZATION, edge.getRelationType());
        assertEquals("Adapter", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("Target", edge.getRightNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(2);
        assertEquals(EdgeType.ASSOCIATION, edge.getRelationType());
        assertEquals("Client", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("Target", edge.getRightNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getRightNode().getType());
        
        assertEquals("adapter", edge.getLeftNode().getAttributes().get(0).getName());
        assertEquals("Target", edge.getLeftNode().getAttributes().get(0).getType().getName());
        assertEquals(Visibility.PUBLIC, edge.getLeftNode().getAttributes().get(0).getVisibility());
    }

    /**
     * Test the happy flow of parsing a complicated XMI input file by the {@link ArgoUMLSystemParser}.
     */
    @Test
    public void testParse2() {
        final ArgoUMLSystemParser parser = new ArgoUMLSystemParser();
        final String path = getPath(VALID_ADAPTERS);
        Edge edge;
        
        final SystemUnderConsideration parseResult = parser.parse(path);
        numberOfNodes = parser.getNumberOfNodes();

        assertEquals("Adapters", parseResult.getName());
        assertEquals(40, parser.getNumberOfNodes());
        assertEquals(28, parseResult.getEdges().size());
        
        //Examine edges in order of appearing in the .xmi
        edge = parseResult.getEdges().get(0);
        assertEquals(EdgeType.ASSOCIATION_DIRECTED, edge.getRelationType());
        assertEquals("SquarePegAdapter", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("SquarePeg", edge.getRightNode().getName());
        assertEquals(NodeType.CLASS, edge.getRightNode().getType());
        
        assertEquals("sp", edge.getLeftNode().getAttributes().get(0).getName());
        assertEquals("SquarePeg", edge.getLeftNode().getAttributes().get(0).getType().getName());
        assertEquals(Visibility.PRIVATE, edge.getLeftNode().getAttributes().get(0).getVisibility());
        
        assertEquals("width", edge.getRightNode().getAttributes().get(0).getName());
        assertEquals("Double", edge.getRightNode().getAttributes().get(0).getType().getName());
        assertEquals(Visibility.PUBLIC, edge.getRightNode().getAttributes().get(0).getVisibility());

        edge = parseResult.getEdges().get(1);
        assertEquals(EdgeType.ASSOCIATION_DIRECTED, edge.getRelationType());
        assertEquals("AdapterDemoSquarePeg", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("SquarePegAdapter", edge.getRightNode().getName());
        assertEquals(NodeType.CLASS, edge.getRightNode().getType());
        
        assertEquals("rh", edge.getLeftNode().getAttributes().get(0).getName());
        assertEquals("RoundHole", edge.getLeftNode().getAttributes().get(0).getType().getName());
        assertEquals(Visibility.PUBLIC, edge.getLeftNode().getAttributes().get(0).getVisibility());
        
        assertEquals("sp", edge.getRightNode().getAttributes().get(0).getName());
        assertEquals("SquarePeg", edge.getRightNode().getAttributes().get(0).getType().getName());
        assertEquals(Visibility.PRIVATE, edge.getRightNode().getAttributes().get(0).getVisibility());

        edge = parseResult.getEdges().get(2);
        assertEquals(EdgeType.ASSOCIATION_DIRECTED, edge.getRelationType());
        assertEquals("AdapterDemoSquarePeg", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("RoundHole", edge.getRightNode().getName());
        assertEquals(NodeType.CLASS, edge.getRightNode().getType());
        
        assertEquals("rh", edge.getLeftNode().getAttributes().get(0).getName());
        assertEquals("RoundHole", edge.getLeftNode().getAttributes().get(0).getType().getName());
        assertEquals(Visibility.PUBLIC, edge.getLeftNode().getAttributes().get(0).getVisibility());
        
        assertEquals("radius", edge.getRightNode().getAttributes().get(0).getName());
        assertEquals("Integer", edge.getRightNode().getAttributes().get(0).getType().getName());
        assertEquals(Visibility.PUBLIC, edge.getRightNode().getAttributes().get(0).getVisibility());

        edge = parseResult.getEdges().get(3);
        assertEquals(EdgeType.ASSOCIATION_DIRECTED, edge.getRelationType());
        assertEquals("SquarePegAdapter", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("RoundHole", edge.getRightNode().getName());
        assertEquals(NodeType.CLASS, edge.getRightNode().getType());
        
        assertEquals("sp", edge.getLeftNode().getAttributes().get(0).getName());
        assertEquals("SquarePeg", edge.getLeftNode().getAttributes().get(0).getType().getName());
        assertEquals(Visibility.PRIVATE, edge.getLeftNode().getAttributes().get(0).getVisibility());
        
        assertEquals("radius", edge.getRightNode().getAttributes().get(0).getName());
        assertEquals("Integer", edge.getRightNode().getAttributes().get(0).getType().getName());
        assertEquals(Visibility.PUBLIC, edge.getRightNode().getAttributes().get(0).getVisibility());

        edge = parseResult.getEdges().get(4);
        assertEquals(EdgeType.ASSOCIATION_DIRECTED, edge.getRelationType());
        assertEquals("Client", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("Target", edge.getRightNode().getName());
        assertEquals(NodeType.CLASS, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(5);
        assertEquals(EdgeType.INHERITANCE, edge.getRelationType());
        assertEquals("Adapter", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("Target", edge.getRightNode().getName());
        assertEquals(NodeType.CLASS, edge.getRightNode().getType());
        
        assertEquals("newAttr", edge.getLeftNode().getAttributes().get(0).getName());
        assertEquals("Integer", edge.getLeftNode().getAttributes().get(0).getType().getName());
        assertEquals(Visibility.PUBLIC, edge.getLeftNode().getAttributes().get(0).getVisibility());

        edge = parseResult.getEdges().get(6);
        assertEquals(EdgeType.ASSOCIATION_DIRECTED, edge.getRelationType());
        assertEquals("Adapter", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("Adaptee", edge.getRightNode().getName());
        assertEquals(NodeType.CLASS, edge.getRightNode().getType());

        assertEquals("newAttr", edge.getLeftNode().getAttributes().get(0).getName());
        assertEquals("Integer", edge.getLeftNode().getAttributes().get(0).getType().getName());
        assertEquals(Visibility.PUBLIC, edge.getLeftNode().getAttributes().get(0).getVisibility());

        edge = parseResult.getEdges().get(7);
        assertEquals(EdgeType.REALIZATION, edge.getRelationType());
        assertEquals("Mp4Player", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("AdvancedMediaPlayer", edge.getRightNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(8);
        assertEquals(EdgeType.REALIZATION, edge.getRelationType());
        assertEquals("VlcPlayer", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("AdvancedMediaPlayer", edge.getRightNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(9);
        assertEquals(EdgeType.REALIZATION, edge.getRelationType());
        assertEquals("MediaAdapter", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("MediaPlayer", edge.getRightNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getRightNode().getType());
        
        assertEquals("advancedMusicPlayer", edge.getLeftNode().getAttributes().get(0).getName());
        assertEquals("AdvancedMediaPlayer", edge.getLeftNode().getAttributes().get(0).getType().getName());
        assertEquals(Visibility.PRIVATE, edge.getLeftNode().getAttributes().get(0).getVisibility());

        edge = parseResult.getEdges().get(10);
        assertEquals(EdgeType.ASSOCIATION_DIRECTED, edge.getRelationType());
        assertEquals("MediaAdapter", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("AdvancedMediaPlayer", edge.getRightNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getRightNode().getType());
        
        assertEquals("advancedMusicPlayer", edge.getLeftNode().getAttributes().get(0).getName());
        assertEquals("AdvancedMediaPlayer", edge.getLeftNode().getAttributes().get(0).getType().getName());
        assertEquals(Visibility.PRIVATE, edge.getLeftNode().getAttributes().get(0).getVisibility());

        edge = parseResult.getEdges().get(11);
        assertEquals(EdgeType.REALIZATION, edge.getRelationType());
        assertEquals("AudioPlayer", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("MediaPlayer", edge.getRightNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getRightNode().getType());
        
        assertEquals("mediaAdapter", edge.getLeftNode().getAttributes().get(0).getName());
        assertEquals("MediaAdapter", edge.getLeftNode().getAttributes().get(0).getType().getName());
        assertEquals(Visibility.PRIVATE, edge.getLeftNode().getAttributes().get(0).getVisibility());

        edge = parseResult.getEdges().get(12);
        assertEquals(EdgeType.ASSOCIATION_DIRECTED, edge.getRelationType());
        assertEquals("AudioPlayer", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("MediaAdapter", edge.getRightNode().getName());
        assertEquals(NodeType.CLASS, edge.getRightNode().getType());
        
        assertEquals("mediaAdapter", edge.getLeftNode().getAttributes().get(0).getName());
        assertEquals("MediaAdapter", edge.getLeftNode().getAttributes().get(0).getType().getName());
        assertEquals(Visibility.PRIVATE, edge.getLeftNode().getAttributes().get(0).getVisibility());
        
        assertEquals("advancedMusicPlayer", edge.getRightNode().getAttributes().get(0).getName());
        assertEquals("AdvancedMediaPlayer", edge.getRightNode().getAttributes().get(0).getType().getName());
        assertEquals(Visibility.PRIVATE, edge.getRightNode().getAttributes().get(0).getVisibility());

        edge = parseResult.getEdges().get(13);
        assertEquals(EdgeType.ASSOCIATION_DIRECTED, edge.getRelationType());
        assertEquals("MediaPlayerAdapterDemo", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("AudioPlayer", edge.getRightNode().getName());
        assertEquals(NodeType.CLASS, edge.getRightNode().getType());
        
        assertEquals("audioPlayer", edge.getLeftNode().getAttributes().get(0).getName());
        assertEquals("AudioPlayer", edge.getLeftNode().getAttributes().get(0).getType().getName());
        assertEquals(Visibility.PUBLIC, edge.getLeftNode().getAttributes().get(0).getVisibility());
        
        assertEquals("mediaAdapter", edge.getRightNode().getAttributes().get(0).getName());
        assertEquals("MediaAdapter", edge.getRightNode().getAttributes().get(0).getType().getName());
        assertEquals(Visibility.PRIVATE, edge.getRightNode().getAttributes().get(0).getVisibility());

        edge = parseResult.getEdges().get(14);
        assertEquals(EdgeType.DEPENDENCY, edge.getRelationType());
        assertEquals("AudioPlayer_rev", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("MediaPlayer_rev", edge.getRightNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(15);
        assertEquals(EdgeType.DEPENDENCY, edge.getRelationType());
        assertEquals("MediaAdapter_rev", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("MediaPlayer_rev", edge.getRightNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getRightNode().getType());
        
        assertEquals("ap", edge.getLeftNode().getAttributes().get(0).getName());
        assertEquals("AudioPlayer_rev", edge.getLeftNode().getAttributes().get(0).getType().getName());
        assertEquals(Visibility.PUBLIC, edge.getLeftNode().getAttributes().get(0).getVisibility());
        
        edge = parseResult.getEdges().get(16);
        assertEquals(EdgeType.ASSOCIATION_DIRECTED, edge.getRelationType());
        assertEquals("MediaPlayerDemo_rev", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("MediaAdapter_rev", edge.getRightNode().getName());
        assertEquals(NodeType.CLASS, edge.getRightNode().getType());
        
        assertEquals("ap", edge.getRightNode().getAttributes().get(0).getName());
        assertEquals("AudioPlayer_rev", edge.getRightNode().getAttributes().get(0).getType().getName());
        assertEquals(Visibility.PUBLIC, edge.getRightNode().getAttributes().get(0).getVisibility());

        edge = parseResult.getEdges().get(17);
        assertEquals(EdgeType.ASSOCIATION_DIRECTED, edge.getRelationType());
        assertEquals("MediaAdapter_rev", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("AdvancedMediaplayer_rev", edge.getRightNode().getName());
        assertEquals(NodeType.CLASS, edge.getRightNode().getType());
        
        assertEquals("ap", edge.getLeftNode().getAttributes().get(0).getName());
        assertEquals("AudioPlayer_rev", edge.getLeftNode().getAttributes().get(0).getType().getName());
        assertEquals(Visibility.PUBLIC, edge.getLeftNode().getAttributes().get(0).getVisibility());

        edge = parseResult.getEdges().get(18);
        assertEquals(EdgeType.DEPENDENCY, edge.getRelationType());
        assertEquals("Line", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("Shape", edge.getRightNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(19);
        assertEquals(EdgeType.DEPENDENCY, edge.getRelationType());
        assertEquals("TextShape", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("Shape", edge.getRightNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getRightNode().getType());
        
        assertEquals("textview", edge.getLeftNode().getAttributes().get(0).getName());
        assertEquals("TextView", edge.getLeftNode().getAttributes().get(0).getType().getName());
        assertEquals(Visibility.PRIVATE, edge.getLeftNode().getAttributes().get(0).getVisibility());

        edge = parseResult.getEdges().get(20);
        assertEquals(EdgeType.ASSOCIATION_DIRECTED, edge.getRelationType());
        assertEquals("TextShape", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("TextView", edge.getRightNode().getName());
        assertEquals(NodeType.CLASS, edge.getRightNode().getType());
        
        assertEquals("textview", edge.getLeftNode().getAttributes().get(0).getName());
        assertEquals("TextView", edge.getLeftNode().getAttributes().get(0).getType().getName());
        assertEquals(Visibility.PRIVATE, edge.getLeftNode().getAttributes().get(0).getVisibility());

        edge = parseResult.getEdges().get(21);
        assertEquals(EdgeType.ASSOCIATION_DIRECTED, edge.getRelationType());
        assertEquals("DrawingEditor", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("Shape", edge.getRightNode().getName());
        assertEquals(NodeType.CLASS, edge.getRightNode().getType());
  
        edge = parseResult.getEdges().get(22);
        assertEquals(EdgeType.INHERITANCE, edge.getRelationType());
        assertEquals("Line", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("Shape", edge.getRightNode().getName());
        assertEquals(NodeType.CLASS, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(23);
        assertEquals(EdgeType.INHERITANCE, edge.getRelationType());
        assertEquals("TextShape", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("Shape", edge.getRightNode().getName());
        assertEquals(NodeType.CLASS, edge.getRightNode().getType());
        
        assertEquals("textview", edge.getLeftNode().getAttributes().get(0).getName());
        assertEquals("TextView", edge.getLeftNode().getAttributes().get(0).getType().getName());
        assertEquals(Visibility.PRIVATE, edge.getLeftNode().getAttributes().get(0).getVisibility());

        edge = parseResult.getEdges().get(24);
        assertEquals(EdgeType.ASSOCIATION_DIRECTED, edge.getRelationType());
        assertEquals("textShapeDemo", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("TextShape", edge.getRightNode().getName());
        assertEquals(NodeType.CLASS, edge.getRightNode().getType());
        
        assertEquals("textShape", edge.getLeftNode().getAttributes().get(0).getName());
        assertEquals("TextShape", edge.getLeftNode().getAttributes().get(0).getType().getName());
        assertEquals(Visibility.PUBLIC, edge.getLeftNode().getAttributes().get(0).getVisibility());
        
        assertEquals("textview", edge.getRightNode().getAttributes().get(0).getName());
        assertEquals("TextView", edge.getRightNode().getAttributes().get(0).getType().getName());
        assertEquals(Visibility.PRIVATE, edge.getRightNode().getAttributes().get(0).getVisibility());
        
        edge = parseResult.getEdges().get(25);
        assertEquals(EdgeType.ASSOCIATION_DIRECTED, edge.getRelationType());
        assertEquals("Adapter", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("Adaptee", edge.getRightNode().getName());
        assertEquals(NodeType.CLASS, edge.getRightNode().getType());
        
        assertEquals("adaptee", edge.getLeftNode().getAttributes().get(0).getName());
        assertEquals("Adaptee", edge.getLeftNode().getAttributes().get(0).getType().getName());
        assertEquals(Visibility.PUBLIC, edge.getLeftNode().getAttributes().get(0).getVisibility());
        
        edge = parseResult.getEdges().get(26);
        assertEquals(EdgeType.REALIZATION, edge.getRelationType());
        assertEquals("Adapter", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("Target", edge.getRightNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getRightNode().getType());
        
        assertEquals("adaptee", edge.getLeftNode().getAttributes().get(0).getName());
        assertEquals("Adaptee", edge.getLeftNode().getAttributes().get(0).getType().getName());
        assertEquals(Visibility.PUBLIC, edge.getLeftNode().getAttributes().get(0).getVisibility());
        
        edge = parseResult.getEdges().get(27);
        assertEquals(EdgeType.ASSOCIATION_DIRECTED, edge.getRelationType());
        assertEquals("Client", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("Target", edge.getRightNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getRightNode().getType());
        
        assertEquals("adapter", edge.getLeftNode().getAttributes().get(0).getName());
        assertEquals("Adapter", edge.getLeftNode().getAttributes().get(0).getType().getName());
        assertEquals(Visibility.PUBLIC, edge.getLeftNode().getAttributes().get(0).getVisibility());
        
       
   }

    /**
     * Test the happy flow of parsing an XMI input file by the {@link ArgoUMLSystemParser}.
     */
    @Test
    public void testAbstractFactory() {
        final ArgoUMLSystemParser parser = new ArgoUMLSystemParser();
        final String path = getPath(ABSTRACT_FACTORY);
        Edge edge;
        
        final SystemUnderConsideration parseResult = parser.parse(path);
        
        numberOfNodes = parser.getNumberOfNodes();
        
        assertEquals(13, parser.getNumberOfNodes());
        assertEquals(18, parseResult.getEdges().size());
        
        //Examine edges in order of appearing in the .xmi
        edge = parseResult.getEdges().get(0);
        assertEquals(EdgeType.DEPENDENCY, edge.getRelationType());
        assertEquals("ConcFact1", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("Prod1A", edge.getRightNode().getName());
        assertEquals(NodeType.CLASS, edge.getRightNode().getType());
        
        edge = parseResult.getEdges().get(1);
        assertEquals(EdgeType.DEPENDENCY, edge.getRelationType());
        assertEquals("ConcFact1", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("Prod1B", edge.getRightNode().getName());
        assertEquals(NodeType.CLASS, edge.getRightNode().getType());
        
        edge = parseResult.getEdges().get(2);
        assertEquals(EdgeType.DEPENDENCY, edge.getRelationType());
        assertEquals("ConcFact1", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("Prod1C", edge.getRightNode().getName());
        assertEquals(NodeType.CLASS, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(3);
        assertEquals(EdgeType.DEPENDENCY, edge.getRelationType());
        assertEquals("ConcFact2", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("Prod2A", edge.getRightNode().getName());
        assertEquals(NodeType.CLASS, edge.getRightNode().getType());
        
        edge = parseResult.getEdges().get(4);
        assertEquals(EdgeType.DEPENDENCY, edge.getRelationType());
        assertEquals("ConcFact2", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("Prod2B", edge.getRightNode().getName());
        assertEquals(NodeType.CLASS, edge.getRightNode().getType());
        
        edge = parseResult.getEdges().get(5);
        assertEquals(EdgeType.DEPENDENCY, edge.getRelationType());
        assertEquals("ConcFact2", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("Prod2C", edge.getRightNode().getName());
        assertEquals(NodeType.CLASS, edge.getRightNode().getType());
        
        edge = parseResult.getEdges().get(6);
        assertEquals(EdgeType.ASSOCIATION_DIRECTED, edge.getRelationType());
        assertEquals("User", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("AbstrFact", edge.getRightNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getRightNode().getType());
        
        edge = parseResult.getEdges().get(7);
        assertEquals(EdgeType.ASSOCIATION_DIRECTED, edge.getRelationType());
        assertEquals("User", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("AbstrProdA", edge.getRightNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getRightNode().getType());
        
        edge = parseResult.getEdges().get(8);
        assertEquals(EdgeType.ASSOCIATION_DIRECTED, edge.getRelationType());
        assertEquals("User", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("AbstrProdB", edge.getRightNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getRightNode().getType());
        
        edge = parseResult.getEdges().get(9);
        assertEquals(EdgeType.REALIZATION, edge.getRelationType());
        assertEquals("Prod1A", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("AbstrProdA", edge.getRightNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(10);
        assertEquals(EdgeType.REALIZATION, edge.getRelationType());
        assertEquals("Prod2A", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("AbstrProdA", edge.getRightNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getRightNode().getType());
 
        edge = parseResult.getEdges().get(11);
        assertEquals(EdgeType.REALIZATION, edge.getRelationType());
        assertEquals("Prod1B", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("AbstrProdB", edge.getRightNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(12);
        assertEquals(EdgeType.REALIZATION, edge.getRelationType());
        assertEquals("Prod2B", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("AbstrProdB", edge.getRightNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(13);
        assertEquals(EdgeType.REALIZATION, edge.getRelationType());
        assertEquals("ConcFact1", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("AbstrFact", edge.getRightNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(14);
        assertEquals(EdgeType.REALIZATION, edge.getRelationType());
        assertEquals("ConcFact2", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("AbstrFact", edge.getRightNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(15);
        assertEquals(EdgeType.REALIZATION, edge.getRelationType());
        assertEquals("Prod1C", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("AbstrProdC", edge.getRightNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(16);
        assertEquals(EdgeType.REALIZATION, edge.getRelationType());
        assertEquals("Prod2C", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("AbstrProdC", edge.getRightNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(17);
        assertEquals(EdgeType.ASSOCIATION_DIRECTED, edge.getRelationType());
        assertEquals("User", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("AbstrProdC", edge.getRightNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getRightNode().getType());
}
    
    /**
     * Test the happy flow of parsing an XMI input file by the {@link ArgoUMLSystemParser}.
     */
    @Test
    public void testParseBaBrahem() {
        final ArgoUMLSystemParser parser = new ArgoUMLSystemParser();
        final String path = getPath(BA_BRAHEM);
        Edge edge;

        final SystemUnderConsideration parseResult = parser.parse(path);
        numberOfNodes = parser.getNumberOfNodes();
        
        assertEquals(5, parser.getNumberOfNodes());
        assertEquals(6, parseResult.getEdges().size());
        
        //Examine edges in order of appearing in the .xmi
        edge = parseResult.getEdges().get(0);
        assertEquals(EdgeType.DEPENDENCY, edge.getRelationType());
        assertEquals("D", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("E", edge.getRightNode().getName());
        assertEquals(NodeType.CLASS, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(1);
        assertEquals(EdgeType.INHERITANCE, edge.getRelationType());
        assertEquals("D", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("B", edge.getRightNode().getName());
        assertEquals(NodeType.CLASS, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(2);
        assertEquals(EdgeType.INHERITANCE, edge.getRelationType());
        assertEquals("E", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("C", edge.getRightNode().getName());
        assertEquals(NodeType.CLASS, edge.getRightNode().getType());
 
        edge = parseResult.getEdges().get(3);
        assertEquals(EdgeType.ASSOCIATION_DIRECTED, edge.getRelationType());
        assertEquals("C", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("B", edge.getRightNode().getName());
        assertEquals(NodeType.CLASS, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(4);
        assertEquals(EdgeType.ASSOCIATION_DIRECTED, edge.getRelationType());
        assertEquals("A", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("B", edge.getRightNode().getName());
        assertEquals(NodeType.CLASS, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(5);
        assertEquals(EdgeType.ASSOCIATION_DIRECTED, edge.getRelationType());
        assertEquals("A", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("C", edge.getRightNode().getName());
        assertEquals(NodeType.CLASS, edge.getRightNode().getType());
}
    
    /**
     * Test the happy flow of parsing an XMI input file by the {@link ArgoUMLSystemParser}.
     */
    @Test
    public void testParseBridge() {
        final ArgoUMLSystemParser parser = new ArgoUMLSystemParser();
        final String path = getPath(BRIDGE);
        Edge edge;

        final SystemUnderConsideration parseResult = parser.parse(path);
        numberOfNodes = parser.getNumberOfNodes();

        assertEquals(8, parser.getNumberOfNodes());
        assertEquals(8, parseResult.getEdges().size());
        
        //Examine edges in order of appearing in the .xmi
        edge = parseResult.getEdges().get(0);
        assertEquals(EdgeType.ASSOCIATION_DIRECTED, edge.getRelationType());
        assertEquals("ConcAbstr2", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("ConcImpl1", edge.getRightNode().getName());
        assertEquals(NodeType.CLASS, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(1);
        assertEquals(EdgeType.REALIZATION, edge.getRelationType());
        assertEquals("ConcAbstr2", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("Ab", edge.getRightNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(2);
        assertEquals(EdgeType.REALIZATION, edge.getRelationType());
        assertEquals("ConcAbstr1", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("Ab", edge.getRightNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getRightNode().getType());
 
        edge = parseResult.getEdges().get(3);
        assertEquals(EdgeType.REALIZATION, edge.getRelationType());
        assertEquals("ConcImpl1", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("Impl", edge.getRightNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(4);
        assertEquals(EdgeType.REALIZATION, edge.getRelationType());
        assertEquals("ConcImpl2", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("Impl", edge.getRightNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(5);
        assertEquals(EdgeType.REALIZATION, edge.getRelationType());
        assertEquals("ConcImpl3", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("Impl", edge.getRightNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(6);
        assertEquals(EdgeType.ASSOCIATION_DIRECTED, edge.getRelationType());
        assertEquals("Ab", edge.getLeftNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getLeftNode().getType());
        assertEquals("Impl", edge.getRightNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(7);
        assertEquals(EdgeType.ASSOCIATION_DIRECTED, edge.getRelationType());
        assertEquals("Client", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("Ab", edge.getRightNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getRightNode().getType());

   }
    
    /**
     * Test the happy flow of parsing an XMI input file by the {@link ArgoUMLSystemParser}.
     */
    @Test
    public void testParseBuilder() {
        final ArgoUMLSystemParser parser = new ArgoUMLSystemParser();
        final String path = getPath(BUILDER);
        Edge edge;

        final SystemUnderConsideration parseResult = parser.parse(path);
        numberOfNodes = parser.getNumberOfNodes();
        
        assertEquals(4, parser.getNumberOfNodes());
        assertEquals(3, parseResult.getEdges().size());
        
        //Examine edges in order of appearing in the .xmi
        edge = parseResult.getEdges().get(0);
        assertEquals(EdgeType.DEPENDENCY, edge.getRelationType());
        assertEquals("ConcrMaker", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("Prod", edge.getRightNode().getName());
        assertEquals(NodeType.CLASS, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(1);
        assertEquals(EdgeType.ASSOCIATION_DIRECTED, edge.getRelationType());
        assertEquals("Direc", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("Maker", edge.getRightNode().getName());
        assertEquals(NodeType.CLASS, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(2);
        assertEquals(EdgeType.INHERITANCE, edge.getRelationType());
        assertEquals("ConcrMaker", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("Maker", edge.getRightNode().getName());
        assertEquals(NodeType.CLASS, edge.getRightNode().getType());
    }
    
    /**
     * Test the happy flow of parsing an XMI input file by the {@link ArgoUMLSystemParser}.
     */
    @Test
    public void testParseChainOfResponsibility() {
        final ArgoUMLSystemParser parser = new ArgoUMLSystemParser();
        final String path = getPath(CHAIN_OF_RESPONSIBILITY);
        Edge edge;

        final SystemUnderConsideration parseResult = parser.parse(path);
        numberOfNodes = parser.getNumberOfNodes();;
        
        assertEquals(4, parser.getNumberOfNodes());
        assertEquals(4, parseResult.getEdges().size());
        
        //Examine edges in order of appearing in the .xmi
        edge = parseResult.getEdges().get(0);
        assertEquals(EdgeType.ASSOCIATION_DIRECTED, edge.getRelationType());
        assertEquals("User", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("Processor", edge.getRightNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(1);
        assertEquals(EdgeType.REALIZATION, edge.getRelationType());
        assertEquals("ConcProcess1", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("Processor", edge.getRightNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(2);
        assertEquals(EdgeType.REALIZATION, edge.getRelationType());
        assertEquals("ConcProcess2", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("Processor", edge.getRightNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getRightNode().getType());

         edge = parseResult.getEdges().get(3);
        assertEquals(EdgeType.ASSOCIATION_DIRECTED, edge.getRelationType());
        assertEquals("Processor", edge.getLeftNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getLeftNode().getType());
        assertEquals("Processor", edge.getRightNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getRightNode().getType());
}
    
    /**
     * Test the happy flow of parsing an XMI input file by the {@link ArgoUMLSystemParser}.
     */
    @Test
    public void testParseCommand() {
        final ArgoUMLSystemParser parser = new ArgoUMLSystemParser();
        final String path = getPath(COMMAND);
        Edge edge;

        final SystemUnderConsideration parseResult = parser.parse(path);
        numberOfNodes = parser.getNumberOfNodes();

        assertEquals(5, parser.getNumberOfNodes());
        assertEquals(5, parseResult.getEdges().size());
        
        //Examine edges in order of appearing in the .xmi
        edge = parseResult.getEdges().get(0);
        assertEquals(EdgeType.DEPENDENCY, edge.getRelationType());
        assertEquals("User", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("ConcTask", edge.getRightNode().getName());
        assertEquals(NodeType.CLASS, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(1);
        assertEquals(EdgeType.ASSOCIATION_DIRECTED, edge.getRelationType());
        assertEquals("Caller", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("Task", edge.getRightNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(2);
        assertEquals(EdgeType.REALIZATION, edge.getRelationType());
        assertEquals("ConcTask", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("Task", edge.getRightNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(3);
        assertEquals(EdgeType.ASSOCIATION_DIRECTED, edge.getRelationType());
        assertEquals("ConcTask", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("Rec", edge.getRightNode().getName());
        assertEquals(NodeType.CLASS, edge.getRightNode().getType());
	
		edge = parseResult.getEdges().get(4);
		assertEquals(EdgeType.ASSOCIATION_DIRECTED, edge.getRelationType());
		assertEquals("User", edge.getLeftNode().getName());
		assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
		assertEquals("Rec", edge.getRightNode().getName());
		assertEquals(NodeType.CLASS, edge.getRightNode().getType());	
	}   

	/**
     * Test the happy flow of parsing an XMI input file by the {@link ArgoUMLSystemParser}.
     */
    @Test
    public void testParseComposite() {
        final ArgoUMLSystemParser parser = new ArgoUMLSystemParser();
        final String path = getPath(COMPOSITE);
        Edge edge;
        
        final SystemUnderConsideration parseResult = parser.parse(path);
        numberOfNodes = parser.getNumberOfNodes();

        assertEquals(4, parser.getNumberOfNodes());
        assertEquals(4, parseResult.getEdges().size());
        
        //Examine edges in order of appearing in the .xmi
        edge = parseResult.getEdges().get(0);
        assertEquals(EdgeType.ASSOCIATION_DIRECTED, edge.getRelationType());
        assertEquals("User", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("Part", edge.getRightNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(1);
        assertEquals(EdgeType.REALIZATION, edge.getRelationType());
        assertEquals("End", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("Part", edge.getRightNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(2);
        assertEquals(EdgeType.REALIZATION, edge.getRelationType());
        assertEquals("Union", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("Part", edge.getRightNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(3);
        assertEquals(EdgeType.ASSOCIATION_DIRECTED, edge.getRelationType());
        assertEquals("Union", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("Part", edge.getRightNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getRightNode().getType());	
    }
    
    /**
     * Test the happy flow of parsing an XMI input file by the {@link ArgoUMLSystemParser}.
     */
    @Test
    public void testParseDecorator() {
        final ArgoUMLSystemParser parser = new ArgoUMLSystemParser();
        final String path = getPath(DECORATOR);
        Edge edge;

        final SystemUnderConsideration parseResult = parser.parse(path);
        numberOfNodes = parser.getNumberOfNodes();

        assertEquals(5, parser.getNumberOfNodes());
        assertEquals(5, parseResult.getEdges().size());
        
        //Examine edges in order of appearing in the .xmi
        edge = parseResult.getEdges().get(0);
        assertEquals(EdgeType.REALIZATION, edge.getRelationType());
        assertEquals("ConcretePart", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("Part", edge.getRightNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(1);
        assertEquals(EdgeType.REALIZATION, edge.getRelationType());
        assertEquals("Dec", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("Part", edge.getRightNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(2);
        assertEquals(EdgeType.INHERITANCE, edge.getRelationType());
        assertEquals("ConcDecA", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("Dec", edge.getRightNode().getName());
        assertEquals(NodeType.CLASS, edge.getRightNode().getType());

        edge = parseResult.getEdges().get(3);
        assertEquals(EdgeType.INHERITANCE, edge.getRelationType());
        assertEquals("ConcDecB", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("Dec", edge.getRightNode().getName());
        assertEquals(NodeType.CLASS, edge.getRightNode().getType());	
 
        edge = parseResult.getEdges().get(4);
        assertEquals(EdgeType.ASSOCIATION_DIRECTED, edge.getRelationType());
        assertEquals("Dec", edge.getLeftNode().getName());
        assertEquals(NodeType.CLASS, edge.getLeftNode().getType());
        assertEquals("Part", edge.getRightNode().getName());
        assertEquals(NodeType.INTERFACE, edge.getRightNode().getType());	
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
        numberOfNodes = parser.getNumberOfNodes();
 
        assertEquals(4, parser.getNumberOfNodes());
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
        numberOfNodes = parser.getNumberOfNodes();
 
        assertEquals(5, parser.getNumberOfNodes());
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
        numberOfNodes = parser.getNumberOfNodes();
 
        assertEquals(5, parser.getNumberOfNodes());
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
        numberOfNodes = parser.getNumberOfNodes();
 
        assertEquals(5, parser.getNumberOfNodes());
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
        numberOfNodes = parser.getNumberOfNodes();
 
        assertEquals(5, parser.getNumberOfNodes());
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
        numberOfNodes = parser.getNumberOfNodes();

        assertEquals(3, parser.getNumberOfNodes());
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
        numberOfNodes = parser.getNumberOfNodes();

        assertEquals(4, parser.getNumberOfNodes());
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
        numberOfNodes = parser.getNumberOfNodes();
 
        assertEquals(4, parser.getNumberOfNodes());
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
        numberOfNodes = parser.getNumberOfNodes();
        
        assertEquals(5, parser.getNumberOfNodes());
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
	
	/**
	 *  Print the edges and nodes of the SystemUnderConsideration
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
