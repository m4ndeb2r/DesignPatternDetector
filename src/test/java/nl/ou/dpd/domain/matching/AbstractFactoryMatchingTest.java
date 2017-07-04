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

/**
 * Test xml-files against the patterns.xsd.
 *
 * @author Martin de Boer
 * @author Peter Vansweevelt
 */
public class AbstractFactoryMatchingTest {

    private URL patternsXmlUrl;
    private URL sucXmiUrl;
    private PatternsParser patternsParser;
    private ArgoUMLParser xmiParser;

    @Before
    public void initTests() {
        xmiParser = new ArgoUMLParser();
    }

    //test an AbstractFactory written conforming the pattern
    @Test
    public void testMatchingAbstractFactoryWithoutMethods() {
        patternsXmlUrl = AbstractFactoryMatchingTest.class.getResource("/patterns_abstractfactory.xml");
        patternsParser = new PatternsParser();
        sucXmiUrl = AbstractFactoryMatchingTest.class.getResource("/MyAbstractFactoryWithoutMethodsSimplified.xmi");

        // Parse the observer pattern xml ands create a DesignPattern
        final List<DesignPattern> designPatterns = patternsParser.parse(patternsXmlUrl.getFile());
        final DesignPattern designPattern = designPatterns.get(0);

        // Create a system under consideration containing the observer pattern
        final SystemUnderConsideration system = xmiParser.parse(sucXmiUrl);

        // Inspect the system for patterns
        final PatternInspector patternInspector = new PatternInspector(system, designPattern);

        // TODO Temporary method for visual feedback
        TestHelper.printFeedback(designPattern, system, patternInspector);

        assertTrue(patternInspector.isomorphismExists());
        //more detailed, but not exhaustive inspection
        List<Solution> solutions = patternInspector.getMatchingResult().getSolutions();
        assertEquals(4, solutions.size());
        assertTrue(TestHelper.areMatchingNodes(solutions, "MyUser", "Client"));
        assertTrue(TestHelper.areMatchingNodes(solutions, "ResFactory", "AbstractFactory"));
        assertTrue(TestHelper.areMatchingNodes(solutions, "DisplayDriverFact", "AbstractProductA"));
        assertTrue(TestHelper.areMatchingNodes(solutions, "PrintDriverFact", "AbstractProductB"));
        assertTrue(TestHelper.areMatchingNodes(solutions, "LRDD", "ProductA1"));
        assertTrue(TestHelper.areMatchingNodes(solutions, "LRPD", "ProductA2"));
        assertTrue(TestHelper.areMatchingNodes(solutions, "HRDD", "ProductB1"));
        assertTrue(TestHelper.areMatchingNodes(solutions, "HRPD", "ProductB2"));
        assertTrue(TestHelper.areMatchingNodes(solutions, "LowResFact", "ConcreteFactory1"));
        assertTrue(TestHelper.areMatchingNodes(solutions, "HighResFact", "ConcreteFactory2"));        
    }

    //test an AbstractFactory with more abstract factories
    @Test
    public void testMatchingAbstractFactoryThreeFactories() {
        patternsXmlUrl = AbstractFactoryMatchingTest.class.getResource("/patterns_abstractfactory.xml");
        patternsParser = new PatternsParser();
        sucXmiUrl = AbstractFactoryMatchingTest.class.getResource("/MyAbstractFactoryWithoutMethods.xmi");

        // Parse the observer pattern xml ands create a DesignPattern
        final List<DesignPattern> designPatterns = patternsParser.parse(patternsXmlUrl.getFile());
        final DesignPattern designPattern = designPatterns.get(0);

        // Create a system under consideration containing the observer pattern
        final SystemUnderConsideration system = xmiParser.parse(sucXmiUrl);

        // Inspect the system for patterns
        final PatternInspector patternInspector = new PatternInspector(system, designPattern);

        // TODO Temporary method for visual feedback
        TestHelper.printFeedback(designPattern, system, patternInspector);

        assertTrue(patternInspector.isomorphismExists());
        List<Solution> solutions = patternInspector.getMatchingResult().getSolutions();
        assertEquals(12, solutions.size());
    }
    
    //test an AbstractFactory with more abstract factories and more concrete products
    @Test
    public void testMatchingAbstractFactoryFourFactories() {
        patternsXmlUrl = AbstractFactoryMatchingTest.class.getResource("/patterns_abstractfactory.xml");
        patternsParser = new PatternsParser();
        sucXmiUrl = AbstractFactoryMatchingTest.class.getResource("/MyAbstractFactoryMultiple.xmi");

        // Parse the observer pattern xml ands create a DesignPattern
        final List<DesignPattern> designPatterns = patternsParser.parse(patternsXmlUrl.getFile());
        final DesignPattern designPattern = designPatterns.get(0);

        // Create a system under consideration containing the observer pattern
        final SystemUnderConsideration system = xmiParser.parse(sucXmiUrl);

        // Inspect the system for patterns
        final PatternInspector patternInspector = new PatternInspector(system, designPattern);

        // TODO Temporary method for visual feedback
        TestHelper.printFeedback(designPattern, system, patternInspector);

        assertTrue(patternInspector.isomorphismExists());
        List<Solution> solutions = patternInspector.getMatchingResult().getSolutions();
        assertEquals(72, solutions.size());
    }
}
