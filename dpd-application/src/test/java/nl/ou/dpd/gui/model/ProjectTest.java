package nl.ou.dpd.gui.model;

import nl.ou.dpd.exception.DesignPatternDetectorException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests the {@link Project} class.
 *
 * @author Martin de Boer
 */
public class ProjectTest {

    private static final String TEST_PROJ_FILE_NAME = "/projects/testproject.dpd";
    private static final String SAVE_PROJ_FILE_NAME = "/projects/saveproject.dpd";

    private static final String DPD_SYSTEM_FILE = "dpd.system.file";
    private static final String DPD_PATTERNS_FILE = "dpd.patterns.file";

    /**
     * Exception rule.
     */
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    private File testProjectFile;
    private File saveProjectFile;

    /**
     * Initialise a project files for each test.
     *
     * @throws IOException when a file error occurs.
     */
    @Before
    public void initProjectFiles() throws IOException {
        testProjectFile = new File(getPath(TEST_PROJ_FILE_NAME));
        saveProjectFile = new File(getPath(SAVE_PROJ_FILE_NAME));
    }

    /**
     * Tests the default constructor of the {@link Project} class.
     */
    @Test
    public void testDefaultConstructor() {
        Project project = new Project();
        assertThat(project.getName(), is("[New project]"));
        assertThat(project.getSystemUnderConsiderationFilePath(), isEmptyOrNullString());
        assertThat(project.getDesignPatternFilePath(), isEmptyOrNullString());

        // A new project is not pristine
        assertFalse(project.isPristine());

        // A new project has no file yet; when a file is also not specified as an argument
        // then saving the project will fail
        thrown.expect(DesignPatternDetectorException.class);
        thrown.expectMessage("Saving project failed.");
        thrown.expectCause(is(NullPointerException.class));
        project.save();
    }

    /**
     * Tests the {@link Project#Project(File)} constructor.
     *
     * @throws FileNotFoundException when a file error occurs.
     */
    @Test
    public void testConstructor() throws FileNotFoundException {
        Project project = new Project(testProjectFile);
        assertThat(project.getName(), is(TEST_PROJ_FILE_NAME.substring(TEST_PROJ_FILE_NAME.lastIndexOf('/') + 1)));
        assertThat(project.getDesignPatternFilePath(), is("/testpatterns.xml"));
        assertThat(project.getSystemUnderConsiderationFilePath(), is("/testinput.xmi"));
        assertTrue(project.isPristine());
    }

    /**
     * Tests the {@link Project#isPristine()} method.
     *
     * @throws FileNotFoundException when a file error occurs.
     */
    @Test
    public void testIsPristine() throws FileNotFoundException {
        final Project project1 = new Project(testProjectFile);
        assertTrue(project1.isPristine());
        project1.setSystemUnderConsiderationPath("newPath");
        assertFalse(project1.isPristine());

        final Project project2 = new Project(testProjectFile);
        assertTrue(project2.isPristine());
        project2.setDesignPatternTemplatePath("newPath");
        assertFalse(project2.isPristine());
    }

    /**
     * Tests the {@link Project#save(File)} method.
     *
     * @throws IOException is not expected to occur
     */
    @Test
    public void testSave() throws IOException {
        if (saveProjectFile.exists()) {
            saveProjectFile.delete();
        }
        saveProjectFile.createNewFile();

        Project project = new Project();

        // Set some attributes to save the file
        project.setSystemUnderConsiderationPath("/sys.xmi");
        project.setDesignPatternTemplatePath("/tpl.xml");

        // Save it
        project.save(saveProjectFile);

        // Check the project's attributes
        assertThat(project.getName(), is(SAVE_PROJ_FILE_NAME.substring(SAVE_PROJ_FILE_NAME.lastIndexOf('/') + 1)));
        assertThat(project.getSystemUnderConsiderationFilePath(), is("/sys.xmi"));
        assertThat(project.getDesignPatternFilePath(), is("/tpl.xml"));
        assertTrue(project.isPristine());
        assertThat(project.getProjectFile(), is(saveProjectFile));

        // Check the project file's content
        Scanner sc = new Scanner(saveProjectFile);
        assertThat(sc.nextLine(), is(DPD_SYSTEM_FILE + ":/sys.xmi"));
        assertThat(sc.nextLine(), is(DPD_PATTERNS_FILE + ":/tpl.xml"));
        assertFalse(sc.hasNext());
    }

    /**
     * Tests the {@link Project#save(File)} method when applied to a newly created project, that is still pristine.
     *
     * @throws IOException is not expected to occur
     */
    @Test
    public void testSaveNewPristineProject() throws IOException {
        if (saveProjectFile.exists()) {
            saveProjectFile.delete();
        }
        saveProjectFile.createNewFile();

        // Create a project and save it right away.
        Project project = new Project();
        project.save(saveProjectFile);

        // Check the project's attributes
        assertThat(project.getName(), is(SAVE_PROJ_FILE_NAME.substring(SAVE_PROJ_FILE_NAME.lastIndexOf('/') + 1)));
        assertThat(project.getSystemUnderConsiderationFilePath(), isEmptyOrNullString());
        assertThat(project.getDesignPatternFilePath(), isEmptyOrNullString());
        assertTrue(project.isPristine());
        assertThat(project.getProjectFile(), is(saveProjectFile));

        // Check the project file's content
        Scanner sc = new Scanner(saveProjectFile);
        assertFalse(sc.hasNext());
    }

    private String getPath(String resourceName) {
        return this.getClass().getResource(resourceName).getPath();
    }
}
