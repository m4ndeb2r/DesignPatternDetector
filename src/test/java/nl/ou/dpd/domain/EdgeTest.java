package nl.ou.dpd.domain;

import nl.ou.dpd.utils.TestHelper;
import org.junit.Test;

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
     * Tests the {@link Edge#equals(Object)} method explicitly. Implicitly the constructor and most
     * of the getter methods are tested as well. Edges are considered equal when all their attributes are
     * equal, except for the locked attribute.
     */
    @Test
    public void testEquals() {
        // Two duplicate edges. They are equal.
        Edge edge1 = TestHelper.createEdge("class1", "class2", EdgeType.DEPENDENCY);
        Edge edge2 = new Edge(edge1);
        assertTrue(edge1.equals(edge2));

        // A locked edge is still regarded as equal to an unlocked one
        edge2.lock();
        assertFalse(edge1.isLocked());
        assertTrue(edge2.isLocked());
        assertTrue(edge1.equals(edge2));

        // Check if different class names are detected -> not equal
        Edge edge3 = TestHelper.createEdge("class3", "class2", EdgeType.DEPENDENCY);
        assertFalse(edge1.equals(edge3));
        Edge edge4 = TestHelper.createEdge("class1", "class3", EdgeType.DEPENDENCY);
        assertFalse(edge1.equals(edge4));

        // Check if different edge types are detected -> not equal
        Edge edge5 = TestHelper.createEdge("class1", "class2", EdgeType.AGGREGATE);
        assertFalse(edge1.equals(edge5));

        // Check that a virtual edge is not equal to a non-virtual edge
        Edge edge6 = new Edge(edge5);
        assertThat(edge5.equals(edge6), is(true));
        edge6.makeVirtual();
        assertTrue(edge6.isVirtual());
        assertFalse(edge5.isVirtual());
        assertFalse(edge5.equals(edge6));
    }

    @Test
    public void testIsSelfRef() {
        Edge edge = TestHelper.createEdge("A", "A", EdgeType.INHERITANCE);
        assertTrue(edge.isSelfRef());

        edge = TestHelper.createEdge("A", "B", EdgeType.INHERITANCE);
        assertFalse(edge.isSelfRef());
    }

}
