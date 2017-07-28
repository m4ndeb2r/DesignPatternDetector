package nl.ou.dpd.domain.matching;

import nl.ou.dpd.IntegrationTest;
import nl.ou.dpd.domain.DesignPattern;
import nl.ou.dpd.domain.SystemUnderConsideration;
import nl.ou.dpd.parsing.ParserFactory;
import nl.ou.dpd.parsing.ArgoUMLParser;
import nl.ou.dpd.parsing.PatternsParser;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test the matching process for a Bridge pattern.
 *
 * @author Martin de Boer
 * @author Peter Vansweevelt
 */
@Category(IntegrationTest.class)
public class BridgeMatchingTest {

    private String patternsXmlFile;
    private PatternsParser patternsParser;
    private ArgoUMLParser xmiParser;

    @Before
    public void initTests() {
        patternsParser = ParserFactory.createPatternParser();
        xmiParser = ParserFactory.createArgoUMLParser();
    }

    @Test
    public void testMatchingBridge() {
        patternsXmlFile = BridgeMatchingTest.class.getResource("/patterns/patterns_bridge.xml").getFile();

        // Parse the bridge pattern xml ands create a DesignPattern
        final DesignPattern designPattern = patternsParser.parse(patternsXmlFile).get(0);

        // Create a system under consideration containing the observer pattern
        final URL sucXmiUrl = BridgeMatchingTest.class.getResource("/systems/MyBridge.xmi");
        final SystemUnderConsideration system = xmiParser.parse(sucXmiUrl);

        // Check for a general note regarding the pattern, available from the xml-file
        assertThat(designPattern.getNotes().size(), is(1));
        assertTrue(designPattern.getNotes().iterator().next().startsWith("ArgoUML diagrams"));

        // Inspect the system for patterns
        final PatternInspector patternInspector = new PatternInspector(system, designPattern);
        final PatternInspector.MatchingResult matchingResult = patternInspector.getMatchingResult();
        final Feedback feedback = matchingResult.getFeedback();

        //TODO: test the values instead of printing it to the console
        TestHelper.printFeedback(designPattern, system, patternInspector);

        assertTrue(patternInspector.isomorphismExists());

        // More detailed, but not exhaustive inspection
        List<Solution> solutions = matchingResult.getSolutions(true);
        assertEquals(12, solutions.size());

        solutions = matchingResult.getSolutions();
        assertEquals(6, solutions.size());
        assertMatch(solutions, "MyAbstraction", "Abstraction");
        assertMatch(solutions, "MyImplementor", "Implementor");
        assertMatch(solutions, "MyConcAbstr1", "RefinedAbstraction");
        assertMatch(solutions, "MyConcAbstr2", "RefinedAbstraction");
        assertAnyMatch(solutions, Arrays.asList(new String[]{"MyConcImpl1", "MyConcImpl2", "MyConcImpl3"}), Arrays.asList(new String[]{"ConcreteImplementorA", "ConcreteImplementorB"}));
        assertMatch(solutions, "MyConcImpl1", "ConcreteImplementorB");

        assertThat(feedback.getNotes(), is(designPattern.getNotes()));

        // TODO Test feedback (getMatchingResult().getFeedback())
    }

    private void assertMatch(List<Solution> solutions, String sysNodeName, String patternNodeName) {
        assertTrue(TestHelper.areMatchingNodes(solutions, sysNodeName, patternNodeName));
    }

    private void assertAnyMatch(List<Solution> solutions, List<String> sysNodeNames, List<String> patternNodeNames) {
        boolean match = false;
        for (String sysNodeName : sysNodeNames) {
            for (String patternNodeName : patternNodeNames) {
                match |= TestHelper.areMatchingNodes(solutions, sysNodeName, patternNodeName);
            }
        }
        assertTrue(match);
    }

}
