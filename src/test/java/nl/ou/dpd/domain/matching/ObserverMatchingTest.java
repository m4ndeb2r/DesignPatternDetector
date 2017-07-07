package nl.ou.dpd.domain.matching;

import nl.ou.dpd.domain.DesignPattern;
import nl.ou.dpd.domain.SystemUnderConsideration;
import nl.ou.dpd.parsing.argoxmi.ArgoUMLParser;
import nl.ou.dpd.parsing.pattern.PatternsParser;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Test the matching process for a Observer pattern.
 *
 * @author Martin de Boer
 * @author Peter Vansweevelt
 */
public class ObserverMatchingTest {

    private String patternsXmlFilename;
    private PatternsParser patternsParser;
    private ArgoUMLParser xmiParser;

    @Before
    public void initTests() {
        xmiParser = new ArgoUMLParser();
        patternsXmlFilename = ObserverMatchingTest.class.getResource("/patterns/patterns_observer.xml").getFile();
        patternsParser = new PatternsParser();
    }

    @Test
    public void testMatchingObserverExample() {
        // Parse the observer pattern xml ands create a DesignPattern
        final DesignPattern designPattern = patternsParser.parse(patternsXmlFilename).get(0);

        // Create a system under consideration containing the observer pattern
        final URL sucXmiUrl = ObserverMatchingTest.class.getResource("/systems/MyObserver.xmi");
        final SystemUnderConsideration system = xmiParser.parse(sucXmiUrl);

        // Inspect the system for patterns
        final PatternInspector patternInspector = new PatternInspector(system, designPattern);

        // TODO Temporary method for visual feedback
        TestHelper.printFeedback(designPattern, system, patternInspector);

        assertTrue(patternInspector.isomorphismExists());
        //more detailed, but not exhaustive inspection
        List<Solution> solutions = patternInspector.getMatchingResult().getSolutions();
        assertEquals(1, solutions.size());
        assertTrue(TestHelper.areMatchingNodes(solutions, "MySubject", "Subject"));
        assertTrue(TestHelper.areMatchingNodes(solutions, "MyObserver", "Observer"));
        assertTrue(TestHelper.areMatchingNodes(solutions, "MyConcreteSubject", "ConcreteSubject"));
        assertTrue(TestHelper.areMatchingNodes(solutions, "MyConcreteObserver", "ConcreteObserver"));
    }

}