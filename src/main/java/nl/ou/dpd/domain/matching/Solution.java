package nl.ou.dpd.domain.matching;

import nl.ou.dpd.domain.node.Node;
import nl.ou.dpd.domain.relation.Relation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A container for matching {@link Node}s for a design pattern, detected during the matching process.
 *
 * @author Martin de Boer
 */
public class Solution {

    private final static int SYSTEM_IDX = 0;
    private final static int PATTERN_IDX = 1;

    private final String designPatternName;
    private final List<Node[]> matchingNodes;
    private final List<Relation[]> matchingRelations;

    public Solution(String designPatternName) {
        this.matchingNodes = new ArrayList<>();
        this.matchingRelations = new ArrayList<>();
        this.designPatternName = designPatternName;
    }

    public String getDesignPatternName() {
        return designPatternName;
    }

    public List<Node[]> getMatchingNodes() {
        return matchingNodes;
    }

    public List<String[]> getMatchingNodeNames() {
        return getMatchingNodes().stream()
                .map(nodes -> new String[]{nodes[SYSTEM_IDX].getName(), nodes[PATTERN_IDX].getName()})
                .collect(Collectors.toList());
    }

    public List<Relation[]> getMatchingRelations() {
        return this.matchingRelations;
    }

    public Solution addMatchingNodes(Node systemNode, Node patternNode) {
        if (!previouslyAddedMatchingNodes(systemNode, patternNode)) {
            matchingNodes.add(new Node[]{systemNode, patternNode});
        }
        return this;
    }

    private boolean previouslyAddedMatchingNodes(Node systemNode, Node patternNode) {
        for (Node[] nodes : getMatchingNodes()) {
            if (systemNode.equals(nodes[SYSTEM_IDX]) && patternNode.equals(nodes[PATTERN_IDX])) {
                return true;
            }
        }
        return false;
    }

    public Solution addMatchingRelations(Relation systemRelation, Relation patternRelation) {
        if (!previouslyAddedMatchingRelations(systemRelation, patternRelation)) {
            this.matchingRelations.add(new Relation[]{systemRelation, patternRelation});
        }
        return this;
    }

    private boolean previouslyAddedMatchingRelations(Relation systemRelation, Relation patternRelation) {
        for (Relation[] relations : getMatchingRelations()) {
            if (systemRelation.equals(relations[SYSTEM_IDX]) && patternRelation.equals(relations[PATTERN_IDX])) {
                return true;
            }
        }
        return false;
    }
}
