package nl.ou.dpd.domain.matching;

/**
 * The types of feedback that are possible during the matching process. The {@link FeedbackType} is related to the
 * "severity" of the feedback messages.
 *
 * @author Martin de Boer
 */
public enum FeedbackType {
    INFO, MATCH, NOT_ANALYSED, MISMATCH
}
