package nl.ou.dpd.domain.matching;

import nl.ou.dpd.IntegrationTest;
import nl.ou.dpd.domain.DesignPattern;
import nl.ou.dpd.domain.SystemUnderConsideration;
import nl.ou.dpd.parsing.ArgoUMLParser;
import nl.ou.dpd.parsing.ParserFactory;
import nl.ou.dpd.parsing.PatternsParser;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.net.URL;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test the matching process for a ChainOfResponsibility pattern.
 *
 * @author Martin de Boer
 * @author Peter Vansweevelt
 */
@Ignore
@Category(IntegrationTest.class)
public class OldChainOfResponsibilityMatchingTest extends AbstractMatchingTest {

    private String patternsXmlFile;
    private PatternsParser patternsParser;
    private ArgoUMLParser xmiParser;

    @Before
    public void initTests() {
        patternsParser = ParserFactory.createPatternParser();
        xmiParser = ParserFactory.createArgoUMLParser();
    }

    @Test
    @Ignore("Extra relationproperties obstruct pattern recognition. Peter is working on it....") // TODO
    public void testMatchingChainOfResponsibilityMatching() {
        patternsXmlFile = OldChainOfResponsibilityMatchingTest.class.getResource("/patterns/patterns_chainofresponsibility.xml").getFile();

        // Parse the chainofresponsibility pattern xml ands create a DesignPattern
        final DesignPattern designPattern = patternsParser.parse(patternsXmlFile).get(0);

        // Create a system under consideration containing the observer pattern
        final URL sucXmiUrl = OldChainOfResponsibilityMatchingTest.class.getResource("/systems/MyChainOfResponsibility.xmi");
        final SystemUnderConsideration system = xmiParser.parse(sucXmiUrl);

        // Inspect the system for patterns
        final PatternInspector patternInspector = new PatternInspector(system, designPattern);

        assertTrue(patternInspector.isomorphismExists());

        //more detailed, but not exhaustive inspection
        List<Solution> solutions = patternInspector.getMatchingResult().getSolutions();
        assertEquals(2, solutions.size());
        assertTrue(areMatchingNodes(solutions, "MyUser", "Client"));
        assertTrue(areMatchingNodes(solutions, "MyProcessor", "Handler"));
        assertTrue(areMatchingNodes(solutions, "MyConcrProcess2", "ConcreteHandlerA"));
        assertTrue(areMatchingNodes(solutions, "MyConcrProcess1", "ConcreteHandlerA"));
        assertTrue(areMatchingNodes(solutions, "MyConcrProcess2", "ConcreteHandlerB"));
        assertTrue(areMatchingNodes(solutions, "MyConcrProcess1", "ConcreteHandlerB"));

        // TODO Test feedback (getMatchingResult().getFeedback())
    }
}
