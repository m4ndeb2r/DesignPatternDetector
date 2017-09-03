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

    // The test subject
    private Relation relation;

    @Before
    public void initRelationProperty() {
        final Cardinality cardinality_1 = Cardinality.valueOf("1");
        when(relationProperty.getRelationType()).thenReturn(RelationType.ASSOCIATES_WITH);
        when(relationProperty.getCardinalityLeft()).thenReturn(cardinality_1);
        when(relationProperty.getCardinalityRight()).thenReturn(cardinality_1);
    }

    @Before
    public void initTestSubject() {
        relation = new Relation("id1", "relation1");
    }

    @Test
    public void testConstructor() {
        assertThat(relation.getId(), is("id1"));
        assertThat(relation.getName(), is("relation1"));
        assertTrue(relation.getRelationProperties().isEmpty());
    }

    @Test
    public void testAddRelationProperty() {
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
        final Relation relation2 = new Relation(null, relation.getName());

        assertEquals(relation, relation);
        assertEquals(relation.hashCode(), relation.hashCode());
        assertNotEquals(relation, null);
        assertNotEquals(relation, relationProperty);
        assertNotEquals(relation, relation2);

        relation2.setId(relation.getId());
        assertEquals(relation, relation2);
        assertEquals(relation.hashCode(), relation2.hashCode());

        relation2.setName(String.format("Not %s", relation2.getName()));
        assertNotEquals(relation, relation2);

        relation2.setName(null);
        assertNotEquals(relation, relation2);

        relation2.setName(relation.getName());
        relation2.addRelationProperty(relationProperty);
        assertNotEquals(relation, relation2);

        relation.addRelationProperty(relationProperty);
        assertEquals(relation, relation2);
        assertEquals(relation.hashCode(), relation2.hashCode());
    }
}
