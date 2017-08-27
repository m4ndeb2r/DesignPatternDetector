package nl.ou.dpd.domain.matching;

import nl.ou.dpd.domain.DesignPattern;
import nl.ou.dpd.domain.SystemUnderConsideration;
import nl.ou.dpd.domain.node.Node;
import nl.ou.dpd.domain.relation.Relation;
import nl.ou.dpd.parsing.ArgoUMLParser;
import nl.ou.dpd.parsing.ParserFactory;

import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * An abstract test class containing shared test functionality.
 *
 * @author Martin de Boer
 * @author Peter Vansweevelt
 */
public abstract class AbstractMatchingTest {

    protected static final String TEMPLATES_XML = "/patterns/designpatterns_templates.xml";

    /**
     * Temporary method to print feedback, generated by the pattern instpector for a certain system and pattern.
     * <p>
     * NOTE: using this method in your unit test or integration test is a 'code smell'. Visual checking of the
     * validity of your code does not belong in an automated test. This method is here just for temporary convenience
     * reasons.
     *
     * @param designPattern    the design pattern to print the feedback for
     * @param system           the system under consideration that was analysed byt the pattern inspector
     * @param patternInspector the pattern inspector that analysed the system in search for design patterns
     * @deprecated This method is here just for temporary convenience reasons.
     */
    @Deprecated
    protected void printFeedback(
            DesignPattern designPattern,
            SystemUnderConsideration system,
            PatternInspector patternInspector) {

        final PatternInspector.MatchingResult matchingResult = patternInspector.getMatchingResult();

        final List<Solution> solutions = matchingResult.getSolutions();
        solutions.forEach(solution -> {
            System.out.println("---------------------------------------");
            System.out.println(solution.getDesignPatternName());
            System.out.println("---------------------------------------");
            solution.getMatchingNodeNames().forEach(names -> System.out.println(names[0] + " matches " + names[1]));
        });

        System.out.println("\n---------------------------------------");
        System.out.println(designPattern.getName() + " feedback");
        System.out.println("---------------------------------------");
        final Feedback feedback = matchingResult.getFeedback();

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

    /**
     * Asserts that the system design in the specified {@code matchingSystemXmi} contains the specified {@code pattern}.
     * It also checks if the specified {@code notes} are present in the {@link DesignPattern} and in the
     * {@link Feedback}.
     *
     * @param matchingSystemXmi an XMI file NOT containing the specified {@code pattern}
     * @param pattern           the p{@link DesignPattern} that must be present in the XMI file
     * @param notes             the expected notes in the pattern, parsed form the XMI file
     */
    protected void assertMatchingPattern(String matchingSystemXmi, DesignPattern pattern, String... notes) {
        final ArgoUMLParser xmiParser = ParserFactory.createArgoUMLParser();
        final URL sucXmiUrl = ObserverMatchingTest.class.getResource(matchingSystemXmi);
        final SystemUnderConsideration system = xmiParser.parse(sucXmiUrl);

        // Check for a general note regarding the pattern, available from the xml-file
        assertEqualNotes(pattern, notes);

        final PatternInspector patternInspector = new PatternInspector(system, pattern);
        assertTrue(patternInspector.isomorphismExists());
        assertMatchingSolutions(patternInspector.getMatchingResult());
        assertMatchingFeedback(patternInspector.getMatchingResult(), pattern);

        final Feedback feedback = patternInspector.getMatchingResult().getFeedback();
        assertTotalOfFeedbackNodes(feedback, system);
        assertTotalOfFeedbackRelations(feedback, system);
    }

    /**
     * Asserts that the generated {@link Solution}s match the expectations.
     *
     * @param matchingResult the object containing the solutions.
     */
    protected abstract void assertMatchingSolutions(PatternInspector.MatchingResult matchingResult);

    /**
     * Asserts that the system design in the specified {@code mismatchingSystemXmi} does not contain the specified
     * {@code pattern}.
     *
     * @param mismatchingSystemXmi an XMI file NOT containing the specified {@code pattern}
     * @param pattern              a {@link DesignPattern} that is NOT present in the specified
     *                             {@code mismatchingSystemXmi} file
     */
    protected void assertMismatchingPattern(String mismatchingSystemXmi, DesignPattern pattern) {
        final ArgoUMLParser xmiParser = ParserFactory.createArgoUMLParser();
        final URL sucXmiUrl = BridgeMatchingTest.class.getResource(mismatchingSystemXmi);
        final SystemUnderConsideration system = xmiParser.parse(sucXmiUrl);

        final PatternInspector patternInspector = new PatternInspector(system, pattern);
        assertFalse(patternInspector.isomorphismExists());

        final Set<Relation> relations = system.edgeSet();
        final Set<Node> nodes = system.vertexSet();
        final Feedback feedback = patternInspector.getMatchingResult().getFeedback();
        assertMinimumFailedMatches(feedback, nodes, relations, 2);
        assertTotalOfFeedbackNodes(feedback, system);
        assertTotalOfFeedbackRelations(feedback, system);
    }

    /**
     * Finds out if the specified system node and and the specified pattern node match at least once in the specified
     * solutions.
     *
     * @param solutions       the solutions to consider
     * @param systemNodeName  the name of the system node to find
     * @param patternNodeName the name of the pattern node to find
     * @return {@code true} if this match exists.
     */
    protected boolean areMatchingNodes(List<Solution> solutions, String systemNodeName, String patternNodeName) {
        for (Solution solution : solutions) {
            for (String[] names : solution.getMatchingNodeNames()) {
                if (names[0].equals(systemNodeName) && names[1].equals(patternNodeName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Get a design pattern from a list, based on the pattern's name.
     *
     * @param patterns    the list of patterns to iterate
     * @param patternName the name of the pattern to pick from ths list
     * @return the pattern with the specified name, if present in the list, or {@code null} otherwise.
     */
    protected DesignPattern getDesignPatternByName(List<DesignPattern> patterns, String patternName) {
        return patterns.stream().filter(dp -> patternName.equals(dp.getName())).findFirst().orElse(null);
    }

    /**
     * Checks if a specified minimum number of mismatch feedback messages is present in the specified feedback.
     *
     * @param feedback  the feedback object to inspect
     * @param nodes     the nodes to search for in the feedback object
     * @param relations the relations to search for in the feedback object
     * @param minimum   the minimum number of expected mismatch messages
     */
    protected void assertMinimumFailedMatches(Feedback feedback, Set<Node> nodes, Set<Relation> relations, int minimum) {
        int mismatches = 0;
        for (Node node : nodes) {
            mismatches += feedback.getFeedbackMessages(node, FeedbackType.MISMATCH).size();
        }
        for (Relation relation : relations) {
            mismatches += feedback.getFeedbackMessages(relation, FeedbackType.MISMATCH).size();
        }
        assertTrue(minimum <= mismatches);
    }

    /**
     * Asserts that there is feedback info for all the system nodes.
     *
     * @param feedback the feedback object
     * @param system   the system under consideration
     */
    protected void assertTotalOfFeedbackNodes(Feedback feedback, SystemUnderConsideration system) {
        final int feedbackNodes = feedback.getNodeSet().size();
        final int systemNodes = system.vertexSet().size();
        assertThat(feedbackNodes, is(systemNodes));
    }

    /**
     * Asserts that there is feedback info for all the system relations.
     *
     * @param feedback the feedback object
     * @param system   the system under consideration
     */
    protected void assertTotalOfFeedbackRelations(Feedback feedback, SystemUnderConsideration system) {
        final int feedbackRelations = feedback.getRelationSet().size();
        final int systemRelations = system.edgeSet().size();
        assertThat(feedbackRelations, is(systemRelations));
    }

    /**
     * Checks is the specified notes are present in the design pattern.
     *
     * @param pattern the design pattern to inspect
     * @param notes   the notes expected to be present in the design pattern
     */
    protected void assertEqualNotes(DesignPattern pattern, String... notes) {
        assertThat(pattern.getNotes().size(), is(notes.length));
        for (int i = 0; i < notes.length; i++) {
            assertTrue(pattern.getNotes().contains(notes[i]));
        }
    }

    /**
     * Asserts that two nodenames ({@code sysNodeName} and {@code patternNodeName}) are present in the list of
     * {@link Solution}s as a matching couple.
     *
     * @param solutions       the {@link Solution}s to inspect
     * @param sysNodeName     the system node name to find and check
     * @param patternNodeName the pattern node name to find and check
     */
    protected void assertMatchingNodes(List<Solution> solutions, String sysNodeName, String patternNodeName) {
        assertTrue(areMatchingNodes(solutions, sysNodeName, patternNodeName));
    }

    /**
     * Asserts that any of the system nodes (specified by {@code sysNodeNames}) matches any of the design pattern nodes
     * (specified by {@code patternNodeNames}) in the list of {@link Solution}s.
     *
     * @param solutions        the list of {@link Solution}s to inspect
     * @param sysNodeNames     the list of names of system nodes
     * @param patternNodeNames the list of names of design pattern nodes
     */
    protected void assertAnyMatchingNode(List<Solution> solutions, List<String> sysNodeNames, List<String> patternNodeNames) {
        boolean match = false;
        for (String sysNodeName : sysNodeNames) {
            for (String patternNodeName : patternNodeNames) {
                match |= areMatchingNodes(solutions, sysNodeName, patternNodeName);
            }
        }
        assertTrue(match);
    }

    /**
     * Asserts that (1) the general notes in the {@link Feedback} object match with the notes specified in the
     * {@link DesignPattern}, and that (2) for every system {@link Node}, and every system {@link Relation}, there is
     * a positive match message in the {@link Feedback} object in the {@code matchingResult}.
     *
     * @param matchingResult contains a feedback object
     * @param designPattern  the {@link DesignPattern}, containing the expected general notes
     */
    protected void assertMatchingFeedback(PatternInspector.MatchingResult matchingResult, DesignPattern designPattern) {
        final Feedback feedback = matchingResult.getFeedback();
        assertThat(feedback.getNotes(), is(designPattern.getNotes()));
        assertMatchingNodesFeedback(matchingResult);
    }

    /**
     * Asserts that for every matching {@link Node} in de {@link Solution}s, there is a corresponding {@link Feedback}
     * message that confirms this match.
     *
     * @param matchingResult contains the list of {@link Solution}s as well as the {@link Feedback}.
     */
    private void assertMatchingNodesFeedback(PatternInspector.MatchingResult matchingResult) {
        final Feedback feedback = matchingResult.getFeedback();
        final List<Node> matchingSystemNodes = getMatchingSystemNodes(matchingResult);
        matchingSystemNodes.forEach(node -> assertTrue(feedback.getFeedbackMessages(node, FeedbackType.MATCH).size() > 0));
    }

    private List<Node> getMatchingSystemNodes(PatternInspector.MatchingResult matchingResult) {
        return matchingResult.getSolutions()
                .stream()
                .flatMap(solution -> solution.getMatchingNodes().stream())
                .map(nodes -> nodes[0])
                .distinct()
                .collect(Collectors.toList());
    }

}
