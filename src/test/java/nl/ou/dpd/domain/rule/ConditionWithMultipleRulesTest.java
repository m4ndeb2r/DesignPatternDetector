package nl.ou.dpd.domain.rule;

import nl.ou.dpd.domain.edge.Edge;
import nl.ou.dpd.domain.node.Node;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests the {@link Condition} class. In this test class we set up a {@link Condition} with one {@link EdgeRule}, two
 * {@link NodeRule}s for the left node and one {@link NodeRule} for the right node. All the edges, nodes and rules are
 * mocked, so we only test the {@link Condition}'s behaviour. Other classes are tested elsewhere. The {@link Purview}
 * of the {@link Condition} in this test class is {@link Purview#FEEDBACK_ONLY} for all tests.
 *
 * @author Martin de Boer
 */
@RunWith(MockitoJUnitRunner.class)
public class ConditionWithMultipleRulesTest {

    @Mock
    private EdgeRule edgeRule;
    @Mock
    private NodeRule firstLeftNodeRule;
    @Mock
    private NodeRule secondLeftNodeRule;
    @Mock
    private NodeRule rightNodeRule;

    @Mock
    private Edge systemEdge;
    @Mock
    private Node systemLeftNode;
    @Mock
    private Node systemRightNode;

    private Condition conditionWithEdgeAndNodeRules;

    /**
     * Initializes mocks and test subject(s).
     */
    @Before
    public void createAssociationCondition() {
        // Create a mandatory condition with one edge rule
        conditionWithEdgeAndNodeRules = new Condition("1", "Checks one edge and multiple nodes.");
        conditionWithEdgeAndNodeRules.setPurview(Purview.FEEDBACK_ONLY);
        conditionWithEdgeAndNodeRules.getEdgeRules().add(edgeRule);
        conditionWithEdgeAndNodeRules.getLeftNodeRules().add(firstLeftNodeRule);
        conditionWithEdgeAndNodeRules.getLeftNodeRules().add(secondLeftNodeRule);
        conditionWithEdgeAndNodeRules.getRightNodeRules().add(rightNodeRule);
    }

    /**
     * Tests the {@link Condition#process(Edge)} method. Test the situation where all the edge rules and all the node
     * rules return {@code true}. We expect the method {@link Condition#process(Edge)} to return {@code true} as well.
     */
    @Test
    public void testAllRulesOK() {
        // Check situation before processing
        assertFalse(conditionWithEdgeAndNodeRules.isProcessed());
        assertFalse(conditionWithEdgeAndNodeRules.isProcessedSuccessfully());
        assertFalse(conditionWithEdgeAndNodeRules.isProcessedUnsuccessfully());

        // Set expectations
        when(edgeRule.process(systemEdge)).thenReturn(true);
        when(systemEdge.getNode1()).thenReturn(systemLeftNode);
        when(systemEdge.getNode2()).thenReturn(systemRightNode);
        when(edgeRule.process(systemEdge)).thenReturn(true);
        when(firstLeftNodeRule.process(systemLeftNode)).thenReturn(true);
        when(secondLeftNodeRule.process(systemLeftNode)).thenReturn(true);
        when(rightNodeRule.process(systemRightNode)).thenReturn(true);

        // Process the first time
        assertTrue(conditionWithEdgeAndNodeRules.process(systemEdge));
        assertTrue(conditionWithEdgeAndNodeRules.isProcessed());
        assertTrue(conditionWithEdgeAndNodeRules.isProcessedSuccessfully());
        assertFalse(conditionWithEdgeAndNodeRules.isProcessedUnsuccessfully());

        // Process again (already processed to the rules are not processed again
        assertTrue(conditionWithEdgeAndNodeRules.process(systemEdge));

        // Verify the number of times the rules were processed
        verify(edgeRule, times(1)).process(systemEdge);
        verify(firstLeftNodeRule, times(1)).process(systemLeftNode);
        verify(secondLeftNodeRule, times(1)).process(systemLeftNode);
        verify(rightNodeRule, times(1)).process(systemRightNode);
    }

    /**
     * Tests the {@link Condition#process(Edge)} method. In this test the last node rule returns {@code false},
     * resulting in an accumulated result of {@code false} also.
     */
    @Test
    public void testRightNodeRuleNOK() {
        // Check situation before processing
        assertFalse(conditionWithEdgeAndNodeRules.isProcessed());
        assertFalse(conditionWithEdgeAndNodeRules.isProcessedSuccessfully());
        assertFalse(conditionWithEdgeAndNodeRules.isProcessedUnsuccessfully());

        // Set expectations
        when(edgeRule.process(systemEdge)).thenReturn(true);
        when(systemEdge.getNode1()).thenReturn(systemLeftNode);
        when(systemEdge.getNode2()).thenReturn(systemRightNode);
        when(edgeRule.process(systemEdge)).thenReturn(true);
        when(firstLeftNodeRule.process(systemLeftNode)).thenReturn(true);
        when(secondLeftNodeRule.process(systemLeftNode)).thenReturn(true);
        when(rightNodeRule.process(systemRightNode)).thenReturn(false);

        // Process the first time
        assertFalse(conditionWithEdgeAndNodeRules.process(systemEdge));
        assertTrue(conditionWithEdgeAndNodeRules.isProcessed());
        assertFalse(conditionWithEdgeAndNodeRules.isProcessedSuccessfully());
        assertTrue(conditionWithEdgeAndNodeRules.isProcessedUnsuccessfully());

        // Process again (already processed to the rules are not processed again
        assertFalse(conditionWithEdgeAndNodeRules.process(systemEdge));

        // Verify the number of times the rules were processed
        verify(edgeRule, times(1)).process(systemEdge);
        verify(firstLeftNodeRule, times(1)).process(systemLeftNode);
        verify(secondLeftNodeRule, times(1)).process(systemLeftNode);
        verify(rightNodeRule, times(1)).process(systemRightNode);
    }

    /**
     * Tests the {@link Condition#process(Edge)} method. In this test the first node rule returns {@code false},
     * resulting in an accumulated result of {@code false} also.
     */
    @Test
    public void testFirstLeftNodeRuleNOK() {
        // Check situation before processing
        assertFalse(conditionWithEdgeAndNodeRules.isProcessed());
        assertFalse(conditionWithEdgeAndNodeRules.isProcessedSuccessfully());
        assertFalse(conditionWithEdgeAndNodeRules.isProcessedUnsuccessfully());

        // Set expectations
        when(edgeRule.process(systemEdge)).thenReturn(true);
        when(systemEdge.getNode1()).thenReturn(systemLeftNode);
        when(systemEdge.getNode2()).thenReturn(systemRightNode);
        when(edgeRule.process(systemEdge)).thenReturn(true);
        when(firstLeftNodeRule.process(systemLeftNode)).thenReturn(false);

        // Process the first time
        assertFalse(conditionWithEdgeAndNodeRules.process(systemEdge));
        assertTrue(conditionWithEdgeAndNodeRules.isProcessed());
        assertFalse(conditionWithEdgeAndNodeRules.isProcessedSuccessfully());
        assertTrue(conditionWithEdgeAndNodeRules.isProcessedUnsuccessfully());

        // Process again (already processed to the rules are not processed again
        assertFalse(conditionWithEdgeAndNodeRules.process(systemEdge));

        // Verify the number of times the rules were processed
        verify(edgeRule, times(1)).process(systemEdge);
        verify(firstLeftNodeRule, times(1)).process(systemLeftNode);
        verify(secondLeftNodeRule, times(0)).process(systemLeftNode);
        verify(rightNodeRule, times(0)).process(systemRightNode);
    }

}
