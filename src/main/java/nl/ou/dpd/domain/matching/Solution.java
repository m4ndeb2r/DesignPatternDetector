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

    private final String designPatternName;
    private final List<Node[]> matchingNodes;
    private final List<Relation> matchingRelations;

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
                .map(nodes -> new String[]{nodes[0].getName(), nodes[1].getName()})
                .collect(Collectors.toList());
    }

    public Solution addMatchingNodes(Node systemNode, Node patternNode) {
        if (!previouslyAddedMatchingNodes(systemNode, patternNode)) {
            matchingNodes.add(new Node[]{systemNode, patternNode});
        }
        return this;
    }

    private boolean previouslyAddedMatchingNodes(Node systemNode, Node patternNode) {
        for (Node[] nodes : getMatchingNodes()) {
            if (systemNode.equals(nodes[0]) && patternNode.equals(nodes[1])) {
                return true;
            }
        }
        return false;
    }

    public Solution addMatchingRelation(Relation relation) {
        this.matchingRelations.add(relation);
        return this;
    }

    public List<Relation> getMatchingRelations() {
        return this.matchingRelations;
    }
}
