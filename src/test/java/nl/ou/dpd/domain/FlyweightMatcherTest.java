package nl.ou.dpd.domain;

import nl.ou.dpd.domain.edge.Edge;
import nl.ou.dpd.domain.edge.EdgeType;
import nl.ou.dpd.domain.node.Clazz;
import nl.ou.dpd.utils.TestHelper;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertTrue;

/**
 * Tests the {@link Matcher} class.
 * <p>
 * TODO:
 * Detection of a flyweight works well when only one UnsharedConcreteFlyweight exists. But the GoF book provides an
 * example on page 211 that does not work. This example contains two unsharedConcreteFlyweights (row and column). When
 * this example is being run as a test, the application finds 4 candidate matches, two of which are flat out wrong.
 * This is something that should be fixed in the application. FIXME
 *
 * @author Martin de Boer
 */
public class FlyweightMatcherTest {

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
     * Tests if the flyweight pattern is detected with no missing edge allowed.
     */
    @Test
    public void testMatch() {
        final DesignPattern pattern = TestHelper.createFlyweightPattern();
        final Solutions matchResult = matcher.match(pattern, system, 0);
        final List<Solution> solutions = matchResult.getSolutions();

        // Check number of times the pattern is detected.
        assertThat(solutions.size(), is(1));

        final Solution s0 = solutions.get(0);
        final MatchedNodes mc0 = s0.getMatchedNodes();
        final Set<Edge> se0 = s0.getSuperfluousEdges();
        final Set<Edge> me0 = s0.getMissingEdges();

        // Check the name
        assertThat(s0.getDesignPatternName(), is("Flyweight"));

        // Check matching classes
        assertThat(mc0.get(new Clazz("Glyph", "Glyph")).getName(), is("Flyweight"));
        assertThat(mc0.get(new Clazz("GlyphFactory", "GlyphFactory")).getName(), is("FlyweightFactory"));
        assertThat(mc0.get(new Clazz("Client", "Client")).getName(), is("Client"));
        assertThat(mc0.get(new Clazz("Character", "Character")).getName(), is("ConcreteFlyweight"));
        assertThat(mc0.get(new Clazz("Row", "Row")).getName(), is("UnsharedConcreteFlyweight"));
        // FIXME assertThat(mc0.get(new Clazz("Column")).getName(), is("UnsharedConcreteFlyweight"));

        // Check superfluous edges
        assertThat(se0.size(), is(1));
        assertTrue(se0.contains(new Edge(new Clazz("Glyph", "Glyph"), new Clazz("Row", "Row"), EdgeType.AGGREGATE)));

        // Check missing edges
        assertThat(me0.size(), is(0));
    }

    private SystemUnderConsideration createSystemUnderConsideration() {
        SystemUnderConsideration result = new SystemUnderConsideration();
        result.add(new Edge(new Clazz("Glyph", "Glyph"), new Clazz("GlyphFactory", "GlyphFactory"), EdgeType.AGGREGATE));
        result.add(new Edge(new Clazz("Glyph", "Glyph"), new Clazz("Row", "Row"), EdgeType.AGGREGATE)); // Superfluous
        // FIXME result.add(new Edge(new Clazz("Glyph"), new Clazz("Column"), EdgeType.AGGREGATE)); // Superfluous
        result.add(new Edge(new Clazz("Row", "Row"), new Clazz("Glyph", "Glyph"), EdgeType.INHERITANCE));
        // FIXME result.add(new Edge(new Clazz("Column"), new Clazz("Glyph"), EdgeType.INHERITANCE));
        result.add(new Edge(new Clazz("Character", "Character"), new Clazz("Glyph", "Glyph"), EdgeType.INHERITANCE));
        result.add(new Edge(new Clazz("Client", "Client"), new Clazz("Character", "Character"), EdgeType.ASSOCIATION_DIRECTED));
        result.add(new Edge(new Clazz("Client", "Client"), new Clazz("Row", "Row"), EdgeType.ASSOCIATION_DIRECTED));
        // FIXME result.add(new Edge(new Clazz("Client"), new Clazz("Column"), EdgeType.ASSOCIATION_DIRECTED));
        result.add(new Edge(new Clazz("Client", "Client"), new Clazz("GlyphFactory", "GlyphFactory"), EdgeType.ASSOCIATION_DIRECTED));
        return result;
    }

}
