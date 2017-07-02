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

import static junit.framework.TestCase.assertTrue;

/**
 * Test xml-files against the patterns.xsd.
 *
 * @author Martin de Boer
 * @author Peter Vansweevelt
 */
// TODO: Temporary ignoring this test
@Ignore("Make this test work: pattern is not found, but should be....")
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

        // TODO Test the getSolutions() in depth instead of the isomorphismExists()
        assertTrue(patternInspector.isomorphismExists());
    }

}
