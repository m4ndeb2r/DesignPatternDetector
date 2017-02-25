package nl.ou.dpd.fourtuples.template;

import nl.ou.dpd.exception.DesignPatternDetectorException;
import nl.ou.dpd.fourtuples.FourTupleArray;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.rules.ExpectedException;
import org.xml.sax.SAXParseException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Tests the {@link Templates} class.
 * TODO: The current verion of Templates is not very testable, except when we capture the output to stdout ...
 *
 * @author Martin de Boer
 */
public class TemplatesTest {

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
     * A rule to capture the output written to System.out.
     */
    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    /**
     * Clean the captured output after every test.
     */
    @After
    public void clearLog() {
        systemOutRule.clearLog();
    }

    /**
     * Tests the exception handling in case of a {@link SAXParseException} during parsing input by a {@link Templates}
     * instance.
     */
    @Test
    public void testSAXParseException() {
        thrown.expect(DesignPatternDetectorException.class);
        thrown.expectCause(is(SAXParseException.class));
        thrown.expectMessage("Het bestand " + getPath(INVALID_XML) + " kon niet worden geparsed.");
        final Templates templates = createTemplates(INVALID_XML);
        templates.parse();
    }

    /**
     * Tests the exception handling in case of a {@link IOException} during parsing input by an {@link ArgoXMI}
     * instance.
     */
    @Test
    public void testFileNotFoundException() {
        thrown.expect(DesignPatternDetectorException.class);
        thrown.expectCause(is(FileNotFoundException.class));
        thrown.expectMessage("Het bestand oops.xmi kon niet worden gevonden.");
        final Templates templates = new Templates("oops.xmi");
        templates.parse();
    }

    /**
     * Test the happy flow of parsing an XMI input file by the {@link ArgoXMI}.
     */
    @Test
    public void testParse() {
        final FourTupleArray fta = new FourTupleArray("Ba_Brahem");
        final Templates templates = createTemplates(BA_BRAHEM_TEST_XML);

        final ArrayList<FourTupleArray> parseResult = templates.parse();
        assertThat(parseResult.size(), is(1));

        final String expectedOutput =
                "Design pattern: Ba_Brahem\n" +
                "(              P,               Q, type relatie 10, self ref: nee, matched: nee)\n" +
                "(              R,               Q, type relatie  4, self ref: nee, matched: nee)\n" +
                "\n";

        // Print the fourTuples to the stdout
        parseResult.get(0).show();

        // Assert the output, and compare it to our expectations
        assertThat(systemOutRule.getLog(), is(expectedOutput));
    }

    private Templates createTemplates(String resourceName) {
        return new Templates(getPath(resourceName));
    }

    private String getPath(String resourceName) {
        return this.getClass().getResource(resourceName).getPath();
    }
}
