package nl.ou.dpd.domain.matching;

import java.util.Comparator;

public interface FeedbackEnabledComparator<T> extends Comparator<T> {

    Feedback getFeedback();

}
