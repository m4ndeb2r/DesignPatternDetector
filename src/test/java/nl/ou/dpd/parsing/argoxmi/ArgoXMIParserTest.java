package nl.ou.dpd.parsing.argoxmi;

import nl.ou.dpd.domain.SystemUnderConsideration;
import nl.ou.dpd.domain.edge.Edge;
import nl.ou.dpd.domain.edge.EdgeType;
import nl.ou.dpd.parsing.ParseException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.xml.sax.SAXParseException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Tests the {@link ArgoXMIParser} class.
 *
 * @author Martin de Boer
 */
public class ArgoXMIParserTest {

    // A test file containing invalid XMI.
    private static final String INVALID_XMI = "/invalid.xmi";
    // A test file containing the Ba Brahem "system under consideration" example.
    private static final String BA_BRAHEM_TEST_XMI = "/Ba_Brahem.xmi";
    // A test file containing the default input.xmi
    private static final String DEFAUL_INPUT_XMI = "/input.xmi";

    // Test files containing XMI exports of strategy UMLs.
    private static final String STRATEGY_XMI = "/Strategy.xmi";
    private static final String ABSTRACT_FACTORY_XMI = "/AbstractFactory.xmi";

    /**
     * Exception rule.
     */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Tests the exception handling in case of a {@link SAXParseException} during parsing input by an {@link ArgoXMIParser}
     * instance.
     */
    @Test
    public void testSAXParseException() {
        final String path = getPath(INVALID_XMI);
        final ArgoXMIParser argoXMIParser = new ArgoXMIParser();

        thrown.expect(ParseException.class);
        thrown.expectCause(is(SAXParseException.class));
        thrown.expectMessage("The file " + path + " could not be parsed.");

        argoXMIParser.parse(path);
    }

    /**
     * Tests the exception handling in case of a {@link IOException} during parsing input by an {@link ArgoXMIParser}
     * instance.
     */
    @Test
    public void testFileNotFoundException() {
        final ArgoXMIParser argoXMIParser = new ArgoXMIParser();

        thrown.expect(ParseException.class);
        thrown.expectCause(is(FileNotFoundException.class));
        thrown.expectMessage("The file oops.xmi could not be found.");

        argoXMIParser.parse("oops.xmi");
    }

    /**
     * Test the happy flow of parsing a Ba_Brahem XMI input file by the {@link ArgoXMIParser}.
     */
    @Test
    public void testBa_BrahemXmiParse() {
        final SystemUnderConsideration system = new SystemUnderConsideration();
        final ArgoXMIParser argoXMIParser = new ArgoXMIParser();
        final String path = getPath(BA_BRAHEM_TEST_XMI);

        final List<Edge> edges = argoXMIParser.parse(path).getEdges();

        assertThat(edges.size(), is(6));
        assertEdge(edges.get(0), "D", "E", EdgeType.DEPENDENCY);
        assertEdge(edges.get(1), "E", "C", EdgeType.INHERITANCE);
        assertEdge(edges.get(2), "D", "B", EdgeType.INHERITANCE);
        assertEdge(edges.get(3), "C", "B", EdgeType.ASSOCIATION_DIRECTED);
        assertEdge(edges.get(4), "A", "B", EdgeType.ASSOCIATION_DIRECTED);
        assertEdge(edges.get(5), "A", "C", EdgeType.ASSOCIATION_DIRECTED);
    }

    /**
     * Test the happy flow of parsing an AbstractFactory XMI input file by the {@link ArgoXMIParser}.
     */
    @Test
    public void testAbstractFactoryXmiParse() {
        final SystemUnderConsideration system = new SystemUnderConsideration();
        final ArgoXMIParser argoXMIParser = new ArgoXMIParser();
        final String path = getPath(ABSTRACT_FACTORY_XMI);

        final List<Edge> edges = argoXMIParser.parse(path).getEdges();

        assertThat(edges.size(), is(18));
        assertEdge(edges.get(0), "ConcFact1", "Prod1A", EdgeType.DEPENDENCY);
        assertEdge(edges.get(1), "ConcFact1", "Prod1B", EdgeType.DEPENDENCY);
        assertEdge(edges.get(2), "ConcFact1", "Prod1C", EdgeType.DEPENDENCY);
        assertEdge(edges.get(3), "ConcFact2", "Prod2A", EdgeType.DEPENDENCY);
        assertEdge(edges.get(4), "ConcFact2", "Prod2B", EdgeType.DEPENDENCY);
        assertEdge(edges.get(5), "ConcFact2", "Prod2C", EdgeType.DEPENDENCY);
        assertEdge(edges.get(6), "Prod1A", "AbstrProdA", EdgeType.INHERITANCE);
        assertEdge(edges.get(7), "Prod2A", "AbstrProdA", EdgeType.INHERITANCE);
        assertEdge(edges.get(8), "Prod1B", "AbstrProdB", EdgeType.INHERITANCE);
        assertEdge(edges.get(9), "Prod2B", "AbstrProdB", EdgeType.INHERITANCE);
        assertEdge(edges.get(10), "ConcFact1", "AbstrFact", EdgeType.INHERITANCE);
        assertEdge(edges.get(11), "ConcFact2", "AbstrFact", EdgeType.INHERITANCE);
        assertEdge(edges.get(12), "Prod1C", "AbstrProdC", EdgeType.INHERITANCE);
        assertEdge(edges.get(13), "Prod2C", "AbstrProdC", EdgeType.INHERITANCE);
        assertEdge(edges.get(14), "User", "AbstrFact", EdgeType.ASSOCIATION_DIRECTED);
        assertEdge(edges.get(15), "User", "AbstrProdA", EdgeType.ASSOCIATION_DIRECTED);
        assertEdge(edges.get(16), "User", "AbstrProdB", EdgeType.ASSOCIATION_DIRECTED);
        assertEdge(edges.get(17), "User", "AbstrProdC", EdgeType.ASSOCIATION_DIRECTED);
    }

    /**
     * Test the happy flow of parsing a Strategy XMI input file by the {@link ArgoXMIParser}.
     */
    @Test
    public void testStrategyXmiParse() {
        final SystemUnderConsideration system = new SystemUnderConsideration();
        final ArgoXMIParser argoXMIParser = new ArgoXMIParser();
        final String path = getPath(STRATEGY_XMI);

        final List<Edge> edges = argoXMIParser.parse(path).getEdges();

        assertThat(edges.size(), is(4));
        assertEdge(edges.get(0), "ConcrStratB", "Strat", EdgeType.INHERITANCE);
        assertEdge(edges.get(1), "ConcrStratC", "Strat", EdgeType.INHERITANCE);
        assertEdge(edges.get(2), "ConcrStratA", "Strat", EdgeType.INHERITANCE);
        assertEdge(edges.get(3), "Strat", "Cont", EdgeType.AGGREGATE);
    }

    private void assertEdge(Edge edge, String leftNodeName, String rightNodeName, EdgeType edgeType) {
        assertThat(edge.getLeftNode().getName(), is(leftNodeName));
        assertThat(edge.getRightNode().getName(), is(rightNodeName));
        assertThat(edge.getRelationType(), is(edgeType));
        assertThat(edge.isSelfRef(), is(false));
        assertThat(edge.isVirtual(), is(false));
        assertThat(edge.isLocked(), is(false));
    }

    private String getPath(String resourceName) {
        return this.getClass().getResource(resourceName).getPath();
    }
}
