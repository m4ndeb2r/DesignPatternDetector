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
        final RelationType[] relationTypes = new RelationType[]{
                RelationType.ASSOCIATES_WITH,
                RelationType.CALLS_METHOD_OF,
                RelationType.IMPLEMENTS
        };
        final Cardinality[] leftCardinalities = new Cardinality[]{left_1, left_1_UNLIMITED};
        final Cardinality[] rightCardinalities = new Cardinality[]{right_0_1, right_UNLIMITED};

        final RelationProperty rp1 = new RelationProperty(RelationType.IMPLEMENTS, left_1_UNLIMITED, right_0_1);
        assertEquals(rp1, rp1);
        assertNotEquals(rp1, null);

        for (RelationType relationType : relationTypes) {
            for (Cardinality leftCardinality : leftCardinalities) {
                for (Cardinality rightCardinality : rightCardinalities) {
                    final RelationProperty rp2 = new RelationProperty(relationType);
                    rp2.setCardinalityLeft(leftCardinality);
                    rp2.setCardinalityRight(rightCardinality);
                    if (rp1.getRelationType().equals(rp2.getRelationType()) &&
                            rp1.getCardinalityLeft().equals(rp2.getCardinalityLeft()) &&
                            rp1.getCardinalityRight().equals(rp2.getCardinalityRight())) {
                        assertEquals(rp1, rp2);
                        assertEquals(rp1.hashCode(), rp2.hashCode());
                    } else {
                        assertNotEquals(rp1, rp2);
                    }
                }
            }
        }
    }


}
