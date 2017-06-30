package nl.ou.dpd.domain.matching;

import nl.ou.dpd.domain.relation.Relation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Tests the {@link RelationComparatorFactory} and {@link FeedbackEnabledComparator<Relation>}class.
 *
 * @author Martin de Boer
 */
@RunWith(MockitoJUnitRunner.class)
public class RelationComparatorFactoryTest {

    private CompoundComparator<Relation> compoundComparator;

    @Mock
    private FeedbackEnabledComparator<Relation> subComparator1, subComparator2, subComparator3;
    @Mock
    private Relation relation1, relation2;

    @Before
    public void initComparator() {
        compoundComparator = RelationComparatorFactory.createCompoundRelationComparator(subComparator1, subComparator2);
    }

    @Test
    public void testAddComparator() {
        when(subComparator1.compare(relation1, relation2)).thenReturn(0); // relation1 == relation2
        when(subComparator2.compare(relation1, relation2)).thenReturn(0); // relation1 == relation2
        when(subComparator3.compare(relation1, relation2)).thenReturn(1); // relation1 != relation2

        // Check the collective result of subComparator1 and subComparator2
        assertThat(compoundComparator.compare(relation1, relation2), is(0));

        // Add an extra subComparator
        compoundComparator.addComparator(subComparator3);

        // Check the collective result of subComparator1, subComparator2 and subComparator3
        assertThat(compoundComparator.compare(relation1, relation2), is(1));
    }
}
