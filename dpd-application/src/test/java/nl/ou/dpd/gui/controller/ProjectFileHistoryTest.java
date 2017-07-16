package nl.ou.dpd.gui.controller;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;


/**
 * Tests the {@link ProjectFileHistory} enum.
 *
 * @author Martin de Boer
 */
public class ProjectFileHistoryTest {

    private static final String TEST_PROJ_FILE_NAME1 = "/projects/testprojecthistory1.dpd";
    private static final String TEST_PROJ_FILE_NAME2 = "/projects/testprojecthistory2.dpd";
    private static final String TEST_PROJ_FILE_NAME3 = "/projects/testprojecthistory3.dpd";
    private static final String TEST_PROJ_FILE_NAME4 = "/projects/testprojecthistory4.dpd";
    private static final String TEST_PROJ_FILE_NAME5 = "/projects/testprojecthistory5.dpd";
    private static final String TEST_PROJ_FILE_NAME6 = "/projects/testprojecthistory6.dpd";
    private static final String NONEXISTANT_FILE = "/projects/nonexistant.dpd";

    private File testProjectHistoryFile1;
    private File testProjectHistoryFile2;
    private File testProjectHistoryFile3;
    private File testProjectHistoryFile4;
    private File testProjectHistoryFile5;
    private File testProjectHistoryFile6;
    private File nonexistantFile;

    /**
     * Initialise a project files for each test.
     *
     * @throws IOException when a file error occurs
     */
    @Before
    public void initProjectFiles() throws IOException {
        ProjectFileHistory.INSTANCE.getProjectFiles().removeAll(ProjectFileHistory.INSTANCE.getProjectFiles());
        testProjectHistoryFile1 = new File(getPath(TEST_PROJ_FILE_NAME1));
        testProjectHistoryFile2 = new File(getPath(TEST_PROJ_FILE_NAME2));
        testProjectHistoryFile3 = new File(getPath(TEST_PROJ_FILE_NAME3));
        testProjectHistoryFile4 = new File(getPath(TEST_PROJ_FILE_NAME4));
        testProjectHistoryFile5 = new File(getPath(TEST_PROJ_FILE_NAME5));
        testProjectHistoryFile6 = new File(getPath(TEST_PROJ_FILE_NAME6));
        nonexistantFile = new File(NONEXISTANT_FILE);
    }

    /**
     * Tests the {@link ProjectFileHistory#addProjectFile(File)} method.
     *
     * @throws FileNotFoundException not expected to happen
     */
    @Test
    public void testAddProjectFile() throws FileNotFoundException {
        ProjectFileHistory.INSTANCE.addProjectFile(testProjectHistoryFile1);
        assertThat(ProjectFileHistory.INSTANCE.getProjectFiles().size(), is(1));
        assertThat(ProjectFileHistory.INSTANCE.getProjectFiles().get(0), is(testProjectHistoryFile1));

        ProjectFileHistory.INSTANCE.addProjectFile(nonexistantFile);
        assertThat(ProjectFileHistory.INSTANCE.getProjectFiles().size(), is(1));
        assertThat(ProjectFileHistory.INSTANCE.getProjectFiles().get(0), is(testProjectHistoryFile1));

        ProjectFileHistory.INSTANCE.addProjectFile(testProjectHistoryFile2);
        assertThat(ProjectFileHistory.INSTANCE.getProjectFiles().size(), is(2));
        assertThat(ProjectFileHistory.INSTANCE.getProjectFiles().get(0), is(testProjectHistoryFile2));
        assertThat(ProjectFileHistory.INSTANCE.getProjectFiles().get(1), is(testProjectHistoryFile1));

        ProjectFileHistory.INSTANCE.addProjectFile(testProjectHistoryFile3);
        ProjectFileHistory.INSTANCE.addProjectFile(testProjectHistoryFile4);
        ProjectFileHistory.INSTANCE.addProjectFile(testProjectHistoryFile5);
        ProjectFileHistory.INSTANCE.addProjectFile(testProjectHistoryFile6);
        assertThat(ProjectFileHistory.INSTANCE.getProjectFiles().size(), is(5)); // Max = 5
        assertThat(ProjectFileHistory.INSTANCE.getProjectFiles().get(0), is(testProjectHistoryFile6));
        assertThat(ProjectFileHistory.INSTANCE.getProjectFiles().get(1), is(testProjectHistoryFile5));
        assertThat(ProjectFileHistory.INSTANCE.getProjectFiles().get(2), is(testProjectHistoryFile4));
        assertThat(ProjectFileHistory.INSTANCE.getProjectFiles().get(3), is(testProjectHistoryFile3));
        assertThat(ProjectFileHistory.INSTANCE.getProjectFiles().get(4), is(testProjectHistoryFile2));
        // testProjectHistoryFile1 will be removed (not recent enough, max = 5)

        ProjectFileHistory.INSTANCE.addProjectFile(testProjectHistoryFile3);
        ProjectFileHistory.INSTANCE.addProjectFile(nonexistantFile);
        assertThat(ProjectFileHistory.INSTANCE.getProjectFiles().size(), is(5)); // Unchanged
        assertThat(ProjectFileHistory.INSTANCE.getProjectFiles().get(0), is(testProjectHistoryFile3));
        assertThat(ProjectFileHistory.INSTANCE.getProjectFiles().get(1), is(testProjectHistoryFile6));
        assertThat(ProjectFileHistory.INSTANCE.getProjectFiles().get(2), is(testProjectHistoryFile5));
        assertThat(ProjectFileHistory.INSTANCE.getProjectFiles().get(3), is(testProjectHistoryFile4));
        assertThat(ProjectFileHistory.INSTANCE.getProjectFiles().get(4), is(testProjectHistoryFile2));
    }

    private String getPath(String resourceName) {
        return this.getClass().getResource(resourceName).getPath();
    }

}
