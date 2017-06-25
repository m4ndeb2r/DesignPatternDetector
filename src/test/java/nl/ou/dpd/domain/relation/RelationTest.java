package nl.ou.dpd.domain.relation;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Peter Vansweevelt
 */
public class RelationTest {

    @Test
    public void testConstructor() {
        Relation rel1 = new Relation("id1", "relation1");
        assertEquals("id1", rel1.getId());
        assertEquals("relation1", rel1.getName());
        rel1.setName("relation");
        assertEquals("relation", rel1.getName());
        assertEquals(0, rel1.getRelationProperties().size());

        RelationProperty rp1 = new RelationProperty(RelationType.ASSOCIATES_WITH);
        rel1.addRelationProperty(rp1);
        assertEquals(1, rel1.getRelationProperties().size());
        assertTrue(rel1.getRelationProperties().contains(rp1));
    }

    @Test
    public void testEquals() {
        Relation rel1 = new Relation("id1", "relation1");
        Relation rel2 = new Relation(null, "relation1");
        Relation rel3 = new Relation("id3", "relation1");

        assertNotEquals(rel1, rel2);
        rel2.setId("id1");

        assertEquals(rel1, rel1);
        assertEquals(rel1.hashCode(), rel1.hashCode());
        assertNotEquals(rel1, null);
        assertNotEquals(rel1, rel3);

        assertEquals(rel1, rel2);
        assertEquals(rel1.hashCode(), rel2.hashCode());
        rel2.setName("anotherName");
        assertNotEquals(rel1, rel2);
        rel2.setName(null);
        assertNotEquals(rel1, rel2);

        rel2.setName("relation1");
        RelationProperty rp1 = new RelationProperty(RelationType.ASSOCIATES_WITH);

        assertNotEquals(rel1, rp1);

        rel2.addRelationProperty(rp1);
        assertNotEquals(rel1, rel2);
        rel1.addRelationProperty(rp1);
        assertEquals(rel1, rel2);
        assertEquals(rel1.hashCode(), rel2.hashCode());
    }
}
