package nl.ou.dpd.domain.rule;

import nl.ou.dpd.domain.edge.Edge;
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
 * Tests the {@link Condition} class. In this test class we set up a {@link Condition} with one {@link EdgeRule}. All
 * the edges and rules are mocked, so we only test the behaviour of the {@link Condition}. The {@link Purview} of the
 * {@link Condition} in this test class is {@link Purview#MANDATORY} for all tests.
 *
 * @author Martin de Boer
 */
@RunWith(MockitoJUnitRunner.class)
public class ConditionWithEdgeRuleTest {

    @Mock
    private EdgeRule edgeRule;
    @Mock
    private Edge systemEdge;

    private Condition conditionWithEdgeRule;

    /**
     * Initializes mocks and test subject(s).
     */
    @Before
    public void createCondition() {
        // Create a mandatory condition with one edge rule
        conditionWithEdgeRule = new Condition("1", "Some edge rule in this condition must be checked");
        conditionWithEdgeRule.getEdgeRules().add(edgeRule);
        conditionWithEdgeRule.setPurview(Purview.MANDATORY);
    }

    /**
     * Tests the {@link Condition#process(Edge)} method. Expects the method to return {@code true}, because the
     * {@link EdgeRule} inside the {@link Condition} returns {@code true}.
     */
    @Test
    public void testConditionOK() {
        // Check situation before processing
        assertFalse(conditionWithEdgeRule.isProcessed());
        assertFalse(conditionWithEdgeRule.isProcessedSuccessfully());
        assertFalse(conditionWithEdgeRule.isProcessedUnsuccessfully());

        // Set expectations
        when(edgeRule.process(systemEdge)).thenReturn(true);

        // Process the first time
        assertTrue(conditionWithEdgeRule.process(systemEdge));
        assertTrue(conditionWithEdgeRule.isProcessed());
        assertTrue(conditionWithEdgeRule.isProcessedSuccessfully());
        assertFalse(conditionWithEdgeRule.isProcessedUnsuccessfully());

        // Process again (already processed to the rules are not processed again
        assertTrue(conditionWithEdgeRule.process(systemEdge));

        // Verify the edge rule is only processed once
        verify(edgeRule, times(1)).process(systemEdge);

        // Clear processing and test the result
        conditionWithEdgeRule.clearProcessed();
        assertFalse(conditionWithEdgeRule.isProcessed());
        assertFalse(conditionWithEdgeRule.isProcessedSuccessfully());
        assertFalse(conditionWithEdgeRule.isProcessedUnsuccessfully());
    }

    /**
     * Tests the {@link Condition#process(Edge)} method. Expects the method to return {@code false}, because the
     * {@link EdgeRule} inside the {@link Condition} returns {@code false}.
     */
    @Test
    public void testConditionNOK() {
        // Check situation before processing
        assertFalse(conditionWithEdgeRule.isProcessed());
        assertFalse(conditionWithEdgeRule.isProcessedSuccessfully());
        assertFalse(conditionWithEdgeRule.isProcessedUnsuccessfully());

        // Set expectations
        when(edgeRule.process(systemEdge)).thenReturn(false);

        // Process the first time
        assertFalse(conditionWithEdgeRule.process(systemEdge));
        assertTrue(conditionWithEdgeRule.isProcessed());
        assertFalse(conditionWithEdgeRule.isProcessedSuccessfully());
        assertTrue(conditionWithEdgeRule.isProcessedUnsuccessfully());

        // Process again (already processed to the rules are not processed again
        assertFalse(conditionWithEdgeRule.process(systemEdge));

        // Verify the edge rule is only processed once
        verify(edgeRule, times(1)).process(systemEdge);
    }

}
