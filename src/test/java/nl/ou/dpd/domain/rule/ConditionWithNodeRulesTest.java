package nl.ou.dpd.domain.rule;

import nl.ou.dpd.domain.edge.Edge;
import nl.ou.dpd.domain.node.Attribute;
import nl.ou.dpd.domain.node.Node;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Tests the {@link Condition} class. In this test class we set up a {@link Condition} with some {@link NodeRule}s.
 * The {@link Purview} of the {@link Condition} in this test class is {@link Purview#MANDATORY} for all tests.
 *
 * @author Martin de Boer
 */
@RunWith(MockitoJUnitRunner.class)
public class ConditionWithNodeRulesTest {

    @Mock
    private NodeRule nodeRule1;
    @Mock
    private NodeRule nodeRule2;

    @Mock
    private Edge systemEdge;
    @Mock
    private Node systemLeftNode;
    @Mock
    private Node systemRightNode;

    @Mock
    private Edge patternEdge;
    @Mock
    private Node patternLeftNode;
    @Mock
    private Node patternRightNode;

    private Condition conditionWithNodeRules;

    @Before
    public void createCondition() {
        // Mock systemEdge behaviour
        when(systemEdge.getLeftNode()).thenReturn(systemLeftNode);
        when(systemEdge.getRightNode()).thenReturn(systemRightNode);
        when(systemLeftNode.getName()).thenReturn("SystemLeftNodeName");
        when(systemRightNode.getName()).thenReturn("SystemRightNodeName");

        // Mock patternEdge behaviour
        when(patternEdge.getLeftNode()).thenReturn(patternLeftNode);
        when(patternLeftNode.getName()).thenReturn("PatternLeftNodeName");
        when(patternEdge.getRightNode()).thenReturn(patternRightNode);
        when(patternRightNode.getName()).thenReturn("PatternRightNodeName");

        // Mock rules' behaviour
        when(nodeRule1.getMould()).thenReturn(patternLeftNode);
        when(nodeRule2.getMould()).thenReturn(patternRightNode);

        conditionWithNodeRules = new Condition("1", "Node rules condition");
        conditionWithNodeRules.getNodeRules().add(nodeRule1);
        conditionWithNodeRules.getNodeRules().add(nodeRule2);
        conditionWithNodeRules.setPurview(Purview.MANDATORY);
    }

    @Test
    public void testConditionOK() {
        // Mock rules' behaviour for this test
        when(nodeRule1.process(any(Node.class))).thenReturn(true);
        when(nodeRule2.process(any(Node.class))).thenReturn(true);

        // Check situation before processing
        assertFalse(conditionWithNodeRules.isProcessed());
        assertFalse(conditionWithNodeRules.isProcessedSuccessfully());
        assertFalse(conditionWithNodeRules.isProcessedUnsuccessfully());

        // Check results: all rules return true, accumulated result should be true as well
        assertTrue(conditionWithNodeRules.process(systemEdge, patternEdge));

        // Check situation after processing
        assertTrue(conditionWithNodeRules.isProcessed());
        assertTrue(conditionWithNodeRules.isProcessedSuccessfully());
        assertFalse(conditionWithNodeRules.isProcessedUnsuccessfully());
    }

    @Test
    public void testConditionNotOK() {
        // Mock rules' behaviour for this test
        when(nodeRule1.process(systemLeftNode)).thenReturn(true);
        when(nodeRule2.process(systemLeftNode)).thenReturn(true);
        when(nodeRule2.process(systemRightNode)).thenReturn(false);

        // Check situation before processing
        assertFalse(conditionWithNodeRules.isProcessed());
        assertFalse(conditionWithNodeRules.isProcessedSuccessfully());
        assertFalse(conditionWithNodeRules.isProcessedUnsuccessfully());

        // Check results: some rules true, some rules false, accumulated result should be false
        assertFalse(conditionWithNodeRules.process(systemEdge, patternEdge));

        // Check situation after processing
        assertTrue(conditionWithNodeRules.isProcessed());
        assertFalse(conditionWithNodeRules.isProcessedSuccessfully());
        assertTrue(conditionWithNodeRules.isProcessedUnsuccessfully());
    }

}
