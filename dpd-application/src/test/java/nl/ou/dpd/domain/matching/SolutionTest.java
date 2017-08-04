package nl.ou.dpd.domain.matching;

import nl.ou.dpd.domain.node.Node;
import nl.ou.dpd.domain.relation.Relation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Tests the {@link Solution} class.
 *
 * @author Martin de Boer
 */
@RunWith(MockitoJUnitRunner.class)
public class SolutionTest {

    private static final String MY_PATTERN = "myPattern";
    private static final String PATTERN_FAMILY = "patternFamily";

    private Solution solution;

    @Mock
    private Node systemNode, systemNode2, patternNode, patternNode2;

    @Mock
    private Relation systemRelation, patternRelation, patternRelation2;

    @Before
    public void initSolution() {
        solution = new Solution(MY_PATTERN, PATTERN_FAMILY);
    }

    @Before
    public void initNodeMocks() {
        when(systemNode.getName()).thenReturn("systemNode");
        when(systemNode.getId()).thenReturn("SystemNode");

        when(systemNode2.getName()).thenReturn("systemNode2");
        when(systemNode2.getId()).thenReturn("SystemNode2");

        when(patternNode.getName()).thenReturn("patternNode");
        when(patternNode.getId()).thenReturn("PatternNode");

        when(patternNode2.getName()).thenReturn("patternNode2");
        when(patternNode2.getId()).thenReturn("PatternNode2");
    }

    @Test
    public void testConstructor() {
        assertThat(solution.getDesignPatternName(), is(MY_PATTERN));
        assertThat(solution.getPatternFamilyName(), is(PATTERN_FAMILY));
        assertThat(solution.getMatchingNodes().size(), is(0));
        assertThat(solution.getMatchingRelations().size(), is(0));
        assertThat(solution.getMatchingNodeNames().size(), is(0));
    }

    @Test
    public void testAddMatchingNodes() {
        solution.addMatchingNodes(systemNode, patternNode);
        assertMostRecentlyAddedNodes(systemNode, patternNode, 1);

        // Adding the same nodes twice should be prevented: expect no changes
        solution.addMatchingNodes(systemNode, patternNode);
        assertMostRecentlyAddedNodes(systemNode, patternNode, 1);

        // Adding a different set of nodes should result in changes
        solution.addMatchingNodes(systemNode, patternNode2);
        assertMostRecentlyAddedNodes(systemNode, patternNode2, 2);
    }

    private void assertMostRecentlyAddedNodes(Node systemNode, Node patternNode, int expectedSize) {
        assertThat(solution.getMatchingNodes().size(), is(expectedSize));
        assertThat(solution.getMatchingNodeNames().size(), is(expectedSize));

        final Node[] nodes = solution.getMatchingNodes().get(expectedSize - 1);
        assertThat(nodes.length, is(2));
        assertThat(nodes[0], is(systemNode));
        assertThat(nodes[1], is(patternNode));

        final String[] nodeNames = solution.getMatchingNodeNames().get(expectedSize - 1);
        assertThat(nodeNames.length, is(2));
        assertThat(nodeNames[0], is(systemNode.getName()));
        assertThat(nodeNames[1], is(patternNode.getName()));
    }

    @Test
    public void testAddMatchingRelations() {
        solution.addMatchingRelations(systemRelation, patternRelation);
        assertMostRecentlyAddedRelations(systemRelation, patternRelation, 1);

        // Adding the same relations twice should be prevented: expect no changes
        solution.addMatchingRelations(systemRelation, patternRelation);
        assertMostRecentlyAddedRelations(systemRelation, patternRelation, 1);

        // Adding a different set of relations should result in changes
        solution.addMatchingRelations(systemRelation, patternRelation2);
        assertMostRecentlyAddedRelations(systemRelation, patternRelation2, 2);
    }

    private void assertMostRecentlyAddedRelations(Relation systemRelation, Relation patternRelation, int expectedSize) {
        assertThat(solution.getMatchingRelations().size(), is(expectedSize));

        final Relation[] relations = solution.getMatchingRelations().get(expectedSize - 1);
        assertThat(relations.length, is(2));
        assertThat(relations[0], is(systemRelation));
        assertThat(relations[1], is(patternRelation));
    }

    @Test
    public void testIsSimilar() {
        solution.addMatchingNodes(systemNode, patternNode);
        solution.addMatchingRelations(systemRelation, patternRelation);
        final Solution other = new Solution(solution.getDesignPatternName(), solution.getPatternFamilyName());
        assertFalse(solution.isSimilar(other));

        other.addMatchingNodes(systemNode, patternNode2);
        assertFalse(solution.isSimilar(other));

        other.addMatchingRelations(systemRelation, patternRelation2);
        assertTrue(solution.isSimilar(other));

        other.addMatchingNodes(systemNode2, patternNode2);
        assertFalse(solution.isSimilar(other));

        solution.addMatchingNodes(systemNode2, patternNode2);
        assertTrue(solution.isSimilar(other));

        final Solution solutionWithDifferentName = new Solution("different", solution.getPatternFamilyName());
        assertFalse(solution.isSimilar(solutionWithDifferentName));

        final Solution solutionWithDifferentFamilyName = new Solution(solution.getDesignPatternName(), "different");
        assertFalse(solution.isSimilar(solutionWithDifferentFamilyName));
    }

}
