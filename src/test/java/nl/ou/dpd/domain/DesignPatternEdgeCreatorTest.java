package nl.ou.dpd.domain;

import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Tests the {@link DesignPatternEdgeCreator} class.
 *
 * @author Martin de Boer
 */
public class DesignPatternEdgeCreatorTest {

    /**
     * Tests the create methods of the {@link DesignPatternEdgeCreator}.
     */
    @Test
    public void testCreate() {
        final DesignPatternEdgeCreator creator = new DesignPatternEdgeCreator();

        final DesignPatternEdge edge = creator.create("c1", "c2", EdgeType.AGGREGATE);
        assertThat(edge.getClassName1(), is("c1"));
        assertThat(edge.getClassName2(), is("c2"));
        assertThat(edge.getTypeRelation(), is(EdgeType.AGGREGATE));

        final DesignPatternEdge duplicate = creator.create(edge);
        assertThat(duplicate.getClassName1(), is("c1"));
        assertThat(duplicate.getClassName2(), is("c2"));
        assertThat(duplicate.getTypeRelation(), is(EdgeType.AGGREGATE));

        assertThat(edge == duplicate, is(false));
    }

}
