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
 * Test the matching process for a Command pattern.
 *
 * @author Martin de Boer
 */
@Category(IntegrationTest.class)
public class CommandMatchingTest extends AbstractMatchingTest {

    private static final String PATTERN_NAME = "Command";
    private static final String MATCHING_SYSTEM_XMI = "/systems/MyCommand.xmi";
    private static final String MISMATCHING_SYSTEM_XMI = "/systems/MyBuilder.xmi";

    private static final String[] EXPECTED_NOTES = {
            "The execute method of the ConcreteCommand must invoke an action of the Reciever.",
            "The attribute of type Reciever of the ConcreteCommand should be set with a private visibility."
    };

    private DesignPattern designPattern;

    @Before
    public void initTests() {
        final PatternsParser patternsParser = ParserFactory.createPatternParser();
        final String patternsXmlFile = CommandMatchingTest.class.getResource(TEMPLATES_XML).getFile();
        designPattern = getDesignPatternByName(patternsParser.parse(patternsXmlFile), PATTERN_NAME);
    }

    @Test
    public void testMatchingCommand() {
        assertMatchingPattern(MATCHING_SYSTEM_XMI, designPattern, EXPECTED_NOTES);
    }

    @Test
    public void testMismatchingCommand() {
        assertMismatchingPattern(MISMATCHING_SYSTEM_XMI, designPattern);
    }

    protected void assertMatchingSolutions(PatternInspector.MatchingResult matchingResult) {
        final List<Solution> solutions = matchingResult.getSolutions();

        assertEquals(2, solutions.size());
        assertMatchingNodes(solutions, "Light", "Receiver");
        assertMatchingNodes(solutions, "Switch", "Invoker");
        assertMatchingNodes(solutions, "Command", "Command");
        assertMatchingNodes(solutions, "PressSwitch", "Client");
        assertAnyMatchingNode(solutions,
                Arrays.asList(new String[]{"FlipUpCommand", "FlipDownCommand"}),
                Arrays.asList(new String[]{"ConcreteCommand"}));
    }

}
