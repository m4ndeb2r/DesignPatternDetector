package nl.ou.dpd.domain.relation;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Tests the {@link Relation} class.
 *
 * @author Peter Vansweevelt
 * @author Martin de Boer
 */
@RunWith(MockitoJUnitRunner.class)
public class RelationTest {

    @Mock
    private RelationProperty relationProperty;

    @Before
    public void initRelationProperty() {
        when(relationProperty.getRelationType()).thenReturn(RelationType.ASSOCIATES_WITH);
        when(relationProperty.getCardinalityLeft()).thenReturn(Cardinality.valueOf("1"));
        when(relationProperty.getCardinalityRight()).thenReturn(Cardinality.valueOf("1"));
    }

    @Test
    public void testConstructor() {
        final Relation relation = new Relation("id1", "relation1");
        assertThat(relation.getId(), is("id1"));
        assertThat(relation.getName(), is("relation1"));
        assertThat(relation.getRelationProperties().size(), is(0));
    }

    @Test
    public void testAddRelationProperty() {
        final Relation relation = new Relation("id1", "relation1");
        relation.addRelationProperty(relationProperty);
        assertEquals(1, relation.getRelationProperties().size());
        assertTrue(relation.getRelationProperties().contains(relationProperty));

        // No doubles allowed: adding it again, and expect no changes
        relation.addRelationProperty(relationProperty);
        assertEquals(1, relation.getRelationProperties().size());
        assertTrue(relation.getRelationProperties().contains(relationProperty));
    }

    @Test
    public void testEquals() {
        Relation rel1 = new Relation("id1", "relation1");
        Relation rel2 = new Relation(null, "relation1");

        assertEquals(rel1, rel1);
        assertEquals(rel1.hashCode(), rel1.hashCode());
        assertNotEquals(rel1, null);
        assertNotEquals(rel1, relationProperty);
        assertNotEquals(rel1, rel2);

        rel2.setId("id1");
        assertEquals(rel1, rel2);
        assertEquals(rel1.hashCode(), rel2.hashCode());

        rel2.setName("anotherName");
        assertNotEquals(rel1, rel2);

        rel2.setName(null);
        assertNotEquals(rel1, rel2);

        rel2.setName("relation1");
        rel2.addRelationProperty(relationProperty);
        assertNotEquals(rel1, rel2);

        rel1.addRelationProperty(relationProperty);
        assertEquals(rel1, rel2);
        assertEquals(rel1.hashCode(), rel2.hashCode());
    }
}
