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

        final SystemUnderConsiderationClass c1 = new SystemUnderConsiderationClass("c1");
        final SystemUnderConsiderationClass c2 = new SystemUnderConsiderationClass("c2");
        final SystemUnderConsiderationEdge edge = factory.create(c1, c2, EdgeType.DEPENDENCY);
        assertThat(edge.getClass1().getName(), is("c1"));
        assertThat(edge.getClass2().getName(), is("c2"));
        assertThat(edge.getTypeRelation(), is(EdgeType.DEPENDENCY));

        final SystemUnderConsiderationEdge duplicate = factory.create(edge);
        assertThat(duplicate.getClass1().getName(), is("c1"));
        assertThat(duplicate.getClass2().getName(), is("c2"));
        assertThat(duplicate.getTypeRelation(), is(EdgeType.DEPENDENCY));

        assertThat(edge == duplicate, is(false));
    }

}
