package nl.ou.dpd.domain;

import nl.ou.dpd.domain.edge.Edge;
import nl.ou.dpd.domain.edge.EdgeType;
import nl.ou.dpd.domain.node.Clazz;
import nl.ou.dpd.domain.node.Node;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.TestCase.assertFalse;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Test the {@link MatchedNodes} class.
 *
 * @author Martin de Boer
 */
@RunWith(MockitoJUnitRunner.class)
public class MatchedNodesTest {

    private MatchedNodes matchedNodes;

    @Mock
    private Edge systemEdge;

    @Mock
    private Node systemNode1;

    @Mock
    private Node systemNode2;

    @Mock
    private Edge patternEdge;

    @Mock
    private Node patternNode1;

    @Mock
    private Node patternNode2;

    /**
     * Initialize the test(s).
     */
    @Before
    public void setUp() {
        matchedNodes = new MatchedNodes();
    }

    /**
     * Tests the {@link MatchedNodes#canMatch(Edge, Edge)} method for handling incompatible edge types correctly.
     */
    @Test
    public void testCanNotMatchIncompatibleEdgeTypes() {
        when(systemEdge.getRelationType()).thenReturn(EdgeType.AGGREGATE);
        when(patternEdge.getRelationType()).thenReturn(EdgeType.ASSOCIATION);

        matchedNodes.prepareMatch(systemEdge);
        assertFalse(matchedNodes.canMatch(systemEdge, patternEdge));
    }

    /**
     * Tests the {@link MatchedNodes#canMatch(Edge, Edge)} method for handling incompatible selfRef values correctly.
     */
    @Test
    public void testCanNotMatchIncompatibleSelfRefs() {
        when(systemEdge.getRelationType()).thenReturn(EdgeType.INHERITANCE);
        when(systemEdge.isSelfRef()).thenReturn(true);

        when(patternEdge.getRelationType()).thenReturn(EdgeType.INHERITANCE_MULTI);
        when(patternEdge.isSelfRef()).thenReturn(false);

        matchedNodes.prepareMatch(systemEdge);
        assertFalse(matchedNodes.canMatch(systemEdge, patternEdge));
    }

    /**
     * Tests the {@link MatchedNodes#canMatch(Edge, Edge)} method for handling unbound edges (system edges have empty
     * classed and pattern edges are not in the list of matched edges).
     */
    @Test
    public void testCanMatchUnboundPatternEdges() {
        when(systemEdge.getRelationType()).thenReturn(EdgeType.INHERITANCE);
        when(systemEdge.isSelfRef()).thenReturn(false);
        when(systemEdge.getLeftNode()).thenReturn(Node.EMPTY_NODE);
        when(systemEdge.getRightNode()).thenReturn(Node.EMPTY_NODE);

        when(patternEdge.getRelationType()).thenReturn(EdgeType.INHERITANCE_MULTI);
        when(patternEdge.isSelfRef()).thenReturn(false);
        when(patternEdge.getLeftNode()).thenReturn(patternNode1);
        when(patternEdge.getRightNode()).thenReturn(patternNode2);

        matchedNodes.prepareMatch(systemEdge);
        assertTrue(matchedNodes.canMatch(systemEdge, patternEdge));
    }

    /**
     * Tests the {@link MatchedNodes#canMatch(Edge, Edge)} method for handling bound dsign pattern edges that are
     * already matched to other system edges. The pattern edges are therefore not matchable anymore. This must result
     * in canMatch == false.
     */
    @Test
    public void testCanNotMatchBoundPatternEdges() {
        when(systemEdge.getRelationType()).thenReturn(EdgeType.INHERITANCE);
        when(systemEdge.isSelfRef()).thenReturn(false);
        when(systemEdge.getLeftNode()).thenReturn(Node.EMPTY_NODE);
        when(systemEdge.getRightNode()).thenReturn(Node.EMPTY_NODE);

        when(patternEdge.getRelationType()).thenReturn(EdgeType.INHERITANCE_MULTI);
        when(patternEdge.isSelfRef()).thenReturn(false);
        when(patternEdge.getLeftNode()).thenReturn(patternNode1);
        when(patternEdge.getRightNode()).thenReturn(patternNode2);

        matchedNodes.prepareMatch(systemEdge);

        // Bind the patternEdge to thePartyPooper; after this, we cannot match the systemEdge to the patternEdge,
        // because the latter is already bound to thePartyPooper.
        final Edge thePartyPooper = new Edge(systemNode1, systemNode2, EdgeType.INHERITANCE);
        matchedNodes.makeMatch(thePartyPooper, patternEdge);

        // No match possible
        assertFalse(matchedNodes.canMatch(this.systemEdge, patternEdge));
    }

    /**
     * Tests the {@link MatchedNodes#canMatch(Edge, Edge)} method for handling edges that are already successfully
     * matched to each other. Since that match already succeeded, canMatch will return {@code true}.
     */
    @Test
    public void testCanMatchBoundPatternEdges() {
        when(systemEdge.getRelationType()).thenReturn(EdgeType.INHERITANCE);
        when(systemEdge.isSelfRef()).thenReturn(false);
        when(systemEdge.getLeftNode()).thenReturn(systemNode1);
        when(systemEdge.getRightNode()).thenReturn(systemNode2);

        when(patternEdge.getRelationType()).thenReturn(EdgeType.INHERITANCE_MULTI);
        when(patternEdge.isSelfRef()).thenReturn(false);
        when(patternEdge.getLeftNode()).thenReturn(patternNode1);
        when(patternEdge.getRightNode()).thenReturn(patternNode2);

        matchedNodes.prepareMatch(systemEdge);

        // Bind the patternEdge to the systemEdge.
        matchedNodes.makeMatch(systemEdge, patternEdge);

        // Match possible (in fact, already matched)
        assertTrue(matchedNodes.canMatch(this.systemEdge, patternEdge));
    }

    /**
     * Tests the {@link MatchedNodes#canMatch(Edge, Edge)} method for handling edges that partly bound, but the left
     * class is still matchable.
     */
    @Test
    public void testCanMatchOnTheLeft() {
        when(systemEdge.getRelationType()).thenReturn(EdgeType.INHERITANCE);
        when(systemEdge.isSelfRef()).thenReturn(false);
        when(systemEdge.getLeftNode()).thenReturn(systemNode1);
        when(systemEdge.getRightNode()).thenReturn(systemNode2);

        when(patternEdge.getRelationType()).thenReturn(EdgeType.INHERITANCE_MULTI);
        when(patternEdge.isSelfRef()).thenReturn(false);
        when(patternEdge.getLeftNode()).thenReturn(patternNode1);
        when(patternEdge.getRightNode()).thenReturn(patternNode2);

        matchedNodes.prepareMatch(systemEdge);

        // Bind the systemEdge to some pattern edge that has the same "right" class as the patternEdge. After this,
        // both the systemEdge and the patternEdge have their right classes bound to each other, but both their "left"
        // classes are unbound.
        matchedNodes.makeMatch(
                new Edge(new Clazz("Y"), systemNode2, EdgeType.INHERITANCE),
                new Edge(new Clazz("X"), patternNode2, EdgeType.INHERITANCE)
        );

        // Match is possible
        assertTrue(matchedNodes.canMatch(this.systemEdge, patternEdge));
    }

    /**
     * Tests the {@link MatchedNodes#canMatch(Edge, Edge)} method for handling edges that partly bound, but the right
     * class is still matchable.
     */
    @Test
    public void testCanMatchOnTheRight() {
        when(systemEdge.getRelationType()).thenReturn(EdgeType.INHERITANCE);
        when(systemEdge.isSelfRef()).thenReturn(false);
        when(systemEdge.getLeftNode()).thenReturn(systemNode1);
        when(systemEdge.getRightNode()).thenReturn(systemNode2);

        when(patternEdge.getRelationType()).thenReturn(EdgeType.INHERITANCE_MULTI);
        when(patternEdge.isSelfRef()).thenReturn(false);
        when(patternEdge.getLeftNode()).thenReturn(patternNode1);
        when(patternEdge.getRightNode()).thenReturn(patternNode2);

        matchedNodes.prepareMatch(systemEdge);

        // Bind the systemEdge to some pattern edge that has the same "left" class as the patternEdge. After this,
        // both the systemEdge and the patternEdge have their left classes bound to each other, but both their "right"
        // classes are unbound.
        matchedNodes.makeMatch(
                new Edge(systemNode1, new Clazz("Y"), EdgeType.INHERITANCE),
                new Edge(patternNode1, new Clazz("X"), EdgeType.INHERITANCE)
        );

        // Match is possible
        assertTrue(matchedNodes.canMatch(this.systemEdge, patternEdge));
    }
}
