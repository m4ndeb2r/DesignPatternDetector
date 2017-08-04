package nl.ou.dpd.domain.relation;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Tests the {@link RelationProperty} test.
 *
 * @author Peter Vansweevelt
 * @author Martin de Boer
 */
@RunWith(MockitoJUnitRunner.class)
public class RelationPropertyTest {

    /**
     * Exception rule.
     */
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Mock
    private Cardinality left_1, left_1_UNLIMITED, right_UNLIMITED, right_0_1;

    private Cardinality defaultCardinality;

    @Before
    public void initCardinalities() {
        when(left_1.toString()).thenReturn("1");
        when(left_1_UNLIMITED.toString()).thenReturn("1..*");
        when(right_0_1.toString()).thenReturn("0..1");
        when(right_UNLIMITED.toString()).thenReturn("*");
        defaultCardinality = Cardinality.valueOf("1");
    }

    @Test
    public void testConstructor() {
        RelationProperty relationProperty = new RelationProperty(RelationType.ASSOCIATES_WITH);
        assertThat(relationProperty.getRelationType(), is(RelationType.ASSOCIATES_WITH));
        assertThat(relationProperty.getCardinalityLeft(), is(defaultCardinality));
        assertThat(relationProperty.getCardinalityRight(), is(defaultCardinality));

        relationProperty = new RelationProperty(RelationType.ASSOCIATES_WITH, left_1_UNLIMITED, right_UNLIMITED);
        assertThat(relationProperty.getRelationType(), is(RelationType.ASSOCIATES_WITH));
        assertThat(relationProperty.getCardinalityLeft(), is(left_1_UNLIMITED));
        assertThat(relationProperty.getCardinalityRight(), is(right_UNLIMITED));
    }

    @Test
    public void testEquals() {
        RelationProperty rp1 = new RelationProperty(RelationType.ASSOCIATES_WITH);
        RelationProperty rp2 = new RelationProperty(RelationType.ASSOCIATES_WITH);

        assertEquals(rp1, rp1);
        assertEquals(rp1, rp2);
        assertEquals(rp1.hashCode(), rp2.hashCode());
        assertNotEquals(rp1, null);
        assertNotEquals(rp1, left_1_UNLIMITED);

        rp1.setCardinalityLeft(left_1_UNLIMITED);
        assertNotEquals(rp1, rp2);
        rp2.setCardinalityLeft(left_1_UNLIMITED);
        assertEquals(rp1, rp2);
        assertEquals(rp1.hashCode(), rp2.hashCode());

        rp2.setCardinalityRight(right_UNLIMITED);
        assertNotEquals(rp1, rp2);

        RelationProperty rp3 = new RelationProperty(RelationType.CREATES_INSTANCE_OF);
        rp3.setCardinalityLeft(left_1_UNLIMITED);
        assertNotEquals(rp1, rp3);
    }


}
