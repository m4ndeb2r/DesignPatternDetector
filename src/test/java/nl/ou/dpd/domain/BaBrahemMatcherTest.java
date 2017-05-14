package nl.ou.dpd.domain;

import nl.ou.dpd.domain.edge.Edge;
import nl.ou.dpd.domain.edge.EdgeType;
import nl.ou.dpd.domain.node.Node;
import nl.ou.dpd.utils.TestHelper;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertTrue;

/**
 * Tests the {@link Matcher} class. This test uses the BaBrahem system design as input and attempts to detect a number
 * of patterns.
 *
 * @author Martin de Boer
 */
public class BaBrahemMatcherTest {

    private Matcher matcher;
    private SystemUnderConsideration system;

    /**
     * Initialises the test(s).
     */
    @Before
    public void initMatcher() {
        matcher = new Matcher();
        system = createBaBrahemSystemUnderConsideration();
    }

    /**
     * Tests if the adapter pattern is detected with 1 missing edge allowed.
     */
    @Test
    public void testMatchAdapter() {
        final DesignPattern pattern = TestHelper.createAdapterPattern();
        final Solutions matchResult = matcher.match(pattern, system, 1);
        final List<Solution> solutions = matchResult.getSolutions();

        // Check number of times the pattern was found
        assertThat(solutions.size(), is(3));

        final Solution s0 = solutions.get(0);
        final MatchedNodes mc0 = s0.getMatchedNodes();
        final Set<Edge> se0 = s0.getSuperfluousEdges();
        final Set<Edge> me0 = s0.getMissingEdges();

        // Check name
        assertThat(s0.getDesignPatternName(), is("Adapter"));

        // Check matching classes
        assertThat(mc0.get(TestHelper.createClazz("B")).getName(), is("Target"));
        assertThat(mc0.get(TestHelper.createClazz("C")).getName(), is("Client"));
        assertThat(mc0.get(TestHelper.createClazz("D")).getName(), is("Adapter"));

        // Check superfluous edges
        assertThat(se0.size(), is(0));

        // Check missing edges
        assertThat(me0.size(), is(1));
        assertTrue(me0.contains(
                new Edge(
                        TestHelper.createClazz("Adapter"),
                        TestHelper.createClazz("Adaptee"),
                        EdgeType.ASSOCIATION_DIRECTED)));

        final Solution s1 = solutions.get(1);
        final MatchedNodes mc1 = s1.getMatchedNodes();
        final Set<Edge> se1 = s1.getSuperfluousEdges();
        final Set<Edge> me1 = s1.getMissingEdges();

        // Check name
        assertThat(s1.getDesignPatternName(), is("Adapter"));

        // Check matching classes
        assertThat(mc1.get(TestHelper.createClazz("A")).getName(), is("Client"));
        assertThat(mc1.get(TestHelper.createClazz("B")).getName(), is("Target"));
        assertThat(mc1.get(TestHelper.createClazz("D")).getName(), is("Adapter"));

        // Check superfluous edges
        assertThat(se1.size(), is(0));

        // Check missing edges
        assertThat(me1.size(), is(1));
        assertTrue(me1.contains(
                new Edge(
                        TestHelper.createClazz("Adapter"),
                        TestHelper.createClazz("Adaptee"),
                        EdgeType.ASSOCIATION_DIRECTED)));

        final Solution s2 = solutions.get(2);
        final MatchedNodes mc2 = s2.getMatchedNodes();
        final Set<Edge> se2 = s2.getSuperfluousEdges();
        final Set<Edge> me2 = s2.getMissingEdges();

        // Check name
        assertThat(s2.getDesignPatternName(), is("Adapter"));

        // Check matching classes
        assertThat(mc2.get(TestHelper.createClazz("A")).getName(), is("Client"));
        assertThat(mc2.get(TestHelper.createClazz("C")).getName(), is("Target"));
        assertThat(mc2.get(TestHelper.createClazz("E")).getName(), is("Adapter"));

        // Check superfluous edges
        assertThat(se2.size(), is(0));

        // Check missing edges
        assertThat(me2.size(), is(1));
        assertTrue(me2.contains(
                new Edge(
                        TestHelper.createClazz("Adapter"),
                        TestHelper.createClazz("Adaptee"),
                        EdgeType.ASSOCIATION_DIRECTED)));
    }

    /**
     * Tests if the adapter pattern is detected with 0 missing edges allowed.
     */
    @Test
    public void testMatchAdapterNoMissingEdges() {
        final DesignPattern pattern = TestHelper.createAdapterPattern();
        final Solutions matchResult = matcher.match(pattern, system, 0);
        // The pattern is not expected to be detected
        assertThat(matchResult.getSolutions().size(), is(0));
    }

    /**
     * Tests if the bridge pattern is detected with 1 missing edge allowed.
     */
    @Test
    public void testMatchBridge() {
        final DesignPattern pattern = TestHelper.createBridgePattern();
        final Solutions matchResult = matcher.match(pattern, system, 1);
        final List<Solution> solutions = matchResult.getSolutions();

        // Check number of times the pattern is detected.
        assertThat(solutions.size(), is(1));

        final Solution s0 = solutions.get(0);
        final MatchedNodes mc0 = s0.getMatchedNodes();
        final Set<Edge> se0 = s0.getSuperfluousEdges();
        final Set<Edge> me0 = s0.getMissingEdges();

        // Check the name
        assertThat(s0.getDesignPatternName(), is("Bridge"));

        // Check matching classes
        final Node a = TestHelper.createClazz("A");
        final Node b = TestHelper.createClazz("B");
        final Node c = TestHelper.createClazz("C");
        final Node d = TestHelper.createClazz("D");
        final Node e = TestHelper.createClazz("E");
        assertThat(mc0.get(a).getName(), is("Client"));
        assertThat(mc0.get(b).getName(), is("Abstraction"));
        assertThat(mc0.get(c).getName(), is("Implementor"));
        assertThat(mc0.get(d).getName(), is("RefinedAbstraction"));
        assertThat(mc0.get(e).getName(), is("ConcreteImplementor"));

        // Check superfluous edges
        assertThat(se0.size(), is(3));
        assertTrue(se0.contains(new Edge(d, e, EdgeType.DEPENDENCY)));
        assertTrue(se0.contains(new Edge(c, b, EdgeType.ASSOCIATION_DIRECTED)));
        assertTrue(se0.contains(new Edge(a, c, EdgeType.ASSOCIATION_DIRECTED)));

        // Check missing edges
        final Node implementor = TestHelper.createInterface("Implementor");
        final Node abstraction = TestHelper.createAbstractClazz("Abstraction");
        assertThat(me0.size(), is(1));
        assertTrue(me0.contains(new Edge(implementor, abstraction, EdgeType.AGGREGATE)));
    }

    /**
     * Tests if the bridge pattern is detected with 0 missing edges allowed.
     */
    @Test
    public void testMatchBridgeNoMissingEdges() {
        final DesignPattern pattern = TestHelper.createBridgePattern();
        final Solutions matchResult = matcher.match(pattern, system, 0);
        // The pattern is not expected to be detected
        assertThat(matchResult.getSolutions().size(), is(0));
    }

    /**
     * Tests if the builder pattern is detected with 1 missing edge allowed.
     */
    @Test
    public void testMatchBuilder() {
        final DesignPattern pattern = TestHelper.createBuilderPattern();
        final Solutions matchResult = matcher.match(pattern, system, 1);
        final List<Solution> solutions = matchResult.getSolutions();

        // Check number of times the pattern is detected
        assertThat(solutions.size(), is(1));

        final Solution s0 = solutions.get(0);
        final MatchedNodes mc0 = s0.getMatchedNodes();
        final Set<Edge> se0 = s0.getSuperfluousEdges();
        final Set<Edge> me0 = s0.getMissingEdges();

        // Check the name
        assertThat(s0.getDesignPatternName(), is("Builder"));

        // Check the matching classes
        assertThat(mc0.get(TestHelper.createClazz("B")).getName(), is("Builder"));
        assertThat(mc0.get(TestHelper.createClazz("D")).getName(), is("ConcreteBuilder"));
        assertThat(mc0.get(TestHelper.createClazz("E")).getName(), is("Product"));

        // Check superfluous edges
        assertThat(se0.size(), is(0));

        // Check missing edges
        final Node builder = TestHelper.createInterface("Builder");
        final Node director = TestHelper.createClazz("Director");
        assertThat(me0.size(), is(1));
        assertTrue(me0.contains(new Edge(builder, director, EdgeType.AGGREGATE)));
    }

    /**
     * Tests if the builder pattern is detected with 0 missing edges allowed.
     */
    @Test
    public void testMatchBuilderNoMissingEdges() {
        final DesignPattern pattern = TestHelper.createBuilderPattern();
        final Solutions matchResult = matcher.match(pattern, system, 0);
        // The pattern is not expected to be detected
        assertThat(matchResult.getSolutions().size(), is(0));
    }

    /**
     * Tests if the chain of responsibility pattern is detected with 1 missing edge allowed.
     */
    @Test
    public void testMatchChainOfResponsibility() {
        final DesignPattern pattern = TestHelper.createChainOfResponsibilityPattern();
        final Solutions matchResult = matcher.match(pattern, system, 1);
        final List<Solution> solutions = matchResult.getSolutions();

        final Node handler = TestHelper.createAbstractClazz("Handler");

        // Check number of detected patterns
        assertThat(solutions.size(), is(3));

        final Solution s0 = solutions.get(0);
        final MatchedNodes mc0 = s0.getMatchedNodes();
        final Set<Edge> se0 = s0.getSuperfluousEdges();
        final Set<Edge> me0 = s0.getMissingEdges();

        // Check name
        assertThat(s0.getDesignPatternName(), is("ChainOfResponsibility"));

        // Check matches classes
        assertThat(mc0.get(TestHelper.createClazz("A")).getName(), is("Client"));
        assertThat(mc0.get(TestHelper.createClazz("C")).getName(), is("Handler"));
        assertThat(mc0.get(TestHelper.createClazz("E")).getName(), is("ConcreteHandler"));

        // Check superfluous edges
        assertThat(se0.size(), is(0));

        // Check missing edges
        assertThat(me0.size(), is(1));
        assertTrue(me0.contains(new Edge(handler, handler, EdgeType.ASSOCIATION_DIRECTED)));

        final Solution s1 = solutions.get(1);
        final MatchedNodes mc1 = s1.getMatchedNodes();
        final Set<Edge> se1 = s1.getSuperfluousEdges();
        final Set<Edge> me1 = s1.getMissingEdges();

        // Check name
        assertThat(s1.getDesignPatternName(), is("ChainOfResponsibility"));

        // Check matching classes
        assertThat(mc1.get(TestHelper.createClazz("B")).getName(), is("Handler"));
        assertThat(mc1.get(TestHelper.createClazz("C")).getName(), is("Client"));
        assertThat(mc1.get(TestHelper.createClazz("D")).getName(), is("ConcreteHandler"));

        // Check superfluous edges
        assertThat(se1.size(), is(0));

        // Check missing edges
        assertThat(me1.size(), is(1));
        assertTrue(me1.contains(new Edge(handler, handler, EdgeType.ASSOCIATION_DIRECTED)));

        final Solution s2 = solutions.get(2);
        final MatchedNodes mc2 = s2.getMatchedNodes();
        final Set<Edge> se2 = s2.getSuperfluousEdges();
        final Set<Edge> me2 = s2.getMissingEdges();

        // Check name
        assertThat(s2.getDesignPatternName(), is("ChainOfResponsibility"));

        // Check matching classes
        assertThat(mc2.get(TestHelper.createClazz("A")).getName(), is("Client"));
        assertThat(mc2.get(TestHelper.createClazz("B")).getName(), is("Handler"));
        assertThat(mc2.get(TestHelper.createClazz("D")).getName(), is("ConcreteHandler"));

        // Check superfluous edges
        assertThat(se2.size(), is(0));

        // Check missing edges
        assertThat(me2.size(), is(1));
        assertTrue(me2.contains(new Edge(handler, handler, EdgeType.ASSOCIATION_DIRECTED)));

    }

    /**
     * Tests if the builder pattern is detected with 0 missing edges allowed.
     */
    @Test
    public void testMatchChainOfResponsibilityNoMissingEdges() {
        final DesignPattern pattern = TestHelper.createChainOfResponsibilityPattern();
        final Solutions matchResult = matcher.match(pattern, system, 0);
        // The pattern is not expected to be detected
        assertThat(matchResult.getSolutions().size(), is(0));
    }

    /**
     * Tests if the factory method pattern is detected with 1 missing edge allowed.
     */
    @Test
    public void testMatchFactoryMethod() {
        final DesignPattern pattern = TestHelper.createFactoryMethodPattern();
        final Solutions matchResult = matcher.match(pattern, system, 1);
        final List<Solution> solutions = matchResult.getSolutions();

        // Check number of matching patterns
        assertThat(solutions.size(), is(1));

        final Solution s0 = solutions.get(0);
        final MatchedNodes mc0 = s0.getMatchedNodes();
        final Set<Edge> se0 = s0.getSuperfluousEdges();
        final Set<Edge> me0 = s0.getMissingEdges();

        // Check pattern name
        assertThat(s0.getDesignPatternName(), is("Factory Method"));

        // Check matched nodes
        assertThat(mc0.get(TestHelper.createClazz("B")).getName(), is("Creator"));
        assertThat(mc0.get(TestHelper.createClazz("C")).getName(), is("Product"));
        assertThat(mc0.get(TestHelper.createClazz("D")).getName(), is("ConcreteCreator"));
        assertThat(mc0.get(TestHelper.createClazz("E")).getName(), is("ConcreteProduct"));

        // Check superfluous edges
        assertThat(se0.size(), is(1));
        assertTrue(se0.contains(
                new Edge(
                        TestHelper.createClazz("C"),
                        TestHelper.createClazz("B"),
                        EdgeType.ASSOCIATION_DIRECTED)));

        // Check missing edges
        assertThat(me0.size(), is(0));
    }

    /**
     * Tests if the factory method pattern is detected with 0 missing edges allowed.
     */
    @Test
    public void testMatchFactoryMethodNoMissingEdges() {
        final DesignPattern pattern = TestHelper.createFactoryMethodPattern();
        final Solutions matchResult = matcher.match(pattern, system, 0);
        final List<Solution> solutions = matchResult.getSolutions();

        //Check number of times the pattern was found
        assertThat(solutions.size(), is(1));

        final Solution s0 = solutions.get(0);
        final MatchedNodes mc0 = s0.getMatchedNodes();
        final Set<Edge> se0 = s0.getSuperfluousEdges();
        final Set<Edge> me0 = s0.getMissingEdges();

        // Check the name
        assertThat(s0.getDesignPatternName(), is("Factory Method"));

        // Ccheck matched nodes
        assertThat(mc0.get(TestHelper.createClazz("B")).getName(), is("Creator"));
        assertThat(mc0.get(TestHelper.createClazz("C")).getName(), is("Product"));
        assertThat(mc0.get(TestHelper.createClazz("D")).getName(), is("ConcreteCreator"));
        assertThat(mc0.get(TestHelper.createClazz("E")).getName(), is("ConcreteProduct"));

        // Check spuerfluous edges
        assertThat(se0.size(), is(1));
        assertTrue(se0.contains(
                new Edge(
                        TestHelper.createClazz("C"),
                        TestHelper.createClazz("B"),
                        EdgeType.ASSOCIATION_DIRECTED)));

        // Check missing edges
        assertThat(me0.size(), is(0));
    }

    /**
     * Tests if the iterator pattern is detected with 1 missing edge allowed.
     */
    @Test
    public void testMatchIterator() {
        final DesignPattern pattern = TestHelper.createIteratorPattern();
        final Solutions matchResult = matcher.match(pattern, system, 1);
        final List<Solution> solutions = matchResult.getSolutions();

        // Check number of time the pattern was detected
        assertThat(solutions.size(), is(1));

        final Solution s0 = solutions.get(0);
        final MatchedNodes mc0 = s0.getMatchedNodes();
        final Set<Edge> se0 = s0.getSuperfluousEdges();
        final Set<Edge> me0 = s0.getMissingEdges();

        // Check the name
        assertThat(s0.getDesignPatternName(), is("Iterator"));

        // Check matched nodes
        final Node a = TestHelper.createClazz("A");
        final Node b = TestHelper.createClazz("B");
        final Node c = TestHelper.createClazz("C");
        final Node d = TestHelper.createClazz("D");
        final Node e = TestHelper.createClazz("E");
        assertThat(mc0.get(a).getName(), is("Client"));
        assertThat(mc0.get(b).getName(), is("Aggregate"));
        assertThat(mc0.get(c).getName(), is("Iterator"));
        assertThat(mc0.get(d).getName(), is("ConcreteAggregate"));
        assertThat(mc0.get(e).getName(), is("ConcreteIterator"));

        // Check superfuous edges
        assertThat(se0.size(), is(1));
        assertTrue(se0.contains(new Edge(c, b, EdgeType.ASSOCIATION_DIRECTED)));


        // Check missing edges
        final Node concreteIterator = TestHelper.createClazz("ConcreteIterator");
        final Node concreteAggregate = TestHelper.createClazz("ConcreteAggregate");
        assertThat(me0.size(), is(1));
        assertTrue(me0.contains(new Edge(concreteIterator, concreteAggregate, EdgeType.ASSOCIATION_DIRECTED)));
    }

    /**
     * Tests if the iterator pattern is detected with 0 missing edges allowed.
     */
    @Test
    public void testMatchIteratorNoMissingEdges() {
        final DesignPattern pattern = TestHelper.createIteratorPattern();
        final Solutions matchResult = matcher.match(pattern, system, 0);
        // The pattern is not expected to be detected
        assertThat(matchResult.getSolutions().size(), is(0));
    }

    /**
     * Tests if the memento pattern is detected with 1 missing edge allowed.
     */
    @Test
    public void testMatchMemento() {
        final DesignPattern pattern = TestHelper.createMementoPattern();
        final Solutions matchResult = matcher.match(pattern, system, 1);
        final List<Solution> solutions = matchResult.getSolutions();

        final Node memento = TestHelper.createClazz("Memento");
        final Node caretaker = TestHelper.createClazz("Caretaker");

        // Check number of times the pattern was detected
        assertThat(solutions.size(), is(1));

        final Solution s0 = solutions.get(0);
        final MatchedNodes mc0 = s0.getMatchedNodes();
        final Set<Edge> se0 = s0.getSuperfluousEdges();
        final Set<Edge> me0 = s0.getMissingEdges();

        // Check the name
        assertThat(s0.getDesignPatternName(), is("Memento"));

        // Check the matching classes
        assertThat(mc0.get(TestHelper.createClazz("D")).getName(), is("Originator"));
        assertThat(mc0.get(TestHelper.createClazz("E")).getName(), is("Memento"));

        // Check the superfluous edges
        assertThat(se0.size(), is(0));

        // Check the missing edges
        assertThat(me0.size(), is(1));
        assertTrue(me0.contains(new Edge(memento, caretaker, EdgeType.AGGREGATE)));
    }

    /**
     * Tests if the memento pattern is detected with 0 missing edges allowed.
     */
    @Test
    public void testMatchMementoNoMissingEdges() {
        final DesignPattern pattern = TestHelper.createMementoPattern();
        final Solutions matchResult = matcher.match(pattern, system, 0);
        // The pattern is not expected to be detected
        assertThat(matchResult.getSolutions().size(), is(0));
    }

    /**
     * Tests if the observer pattern is detected with 1 missing edge allowed.
     */
    @Test
    public void testMatchObserver() {
        final DesignPattern pattern = TestHelper.createObserverPattern();
        final Solutions matchResult = matcher.match(pattern, system, 1);
        final List<Solution> solutions = matchResult.getSolutions();

        final Node concreteObserver = TestHelper.createClazz("ConcreteObserver");
        final Node concreteSubject = TestHelper.createClazz("ConcreteSubject");

        // Check number of times the pattern was detected
        assertThat(solutions.size(), is(1));

        final Solution s0 = solutions.get(0);
        final MatchedNodes mc0 = s0.getMatchedNodes();
        final Set<Edge> se0 = s0.getSuperfluousEdges();
        final Set<Edge> me0 = s0.getMissingEdges();

        // Check the name
        assertThat(s0.getDesignPatternName(), is("Observer"));

        // Check the matching classes
        assertThat(mc0.get(TestHelper.createClazz("B")).getName(), is("Observer"));
        assertThat(mc0.get(TestHelper.createClazz("C")).getName(), is("Subject"));
        assertThat(mc0.get(TestHelper.createClazz("D")).getName(), is("ConcreteObserver"));
        assertThat(mc0.get(TestHelper.createClazz("E")).getName(), is("ConcreteSubject"));

        // Check the superfluous edges
        assertThat(se0.size(), is(1));
        assertTrue(se0.contains(
                new Edge(
                        TestHelper.createClazz("D"),
                        TestHelper.createClazz("E"),
                        EdgeType.DEPENDENCY)));

        // Check the missing edges
        assertThat(me0.size(), is(1));
        assertTrue(me0.contains(new Edge(concreteObserver, concreteSubject, EdgeType.ASSOCIATION_DIRECTED)));
    }

    /**
     * Tests if the observer pattern is detected with 0 missing edges allowed.
     */
    @Test
    public void testMatchObserverNoMissingEdges() {
        final DesignPattern pattern = TestHelper.createObserverPattern();
        final Solutions matchResult = matcher.match(pattern, system, 0);
        // The pattern is not expected to be detected
        assertThat(matchResult.getSolutions().size(), is(0));
    }

    /**
     * Tests if the state or strategy pattern is detected with 1 missing edge allowed.
     */
    @Test
    public void testMatchStateStrategy() {
        final DesignPattern pattern = TestHelper.createStateStrategyPattern();
        final Solutions matchResult = matcher.match(pattern, system, 1);
        final List<Solution> solutions = matchResult.getSolutions();

        final Node strategy = TestHelper.createInterface("Strategy");
        final Node context = TestHelper.createClazz("Context");

        // Check the number of times the pattern was detected
        assertThat(solutions.size(), is(2));

        final Solution s0 = solutions.get(0);
        final MatchedNodes mc0 = s0.getMatchedNodes();
        final Set<Edge> se0 = s0.getSuperfluousEdges();
        final Set<Edge> me0 = s0.getMissingEdges();

        // Check the name
        assertThat(s0.getDesignPatternName(), is("State - Strategy"));

        // Check the matched nodes
        assertThat(mc0.get(TestHelper.createClazz("C")).getName(), is("Strategy"));
        assertThat(mc0.get(TestHelper.createClazz("E")).getName(), is("ConcreteStrategy"));

        // Check the superfluous edges
        assertThat(se0.size(), is(0));

        // Check the missing edges
        assertThat(me0.size(), is(1));
        assertTrue(me0.contains(new Edge(strategy, context, EdgeType.AGGREGATE)));

        final Solution s1 = solutions.get(1);
        final MatchedNodes mc1 = s1.getMatchedNodes();
        final Set<Edge> se1 = s1.getSuperfluousEdges();
        final Set<Edge> me1 = s1.getMissingEdges();

        // Check the name
        assertThat(s1.getDesignPatternName(), is("State - Strategy"));

        // Check the matching classes
        assertThat(mc1.get(TestHelper.createClazz("B")).getName(), is("Strategy"));
        assertThat(mc1.get(TestHelper.createClazz("D")).getName(), is("ConcreteStrategy"));

        // Check the superfluous edges
        assertThat(se1.size(), is(0));

        // Check the missing edges
        assertThat(me1.size(), is(1));
        assertTrue(me1.contains(new Edge(strategy, context, EdgeType.AGGREGATE)));
    }

    /**
     * Tests if the state/strategy pattern is detected with 0 missing edges allowed.
     */
    @Test
    public void testMatchStateStrategyNoMissingEdges() {
        final DesignPattern pattern = TestHelper.createStateStrategyPattern();
        final Solutions matchResult = matcher.match(pattern, system, 0);
        // The pattern is not expected to be detected
        assertThat(matchResult.getSolutions().size(), is(0));
    }

    private SystemUnderConsideration createBaBrahemSystemUnderConsideration() {
        SystemUnderConsideration result = new SystemUnderConsideration();
        result.add(new Edge(TestHelper.createClazz("D"), TestHelper.createClazz("E"), EdgeType.DEPENDENCY));
        result.add(new Edge(TestHelper.createClazz("E"), TestHelper.createClazz("C"), EdgeType.INHERITANCE));
        result.add(new Edge(TestHelper.createClazz("D"), TestHelper.createClazz("B"), EdgeType.INHERITANCE));
        result.add(new Edge(TestHelper.createClazz("C"), TestHelper.createClazz("B"), EdgeType.ASSOCIATION_DIRECTED));
        result.add(new Edge(TestHelper.createClazz("A"), TestHelper.createClazz("B"), EdgeType.ASSOCIATION_DIRECTED));
        result.add(new Edge(TestHelper.createClazz("A"), TestHelper.createClazz("C"), EdgeType.ASSOCIATION_DIRECTED));
        return result;
    }

}
