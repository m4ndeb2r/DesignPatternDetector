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

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Test xml-files against the patterns.xsd.
 *
 * @author Martin de Boer
 * @author Peter Vansweevelt
 */
@Ignore
@Category(IntegrationTest.class)
public class OldAbstractFactoryMatchingTest extends AbstractMatchingTest {

    private URL patternsXmlUrl;
    private URL sucXmiUrl;
    private PatternsParser patternsParser;
    private ArgoUMLParser xmiParser;

    @Before
    public void initTests() {
        patternsParser = ParserFactory.createPatternParser();
        xmiParser = ParserFactory.createArgoUMLParser();
    }

    //test an AbstractFactory written conforming the pattern
    @Test
    public void testMatchingAbstractFactoryWithoutMethods() {
        patternsXmlUrl = OldAbstractFactoryMatchingTest.class.getResource("/patterns/designpatterns_templates.xml");
        sucXmiUrl = OldAbstractFactoryMatchingTest.class.getResource("/systems/MyAbstractFactory.xmi");

        // Parse the observer pattern xml ands create a DesignPattern
        final List<DesignPattern> designPatterns = patternsParser.parse(patternsXmlUrl.getFile());
        final DesignPattern designPattern = designPatterns.get(0);

        // Create a system under consideration containing the observer pattern
        final SystemUnderConsideration system = xmiParser.parse(sucXmiUrl);

        // Inspect the system for patterns
        final PatternInspector patternInspector = new PatternInspector(system, designPattern);

        assertTrue(patternInspector.isomorphismExists());
        //more detailed, but not exhaustive inspection
        List<Solution> solutions = patternInspector.getMatchingResult().getSolutions(true);
        assertEquals(4, solutions.size());
        assertTrue(areMatchingNodes(solutions, "MyUser", "Client"));
        assertTrue(areMatchingNodes(solutions, "ResFactory", "AbstractFactory"));
        assertTrue(areMatchingNodes(solutions, "DisplayDriverFact", "AbstractProductA"));
        assertTrue(areMatchingNodes(solutions, "PrintDriverFact", "AbstractProductB"));
        assertTrue(areMatchingNodes(solutions, "LRDD", "ProductA1"));
        assertTrue(areMatchingNodes(solutions, "LRPD", "ProductA2"));
        assertTrue(areMatchingNodes(solutions, "HRDD", "ProductB1"));
        assertTrue(areMatchingNodes(solutions, "HRPD", "ProductB2"));
        assertTrue(areMatchingNodes(solutions, "LowResFact", "ConcreteFactory1"));
        assertTrue(areMatchingNodes(solutions, "HighResFact", "ConcreteFactory2"));

        solutions = patternInspector.getMatchingResult().getSolutions();
        assertEquals(1, solutions.size()); // TODO: Check this in depth

        // TODO Test feedback (getMatchingResult().getFeedback())
    }

    //test an AbstractFactory with more abstract factories
    @Test
    public void testMatchingAbstractFactoryThreeFactories() {
        patternsXmlUrl = OldAbstractFactoryMatchingTest.class.getResource("/patterns/patterns_abstractfactory.xml");
        sucXmiUrl = OldAbstractFactoryMatchingTest.class.getResource("/systems/MyAbstractFactoryThreeFactories.xmi");

        // Parse the observer pattern xml ands create a DesignPattern
        final List<DesignPattern> designPatterns = patternsParser.parse(patternsXmlUrl.getFile());
        final DesignPattern designPattern = designPatterns.get(0);

        // Create a system under consideration containing the observer pattern
        final SystemUnderConsideration system = xmiParser.parse(sucXmiUrl);

        // Inspect the system for patterns
        final PatternInspector patternInspector = new PatternInspector(system, designPattern);

        assertTrue(patternInspector.isomorphismExists());
        List<Solution> solutions = patternInspector.getMatchingResult().getSolutions(true);
        assertEquals(12, solutions.size());

        solutions = patternInspector.getMatchingResult().getSolutions();
        assertEquals(3, solutions.size()); // TODO: Check this in depth

        // TODO Test feedback (getMatchingResult().getFeedback())
    }
    
    //test an AbstractFactory with more abstract factories and more concrete products
    @Test
    public void testMatchingAbstractFactoryFourFactories() {
        patternsXmlUrl = OldAbstractFactoryMatchingTest.class.getResource("/patterns/patterns_abstractfactory.xml");
        sucXmiUrl = OldAbstractFactoryMatchingTest.class.getResource("/systems/MyAbstractFactoryMultiple.xmi");

        // Parse the observer pattern xml ands create a DesignPattern
        final List<DesignPattern> designPatterns = patternsParser.parse(patternsXmlUrl.getFile());
        final DesignPattern designPattern = designPatterns.get(0);

        // Create a system under consideration containing the observer pattern
        final SystemUnderConsideration system = xmiParser.parse(sucXmiUrl);

        // Inspect the system for patterns
        final PatternInspector patternInspector = new PatternInspector(system, designPattern);

        assertTrue(patternInspector.isomorphismExists());
        List<Solution> solutions = patternInspector.getMatchingResult().getSolutions(true);
        assertEquals(72, solutions.size());

        solutions = patternInspector.getMatchingResult().getSolutions();
        assertEquals(18, solutions.size()); // TODO: Check this in depth

        // TODO Test feedback (getMatchingResult().getFeedback())
    }
}
