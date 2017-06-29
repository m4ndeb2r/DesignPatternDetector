package nl.ou.dpd.domain.matching;

import java.util.Comparator;

/**
 * A {@link FeedbackEnabledComparator} is a {@link Comparator} that is {@link FeedbackEnabled}, meaning, it can provide
 * feedback after comparison. Feedback is provided by the {@link #getFeedback()} method.
 *
 * {@link FeedbackEnabledComparator}s are the engines that drive the matching process of the application, and at the
 * same time gather and provide feedback information about the mathing process.
 *
 * @param <T> the subject type of this comparator (the type of the elements that are matched)
 * @author Martin de Boer
 */
public interface FeedbackEnabledComparator<T> extends Comparator<T>, FeedbackEnabled {

}
