package nl.ou.dpd.domain;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Tests the class {@link DesignPatternEdge}.
 *
 * TODO: write more tests to test setter methods and get/setVirtual?
 *
 * @author Martin de Boer
 */
public class DesignPatternEdgeTest {

    /**
     * Tests the {@link DesignPatternEdge#equals(FourTuple)} method explicitly. Implicitly the constructor and most
     * of the getter methods are tested as well.
     */
    @Test
    public void testEquals() {
        DesignPatternEdge edge1 = new DesignPatternEdge("class1", "class2", EdgeType.DEPENDENCY);
        DesignPatternEdge edge2 = new DesignPatternEdge(edge1);
        assertThat(edge1.equals(edge2), is(true));

        DesignPatternEdge edge3 = new DesignPatternEdge("class3", "class2", EdgeType.DEPENDENCY);
        assertThat(edge1.equals(edge3), is(false));

        DesignPatternEdge edge4 = new DesignPatternEdge("class1", "class3", EdgeType.DEPENDENCY);
        assertThat(edge1.equals(edge4), is(false));

        DesignPatternEdge edge5 = new DesignPatternEdge("class1", "class2", EdgeType.AGGREGATE);
        assertThat(edge1.equals(edge5), is(false));
    }

}
