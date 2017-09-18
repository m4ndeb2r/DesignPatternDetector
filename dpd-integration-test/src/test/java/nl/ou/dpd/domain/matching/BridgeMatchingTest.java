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
 * Test the matching process for a Bridge pattern.
 *
 * @author Martin de Boer
 */
@Category(IntegrationTest.class)
public class BridgeMatchingTest extends AbstractMatchingTest {

    private static final String PATTERN_NAME = "Bridge";
    private static final String MATCHING_SYSTEM_XMI = "/systems/MyBridge.xmi";
    private static final String MISMATCHING_SYSTEM_XMI = "/systems/MyDecorator.xmi";
    private static final String NOT_ANALYSED_SYSTEM_XMI = "/systems/MyClassAdapter.xmi";

    private static final String[] EXPECTED_NOTES = {
            "The Operation of the Abstraction class must call a method of the attribute of type Implementor.",
    };

    private DesignPattern designPattern;

    @Before
    public void initTests() {
        final PatternsParser patternsParser = ParserFactory.createPatternParser();
        final String patternsXmlFile = BridgeMatchingTest.class.getResource(TEMPLATES_XML).getFile();
        designPattern = getDesignPatternByName(patternsParser.parse(patternsXmlFile), PATTERN_NAME);
    }

    @Test
    public void testMatchingBridge() {
        assertMatchingPattern(MATCHING_SYSTEM_XMI, designPattern, EXPECTED_NOTES);
    }

    @Test
    public void testMismatchingBridge() {
        assertMismatchingPattern(MISMATCHING_SYSTEM_XMI, designPattern);
    }

    @Test
    public void testMismatchingBridgeWithoutAnalysingNodesAndRelations() {
        assertMismatchingPatternWithoutAnalysingNodesAndRelations(NOT_ANALYSED_SYSTEM_XMI, designPattern);
    }

    protected void assertMatchingSolutions(PatternInspector.MatchingResult matchingResult) {
        final List<Solution> solutions = matchingResult.getSolutions();

        assertEquals(6, solutions.size());
        assertMatchingNodes(solutions, "MyConcImpl1", "ConcreteImplementorB");
        assertMatchingNodes(solutions, "MyConcAbstr1", "RefinedAbstraction");
        assertMatchingNodes(solutions, "MyConcAbstr2", "RefinedAbstraction");
        assertMatchingNodes(solutions, "MyAbstraction", "Abstraction");
        assertMatchingNodes(solutions, "MyImplementor", "Implementor");
        assertAnyMatchingNode(solutions,
                Arrays.asList(new String[]{"MyConcImpl1", "MyConcImpl2", "MyConcImpl3"}),
                Arrays.asList(new String[]{"ConcreteImplementorA", "ConcreteImplementorB"})
        );
    }

}
