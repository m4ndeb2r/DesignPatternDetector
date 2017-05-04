package nl.ou.dpd.domain;

import nl.ou.dpd.domain.edge.Edge;
import nl.ou.dpd.domain.edge.EdgeType;
import nl.ou.dpd.domain.edge.Edges;
import nl.ou.dpd.domain.node.Clazz;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests the {@link DesignPattern} class.
 *
 * @author Martin de Boer
 */
public class DesignPatternTest {

    /**
     * Tests the {@link DesignPattern#order()} method.
     */
    @Test
    public void testOrder() {
        DesignPattern dp = new DesignPattern("test");
        dp.add(new Edge(new Clazz("A", "A"), new Clazz("B", "B"), EdgeType.AGGREGATE));
        dp.add(new Edge(new Clazz("C", "C"), new Clazz("D", "D"), EdgeType.AGGREGATE));
        dp.add(new Edge(new Clazz("E", "E"), new Clazz("F", "F"), EdgeType.AGGREGATE));
        dp.add(new Edge(new Clazz("A", "A"), new Clazz("C", "C"), EdgeType.AGGREGATE));
        dp.add(new Edge(new Clazz("B", "B"), new Clazz("E", "E"), EdgeType.AGGREGATE));

        assertLeftAndRightNode(dp.getEdges().get(0), "A", "B");
        assertLeftAndRightNode(dp.getEdges().get(1), "C", "D");
        assertLeftAndRightNode(dp.getEdges().get(2), "E", "F");
        assertLeftAndRightNode(dp.getEdges().get(3), "A", "C");
        assertLeftAndRightNode(dp.getEdges().get(4), "B", "E");

        dp.order();

        assertLeftAndRightNode(dp.getEdges().get(0), "A", "B");
        assertLeftAndRightNode(dp.getEdges().get(1), "A", "C");
        assertLeftAndRightNode(dp.getEdges().get(2), "C", "D");
        assertLeftAndRightNode(dp.getEdges().get(3), "B", "E");
        assertLeftAndRightNode(dp.getEdges().get(4), "E", "F");
    }

    /**
     * Tests the {@link DesignPattern#order()} method.
     */
    @Test
    public void testOrderNoChanges() {
        DesignPattern dp = new DesignPattern("test");
        dp.add(new Edge(new Clazz("A", "A"), new Clazz("B", "B"), EdgeType.AGGREGATE));
        dp.add(new Edge(new Clazz("A", "A"), new Clazz("C", "C"), EdgeType.AGGREGATE));
        dp.add(new Edge(new Clazz("A", "A"), new Clazz("D", "D"), EdgeType.AGGREGATE));
        dp.add(new Edge(new Clazz("E", "E"), new Clazz("B", "B"), EdgeType.AGGREGATE));
        dp.add(new Edge(new Clazz("F", "F"), new Clazz("B", "B"), EdgeType.AGGREGATE));
        dp.add(new Edge(new Clazz("E", "E"), new Clazz("G", "G"), EdgeType.AGGREGATE));
        dp.add(new Edge(new Clazz("F", "F"), new Clazz("H", "H"), EdgeType.AGGREGATE));
        dp.add(new Edge(new Clazz("E", "E"), new Clazz("I", "I"), EdgeType.AGGREGATE));
        dp.add(new Edge(new Clazz("F", "F"), new Clazz("J", "J"), EdgeType.AGGREGATE));
        dp.add(new Edge(new Clazz("G", "G"), new Clazz("C", "C"), EdgeType.AGGREGATE));
        dp.add(new Edge(new Clazz("H", "H"), new Clazz("C", "C"), EdgeType.AGGREGATE));
        dp.add(new Edge(new Clazz("I", "I"), new Clazz("D", "D"), EdgeType.AGGREGATE));
        dp.add(new Edge(new Clazz("J", "J"), new Clazz("D", "D"), EdgeType.AGGREGATE));

        assertLeftAndRightNode(dp.getEdges().get(0), "A", "B");
        assertLeftAndRightNode(dp.getEdges().get(1), "A", "C");
        assertLeftAndRightNode(dp.getEdges().get(2), "A", "D");
        assertLeftAndRightNode(dp.getEdges().get(3), "E", "B");
        assertLeftAndRightNode(dp.getEdges().get(4), "F", "B");
        assertLeftAndRightNode(dp.getEdges().get(5), "E", "G");
        assertLeftAndRightNode(dp.getEdges().get(6), "F", "H");
        assertLeftAndRightNode(dp.getEdges().get(7), "E", "I");
        assertLeftAndRightNode(dp.getEdges().get(8), "F", "J");
        assertLeftAndRightNode(dp.getEdges().get(9), "G", "C");
        assertLeftAndRightNode(dp.getEdges().get(10), "H", "C");
        assertLeftAndRightNode(dp.getEdges().get(11), "I", "D");
        assertLeftAndRightNode(dp.getEdges().get(12), "J", "D");

        dp.order();

        assertLeftAndRightNode(dp.getEdges().get(0), "A", "B");
        assertLeftAndRightNode(dp.getEdges().get(1), "A", "C");
        assertLeftAndRightNode(dp.getEdges().get(2), "A", "D");
        assertLeftAndRightNode(dp.getEdges().get(3), "E", "B");
        assertLeftAndRightNode(dp.getEdges().get(4), "F", "B");
        assertLeftAndRightNode(dp.getEdges().get(5), "E", "G");
        assertLeftAndRightNode(dp.getEdges().get(6), "F", "H");
        assertLeftAndRightNode(dp.getEdges().get(7), "E", "I");
        assertLeftAndRightNode(dp.getEdges().get(8), "F", "J");
        assertLeftAndRightNode(dp.getEdges().get(9), "G", "C");
        assertLeftAndRightNode(dp.getEdges().get(10), "H", "C");
        assertLeftAndRightNode(dp.getEdges().get(11), "I", "D");
        assertLeftAndRightNode(dp.getEdges().get(12), "J", "D");
    }

    /**
     * Tests the {@link Edges#add(Edge)} method. When an edge is added with type ASSOCIATION, two edges should be added,
     * of which the second must be virtual. Any other type will result in the addition of jsut one single edge.
     */
    @Test
    public void testAdd() {
        DesignPattern pattern = new DesignPattern("test");

        assertThat(pattern.getEdges().size(), is(0));

        pattern.add(new Edge(new Clazz("A", "A"), new Clazz("B", "B"), EdgeType.ASSOCIATION));

        assertThat(pattern.getEdges().size(), is(2));
        assertLeftAndRightNode(pattern.getEdges().get(0), "A", "B");
        assertFalse(pattern.getEdges().get(0).isVirtual());
        assertLeftAndRightNode(pattern.getEdges().get(1), "B", "A");
        assertTrue(pattern.getEdges().get(1).isVirtual());

        pattern.add(new Edge(new Clazz("C", "C"), new Clazz("D", "D"), EdgeType.INHERITANCE));

        assertThat(pattern.getEdges().size(), is(3));
        assertLeftAndRightNode(pattern.getEdges().get(0), "A", "B");
        assertFalse(pattern.getEdges().get(0).isVirtual());
        assertLeftAndRightNode(pattern.getEdges().get(1), "B", "A");
        assertTrue(pattern.getEdges().get(1).isVirtual());
        assertLeftAndRightNode(pattern.getEdges().get(2), "C", "D");
        assertFalse(pattern.getEdges().get(2).isVirtual());
    }

    private void assertLeftAndRightNode(Edge edge, String leftId, String rightId) {
        assertThat(edge.getLeftNode().getId(), is(leftId));
        assertThat(edge.getRightNode().getId(), is(rightId));
    }

}
