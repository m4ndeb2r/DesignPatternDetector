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
 * Test the matching process for a Builder pattern.
 *
 * @author Martin de Boer
 */
@Category(IntegrationTest.class)
public class BuilderMatchingTest extends AbstractMatchingTest {

    private static final String PATTERN_NAME = "Builder";
    private static final String MATCHING_SYSTEM_XMI = "/systems/MyBuilder.xmi";
    private static final String MISMATCHING_SYSTEM_XMI = "/systems/MyDecorator.xmi";

    private static final String[] EXPECTED_NOTES = {
            "The Director may not deliver a Product. The Product must be provided by calling a method of the ConcreteBuilder."
    };

    private DesignPattern designPattern;

    @Before
    public void initTests() {
        final PatternsParser patternsParser = ParserFactory.createPatternParser();
        final String patternsXmlFile = BuilderMatchingTest.class.getResource(TEMPLATES_XML).getFile();
        designPattern = getDesignPatternByName(patternsParser.parse(patternsXmlFile), PATTERN_NAME);
    }

    @Test
    public void testMatchingBuilder() {
        assertMatchingPattern(MATCHING_SYSTEM_XMI, designPattern, EXPECTED_NOTES);
    }

    @Test
    public void testMismatchingBuilder() {
        assertMismatchingPattern(MISMATCHING_SYSTEM_XMI, designPattern);
    }

    protected void assertMatchingSolutions(PatternInspector.MatchingResult matchingResult) {
        final List<Solution> solutions = matchingResult.getSolutions();

        assertEquals(2, solutions.size());
        assertMatchingNodes(solutions, "Pizza", "Product");
        assertMatchingNodes(solutions, "Waiter", "Director");
        assertMatchingNodes(solutions, "PizzaBuilder", "Builder");
        assertAnyMatchingNode(solutions,
                Arrays.asList(new String[]{"SpicyPizza", "HawaiianPizza"}),
                Arrays.asList(new String[]{"ConcreteBuilder"})
        );
    }

}
