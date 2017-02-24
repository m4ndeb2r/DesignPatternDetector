package nl.ou.dpd;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * An overall test of the {@link DesignPatternDetector}, comparing the output of the program (old style, as printed to
 * the console, with the expected output (as was printed to the console by the original program by Ed van Doorn).
 *
 * @author Martin de Boer
 */
public class DesignPatternDetectorTest {

    private static final String INPUT_XMI = "/input.xmi";
    private static final String TEMPLATES_XML = "/templates.xml";

    private static final String LEGACY_VERSION_COMPLEX_OUTPUT =
            "Design Pattern: Adapter\n" +
            "                   B -->                    Target\n" +
            "                   C -->                    Client\n" +
            "                   D -->                   Adapter\n" +
            "------------------------\n" +
            "\n" +
            "Design Pattern: Adapter\n" +
            "                   A -->                    Client\n" +
            "                   B -->                    Target\n" +
            "                   D -->                   Adapter\n" +
            "------------------------\n" +
            "\n" +
            "Design Pattern: Adapter\n" +
            "                   A -->                    Client\n" +
            "                   C -->                    Target\n" +
            "                   E -->                   Adapter\n" +
            "------------------------\n" +
            "\n" +
            "Design Pattern: Bridge\n" +
            "                   A -->                    Client\n" +
            "                   B -->               Abstraction\n" +
            "                   C -->               Implementor\n" +
            "                   D -->        RefinedAbstraction\n" +
            "                   E -->       ConcreteImplementor\n" +
            "------------------------\n" +
            "Edges which do not belong to this design pattern:\n" +
            "D --> E\n" +
            "C --> B\n" +
            "A --> C\n" +
            "==================================================\n" +
            "\n" +
            "Design Pattern: Builder\n" +
            "                   B -->                   Builder\n" +
            "                   D -->           ConcreteBuilder\n" +
            "                   E -->                   Product\n" +
            "------------------------\n" +
            "\n" +
            "Design Pattern: ChainOfResponsibility\n" +
            "                   A -->                    Client\n" +
            "                   C -->                   Handler\n" +
            "                   E -->           ConcreteHandler\n" +
            "------------------------\n" +
            "\n" +
            "Design Pattern: ChainOfResponsibility\n" +
            "                   B -->                   Handler\n" +
            "                   C -->                    Client\n" +
            "                   D -->           ConcreteHandler\n" +
            "------------------------\n" +
            "\n" +
            "Design Pattern: ChainOfResponsibility\n" +
            "                   A -->                    Client\n" +
            "                   B -->                   Handler\n" +
            "                   D -->           ConcreteHandler\n" +
            "------------------------\n" +
            "\n" +
            "Design Pattern: Factory Method\n" +
            "                   B -->                   Creator\n" +
            "                   C -->           ConcreteProduct\n" +
            "                   D -->           ConcreteCreator\n" +
            "                   E -->                   Product\n" +
            "------------------------\n" +
            "Edges which do not belong to this design pattern:\n" +
            "C --> B\n" +
            "==================================================\n" +
            "\n" +
            "Design Pattern: Iterator\n" +
            "                   A -->                    Client\n" +
            "                   B -->                 Aggregate\n" +
            "                   C -->                  Iterator\n" +
            "                   D -->         ConcreteAggregate\n" +
            "                   E -->          ConcreteIterator\n" +
            "------------------------\n" +
            "Edges which do not belong to this design pattern:\n" +
            "C --> B\n" +
            "==================================================\n" +
            "\n" +
            "Design Pattern: Memento\n" +
            "                   D -->                Originator\n" +
            "                   E -->                   Memento\n" +
            "------------------------\n" +
            "\n" +
            "Design Pattern: State - Strategy\n" +
            "                   C -->                  Strategy\n" +
            "                   E -->          ConcreteStrategy\n" +
            "------------------------\n" +
            "\n" +
            "Design Pattern: State - Strategy\n" +
            "                   B -->                  Strategy\n" +
            "                   D -->          ConcreteStrategy\n" +
            "------------------------\n" +
            "\n";

    private static final String BA_BRAHEM_INPUT_XMI = "/Ba_Brahem.xmi";
    private static final String BA_BRAHEM_TEMPLATES_XML = "/Ba_Brahem.xml";

    private static final String LEGACY_VERSION_BA_BRAHEM_OUTPUT =
            "Design Pattern: Ba_Brahem\n" +
            "                   B -->                         Q\n" +
            "                   C -->                         P\n" +
            "                   D -->                         R\n" +
            "------------------------\n" +
            "\n" +
            "Design Pattern: Ba_Brahem\n" +
            "                   A -->                         P\n" +
            "                   B -->                         Q\n" +
            "                   D -->                         R\n" +
            "------------------------\n" +
            "\n" +
            "Design Pattern: Ba_Brahem\n" +
            "                   A -->                         P\n" +
            "                   C -->                         Q\n" +
            "                   E -->                         R\n" +
            "------------------------\n" +
            "\n";

    /**
     * A rule to capture the output written to System.out.
     */
    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    /**
     * Clean the captured output after every test.
     */
    @After
    public void clearLog() {
        systemOutRule.clearLog();
    }

    /**
     * Run the application and compare the output to the output of the legacy system, when the same input was provided.
     * (This legacy output was harvested by running the original version of the application in a terminal window.) This
     * test uses the default input.
     */
    @Test
    public void testApplicationRunWithDefaults() {
        final String inputFile = createPathFromResource(INPUT_XMI);
        final String templatesFile = createPathFromResource(TEMPLATES_XML);
        final String[] args = {"-x", inputFile, "-t", templatesFile, "-n", "1"};
        runApplicationRun(args, LEGACY_VERSION_COMPLEX_OUTPUT);
    }

    /**
     * Run the application and compare the output to the output of the legacy system, when the same input was provided.
     * (This legacy output was harvested by running the original version of the application in a terminal window.) This
     * test uses the input used in a Ba_Brahem test mentioned in the academic articles by Ed van Doorn and Ba Brahm et
     * al.
     */
    @Test
    public void testApplicationRunWithBaBrahemFiles() {
        final String inputFile = createPathFromResource(BA_BRAHEM_INPUT_XMI);
        final String templatesFile = createPathFromResource(BA_BRAHEM_TEMPLATES_XML);
        final String[] args = {"-x", inputFile, "-t", templatesFile, "-n", "0"};
        runApplicationRun(args, LEGACY_VERSION_BA_BRAHEM_OUTPUT);
    }

    private void runApplicationRun(String[] args, String expectedOutput) {
        // Run the application (simulate a command from the command line), by calling the main method.
        DesignPatternDetector.main(args);

        // Check the output: this should be equal to the output of the legacy version of the application (until we
        // actually change the functional workings of the application).
        // We have to strip the first sentence of the output (being the current directory printed to the output), and
        // compare the strippedOutput to the legacy (stripped) output.
        final String actualOutput = systemOutRule.getLog();
        final String strippedOutput = actualOutput.substring(actualOutput.indexOf("Design Pattern:"));
        assertThat(expectedOutput, is(strippedOutput));
    }

    private String createPathFromResource(String resourceName) {
        return DesignPatternDetectorTest.class.getResource(resourceName).getPath();
    }


}
