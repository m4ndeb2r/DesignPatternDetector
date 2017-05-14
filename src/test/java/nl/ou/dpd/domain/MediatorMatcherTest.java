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

/**
 * Tests the {@link Matcher} class.
 * <p>
 * TODO:
 * The problem with the mediator design pattern definition in this application, is that it is not extendable. It
 * expects exactly two ConcreteColleagues. I would suppose an INHERITANCE_MULTI to fix this, but in the current state
 * of the application we would run in to other problems, because the ConcreteCollegues have a directed association
 * with the ConcreteMediator. This should be fixed in the application. FIXME.
 * <p>
 * This test uses an example with two concrete colleagues, so we don;t run into problems for now ...
 *
 * @author Martin de Boer
 */
public class MediatorMatcherTest {

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
     * Tests if the mediator pattern is detected with no missing edge allowed.
     */
    @Test
    public void testMatch() {
        final DesignPattern pattern = TestHelper.createMediatorPattern();
        final Solutions matchResult = matcher.match(pattern, system, 0);
        final List<Solution> solutions = matchResult.getSolutions();

        // Check number of times the pattern is detected.
        assertThat(solutions.size(), is(1));

        final Solution s0 = solutions.get(0);
        final MatchedNodes mc0 = s0.getMatchedNodes();
        final Set<Edge> se0 = s0.getSuperfluousEdges();
        final Set<Edge> me0 = s0.getMissingEdges();

        // Check the name
        assertThat(s0.getDesignPatternName(), is("Mediator"));

        // Check matching classes
        assertThat(mc0.get(new Clazz("DialogDirector", "DialogDirector")).getName(), is("Mediator"));
        assertThat(mc0.get(new Clazz("FrontDialogDirector", "FrontDialogDirector")).getName(), is("ConcreteMediator"));
        assertThat(mc0.get(new Clazz("Widget", "Widget")).getName(), is("Colleague"));
        assertThat(mc0.get(new Clazz("ListBox", "ListBox")).getName(), is("ConcreteColleague1"));
        assertThat(mc0.get(new Clazz("EntryField", "EntryField")).getName(), is("ConcreteColleague2"));

        // Check superfluous edges
        assertThat(se0.size(), is(0));

        // Check missing edges
        assertThat(me0.size(), is(0));
    }

    private SystemUnderConsideration createSystemUnderConsideration() {
        SystemUnderConsideration result = new SystemUnderConsideration();
        result.add(new Edge(new Clazz("Widget", "Widget"), new Clazz("DialogDirector", "DialogDirector"), EdgeType.ASSOCIATION_DIRECTED));
        result.add(new Edge(new Clazz("FrontDialogDirector", "FrontDialogDirector"), new Clazz("DialogDirector", "DialogDirector"), EdgeType.INHERITANCE));
        result.add(new Edge(new Clazz("ListBox", "ListBox"), new Clazz("Widget", "Widget"), EdgeType.INHERITANCE));
        result.add(new Edge(new Clazz("EntryField", "EntryField"), new Clazz("Widget", "Widget"), EdgeType.INHERITANCE));
        result.add(new Edge(new Clazz("FrontDialogDirector", "FrontDialogDirector"), new Clazz("ListBox", "ListBox"), EdgeType.ASSOCIATION_DIRECTED));
        result.add(new Edge(new Clazz("FrontDialogDirector", "FrontDialogDirector"), new Clazz("EntryField", "EntryField"), EdgeType.ASSOCIATION_DIRECTED));
        return result;
    }

}
