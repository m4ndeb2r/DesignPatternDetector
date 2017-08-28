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
 * Test the matching process for a Strategy pattern with an abstract class for the Strategy node.
 *
 * @author Martin de Boer
 */
@Category(IntegrationTest.class)
public class StrategyAbstractMatchingTest extends AbstractMatchingTest {

    private static final String PATTERN_NAME = "Strategy";
    private static final String MATCHING_SYSTEM_XMI = "/systems/MyStrategyAbstract.xmi";
    private static final String MISMATCHING_SYSTEM_XMI = "/systems/MyBuilder.xmi";
    private static final String NOT_ANALYSED_SYSTEM_XMI = "/systems/MyClassAdapter.xmi";

    private static final String[] EXPECTED_NOTES = {
            "The structure of the Strategy pattern is rather straightforward. " +
                    "Bear in mind the context and the semantics to decide if the Strategy pattern is genuine.",
            "According the patttern, at least three different concrete strategies must be defined."
    };

    private DesignPattern designPattern;

    @Before
    public void initTests() {
        final PatternsParser patternsParser = ParserFactory.createPatternParser();
        final String patternsXmlFile = StrategyAbstractMatchingTest.class.getResource(TEMPLATES_XML).getFile();
        designPattern = getDesignPatternByName(patternsParser.parse(patternsXmlFile), PATTERN_NAME);
    }

    @Test
    public void testMatchingStrategyAbstract() {
        assertMatchingPattern(MATCHING_SYSTEM_XMI, designPattern, EXPECTED_NOTES);

    }

    @Test
    public void testMismatchingStrategyAbstract() {
        assertMismatchingPattern(MISMATCHING_SYSTEM_XMI, designPattern);
    }

    @Test
    public void testMismatchingStrategyAbstractWithoutAnalysingNodesAndRelations() {
        assertMismatchingPatternWithoutAnalysingNodesAndRelations(NOT_ANALYSED_SYSTEM_XMI, designPattern);
    }

    protected void assertMatchingSolutions(PatternInspector.MatchingResult matchingResult) {
        final List<Solution> solutions = matchingResult.getSolutions();

        assertEquals(1, solutions.size());
        assertMatchingNodes(solutions, "Taxi", "ConcreteStrategyA");
        assertMatchingNodes(solutions, "Vehicle", "Strategy");
        assertMatchingNodes(solutions, "CityBus", "ConcreteStrategyC");
        assertMatchingNodes(solutions, "PersonalCar", "ConcreteStrategyB");
        assertMatchingNodes(solutions, "TransportationToAirport", "Context");
    }

}
