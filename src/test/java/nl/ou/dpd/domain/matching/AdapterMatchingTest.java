package nl.ou.dpd.domain.matching;

import nl.ou.dpd.domain.DesignPattern;
import nl.ou.dpd.domain.SystemUnderConsideration;
import nl.ou.dpd.domain.matching.PatternInspector;
import nl.ou.dpd.domain.matching.Solution;
import nl.ou.dpd.parsing.argoxmi.ArgoUMLParser;
import nl.ou.dpd.parsing.pattern.PatternsParser;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;

/**
 * Test xml-files against the patterns.xsd.
 *
 * @author Martin de Boer
 */
public class AdapterMatchingTest {

    private URL xsdUrl;
    private URL patternsXmlUrl;
    private URL sucXmiUrl;
    private PatternsParser patternsParser;
    private ArgoUMLParser xmiParser;

    @Before
    public void initTests() {
        xsdUrl = AdapterMatchingTest.class.getResource("/patterns.xsd");
        xmiParser = new ArgoUMLParser();
    }
    
    //test an adapter written conforming the pattern 
   @Test
    public void testMatchingAdapter() {

       patternsXmlUrl = AdapterMatchingTest.class.getResource("/patterns_adapter.xml");
       patternsParser = new PatternsParser();
       sucXmiUrl = AdapterMatchingTest.class.getResource("/adapters_interface.xmi");

	   // Parse the observer pattern xml ands create a DesignPattern
        final List<DesignPattern> designPatterns = patternsParser.parse(patternsXmlUrl, xsdUrl);
        final DesignPattern designPattern = designPatterns.get(0);

        // Create a system under consideration containing the observer pattern
        final SystemUnderConsideration system = xmiParser.parse(sucXmiUrl);
        final Solution solution = new Solution(designPattern.getName());

        // Inspect the system for patterns
        final PatternInspector patternInspector = new PatternInspector(system, designPattern);
        //TODO
        TestHelper.printFeedback(designPattern, system, patternInspector, patternInspector.getSolutions());
        assertTrue(patternInspector.isomorphismExists());
    }
   
    //test with an undirected association between Client and Target
    @Test
    public void testMatchingAdapterWithAssociation() {
    	
        patternsXmlUrl = AdapterMatchingTest.class.getResource("/patterns_adapter.xml");
        patternsParser = new PatternsParser();
        sucXmiUrl = AdapterMatchingTest.class.getResource("/adapters_structures_association.xmi");
    	
        // Parse the observer pattern xml ands create a DesignPattern
        final List<DesignPattern> designPatterns = patternsParser.parse(patternsXmlUrl, xsdUrl);
        final DesignPattern designPattern = designPatterns.get(0);

        // Create a system under consideration containing the observer pattern
        final SystemUnderConsideration system = xmiParser.parse(sucXmiUrl);
        final Solution solution = new Solution(designPattern.getName());

        // Inspect the system for patterns
        final PatternInspector patternInspector = new PatternInspector(system, designPattern);
        //TODO
        TestHelper.printFeedback(designPattern, system, patternInspector, patternInspector.getSolutions());
        //TODO Looks like there are problems with the bidirected association - not all nodes are compared
        assertFalse(patternInspector.isomorphismExists());
    }

    //test with an undirected association to a Dummy and an extra Attribute in Adapter
    @Test
    public void testMatchingAdapterWithExtras() {
    	
        patternsXmlUrl = AdapterMatchingTest.class.getResource("/patterns_adapter.xml");
        patternsParser = new PatternsParser();
        sucXmiUrl = AdapterMatchingTest.class.getResource("/adaptersWithExtras.xmi");
    	
        // Parse the observer pattern xml ands create a DesignPattern
        final List<DesignPattern> designPatterns = patternsParser.parse(patternsXmlUrl, xsdUrl);
        final DesignPattern designPattern = designPatterns.get(0);

        // Create a system under consideration containing the observer pattern
        final SystemUnderConsideration system = xmiParser.parse(sucXmiUrl);
        final Solution solution = new Solution(designPattern.getName());

        // Inspect the system for patterns
        final PatternInspector patternInspector = new PatternInspector(system, designPattern);
        //TODO
        TestHelper.printFeedback(designPattern, system, patternInspector, patternInspector.getSolutions());
        assertTrue(patternInspector.isomorphismExists());
    }

}
