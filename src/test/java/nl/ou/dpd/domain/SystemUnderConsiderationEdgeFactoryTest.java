package nl.ou.dpd.domain;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Tests the {@link SystemUnderConsiderationEdgeFactory} class.
 *
 * @author Martin de Boer
 */
public class SystemUnderConsiderationEdgeFactoryTest {

    /**
     * Tests the create methods of the {@link SystemUnderConsiderationEdgeFactory}.
     */
    @Test
    public void testCreate() {
        final SystemUnderConsiderationEdgeFactory factory = new SystemUnderConsiderationEdgeFactory();

        final SystemUnderConsiderationEdge edge = factory.create("c1", "c2", EdgeType.DEPENDENCY);
        assertThat(edge.getClassName1(), is("c1"));
        assertThat(edge.getClassName2(), is("c2"));
        assertThat(edge.getTypeRelation(), is(EdgeType.DEPENDENCY));

        final SystemUnderConsiderationEdge duplicate = factory.create(edge);
        assertThat(duplicate.getClassName1(), is("c1"));
        assertThat(duplicate.getClassName2(), is("c2"));
        assertThat(duplicate.getTypeRelation(), is(EdgeType.DEPENDENCY));

        assertThat(edge == duplicate, is(false));
    }

}
