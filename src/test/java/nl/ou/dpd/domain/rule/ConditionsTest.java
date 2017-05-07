package nl.ou.dpd.domain.rule;

import nl.ou.dpd.domain.edge.Edge;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests the {@link Conditions} class.
 */
@RunWith(MockitoJUnitRunner.class)
public class ConditionsTest {

    /**
     * Exception rule.
     */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private Condition mandatoryCondition;

    @Mock
    private Condition feedbackCondition;

    @Mock
    private Condition ignoredCondition;

    @Mock
    private Edge systemEdge;

    @Mock
    private Edge patternEdge;

    private Conditions conditions;

    @Before
    public void setUp() {
        conditions = new Conditions();
        conditions.getConditions().add(mandatoryCondition);
        conditions.getConditions().add(feedbackCondition);
        conditions.getConditions().add(ignoredCondition);

        // Set general expectations
        when(mandatoryCondition.isProcessed()).thenReturn(true);
        when(mandatoryCondition.getPurview()).thenReturn(Purview.MANDATORY);
        when(feedbackCondition.isProcessed()).thenReturn(true);
        when(feedbackCondition.getPurview()).thenReturn(Purview.FEEDBACK_ONLY);
        when(ignoredCondition.isProcessed()).thenReturn(false);
        when(ignoredCondition.getPurview()).thenReturn(Purview.IGNORE);
    }

    @Test
    public void testAllOK() {
        // Set test specific expectations
        when(mandatoryCondition.process(systemEdge, patternEdge)).thenReturn(true);
        when(mandatoryCondition.isProcessedUnsuccessfully()).thenReturn(false);
        when(feedbackCondition.process(systemEdge, patternEdge)).thenReturn(true);
        when(ignoredCondition.process(systemEdge, patternEdge)).thenReturn(true);

        assertTrue(conditions.processConditions(systemEdge, patternEdge));

        // Verifiy number of calls
        verify(mandatoryCondition, times(1)).process(systemEdge, patternEdge);
        verify(mandatoryCondition, times(1)).isProcessedUnsuccessfully();
        verify(feedbackCondition, times(1)).process(systemEdge, patternEdge);
        verify(ignoredCondition, times(1)).process(systemEdge, patternEdge);
    }
    @Test
    public void testMandatoryNOK() {
        // Set test specific expectations
        when(mandatoryCondition.process(systemEdge, patternEdge)).thenReturn(false);
        when(mandatoryCondition.isProcessedUnsuccessfully()).thenReturn(true);
        when(feedbackCondition.process(systemEdge, patternEdge)).thenReturn(true);
        when(ignoredCondition.process(systemEdge, patternEdge)).thenReturn(true);

        assertFalse(conditions.processConditions(systemEdge, patternEdge));

        // Verifiy number of calls
        verify(mandatoryCondition, times(1)).process(systemEdge, patternEdge);
        verify(mandatoryCondition, times(1)).isProcessedUnsuccessfully();
        verify(feedbackCondition, times(0)).process(systemEdge, patternEdge);
        verify(ignoredCondition, times(0)).process(systemEdge, patternEdge);
    }

    @Test
    public void testFeedBackNOK() {
        // Set test specific expectations
        when(mandatoryCondition.process(systemEdge, patternEdge)).thenReturn(true);
        when(mandatoryCondition.isProcessedUnsuccessfully()).thenReturn(false);
        when(feedbackCondition.process(systemEdge, patternEdge)).thenReturn(false);
        when(ignoredCondition.process(systemEdge, patternEdge)).thenReturn(true);

        assertTrue(conditions.processConditions(systemEdge, patternEdge));

        // Verifiy number of calls
        verify(mandatoryCondition, times(1)).process(systemEdge, patternEdge);
        verify(mandatoryCondition, times(1)).isProcessedUnsuccessfully();
        verify(feedbackCondition, times(1)).process(systemEdge, patternEdge);
        verify(ignoredCondition, times(1)).process(systemEdge, patternEdge);
    }

}
