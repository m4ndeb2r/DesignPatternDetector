package nl.ou.dpd.domain.matching;

import nl.ou.dpd.domain.SystemUnderConsideration;
import nl.ou.dpd.domain.node.Node;
import nl.ou.dpd.domain.relation.Relation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Tests the {@link Feedback} class.
 *
 * @author Martin de Boer
 */
@RunWith(MockitoJUnitRunner.class)
public class FeedbackTest {

    @Mock
    private Node node1, node2, node3;

    @Mock
    private Relation relation1, relation2;

    @Mock
    private SystemUnderConsideration systemUnderConsideration;

    @Before
    public void initSystemUnderConsideration() {
        final Set<Relation> edgeSet = new HashSet<>();
        edgeSet.add(relation1);
        when(systemUnderConsideration.edgeSet()).thenReturn(edgeSet);
        when(systemUnderConsideration.getEdgeSource(relation1)).thenReturn(node1);
        when(systemUnderConsideration.getEdgeTarget(relation1)).thenReturn(node2);
    }

    @Test
    public void testConstructor() {
        final Feedback feedback = new Feedback(systemUnderConsideration);
        assertThat(feedback.getFeedbackMessages(relation1, FeedbackType.NOT_ANALYSED).size(), is(1));
        assertThat(feedback.getFeedbackMessages(node1, FeedbackType.NOT_ANALYSED).size(), is(1));
        assertThat(feedback.getFeedbackMessages(node2, FeedbackType.NOT_ANALYSED).size(), is(1));
    }

    @Test
    public void testGetNodeSet() {
        final Feedback feedback = new Feedback(systemUnderConsideration);
        assertThat(feedback.getNodeSet().size(), is(2));
        assertTrue(feedback.getNodeSet().contains(node1));
        assertTrue(feedback.getNodeSet().contains(node2));

        feedback.addFeedbackMessage(node2, FeedbackType.INFO, "Testing...");
        assertThat(feedback.getNodeSet().size(), is(2));
        assertTrue(feedback.getNodeSet().contains(node1));
        assertTrue(feedback.getNodeSet().contains(node2));

        feedback.addFeedbackMessage(node3, FeedbackType.MISMATCH, "Testing...");
        assertThat(feedback.getNodeSet().size(), is(3));
        assertTrue(feedback.getNodeSet().contains(node1));
        assertTrue(feedback.getNodeSet().contains(node2));
        assertTrue(feedback.getNodeSet().contains(node3));
    }

    @Test
    public void testGetRelationSet() {
        final Feedback feedback = new Feedback(systemUnderConsideration);
        assertThat(feedback.getRelationSet().size(), is(1));
        assertTrue(feedback.getRelationSet().contains(relation1));

        feedback.addFeedbackMessage(relation1, FeedbackType.INFO, "Testing...");
        assertThat(feedback.getRelationSet().size(), is(1));
        assertTrue(feedback.getRelationSet().contains(relation1));

        feedback.addFeedbackMessage(relation2, FeedbackType.INFO, "Testing...");
        assertThat(feedback.getRelationSet().size(), is(2));
        assertTrue(feedback.getRelationSet().contains(relation1));
        assertTrue(feedback.getRelationSet().contains(relation2));
    }

    /**
     * Tests the merging of multiple {@link Feedback} instances.
     */
    @Test
    public void testMerge() {
        final String aNote = "Note";
        final String infoMsg = "Info";
        final String matchMsg = "Match";
        final String misMatchMsg = "Mismatch";
        final String notAnalysedMsg = "Not analysed";

        final Feedback fb1 = new Feedback();
        fb1.addFeedbackMessage(node1, FeedbackType.NOT_ANALYSED, notAnalysedMsg);
        fb1.addFeedbackMessage(relation1, FeedbackType.MISMATCH, misMatchMsg);

        final Feedback fb2 = new Feedback();
        final Set<String> notes = new HashSet<>();
        notes.addAll(Arrays.asList(aNote));
        fb2.addNotes(notes);
        fb2.addFeedbackMessage(node2, FeedbackType.INFO, infoMsg);
        fb2.addFeedbackMessage(relation2, FeedbackType.MATCH, matchMsg);

        final Feedback fb3 = fb1.merge(fb2).merge(new Feedback()).merge(null);

        assertNotes(fb3, aNote);
        assertMessages(fb3, node1, FeedbackType.INFO, new String[]{});
        assertMessages(fb3, node1, FeedbackType.MATCH, new String[]{});
        assertMessages(fb3, node1, FeedbackType.MISMATCH, new String[]{});
        assertMessages(fb3, node1, FeedbackType.NOT_ANALYSED, new String[]{notAnalysedMsg});

        assertMessages(fb3, node2, FeedbackType.INFO, new String[]{infoMsg});
        assertMessages(fb3, node2, FeedbackType.MATCH, new String[]{});
        assertMessages(fb3, node2, FeedbackType.MISMATCH, new String[]{});
        assertMessages(fb3, node2, FeedbackType.NOT_ANALYSED, new String[]{});

        assertMessages(fb3, relation1, FeedbackType.INFO, new String[]{});
        assertMessages(fb3, relation1, FeedbackType.MATCH, new String[]{});
        assertMessages(fb3, relation1, FeedbackType.MISMATCH, new String[]{misMatchMsg});
        assertMessages(fb3, relation1, FeedbackType.NOT_ANALYSED, new String[]{});

        assertMessages(fb3, relation2, FeedbackType.INFO, new String[]{});
        assertMessages(fb3, relation2, FeedbackType.MATCH, new String[]{matchMsg});
        assertMessages(fb3, relation2, FeedbackType.MISMATCH, new String[]{});
        assertMessages(fb3, relation2, FeedbackType.NOT_ANALYSED, new String[]{});

        assertMessages(fb3, (Node)null, FeedbackType.INFO, new String[]{});
        assertMessages(fb3, (Relation)null, FeedbackType.INFO, new String[]{});
    }

    private void assertMessages(Feedback feedback, Node node, FeedbackType feedbackType, String... messages) {
        final List<String> feedbackMessages = feedback.getFeedbackMessages(node, feedbackType);
        assertThat(feedbackMessages.size(), is(messages.length));
        assertTrue(feedbackMessages.containsAll(Arrays.asList(messages)));
    }

    private void assertMessages(Feedback feedback, Relation relation, FeedbackType feedbackType, String... messages) {
        final List<String> feedbackMessages = feedback.getFeedbackMessages(relation, feedbackType);
        assertThat(feedbackMessages.size(), is(messages.length));
        assertTrue(feedbackMessages.containsAll(Arrays.asList(messages)));
    }

    private void assertNotes(Feedback feedback, String... notes) {
        final Set<String> feedbackNotes = feedback.getNotes();
        assertThat(feedbackNotes.size(), is(notes.length));
        final Iterator<String> feedbackIterator = feedbackNotes.iterator();
        for (int i = 0; i < notes.length; i++) {
            assertTrue(feedbackIterator.hasNext());
            assertThat(feedbackIterator.next(), is(notes[i]));
        }
    }

}
