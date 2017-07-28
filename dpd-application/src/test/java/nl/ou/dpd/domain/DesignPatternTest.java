package nl.ou.dpd.domain;

import nl.ou.dpd.domain.matching.FeedbackEnabledComparator;
import nl.ou.dpd.domain.node.Node;
import nl.ou.dpd.domain.relation.Relation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test the {@link DesignPattern} class.
 *
 * @author Martin de Boer
 */
@RunWith(MockitoJUnitRunner.class)
public class DesignPatternTest {

    private DesignPattern designPattern;

    @Mock
    private FeedbackEnabledComparator<Node> nodeComparator;
    @Mock
    private FeedbackEnabledComparator<Relation> relationComparator;

    @Before
    public void initDesignPattern() {
        designPattern = new DesignPattern("myPattern", "myPatternFamily");
    }

    @Test
    public void testConstructor() {
        assertThat(designPattern.getName(), is("myPattern"));
        assertThat(designPattern.getFamily(), is("myPatternFamily"));
        assertThat(designPattern.getNotes().size(), is(0));

        assertNull(designPattern.getNodeComparator());
        assertNull(designPattern.getRelationComparator());
    }

    @Test
    public void testAddNotes() {
        designPattern.addNote("note1");
        assertThat(designPattern.getNotes().size(), is(1));

        designPattern.addNote("note2");
        assertThat(designPattern.getNotes().size(), is(2));

        assertTrue(designPattern.getNotes().contains("note1"));
        assertTrue(designPattern.getNotes().contains("note2"));
    }

    @Test
    public void testSetComparators() {
        designPattern.setNodeComparator(nodeComparator);
        designPattern.setRelationComparator(relationComparator);

        assertThat(designPattern.getNodeComparator(), is(nodeComparator));
        assertThat(designPattern.getRelationComparator(), is(relationComparator));
    }

}
