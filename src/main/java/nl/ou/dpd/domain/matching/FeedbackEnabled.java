package nl.ou.dpd.domain.matching;

/**
 * A {@link FeedbackEnabled} is an object that is enabled to provide feedback. Feedback is provided by the
 * {@link #getFeedback()} method.
 *
 * @author Martin de Boer
 */
public interface FeedbackEnabled {

    /**
     * Returns feedback after processing whatever the core business of a object is.
     *
     * @return A {@link Feedback} object containing the gathered feedback info.
     */
    Feedback getFeedback();

}
