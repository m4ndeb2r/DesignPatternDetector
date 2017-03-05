package nl.ou.dpd.data.template;

import nl.ou.dpd.domain.DesignPattern;
import nl.ou.dpd.domain.Edge;
import nl.ou.dpd.domain.EdgeType;
import nl.ou.dpd.exception.DesignPatternDetectorException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.xml.sax.SAXParseException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Tests the {@link TemplatesParser} class.
 * TODO: The current verion of TemplatesParser is not very testable, except when we capture the output to stdout ...
 *
 * @author Martin de Boer
 */
public class TemplatesParserTest {

    // A test file containing invalid XMI.
    private static final String INVALID_XML = "/invalid.xml";
    // A test file containing the Ba Brahem templates example.
    private static final String BA_BRAHEM_TEST_XML = "/Ba_Brahem.xml";

    /**
     * Exception rule.
     */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Tests the exception handling in case of a {@link SAXParseException} during parsing input by a
     * {@link TemplatesParser} instance.
     */
    @Test
    public void testSAXParseException() {
        final String path = getPath(INVALID_XML);
        final TemplatesParser templatesParser = new TemplatesParser();

        thrown.expect(DesignPatternDetectorException.class);
        thrown.expectCause(is(SAXParseException.class));
        thrown.expectMessage("Het bestand " + path + " kon niet worden geparsed.");

        templatesParser.parse(path);
    }

    /**
     * Tests the exception handling in case of a {@link IOException} during parsing input by an {@link ArgoXMI}
     * instance.
     */
    @Test
    public void testFileNotFoundException() {
        final TemplatesParser templatesParser = new TemplatesParser();

        thrown.expect(DesignPatternDetectorException.class);
        thrown.expectCause(is(FileNotFoundException.class));
        thrown.expectMessage("Het bestand oops.xmi kon niet worden gevonden.");

        templatesParser.parse("oops.xmi");
    }

    /**
     * Test the happy flow of parsing an XMI input file by the {@link ArgoXMI}.
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

    private void assertEdge(Edge edge, String class1, String class2, EdgeType edgeType) {
        assertThat(edge.getClass1().getName(), is(class1));
        assertThat(edge.getClass2().getName(), is(class2));
        assertThat(edge.getTypeRelation(), is(edgeType));
        assertThat(edge.isSelfRef(), is(false));
        assertThat(edge.isVirtual(), is(false));
        assertThat(edge.isLocked(), is(false));
    }
}
