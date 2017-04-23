package nl.ou.dpd.data.template;

import nl.ou.dpd.domain.DesignPattern;
import nl.ou.dpd.domain.edge.Edge;
import nl.ou.dpd.domain.edge.EdgeType;
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

/**
 * Tests the {@link TemplatesParser} class.
 *
 * @author Martin de Boer
 * @author Peter Vansweevelt
 */
public class TemplatesParserWithConditionsTest {

    // A test file containing valid XML.
    private static final String ADAPTERTEMPLATES_XML = "/template_adapters.xml";
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
    @Test
    public void testXMLStreamException() {
        final String path = getPath(INVALID_XML);
        final TemplatesParserWithConditions templatesParser = new TemplatesParserWithConditions();

        thrown.expect(DesignPatternDetectorException.class);
        thrown.expectCause(is(XMLStreamException.class));
        thrown.expectMessage("The pattern template file " + path + " could not be parsed.");

        templatesParser.parse(path);
    }

    /**
     * Tests the exception handling a case of a node id which is not unique, resulting in a {@link XMLStreamException} during
     * parsing a template file by a {@link TemplatesParserWithConditions}.
     */
    @Test
    public void testDoubleNodeException() {
        final String path = getPath(DOUBLE_NODE_XML);
        final TemplatesParserWithConditions templatesParser = new TemplatesParserWithConditions();

        thrown.expect(DesignPatternDetectorException.class);
        thrown.expectCause(is(XMLStreamException.class));
        thrown.expectMessage("The node id Adaptee is not unique in this pattern.");

        templatesParser.parse(path);
    }

    /**
     * Tests the exception handling  a case of an edge id which is not unique, resulting in a {@link XMLStreamException} during
     * parsing a template file by a {@link TemplatesParserWithConditions}.
     */
    @Test
    public void testDoubleEdgeException() {
        final String path = getPath(DOUBLE_EDGE_XML);
        final TemplatesParserWithConditions templatesParser = new TemplatesParserWithConditions();

        thrown.expect(DesignPatternDetectorException.class);
        thrown.expectCause(is(XMLStreamException.class));
        thrown.expectMessage("The edge id ClientTarget is not unique in this pattern.");

        templatesParser.parse(path);
    }

    /**
     * Tests the exception handling  a case of a missing edge, resulting in a {@link XMLStreamException} during
     * parsing a template file by a {@link TemplatesParserWithConditions}.
     */
    @Test
    public void testEdgeNotFoundException() {
        final String path = getPath(MISSING_EDGE_XML);
        final TemplatesParserWithConditions templatesParser = new TemplatesParserWithConditions();

        thrown.expect(DesignPatternDetectorException.class);
        thrown.expectCause(is(XMLStreamException.class));
        thrown.expectMessage("An edge between Adapter and Adaptee could not be found.");

        templatesParser.parse(path);
    }

    /**
     * Tests the exception handling  a case of a missing edge, resulting in a {@link XMLStreamException} during
     * parsing a template file by a {@link TemplatesParserWithConditions}.
     */
    @Test
    public void testTagNotFoundException() {
        final String path = getPath(INVALID_TAG_XML);
        final TemplatesParserWithConditions templatesParser = new TemplatesParserWithConditions();

        thrown.expect(DesignPatternDetectorException.class);
        thrown.expectCause(is(XMLStreamException.class));
        thrown.expectMessage("The pattern template tag invalidtag could not be handled.");

        templatesParser.parse(path);
    }
     /**
     * Tests the exception handling in case of a {@link IOException} during parsing a template file by a
     * {@link TemplatesParserWithConditions}.
     */
    @Test
    public void testFileNotFoundException() {
        final TemplatesParserWithConditions parser = new TemplatesParserWithConditions();

        thrown.expect(DesignPatternDetectorException.class);
        thrown.expectCause(is(FileNotFoundException.class));
        thrown.expectMessage("The pattern template file missing.xml could not be found.");

        parser.parse("missing.xml");
    }

    /**
     * Test the happy flow of parsing an XMI input file by the {@link TemplatesParser}.
     */
    @Test
    public void testParse() {
        final TemplatesParserWithConditions parser = new TemplatesParserWithConditions();
        final String path = getPath(ADAPTERTEMPLATES_XML);

        final List<DesignPattern> parseResult = parser.parse(path);

        DesignPattern dp1 = parseResult.get(0);
        DesignPattern dp2 = parseResult.get(1);
        DesignPattern dp3 = parseResult.get(2);
        
        assertThat(parseResult.size(), is(3));
        assertThat(parseResult.get(0).getEdges().size(), is(3));
    }

	/**
	 * @param adaptertemplatesXml
	 * @return
	 */
	private String getPath(String resourceName) {
        return this.getClass().getResource(resourceName).getPath();
	}
}
