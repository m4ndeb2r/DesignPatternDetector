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
 * Test the matching process for a ChainOfResponsibility pattern.
 *
 * @author Martin de Boer
 */
@Category(IntegrationTest.class)
public class ChainOfResponsibilityMatchingTest extends AbstractMatchingTest {

    private static final String PATTERN_NAME = "Chain Of Responsibility";
    private static final String MATCHING_SYSTEM_XMI = "/systems/MyChainOfResponsibility.xmi";
    private static final String MISMATCHING_SYSTEM_XMI = "/systems/MyBuilder.xmi";
    private static final String NOT_ANALYSED_SYSTEM_XMI = "/systems/MyClassAdapter.xmi";

    private static final String[] EXPECTED_NOTES = {};

    private DesignPattern designPattern;

    @Before
    public void initTests() {
        final PatternsParser patternsParser = ParserFactory.createPatternParser();
        final String patternsXmlFile = ChainOfResponsibilityMatchingTest.class.getResource(TEMPLATES_XML).getFile();
        designPattern = getDesignPatternByName(patternsParser.parse(patternsXmlFile), PATTERN_NAME);
    }

    @Test
    public void testMatchingChainOfResponsibility() {
        assertMatchingPattern(MATCHING_SYSTEM_XMI, designPattern, EXPECTED_NOTES);
    }

    @Test
    public void testMismatchingChainOfResponsibility() {
        assertMismatchingPattern(MISMATCHING_SYSTEM_XMI, designPattern);
    }

    @Test
    public void testMismatchingChainOfResponsibilityWithoutAnalysingNodesAndRelations() {
        assertMismatchingPatternWithoutAnalysingNodesAndRelations(NOT_ANALYSED_SYSTEM_XMI, designPattern);
    }

    protected void assertMatchingSolutions(PatternInspector.MatchingResult matchingResult) {
        final List<Solution> solutions = matchingResult.getSolutions();

        assertEquals(1, solutions.size());
        assertMatchingNodes(solutions, "MyUser", "Client");
        assertMatchingNodes(solutions, "MyProcessor", "Handler");
        assertMatchingNodes(solutions, "MyConcrProcess1", "ConcreteHandlerA");
        assertMatchingNodes(solutions, "MyConcrProcess2", "ConcreteHandlerB");
    }

}
