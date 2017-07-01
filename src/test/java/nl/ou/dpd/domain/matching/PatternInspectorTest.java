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

    @Mock
    private SystemUnderConsideration system;

    @Mock
    private DesignPattern designPattern;

    @Mock
    private FeedbackEnabledComparator<Node> nodeComparator;

    @Mock
    private FeedbackEnabledComparator<Relation> relationComparator;

    @Before
    public void initMockObjects() {
        when(designPattern.getNodeComparator()).thenReturn(nodeComparator);
        when(designPattern.getRelationComparator()).thenReturn(relationComparator);
    }

    @Test
    public void testConstructor() {
        new PatternInspector(system, designPattern);
        verify(designPattern, times(1)).getNodeComparator();
        verify(designPattern, times(1)).getRelationComparator();
    }
}
