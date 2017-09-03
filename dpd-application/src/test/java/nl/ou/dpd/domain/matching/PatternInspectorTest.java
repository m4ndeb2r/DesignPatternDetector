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
import static nl.ou.dpd.domain.matching.MatchingTestHelper.assertFeedbackMessages;
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

    private static final String ANALYSED_MSG = "Analysed!";
    private static final String NODE_MATCH_MSG = "Node match!";
    private static final String NODE_MISMATCH_MSG = "Node mismatch!";
    private static final String RELATION_MATCH_MSG = "Relation match!";
    private static final String RELATION_MISMATCH_MSG = "Relation mismatch!";

    private DesignPattern designPattern;
    private SystemUnderConsideration system;

    @Mock
    private DesignPattern designPatternMock;
    @Mock
    private Relation systemRelation, patternRelation;
    @Mock
    private Node systemNode1, systemNode2, patternNode1, patternNode2;
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
        systemNodeFeedback.addFeedbackMessage(systemNode1, FeedbackType.INFO, ANALYSED_MSG);
        systemNodeFeedback.addFeedbackMessage(systemNode1, FeedbackType.MISMATCH, NODE_MISMATCH_MSG);
        systemNodeFeedback.addFeedbackMessage(systemNode2, FeedbackType.INFO, ANALYSED_MSG);
        systemNodeFeedback.addFeedbackMessage(systemNode2, FeedbackType.MISMATCH, NODE_MISMATCH_MSG);
        when(rejectingNodeComparator.getFeedback()).thenReturn(systemNodeFeedback);
        when(rejectingNodeComparator.compare(any(Node.class), any(Node.class))).thenReturn(-1);

        final Feedback systemRelationFeedback = new Feedback();
        systemRelationFeedback.addFeedbackMessage(systemRelation, FeedbackType.INFO, ANALYSED_MSG);
        systemRelationFeedback.addFeedbackMessage(systemRelation, FeedbackType.MISMATCH, RELATION_MISMATCH_MSG);
        when(rejectingRelationComparator.getFeedback()).thenReturn(systemRelationFeedback);
        when(rejectingRelationComparator.compare(any(Relation.class), any(Relation.class))).thenReturn(-1);
    }

    @Before
    public void initAcceptingComparatorMocks() {
        final Feedback systemNodeFeedback = new Feedback();
        systemNodeFeedback.addFeedbackMessage(systemNode1, FeedbackType.INFO, ANALYSED_MSG);
        systemNodeFeedback.addFeedbackMessage(systemNode1, FeedbackType.MATCH, NODE_MATCH_MSG);
        systemNodeFeedback.addFeedbackMessage(systemNode2, FeedbackType.INFO, ANALYSED_MSG);
        systemNodeFeedback.addFeedbackMessage(systemNode2, FeedbackType.MATCH, NODE_MATCH_MSG);
        when(acceptingNodeComparator.getFeedback()).thenReturn(systemNodeFeedback);
        when(acceptingNodeComparator.compare(any(Node.class), any(Node.class))).thenReturn(0);

        final Feedback systemRelationFeedback = new Feedback();
        systemRelationFeedback.addFeedbackMessage(systemRelation, FeedbackType.INFO, ANALYSED_MSG);
        systemRelationFeedback.addFeedbackMessage(systemRelation, FeedbackType.MATCH, RELATION_MATCH_MSG);
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

        assertFeedbackMessages(feedback, systemNode1, FeedbackType.NOT_ANALYSED, new String[]{});
        assertFeedbackMessages(feedback, systemNode2, FeedbackType.NOT_ANALYSED, new String[]{});
        assertFeedbackMessages(feedback, systemRelation, FeedbackType.NOT_ANALYSED, new String[]{});

        assertFeedbackMessages(feedback, systemNode1, FeedbackType.MISMATCH, new String[]{});
        assertFeedbackMessages(feedback, systemNode2, FeedbackType.MISMATCH, new String[]{});
        assertFeedbackMessages(feedback, systemRelation, FeedbackType.MISMATCH, new String[]{});

        assertFeedbackMessages(feedback, systemNode1, FeedbackType.INFO, new String[]{ANALYSED_MSG});
        assertFeedbackMessages(feedback, systemNode2, FeedbackType.INFO, new String[]{ANALYSED_MSG});
        assertFeedbackMessages(feedback, systemRelation, FeedbackType.INFO, new String[]{ANALYSED_MSG});

        assertFeedbackMessages(feedback, systemNode1, FeedbackType.MATCH, new String[]{NODE_MATCH_MSG});
        assertFeedbackMessages(feedback, systemNode2, FeedbackType.MATCH, new String[]{NODE_MATCH_MSG});
        assertFeedbackMessages(feedback, systemRelation, FeedbackType.MATCH, new String[]{RELATION_MATCH_MSG});
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
        assertTrue(solutions.isEmpty());

        // Check feedback
        final Feedback feedback = patternInspector.getMatchingResult().getFeedback();

        assertFeedbackMessages(feedback, systemNode1, FeedbackType.NOT_ANALYSED, new String[]{});
        assertFeedbackMessages(feedback, systemNode2, FeedbackType.NOT_ANALYSED, new String[]{});
        assertFeedbackMessages(feedback, systemRelation, FeedbackType.NOT_ANALYSED, new String[]{});

        assertFeedbackMessages(feedback, systemNode1, FeedbackType.MATCH, new String[]{});
        assertFeedbackMessages(feedback, systemNode2, FeedbackType.MATCH, new String[]{});
        assertFeedbackMessages(feedback, systemRelation, FeedbackType.MATCH, new String[]{});

        assertFeedbackMessages(feedback, systemNode1, FeedbackType.INFO, new String[]{ANALYSED_MSG});
        assertFeedbackMessages(feedback, systemNode2, FeedbackType.INFO, new String[]{ANALYSED_MSG});
        assertFeedbackMessages(feedback, systemRelation, FeedbackType.INFO, new String[]{ANALYSED_MSG});

        assertFeedbackMessages(feedback, systemNode1, FeedbackType.MISMATCH, new String[]{NODE_MISMATCH_MSG});
        assertFeedbackMessages(feedback, systemNode2, FeedbackType.MISMATCH, new String[]{NODE_MISMATCH_MSG});
        assertFeedbackMessages(feedback, systemRelation, FeedbackType.MISMATCH, new String[]{RELATION_MISMATCH_MSG});
    }


}
