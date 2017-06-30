package nl.ou.dpd.domain.matching;

import nl.ou.dpd.domain.SystemUnderConsideration;
import nl.ou.dpd.domain.node.Node;
import nl.ou.dpd.domain.relation.Relation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

/**
 * Tests the {@link Feedback} class.
 *
 * @author Martin de Boer
 */
@RunWith(MockitoJUnitRunner.class)
public class FeedbackTest {

    @Mock
    private Node node1, node2;

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

    /**
     * Tests the merging of multiple {@link Feedback} instances.
     */
    @Test
    public void testMerge() {
        final Feedback fb1 = new Feedback();
        final Feedback fb2 = new Feedback();

        fb1.addFeedbackMessage(node1, FeedbackType.INFO, "node1-msg");
        fb1.addFeedbackMessage(relation1, FeedbackType.INFO, "relation1-msg");
        fb2.addFeedbackMessage(node2, FeedbackType.INFO, "node2-msg");
        fb2.addFeedbackMessage(relation2, FeedbackType.MATCH, "relation2-msg");

        final Feedback fb3 = fb1.merge(fb2).merge(new Feedback()).merge(null);

        assertThat(fb3.getFeedbackMessages(node1, FeedbackType.NOT_ANALYSED).size(), is(0));
        assertThat(fb3.getFeedbackMessages(node1, FeedbackType.MATCH).size(), is(0));
        assertThat(fb3.getFeedbackMessages(node1, FeedbackType.MISMATCH).size(), is(0));
        assertThat(fb3.getFeedbackMessages(node1, FeedbackType.INFO).size(), is(1));
        assertThat(fb3.getFeedbackMessages(node1, FeedbackType.INFO).get(0), is("node1-msg"));

        assertThat(fb3.getFeedbackMessages(node2, FeedbackType.NOT_ANALYSED).size(), is(0));
        assertThat(fb3.getFeedbackMessages(node2, FeedbackType.MATCH).size(), is(0));
        assertThat(fb3.getFeedbackMessages(node2, FeedbackType.MISMATCH).size(), is(0));
        assertThat(fb3.getFeedbackMessages(node2, FeedbackType.INFO).size(), is(1));
        assertThat(fb3.getFeedbackMessages(node2, FeedbackType.INFO).get(0), is("node2-msg"));

        assertThat(fb3.getFeedbackMessages(relation1, FeedbackType.NOT_ANALYSED).size(), is(0));
        assertThat(fb3.getFeedbackMessages(relation1, FeedbackType.MATCH).size(), is(0));
        assertThat(fb3.getFeedbackMessages(relation1, FeedbackType.MISMATCH).size(), is(0));
        assertThat(fb3.getFeedbackMessages(relation1, FeedbackType.INFO).size(), is(1));
        assertThat(fb3.getFeedbackMessages(relation1, FeedbackType.INFO).iterator().next(), is("relation1-msg"));

        assertThat(fb3.getFeedbackMessages(relation2, FeedbackType.NOT_ANALYSED).size(), is(0));
        assertThat(fb3.getFeedbackMessages(relation2, FeedbackType.INFO).size(), is(0));
        assertThat(fb3.getFeedbackMessages(relation2, FeedbackType.MISMATCH).size(), is(0));
        assertThat(fb3.getFeedbackMessages(relation2, FeedbackType.MATCH).size(), is(1));
        assertThat(fb3.getFeedbackMessages(relation2, FeedbackType.MATCH).iterator().next(), is("relation2-msg"));

        assertThat(fb3.getFeedbackMessages((Relation)null, FeedbackType.INFO).size(), is(0));
        assertThat(fb3.getFeedbackMessages((Node)null, FeedbackType.INFO).size(), is(0));
    }

}
