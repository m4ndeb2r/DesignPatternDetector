package nl.ou.dpd.domain.matching;

import nl.ou.dpd.domain.DesignPattern;
import nl.ou.dpd.domain.SystemUnderConsideration;
import nl.ou.dpd.domain.node.Node;
import nl.ou.dpd.domain.relation.Relation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests the {@link PatternInspector} class.
 *
 * @author Martin de Boer
 */
@RunWith(MockitoJUnitRunner.class)
public class PatternInspectorTest {

    private SystemUnderConsideration system;
    private DesignPattern designPattern;

    @Mock
    private Node systemNode1, systemNode2, patternNode1, patternNode2;
    @Mock
    private Relation systemRelation, patternRelation;
    @Mock
    private DesignPattern designPatternMock;

    @Mock
    private FeedbackEnabledComparator<Node> rejectingNodeComparator, acceptingNodeComparator;
    @Mock
    private FeedbackEnabledComparator<Relation> rejectingRelationComparator, acceptingRelationComparator;

    @Before
    public void initSystem() {
        system = new SystemUnderConsideration("sysId", "sysName");
        system.addVertex(systemNode1);
        system.addVertex(systemNode2);
        system.addEdge(systemNode1, systemNode2, systemRelation);
    }

    @Before
    public void initDesignPattern() {
        designPattern = new DesignPattern("patternName", "patternFamily");
        designPattern.addVertex(patternNode1);
        designPattern.addVertex(patternNode2);
        designPattern.addEdge(patternNode1, patternNode2, patternRelation);

    }

    @Before
    public void initRejectingComparatorMocks() {
        final Feedback systemNodeFeedback = new Feedback();
        systemNodeFeedback.addFeedbackMessage(systemNode1, FeedbackType.INFO, "Analysed!");
        systemNodeFeedback.addFeedbackMessage(systemNode1, FeedbackType.MISMATCH, "Node mismatch!");
        systemNodeFeedback.addFeedbackMessage(systemNode2, FeedbackType.INFO, "Analysed!");
        systemNodeFeedback.addFeedbackMessage(systemNode2, FeedbackType.MISMATCH, "Node mismatch!");
        when(rejectingNodeComparator.getFeedback()).thenReturn(systemNodeFeedback);
        when(rejectingNodeComparator.compare(any(Node.class), any(Node.class))).thenReturn(-1);

        final Feedback systemRelationFeedback = new Feedback();
        systemRelationFeedback.addFeedbackMessage(systemRelation, FeedbackType.INFO, "Analysed!");
        systemRelationFeedback.addFeedbackMessage(systemRelation, FeedbackType.MISMATCH, "Relation mismatch!");
        when(rejectingRelationComparator.getFeedback()).thenReturn(systemRelationFeedback);
        when(rejectingRelationComparator.compare(any(Relation.class), any(Relation.class))).thenReturn(-1);
    }

    @Before
    public void initAcceptingComparatorMocks() {
        final Feedback systemNodeFeedback = new Feedback();
        systemNodeFeedback.addFeedbackMessage(systemNode1, FeedbackType.INFO, "Analysed!");
        systemNodeFeedback.addFeedbackMessage(systemNode1, FeedbackType.MATCH, "Node match!");
        systemNodeFeedback.addFeedbackMessage(systemNode2, FeedbackType.INFO, "Analysed!");
        systemNodeFeedback.addFeedbackMessage(systemNode2, FeedbackType.MATCH, "Node match!");
        when(acceptingNodeComparator.getFeedback()).thenReturn(systemNodeFeedback);
        when(acceptingNodeComparator.compare(any(Node.class), any(Node.class))).thenReturn(0);

        final Feedback systemRelationFeedback = new Feedback();
        systemRelationFeedback.addFeedbackMessage(systemRelation, FeedbackType.INFO, "Analysed!");
        systemRelationFeedback.addFeedbackMessage(systemRelation, FeedbackType.MATCH, "Relation match!");
        when(acceptingRelationComparator.getFeedback()).thenReturn(systemRelationFeedback);
        when(acceptingRelationComparator.compare(any(Relation.class), any(Relation.class))).thenReturn(0);
    }

    @Test
    public void testConstructor() {
        new PatternInspector(system, designPatternMock);
        // Test if the comparators are retrieved to pass to super()
        verify(designPatternMock, times(1)).getNodeComparator();
        verify(designPatternMock, times(1)).getRelationComparator();
    }

    @Test
    public void testMatch() {
        // These comparators accept any node and any relation.
        designPattern
                .setNodeComparator(acceptingNodeComparator)
                .setRelationComparator(acceptingRelationComparator);

        final PatternInspector patternInspector = new PatternInspector(system, designPattern);
        assertTrue(patternInspector.isomorphismExists());

        // Check solutions
        List<Solution> solutions = patternInspector.getMatchingResult().getSolutions(true);
        assertThat(solutions.size(), is(1));
        solutions = patternInspector.getMatchingResult().getSolutions();
        assertThat(solutions.size(), is(1));

        final Solution solution = solutions.get(0);
        final List<Node[]> matchingNodes = solution.getMatchingNodes();
        assertThat(matchingNodes.size(), is(2));
        assertThat(matchingNodes.get(0)[0], is(systemNode1));
        assertThat(matchingNodes.get(0)[1], is(patternNode1));
        assertThat(matchingNodes.get(1)[0], is(systemNode2));
        assertThat(matchingNodes.get(1)[1], is(patternNode2));

        final List<Relation[]> matchingRelations = solution.getMatchingRelations();
        assertThat(matchingRelations.size(), is(1));
        assertThat(matchingRelations.get(0)[0], is(systemRelation));
        assertThat(matchingRelations.get(0)[1], is(patternRelation));

        // Check feedback
        final Feedback feedback = patternInspector.getMatchingResult().getFeedback();
        assertThat(feedback.getFeedbackMessages(systemNode1, FeedbackType.NOT_ANALYSED).size(), is(0));
        assertThat(feedback.getFeedbackMessages(systemNode2, FeedbackType.NOT_ANALYSED).size(), is(0));
        assertThat(feedback.getFeedbackMessages(systemRelation, FeedbackType.NOT_ANALYSED).size(), is(0));

        assertThat(feedback.getFeedbackMessages(systemNode1, FeedbackType.MISMATCH).size(), is(0));
        assertThat(feedback.getFeedbackMessages(systemNode2, FeedbackType.MISMATCH).size(), is(0));
        assertThat(feedback.getFeedbackMessages(systemRelation, FeedbackType.MISMATCH).size(), is(0));

        assertThat(feedback.getFeedbackMessages(systemNode1, FeedbackType.INFO).size(), is(1));
        assertThat(feedback.getFeedbackMessages(systemNode1, FeedbackType.INFO).get(0), is("Analysed!"));
        assertThat(feedback.getFeedbackMessages(systemNode2, FeedbackType.INFO).size(), is(1));
        assertThat(feedback.getFeedbackMessages(systemNode2, FeedbackType.INFO).get(0), is("Analysed!"));
        assertThat(feedback.getFeedbackMessages(systemRelation, FeedbackType.INFO).size(), is(1));
        assertThat(feedback.getFeedbackMessages(systemRelation, FeedbackType.INFO).get(0), is("Analysed!"));

        assertThat(feedback.getFeedbackMessages(systemNode1, FeedbackType.MATCH).size(), is(1));
        assertThat(feedback.getFeedbackMessages(systemNode1, FeedbackType.MATCH).get(0), is("Node match!"));
        assertThat(feedback.getFeedbackMessages(systemNode2, FeedbackType.MATCH).size(), is(1));
        assertThat(feedback.getFeedbackMessages(systemNode2, FeedbackType.MATCH).get(0), is("Node match!"));
        assertThat(feedback.getFeedbackMessages(systemRelation, FeedbackType.MATCH).size(), is(1));
        assertThat(feedback.getFeedbackMessages(systemRelation, FeedbackType.MATCH).get(0), is("Relation match!"));
    }

    @Test
    public void testNoMatch() {
        // These comparators reject any node and any relation.
        designPattern
                .setNodeComparator(rejectingNodeComparator)
                .setRelationComparator(rejectingRelationComparator);

        final PatternInspector patternInspector = new PatternInspector(system, designPattern);
        assertFalse(patternInspector.isomorphismExists());

        // Check solutions
        final List<Solution> solutions = patternInspector.getMatchingResult().getSolutions();
        assertThat(solutions.size(), is(0));

        // Check feedback
        final Feedback feedback = patternInspector.getMatchingResult().getFeedback();
        assertThat(feedback.getFeedbackMessages(systemNode1, FeedbackType.NOT_ANALYSED).size(), is(0));
        assertThat(feedback.getFeedbackMessages(systemNode2, FeedbackType.NOT_ANALYSED).size(), is(0));
        assertThat(feedback.getFeedbackMessages(systemRelation, FeedbackType.NOT_ANALYSED).size(), is(0));

        assertThat(feedback.getFeedbackMessages(systemNode1, FeedbackType.MATCH).size(), is(0));
        assertThat(feedback.getFeedbackMessages(systemNode2, FeedbackType.MATCH).size(), is(0));
        assertThat(feedback.getFeedbackMessages(systemRelation, FeedbackType.MATCH).size(), is(0));

        assertThat(feedback.getFeedbackMessages(systemNode1, FeedbackType.INFO).size(), is(1));
        assertThat(feedback.getFeedbackMessages(systemNode1, FeedbackType.INFO).get(0), is("Analysed!"));
        assertThat(feedback.getFeedbackMessages(systemNode2, FeedbackType.INFO).size(), is(1));
        assertThat(feedback.getFeedbackMessages(systemNode2, FeedbackType.INFO).get(0), is("Analysed!"));
        assertThat(feedback.getFeedbackMessages(systemRelation, FeedbackType.INFO).size(), is(1));
        assertThat(feedback.getFeedbackMessages(systemRelation, FeedbackType.INFO).get(0), is("Analysed!"));

        assertThat(feedback.getFeedbackMessages(systemNode1, FeedbackType.MISMATCH).size(), is(1));
        assertThat(feedback.getFeedbackMessages(systemNode1, FeedbackType.MISMATCH).get(0), is("Node mismatch!"));
        assertThat(feedback.getFeedbackMessages(systemNode2, FeedbackType.MISMATCH).size(), is(1));
        assertThat(feedback.getFeedbackMessages(systemNode2, FeedbackType.MISMATCH).get(0), is("Node mismatch!"));
        assertThat(feedback.getFeedbackMessages(systemRelation, FeedbackType.MISMATCH).size(), is(1));
        assertThat(feedback.getFeedbackMessages(systemRelation, FeedbackType.MISMATCH).get(0), is("Relation mismatch!"));
    }


}
