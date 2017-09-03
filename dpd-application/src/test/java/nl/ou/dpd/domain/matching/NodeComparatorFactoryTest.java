package nl.ou.dpd.domain.matching;

import nl.ou.dpd.domain.node.Node;
import nl.ou.dpd.domain.node.NodeType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashSet;

import static nl.ou.dpd.domain.matching.CompoundComparator.MATCHED_WITH_MSG;
import static nl.ou.dpd.domain.matching.CompoundComparator.MATCH_FAILED_WITH_MSG;
import static nl.ou.dpd.domain.matching.MatchingTestHelper.assertFeedbackMessages;
import static nl.ou.dpd.domain.matching.NodeComparatorFactory.MISMATCH_MISSING_NODE_TYPE_MSG;
import static nl.ou.dpd.domain.matching.NodeComparatorFactory.NODE_TYPES_ANALYSED_MSG;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Tests the {@link NodeComparatorFactory} and its inner {@link FeedbackEnabledComparator} classes.
 *
 * @author Martin de Boer
 */
@RunWith(MockitoJUnitRunner.class)
public class NodeComparatorFactoryTest {

    private CompoundComparator<Node> customCompoundComparator, defaultCompoundComparator;

    @Mock
    private FeedbackEnabledComparator<Node> subComparator1, subComparator2, subComparator3;
    @Mock
    private Node dummyNode1, dummyNode2, concreteClassNode, abstractClassNode;

    @Before
    public void initComparator() {
        customCompoundComparator = NodeComparatorFactory.createCompoundNodeComparator(subComparator1, subComparator2);
        defaultCompoundComparator = NodeComparatorFactory.createCompoundNodeComparator();
    }

    @Before
    public void initNodes() {
        // Init concreteClassNode
        final HashSet<NodeType> types1 = new HashSet<>();
        types1.add(NodeType.CONCRETE_CLASS);
        when(concreteClassNode.getTypes()).thenReturn(types1);
        when(concreteClassNode.getId()).thenReturn("concreteClassNode");
        when(concreteClassNode.getName()).thenReturn("ConcreteClassNode");

        // Init concreteClassNode
        final HashSet<NodeType> types2 = new HashSet<>();
        types2.add(NodeType.ABSTRACT_CLASS);
        when(abstractClassNode.getTypes()).thenReturn(types2);
        when(abstractClassNode.getId()).thenReturn("abstractClassNode");
        when(abstractClassNode.getName()).thenReturn("AbstractClassNode");
    }

    /**
     * Tests the behaviour of a compound comparator and its feedback when two nodes match.
     */
    @Test
    public void testGetFeedbackForMatch() {
        defaultCompoundComparator.compare(concreteClassNode, concreteClassNode);
        final Feedback feedback = defaultCompoundComparator.getFeedback();

        assertFeedbackMessages(feedback, concreteClassNode, FeedbackType.MATCH, new String[]{
                String.format(
                        MATCHED_WITH_MSG,
                        concreteClassNode.getName())});
        assertFeedbackMessages(feedback, concreteClassNode, FeedbackType.MISMATCH, new String[]{});
        assertFeedbackMessages(feedback, concreteClassNode, FeedbackType.INFO, new String[]{NODE_TYPES_ANALYSED_MSG});
    }

    /**
     * Test the behaviour of a compound comparator and its feedback when matching the node type fails.
     */
    @Test
    public void testGetFeedbackForRelationTypeMismatch() {
        defaultCompoundComparator.compare(concreteClassNode, abstractClassNode);
        final Feedback feedback = defaultCompoundComparator.getFeedback();
        assertFeedbackMessages(feedback, concreteClassNode, FeedbackType.MATCH, new String[]{});
        assertFeedbackMessages(feedback, concreteClassNode, FeedbackType.INFO, new String[]{NODE_TYPES_ANALYSED_MSG});
        assertFeedbackMessages(feedback, concreteClassNode, FeedbackType.MISMATCH, new String[]{
                String.format(
                        MATCH_FAILED_WITH_MSG,
                        abstractClassNode.getName()),
                String.format(
                        MISMATCH_MISSING_NODE_TYPE_MSG,
                        abstractClassNode.getName(),
                        NodeType.ABSTRACT_CLASS.name(),
                        concreteClassNode.getName())
        });
    }

    /**
     * Test adding a comparator.
     */
    @Test
    public void testAddComparator() {
        when(subComparator1.compare(dummyNode1, dummyNode2)).thenReturn(0); // dummyNode1 == dummyNode2
        when(subComparator2.compare(dummyNode1, dummyNode2)).thenReturn(0); // dummyNode1 == dummyNode2
        when(subComparator3.compare(dummyNode1, dummyNode2)).thenReturn(1); // dummyNode1 != dummyNode2

        // Check the collective result of subComparator1 and subComparator2
        assertThat(customCompoundComparator.compare(dummyNode1, dummyNode2), is(0));

        // Add an extra subComparator
        customCompoundComparator.addComparator(subComparator3);

        // Check the collective result of subComparator1, subComparator2 and subComparator3
        assertThat(customCompoundComparator.compare(dummyNode1, dummyNode2), is(1));
    }

}
