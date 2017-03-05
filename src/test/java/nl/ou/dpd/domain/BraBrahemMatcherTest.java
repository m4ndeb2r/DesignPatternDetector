package nl.ou.dpd.domain;

import nl.ou.dpd.utils.TestHelper;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertTrue;

/**
 * Tests the {@link Matcher} class.
 *
 * @author Martin de Boer
 */
public class BraBrahemMatcherTest {

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
        final MatchedClasses mc0 = s0.getMatchedClasses();
        final Set<Edge> se0 = s0.getSuperfluousEdges();
        final Set<Edge> me0 = s0.getMissingEdges();

        // Check name
        assertThat(s0.getDesignPatternName(), is("Adapter"));

        // Check matching classes
        assertThat(mc0.get(new Clazz("B")).getName(), is("Target"));
        assertThat(mc0.get(new Clazz("C")).getName(), is("Client"));
        assertThat(mc0.get(new Clazz("D")).getName(), is("Adapter"));

        // Check superfluous edges
        assertThat(se0.size(), is(0));

        // Check missing edges
        assertThat(me0.size(), is(1));
        assertTrue(me0.contains(
                new Edge(
                        new Clazz("Adapter"),
                        new Clazz("Adaptee"),
                        EdgeType.ASSOCIATION_DIRECTED)));

        final Solution s1 = solutions.get(1);
        final MatchedClasses mc1 = s1.getMatchedClasses();
        final Set<Edge> se1 = s1.getSuperfluousEdges();
        final Set<Edge> me1 = s1.getMissingEdges();

        // Check name
        assertThat(s1.getDesignPatternName(), is("Adapter"));

        // Check matching classes
        assertThat(mc1.get(new Clazz("A")).getName(), is("Client"));
        assertThat(mc1.get(new Clazz("B")).getName(), is("Target"));
        assertThat(mc1.get(new Clazz("D")).getName(), is("Adapter"));

        // Check superfluous edges
        assertThat(se1.size(), is(0));

        // Check missing edges
        assertThat(me1.size(), is(1));
        assertTrue(me1.contains(
                new Edge(
                        new Clazz("Adapter"),
                        new Clazz("Adaptee"),
                        EdgeType.ASSOCIATION_DIRECTED)));

        final Solution s2 = solutions.get(2);
        final MatchedClasses mc2 = s2.getMatchedClasses();
        final Set<Edge> se2 = s2.getSuperfluousEdges();
        final Set<Edge> me2 = s2.getMissingEdges();

        // Check name
        assertThat(s2.getDesignPatternName(), is("Adapter"));

        // Check matching classes
        assertThat(mc2.get(new Clazz("A")).getName(), is("Client"));
        assertThat(mc2.get(new Clazz("C")).getName(), is("Target"));
        assertThat(mc2.get(new Clazz("E")).getName(), is("Adapter"));

        // Check superfluous edges
        assertThat(se2.size(), is(0));

        // Check missing edges
        assertThat(me2.size(), is(1));
        assertTrue(me2.contains(
                new Edge(
                        new Clazz("Adapter"),
                        new Clazz("Adaptee"),
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
        final MatchedClasses mc0 = s0.getMatchedClasses();
        final Set<Edge> se0 = s0.getSuperfluousEdges();
        final Set<Edge> me0 = s0.getMissingEdges();

        // Check the name
        assertThat(s0.getDesignPatternName(), is("Bridge"));

        // Check matching classes
        assertThat(mc0.get(new Clazz("A")).getName(), is("Client"));
        assertThat(mc0.get(new Clazz("B")).getName(), is("Abstraction"));
        assertThat(mc0.get(new Clazz("C")).getName(), is("Implementor"));
        assertThat(mc0.get(new Clazz("D")).getName(), is("RefinedAbstraction"));
        assertThat(mc0.get(new Clazz("E")).getName(), is("ConcreteImplementor"));

        // Check superfluous edges
        assertThat(se0.size(), is(3));
        assertTrue(se0.contains(new Edge(new Clazz("D"), new Clazz("E"), EdgeType.DEPENDENCY)));
        assertTrue(se0.contains(new Edge(new Clazz("C"), new Clazz("B"), EdgeType.ASSOCIATION_DIRECTED)));
        assertTrue(se0.contains(new Edge(new Clazz("A"), new Clazz("C"), EdgeType.ASSOCIATION_DIRECTED)));

        // Check missing edges
        assertThat(me0.size(), is(1));
        assertTrue(me0.contains(
                new Edge(
                        new Clazz("Implementor"),
                        new Clazz("Abstraction"),
                        EdgeType.AGGREGATE)));
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
        final MatchedClasses mc0 = s0.getMatchedClasses();
        final Set<Edge> se0 = s0.getSuperfluousEdges();
        final Set<Edge> me0 = s0.getMissingEdges();

        // Check the name
        assertThat(s0.getDesignPatternName(), is("Builder"));

        // Check the matching classes
        assertThat(mc0.get(new Clazz("B")).getName(), is("Builder"));
        assertThat(mc0.get(new Clazz("D")).getName(), is("ConcreteBuilder"));
        assertThat(mc0.get(new Clazz("E")).getName(), is("Product"));

        // Check superfluous edges
        assertThat(se0.size(), is(0));

        // Check missing edges
        assertThat(me0.size(), is(1));
        assertTrue(me0.contains(
                new Edge(
                        new Clazz("Builder"),
                        new Clazz("Director"),
                        EdgeType.AGGREGATE)));
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

        // Check number of detected patterns
        assertThat(solutions.size(), is(3));

        final Solution s0 = solutions.get(0);
        final MatchedClasses mc0 = s0.getMatchedClasses();
        final Set<Edge> se0 = s0.getSuperfluousEdges();
        final Set<Edge> me0 = s0.getMissingEdges();

        // Check name
        assertThat(s0.getDesignPatternName(), is("ChainOfResponsibility"));

        // Check matches classes
        assertThat(mc0.get(new Clazz("A")).getName(), is("Client"));
        assertThat(mc0.get(new Clazz("C")).getName(), is("Handler"));
        assertThat(mc0.get(new Clazz("E")).getName(), is("ConcreteHandler"));

        // Check superfluous edges
        assertThat(se0.size(), is(0));

        // Check missing edges
        assertThat(me0.size(), is(1));
        assertTrue(me0.contains(
                new Edge(
                        new Clazz("Handler"),
                        new Clazz("Handler"),
                        EdgeType.ASSOCIATION_DIRECTED)));

        final Solution s1 = solutions.get(1);
        final MatchedClasses mc1 = s1.getMatchedClasses();
        final Set<Edge> se1 = s1.getSuperfluousEdges();
        final Set<Edge> me1 = s1.getMissingEdges();

        // Check name
        assertThat(s1.getDesignPatternName(), is("ChainOfResponsibility"));

        // Check matching classes
        assertThat(mc1.get(new Clazz("B")).getName(), is("Handler"));
        assertThat(mc1.get(new Clazz("C")).getName(), is("Client"));
        assertThat(mc1.get(new Clazz("D")).getName(), is("ConcreteHandler"));

        // Check superfluous edges
        assertThat(se1.size(), is(0));

        // Check missing edges
        assertThat(me1.size(), is(1));
        assertTrue(me1.contains(
                new Edge(
                        new Clazz("Handler"),
                        new Clazz("Handler"),
                        EdgeType.ASSOCIATION_DIRECTED)));

        final Solution s2 = solutions.get(2);
        final MatchedClasses mc2 = s2.getMatchedClasses();
        final Set<Edge> se2 = s2.getSuperfluousEdges();
        final Set<Edge> me2 = s2.getMissingEdges();

        // Check name
        assertThat(s2.getDesignPatternName(), is("ChainOfResponsibility"));

        // Check matching classes
        assertThat(mc2.get(new Clazz("A")).getName(), is("Client"));
        assertThat(mc2.get(new Clazz("B")).getName(), is("Handler"));
        assertThat(mc2.get(new Clazz("D")).getName(), is("ConcreteHandler"));

        // Check superfluous edges
        assertThat(se2.size(), is(0));

        // Check missing edges
        assertThat(me2.size(), is(1));
        assertTrue(me2.contains(
                new Edge(
                        new Clazz("Handler"),
                        new Clazz("Handler"),
                        EdgeType.ASSOCIATION_DIRECTED)));

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
        final MatchedClasses mc0 = s0.getMatchedClasses();
        final Set<Edge> se0 = s0.getSuperfluousEdges();
        final Set<Edge> me0 = s0.getMissingEdges();

        // Check pattern name
        assertThat(s0.getDesignPatternName(), is("Factory Method"));

        // Check matched classes
        assertThat(mc0.get(new Clazz("B")).getName(), is("Creator"));
        assertThat(mc0.get(new Clazz("C")).getName(), is("Product"));
        assertThat(mc0.get(new Clazz("D")).getName(), is("ConcreteCreator"));
        assertThat(mc0.get(new Clazz("E")).getName(), is("ConcreteProduct"));

        // Check superfluous edges
        assertThat(se0.size(), is(1));
        assertTrue(se0.contains(
                new Edge(
                        new Clazz("C"),
                        new Clazz("B"),
                        EdgeType.ASSOCIATION_DIRECTED)));

        // Check missing edges
        // TODO: BUG in the application? We are finding a missing edge: dependency. But it is actually there, so not missing at all....
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
        final MatchedClasses mc0 = s0.getMatchedClasses();
        final Set<Edge> se0 = s0.getSuperfluousEdges();
        final Set<Edge> me0 = s0.getMissingEdges();

        // Check the name
        assertThat(s0.getDesignPatternName(), is("Factory Method"));

        // Ccheck matched classes
        assertThat(mc0.get(new Clazz("B")).getName(), is("Creator"));
        assertThat(mc0.get(new Clazz("C")).getName(), is("Product"));
        assertThat(mc0.get(new Clazz("D")).getName(), is("ConcreteCreator"));
        assertThat(mc0.get(new Clazz("E")).getName(), is("ConcreteProduct"));

        // Check spuerfluous edges
        assertThat(se0.size(), is(1));
        assertTrue(se0.contains(
                new Edge(
                        new Clazz("C"),
                        new Clazz("B"),
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
        final MatchedClasses mc0 = s0.getMatchedClasses();
        final Set<Edge> se0 = s0.getSuperfluousEdges();
        final Set<Edge> me0 = s0.getMissingEdges();

        // Check the name
        assertThat(s0.getDesignPatternName(), is("Iterator"));

        // Check matched classes
        assertThat(mc0.get(new Clazz("A")).getName(), is("Client"));
        assertThat(mc0.get(new Clazz("B")).getName(), is("Aggregate"));
        assertThat(mc0.get(new Clazz("C")).getName(), is("Iterator"));
        assertThat(mc0.get(new Clazz("D")).getName(), is("ConcreteAggregate"));
        assertThat(mc0.get(new Clazz("E")).getName(), is("ConcreteIterator"));

        // Check superfuous edges
        assertThat(se0.size(), is(1));
        assertTrue(se0.contains(
                new Edge(
                        new Clazz("C"),
                        new Clazz("B"),
                        EdgeType.ASSOCIATION_DIRECTED)));


        // Check missing edges
        assertThat(me0.size(), is(1));
        assertTrue(me0.contains(
                new Edge(
                        new Clazz("ConcreteIterator"),
                        new Clazz("ConcreteAggregate"),
                        EdgeType.ASSOCIATION_DIRECTED)));
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

        // Check number of times the pattern was detected
        assertThat(solutions.size(), is(1));

        final Solution s0 = solutions.get(0);
        final MatchedClasses mc0 = s0.getMatchedClasses();
        final Set<Edge> se0 = s0.getSuperfluousEdges();
        final Set<Edge> me0 = s0.getMissingEdges();

        // Check the name
        assertThat(s0.getDesignPatternName(), is("Memento"));

        // Check the matching classes
        assertThat(mc0.get(new Clazz("D")).getName(), is("Originator"));
        assertThat(mc0.get(new Clazz("E")).getName(), is("Memento"));

        // Check the superfluous edges
        assertThat(se0.size(), is(0));

        // Check the missing edges
        assertThat(me0.size(), is(1));
        assertTrue(me0.contains(
                new Edge(
                        new Clazz("Memento"),
                        new Clazz("Caretaker"),
                        EdgeType.AGGREGATE)));
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
     * Tests if the state or strategy pattern is detected with 1 missing edge allowed.
     */
    @Test
    public void testMatchStateStrategy() {
        final DesignPattern pattern = TestHelper.createStateStrategyPattern();
        final Solutions matchResult = matcher.match(pattern, system, 1);
        final List<Solution> solutions = matchResult.getSolutions();

        // Check the number of times the pattern was detected
        assertThat(solutions.size(), is(2));

        final Solution s0 = solutions.get(0);
        final MatchedClasses mc0 = s0.getMatchedClasses();
        final Set<Edge> se0 = s0.getSuperfluousEdges();
        final Set<Edge> me0 = s0.getMissingEdges();

        // Check the name
        assertThat(s0.getDesignPatternName(), is("State - Strategy"));

        // Check the matched classes
        assertThat(mc0.get(new Clazz("C")).getName(), is("Strategy"));
        assertThat(mc0.get(new Clazz("E")).getName(), is("ConcreteStrategy"));

        // Check the superfluous edges
        assertThat(se0.size(), is(0));

        // Check the missing edges
        assertThat(me0.size(), is(1));
        assertTrue(me0.contains(
                new Edge(
                        new Clazz("Strategy"),
                        new Clazz("Context"),
                        EdgeType.AGGREGATE)));

        final Solution s1 = solutions.get(1);
        final MatchedClasses mc1 = s1.getMatchedClasses();
        final Set<Edge> se1 = s1.getSuperfluousEdges();
        final Set<Edge> me1 = s1.getMissingEdges();

        // Check the name
        assertThat(s1.getDesignPatternName(), is("State - Strategy"));

        // Check the matching classes
        assertThat(mc1.get(new Clazz("B")).getName(), is("Strategy"));
        assertThat(mc1.get(new Clazz("D")).getName(), is("ConcreteStrategy"));

        // Check the superfluous edges
        assertThat(se1.size(), is(0));

        // Check the missing edges
        assertThat(me1.size(), is(1));
        assertTrue(me1.contains(
                new Edge(
                        new Clazz("Strategy"),
                        new Clazz("Context"),
                        EdgeType.AGGREGATE)));
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
        result.add(new Edge(new Clazz("D"), new Clazz("E"), EdgeType.DEPENDENCY));
        result.add(new Edge(new Clazz("E"), new Clazz("C"), EdgeType.INHERITANCE));
        result.add(new Edge(new Clazz("D"), new Clazz("B"), EdgeType.INHERITANCE));
        result.add(new Edge(new Clazz("C"), new Clazz("B"), EdgeType.ASSOCIATION_DIRECTED));
        result.add(new Edge(new Clazz("A"), new Clazz("B"), EdgeType.ASSOCIATION_DIRECTED));
        result.add(new Edge(new Clazz("A"), new Clazz("C"), EdgeType.ASSOCIATION_DIRECTED));
        return result;
    }

}
