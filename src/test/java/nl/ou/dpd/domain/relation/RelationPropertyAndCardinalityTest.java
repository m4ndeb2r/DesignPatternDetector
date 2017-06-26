package nl.ou.dpd.domain.relation;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Peter Vansweevelt
 */
public class RelationPropertyAndCardinalityTest {

    /**
     * Exception rule.
     */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testConstructor() {
        RelationProperty rp1 = new RelationProperty(RelationType.ASSOCIATES_WITH);
        Cardinality card1 = new Cardinality(1, 1);
        assertEquals(1, card1.getLower());
        assertEquals(1, card1.getUpper());
        assertEquals("1", card1.toString());

        Cardinality card2 = new Cardinality(0, -1);
        assertEquals(0, card2.getLower());
        assertEquals(Cardinality.UNLIMITED, card2.getUpper());
        assertEquals("*", card2.toString());

        rp1.setCardinalityLeft(card1);
        rp1.setCardinalityRight(card2);
        assertEquals(1, rp1.getCardinalityLeft().getLower());
        assertEquals(1, rp1.getCardinalityLeft().getUpper());
        assertEquals(0, rp1.getCardinalityRight().getLower());
        assertEquals(-1, rp1.getCardinalityRight().getUpper());
        assertEquals(RelationType.ASSOCIATES_WITH, rp1.getRelationType());
        assertEquals("[ relation type = 'ASSOCIATES_WITH', cardinality left = '1', cardinality right = '*' ]", rp1.toString());

        RelationProperty rp2 = new RelationProperty(RelationType.CALLS_METHOD_OF, card1, card2);
        assertEquals(1, rp2.getCardinalityLeft().getLower());
        assertEquals(1, rp2.getCardinalityLeft().getUpper());
        assertEquals(0, rp2.getCardinalityRight().getLower());
        assertEquals(-1, rp2.getCardinalityRight().getUpper());
        assertEquals(RelationType.CALLS_METHOD_OF, rp2.getRelationType());
        assertEquals("[ relation type = 'CALLS_METHOD_OF', cardinality left = '1', cardinality right = '*' ]", rp2.toString());
    }

    @Test
    public void testEquals() {
        RelationProperty rp1 = new RelationProperty(RelationType.ASSOCIATES_WITH);
        RelationProperty rp2 = new RelationProperty(RelationType.ASSOCIATES_WITH);
        Cardinality card1 = new Cardinality(1, Cardinality.UNLIMITED);

        assertEquals(rp1, rp1);
        assertEquals(rp1, rp2);
        assertEquals(rp1.hashCode(), rp2.hashCode());
        assertNotEquals(rp1, null);
        assertNotEquals(rp1, card1);

        rp1.setCardinalityLeft(card1);
        assertNotEquals(rp1, rp2);
        rp2.setCardinalityLeft(new Cardinality(1, -1));
        assertEquals(rp1, rp2);
        assertEquals(rp1.hashCode(), rp2.hashCode());
        rp2.setCardinalityRight(new Cardinality(0, 1));
        assertNotEquals(rp1, rp2);

        RelationProperty rp3 = new RelationProperty(RelationType.CREATES_INSTANCE_OF);
        rp3.setCardinalityLeft(card1);
        assertNotEquals(rp1, rp3);
    }

    @Test
    public void testCardinality() {

        Cardinality card1 = new Cardinality(1, 1);
        Cardinality card2 = new Cardinality(0, Cardinality.UNLIMITED);
        Cardinality card3 = new Cardinality(5, 10);
        Cardinality card4 = new Cardinality(1, 10);

        assertTrue(card1.isWithinLimitsOf(card2));
        assertTrue(card3.isWithinLimitsOf(card2));
        assertTrue(card2.isWithinLimitsOf(card2));
        assertFalse(card2.isWithinLimitsOf(card1));
        assertFalse(card1.isWithinLimitsOf(card3));
        assertTrue(card1.isWithinLimitsOf(card4));
        assertFalse(card4.isWithinLimitsOf(card1));

        assertEquals(card1, Cardinality.valueOf("1,1"));
        assertEquals(card1, Cardinality.valueOf("1..1"));
        assertEquals(card1, Cardinality.valueOf("1"));
        assertEquals(card2, Cardinality.valueOf("0..*"));
        assertEquals(card2, Cardinality.valueOf("0,-1"));
        assertEquals(card2, Cardinality.valueOf("*"));
        assertEquals(card3, Cardinality.valueOf("5,10"));
        assertEquals(card3, Cardinality.valueOf("5..10"));

        assertEquals(card3, card3);
        assertNotEquals(card3, null);
        assertNotEquals(card3, new RelationProperty(RelationType.DEPENDS_ON));
    }

    @Test
    public void testValueOfException() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Illegal cardinality value: 'a..z'.");

        Cardinality.valueOf("a..z");
    }

    @Test
    public void testValueOfSeparatorException() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Illegal cardinality value: '1/5'.");

        Cardinality.valueOf("1/5");
    }

    @Test
    public void testValueOfNumberOfIntegersException() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Illegal cardinality value: '1,2,5'.");

        Cardinality.valueOf("1,2,5");
    }

    @Test
    public void testCardinalityUpperBoundException() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Upperbound value must be >= lowerbound value.");

        Cardinality card2 = new Cardinality(5, 1);
    }

    @Test
    public void testUnlimitedLowerException() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Unlimited value not allowed for lowerbound value.");

        Cardinality card2 = new Cardinality(-1, 55);
    }

    @Test
    public void testNegativeValueException() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("No negative values allowed in cardinality.");

        Cardinality card2 = new Cardinality(-6, 3);
    }
}
