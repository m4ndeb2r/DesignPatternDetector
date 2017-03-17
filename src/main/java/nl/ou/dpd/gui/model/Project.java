package nl.ou.dpd.gui.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Represents a project a user can create, save and open in the GUI, to store his/her analyse settings.
 *
 * @author Martin de Boer
 */
public class Project {

    private static final Logger LOGGER = LogManager.getLogger(Project.class);


    private static final String DPD_PROJ_NAME = "dpd.prname";
    private static final String DPD_SYSTEM = "dpd.system";
    private static final String DPD_TEMPLATES = "dpd.tmplts";
    private static final String DPD_MAX_MISSING_EDGES = "dpd.maxedg";

    private String name;
    private String systemUnderConsiderationPath;
    private String designPatternTemplatePath;
    private int maxMissingEdges;
    private File projectFile;

    /**
     * Creates a new {@link Project} with default attributes.
     */
    public Project() {
        this("New project *", null, null, 0);
    }

    /**
     * Creates a new {@link Project} and reads the attributes from the specified project {@link File}.
     *
     * @param projectFile the project file containing the project attributes
     */
    public Project(File projectFile) {
        final Scanner sc = new Scanner(projectFile.getAbsolutePath());
        while (sc.hasNextLine()) {
            final String line = sc.nextLine();
            if (line.startsWith(DPD_SYSTEM)) {
                this.setSystemUnderConsiderationPath(line.substring(line.indexOf(":") + 1).trim());
            }
            if (line.startsWith(DPD_TEMPLATES)) {
                this.setDesignPatternTemplatePath(line.substring(line.indexOf(":") + 1).trim());
            }
            if (line.startsWith(DPD_MAX_MISSING_EDGES)) {
                this.setMaxMissingEdges(Integer.getInteger(line.substring(line.indexOf(":") + 1).trim()));
            }
        }
        this.name = projectFile.getName().replace(".dpd", "");
        this.projectFile = projectFile;
    }

    /**
     * Creates a {@link Project} based on the specified parameters.
     *
     * @param name                         the name of the project
     * @param systemUnderConsiderationPath the name of the input file containing the system under consideration. This is an export (*.xmi) form ArgoUML at this moment.
     * @param designPatternTemplatePath    the name of the template file containing the design pattern templates in XML-format
     * @param maxMissingEdges              the number of missing edges allowed when analysing the system under consideration
     */
    public Project(String name, String systemUnderConsiderationPath, String designPatternTemplatePath, int maxMissingEdges) {
        this.name = name;
        this.systemUnderConsiderationPath = systemUnderConsiderationPath;
        this.designPatternTemplatePath = designPatternTemplatePath;
        this.maxMissingEdges = maxMissingEdges;
    }

    /**
     * Save the project to disk.
     *
     * @return {@code true} if the project was successfully saved, or {@code false} otherwise.
     */
    public boolean save() {
        return save(this.projectFile);
    }

    /**
     * Save the project ot the specified file on disk.
     *
     * @param file the file to store the project to.
     * @return {@code true} if the project was successfully saved, or {@code false} otherwise.
     */
    public boolean save(File file) {
        if (file != null) {
            final String projectName = file.getName().replace(".dpd", "");
            BufferedWriter out = null;
            try {
                out = new BufferedWriter(new FileWriter(file));
                out.write(DPD_SYSTEM + ":" + this.systemUnderConsiderationPath);
                out.newLine();
                out.write(DPD_TEMPLATES + ":" + this.designPatternTemplatePath);
                out.newLine();
                out.write(DPD_MAX_MISSING_EDGES + ":" + this.maxMissingEdges);
                out.newLine();

                // Succesfully written to file. Change project name and file.
                this.setName(projectName);
                this.projectFile = file;
                return true;

            } catch (Exception ex) {
                LOGGER.error("Saving project failed.", ex);

                // TODO show dialog

            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                    }
                }
            }
        }
        return false;
    }

    /**
     * Gets the name of the project.
     *
     * @return the name of the project
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the project.
     *
     * @param name the new name of the project
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the file name of the XMI-input file (system under consideration).
     *
     * @return the complete filename
     */
    public String getSystemUnderConsiderationPath() {
        return systemUnderConsiderationPath;
    }

    /**
     * Sets the file name of the XMI-input file (system under consideration).
     *
     * @param systemUnderConsiderationPath the new complete filename
     */
    public void setSystemUnderConsiderationPath(String systemUnderConsiderationPath) {
        this.systemUnderConsiderationPath = systemUnderConsiderationPath;
    }

    /**
     * Gets the filename of the XML-imput file containing the design pattern templates.
     *
     * @return the complete filename
     */
    public String getDesignPatternTemplatePath() {
        return designPatternTemplatePath;
    }

    /**
     * Sets the filename of the XML-imput file containing the design pattern templates.
     *
     * @param designPatternTemplatePath the new complete filename
     */
    public void setDesignPatternTemplatePath(String designPatternTemplatePath) {
        this.designPatternTemplatePath = designPatternTemplatePath;
    }

    /**
     * Gets the max number of missing edges that are allowed when the input is analysed.
     *
     * @return the max allowed number of missing edges.
     */
    public int getMaxMissingEdges() {
        return maxMissingEdges;
    }

    /**
     * Sets the maximum allowed number of missing edges that are allowed when the input is analysed.
     *
     * @param maxMissingEdges the new maximum
     */
    public void setMaxMissingEdges(int maxMissingEdges) {
        this.maxMissingEdges = maxMissingEdges;
    }

    /**
     * Gets the file this project is stored in.
     *
     * @return the project file
     */
    public File getProjectFile() {
        return projectFile;
    }

    /**
     * Sets the file to store this project in.
     *
     * @param projectFile the project file
     */
    public void setProjectFile(File projectFile) {
        this.projectFile = projectFile;
    }
}
