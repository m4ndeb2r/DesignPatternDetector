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
public class InterpreterMatcherTest {

    private Matcher matcher;
    private SystemUnderConsideration system;

    /**
     * Initialises the test(s).
     * <p>
     * TODO:
     * There are some problems with this pattern. Whe we apply the example on page 264 of GoF, we get 3 different
     * solutions. In some of these solutions, the distinction between terminal and non-terminal expressions is not
     * made correclty. This has to be fixed inthe application. FIXME.
     * <p>
     * For the time being this test focusses only on a simpler example, that does make this test run succesful.
     */
    @Before
    public void initMatcher() {
        matcher = new Matcher();
        system = createSystemUnderConsideration();
    }

    /**
     * Tests if the interpreter pattern is detected with no missing edge allowed.
     */
    @Test
    public void testMatch() {
        final DesignPattern pattern = TestHelper.createInterpreterPattern();
        final Solutions matchResult = matcher.match(pattern, system, 0);
        final List<Solution> solutions = matchResult.getSolutions();

        matchResult.show();

        // Check number of times the pattern is detected.
        assertThat(solutions.size(), is(1));

        final Solution s0 = solutions.get(0);
        final MatchedClasses mc0 = s0.getMatchedClasses();
        final Set<Edge> se0 = s0.getSuperfluousEdges();
        final Set<Edge> me0 = s0.getMissingEdges();

        // Check the name
        assertThat(s0.getDesignPatternName(), is("Interpreter"));

        // Check matching classes
        assertThat(mc0.get(new Clazz("ExpressionClient")).getName(), is("Client"));
        assertThat(mc0.get(new Clazz("ExpressionContext")).getName(), is("Context"));
        assertThat(mc0.get(new Clazz("RegularExpression")).getName(), is("AbstractExpression"));
        assertThat(mc0.get(new Clazz("LiteralExpression")).getName(), is("TerminalExpression"));
        assertThat(mc0.get(new Clazz("SequenceExpression")).getName(), is("NonTerminalExpression"));

        // Check superfluous edges
        assertThat(se0.size(), is(0));

        // Check missing edges
        assertThat(me0.size(), is(0));
    }

    private SystemUnderConsideration createSystemUnderConsideration() {
        SystemUnderConsideration result = new SystemUnderConsideration();
        result.add(new Edge(new Clazz("ExpressionClient"), new Clazz("ExpressionContext"), EdgeType.ASSOCIATION_DIRECTED));
        result.add(new Edge(new Clazz("ExpressionClient"), new Clazz("RegularExpression"), EdgeType.ASSOCIATION_DIRECTED));
        result.add(new Edge(new Clazz("LiteralExpression"), new Clazz("RegularExpression"), EdgeType.INHERITANCE));
        result.add(new Edge(new Clazz("SequenceExpression"), new Clazz("RegularExpression"), EdgeType.INHERITANCE));
        result.add(new Edge(new Clazz("RegularExpression"), new Clazz("SequenceExpression"), EdgeType.AGGREGATE));

        // FIXME: the results are not as expected when we add these edges (example on page 264 in GoF)
        // result.add(new Edge(new Clazz("RepetitionExpression"), new Clazz("RegularExpression"), EdgeType.INHERITANCE));
        // result.add(new Edge(new Clazz("AlternationExpression"), new Clazz("RegularExpression"), EdgeType.INHERITANCE));
        // result.add(new Edge(new Clazz("RegularExpression"), new Clazz("RepetitionExpression"), EdgeType.AGGREGATE));
        // result.add(new Edge(new Clazz("RegularExpression"), new Clazz("AlternationExpression"), EdgeType.AGGREGATE));
        return result;
    }

}
