package nl.ou.dpd.domain.relation;

import nl.ou.dpd.domain.node.Node;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

/**
 * Tests the {@link RelationFactory} class.
 *
 * @author Martin de Boer
 */
@RunWith(MockitoJUnitRunner.class)
public class RelationFactoryTest {

    @Mock
    Node leftNode, rightNode;

    @Before
    public void setUp() {
        when(leftNode.getId()).thenReturn("left");
        when(rightNode.getId()).thenReturn("right");
        when(leftNode.getName()).thenReturn("Left");
        when(rightNode.getName()).thenReturn("Right");
    }

    @Test
    public void testMethod() {
        final Relation relation = new RelationFactory().createEdge(leftNode, rightNode);
        assertThat(relation.getId(), is("left-right"));
        assertThat(relation.getName(), is("Left-Right"));
    }
}
