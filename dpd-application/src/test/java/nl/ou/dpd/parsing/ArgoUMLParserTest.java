package nl.ou.dpd.parsing;

import nl.ou.dpd.domain.SystemUnderConsideration;
import nl.ou.dpd.domain.node.Node;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.URL;
import java.util.Map;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests the {@link ArgoUMLParser} class.
 *
 * @author Martin de Boer
 */
@RunWith(MockitoJUnitRunner.class)
public class ArgoUMLParserTest {

    // This filename is just to satisfy the FileInputStream of the parser
    private static final String DUMMY_XMI = "/argoUML/dummy.xmi";

    @Mock
    private ArgoUMLNodeParser nodeParser;

    @Mock
    private ArgoUMLRelationParser relationParser;

    @Mock
    private SystemRelationsExtractor systemRelationsExtractor;

    @Mock
    Map<String, Node> nodes;

    @Mock
    SystemUnderConsideration system;

    @Test
    public void testParseFile() {
        final ArgoUMLParser parser = new ArgoUMLParser(nodeParser, relationParser, systemRelationsExtractor);
        final String xmiFilename = getPath(DUMMY_XMI);
        when(nodeParser.parse(eq(xmiFilename))).thenReturn(nodes);
        when(relationParser.parse(xmiFilename, nodes)).thenReturn(system);

        parser.parse(xmiFilename);

        verify(nodeParser, times(1)).parse(xmiFilename);
        verify(relationParser, times(1)).parse(xmiFilename, nodes);
        verify(systemRelationsExtractor, times(1)).execute(system);
    }

    @Test
    public void testParseURL() {
        final ArgoUMLParser parser = new ArgoUMLParser(nodeParser, relationParser, systemRelationsExtractor);
        final URL xmiURL = getURL(DUMMY_XMI);
        final String xmiFilename = xmiURL.getPath();
        when(nodeParser.parse(eq(xmiFilename))).thenReturn(nodes);
        when(relationParser.parse(xmiFilename, nodes)).thenReturn(system);

        parser.parse(xmiURL);

        verify(nodeParser, times(1)).parse(xmiFilename);
        verify(relationParser, times(1)).parse(xmiFilename, nodes);
        verify(systemRelationsExtractor, times(1)).execute(system);
    }

    private String getPath(String resourceName) {
        return this.getClass().getResource(resourceName).getPath();
    }

    private URL getURL(String resourceName) {
        return this.getClass().getResource(resourceName);
    }
}
