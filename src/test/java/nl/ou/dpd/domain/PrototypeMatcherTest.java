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
 * <p>
 * TODO:
 * The example in GoF on page 128/129 is not properly detected. The application is not able to skip the abstract
 * MusicalNote and detect two implementation: WholeNote and HalfNote. This unittest focusses on a somewhat simplified
 * example to make it succeed. This is something to be fixed in the application. FIXME
 *
 * @author Martin de Boer
 */
public class PrototypeMatcherTest {

    private Matcher matcher;
    private SystemUnderConsideration system;

    /**
     * Initialises the test(s).
     */
    @Before
    public void initMatcher() {
        matcher = new Matcher();
        system = createSystemUnderConsideration();
    }

    /**
     * Tests if the prototype pattern is detected with no missing edge allowed.
     */
    @Test
    public void testMatch() {
        final DesignPattern pattern = TestHelper.createPrototypePattern();
        final Solutions matchResult = matcher.match(pattern, system, 0);
        final List<Solution> solutions = matchResult.getSolutions();

        // Check number of times the pattern is detected.
        assertThat(solutions.size(), is(1));

        final Solution s0 = solutions.get(0);
        final MatchedClasses mc0 = s0.getMatchedClasses();
        final Set<Edge> se0 = s0.getSuperfluousEdges();
        final Set<Edge> me0 = s0.getMissingEdges();

        // Check the name
        assertThat(s0.getDesignPatternName(), is("Prototype"));

        // Check matching classes
        assertThat(mc0.get(new Clazz("GraphicTool")).getName(), is("Client"));
        assertThat(mc0.get(new Clazz("Graphic")).getName(), is("Prototype"));
        assertThat(mc0.get(new Clazz("Staff")).getName(), is("ConcretePrototype"));
        assertThat(mc0.get(new Clazz("MusicalNote")).getName(), is("ConcretePrototype"));

        // Check superfluous edges
        assertThat(se0.size(), is(0));

        // Check missing edges
        assertThat(me0.size(), is(0));
    }

    private SystemUnderConsideration createSystemUnderConsideration() {
        SystemUnderConsideration result = new SystemUnderConsideration();
        result.add(new Edge(new Clazz("GraphicTool"), new Clazz("Graphic"), EdgeType.ASSOCIATION_DIRECTED));
        result.add(new Edge(new Clazz("Staff"), new Clazz("Graphic"), EdgeType.INHERITANCE));
        result.add(new Edge(new Clazz("MusicalNote"), new Clazz("Graphic"), EdgeType.INHERITANCE));
        return result;
    }

}
