package nl.ou.dpd.domain;

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
        dp.add(new Edge(new Clazz("A"), new Clazz("B"), EdgeType.AGGREGATE));
        dp.add(new Edge(new Clazz("C"), new Clazz("D"), EdgeType.AGGREGATE));
        dp.add(new Edge(new Clazz("E"), new Clazz("F"), EdgeType.AGGREGATE));
        dp.add(new Edge(new Clazz("A"), new Clazz("C"), EdgeType.AGGREGATE));
        dp.add(new Edge(new Clazz("B"), new Clazz("E"), EdgeType.AGGREGATE));

        assertThat(dp.getEdges().get(0).getClass1().getName(), is("A"));
        assertThat(dp.getEdges().get(0).getClass2().getName(), is("B"));
        assertThat(dp.getEdges().get(1).getClass1().getName(), is("C"));
        assertThat(dp.getEdges().get(1).getClass2().getName(), is("D"));
        assertThat(dp.getEdges().get(2).getClass1().getName(), is("E"));
        assertThat(dp.getEdges().get(2).getClass2().getName(), is("F"));
        assertThat(dp.getEdges().get(3).getClass1().getName(), is("A"));
        assertThat(dp.getEdges().get(3).getClass2().getName(), is("C"));
        assertThat(dp.getEdges().get(4).getClass1().getName(), is("B"));
        assertThat(dp.getEdges().get(4).getClass2().getName(), is("E"));

        dp.order();

        assertThat(dp.getEdges().get(0).getClass1().getName(), is("A"));
        assertThat(dp.getEdges().get(0).getClass2().getName(), is("B"));
        assertThat(dp.getEdges().get(1).getClass1().getName(), is("A"));
        assertThat(dp.getEdges().get(1).getClass2().getName(), is("C"));
        assertThat(dp.getEdges().get(2).getClass1().getName(), is("C"));
        assertThat(dp.getEdges().get(2).getClass2().getName(), is("D"));
        assertThat(dp.getEdges().get(3).getClass1().getName(), is("B"));
        assertThat(dp.getEdges().get(3).getClass2().getName(), is("E"));
        assertThat(dp.getEdges().get(4).getClass1().getName(), is("E"));
        assertThat(dp.getEdges().get(4).getClass2().getName(), is("F"));
    }

    /**
     * Tests the {@link Edges#add(Edge)} method. When an edge is added with type ASSOCIATION, two edges should be added,
     * of which the second must be virtual. Any other type will result in the addition of jsut one single edge.
     */
    @Test
    public void testAdd() {
        DesignPattern pattern = new DesignPattern("test");

        assertThat(pattern.getEdges().size(), is(0));

        pattern.add(new Edge(new Clazz("A"), new Clazz("B"), EdgeType.ASSOCIATION));

        assertThat(pattern.getEdges().size(), is(2));
        assertThat(pattern.getEdges().get(0).getClass1().getName(), is("A"));
        assertThat(pattern.getEdges().get(0).getClass2().getName(), is("B"));
        assertFalse(pattern.getEdges().get(0).isVirtual());
        assertThat(pattern.getEdges().get(1).getClass1().getName(), is("B"));
        assertThat(pattern.getEdges().get(1).getClass2().getName(), is("A"));
        assertTrue(pattern.getEdges().get(1).isVirtual());

        pattern.add(new Edge(new Clazz("C"), new Clazz("D"), EdgeType.INHERITANCE));

        assertThat(pattern.getEdges().size(), is(3));
        assertThat(pattern.getEdges().get(0).getClass1().getName(), is("A"));
        assertThat(pattern.getEdges().get(0).getClass2().getName(), is("B"));
        assertFalse(pattern.getEdges().get(0).isVirtual());
        assertThat(pattern.getEdges().get(1).getClass1().getName(), is("B"));
        assertThat(pattern.getEdges().get(1).getClass2().getName(), is("A"));
        assertTrue(pattern.getEdges().get(1).isVirtual());
        assertThat(pattern.getEdges().get(2).getClass1().getName(), is("C"));
        assertThat(pattern.getEdges().get(2).getClass2().getName(), is("D"));
        assertFalse(pattern.getEdges().get(2).isVirtual());
    }
}
