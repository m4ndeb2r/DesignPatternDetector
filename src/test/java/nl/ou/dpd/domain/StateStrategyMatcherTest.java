package nl.ou.dpd.domain;

import nl.ou.dpd.domain.edge.Edge;
import nl.ou.dpd.domain.edge.EdgeType;
import nl.ou.dpd.domain.node.Clazz;
import nl.ou.dpd.domain.node.Interface;
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
public class StateStrategyMatcherTest {

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
     * Tests if the state and strategy pattern is detected with no missing edge allowed.
     */
    @Test
    public void testMatch() {
/*        final DesignPattern pattern = TestHelper.createStateStrategyPattern();
        final Solutions matchResult = matcher.match(pattern, system, 0);
        final List<Solution> solutions = matchResult.getSolutions();

        // Check number of times the pattern is detected.
        assertThat(solutions.size(), is(1));

        final Solution s0 = solutions.get(0);
        final MatchedNodes mc0 = s0.getMatchedNodes();
        final Set<Edge> se0 = s0.getSuperfluousEdges();
        final Set<Edge> me0 = s0.getMissingEdges();

        // Check the name
        assertThat(s0.getDesignPatternName(), is("State - Strategy"));

        // Check matching classes
        assertThat(mc0.get(new Clazz("TCPConnection", "TCPConnection")).getName(), is("Context"));
        assertThat(mc0.get(new Interface("TCPState", "TCPState")).getName(), is("Strategy"));
        assertThat(mc0.get(new Clazz("TCPEstablished", "TCPEstablished")).getName(), is("ConcreteStrategy"));
        assertThat(mc0.get(new Clazz("TCPListen", "TCPListen")).getName(), is("ConcreteStrategy"));
        assertThat(mc0.get(new Clazz("TCPClosed", "TCPClosed")).getName(), is("ConcreteStrategy"));

        // Check superfluous edges
        assertThat(se0.size(), is(0));

        // Check missing edges
        assertThat(me0.size(), is(0));
*/    }

    private SystemUnderConsideration createSystemUnderConsideration() {
        final Interface tcpState = new Interface("TCPState", "TCPState");
        final Clazz tcpConnection = new Clazz("TCPConnection", "TCPConnection");
        final Clazz tcpEstablished = new Clazz("TCPEstablished", "TCPEstablished");
        final Clazz tcpListen = new Clazz("TCPListen", "TCPListen");
        final Clazz tcpClosed = new Clazz("TCPClosed", "TCPClosed");
        final SystemUnderConsideration result = new SystemUnderConsideration();

        result.add(new Edge(tcpState, tcpConnection, EdgeType.AGGREGATE));
        result.add(new Edge(tcpEstablished, tcpState, EdgeType.INHERITANCE));
        result.add(new Edge(tcpListen, tcpState, EdgeType.INHERITANCE));
        result.add(new Edge(tcpClosed, tcpState, EdgeType.INHERITANCE));

        return result;
    }

}
