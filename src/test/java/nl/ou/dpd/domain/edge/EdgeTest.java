package nl.ou.dpd.domain.edge;

import nl.ou.dpd.domain.node.Clazz;
import nl.ou.dpd.domain.node.Node;
import org.junit.Before;
import org.junit.Ignore;
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

    private Edge edgeForEqTests1;
    private Edge edgeForEqTests2;

    /**
     * There are several equality tests in this test class. Set up two equal edges before each test here, so in the
     * test one attribute at a time can be changed and the edge can be tested for (in)equality.
     */
    @Before
    public void initEdgesForEqualityTests() {
        final Node leftNode = new Clazz("a", "A");
        final Node rightNode = new Clazz("b", "B");
        edgeForEqTests1 = new Edge("id", "name", leftNode, rightNode);
        edgeForEqTests1.setRelationType(EdgeType.DEPENDENCY);
        edgeForEqTests1.setCardinalityLeft(Cardinality.valueOf("1"));
        edgeForEqTests1.setCardinalityRight(Cardinality.valueOf("*"));
        edgeForEqTests2 = edgeForEqTests1.duplicate();
    }

    @Test
    public void testEdgeNotEqualsNull() {
        assertFalse(edgeForEqTests1.equals(null));
    }

    @Test
    public void testEqualsOkay() {
        assertTrue(edgeForEqTests1.equals(edgeForEqTests1));
        assertTrue(edgeForEqTests1.equals(edgeForEqTests2));
    }

    @Test
    public void testEqualsIgnoresLock() {
        // A locked edge is still regarded as equal to an unlocked one
        edgeForEqTests2.lock();
        assertFalse(edgeForEqTests1.isLocked());
        assertTrue(edgeForEqTests2.isLocked());
        assertTrue(edgeForEqTests1.equals(edgeForEqTests2));
    }

    @Test
    public void testEqualsNotOkWhenNameDiffers() {
        edgeForEqTests1.setName("different");
        assertFalse(edgeForEqTests1.equals(edgeForEqTests2));
    }

    @Test
    @Ignore("In the current version the id is not always set. Fix that and activate this test.") // TODO
    public void testEqualsNotOkWhenIdDiffers() {
        edgeForEqTests1.setId("different");
        assertFalse(edgeForEqTests1.equals(edgeForEqTests2));
    }

    @Test
    public void testEqualsNotOkWhenClassNameDiffer() {
        // Check if different class names are detected -> not equal
        edgeForEqTests1.setLeftNode(new Clazz("a", "NotA"));
        assertFalse(edgeForEqTests1.equals(edgeForEqTests2));
        edgeForEqTests1.setLeftNode(edgeForEqTests2.getLeftNode());
        edgeForEqTests1.setRightNode(new Clazz("b", "NotB"));
        assertFalse(edgeForEqTests1.equals(edgeForEqTests2));
    }

    @Test
    public void testEqualsNotOkWhenClassIdsDiffer() {
        // Check if different class ids are detected -> not equal
        edgeForEqTests1.setLeftNode(new Clazz("not_a", "A"));
        assertFalse(edgeForEqTests1.equals(edgeForEqTests2));
        edgeForEqTests1.setLeftNode(edgeForEqTests2.getLeftNode());
        assertTrue(edgeForEqTests1.equals(edgeForEqTests2));
        edgeForEqTests1.setRightNode(new Clazz("not_b", "B"));
        assertFalse(edgeForEqTests1.equals(edgeForEqTests2));
    }

    @Test
    public void testEqualsNotOkWhenEdgeTypesDiffer() {
        edgeForEqTests1.setRelationType(EdgeType.AGGREGATE);
        assertFalse(edgeForEqTests1.equals(edgeForEqTests2));
    }

    @Test
    public void testEqualsNotOkayWhenVirtuallityDiffers() {
        edgeForEqTests1.makeVirtual();
        assertFalse(edgeForEqTests1.equals(edgeForEqTests2));
    }

    @Test
    public void testEqualsNotOkWhenLeftCardinalitiesDiffer() {
        edgeForEqTests1.removeCardinalityLeft();
        assertFalse(edgeForEqTests1.equals(edgeForEqTests2));
    }

    @Test
    public void testEqualsNotOkWhenRightCardinalitiesDiffer() {
        edgeForEqTests1.setCardinalityRight(Cardinality.valueOf("1..5"));
        assertFalse(edgeForEqTests1.equals(edgeForEqTests2));
    }

    /**
     * Test the {@link Edge} constructor(s).
     * // TODO: there are several constructors; clean that up, and write a test for the ones that stay ....
     */
    @Test
    public void testConstructor() {
        Edge edge = new Edge(new Clazz("class1", "class1"), new Clazz("class2", "class2"), EdgeType.DEPENDENCY, "edge");
        Edge edge2 = edge.duplicate();

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

    @Test
    public void testIsSelfRef() {
        Edge edge = new Edge(new Clazz("A", "A"), new Clazz("A", "A"), EdgeType.INHERITANCE);
        assertTrue(edge.isSelfRef());
        edge = new Edge(new Clazz("A", "A"), new Clazz("B", "B"), EdgeType.INHERITANCE);
        assertFalse(edge.isSelfRef());
    }

}
