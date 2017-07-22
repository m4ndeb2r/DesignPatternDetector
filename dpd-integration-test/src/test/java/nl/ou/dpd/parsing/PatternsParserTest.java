package nl.ou.dpd.parsing;

import nl.ou.dpd.IntegrationTest;
import nl.ou.dpd.domain.DesignPattern;
import nl.ou.dpd.domain.node.Node;
import nl.ou.dpd.domain.node.NodeType;
import nl.ou.dpd.domain.relation.RelationProperty;
import nl.ou.dpd.domain.relation.RelationType;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.net.URL;
import java.util.List;
import java.util.Set;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Test xml-files against the patterns.xsd.
 *
 * @author Martin de Boer
 */
@Category(IntegrationTest.class)
public class PatternsParserTest {

    private PatternsParser parser;

    @Before
    public void initParser() {
        parser = ParserFactory.createPatternParser();
    }

    // TODO: Is this test still relevant, considering it is unsing the CLASS_WITH_PRIVATE_CONSTRUCTOR NodeType???
    @Test
    public void testParsingSingletonXml() {
        final URL xmlUrl = PatternsParserTest.class.getResource("/patterns/patterns_singleton.xml");
        final List<DesignPattern> designPatterns = parser.parse(xmlUrl.getFile());
        assertThat(designPatterns.size(), is(1));

        // Check name of pattern
        final DesignPattern designPattern = designPatterns.get(0);
        assertThat(designPattern.getName(), is("Singleton"));
        assertThat(designPattern.getFamily(), is("Singleton"));

        // Check number of edges and vertices
        assertThat(designPattern.edgeSet().size(), is(1));
        assertThat(designPattern.vertexSet().size(), is(1));

        // Check number of types
        final int[] count = {0};
        designPattern.edgeSet().forEach(relation -> count[0] += relation.getRelationProperties().size());
        assertThat(count[0], is(2));

        // Get nodes
        final Node singleton = getNodeById(designPattern, "Singleton");

        // Check types of nodes
        assertTrue(singleton.getTypes().contains(NodeType.CONCRETE_CLASS));
        assertTrue(singleton.getTypes().contains(NodeType.CLASS_WITH_PRIVATE_CONSTRUCTOR));

        // Check number of nodes by type
        int countSingleton = 0;
        for (Node node : designPattern.vertexSet()) {
            if (node == singleton) countSingleton++;
        }
        assertThat(countSingleton, is(1));

        // Check types of relations
        assertTypes(designPattern, singleton, singleton, new RelationType[]{RelationType.CREATES_INSTANCE_OF, RelationType.HAS_ATTRIBUTE_OF});
    }

    @Test
    public void testParsingObserverXml() {
        final URL xmlUrl = PatternsParserTest.class.getResource("/patterns/patterns_observer.xml");
        final List<DesignPattern> designPatterns = parser.parse(xmlUrl.getFile());
        assertThat(designPatterns.size(), is(1));

        // Check name of pattern
        final DesignPattern designPattern = designPatterns.get(0);
        assertThat(designPattern.getName(), is("Observer"));
        assertThat(designPattern.getFamily(), is("Observer"));

        // Check number of edges and vertices
        assertThat(designPattern.edgeSet().size(), is(4));
        assertThat(designPattern.vertexSet().size(), is(4));

        // Check number of types
        final int[] count = {0};
        designPattern.edgeSet().forEach(relation -> count[0] += relation.getRelationProperties().size());
        assertThat(count[0], is(7));

        // Get nodes
        final Node subject = getNodeById(designPattern, "Subject");
        final Node concreteSubject = getNodeById(designPattern, "ConcreteSubject");
        final Node observer = getNodeById(designPattern, "Observer");
        final Node concreteObserver = getNodeById(designPattern, "ConcreteObserver");

        // Check types of nodes
        assertTrue(subject.getTypes().contains(NodeType.ABSTRACT_CLASS));
        assertTrue(concreteSubject.getTypes().contains(NodeType.CONCRETE_CLASS));
        assertTrue(observer.getTypes().contains(NodeType.INTERFACE));
        assertTrue(concreteObserver.getTypes().contains(NodeType.CONCRETE_CLASS));

        // Check number of nodes by type
        int countSubject = 0;
        int countObserver = 0;
        int countConcreteSubject = 0;
        int countConcreteObserver = 0;
        for (Node node : designPattern.vertexSet()) {
            if (node == subject) countSubject++;
            if (node == observer) countObserver++;
            if (node == concreteSubject) countConcreteSubject++;
            if (node == concreteObserver) countConcreteObserver++;
        }
        assertThat(countSubject, is(1));
        assertThat(countObserver, is(1));
        assertThat(countConcreteSubject, is(1));
        assertThat(countConcreteObserver, is(1));

        // Check types of relations
        assertTypes(designPattern, subject, observer, new RelationType[]{RelationType.HAS_ATTRIBUTE_OF, RelationType.ASSOCIATES_WITH, RelationType.HAS_METHOD_PARAMETER_OF_TYPE});
        assertTypes(designPattern, concreteSubject, subject, new RelationType[]{RelationType.INHERITS_FROM});
        assertTypes(designPattern, concreteObserver, concreteSubject, new RelationType[]{RelationType.HAS_ATTRIBUTE_OF, RelationType.ASSOCIATES_WITH});
        assertTypes(designPattern, concreteObserver, observer, new RelationType[]{RelationType.IMPLEMENTS});
    }

    private void assertTypes(DesignPattern designPattern, Node node1, Node node2, RelationType[] relationTypes) {
        for (int i = 0; i < relationTypes.length; i++) {
            final RelationType type = relationTypes[i];
            final Set<RelationProperty> relationProperties = designPattern.getEdge(node1, node2).getRelationProperties();
            assertNotNull(relationProperties.stream()
                    .filter(properties -> properties.getRelationType().equals(type))
                    .findFirst()
                    .orElse(null));
        }
    }

    private Node getNodeById(DesignPattern designPattern, String id) {
        return designPattern.vertexSet().stream().filter(node -> node.getId().equals(id)).findFirst().orElse(null);
    }

}
