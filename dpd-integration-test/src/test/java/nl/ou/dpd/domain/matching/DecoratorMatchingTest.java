package nl.ou.dpd.domain.matching;

import nl.ou.dpd.IntegrationTest;
import nl.ou.dpd.domain.DesignPattern;
import nl.ou.dpd.domain.SystemUnderConsideration;
import nl.ou.dpd.domain.node.Node;
import nl.ou.dpd.domain.relation.Relation;
import nl.ou.dpd.parsing.ArgoUMLParser;
import nl.ou.dpd.parsing.ParserFactory;
import nl.ou.dpd.parsing.PatternsParser;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test the matching process for a Builder pattern.
 *
 * @author Martin de Boer
 */
@Category(IntegrationTest.class)
public class DecoratorMatchingTest extends AbstractMatchingTest {

    private static final String MATCHING_SYSTEM_XMI = "/systems/MyDecorator.xmi";
    private static final String MISMATCHING_SYSTEM_XMI = "/systems/MyBuilder.xmi";
    private static final String PATTERN_NAME = "Decorator";

    private DesignPattern designPattern;

    @Before
    public void initTests() {
        final PatternsParser patternsParser = ParserFactory.createPatternParser();
        final String patternsXmlFile = DecoratorMatchingTest.class.getResource(TEMPLATES_XML).getFile();
        designPattern = getDesignPatternByName(patternsParser.parse(patternsXmlFile), PATTERN_NAME);
    }

    @Test
    public void testMatchingDecorator() {
        final ArgoUMLParser xmiParser = ParserFactory.createArgoUMLParser();
        final URL sucXmiUrl = DecoratorMatchingTest.class.getResource(MATCHING_SYSTEM_XMI);
        final SystemUnderConsideration system = xmiParser.parse(sucXmiUrl);

        // Check for a general note regarding the pattern, available from the xml-file
        assertEqualNotes(designPattern, new String[]{});

        final PatternInspector patternInspector = new PatternInspector(system, designPattern);
        assertTrue(patternInspector.isomorphismExists());
        assertMatchingSolutions(patternInspector.getMatchingResult());
        assertMatchingFeedback(patternInspector.getMatchingResult(), designPattern);
    }

    @Test
    public void testMismatchingDecorator() {
        final ArgoUMLParser xmiParser = ParserFactory.createArgoUMLParser();
        final URL sucXmiUrl = DecoratorMatchingTest.class.getResource(MISMATCHING_SYSTEM_XMI);
        final SystemUnderConsideration system = xmiParser.parse(sucXmiUrl);

        final PatternInspector patternInspector = new PatternInspector(system, designPattern);
        assertFalse(patternInspector.isomorphismExists());

        final Set<Relation> relations = system.edgeSet();
        final Set<Node> nodes = system.vertexSet();
        final Feedback feedback = patternInspector.getMatchingResult().getFeedback();
        assertMinimumFailedMatches(feedback, nodes, relations, 2);
    }

    private void assertMatchingSolutions(PatternInspector.MatchingResult matchingResult) {
        final List<Solution> solutions = matchingResult.getSolutions();
        assertEquals(1, solutions.size());

        assertMatchingNodes(solutions, "MyPart", "Component");
        assertMatchingNodes(solutions, "MyDecorator", "Decorator");
        assertAnyMatchingNode(solutions,
                Arrays.asList(new String[]{"MyConcrDecA", "MyConcrDecB"}),
                Arrays.asList(new String[]{"ConcreteDecoratorA", "ConcreteDecoratorB"}));
        assertMatchingNodes(solutions, "MyConcretePart", "ConcreteComponent");
    }

}
