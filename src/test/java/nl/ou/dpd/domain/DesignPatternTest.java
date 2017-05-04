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

        assertThat(dp.getEdges().get(0).getLeftNode().getName(), is("A"));
        assertThat(dp.getEdges().get(0).getRightNode().getName(), is("B"));
        assertThat(dp.getEdges().get(1).getLeftNode().getName(), is("C"));
        assertThat(dp.getEdges().get(1).getRightNode().getName(), is("D"));
        assertThat(dp.getEdges().get(2).getLeftNode().getName(), is("E"));
        assertThat(dp.getEdges().get(2).getRightNode().getName(), is("F"));
        assertThat(dp.getEdges().get(3).getLeftNode().getName(), is("A"));
        assertThat(dp.getEdges().get(3).getRightNode().getName(), is("C"));
        assertThat(dp.getEdges().get(4).getLeftNode().getName(), is("B"));
        assertThat(dp.getEdges().get(4).getRightNode().getName(), is("E"));

        dp.order();

        assertThat(dp.getEdges().get(0).getLeftNode().getName(), is("A"));
        assertThat(dp.getEdges().get(0).getRightNode().getName(), is("B"));
        assertThat(dp.getEdges().get(1).getLeftNode().getName(), is("A"));
        assertThat(dp.getEdges().get(1).getRightNode().getName(), is("C"));
        assertThat(dp.getEdges().get(2).getLeftNode().getName(), is("C"));
        assertThat(dp.getEdges().get(2).getRightNode().getName(), is("D"));
        assertThat(dp.getEdges().get(3).getLeftNode().getName(), is("B"));
        assertThat(dp.getEdges().get(3).getRightNode().getName(), is("E"));
        assertThat(dp.getEdges().get(4).getLeftNode().getName(), is("E"));
        assertThat(dp.getEdges().get(4).getRightNode().getName(), is("F"));
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
        assertThat(pattern.getEdges().get(0).getLeftNode().getName(), is("A"));
        assertThat(pattern.getEdges().get(0).getRightNode().getName(), is("B"));
        assertFalse(pattern.getEdges().get(0).isVirtual());
        assertThat(pattern.getEdges().get(1).getLeftNode().getName(), is("B"));
        assertThat(pattern.getEdges().get(1).getRightNode().getName(), is("A"));
        assertTrue(pattern.getEdges().get(1).isVirtual());

        pattern.add(new Edge(new Clazz("C", "C"), new Clazz("D", "D"), EdgeType.INHERITANCE));

        assertThat(pattern.getEdges().size(), is(3));
        assertThat(pattern.getEdges().get(0).getLeftNode().getName(), is("A"));
        assertThat(pattern.getEdges().get(0).getRightNode().getName(), is("B"));
        assertFalse(pattern.getEdges().get(0).isVirtual());
        assertThat(pattern.getEdges().get(1).getLeftNode().getName(), is("B"));
        assertThat(pattern.getEdges().get(1).getRightNode().getName(), is("A"));
        assertTrue(pattern.getEdges().get(1).isVirtual());
        assertThat(pattern.getEdges().get(2).getLeftNode().getName(), is("C"));
        assertThat(pattern.getEdges().get(2).getRightNode().getName(), is("D"));
        assertFalse(pattern.getEdges().get(2).isVirtual());
    }
}
