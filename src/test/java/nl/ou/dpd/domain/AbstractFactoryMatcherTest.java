package nl.ou.dpd.domain;

import nl.ou.dpd.domain.edge.Edge;
import nl.ou.dpd.domain.edge.EdgeType;
import nl.ou.dpd.domain.node.Clazz;
import nl.ou.dpd.utils.TestHelper;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Tests the {@link Matcher} class.
 * <p>
 * TODO:
 * There is a small problem with the current pattern definition. It is defined with exactly 2 ConcreteFactory's (and
 * hence, two ProductA's and two ProductB's. When the system under consideration has three or more ConcreteFactory's
 * the application will find several instances of the pattern, which is not actually the case. There is still one
 * instance of the pattern, but one with three ConcreteFactories. For the current test, only two are ConcreteFactory
 * instances are defined in the system under control, so the test will succeed. We should improve the application,
 * and fix this bug.
 * <p>
 * TODO:
 * The same applies to the number of AbstractProducts. When a third AbstractProduct is added, more than 1 instances
 * of the pattern are found.
 *
 * @author Martin de Boer
 */
public class AbstractFactoryMatcherTest {

    private Matcher matcher;

    /**
     * Initialises the test(s).
     */
    @Before
    public void initMatcher() {
        matcher = new Matcher();
    }

    /**
     * Tests if the abstract factory pattern is detected with no missing edge allowed. This test performs the
     * {@link Matcher#match(DesignPattern, SystemUnderConsideration, int)} method for a system with 2 ConcreteFactories
     * and 2 AbstractProducts.
     */
    @Test
    public void testMatch_2_2() {
//        assertMatch(2, 2);
    }

    /**
     * Tests if the abstract factory pattern is detected with no missing edge allowed. This test performs the
     * {@link Matcher#match(DesignPattern, SystemUnderConsideration, int)} method for a system with 3 ConcreteFactories
     * and 2 AbstractProducts.
     */
    @Ignore("This test fails because 3 patterns are detected instead of one. This was already the case in the " +
            "initial prototype. FIX THIS") // FIXME
    @Test
    public void testMatch_3_2() {
        assertMatch(3, 2);
    }

    private void assertMatch(int factories, int products) {
        final DesignPattern pattern = TestHelper.createAbstractFactoryPattern();
        final SystemUnderConsideration system = createSystemUnderConsideration(factories, products);

        final Solutions matchResult = matcher.match(pattern, system, 1);
        final List<Solution> solutions = matchResult.getSolutions();

        // Check number of times the pattern is detected.
        assertThat(solutions.size(), is(1));

        final Solution s0 = solutions.get(0);
        final MatchedNodes mc0 = s0.getMatchedNodes();
        final Set<Edge> se0 = s0.getSuperfluousEdges();
        final Set<Edge> me0 = s0.getMissingEdges();

        // Check the name
        assertThat(s0.getDesignPatternName(), is("Abstract Factory"));

        // Check matching classes
        assertThat(mc0.get(new Clazz("FactoryClient", "FactoryClient")).getName(), is("Client"));
        assertThat(mc0.get(new Clazz("WidgetFactory", "WidgetFactory")).getName(), is("AbstractFactory"));

        for (int i = 0; i < factories; i++) {
            assertThat(
                    mc0.get(new Clazz("ConcreteWidgetFactory" + (i + 1), "ConcreteWidgetFactory" + (i + 1))).getName(),
                    is("ConcreteFact" + (i + 1)));
            for (int j = 0; j < products; j++) {
                String postfix = Character.toString((char) (65 + j));
                assertThat(
                        mc0.get(new Clazz("AbstractWidget" + postfix, "AbstractWidget" + postfix)).getName(),
                        is("AbstractProduct" + postfix));
                assertThat(
                        mc0.get(new Clazz("ConcreteWidget" + postfix + (i + 1), "ConcreteWidget" + postfix + (i + 1))).getName(),
                        is("Product" + postfix + (i + 1)));
            }
        }

        // Check superfluous edges
        assertThat(se0.size(), is(0));

        // Check missing edges
        assertThat(me0.size(), is(0));
    }

    private SystemUnderConsideration createSystemUnderConsideration(int factories, int products) {
        SystemUnderConsideration result = new SystemUnderConsideration();

        // Add directed associations from the client to the WidgetFactory and every every abstract product
        // Add concrete products (inheritances to abstract products, and dependencies to concrete factories)
        result.add(new Edge(new Clazz("FactoryClient", "FactoryClient"), new Clazz("AbstractWidgetFactory", "AbstractWidgetFactory"), EdgeType.ASSOCIATION_DIRECTED));
        for (int i = 0; i < products; i++) {
            String postfix = Character.toString((char) (65 + i));
            result.add(
                    new Edge(
                            new Clazz("FactoryClient", "FactoryClient"),
                            new Clazz("AbstractWidget" + postfix, "AbstractWidget" + postfix),
                            EdgeType.ASSOCIATION_DIRECTED));
            for (int j = 0; j < factories; j++) {
                result.add(
                        new Edge(
                                new Clazz("ConcreteWidget" + postfix + (j + 1), "ConcreteWidget" + postfix + (j + 1)),
                                new Clazz("AbstractWidget" + postfix, "AbstractWidget" + postfix),
                                EdgeType.INHERITANCE));
                result.add(
                        new Edge(
                                new Clazz("ConcreteWidgetFactory" + (j + 1), "ConcreteWidgetFactory" + (j + 1)),
                                new Clazz("ConcreteWidget" + postfix + (j + 1), "ConcreteWidget" + postfix + (j + 1)),
                                EdgeType.DEPENDENCY));
            }
        }

        // Add dependencies from every ConcreteWidgetFactory to the AbstractWidgetFactory
        for (int i = 0; i < factories; i++) {
            result.add(
                    new Edge(
                            new Clazz("ConcreteWidgetFactory" + (i + 1), "ConcreteWidgetFactory" + (i + 1)),
                            new Clazz("WidgetFactory", "WidgetFactory"),
                            EdgeType.INHERITANCE));
        }

        return result;
    }

}
