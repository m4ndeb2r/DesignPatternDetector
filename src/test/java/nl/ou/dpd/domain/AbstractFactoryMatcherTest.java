package nl.ou.dpd.domain;

import nl.ou.dpd.utils.TestHelper;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Tests the {@link Matcher} class.
 *
 * @author Martin de Boer
 */
public class AbstractFactoryMatcherTest {

    private Matcher matcher;
    private SystemUnderConsideration system;

    /**
     * Initialises the test(s).
     */
    @Before
    public void initMatcher() {
        matcher = new Matcher();
        system = createSystemUnderConsiderationContainingBridge();
    }

    /**
     * Tests if the abstract factory pattern is detected with no missing edge allowed.
     */
    @Test
    public void testMatch() {
        final DesignPattern pattern = TestHelper.createAbstractFactoryPattern();
        final Solutions matchResult = matcher.match(pattern, system, 1);
        final List<Solution> solutions = matchResult.getSolutions();

        // Check number of times the pattern is detected.
        assertThat(solutions.size(), is(1));

        final Solution s0 = solutions.get(0);
        final MatchedClasses mc0 = s0.getMatchedClasses();
        final Set<Edge> se0 = s0.getSuperfluousEdges();
        final Set<Edge> me0 = s0.getMissingEdges();

        // Check the name
        assertThat(s0.getDesignPatternName(), is("Abstract Factory"));

        // Check matching classes
        assertThat(mc0.get(new Clazz("FactoryClient")).getName(), is("Client"));
        assertThat(mc0.get(new Clazz("WidgetFactory")).getName(), is("AbstractFactory"));

        assertThat(mc0.get(new Clazz("Window")).getName(), is("AbstractProductA"));
        assertThat(mc0.get(new Clazz("PMWindow")).getName(), is("ProductA1"));
        assertThat(mc0.get(new Clazz("MotifWindow")).getName(), is("ProductA2"));

        assertThat(mc0.get(new Clazz("ScrollBar")).getName(), is("AbstractProductB"));
        assertThat(mc0.get(new Clazz("PMScrollBar")).getName(), is("ProductB1"));
        assertThat(mc0.get(new Clazz("MotifScrollBar")).getName(), is("ProductB2"));

        assertThat(mc0.get(new Clazz("PMWidgetFactory")).getName(), is("ConcreteFact1"));
        assertThat(mc0.get(new Clazz("MotifWidgetFactory")).getName(), is("ConcreteFact2"));

        // Check superfluous edges
        assertThat(se0.size(), is(0));

        // Check missing edges
        assertThat(me0.size(), is(0));
    }

    private SystemUnderConsideration createSystemUnderConsiderationContainingBridge() {
        SystemUnderConsideration result = new SystemUnderConsideration();

        result.add(new Edge(new Clazz("FactoryClient"), new Clazz("WidgetFactory"), EdgeType.ASSOCIATION_DIRECTED));
        result.add(new Edge(new Clazz("FactoryClient"), new Clazz("Window"), EdgeType.ASSOCIATION_DIRECTED));
        result.add(new Edge(new Clazz("FactoryClient"), new Clazz("ScrollBar"), EdgeType.ASSOCIATION_DIRECTED));

        result.add(new Edge(new Clazz("PMWindow"), new Clazz("Window"), EdgeType.INHERITANCE));
        result.add(new Edge(new Clazz("MotifWindow"), new Clazz("Window"), EdgeType.INHERITANCE));
        result.add(new Edge(new Clazz("PMScrollBar"), new Clazz("ScrollBar"), EdgeType.INHERITANCE));
        result.add(new Edge(new Clazz("MotifScrollBar"), new Clazz("ScrollBar"), EdgeType.INHERITANCE));

        result.add(new Edge(new Clazz("PMWidgetFactory"), new Clazz("WidgetFactory"), EdgeType.INHERITANCE));
        result.add(new Edge(new Clazz("PMWidgetFactory"), new Clazz("PMWindow"), EdgeType.DEPENDENCY));
        result.add(new Edge(new Clazz("PMWidgetFactory"), new Clazz("PMScrollBar"), EdgeType.DEPENDENCY));

        result.add(new Edge(new Clazz("MotifWidgetFactory"), new Clazz("WidgetFactory"), EdgeType.INHERITANCE));
        result.add(new Edge(new Clazz("MotifWidgetFactory"), new Clazz("MotifWindow"), EdgeType.DEPENDENCY));
        result.add(new Edge(new Clazz("MotifWidgetFactory"), new Clazz("MotifScrollBar"), EdgeType.DEPENDENCY));

        return result;
    }

}
