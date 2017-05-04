package nl.ou.dpd.parsing.template;

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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Tests the {@link TemplatesParser} class.
 *
 * @author Martin de Boer
 */
public class TemplatesParserTest {

    // A test file containing invalid XML.
    private static final String INVALID_XML = "/invalid.xml";
    // A test file containing an invalid edge tag.
    private static final String INVALID_EDGE_TAG_XML = "/invalid-edge-tag.xml";
    // A test file containing an invalid edge tag.
    private static final String INVALID_TEMPLATE_TAG_XML = "/invalid-template-tag.xml";
    // A test file containing the Ba Brahem templates example.
    private static final String BA_BRAHEM_TEST_XML = "/Ba_Brahem.xml";

    /**
     * Exception rule.
     */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Tests the exception handling in case of incorrect XML resulting in a {@link SAXParseException} during parsing
     * a template file by a {@link TemplatesParser}.
     */
    @Test
    public void testSAXParseException() {
        final String path = getPath(INVALID_XML);
        final TemplatesParser templatesParser = new TemplatesParser();

        thrown.expect(DesignPatternDetectorException.class);
        thrown.expectCause(is(SAXParseException.class));
        thrown.expectMessage("The pattern template file " + path + " could not be parsed.");

        templatesParser.parse(path);
    }

    /**
     * Tests the exception handling in case of an unexpected edge tag, resulting in a {@link SAXException} during
     * parsing a template file by a {@link TemplatesParser}.
     */
    @Test
    public void testSAXException1() {
        final String path = getPath(INVALID_EDGE_TAG_XML);
        final TemplatesParser templatesParser = new TemplatesParser();

        thrown.expect(DesignPatternDetectorException.class);
        thrown.expectCause(is(SAXException.class));
        thrown.expectMessage("The pattern template file " + path + " could not be parsed.");

        templatesParser.parse(path);
    }

    /**
     * Tests the exception handling in case of an unexpected template tag, resulting in a {@link SAXException} during
     * parsing a template file by a {@link TemplatesParser}.
     */
    @Test
    public void testSAXException2() {
        final String path = getPath(INVALID_TEMPLATE_TAG_XML);
        final TemplatesParser templatesParser = new TemplatesParser();

        thrown.expect(DesignPatternDetectorException.class);
        thrown.expectCause(is(SAXException.class));
        thrown.expectMessage("The pattern template file " + path + " could not be parsed.");

        templatesParser.parse(path);
    }

    /**
     * Tests the exception handling in case of a {@link IOException} during parsing a template file by a
     * {@link TemplatesParser}.
     */
    @Test
    public void testFileNotFoundException() {
        final TemplatesParser templatesParser = new TemplatesParser();

        thrown.expect(DesignPatternDetectorException.class);
        thrown.expectCause(is(FileNotFoundException.class));
        thrown.expectMessage("The pattern template file missing.xml could not be found.");

        templatesParser.parse("missing.xml");
    }

    /**
     * Test the happy flow of parsing an XMI input file by the {@link TemplatesParser}.
     */
    @Test
    public void testParse() {
        final TemplatesParser templatesParser = new TemplatesParser();
        final String path = getPath(BA_BRAHEM_TEST_XML);

        final List<DesignPattern> parseResult = templatesParser.parse(path);

        assertThat(parseResult.size(), is(1));
        assertThat(parseResult.get(0).getEdges().size(), is(2));
        assertEdge(parseResult.get(0).getEdges().get(0), "P", "Q", EdgeType.ASSOCIATION_DIRECTED);
        assertEdge(parseResult.get(0).getEdges().get(1), "R", "Q", EdgeType.INHERITANCE);
    }

    private String getPath(String resourceName) {
        return this.getClass().getResource(resourceName).getPath();
    }

    private void assertEdge(Edge edge, String leftNodeName, String rightNodeName, EdgeType edgeType) {
        assertThat(edge.getLeftNode().getName(), is(leftNodeName));
        assertThat(edge.getRightNode().getName(), is(rightNodeName));
        assertThat(edge.getRelationType(), is(edgeType));
        assertThat(edge.isSelfRef(), is(false));
        assertThat(edge.isVirtual(), is(false));
        assertThat(edge.isLocked(), is(false));
    }
}
