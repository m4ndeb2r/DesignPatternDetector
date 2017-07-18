package nl.ou.dpd.domain.matching;

import nl.ou.dpd.IntegrationTest;
import nl.ou.dpd.domain.DesignPattern;
import nl.ou.dpd.domain.SystemUnderConsideration;
import nl.ou.dpd.parsing.argoxmi.ArgoUMLParser;
import nl.ou.dpd.parsing.pattern.PatternsParser;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import javax.xml.XMLConstants;
import javax.xml.stream.XMLInputFactory;
import javax.xml.validation.SchemaFactory;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test the matching process for a Decorator pattern.
 *
 * @author Martin de Boer
 * @author Peter Vansweevelt
 */
@Category(IntegrationTest.class)
public class DecoratorMatchingTest {

    private String patternsXmlFile;
    private PatternsParser patternsParser;
    private ArgoUMLParser xmiParser;

    @Before
    public void initTests() {
        final SchemaFactory xsdSchemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        final XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        patternsParser = new PatternsParser(xsdSchemaFactory, xmlInputFactory);
        xmiParser = new ArgoUMLParser(XMLInputFactory.newInstance());
    }

    @Test
    public void testMatchingDecorator() {
        patternsXmlFile = DecoratorMatchingTest.class.getResource("/patterns/patterns_decorator.xml").getFile();

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
        List<Solution> solutions = patternInspector.getMatchingResult().getSolutions(true);
        assertEquals(2, solutions.size());
        assertMatch(solutions, "MyPart", "Component");
        assertMatch(solutions, "MyPart", "Component");
        assertMatch(solutions, "MyDecorator", "Decorator");
        assertMatch(solutions, "MyConcrDecA", "ConcreteDecoratorA");
        assertMatch(solutions, "MyConcrDecB", "ConcreteDecoratorB");
        assertMatch(solutions, "MyConcretePart", "ConcreteComponent");

        assertMatch(solutions, "MyPart", "Component");
        assertMatch(solutions, "MyDecorator", "Decorator");
        assertMatch(solutions, "MyConcrDecB", "ConcreteDecoratorA");
        assertMatch(solutions, "MyConcrDecA", "ConcreteDecoratorB");
        assertMatch(solutions, "MyConcretePart", "ConcreteComponent");

        solutions = patternInspector.getMatchingResult().getSolutions();
        assertEquals(1, solutions.size());
        assertMatch(solutions, "MyPart", "Component");
        assertMatch(solutions, "MyDecorator", "Decorator");
        assertAnyMatch(solutions, Arrays.asList(new String[]{"MyConcrDecB", "MyConcrDecB"}), Arrays.asList(new String[]{"ConcreteDecoratorA", "ConcreteDecoratorB"}));
        assertMatch(solutions, "MyConcretePart", "ConcreteComponent");

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
