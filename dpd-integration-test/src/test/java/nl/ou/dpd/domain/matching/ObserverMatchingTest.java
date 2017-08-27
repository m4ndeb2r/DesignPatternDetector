package nl.ou.dpd.domain.matching;

import nl.ou.dpd.IntegrationTest;
import nl.ou.dpd.domain.DesignPattern;
import nl.ou.dpd.parsing.ParserFactory;
import nl.ou.dpd.parsing.PatternsParser;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Test the matching process for a Builder pattern.
 *
 * @author Martin de Boer
 */
@Category(IntegrationTest.class)
public class ObserverMatchingTest extends AbstractMatchingTest {

    private static final String PATTERN_NAME = "Observer";
    private static final String MATCHING_SYSTEM_XMI = "/systems/MyObserver.xmi";
    private static final String MISMATCHING_SYSTEM_XMI = "/systems/MyBuilder.xmi";

    private static final String[] EXPECTED_NOTES = {
            "The Subject must provide a method which calls the Update-operation of all Observers.",
    };

    private DesignPattern designPattern;

    @Before
    public void initTests() {
        final PatternsParser patternsParser = ParserFactory.createPatternParser();
        final String patternsXmlFile = ObserverMatchingTest.class.getResource(TEMPLATES_XML).getFile();
        designPattern = getDesignPatternByName(patternsParser.parse(patternsXmlFile), PATTERN_NAME);
    }

    @Test
    public void testMatchingObserver() {
        assertMatchingPattern(MATCHING_SYSTEM_XMI, designPattern, EXPECTED_NOTES);
    }

    @Test
    public void testMismatchingObserver() {
        assertMismatchingPattern(MISMATCHING_SYSTEM_XMI, designPattern);
    }

    protected void assertMatchingSolutions(PatternInspector.MatchingResult matchingResult) {
        final List<Solution> solutions = matchingResult.getSolutions();

        assertEquals(1, solutions.size());
        assertMatchingNodes(solutions, "MySubject", "Subject");
        assertMatchingNodes(solutions, "MyObserver", "Observer");
        assertMatchingNodes(solutions, "MyConcreteSubject", "ConcreteSubject");
        assertMatchingNodes(solutions, "MyConcreteObserver", "ConcreteObserver");
    }

}
