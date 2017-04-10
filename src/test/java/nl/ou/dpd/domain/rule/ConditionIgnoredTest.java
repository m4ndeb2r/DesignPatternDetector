package nl.ou.dpd.domain.rule;

import nl.ou.dpd.domain.edge.Edge;
import org.junit.Before;
import org.junit.Test;
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
 * {@link Condition} in this test class is {@link Purview#IGNORE}, so we don't expect too much happening here.
 *
 * @author Martin de Boer
 */
@RunWith(MockitoJUnitRunner.class)
public class ConditionIgnoredTest {

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
        conditionWithEdgeRule = new Condition("1", "This is an ignored condition");
        conditionWithEdgeRule.getEdgeRules().add(edgeRule);
        conditionWithEdgeRule.setPurview(Purview.IGNORE);
    }

    /**
     * Tests the {@link Condition#process(Edge)} method. Expects the method to return {@code true}, because the
     * {@link EdgeRule} inside the {@link Condition} returns {@code true}.
     */
    @Test
    public void testConditionIgnored() {
        // Check situation before processing
        assertFalse(conditionWithEdgeRule.isProcessed());
        assertFalse(conditionWithEdgeRule.isProcessedSuccessfully());
        assertFalse(conditionWithEdgeRule.isProcessedUnsuccessfully());

        // Process the first time
        assertTrue(conditionWithEdgeRule.process(systemEdge));

        // Nothing should have changed.
        assertFalse(conditionWithEdgeRule.isProcessed());
        assertFalse(conditionWithEdgeRule.isProcessedSuccessfully());
        assertFalse(conditionWithEdgeRule.isProcessedUnsuccessfully());

        // Process again
        assertTrue(conditionWithEdgeRule.process(systemEdge));

        // Nothing should have changed.
        assertFalse(conditionWithEdgeRule.isProcessed());
        assertFalse(conditionWithEdgeRule.isProcessedSuccessfully());
        assertFalse(conditionWithEdgeRule.isProcessedUnsuccessfully());

        // Verify the edge rule is only processed once
        verify(edgeRule, times(0)).process(systemEdge);
    }
}
