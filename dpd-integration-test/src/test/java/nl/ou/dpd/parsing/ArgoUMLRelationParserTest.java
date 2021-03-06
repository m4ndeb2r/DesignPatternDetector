package nl.ou.dpd.parsing;

import nl.ou.dpd.IntegrationTest;
import nl.ou.dpd.domain.node.Node;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.ExpectedException;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.Is.is;

/**
 * Tests the {@link ArgoUMLRelationParser} class for handling file exceptions. We do not need to test the happy flow here,
 * because we have several integration test for matching patterns, that do succesfully parse the XMI input as well.
 *
 * @author Martin de Boer
 * @author Peter Vansweevelt
 */
@Category(IntegrationTest.class)
public class ArgoUMLRelationParserTest {

    // A test file containing invalid XMI.
    private static final String INVALID_XMI = "/systems/invalid.xmi";

    private ArgoUMLRelationParser argoUMLRelationParser;
    private Map<String, Node> nodes;

    @Before
    public void initArgoUMLRelationParser() {
        argoUMLRelationParser = new ArgoUMLRelationParser(XMLInputFactory.newInstance());
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Test the processing of an invalid XMI file.
     */
    @Test
    public void testXMLStreamException() {
        final String path = getPath(INVALID_XMI);

        thrown.expect(ParseException.class);
        thrown.expectCause(is(XMLStreamException.class));
        thrown.expectMessage("The XMI file could not be parsed.");

        nodes = new HashMap<>();
        argoUMLRelationParser.parse(path, nodes);
    }

    /**
     * Test the exception handling in case of a missing XMI file.
     */
    @Test
    public void testFileNotFoundException() {
        thrown.expect(ParseException.class);
        thrown.expectCause(is(FileNotFoundException.class));
        thrown.expectMessage("The XMI file could not be parsed.");

        nodes = new HashMap<>();
        argoUMLRelationParser.parse("missing.xmi", nodes);
    }

    private String getPath(String resourceName) {
        return this.getClass().getResource(resourceName).getPath();
    }
}
