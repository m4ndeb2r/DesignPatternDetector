package nl.ou.dpd.gui.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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

    private static final String TEST_PROJ_FILE_NAME = "/testproject.dpd";
    private static final String SAVE_PROJ_FILE_NAME = "/saveproject.dpd";

    private static final String DPD_SYSTEM = "dpd.system";
    private static final String DPD_TEMPLATES = "dpd.tmplts";
    private static final String DPD_MAX_MISSING_EDGES = "dpd.maxedg";

    private File testProjectFile;
    private File saveProjectFile;


    /**
     * Initialise a project files for each test.
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
        assertThat(project.getName(), is("New project *"));
        assertThat(project.getSystemUnderConsiderationPath(), isEmptyOrNullString());
        assertThat(project.getDesignPatternTemplatePath(), isEmptyOrNullString());
        assertThat(project.getMaxMissingEdges(), is(0));

        // There have been no changes yet, so the project is still pristine
        assertTrue(project.isPristine());

        // A new project has no file yet; when a file is also not specified as an argument
        // then saving the project will fail
        assertFalse(project.save());
    }

    /**
     * Tests the {@link Project#Project(File)} constructor.
     */
    @Test
    public void testConstructor() throws FileNotFoundException {
        Project project = new Project(testProjectFile);
        assertThat(project.getName(), is("testproject.dpd"));
        assertThat(project.getDesignPatternTemplatePath(), is("/testtemplates.xml"));
        assertThat(project.getSystemUnderConsiderationPath(), is("/testinput.xmi"));
        assertThat(project.getMaxMissingEdges(), is(1));
        assertTrue(project.isPristine());
    }

    /**
     * Tests the {@link Project#isPristine()} method.
     */
    @Test
    public void testIsPristine() {
        Project project1 = new Project();
        assertTrue(project1.isPristine());
        project1.setSystemUnderConsiderationPath("newPath");
        assertFalse(project1.isPristine());

        Project project2 = new Project();
        assertTrue(project2.isPristine());
        project2.setDesignPatternTemplatePath("newPath");
        assertFalse(project2.isPristine());

        Project project3 = new Project();
        assertTrue(project3.isPristine());
        project3.setMaxMissingEdges(1);
        assertFalse(project3.isPristine());
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
        project.setMaxMissingEdges(1);

        // Save it
        project.save(saveProjectFile);

        // Check the project's attributes
        assertThat(project.getName(), is(SAVE_PROJ_FILE_NAME.substring(1)));
        assertThat(project.getSystemUnderConsiderationPath(), is("/sys.xmi"));
        assertThat(project.getDesignPatternTemplatePath(), is("/tpl.xml"));
        assertThat(project.getMaxMissingEdges(), is(1));
        assertTrue(project.isPristine());
        assertThat(project.getProjectFile(), is(saveProjectFile));

        // Check the project file's content
        Scanner sc = new Scanner(saveProjectFile);
        assertThat(sc.nextLine(), is(DPD_SYSTEM + ":/sys.xmi"));
        assertThat(sc.nextLine(), is(DPD_TEMPLATES + ":/tpl.xml"));
        assertThat(sc.nextLine(), is(DPD_MAX_MISSING_EDGES + ":1"));
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
        assertThat(project.getName(), is(SAVE_PROJ_FILE_NAME.substring(1)));
        assertThat(project.getSystemUnderConsiderationPath(), isEmptyOrNullString());
        assertThat(project.getDesignPatternTemplatePath(), isEmptyOrNullString());
        assertThat(project.getMaxMissingEdges(), is(0));
        assertTrue(project.isPristine());
        assertThat(project.getProjectFile(), is(saveProjectFile));

        // Check the project file's content
        Scanner sc = new Scanner(saveProjectFile);
        assertThat(sc.nextLine(), is(DPD_MAX_MISSING_EDGES + ":0"));
        assertFalse(sc.hasNext());
    }

    private String getPath(String resourceName) {
        return this.getClass().getResource(resourceName).getPath();
    }
}
