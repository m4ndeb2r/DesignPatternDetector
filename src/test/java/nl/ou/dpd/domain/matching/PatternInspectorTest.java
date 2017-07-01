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
    private Node systemNode1, systemNode2;
    @Mock
    private Relation systemRelation;
    @Mock
    private DesignPattern designPatternMock;
    @Mock
    private Node patternNode1, patternNode2;
    @Mock
    private Relation patternRelation;

    @Mock
    private FeedbackEnabledComparator<Node> nodeComparator;

    @Mock
    private FeedbackEnabledComparator<Relation> relationComparator;

    @Before
    public void setUp() {
        system = new SystemUnderConsideration("sysId", "sysName");
        system.addVertex(systemNode1);
        system.addVertex(systemNode2);
        system.addEdge(systemNode1, systemNode2, systemRelation);

        designPattern = new DesignPattern("patternName", "patternFamily");
        designPattern.addVertex(patternNode1);
        designPattern.addVertex(patternNode2);
        designPattern.addEdge(patternNode1, patternNode2, patternRelation);
    }

    @Test
    public void testConstructor() {
        new PatternInspector(system, designPatternMock);
        // Test if the comparators are retrieved to pass to super()
        verify(designPatternMock, times(1)).getNodeComparator();
        verify(designPatternMock, times(1)).getRelationComparator();
    }

    @Test
    public void testNotAnalysed() {
        // These comparators reject any node and any relation. Nothing is therefore analysed.
        when(nodeComparator.compare(any(Node.class), any(Node.class))).thenReturn(-1);
        designPattern.setNodeComparator(nodeComparator);
        when(relationComparator.compare(any(Relation.class), any(Relation.class))).thenReturn(-1);
        designPattern.setRelationComparator(relationComparator);

        final PatternInspector patternInspector = new PatternInspector(system, designPattern);

        // Check solutions
        final List<Solution> solutions = patternInspector.getSolutions();
        assertThat(solutions.size(), is(0));
        assertFalse(patternInspector.isomorphismExists());

        // Check feedback
        final Feedback feedback = patternInspector.getFeedback();
        assertThat(feedback.getFeedbackMessages(systemNode1, FeedbackType.NOT_ANALYSED).size(), is(1));
        assertThat(feedback.getFeedbackMessages(systemNode2, FeedbackType.NOT_ANALYSED).size(), is(1));
        assertThat(feedback.getFeedbackMessages(systemRelation, FeedbackType.NOT_ANALYSED).size(), is(1));
    }


}
