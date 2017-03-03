package nl.ou.dpd.data.argoxmi;

import nl.ou.dpd.domain.SystemUnderConsideration;
import nl.ou.dpd.exception.DesignPatternDetectorException;
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
 * Tests the {@link ArgoXMIParser} class.
 * TODO: The ArgoXMIParser class is hardly testable at the moment. It needs to be refactored to be testable.
 *
 * @author Martin de Boer
 */
public class ArgoXMIParserTest {

    // A test file containing invalid XMI.
    private static final String INVALID_XMI = "/invalid.xmi";
    // A test file containing the Ba Brahem "system under consideration" example.
    private static final String BA_BRAHEM_TEST_XMI = "/Ba_Brahem.xmi";

    // Test files containing XMI exports of strategy UMLs.
    private static final String STRATEGY_XMI = "/Strategy.xmi";
    private static final String ABSTRACT_FACTORY_XMI = "/AbstractFactory.xmi";

    /**
     * A rule to capture the output written to System.out.
     */
    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();
    /**
     * Exception rule.
     */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Clean the captured output after every test.
     */
    @After
    public void clearLog() {
        systemOutRule.clearLog();
    }

    /**
     * Tests the exception handling in case of a {@link SAXParseException} during parsing input by an {@link ArgoXMIParser}
     * instance.
     */
    @Test
    public void testSAXParseException() {
        final String path = getPath(INVALID_XMI);
        final ArgoXMIParser argoXMIParser = new ArgoXMIParser();

        thrown.expect(DesignPatternDetectorException.class);
        thrown.expectCause(is(SAXParseException.class));
        thrown.expectMessage("Het bestand " + path + " kon niet worden geparsed.");

        argoXMIParser.parse(path);
    }

    /**
     * Tests the exception handling in case of a {@link IOException} during parsing input by an {@link ArgoXMIParser}
     * instance.
     */
    @Test
    public void testFileNotFoundException() {
        final ArgoXMIParser argoXMIParser = new ArgoXMIParser();

        thrown.expect(DesignPatternDetectorException.class);
        thrown.expectCause(is(FileNotFoundException.class));
        thrown.expectMessage("Het bestand oops.xmi kon niet worden gevonden.");

        argoXMIParser.parse("oops.xmi");
    }

    /**
     * Test the happy flow of parsing a Ba_Brahem XMI input file by the {@link ArgoXMIParser}.
     */
    @Test
    public void testBa_BrahemXmiParse() {
        final SystemUnderConsideration system = new SystemUnderConsideration();
        final ArgoXMIParser argoXMIParser = new ArgoXMIParser();
        final String path = getPath(BA_BRAHEM_TEST_XMI);

        final String expectedOutput =
                "(              D,               E, type relatie  5, self ref: nee, locked: nee)\n" +
                        "(              E,               C, type relatie  4, self ref: nee, locked: nee)\n" +
                        "(              D,               B, type relatie  4, self ref: nee, locked: nee)\n" +
                        "(              C,               B, type relatie 10, self ref: nee, locked: nee)\n" +
                        "(              A,               B, type relatie 10, self ref: nee, locked: nee)\n" +
                        "(              A,               C, type relatie 10, self ref: nee, locked: nee)\n" +
                        "\n";

        // Print the fourTuples to the stdout
        argoXMIParser.parse(path).show();

        // Assert the output, and compare it to our expectations
        assertThat(systemOutRule.getLog(), is(expectedOutput));
    }

    /**
     * Test the happy flow of parsing an AbstractFactory XMI input file by the {@link ArgoXMIParser}.
     */
    @Test
    public void testAbstractFactoryXmiParse() {
        final SystemUnderConsideration system = new SystemUnderConsideration();
        final ArgoXMIParser argoXMIParser = new ArgoXMIParser();
        final String path = getPath(ABSTRACT_FACTORY_XMI);

        final String expectedOutput =
                "(      ConcFact1,          Prod1A, type relatie  5, self ref: nee, locked: nee)\n" +
                "(      ConcFact1,          Prob1B, type relatie  5, self ref: nee, locked: nee)\n" +
                "(      ConcFact1,          Prod1C, type relatie  5, self ref: nee, locked: nee)\n" +
                "(      ConcFact2,          Prod2A, type relatie  5, self ref: nee, locked: nee)\n" +
                "(      ConcFact2,          Prob2B, type relatie  5, self ref: nee, locked: nee)\n" +
                "(      ConcFact2,          Prod2C, type relatie  5, self ref: nee, locked: nee)\n" +
                "(         Prod1A,      AbstrProdA, type relatie  4, self ref: nee, locked: nee)\n" +
                "(         Prod2A,      AbstrProdA, type relatie  4, self ref: nee, locked: nee)\n" +
                "(         Prob1B,      AbstrProdB, type relatie  4, self ref: nee, locked: nee)\n" +
                "(         Prob2B,      AbstrProdB, type relatie  4, self ref: nee, locked: nee)\n" +
                "(      ConcFact1,       AbstrFact, type relatie  4, self ref: nee, locked: nee)\n" +
                "(      ConcFact2,       AbstrFact, type relatie  4, self ref: nee, locked: nee)\n" +
                "(         Prod1C,      AbstrProdC, type relatie  4, self ref: nee, locked: nee)\n" +
                "(         Prod2C,      AbstrProdC, type relatie  4, self ref: nee, locked: nee)\n" +
                "(           User,       AbstrFact, type relatie 10, self ref: nee, locked: nee)\n" +
                "(           User,      AbstrProdA, type relatie 10, self ref: nee, locked: nee)\n" +
                "(           User,      AbstrProdB, type relatie 10, self ref: nee, locked: nee)\n" +
                "(           User,      AbstrProdC, type relatie 10, self ref: nee, locked: nee)\n" +
                "\n";

        // Print the fourTuples to the stdout
        argoXMIParser.parse(path).show();

        // Assert the output, and compare it to our expectations
        assertThat(systemOutRule.getLog(), is(expectedOutput));
    }

    /**
     * Test the happy flow of parsing a Strategy XMI input file by the {@link ArgoXMIParser}.
     */
    @Test
    public void testStrategyXmiParse() {
        final SystemUnderConsideration system = new SystemUnderConsideration();
        final ArgoXMIParser argoXMIParser = new ArgoXMIParser();
        final String path = getPath(STRATEGY_XMI);

        final String expectedOutput =
                "(    ConcrStratB,           Strat, type relatie  4, self ref: nee, locked: nee)\n" +
                "(    ConcrStratC,           Strat, type relatie  4, self ref: nee, locked: nee)\n" +
                "(    ConcrStratA,           Strat, type relatie  4, self ref: nee, locked: nee)\n" +
                "(          Strat,            Cont, type relatie  2, self ref: nee, locked: nee)\n" +
                "\n";

        // Print the fourTuples to the stdout
        argoXMIParser.parse(path).show();

        // Assert the output, and compare it to our expectations
        assertThat(systemOutRule.getLog(), is(expectedOutput));
    }

    private String getPath(String resourceName) {
        return this.getClass().getResource(resourceName).getPath();
    }
}
