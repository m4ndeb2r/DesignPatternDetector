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
 * Test the matching process for a Decorator pattern.
 *
 * @author Martin de Boer
 */
@Category(IntegrationTest.class)
public class DecoratorMatchingTest extends AbstractMatchingTest {

    private static final String PATTERN_NAME = "Decorator";
    private static final String MATCHING_SYSTEM_XMI = "/systems/MyDecorator.xmi";
    private static final String MISMATCHING_SYSTEM_XMI = "/systems/MyBuilder.xmi";
    private static final String NOT_ANALYSED_SYSTEM_XMI = "/systems/MyClassAdapter.xmi";

    private static final String[] EXPECTED_NOTES = {};

    private DesignPattern designPattern;

    @Before
    public void initTests() {
        final PatternsParser patternsParser = ParserFactory.createPatternParser();
        final String patternsXmlFile = DecoratorMatchingTest.class.getResource(TEMPLATES_XML).getFile();
        designPattern = getDesignPatternByName(patternsParser.parse(patternsXmlFile), PATTERN_NAME);
    }

    @Test
    public void testMatchingDecorator() {
        assertMatchingPattern(MATCHING_SYSTEM_XMI, designPattern, EXPECTED_NOTES);

    }

    @Test
    public void testMismatchingDecorator() {
        assertMismatchingPattern(MISMATCHING_SYSTEM_XMI, designPattern);
    }

    @Test
    public void testMismatchingDecoratorWithoutAnalysingNodesAndRelations() {
        assertMismatchingPatternWithoutAnalysingNodesAndRelations(NOT_ANALYSED_SYSTEM_XMI, designPattern);
    }

    protected void assertMatchingSolutions(PatternInspector.MatchingResult matchingResult) {
        final List<Solution> solutions = matchingResult.getSolutions();

        assertEquals(1, solutions.size());
        assertMatchingNodes(solutions, "MyPart", "Component");
        assertMatchingNodes(solutions, "MyDecorator", "Decorator");
        assertMatchingNodes(solutions, "MyConcretePart", "ConcreteComponent");
        assertAnyMatchingNode(solutions,
                Arrays.asList(new String[]{"MyConcrDecA", "MyConcrDecB"}),
                Arrays.asList(new String[]{"ConcreteDecoratorA", "ConcreteDecoratorB"}));
    }

}
