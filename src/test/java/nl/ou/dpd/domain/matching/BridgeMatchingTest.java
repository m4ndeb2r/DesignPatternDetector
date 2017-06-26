package nl.ou.dpd.domain.matching;

import nl.ou.dpd.domain.DesignPattern;
import nl.ou.dpd.domain.SystemUnderConsideration;
import nl.ou.dpd.parsing.argoxmi.ArgoUMLParser;
import nl.ou.dpd.parsing.pattern.PatternsParser;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;

import static org.junit.Assert.assertFalse;

/**
 * Test the matching process for a Bridge pattern.
 *
 * @author Martin de Boer
 * @author Peter Vansweevelt
 */
public class BridgeMatchingTest {

    private String patternsXmlFile;
    private PatternsParser patternsParser;
    private ArgoUMLParser xmiParser;

    @Before
    public void initTests() {
        xmiParser = new ArgoUMLParser();
        patternsXmlFile = BridgeMatchingTest.class.getResource("/patterns_bridge.xml").getFile();
        patternsParser = new PatternsParser();
    }
<<<<<<< HEAD
   
    //test a Bridge written conforming the pattern,
    //with two concreteAbstractions and three ConcreteImplementations, from which two have an overriden method of Implementor
    //(pattern not found)
   @Test
    public void testMatchingBridge() {
=======
>>>>>>> origin/graphstructures

    @Test
    public void testMatchingBridge() {

        // Parse the bridge pattern xml ands create a DesignPattern
        final DesignPattern designPattern = patternsParser.parse(patternsXmlFile).get(0);

        // Create a system under consideration containing the observer pattern
        final URL sucXmiUrl = BridgeMatchingTest.class.getResource("/MyBridge.xmi");
        final SystemUnderConsideration system = xmiParser.parse(sucXmiUrl);

        // Inspect the system for patterns
        final PatternInspector patternInspector = new PatternInspector(system, designPattern);

        //TODO: test the values instead of printing it to the console
        TestHelper.printFeedback(designPattern, system, patternInspector, patternInspector.getSolutions());
<<<<<<< HEAD
        assertTrue(patternInspector.isomorphismExists());
=======

        //TODO Seems some nodepairs are not analysed
        assertFalse(patternInspector.isomorphismExists());
>>>>>>> origin/graphstructures
    }
}
