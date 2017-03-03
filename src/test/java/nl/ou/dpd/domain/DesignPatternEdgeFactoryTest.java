package nl.ou.dpd.domain;

import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Tests the {@link DesignPatternEdgeFactory} class.
 *
 * @author Martin de Boer
 */
public class DesignPatternEdgeFactoryTest {

    /**
     * Tests the create methods of the {@link DesignPatternEdgeFactory}.
     */
    @Test
    public void testCreate() {
        final DesignPatternEdgeFactory factory = new DesignPatternEdgeFactory();

        final DesignPatternClass c1 = new DesignPatternClass("c1");
        final DesignPatternClass c2 = new DesignPatternClass("c2");
        final DesignPatternEdge edge = factory.create(c1, c2, EdgeType.AGGREGATE);
        assertThat(edge.getClass1().getName(), is("c1"));
        assertThat(edge.getClass2().getName(), is("c2"));
        assertThat(edge.getTypeRelation(), is(EdgeType.AGGREGATE));

        final DesignPatternEdge duplicate = factory.create(edge);
        assertThat(duplicate.getClass1().getName(), is("c1"));
        assertThat(duplicate.getClass2().getName(), is("c2"));
        assertThat(duplicate.getTypeRelation(), is(EdgeType.AGGREGATE));

        assertThat(edge == duplicate, is(false));
    }

}
