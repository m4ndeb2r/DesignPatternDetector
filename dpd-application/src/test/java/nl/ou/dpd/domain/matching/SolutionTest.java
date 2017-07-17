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

    private Solution solution;

    @Mock
    private Node systemNode, systemNode2, patternNode, patternNode2;

    @Mock
    private Relation systemRelation, patternRelation, patternRelation2;

    @Before
    public void initSolution() {
        solution = new Solution("myPattern", "patternFamily");
    }

    @Before
    public void initNodeMocks() {
        when(systemNode.getName()).thenReturn("systemNode");
        when(systemNode.getId()).thenReturn("systemNode");

        when(systemNode2.getName()).thenReturn("systemNode2");
        when(systemNode2.getId()).thenReturn("systemNode2");

        when(patternNode.getName()).thenReturn("patternNode");
        when(patternNode.getId()).thenReturn("patternNode");

        when(patternNode2.getName()).thenReturn("patternNode2");
        when(patternNode2.getId()).thenReturn("patternNode2");
    }

    @Test
    public void testConstructor() {
        assertThat(solution.getDesignPatternName(), is("myPattern"));
        assertThat(solution.getPatternFamilyName(), is("patternFamily"));
        assertThat(solution.getMatchingNodes().size(), is(0));
        assertThat(solution.getMatchingRelations().size(), is(0));
        assertThat(solution.getMatchingNodeNames().size(), is(0));
    }

    @Test
    public void testAddMatchingNodes() {
        solution.addMatchingNodes(systemNode, patternNode);
        assertThat(solution.getMatchingNodes().size(), is(1));
        assertThat(solution.getMatchingNodeNames().size(), is(1));

        Node[] nodes = solution.getMatchingNodes().get(0);
        assertThat(nodes.length, is(2));
        assertThat(nodes[0], is(systemNode));
        assertThat(nodes[1], is(patternNode));

        String[] nodeNames = solution.getMatchingNodeNames().get(0);
        assertThat(nodeNames.length, is(2));
        assertThat(nodeNames[0], is("systemNode"));
        assertThat(nodeNames[1], is("patternNode"));

        // Adding the same nodes twice should be prevented: expect no changes
        solution.addMatchingNodes(systemNode, patternNode);
        assertThat(solution.getMatchingNodes().size(), is(1));
        assertThat(solution.getMatchingNodeNames().size(), is(1));

        // Adding a different set of nodes should result in changes
        solution.addMatchingNodes(systemNode, patternNode2);
        assertThat(solution.getMatchingNodes().size(), is(2));
        assertThat(solution.getMatchingNodeNames().size(), is(2));

        nodes = solution.getMatchingNodes().get(1);
        assertThat(nodes.length, is(2));
        assertThat(nodes[0], is(systemNode));
        assertThat(nodes[1], is(patternNode2));

        nodeNames = solution.getMatchingNodeNames().get(1);
        assertThat(nodeNames.length, is(2));
        assertThat(nodeNames[0], is("systemNode"));
        assertThat(nodeNames[1], is("patternNode2"));
    }

    @Test
    public void testAddMatchingRelations() {
        solution.addMatchingRelations(systemRelation, patternRelation);
        assertThat(solution.getMatchingRelations().size(), is(1));

        Relation[] relations = solution.getMatchingRelations().get(0);
        assertThat(relations.length, is(2));
        assertThat(relations[0], is(systemRelation));
        assertThat(relations[1], is(patternRelation));

        // Adding the same nodes twice should be prevented: expect no changes
        solution.addMatchingRelations(systemRelation, patternRelation);
        assertThat(solution.getMatchingRelations().size(), is(1));

        // Adding a different set of nodes should result in changes
        solution.addMatchingRelations(systemRelation, patternRelation2);
        assertThat(solution.getMatchingRelations().size(), is(2));

        relations = solution.getMatchingRelations().get(1);
        assertThat(relations.length, is(2));
        assertThat(relations[0], is(systemRelation));
        assertThat(relations[1], is(patternRelation2));
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
