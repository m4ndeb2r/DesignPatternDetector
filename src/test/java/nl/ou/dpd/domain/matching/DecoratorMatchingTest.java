package nl.ou.dpd.domain.matching;

import nl.ou.dpd.domain.DesignPattern;
import nl.ou.dpd.domain.SystemUnderConsideration;
import nl.ou.dpd.parsing.argoxmi.ArgoUMLParser;
import nl.ou.dpd.parsing.pattern.PatternsParser;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test the matching process for a Decorator pattern.
 *
 * @author Martin de Boer
 * @author Peter Vansweevelt
 */
public class DecoratorMatchingTest {

    private String patternsXmlFile;
    private PatternsParser patternsParser;
    private ArgoUMLParser xmiParser;

    @Before
    public void initTests() {
        xmiParser = new ArgoUMLParser();
        patternsXmlFile = DecoratorMatchingTest.class.getResource("/patterns/patterns_decorator.xml").getFile();
        patternsParser = new PatternsParser();
    }

    @Test
    public void testMatchingDecorator() {
        // Parse the decorator pattern xml ands create a DesignPattern
        final DesignPattern designPattern = patternsParser.parse(patternsXmlFile).get(0);

        // Create a system under consideration containing the observer pattern
        final URL sucXmiUrl = DecoratorMatchingTest.class.getResource("/systems/MyDecorator.xmi");
        final SystemUnderConsideration system = xmiParser.parse(sucXmiUrl);

        // Inspect the system for patterns
        final PatternInspector patternInspector = new PatternInspector(system, designPattern);

        //TODO: test the values instead of printing it to the console
        TestHelper.printFeedback(designPattern, system, patternInspector);

        assertTrue(patternInspector.isomorphismExists());
        //more detailed, but not exhaustive inspection
        List<Solution> solutions = patternInspector.getMatchingResult().getSolutions();
        assertEquals(2, solutions.size());
        assertTrue(TestHelper.areMatchingNodes(solutions, "MyPart", "Component"));
        assertTrue(TestHelper.areMatchingNodes(solutions, "MyDecorator", "Decorator"));
        assertTrue(TestHelper.areMatchingNodes(solutions, "MyConcrDecA", "ConcreteDecoratorA"));
        assertTrue(TestHelper.areMatchingNodes(solutions, "MyConcrDecB", "ConcreteDecoratorB"));
        assertTrue(TestHelper.areMatchingNodes(solutions, "MyConcretePart", "ConcreteComponent"));

        // TODO Test feedback (getMatchingResult().getFeedback())
    }
}
