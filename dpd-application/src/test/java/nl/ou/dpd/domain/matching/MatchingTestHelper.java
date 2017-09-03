package nl.ou.dpd.domain.matching;

import nl.ou.dpd.domain.node.Node;
import nl.ou.dpd.domain.relation.Relation;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertTrue;

/**
 * A helper class for test classes in the matching package.
 *
 * @author Martin de Boer
 */
public class MatchingTestHelper {

    /**
     * Private constructor because this is a utility class that cannot be instantiated
     */
    private MatchingTestHelper() {
    }

    /**
     * A package protected helper method that checks if the specified {@code messages} with the specified
     * {@code feedbackType} are present in the specified {@code feedback} for the specified {@code node}.
     *
     * @param feedback     the {@link Feedback} to check
     * @param node         the {@link Node} to look for
     * @param feedbackType the {@link FeedbackType} we're interested in
     * @param messages     the {@link messages} we expect to be present
     */
    static void assertFeedbackMessages(Feedback feedback, Node node, FeedbackType feedbackType, String... messages) {
        final List<String> feedbackMessages = feedback.getFeedbackMessages(node, feedbackType);
        assertThat(feedbackMessages.size(), is(messages.length));
        assertTrue(feedbackMessages.containsAll(Arrays.asList(messages)));
    }

    /**
     * A package protected helper method that checks if the specified {@code messages} with the specified
     * {@code feedbackType} are present in the specified {@code feedback} for the specified {@code relation}.
     *
     * @param feedback     the {@link Feedback} to check
     * @param relation     the {@link Relation} to look for
     * @param feedbackType the {@link FeedbackType} we're interested in
     * @param messages     the {@link messages} we expect to be present
     */
    static void assertFeedbackMessages(Feedback feedback, Relation relation, FeedbackType feedbackType, String... messages) {
        final List<String> feedbackMessages = feedback.getFeedbackMessages(relation, feedbackType);
        assertThat(feedbackMessages.size(), is(messages.length));
        assertTrue(feedbackMessages.containsAll(Arrays.asList(messages)));
    }


}
