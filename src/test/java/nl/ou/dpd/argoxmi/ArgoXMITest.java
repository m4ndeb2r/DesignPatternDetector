package nl.ou.dpd.argoxmi;

import nl.ou.dpd.fourtuples.FourTupleArray;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.net.URL;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;

/**
 * Tests the {@link ArgoXMI} class.
 * TODO: The ArgoXMI class is hardly testable at the moment. It needs to be refactored to be testable.
 *
 * @author Martin de Boer
 */
public class ArgoXMITest {

    // A test file containing the Ba Brahem "system under consideration" example.
    private static final String BA_BRAHEM_TEST_XMI = "/Ba_Brahem.xmi";

    @Test
    public void testSomething() {
        final FourTupleArray fta = new FourTupleArray("Ba_Brahem");
        final ArgoXMI argoXMI = createArgoXML(BA_BRAHEM_TEST_XMI);

        argoXMI.parse();
        argoXMI.fourtuplesAbstractElements(fta);
        argoXMI.fourtuplesAssociationElements(fta);
        argoXMI.fourtuplesInheritanceElements(fta);

        // TODO: unfortunately we only have a show method... :(
        argoXMI.getFourtuples().show();
    }

    private ArgoXMI createArgoXML(String resourceName) {
        final URL url = this.getClass().getResource(resourceName);
        return new ArgoXMI(url.getPath());
    }
}
