package nl.ou.dpd.argoxmi;

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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Tests the {@link ArgoXMI} class.
 * TODO: The ArgoXMI class is hardly testable at the moment. It needs to be refactored to be testable.
 *
 * @author Martin de Boer
 */
public class ArgoXMITest {

    // A test file containing invalid XMI.
    private static final String INVALID_XMI = "/invalid.xmi";
    // A test file containing the Ba Brahem "system under consideration" example.
    private static final String BA_BRAHEM_TEST_XMI = "/Ba_Brahem.xmi";

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
     * Tests the exception handling in case of a {@link SAXParseException} during parsing input by an {@link ArgoXMI}
     * instance.
     */
    @Test
    public void testSAXParseException() {
        thrown.expect(DesignPatternDetectorException.class);
        thrown.expectCause(is(SAXParseException.class));
        thrown.expectMessage("Het bestand " + getPath(INVALID_XMI) + " kon niet worden geparsed.");
        final ArgoXMI argoXMI = createArgoXMI(INVALID_XMI);
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
        final ArgoXMI argoXMI = new ArgoXMI("oops.xmi");
    }

    /**
     * Test the happy flow of parsing an XMI input file by the {@link ArgoXMI}.
     */
    @Test
    public void testParse() {
        final FourTupleArray fta = new FourTupleArray("Ba_Brahem");
        final ArgoXMI argoXMI = createArgoXMI(BA_BRAHEM_TEST_XMI);

        final String expectedOutput =
                "(              D,               E, type relatie  5, self ref: nee, matched: nee)\n" +
                "(              E,               C, type relatie  4, self ref: nee, matched: nee)\n" +
                "(              D,               B, type relatie  4, self ref: nee, matched: nee)\n" +
                "(              C,               B, type relatie 10, self ref: nee, matched: nee)\n" +
                "(              A,               B, type relatie 10, self ref: nee, matched: nee)\n" +
                "(              A,               C, type relatie 10, self ref: nee, matched: nee)\n" +
                "\n";

        // Print the fourTuples to the stdout
        argoXMI.getFourtuples().show();

        // Assert the output, and compare it to our expectations
        assertThat(systemOutRule.getLog(), is(expectedOutput));
    }

    private ArgoXMI createArgoXMI(String resourceName) {
        return new ArgoXMI(getPath(resourceName));
    }

    private String getPath(String resourceName) {
        return this.getClass().getResource(resourceName).getPath();
    }
}
