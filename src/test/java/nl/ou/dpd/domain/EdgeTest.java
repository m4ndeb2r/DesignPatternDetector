package nl.ou.dpd.domain;

import nl.ou.dpd.utils.TestHelper;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Tests the class {@link Edge}.
 *
 * TODO: write more tests to test setter methods and get/setVirtual?
 *
 * @author Martin de Boer
 */
public class EdgeTest {

    /**
     * Tests the {@link Edge#equals(Edge)} method explicitly. Implicitly the constructor and most
     * of the getter methods are tested as well.
     */
    @Test
    public void testEquals() {
        Edge edge1 = TestHelper.createEdge("class1", "class2", EdgeType.DEPENDENCY);
        Edge edge2 = new Edge(edge1);
        assertThat(edge1.equals(edge2), is(true));

        Edge edge3 = TestHelper.createEdge("class3", "class2", EdgeType.DEPENDENCY);
        assertThat(edge1.equals(edge3), is(false));

        Edge edge4 = TestHelper.createEdge("class1", "class3", EdgeType.DEPENDENCY);
        assertThat(edge1.equals(edge4), is(false));

        Edge edge5 = TestHelper.createEdge("class1", "class2", EdgeType.AGGREGATE);
        assertThat(edge1.equals(edge5), is(false));
    }

}
