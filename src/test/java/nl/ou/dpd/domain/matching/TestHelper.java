package nl.ou.dpd.domain.matching;

import nl.ou.dpd.domain.DesignPattern;
import nl.ou.dpd.domain.matching.Feedback;
import nl.ou.dpd.domain.matching.FeedbackType;
import nl.ou.dpd.domain.matching.PatternInspector;
import nl.ou.dpd.domain.matching.Solution;
import nl.ou.dpd.domain.node.Node;
import nl.ou.dpd.domain.node.NodeType;
import nl.ou.dpd.domain.relation.Cardinality;
import nl.ou.dpd.domain.relation.RelationType;
import nl.ou.dpd.domain.SystemUnderConsideration;
import nl.ou.dpd.domain.relation.RelationProperty;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by martindeboer on 25-05-17.
 */
public class TestHelper {

    public static SystemUnderConsideration createSystemUnderConsiderationGraphWithObserver() {
        final SystemUnderConsideration result = new SystemUnderConsideration("System", "System");

        // Add required nodes
        final Node concreteSubject = new Node("aConcreteSubject", "aConcreteSubject", NodeType.CONCRETE_CLASS);
        final Node subject = new Node("aSubject", "aSubject", NodeType.ABSTRACT_CLASS);
        final Node observer = new Node("anObserver", "anObserver", NodeType.INTERFACE);
        final Node concreteObserver = new Node("aConcreteObserver", "aConcreteObserver", NodeType.CONCRETE_CLASS);
        result.addVertex(concreteSubject);
        result.addVertex(subject);
        result.addVertex(observer);
        result.addVertex(concreteObserver);

        // Add required edges
        result.addEdge(subject, observer)
                .addRelationProperty(new RelationProperty(RelationType.HAS_ATTRIBUTE_OF));

        result.addEdge(concreteSubject, subject)
                .addRelationProperty(new RelationProperty(RelationType.INHERITS_FROM))
                .addRelationProperty(new RelationProperty(RelationType.OVERRIDES_METHOD_OF));

        result.addEdge(concreteObserver, concreteSubject)
                .addRelationProperty(new RelationProperty(RelationType.CALLS_METHOD_OF))
                .addRelationProperty(new RelationProperty(RelationType.HAS_ATTRIBUTE_OF));

        result.addEdge(concreteObserver, observer)
                .addRelationProperty(new RelationProperty(RelationType.IMPLEMENTS));

        // Add superfluous nodes (not required for the pattern)
        final Node client = new Node("aClient", "aClient", NodeType.CONCRETE_CLASS);
        final Node dummy = new Node("aDummy", "aDummy", NodeType.CONCRETE_CLASS);
        result.addVertex(client);
        result.addVertex(dummy);

        // Add superfluous edges (not required for the pattern)
        result.addEdge(client, subject)
                .addRelationProperty(new RelationProperty(RelationType.HAS_ATTRIBUTE_OF))
                .addRelationProperty(new RelationProperty(RelationType.CALLS_METHOD_OF));
        result.addEdge(concreteObserver, dummy)
                .addRelationProperty(new RelationProperty(RelationType.INHERITS_FROM));

        return result;
    }

    public static SystemUnderConsideration createSystemUnderConsiderationGraphWithObserverWithWrongCardinality() {
        final SystemUnderConsideration result = new SystemUnderConsideration("System", "System");

        // Add required nodes
        final Node concreteSubject = new Node("aConcreteSubject", "aConcreteSubject", NodeType.CONCRETE_CLASS);
        final Node subject = new Node("aSubject", "aSubject", NodeType.ABSTRACT_CLASS);
        final Node observer = new Node("anObserver", "anObserver", NodeType.INTERFACE);
        final Node concreteObserver = new Node("aConcreteObserver", "aConcreteObserver", NodeType.CONCRETE_CLASS);
        result.addVertex(concreteSubject);
        result.addVertex(subject);
        result.addVertex(observer);
        result.addVertex(concreteObserver);

        // Add required edges
        result.addEdge(subject, observer)
                .addRelationProperty(new RelationProperty(RelationType.HAS_ATTRIBUTE_OF));

        result.addEdge(concreteSubject, subject)
                .addRelationProperty(new RelationProperty(RelationType.INHERITS_FROM))
                .addRelationProperty(new RelationProperty(RelationType.OVERRIDES_METHOD_OF));

        result.addEdge(concreteObserver, concreteSubject)
                .addRelationProperty(new RelationProperty(RelationType.CALLS_METHOD_OF))
                .addRelationProperty(new RelationProperty(RelationType.HAS_ATTRIBUTE_OF));

        result.addEdge(concreteObserver, observer)
                .addRelationProperty(new RelationProperty(RelationType.IMPLEMENTS, Cardinality.valueOf("2"), Cardinality.valueOf("1")));

        // Add superfluous nodes (not required for the pattern)
        final Node client = new Node("aClient", "aClient", NodeType.CONCRETE_CLASS);
        final Node dummy = new Node("aDummy", "aDummy", NodeType.CONCRETE_CLASS);
        result.addVertex(client);
        result.addVertex(dummy);

        // Add superfluous edges (not required for the pattern)
        result.addEdge(client, subject)
                .addRelationProperty(new RelationProperty(RelationType.HAS_ATTRIBUTE_OF))
                .addRelationProperty(new RelationProperty(RelationType.CALLS_METHOD_OF));
        result.addEdge(concreteObserver, dummy)
                .addRelationProperty(new RelationProperty(RelationType.INHERITS_FROM));

        return result;
    }

    public static SystemUnderConsideration createSystemUnderConsiderationGraphWithTwoObserversAndOneSingleton() {
        final SystemUnderConsideration result = new SystemUnderConsideration("System",  "System");

        // Add required nodes for observer 1
        final Node concreteSubject1 = new Node("firstConcreteSubject", "firstConcreteSubject", NodeType.CONCRETE_CLASS);
        final Node subject1 = new Node("firstSubject", "firstSubject", NodeType.ABSTRACT_CLASS);
        final Node observer1 = new Node("firstObserver", "firstObserver", NodeType.INTERFACE);
        final Node concreteObserver1 = new Node("firstConcreteObserver", "firstConcreteObserver", NodeType.CONCRETE_CLASS);
        result.addVertex(concreteSubject1);
        result.addVertex(subject1);
        result.addVertex(observer1);
        result.addVertex(concreteObserver1);

        // Add required nodes for observer 2
        final Node concreteSubject2 = new Node("secondConcreteSubject", "secondConcreteSubject", NodeType.CONCRETE_CLASS);
        final Node subject2 = new Node("secondSubject", "secondSubject", NodeType.ABSTRACT_CLASS);
        final Node observer2 = new Node("secondObserver", "secondObserver", NodeType.INTERFACE);
        final Node concreteObserver2 = new Node("secondConcreteObserver", "secondConcreteObserver", NodeType.CONCRETE_CLASS);
        result.addVertex(concreteSubject2);
        result.addVertex(subject2);
        result.addVertex(observer2);
        result.addVertex(concreteObserver2);

        // Add required edges for observer 1
        result.addEdge(subject1, observer1)
                .addRelationProperty(new RelationProperty(RelationType.HAS_ATTRIBUTE_OF));

        result.addEdge(concreteSubject1, subject1)
                .addRelationProperty(new RelationProperty(RelationType.INHERITS_FROM))
                .addRelationProperty(new RelationProperty(RelationType.OVERRIDES_METHOD_OF));

        result.addEdge(concreteObserver1, concreteSubject1)
                .addRelationProperty(new RelationProperty(RelationType.CALLS_METHOD_OF))
                .addRelationProperty(new RelationProperty(RelationType.HAS_ATTRIBUTE_OF));

        result.addEdge(concreteObserver1, observer1)
                .addRelationProperty(new RelationProperty(RelationType.IMPLEMENTS));

        // Add required edges for observer 2
        result.addEdge(subject2, observer2)
                .addRelationProperty(new RelationProperty(RelationType.HAS_ATTRIBUTE_OF));

        result.addEdge(concreteSubject2, subject2)
                .addRelationProperty(new RelationProperty(RelationType.INHERITS_FROM))
                .addRelationProperty(new RelationProperty(RelationType.OVERRIDES_METHOD_OF));

        result.addEdge(concreteObserver2, concreteSubject2)
                .addRelationProperty(new RelationProperty(RelationType.CALLS_METHOD_OF))
                .addRelationProperty(new RelationProperty(RelationType.HAS_ATTRIBUTE_OF));

        result.addEdge(concreteObserver2, observer2)
                .addRelationProperty(new RelationProperty(RelationType.IMPLEMENTS));

        // Add the Singleton
        final Node singleton = new Node("aSingleton", "aSingleton");
        singleton.addType(NodeType.CONCRETE_CLASS);
        singleton.addType(NodeType.CLASS_WITH_PRIVATE_CONSTRUCTOR);
        result.addVertex(singleton);

        // Add the required edges for the Singleton
        result.addEdge(singleton, singleton)
                .addRelationProperty(new RelationProperty(RelationType.CREATES_INSTANCE_OF))
                .addRelationProperty(new RelationProperty(RelationType.HAS_ATTRIBUTE_OF));


        // Add superfluous nodes (not required for the pattern)
        final Node client1 = new Node("aClient", "aClient", NodeType.CONCRETE_CLASS);
        final Node client2 = new Node("anotherClient", "anotherClient", NodeType.CONCRETE_CLASS);
        final Node dummy1 = new Node("dummy1", "dummy1", NodeType.ABSTRACT_CLASS);
        final Node dummy2 = new Node("dummy2", "dummy2", NodeType.ABSTRACT_CLASS);
        result.addVertex(client1);
        result.addVertex(client2);
        result.addVertex(dummy1);
        result.addVertex(dummy2);

        // Connect the two patterns and superfluous edges
        result.addEdge(client1, subject1)
                .addRelationProperty(new RelationProperty(RelationType.HAS_ATTRIBUTE_OF));
        result.addEdge(client2, subject2)
                .addRelationProperty(new RelationProperty(RelationType.HAS_ATTRIBUTE_OF));
        result.addEdge(concreteObserver2, client2)
                .addRelationProperty(new RelationProperty(RelationType.HAS_ATTRIBUTE_OF));
        result.addEdge(client1, dummy1)
                .addRelationProperty(new RelationProperty(RelationType.HAS_ATTRIBUTE_OF));
        result.addEdge(subject1, dummy2)
                .addRelationProperty(new RelationProperty(RelationType.HAS_ATTRIBUTE_OF));
        result.addEdge(observer2, singleton)
                .addRelationProperty(new RelationProperty(RelationType.HAS_ATTRIBUTE_OF));

        return result;
    }

    /**
     * Temporary method to print feedback. TODO: should eventually be removed....
     *
     * @param designPattern
     * @param system
     * @param patternInspector
     * @param solutions
     */
    public static void printFeedback(DesignPattern designPattern, SystemUnderConsideration system, PatternInspector patternInspector, List<Solution> solutions) {
        solutions.forEach(solution -> {
            System.out.println("---------------------------------------");
            System.out.println(solution.getDesignPatternName());
            System.out.println("---------------------------------------");
            solution.getMatchingNodeNames().forEach(names -> System.out.println(names[0] + " matches " + names[1]));
        });

        System.out.println("\n---------------------------------------");
        System.out.println(designPattern.getName() + " feedback");
        System.out.println("---------------------------------------");
        final Feedback feedback = patternInspector.getFeedback();

        System.out.println("Node feedback:");
        final Set<Node> nodes = new HashSet<>();
        system.edgeSet().iterator().forEachRemaining(r -> {
            nodes.add(system.getEdgeSource(r));
            nodes.add(system.getEdgeTarget(r));
        });
        nodes.iterator().forEachRemaining(node -> {
            System.out.println("\n\tNode: " + node.getName());
            feedback.getFeedbackMessages(node, FeedbackType.INFO).forEach(s -> System.out.println("\t- " + s));
            feedback.getFeedbackMessages(node, FeedbackType.MATCH).forEach(s -> System.out.println("\t- " + s));
            feedback.getFeedbackMessages(node, FeedbackType.MISMATCH).forEach(s -> System.out.println("\t- " + s));
            feedback.getFeedbackMessages(node, FeedbackType.NOT_ANALYSED).forEach(s -> System.out.println("\t- " + s));
        });

        System.out.println("\nRelation feedback:");
        system.edgeSet().iterator().forEachRemaining(r -> {
            System.out.println("\n\tRelation: " + r.getName());
            feedback.getFeedbackMessages(r, FeedbackType.INFO).forEach(s -> System.out.println("\t- " + s));
            feedback.getFeedbackMessages(r, FeedbackType.MATCH).forEach(s -> System.out.println("\t- " + s));
            feedback.getFeedbackMessages(r, FeedbackType.MISMATCH).forEach(s -> System.out.println("\t- " + s));
            feedback.getFeedbackMessages(r, FeedbackType.NOT_ANALYSED).forEach(s -> System.out.println("\t- " + s));
        });
    }


}
