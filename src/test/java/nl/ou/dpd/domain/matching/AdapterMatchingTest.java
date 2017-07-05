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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test the matching process for a Adapter pattern.
 *
 * @author Martin de Boer
 * @author Peter Vansweevelt
 */
public class AdapterMatchingTest {

    private URL patternsXmlUrl;
    private URL sucXmiUrl;
    private PatternsParser patternsParser;
    private ArgoUMLParser xmiParser;

    @Before
    public void initTests() {
        xmiParser = new ArgoUMLParser();
    }

    @Test
    public void testMatchingObjectAdapter() {

        patternsXmlUrl = AdapterMatchingTest.class.getResource("/patterns/patterns_adapter.xml");
        patternsParser = new PatternsParser();
        sucXmiUrl = AdapterMatchingTest.class.getResource("/myPatterns/MyObjectAdapter.xmi");

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
        //more detailed inspection
        List<Solution> solutions = patternInspector.getMatchingResult().getSolutions();
        assertEquals(1, solutions.size());
        assertTrue(TestHelper.areMatchingNodes(solutions, "MyClient", "Client"));
        assertTrue(TestHelper.areMatchingNodes(solutions, "MyTarget", "Target"));
        assertTrue(TestHelper.areMatchingNodes(solutions, "MyAdapter", "Adapter"));
        assertTrue(TestHelper.areMatchingNodes(solutions, "MyAdaptee", "Adaptee"));
    }

    //test with an undirected association between Client and Target
    @Test
    public void testMatchingAdapterWithUndirectedAssociation() {
        patternsXmlUrl = AdapterMatchingTest.class.getResource("/patterns/patterns_adapter.xml");
        patternsParser = new PatternsParser();
        sucXmiUrl = AdapterMatchingTest.class.getResource("/myPatterns/MyAdapterWithUndirectedAssociation.xmi");

        // Parse the observer pattern xml ands create a DesignPattern
        final List<DesignPattern> designPatterns = patternsParser.parse(patternsXmlUrl.getFile());
        final DesignPattern designPattern = designPatterns.get(0);

        // Create a system under consideration containing the observer pattern
        final SystemUnderConsideration system = xmiParser.parse(sucXmiUrl);

        // Inspect the system for patterns
        final PatternInspector patternInspector = new PatternInspector(system, designPattern);

        // TODO Temporary method for visual feedback
        TestHelper.printFeedback(designPattern, system, patternInspector);

        // This pattern is not found because of the bidirected association.
        assertFalse(patternInspector.isomorphismExists());
    }

    //test with an undirected association to a Dummy and an extra Attribute in Adapter
    @Test
    public void testMatchingAdapterWithExtraAssociation() {

        patternsXmlUrl = AdapterMatchingTest.class.getResource("/patterns/patterns_adapter.xml");
        patternsParser = new PatternsParser();
        sucXmiUrl = AdapterMatchingTest.class.getResource("/myPatterns/MyAdapterWithExtraAssociation.xmi");

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
        assertEquals(1, solutions.size());
        assertTrue(TestHelper.areMatchingNodes(solutions, "MyClient", "Client"));
        assertTrue(TestHelper.areMatchingNodes(solutions, "MyTarget", "Target"));
        assertTrue(TestHelper.areMatchingNodes(solutions, "MyAdapter", "Adapter"));
        assertTrue(TestHelper.areMatchingNodes(solutions, "MyAdaptee", "Adaptee"));

        // TODO Test feedback (getMatchingResult().getFeedback())
    }

}
