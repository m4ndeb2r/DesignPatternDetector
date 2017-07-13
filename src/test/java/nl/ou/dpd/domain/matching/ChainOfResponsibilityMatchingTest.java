package nl.ou.dpd.domain.matching;

import nl.ou.dpd.domain.DesignPattern;
import nl.ou.dpd.domain.SystemUnderConsideration;
import nl.ou.dpd.parsing.argoxmi.ArgoUMLParser;
import nl.ou.dpd.parsing.pattern.PatternsParser;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

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
public class ChainOfResponsibilityMatchingTest {

    private String patternsXmlFile;
    private PatternsParser patternsParser;
    private ArgoUMLParser xmiParser;

    @Before
    public void initTests() {
        xmiParser = new ArgoUMLParser();
        patternsXmlFile = ChainOfResponsibilityMatchingTest.class.getResource("/patterns/patterns_chainofresponsibility.xml").getFile();
        patternsParser = new PatternsParser();
    }

    @Test
    @Ignore("Extra relationproperties obstruct pattern recognition. Peter is working on it....")
    public void testMatchingChainOfResponsibilityMatching() {
        // Parse the chainofresponsibility pattern xml ands create a DesignPattern
        final DesignPattern designPattern = patternsParser.parse(patternsXmlFile).get(0);

        // Create a system under consideration containing the observer pattern
        final URL sucXmiUrl = ChainOfResponsibilityMatchingTest.class.getResource("/systems/MyChainOfResponsibility.xmi");
        final SystemUnderConsideration system = xmiParser.parse(sucXmiUrl);

        // Inspect the system for patterns
        final PatternInspector patternInspector = new PatternInspector(system, designPattern);

        //TODO: test the values instead of printing it to the console
        TestHelper.printFeedback(designPattern, system, patternInspector);
        
        assertTrue(patternInspector.isomorphismExists());

        //more detailed, but not exhaustive inspection
        List<Solution> solutions = patternInspector.getMatchingResult().getSolutions();
        assertEquals(2, solutions.size());
        assertTrue(TestHelper.areMatchingNodes(solutions, "MyUser", "Client"));
        assertTrue(TestHelper.areMatchingNodes(solutions, "MyProcessor", "Handler"));
        assertTrue(TestHelper.areMatchingNodes(solutions, "MyConcrProcess2", "ConcreteHandlerA"));
        assertTrue(TestHelper.areMatchingNodes(solutions, "MyConcrProcess1", "ConcreteHandlerA"));
        assertTrue(TestHelper.areMatchingNodes(solutions, "MyConcrProcess2", "ConcreteHandlerB"));
        assertTrue(TestHelper.areMatchingNodes(solutions, "MyConcrProcess1", "ConcreteHandlerB"));

        // TODO Test feedback (getMatchingResult().getFeedback())
    }
}
