package nl.ou.dpd.domain.edge;

import nl.ou.dpd.domain.node.Clazz;
import org.junit.Test;

import static junit.framework.TestCase.assertNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests the class {@link Edge}.
 *
 * @author Martin de Boer
 */
public class EdgeTest {

    /**
     * Test the {@link Edge} constructor(s).
     */
    @Test
    public void testConstructor() {
        Edge edge = new Edge(new Clazz("class1", "class1"), new Clazz("class2", "class2"), EdgeType.DEPENDENCY, "edge");
        Edge edge2 = new Edge(edge);

        assertThat(edge.getName(), is("edge"));
        assertNull(edge.getCardinalityLeft());
        assertNull(edge.getCardinalityRight());
        assertThat(edge.getLeftNode().getName(), is("class1"));
        assertThat(edge.getRightNode().getName(), is("class2"));
        assertThat(edge.getRelationType(), is(EdgeType.DEPENDENCY));
        assertFalse(edge.isLocked());
        assertFalse(edge.isSelfRef());
        assertFalse(edge.isVirtual());

        assertThat(edge2.getName(), is("edge"));
        assertNull(edge2.getCardinalityLeft());
        assertNull(edge2.getCardinalityRight());
        assertThat(edge2.getLeftNode().getName(), is("class1"));
        assertThat(edge2.getRightNode().getName(), is("class2"));
        assertThat(edge2.getRelationType(), is(EdgeType.DEPENDENCY));
        assertFalse(edge2.isLocked());
        assertFalse(edge2.isSelfRef());
        assertFalse(edge2.isVirtual());
    }

    /**
     * Test(s) the {@link Edge#makeVirtual()} and {@link Edge#isVirtual()} methods.
     */
    @Test
    public void testMakeVirtual() {
        Edge edge = new Edge(new Clazz("A", "A"), new Clazz("B", "B"), EdgeType.AGGREGATE, "name1");
        assertThat(edge.getLeftNode().getName(), is("A"));
        assertThat(edge.getRightNode().getName(), is("B"));
        assertFalse(edge.isVirtual());

        edge.makeVirtual();

        // Check that nodes a reversed and the isVirtual property is set to true
        assertThat(edge.getLeftNode().getName(), is("B"));
        assertThat(edge.getRightNode().getName(), is("A"));
        assertTrue(edge.isVirtual());
    }

    /**
     * Tests the {@link Edge#equals(Object)} method explicitly. Implicitly the constructor and most
     * of the getter methods are tested as well. Edges are considered equal when all their attributes are
     * equal, except for the locked attribute.
     */
    @Test
    public void testEquals() {
        // Compare with null. Not equal.
        Edge edge1 = new Edge(new Clazz("class1", "class1"), new Clazz("class2", "class2"), EdgeType.DEPENDENCY);
        assertFalse(edge1.equals(null));

        // Two duplicate edges. They are equal.
        Edge edge2 = new Edge(edge1);
        assertTrue(edge1.equals(edge2));
        assertTrue(edge1.equals(edge1));

        // A locked edge is still regarded as equal to an unlocked one
        edge2.lock();
        assertFalse(edge1.isLocked());
        assertTrue(edge2.isLocked());
        assertTrue(edge1.equals(edge2));

        // Check if different class names are detected -> not equal
        Edge edge3 = new Edge(new Clazz("class3", "class3"), new Clazz("class2", "class2"), EdgeType.DEPENDENCY);
        assertFalse(edge1.equals(edge3));
        Edge edge4 = new Edge(new Clazz("class1", "class1"), new Clazz("class3", "class3"), EdgeType.DEPENDENCY);
        assertFalse(edge1.equals(edge4));

        // Check if different edge types are detected -> not equal
        Edge edge5 = new Edge(new Clazz("class1", "class1"), new Clazz("class2", "class2"), EdgeType.AGGREGATE);
        assertFalse(edge1.equals(edge5));

        // Check that a virtual edge is not equal to a non-virtual edge
        Edge edge6 = new Edge(edge5);
        assertTrue(edge5.equals(edge6));
        edge6.makeVirtual();
        assertTrue(edge6.isVirtual());
        assertFalse(edge5.isVirtual());
        assertFalse(edge5.equals(edge6));

        // Check that edges with different cardinalities are not equal
        Edge edge7 = new Edge(edge5);
        assertTrue(edge7.equals(edge5));
        edge7.setCardinalityRight(Cardinality.valueOf("0..*"));
        assertThat(edge7.getCardinalityRight().getLower(), is(0));
        assertThat(edge7.getCardinalityRight().getUpper(), is(Cardinality.UNLIMITED));
        assertNull(edge5.getCardinalityRight());
        assertFalse(edge7.equals(edge5));

        edge5.setCardinalityRight(Cardinality.valueOf("0..*"));
        assertTrue(edge7.equals(edge5));
        edge7.setCardinalityLeft(Cardinality.valueOf("1"));
        assertThat(edge7.getCardinalityLeft().getLower(), is(1));
        assertThat(edge7.getCardinalityLeft().getUpper(), is(1));
        assertNull(edge5.getCardinalityLeft());
        assertFalse(edge7.equals(edge5));

        // Check that two edges with different names are not equal
        Edge edge8a = new Edge(new Clazz("A", "A"), new Clazz("B", "B"), EdgeType.AGGREGATE, "name1");
        Edge edge8b = new Edge(new Clazz("A", "A"), new Clazz("B", "B"), EdgeType.AGGREGATE, "name1");
        assertTrue(edge8a.equals(edge8b));
        Edge edge9 = new Edge(new Clazz("A", "A"), new Clazz("B", "B"), EdgeType.AGGREGATE, "name2");
        assertFalse(edge9.equals(edge8b));
    }

    @Test
    public void testIsSelfRef() {
        Edge edge = new Edge(new Clazz("A", "A"), new Clazz("A", "A"), EdgeType.INHERITANCE);
        assertTrue(edge.isSelfRef());
        edge = new Edge(new Clazz("A", "A"), new Clazz("B", "B"), EdgeType.INHERITANCE);
        assertFalse(edge.isSelfRef());
    }

}
