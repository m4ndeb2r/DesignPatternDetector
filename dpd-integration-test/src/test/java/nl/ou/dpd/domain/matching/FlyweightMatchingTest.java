package nl.ou.dpd.domain.matching;

import nl.ou.dpd.IntegrationTest;
import nl.ou.dpd.domain.DesignPattern;
import nl.ou.dpd.parsing.ParserFactory;
import nl.ou.dpd.parsing.PatternsParser;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Test the matching process for a Flyweight pattern.
 *
 * @author Martin de Boer
 */
@Category(IntegrationTest.class)
public class FlyweightMatchingTest extends AbstractMatchingTest {

    private static final String PATTERN_NAME = "Flyweight";
    private static final String MATCHING_SYSTEM_XMI = "/systems/MyFlyweight.xmi";
    private static final String MISMATCHING_SYSTEM_XMI = "/systems/MyBuilder.xmi";
    private static final String NOT_ANALYSED_SYSTEM_XMI = "/systems/MyClassAdapter.xmi";

    private static final String[] EXPECTED_NOTES = {
            "The ConcreteFlyweight must be taken from the FlyweightFactory. The constructor of the " +
            "ConcreteFlyweight should be inaccessible, except by the FlyweightFactory."
    };

    private DesignPattern designPattern;

    @Before
    public void initTests() {
        final PatternsParser patternsParser = ParserFactory.createPatternParser();
        final String patternsXmlFile = FlyweightMatchingTest.class.getResource(TEMPLATES_XML).getFile();
        designPattern = getDesignPatternByName(patternsParser.parse(patternsXmlFile), PATTERN_NAME);
    }

    @Test
    public void testMatchingFlyweight() {
        assertMatchingPattern(MATCHING_SYSTEM_XMI, designPattern, EXPECTED_NOTES);
    }

    @Test
    public void testMismatchingFlyweight() {
        assertMismatchingPattern(MISMATCHING_SYSTEM_XMI, designPattern);
    }

    @Test
    public void testMismatchingFlyweightWithoutAnalysingNodesAndRelations() {
        assertMismatchingPatternWithoutAnalysingNodesAndRelations(NOT_ANALYSED_SYSTEM_XMI, designPattern);
    }

    protected void assertMatchingSolutions(PatternInspector.MatchingResult matchingResult) {
        final List<Solution> solutions = matchingResult.getSolutions();

        assertEquals(2, solutions.size());
        assertMatchingNodes(solutions, "Client", "Client");
        assertMatchingNodes(solutions, "FlyweightFactory", "FlyweightFactory");
        assertMatchingNodes(solutions, "Flyweight", "Flyweight");
        assertAnyMatchingNode(solutions,
                Arrays.asList(new String[]{"ConcreteFlyweight", "UnsharedConcreteFlyweight"}),
                Arrays.asList(new String[]{"ConcreteFlyweight"}));
    }

}
