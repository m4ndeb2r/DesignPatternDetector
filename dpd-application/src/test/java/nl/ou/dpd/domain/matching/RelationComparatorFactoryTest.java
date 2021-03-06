package nl.ou.dpd.domain.matching;

import nl.ou.dpd.domain.relation.Cardinality;
import nl.ou.dpd.domain.relation.Relation;
import nl.ou.dpd.domain.relation.RelationProperty;
import nl.ou.dpd.domain.relation.RelationType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashSet;

import static nl.ou.dpd.domain.matching.CompoundComparator.MATCHED_WITH_MSG;
import static nl.ou.dpd.domain.matching.CompoundComparator.MATCH_FAILED_WITH_MSG;
import static nl.ou.dpd.domain.matching.MatchingTestHelper.assertFeedbackMessages;
import static nl.ou.dpd.domain.matching.RelationComparatorFactory.CARDINALITIES_ANALYSED_MSG;
import static nl.ou.dpd.domain.matching.RelationComparatorFactory.MISMATCH_MISSING_RELATION_TYPE_MSG;
import static nl.ou.dpd.domain.matching.RelationComparatorFactory.MISMATCH_UNEXPECTED_LEFT_CARDINALITY_MSG;
import static nl.ou.dpd.domain.matching.RelationComparatorFactory.MISMATCH_UNEXPECTED_RIGHT_CARDINALITY_MSG;
import static nl.ou.dpd.domain.matching.RelationComparatorFactory.RELATION_TYPE_ANALYSED_MSG;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Tests the {@link RelationComparatorFactory} and its inner {@link FeedbackEnabledComparator} classes.
 *
 * @author Martin de Boer
 */
@RunWith(MockitoJUnitRunner.class)
public class RelationComparatorFactoryTest {

    private CompoundComparator<Relation> customCompoundComparator, defaultCompoundComparator;

    @Mock
    private FeedbackEnabledComparator<Relation> subComparator1, subComparator2, subComparator3;
    @Mock
    private Relation dummyRelation1, dummyRelation2,
            inheritanceRelation, inheritance2Relation,
            inheritance3Relation, associationRelation;

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
        inheritance.setCardinalityLeft(Cardinality.valueOf("1"));
        inheritance.setCardinalityRight(Cardinality.valueOf("1"));
        relationProperties1.add(inheritance);
        when(inheritanceRelation.getRelationProperties()).thenReturn(relationProperties1);
        when(inheritanceRelation.getId()).thenReturn("inheritanceRelation");
        when(inheritanceRelation.getName()).thenReturn("inheritanceRelation");

        // Init inheritance2Relation
        final HashSet<RelationProperty> relationProperties2 = new HashSet<>();
        final RelationProperty inheritance2 = new RelationProperty(RelationType.INHERITS_FROM);
        inheritance2.setCardinalityLeft(Cardinality.valueOf("1"));
        inheritance2.setCardinalityRight(Cardinality.valueOf("2"));
        relationProperties2.add(inheritance2);
        when(inheritance2Relation.getRelationProperties()).thenReturn(relationProperties2);
        when(inheritance2Relation.getId()).thenReturn("inheritance2Relation");
        when(inheritance2Relation.getName()).thenReturn("inheritance2Relation");

        // Init inheritance3Relation
        final HashSet<RelationProperty> relationProperties3 = new HashSet<>();
        final RelationProperty inheritance3 = new RelationProperty(RelationType.INHERITS_FROM);
        inheritance3.setCardinalityLeft(Cardinality.valueOf("2"));
        inheritance3.setCardinalityRight(Cardinality.valueOf("1"));
        relationProperties3.add(inheritance3);
        when(inheritance3Relation.getRelationProperties()).thenReturn(relationProperties3);
        when(inheritance3Relation.getId()).thenReturn("inheritance3Relation");
        when(inheritance3Relation.getName()).thenReturn("inheritance3Relation");

        // Init assocationRelation
        final HashSet<RelationProperty> relationProperties4 = new HashSet<>();
        final RelationProperty association = new RelationProperty(RelationType.ASSOCIATES_WITH);
        relationProperties4.add(association);
        association.setCardinalityLeft(Cardinality.valueOf("1"));
        association.setCardinalityRight(Cardinality.valueOf("1"));
        when(associationRelation.getRelationProperties()).thenReturn(relationProperties4);
        when(associationRelation.getId()).thenReturn("associationRelation");
        when(associationRelation.getName()).thenReturn("associationRelation");
    }

    /**
     * Test the behaviour of a compound comparator and its feedback when two relations match.
     */
    @Test
    public void testGetFeedbackForMatch() {
        defaultCompoundComparator.compare(inheritanceRelation, inheritanceRelation);
        Feedback feedback = defaultCompoundComparator.getFeedback();
        assertFeedbackMessages(feedback, inheritanceRelation, FeedbackType.MISMATCH, new String[]{});
        assertFeedbackMessages(feedback, inheritanceRelation, FeedbackType.MATCH, new String[]{
                String.format(
                        MATCHED_WITH_MSG,
                        inheritanceRelation.getName())
        });
        assertFeedbackMessages(feedback, inheritanceRelation, FeedbackType.INFO, new String[]{
                RELATION_TYPE_ANALYSED_MSG,
                CARDINALITIES_ANALYSED_MSG
        });
    }

    /**
     * Test the behaviour of a compound comparator and its feedback when matching the cardinality (right) fails.
     */
    @Test
    public void testGetFeedbackForLeftCardinalityMismatch() {
        defaultCompoundComparator.compare(inheritanceRelation, inheritance2Relation);
        Feedback feedback = defaultCompoundComparator.getFeedback();
        assertFeedbackMessages(feedback, inheritanceRelation, FeedbackType.MATCH, new String[]{});
        assertFeedbackMessages(feedback, inheritanceRelation, FeedbackType.MISMATCH, new String[]{
                String.format(
                        MATCH_FAILED_WITH_MSG,
                        inheritance2Relation.getName()),
                String.format(
                        MISMATCH_UNEXPECTED_RIGHT_CARDINALITY_MSG,
                        inheritance2Relation.getName(),
                        "1",
                        inheritanceRelation.getName())
        });
        assertFeedbackMessages(feedback, inheritanceRelation, FeedbackType.INFO, new String[]{
                RELATION_TYPE_ANALYSED_MSG,
                CARDINALITIES_ANALYSED_MSG
        });
    }

    /**
     * Test the behaviour of a compound comparator and its feedback when matching the cardinality (left) fails.
     */
    @Test
    public void testGetFeedbackForRightCardinalityMismatch() {
        defaultCompoundComparator.compare(inheritanceRelation, inheritance3Relation);
        Feedback feedback = defaultCompoundComparator.getFeedback();
        assertFeedbackMessages(feedback, inheritanceRelation, FeedbackType.MATCH, new String[]{});
        assertFeedbackMessages(feedback, inheritanceRelation, FeedbackType.MISMATCH, new String[]{
                String.format(
                        MATCH_FAILED_WITH_MSG,
                        inheritance3Relation.getName()),
                String.format(
                        MISMATCH_UNEXPECTED_LEFT_CARDINALITY_MSG,
                        inheritance3Relation.getName(),
                        "1",
                        inheritanceRelation.getName())
        });
        assertFeedbackMessages(feedback, inheritanceRelation, FeedbackType.INFO, new String[]{
                RELATION_TYPE_ANALYSED_MSG,
                CARDINALITIES_ANALYSED_MSG
        });
    }

    /**
     * Test the behaviour of a compound comparator and its feedback when matching the relation type fails.
     */
    @Test
    public void testGetFeedbackForRelationTypeMismatch() {
        defaultCompoundComparator.compare(inheritanceRelation, associationRelation);
        Feedback feedback = defaultCompoundComparator.getFeedback();
        assertFeedbackMessages(feedback, inheritanceRelation, FeedbackType.MATCH, new String[]{});
        assertFeedbackMessages(feedback, inheritanceRelation, FeedbackType.MISMATCH, new String[]{
                String.format(
                        MATCH_FAILED_WITH_MSG,
                        associationRelation.getName()),
                String.format(
                        MISMATCH_MISSING_RELATION_TYPE_MSG,
                        associationRelation.getName(),
                        RelationType.ASSOCIATES_WITH.name(),
                        inheritanceRelation.getName())
        });

        // Because there is a mismatch in relation type, we do not expect cardinalities to be analysed (matching prematurely failed)
        assertFeedbackMessages(feedback, inheritanceRelation, FeedbackType.INFO, new String[]{
                RELATION_TYPE_ANALYSED_MSG
        });
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
