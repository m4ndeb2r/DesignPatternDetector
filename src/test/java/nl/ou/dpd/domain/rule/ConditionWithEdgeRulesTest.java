package nl.ou.dpd.domain.rule;

import nl.ou.dpd.domain.edge.Edge;
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
 * Tests the {@link Condition} class. In this test class we set up a {@link Condition} with some {@link EdgeRule}s.
 * The {@link Purview} of the {@link Condition} in this test class is {@link Purview#MANDATORY} for all tests.
 *
 * @author Martin de Boer
 */
@RunWith(MockitoJUnitRunner.class)
public class ConditionWithEdgeRulesTest {

    @Mock
    private EdgeRule edgeRule1;
    @Mock
    private EdgeRule edgeRule2;

    @Mock
    private Edge systemEdge;
    @Mock
    private Edge systemEdgeError;
    @Mock
    private Edge patternEdge;

    private Condition conditionWithEdgeRules;

    @Before
    public void createCondition() {
        // Mock rules' behaviour
        when(edgeRule1.getMould()).thenReturn(patternEdge);
        when(edgeRule2.getMould()).thenReturn(patternEdge);
        when(edgeRule1.process(any(Edge.class))).thenReturn(true);
        when(edgeRule2.process(any(Edge.class))).thenReturn(true);

        conditionWithEdgeRules = new Condition("1", "Edge rules condition");
        conditionWithEdgeRules.getEdgeRules().add(edgeRule1);
        conditionWithEdgeRules.getEdgeRules().add(edgeRule2);
        conditionWithEdgeRules.setPurview(Purview.MANDATORY);
    }

    @Test
    public void testConditionOK() {
        // Check situation before processing
        assertFalse(conditionWithEdgeRules.isProcessed());
        assertFalse(conditionWithEdgeRules.isProcessedSuccessfully());
        assertFalse(conditionWithEdgeRules.isProcessedUnsuccessfully());

        // Check results: all rules return true, accumulated result should be true as well
        assertTrue(conditionWithEdgeRules.process(systemEdge, patternEdge));

        // Check situation after processing
        assertTrue(conditionWithEdgeRules.isProcessed());
        assertTrue(conditionWithEdgeRules.isProcessedSuccessfully());
        assertFalse(conditionWithEdgeRules.isProcessedUnsuccessfully());
    }

    @Test
    public void testConditionNotOK() {
        // Mock rules' behaviour for this test
        when(edgeRule2.process(systemEdgeError)).thenReturn(false);

        // Check situation before processing
        assertFalse(conditionWithEdgeRules.isProcessed());
        assertFalse(conditionWithEdgeRules.isProcessedSuccessfully());
        assertFalse(conditionWithEdgeRules.isProcessedUnsuccessfully());

        // Check results: some rules true, some rules false, accumulated result should be false
        assertFalse(conditionWithEdgeRules.process(systemEdgeError, patternEdge));

        // Check situation after processing
        assertTrue(conditionWithEdgeRules.isProcessed());
        assertFalse(conditionWithEdgeRules.isProcessedSuccessfully());
        assertTrue(conditionWithEdgeRules.isProcessedUnsuccessfully());
    }

}
