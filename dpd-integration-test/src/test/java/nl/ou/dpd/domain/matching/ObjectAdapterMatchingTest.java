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
 * Test the matching process for a Decorator pattern.
 *
 * @author Martin de Boer
 */
@Category(IntegrationTest.class)
public class ObjectAdapterMatchingTest extends AbstractMatchingTest {

    private static final String PATTERN_NAME = "Object Adapter";
    private static final String MATCHING_SYSTEM_XMI = "/systems/MyObjectAdapter.xmi";
    private static final String MISMATCHING_SYSTEM_XMI = "/systems/MyBuilder.xmi";

    private static final String[] EXPECTED_NOTES = {
            "An operation of the Adapter must call an operation of the Adaptee.",
            "Set the adaptee attribute of Adapter with private visibility."
    };

    private DesignPattern designPattern;

    @Before
    public void initTests() {
        final PatternsParser patternsParser = ParserFactory.createPatternParser();
        final String patternsXmlFile = ObjectAdapterMatchingTest.class.getResource(TEMPLATES_XML).getFile();
        designPattern = getDesignPatternByName(patternsParser.parse(patternsXmlFile), PATTERN_NAME);
    }

    @Test
    public void testMatchingObjectAdapter() {
        assertMatchingPattern(MATCHING_SYSTEM_XMI, designPattern, EXPECTED_NOTES);

    }

    @Test
    public void testMismatchingObjectAdapter() {
        assertMismatchingPattern(MISMATCHING_SYSTEM_XMI, designPattern);
    }

    protected void assertMatchingSolutions(PatternInspector.MatchingResult matchingResult) {
        final List<Solution> solutions = matchingResult.getSolutions();

        assertEquals(1, solutions.size());
        assertMatchingNodes(solutions, "MyTarget", "Target");
        assertMatchingNodes(solutions, "MyClient", "Client");
        assertMatchingNodes(solutions, "MyAdapter", "Adapter");
        assertMatchingNodes(solutions, "MyAdaptee", "Adaptee");
    }

}
