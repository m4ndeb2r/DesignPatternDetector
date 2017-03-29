package nl.ou.dpd.domain;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.TestCase.assertFalse;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.when;

/**
 * Test the {@link MatchedClasses} class.
 *
 * @author Martin de Boer
 */
@RunWith(MockitoJUnitRunner.class)
public class MatchedClassesTest {

    private MatchedClasses matchedClasses;

    @Mock
    private Edge systemEdge;

    @Mock
    private Clazz systemClass1;

    @Mock
    private Clazz systemClass2;

    @Mock
    private Edge patternEdge;

    @Mock
    private Clazz patternClass1;

    @Mock
    private Clazz patternClass2;

    /**
     * Initialize the test(s).
     */
    @Before
    public void setUp() {
        matchedClasses = new MatchedClasses();
    }

    /**
     * Tests the {@link MatchedClasses#canMatch(Edge, Edge)} method for handling incompatible edge types correctly.
     */
    @Test
    public void testCanNotMatchIncompatibleEdgeTypes() {
        when(systemEdge.getTypeRelation()).thenReturn(EdgeType.AGGREGATE);
        when(patternEdge.getTypeRelation()).thenReturn(EdgeType.ASSOCIATION);

        matchedClasses.prepareMatch(systemEdge);
        assertFalse(matchedClasses.canMatch(systemEdge, patternEdge));
    }

    /**
     * Tests the {@link MatchedClasses#canMatch(Edge, Edge)} method for handling incompatible selfRef values correctly.
     */
    @Test
    public void testCanNotMatchIncompatibleSelfRefs() {
        when(systemEdge.getTypeRelation()).thenReturn(EdgeType.INHERITANCE);
        when(systemEdge.isSelfRef()).thenReturn(true);

        when(patternEdge.getTypeRelation()).thenReturn(EdgeType.INHERITANCE_MULTI);
        when(patternEdge.isSelfRef()).thenReturn(false);

        matchedClasses.prepareMatch(systemEdge);
        assertFalse(matchedClasses.canMatch(systemEdge, patternEdge));
    }

    /**
     * Tests the {@link MatchedClasses#canMatch(Edge, Edge)} method for handling unbound edges (system edges have empty
     * classed and pattern edges are not in the list of matched edges).
     */
    @Test
    public void testCanMatchUnboundPatternEdges() {
        when(systemEdge.getTypeRelation()).thenReturn(EdgeType.INHERITANCE);
        when(systemEdge.isSelfRef()).thenReturn(false);
        when(systemEdge.getClass1()).thenReturn(Clazz.EMPTY_CLASS);
        when(systemEdge.getClass2()).thenReturn(Clazz.EMPTY_CLASS);

        when(patternEdge.getTypeRelation()).thenReturn(EdgeType.INHERITANCE_MULTI);
        when(patternEdge.isSelfRef()).thenReturn(false);
        when(patternEdge.getClass1()).thenReturn(patternClass1);
        when(patternEdge.getClass2()).thenReturn(patternClass2);

        matchedClasses.prepareMatch(systemEdge);
        assertTrue(matchedClasses.canMatch(systemEdge, patternEdge));
    }

    /**
     * Tests the {@link MatchedClasses#canMatch(Edge, Edge)} method for handling bound dsign pattern edges that are
     * already matched to other system edges. The pattern edges are therefore not matchable anymore. This must result
     * in canMatch == false.
     */
    @Test
    public void testCanNotMatchBoundPatternEdges() {
        when(systemEdge.getTypeRelation()).thenReturn(EdgeType.INHERITANCE);
        when(systemEdge.isSelfRef()).thenReturn(false);
        when(systemEdge.getClass1()).thenReturn(Clazz.EMPTY_CLASS);
        when(systemEdge.getClass2()).thenReturn(Clazz.EMPTY_CLASS);

        when(patternEdge.getTypeRelation()).thenReturn(EdgeType.INHERITANCE_MULTI);
        when(patternEdge.isSelfRef()).thenReturn(false);
        when(patternEdge.getClass1()).thenReturn(patternClass1);
        when(patternEdge.getClass2()).thenReturn(patternClass2);

        matchedClasses.prepareMatch(systemEdge);

        // Bind the patternEdge to thePartyPooper; after this, we cannot match the systemEdge to the patternEdge,
        // because the latter is already bound to thePartyPooper.
        final Edge thePartyPooper = new Edge(systemClass1, systemClass2, EdgeType.INHERITANCE);
        matchedClasses.makeMatch(thePartyPooper, patternEdge);

        // No match possible
        assertFalse(matchedClasses.canMatch(this.systemEdge, patternEdge));
    }

    /**
     * Tests the {@link MatchedClasses#canMatch(Edge, Edge)} method for handling edges that are already successfully
     * matched to each other. Since that match already succeeded, canMatch will return {@code true}.
     */
    @Test
    public void testCanMatchBoundPatternEdges() {
        when(systemEdge.getTypeRelation()).thenReturn(EdgeType.INHERITANCE);
        when(systemEdge.isSelfRef()).thenReturn(false);
        when(systemEdge.getClass1()).thenReturn(systemClass1);
        when(systemEdge.getClass2()).thenReturn(systemClass2);

        when(patternEdge.getTypeRelation()).thenReturn(EdgeType.INHERITANCE_MULTI);
        when(patternEdge.isSelfRef()).thenReturn(false);
        when(patternEdge.getClass1()).thenReturn(patternClass1);
        when(patternEdge.getClass2()).thenReturn(patternClass2);

        matchedClasses.prepareMatch(systemEdge);

        // Bind the patternEdge to the systemEdge.
        matchedClasses.makeMatch(systemEdge, patternEdge);

        // Match possible (in fact, already matched)
        assertTrue(matchedClasses.canMatch(this.systemEdge, patternEdge));
    }

    /**
     * Tests the {@link MatchedClasses#canMatch(Edge, Edge)} method for handling edges that partly bound, but the left
     * class is still matchable.
     */
    @Test
    public void testCanMatchOnTheLeft() {
        when(systemEdge.getTypeRelation()).thenReturn(EdgeType.INHERITANCE);
        when(systemEdge.isSelfRef()).thenReturn(false);
        when(systemEdge.getClass1()).thenReturn(systemClass1);
        when(systemEdge.getClass2()).thenReturn(systemClass2);

        when(patternEdge.getTypeRelation()).thenReturn(EdgeType.INHERITANCE_MULTI);
        when(patternEdge.isSelfRef()).thenReturn(false);
        when(patternEdge.getClass1()).thenReturn(patternClass1);
        when(patternEdge.getClass2()).thenReturn(patternClass2);

        matchedClasses.prepareMatch(systemEdge);

        // Bind the systemEdge to some pattern edge that has the same "right" class as the patternEdge. After this,
        // both the systemEdge and the patternEdge have their right classes bound to each other, but both their "left"
        // classes are unbound.
        matchedClasses.makeMatch(
                new Edge(new Clazz("Y"), systemClass2, EdgeType.INHERITANCE),
                new Edge(new Clazz("X"), patternClass2, EdgeType.INHERITANCE)
        );

        // Match is possible
        assertTrue(matchedClasses.canMatch(this.systemEdge, patternEdge));
    }

    /**
     * Tests the {@link MatchedClasses#canMatch(Edge, Edge)} method for handling edges that partly bound, but the right
     * class is still matchable.
     */
    @Test
    public void testCanMatchOnTheRight() {
        when(systemEdge.getTypeRelation()).thenReturn(EdgeType.INHERITANCE);
        when(systemEdge.isSelfRef()).thenReturn(false);
        when(systemEdge.getClass1()).thenReturn(systemClass1);
        when(systemEdge.getClass2()).thenReturn(systemClass2);

        when(patternEdge.getTypeRelation()).thenReturn(EdgeType.INHERITANCE_MULTI);
        when(patternEdge.isSelfRef()).thenReturn(false);
        when(patternEdge.getClass1()).thenReturn(patternClass1);
        when(patternEdge.getClass2()).thenReturn(patternClass2);

        matchedClasses.prepareMatch(systemEdge);

        // Bind the systemEdge to some pattern edge that has the same "left" class as the patternEdge. After this,
        // both the systemEdge and the patternEdge have their left classes bound to each other, but both their "right"
        // classes are unbound.
        matchedClasses.makeMatch(
                new Edge(systemClass1, new Clazz("Y"), EdgeType.INHERITANCE),
                new Edge(patternClass1, new Clazz("X"), EdgeType.INHERITANCE)
        );

        // Match is possible
        assertTrue(matchedClasses.canMatch(this.systemEdge, patternEdge));
    }
}
