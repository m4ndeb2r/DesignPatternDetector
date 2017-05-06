package nl.ou.dpd.domain.rule;

import nl.ou.dpd.domain.edge.Edge;
import nl.ou.dpd.domain.node.Attribute;
import nl.ou.dpd.domain.node.Node;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.w3c.dom.Attr;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests the {@link Condition} class. In this test class we set up a {@link Condition} with some {@link AttributeRule}s.
 * The {@link Purview} of the {@link Condition} in this test class is {@link Purview#MANDATORY} for all tests.
 *
 * @author Martin de Boer
 */
@RunWith(MockitoJUnitRunner.class)
public class ConditionWithAttributeRulesTest {

    @Mock
    private AttributeRule attribRule1;
    @Mock
    private AttributeRule attribRule2;
    @Mock
    private Edge systemEdge;
    @Mock
    private Node systemLeftNode;
    @Mock
    private Attribute systemLeftAttribute1;
    @Mock
    private Attribute systemLeftAttribute2;
    @Mock
    private Node systemRightNode;

    @Mock
    private Edge patternEdge;
    @Mock
    private Node patternRightNode;
    @Mock
    private Attribute patternLeftAttribute1;
    @Mock
    private Attribute patternLeftAttribute2;

    private Condition conditionWithAttributeRules;

    @Before
    public void createCondition() {
        List<Attribute> systemLeftAttributes = new ArrayList<>();
        systemLeftAttributes.add(systemLeftAttribute1);
        systemLeftAttributes.add(systemLeftAttribute2);

        when(systemEdge.getLeftNode()).thenReturn(systemLeftNode);
        when(systemEdge.getRightNode()).thenReturn(systemRightNode);
        when(systemLeftNode.getAttributes()).thenReturn(systemLeftAttributes);
        when(systemLeftAttribute1.getType()).thenReturn(systemRightNode);
        when(systemLeftAttribute2.getType()).thenReturn(systemRightNode);
        when(systemRightNode.getName()).thenReturn("SystemRightNodeName");

        when(patternEdge.getRightNode()).thenReturn(patternRightNode);
        when(patternLeftAttribute1.getType()).thenReturn(patternRightNode);
        when(patternLeftAttribute2.getType()).thenReturn(patternRightNode);
        when(patternRightNode.getName()).thenReturn("PatternRightNodeName");

        when(attribRule1.getMould()).thenReturn(patternLeftAttribute1);
        when(attribRule2.getMould()).thenReturn(patternLeftAttribute2);

        conditionWithAttributeRules = new Condition("1", "Attribute rules condition");
        conditionWithAttributeRules.getAttributeRules().add(attribRule1);
        conditionWithAttributeRules.getAttributeRules().add(attribRule2);
        conditionWithAttributeRules.setPurview(Purview.MANDATORY);
    }

    @Test
    public void testConditionOK() {
        // Set expectations
        when(attribRule1.process(any(Attribute.class))).thenReturn(true);
        when(attribRule2.process(any(Attribute.class))).thenReturn(true);

        // Check situation before processing
        assertFalse(conditionWithAttributeRules.isProcessed());
        assertFalse(conditionWithAttributeRules.isProcessedSuccessfully());
        assertFalse(conditionWithAttributeRules.isProcessedUnsuccessfully());

        // Check results: all rules return true, accumulated result should be true as well
        assertTrue(conditionWithAttributeRules.process(systemEdge, patternEdge));
    }

    @Test
    public void testConditionNotOK() {
        // Set expectations
        when(attribRule1.process(systemLeftAttribute1)).thenReturn(true);
        when(attribRule1.process(systemLeftAttribute2)).thenReturn(false);
        when(attribRule2.process(systemLeftAttribute1)).thenReturn(true);
        when(attribRule2.process(systemLeftAttribute2)).thenReturn(false);

        // Check situation before processing
        assertFalse(conditionWithAttributeRules.isProcessed());
        assertFalse(conditionWithAttributeRules.isProcessedSuccessfully());
        assertFalse(conditionWithAttributeRules.isProcessedUnsuccessfully());

        // Check results: some rules true, some rules false, accumulated result should be false
        assertFalse(conditionWithAttributeRules.process(systemEdge, patternEdge));
    }

}
