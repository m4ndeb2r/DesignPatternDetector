package nl.ou.dpd.domain.matching;

import nl.ou.dpd.domain.relation.Cardinality;
import nl.ou.dpd.domain.relation.Relation;
import nl.ou.dpd.domain.relation.RelationProperty;
import nl.ou.dpd.domain.relation.RelationType;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * A factory for {@link FeedbackEnabledComparator}s used for matching {@link Relation}s of system designs with design
 * patterns.
 *
 * @author Martin de Boer
 */
public class RelationComparatorFactory {

    /**
     * Creates a {@link CompoundRelationComparator} containing all the available sub comparators for {@link Relation}s.
     *
     * @return the created {@link CompoundRelationComparator}.
     */
    public static CompoundComparator<Relation> createCompoundRelationComparator() {
        final CompoundRelationComparator compoundRelationComparator = new CompoundRelationComparator();
        compoundRelationComparator.addComparator(createRelationComparator());
        return compoundRelationComparator;
    }

    /**
     * Creates a {@link CompoundRelationComparator} containing the specified {@code subComparators}.
     *
     * @param subComparators the sub comparators to be contained in this {@link CompoundRelationComparator}
     * @return the created {@link CompoundRelationComparator}.
     */
    public static CompoundComparator<Relation> createCompoundRelationComparator(FeedbackEnabledComparator<Relation>... subComparators) {
        final CompoundRelationComparator compoundRelationComparator = new CompoundRelationComparator();
        for (FeedbackEnabledComparator<Relation> subComparator : subComparators) {
            compoundRelationComparator.addComparator(subComparator);
        }
        return compoundRelationComparator;
    }

    public static FeedbackEnabledComparator<Relation> createRelationComparator() {
        return new RelationComparator();
    }

    private static class CompoundRelationComparator extends CompoundComparator<Relation> {
        private final Feedback feedback = new Feedback();

        @Override
        public int compare(Relation systemRelation, Relation patternRelation) {
            final int result = super.compare(systemRelation, patternRelation);
            if (result == 0) {
                final String feedbackMsg = String.format("Matched with '%s'.", patternRelation.getName());
                feedback.addFeedbackMessage(systemRelation, FeedbackType.MATCH, feedbackMsg);
            } else {
                final String feedbackMsg = String.format("Match failed with '%s'.", patternRelation.getName());
                feedback.addFeedbackMessage(systemRelation, FeedbackType.MISMATCH, feedbackMsg);
            }
            return result;
        }

        @Override
        public Feedback getFeedback() {
            return super.getFeedback().merge(feedback);
        }

    }

    private static class RelationComparator implements FeedbackEnabledComparator<Relation> {
        private final Feedback feedback = new Feedback();

        @Override
        public Feedback getFeedback() {
            return feedback;
        }

        @Override
        public int compare(Relation systemRelation, Relation patternRelation) {
            int result = compareRelationTypes(systemRelation, patternRelation);
            if (result == 0) {
                result = compareCardinalities(systemRelation, patternRelation);
            }
            return result;
        }

        private int compareRelationTypes(Relation systemRelation, Relation patternRelation) {
            final Set<RelationType> sysTypes = systemRelation.getRelationProperties().stream()
                    .map(relationProperties -> relationProperties.getRelationType())
                    .collect(Collectors.toSet());
            final Set<RelationType> dpTypes = patternRelation.getRelationProperties().stream()
                    .map(relationProperties -> relationProperties.getRelationType())
                    .collect(Collectors.toSet());
            final Set<RelationType> sysDisjunction = getLeftDisjunction(sysTypes, dpTypes);
            final Set<RelationType> dpDisjunction = getLeftDisjunction(dpTypes, sysTypes);

            feedback.addFeedbackMessage(systemRelation, FeedbackType.INFO, "Relation type(s) analysed.");

            final int result = sysDisjunction.size() + dpDisjunction.size();
            if (result != 0) {
                createMismatchFeedback(systemRelation, patternRelation, sysDisjunction, dpDisjunction);
            }
            return result;
        }

        private void createMismatchFeedback(Relation systemRelation, Relation patternRelation, Set<RelationType> sysDisjunction, Set<RelationType> dpDisjunction) {
            sysDisjunction.forEach(relationType -> {
                final String feedbackMsg = String.format(
                        "Mismatch with '%s': unexpected relation type '%s' in relation '%s'.",
                        patternRelation.getName(),
                        relationType,
                        systemRelation.getName());
                feedback.addFeedbackMessage(systemRelation, FeedbackType.MISMATCH, feedbackMsg);
            });

            dpDisjunction.forEach(relationType -> {
                final String feedbackMsg = String.format(
                        "Mismatch with '%s': missing relation type '%s' in relation '%s'.",
                        patternRelation.getName(),
                        relationType,
                        systemRelation.getName());
                feedback.addFeedbackMessage(systemRelation, FeedbackType.MISMATCH, feedbackMsg);
            });
        }

        private Set<RelationType> getLeftDisjunction(Set<RelationType> set1, Set<RelationType> set2) {
            return set1.stream()
                    .filter(type -> !set2.contains(type))
                    .collect(Collectors.toSet());
        }

        private int compareCardinalities(Relation systemRelation, Relation patternRelation) {
            feedback.addFeedbackMessage(systemRelation, FeedbackType.INFO, "Cardinalities analysed.");

            for (RelationProperty patternRelationProperties : patternRelation.getRelationProperties()) {
                final RelationType patternRelationType = patternRelationProperties.getRelationType();
                final Cardinality patternCardinalityLeft = patternRelationProperties.getCardinalityLeft();
                final Cardinality patternCardinalityRight = patternRelationProperties.getCardinalityRight();
                for (RelationProperty systemRelationProperties : systemRelation.getRelationProperties()) {
                    final RelationType systemRelationType = systemRelationProperties.getRelationType();
                    final Cardinality systemCardinalityLeft = systemRelationProperties.getCardinalityLeft();
                    final Cardinality systemCardinalityRight = systemRelationProperties.getCardinalityRight();
                    if (systemRelationType == patternRelationType) {
                        if (!systemCardinalityLeft.isWithinLimitsOf(patternCardinalityLeft)) {
                            final String feedbackMsg = String.format(
                                    "Mismatch with '%s': unexpected left cardinality '%s' in relation '%s'.",
                                    patternRelation.getName(),
                                    systemCardinalityLeft.toString(),
                                    systemRelation.getName());
                            feedback.addFeedbackMessage(systemRelation, FeedbackType.MISMATCH, feedbackMsg);
                            return 1;
                        }
                        if (!systemCardinalityRight.isWithinLimitsOf(patternCardinalityRight)) {
                            final String feedbackMsg = String.format(
                                    "Mismatch with '%s': unexpected right cardinality '%s' in relation '%s'.",
                                    patternRelation.getName(),
                                    systemCardinalityLeft.toString(),
                                    systemRelation.getName());
                            feedback.addFeedbackMessage(systemRelation, FeedbackType.MISMATCH, feedbackMsg);
                            return 1;
                        }
                    }
                }
            }
            return 0;
        }

    }

}
