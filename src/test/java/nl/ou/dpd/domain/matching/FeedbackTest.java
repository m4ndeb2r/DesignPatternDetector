package nl.ou.dpd.domain.matching;

import nl.ou.dpd.domain.node.Node;
import nl.ou.dpd.domain.node.NodeType;
import nl.ou.dpd.domain.relation.Relation;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class FeedbackTest {

    @Test
    public void testMerge() {
        final Feedback fb1 = new Feedback();
        final Feedback fb2 = new Feedback();
        final Node node1 = new Node("1", "node1");
        node1.addType(NodeType.CONCRETE_CLASS);
        final Node node2 = new Node("2", "node2");
        node2.addType(NodeType.CONCRETE_CLASS);
        final Relation relation1 = new Relation("1", "relation1");
        final Relation relation2 = new Relation("2", "relation2");

        fb1.addFeedbackMessage(node1, FeedbackType.INFO, "node1-msg");
        fb1.addFeedbackMessage(relation1, FeedbackType.INFO, "relation1-msg");
        fb2.addFeedbackMessage(node2, FeedbackType.INFO, "node2-msg");
        fb2.addFeedbackMessage(relation2, FeedbackType.MATCH, "relation2-msg");

        final Feedback fb3 = new Feedback();
        fb3.merge(fb1).merge(fb2);

        assertThat(fb3.getFeedbackMessages(node1, FeedbackType.INFO).get(0), is("node1-msg"));
        assertThat(fb3.getFeedbackMessages(node2, FeedbackType.INFO).get(0), is("node2-msg"));
        assertThat(fb3.getFeedbackMessages(relation1, FeedbackType.INFO).iterator().next(), is("relation1-msg"));
        assertThat(fb3.getFeedbackMessages(relation2, FeedbackType.MATCH).iterator().next(), is("relation2-msg"));
    }
}
