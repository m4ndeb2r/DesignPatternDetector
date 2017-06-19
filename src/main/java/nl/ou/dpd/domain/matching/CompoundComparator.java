package nl.ou.dpd.domain.matching;

import java.util.HashSet;
import java.util.Set;

public abstract class CompoundComparator<T> implements FeedbackEnabledComparator<T> {

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

