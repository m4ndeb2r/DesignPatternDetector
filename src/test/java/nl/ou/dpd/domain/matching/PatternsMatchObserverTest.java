package nl.ou.dpd.domain.matching;

import nl.ou.dpd.domain.DesignPattern;
import nl.ou.dpd.domain.SystemUnderConsideration;
import nl.ou.dpd.domain.matching.FeedbackType;
import nl.ou.dpd.domain.matching.PatternInspector;
import nl.ou.dpd.domain.matching.Solution;
import nl.ou.dpd.domain.node.Node;
import nl.ou.dpd.domain.relation.Relation;
import nl.ou.dpd.parsing.pattern.PatternsParser;
import org.jgrapht.GraphMapping;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Test xml-files against the patterns.xsd.
 *
 * @author Martin de Boer
 */
public class PatternsMatchObserverTest {

    private URL xsdUrl;
    private URL xmlUrl;
    private PatternsParser parser;

    @Before
    public void initTests() {
        xsdUrl = PatternsMatchObserverTest.class.getResource("/patterns.xsd");
        xmlUrl = PatternsMatchObserverTest.class.getResource("/patterns_observer.xml");
        parser = new PatternsParser();
    }

    @Test
    public void testMatchingObserver() {
        // Parse the observer pattern xml ands create a DesignPatternGraph
        final List<DesignPattern> designPatterns = parser.parse(xmlUrl, xsdUrl);
        final DesignPattern designPattern = designPatterns.get(0);

        // Create a system under consideration containing the observer pattern
        final SystemUnderConsideration system = TestHelper.createSystemUnderConsiderationGraphWithObserver();
        final Solution solution = new Solution(designPattern.getName());

        // Inspect the system for patterns
        final PatternInspector patternInspector = new PatternInspector(system, designPattern);
        assertTrue(patternInspector.isomorphismExists());
    }

    @Test
    public void testMatchingTwoObservers() {
        // Parse the observer pattern xml ands create a DesignPatternGraph
        final URL xmlUrl = PatternsMatchObserverTest.class.getResource("/patterns_observer.xml");
        final List<DesignPattern> designPatterns = parser.parse(xmlUrl, xsdUrl);
        final DesignPattern designPattern = designPatterns.get(0);

        // Create a system under consideration containing the two observer patterns and a singleton
        final SystemUnderConsideration system = TestHelper.createSystemUnderConsiderationGraphWithTwoObserversAndOneSingleton();

        // Check the number of mappings according toe the inspector.
        final PatternInspector patternInspector = new PatternInspector(system, designPattern);
        final Iterator<GraphMapping<Node, Relation>> iter = patternInspector.getMappings();
        final List<GraphMapping<Node, Relation>> graphMappings = asList(iter);
        assertThat(graphMappings.size(), is(2));

        final List<Solution> solutions = patternInspector.getSolutions();
        assertThat(solutions.size(), is(2));
        assertThat((solutions.get(0).getMatchingNodes().size()), is(4));
        assertThat((solutions.get(1).getMatchingNodes().size()), is(4));

        // TODO: temporary prints....
        TestHelper.printFeedback(designPattern, system, patternInspector, solutions);
    }

    @Test
    public void testMismatchInCardinalitiesForObserver() {
        // Parse the observer pattern xml ands create a DesignPatternGraph
        final URL xmlUrl = PatternsMatchObserverTest.class.getResource("/patterns_observer.xml");
        final List<DesignPattern> designPatterns = parser.parse(xmlUrl, xsdUrl);
        final DesignPattern designPattern = designPatterns.get(0);

        // Create a system under consideration containing the two observer patterns and a singleton
        final SystemUnderConsideration system = TestHelper.createSystemUnderConsiderationGraphWithObserverWithWrongCardinality();

        // Check the number of mappings according toe the inspector.
        final PatternInspector patternInspector = new PatternInspector(system, designPattern);
        final Iterator<GraphMapping<Node, Relation>> iter = patternInspector.getMappings();
        final List<GraphMapping<Node, Relation>> graphMappings = asList(iter);
        assertThat(graphMappings.size(), is(0));

        final List<Solution> solutions = patternInspector.getSolutions();
        assertThat(solutions.size(), is(0));

        final List<String> feedbackMessages = getAllRelationalMismatchFeedbackMessages(patternInspector, system);
        assertThat(feedbackMessages.stream()
                .filter(s -> s.startsWith("Mismatch with 'ConcreteObserver-Observer': unexpected left cardinality"))
                .count(), is(1L));

        // TODO: temporary prints....
        TestHelper.printFeedback(designPattern, system, patternInspector, solutions);
    }

    private List<GraphMapping<Node, Relation>> asList(Iterator<GraphMapping<Node, Relation>> iter) {
        List<GraphMapping<Node, Relation>> graphMappings = new ArrayList<>();
        iter.forEachRemaining(graphMapping -> graphMappings.add(graphMapping));
        return graphMappings;
    }

    private List<String> getAllRelationalMismatchFeedbackMessages(PatternInspector inspector, SystemUnderConsideration system) {
        final List<String> messages = new ArrayList<>();
        system.edgeSet().forEach(relation -> {
            messages.addAll(inspector.getFeedback().getFeedbackMessages(relation, FeedbackType.MISMATCH));
        });
        return messages;
    }

}
