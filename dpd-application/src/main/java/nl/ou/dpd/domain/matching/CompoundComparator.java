package nl.ou.dpd.domain.matching;

import java.util.HashSet;
import java.util.Set;

/**
 * A {@link CompoundComparator} is a {@link FeedbackEnabledComparator} that itself contains multiple
 * {@link FeedbackEnabledComparator}s. The {@link CompoundComparator#compare(Object, Object)} returns the collective
 * result of the comparison by the inner comparators.
 *
 * @param <T> the subject type
 * @author Martin de Boer
 */
public abstract class CompoundComparator<T> implements FeedbackEnabledComparator<T> {

    protected static final String MATCH_FAILED_WITH_MSG = "Match failed with '%s'.";
    protected static final String MATCHED_WITH_MSG = "Matched with '%s'.";

    private Set<FeedbackEnabledComparator<T>> subComparators = new HashSet<>();

    public void addComparator(FeedbackEnabledComparator<T> subComparator) {
        subComparators.add(subComparator);
    }

    @Override
    public int compare(T systemObject, T patternObject) {
        return (int) subComparators.stream()
                .filter(subComparator -> subComparator.compare(systemObject, patternObject) != 0)
                .count();
    }

    @Override
    public Feedback getFeedback() {
        final Feedback feedback = new Feedback();
        subComparators.forEach(subComparator -> feedback.merge(subComparator.getFeedback()));
        return feedback;
    }
}

