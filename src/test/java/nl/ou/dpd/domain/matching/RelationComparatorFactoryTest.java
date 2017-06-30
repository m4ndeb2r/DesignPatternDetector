package nl.ou.dpd.domain.matching;

import nl.ou.dpd.domain.relation.Relation;
import nl.ou.dpd.domain.relation.RelationProperty;
import nl.ou.dpd.domain.relation.RelationType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashSet;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Tests the {@link RelationComparatorFactory} and its inner {@link FeedbackEnabledComparator<Relation>} classes.
 *
 * @author Martin de Boer
 */
@RunWith(MockitoJUnitRunner.class)
public class RelationComparatorFactoryTest {

    private CompoundComparator<Relation> customCompoundComparator, defaultCompoundComparator;

    @Mock
    private FeedbackEnabledComparator<Relation> subComparator1, subComparator2, subComparator3;
    @Mock
    private Relation dummyRelation1, dummyRelation2, inheritanceRelation, associationRelation;

    @Before
    public void initComparator() {
        customCompoundComparator = RelationComparatorFactory.createCompoundRelationComparator(subComparator1, subComparator2);
        defaultCompoundComparator = RelationComparatorFactory.createCompoundRelationComparator();
    }

    @Before
    public void initRelations() {
        // Init inheritanceRelation
        final HashSet<RelationProperty> relationProperties1 = new HashSet<>();
        final RelationProperty inheritance = new RelationProperty(RelationType.INHERITS_FROM);
        relationProperties1.add(inheritance);
        when(inheritanceRelation.getRelationProperties()).thenReturn(relationProperties1);
        when(inheritanceRelation.getId()).thenReturn("inheritanceRelation");
        when(inheritanceRelation.getName()).thenReturn("inheritanceRelation");

        // Init assocationRelation
        final HashSet<RelationProperty> relationProperties2 = new HashSet<>();
        final RelationProperty association = new RelationProperty(RelationType.ASSOCIATES_WITH);
        relationProperties2.add(association);
        when(associationRelation.getRelationProperties()).thenReturn(relationProperties2);
        when(associationRelation.getId()).thenReturn("associationRelation");
        when(associationRelation.getName()).thenReturn("associationRelation");
    }

    @Test
    public void testGetFeedbackForMatch() {
        defaultCompoundComparator.compare(inheritanceRelation, inheritanceRelation);
        Feedback feedback = defaultCompoundComparator.getFeedback();
        assertThat(feedback.getFeedbackMessages(inheritanceRelation, FeedbackType.MATCH).size(), is(1));
        assertThat(feedback.getFeedbackMessages(inheritanceRelation, FeedbackType.MISMATCH).size(), is(0));

        // Because there is a match, we expect all properties to be analysed (relation types as well as cardinalities)
        assertThat(feedback.getFeedbackMessages(inheritanceRelation, FeedbackType.INFO).size(), is(2));
        assertThat(feedback.getFeedbackMessages(inheritanceRelation, FeedbackType.INFO).get(0), is("Relation type(s) analysed."));
        assertThat(feedback.getFeedbackMessages(inheritanceRelation, FeedbackType.INFO).get(1), is("Cardinalities analysed."));
    }

    /**
     * Test the feedback when matching the relation type fails.
     */
    @Test
    public void testGetFeedbackForRelationTypeMismatch() {
        defaultCompoundComparator.compare(inheritanceRelation, associationRelation);
        Feedback feedback = defaultCompoundComparator.getFeedback();
        assertThat(feedback.getFeedbackMessages(inheritanceRelation, FeedbackType.MATCH).size(), is(0));
        assertThat(feedback.getFeedbackMessages(inheritanceRelation, FeedbackType.MISMATCH).size(), is(3));
        assertThat(feedback.getFeedbackMessages(inheritanceRelation, FeedbackType.MISMATCH).get(2), is("Match failed with 'associationRelation'."));

        // Because there is a mismatch in relation type, we do not expect cardinalities to be analysed (matching prematurely failed)
        assertThat(feedback.getFeedbackMessages(inheritanceRelation, FeedbackType.INFO).size(), is(1));
        assertThat(feedback.getFeedbackMessages(inheritanceRelation, FeedbackType.INFO).get(0), is("Relation type(s) analysed."));
    }

    /**
     * Test adding a comparator.
     */
    @Test
    public void testAddComparator() {
        when(subComparator1.compare(dummyRelation1, dummyRelation2)).thenReturn(0); // dummyRelation1 == dummyRelation2
        when(subComparator2.compare(dummyRelation1, dummyRelation2)).thenReturn(0); // dummyRelation1 == dummyRelation2
        when(subComparator3.compare(dummyRelation1, dummyRelation2)).thenReturn(1); // dummyRelation1 != dummyRelation2

        // Check the collective result of subComparator1 and subComparator2
        assertThat(customCompoundComparator.compare(dummyRelation1, dummyRelation2), is(0));

        // Add an extra subComparator
        customCompoundComparator.addComparator(subComparator3);

        // Check the collective result of subComparator1, subComparator2 and subComparator3
        assertThat(customCompoundComparator.compare(dummyRelation1, dummyRelation2), is(1));
    }
}
