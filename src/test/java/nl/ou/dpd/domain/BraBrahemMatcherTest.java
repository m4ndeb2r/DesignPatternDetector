package nl.ou.dpd.domain;

import nl.ou.dpd.utils.TestHelper;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

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
     * Tests if the adapter pattern is detected.
     */
    @Test
    public void testMatchAdapter() {
        final DesignPattern pattern = TestHelper.createAdapterPattern();
        final Solutions matchResult = matcher.match(pattern, system, 1);
        final List<Solution> solutions = matchResult.getSolutions();

        assertThat(solutions.size(), is(3));

        assertThat(solutions.get(0).getDesignPatternName(), is("Adapter"));
        assertThat(solutions.get(0).getSuperfluousEdges().size(), is(0));
        assertThat(solutions.get(0).getMatchedClasses().get(new Clazz("B")).getName(), is("Target"));
        assertThat(solutions.get(0).getMatchedClasses().get(new Clazz("C")).getName(), is("Client"));
        assertThat(solutions.get(0).getMatchedClasses().get(new Clazz("D")).getName(), is("Adapter"));

        assertThat(solutions.get(1).getDesignPatternName(), is("Adapter"));
        assertThat(solutions.get(1).getSuperfluousEdges().size(), is(0));
        assertThat(solutions.get(1).getMatchedClasses().get(new Clazz("A")).getName(), is("Client"));
        assertThat(solutions.get(1).getMatchedClasses().get(new Clazz("B")).getName(), is("Target"));
        assertThat(solutions.get(1).getMatchedClasses().get(new Clazz("D")).getName(), is("Adapter"));

        assertThat(solutions.get(2).getDesignPatternName(), is("Adapter"));
        assertThat(solutions.get(2).getSuperfluousEdges().size(), is(0));
        assertThat(solutions.get(2).getMatchedClasses().get(new Clazz("A")).getName(), is("Client"));
        assertThat(solutions.get(2).getMatchedClasses().get(new Clazz("C")).getName(), is("Target"));
        assertThat(solutions.get(2).getMatchedClasses().get(new Clazz("E")).getName(), is("Adapter"));
    }

    /**
     * Tests if the bridge pattern is detected.
     */
    @Test
    public void testMatchBridge() {
        final DesignPattern pattern = TestHelper.createBridgePattern();
        final Solutions matchResult = matcher.match(pattern, system, 1);
        final List<Solution> solutions = matchResult.getSolutions();

        // TODO: is something the matter with the bridge pattern? Did not find it? There is a different definition in the file compared to the TestHelper?
        //assertThat(solutions.size(), is(1));
    }

    /**
     * Tests if the builder pattern is detected.
     */
    @Test
    public void testMatchBuilder() {
        final DesignPattern pattern = TestHelper.createBuilderPattern();
        final Solutions matchResult = matcher.match(pattern, system, 1);
        final List<Solution> solutions = matchResult.getSolutions();

        assertThat(solutions.size(), is(1));

        assertThat(solutions.get(0).getDesignPatternName(), is("Builder"));
        assertThat(solutions.get(0).getSuperfluousEdges().size(), is(0));
        assertThat(solutions.get(0).getMatchedClasses().get(new Clazz("B")).getName(), is("Builder"));
        assertThat(solutions.get(0).getMatchedClasses().get(new Clazz("D")).getName(), is("ConcreteBuilder"));
        assertThat(solutions.get(0).getMatchedClasses().get(new Clazz("E")).getName(), is("Product"));
    }

    /**
     * Tests if the chain of responsibility pattern is detected.
     */
    @Test
    public void testMatchChainOfResponsibility() {
        final DesignPattern pattern = TestHelper.createChainOfResponsibilityPattern();
        final Solutions matchResult = matcher.match(pattern, system, 1);
        final List<Solution> solutions = matchResult.getSolutions();

        assertThat(solutions.size(), is(3));

        assertThat(solutions.get(0).getDesignPatternName(), is("ChainOfResponsibility"));
        assertThat(solutions.get(0).getMatchedClasses().get(new Clazz("A")).getName(), is("Client"));
        assertThat(solutions.get(0).getMatchedClasses().get(new Clazz("C")).getName(), is("Handler"));
        assertThat(solutions.get(0).getMatchedClasses().get(new Clazz("E")).getName(), is("ConcreteHandler"));

        assertThat(solutions.get(1).getDesignPatternName(), is("ChainOfResponsibility"));
        assertThat(solutions.get(1).getMatchedClasses().get(new Clazz("B")).getName(), is("Handler"));
        assertThat(solutions.get(1).getMatchedClasses().get(new Clazz("C")).getName(), is("Client"));
        assertThat(solutions.get(1).getMatchedClasses().get(new Clazz("D")).getName(), is("ConcreteHandler"));

        assertThat(solutions.get(2).getDesignPatternName(), is("ChainOfResponsibility"));
        assertThat(solutions.get(2).getMatchedClasses().get(new Clazz("A")).getName(), is("Client"));
        assertThat(solutions.get(2).getMatchedClasses().get(new Clazz("B")).getName(), is("Handler"));
        assertThat(solutions.get(2).getMatchedClasses().get(new Clazz("D")).getName(), is("ConcreteHandler"));
    }

    /**
     * Tests if the factory method pattern is detected.
     */
    @Test
    public void testMatchFactoryMethod() {
        final DesignPattern pattern = TestHelper.createFactoryMethodPattern();
        final Solutions matchResult = matcher.match(pattern, system, 1);
        final List<Solution> solutions = matchResult.getSolutions();

        // TODO
    }

    /**
     * Tests if the iterator pattern is detected.
     */
    @Test
    public void testMatchIterator() {
        final DesignPattern pattern = TestHelper.createIteratorPattern();
        final Solutions matchResult = matcher.match(pattern, system, 1);
        final List<Solution> solutions = matchResult.getSolutions();

        // TODO
    }

    /**
     * Tests if the memento pattern is detected.
     */
    @Test
    public void testMatchMemento() {
        final DesignPattern pattern = TestHelper.createMementoPattern();
        final Solutions matchResult = matcher.match(pattern, system, 1);
        final List<Solution> solutions = matchResult.getSolutions();

        // TODO
    }

    /**
     * Tests if the state or strategy pattern is detected.
     */
    @Test
    public void testMatchStateStrategy() {
        final DesignPattern pattern = TestHelper.createStateStrategyPattern();
        final Solutions matchResult = matcher.match(pattern, system, 1);
        final List<Solution> solutions = matchResult.getSolutions();

        // TODO
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
