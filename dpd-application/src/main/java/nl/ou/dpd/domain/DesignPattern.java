package nl.ou.dpd.domain;

import nl.ou.dpd.domain.matching.CompoundComparator;
import nl.ou.dpd.domain.matching.FeedbackEnabledComparator;
import nl.ou.dpd.domain.node.Node;
import nl.ou.dpd.domain.relation.Relation;
import nl.ou.dpd.domain.relation.RelationFactory;
import org.jgrapht.graph.DefaultDirectedGraph;

import java.util.HashSet;
import java.util.Set;

import static nl.ou.dpd.util.Util.tidy;

/**
 * A {@link DesignPattern} is a {@link DefaultDirectedGraph} representation of a design pattern. It contains a set of
 * {@link CompoundComparator}s for matching purposes.
 *
 * @author Martin de Boer
 */
public class DesignPattern extends DefaultDirectedGraph<Node, Relation> {

    final private String name;
    final private String family;
    final private Set<String> notes;

    private FeedbackEnabledComparator<Relation> relationComparator;
    private FeedbackEnabledComparator<Node> nodeComparator;

    public DesignPattern(String name, String family) {
        super(new RelationFactory());
        this.name = name;
        this.family = family;
        this.notes = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public String getFamily() {
        return family;
    }

    public FeedbackEnabledComparator<Relation> getRelationComparator() {
        return relationComparator;
    }

    public DesignPattern setRelationComparator(FeedbackEnabledComparator<Relation> relationComparator) {
        this.relationComparator = relationComparator;
        return this;
    }

    public FeedbackEnabledComparator<Node> getNodeComparator() {
        return nodeComparator;
    }

    public DesignPattern setNodeComparator(FeedbackEnabledComparator<Node> nodeComparator) {
        this.nodeComparator = nodeComparator;
        return this;
    }

    public Set<String> getNotes() {
        return notes;
    }

    public DesignPattern addNote(String note) {
        this.notes.add(tidy(note));
        return this;
    }

}
