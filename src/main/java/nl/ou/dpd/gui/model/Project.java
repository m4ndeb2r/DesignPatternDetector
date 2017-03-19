package nl.ou.dpd.gui.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
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
    private boolean pristine;

    /**
     * Creates a new {@link Project} with default attributes. Use this method when a new project is created from
     * scratch (not loaded from a file project). The newly created project is not yet linked to a file.
     */
    public Project() {
        this.name = "[New project]";
        this.pristine = true;
        this.projectFile = null;
        this.maxMissingEdges = 0;
        this.designPatternTemplatePath = null;
        this.systemUnderConsiderationPath = null;
    }

    /**
     * Creates a new {@link Project} and reads the attributes from the specified project {@link File}.
     *
     * @param projectFile the project file containing the project attributes
     * @throws FileNotFoundException when the project file doe not exist
     */
    public Project(File projectFile) throws FileNotFoundException {
        Scanner sc = new Scanner(projectFile);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (line.startsWith(DPD_SYSTEM)) {
                this.setSystemUnderConsiderationPath(line.substring(line.indexOf(":") + 1).trim());
            }
            if (line.startsWith(DPD_TEMPLATES)) {
                this.setDesignPatternTemplatePath(line.substring(line.indexOf(":") + 1).trim());
            }
            if (line.startsWith(DPD_MAX_MISSING_EDGES)) {
                this.setMaxMissingEdges(Integer.parseInt(line.substring(line.indexOf(":") + 1).trim()));
            }
        }
        this.name = projectFile.getName();
        this.pristine = true;
        this.projectFile = projectFile;
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
        if (file == null) {
            return false;
        }

        boolean success = false;
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(file));
            if (this.systemUnderConsiderationPath != null) {
                out.write(DPD_SYSTEM + ":" + this.systemUnderConsiderationPath);
                out.newLine();
            }
            if (this.designPatternTemplatePath != null) {
                out.write(DPD_TEMPLATES + ":" + this.designPatternTemplatePath);
                out.newLine();
            }
            out.write(DPD_MAX_MISSING_EDGES + ":" + this.maxMissingEdges);
            out.newLine();

            // Succesfully written to file. Change project name and file.
            this.setName(file.getName());
            this.projectFile = file;
            this.pristine = true;
            success = true;

        } catch (Exception ex) {
            LOGGER.error("Saving project failed.", ex);
            throw new RuntimeException(ex);

        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
        return success;
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
     * Sets the file name of the XMI-input file (system under consideration). When the new value  differs form the
     * original value, the pristine attribute of the {@link Project} is set to {@code false}.
     *
     * @param systemUnderConsiderationPath the new complete filename
     */
    public void setSystemUnderConsiderationPath(String systemUnderConsiderationPath) {
        if (different(this.systemUnderConsiderationPath, systemUnderConsiderationPath)) {
            this.systemUnderConsiderationPath = systemUnderConsiderationPath;
            this.pristine = false;
        }
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
     * Sets the filename of the XML-imput file containing the design pattern templates. When the new value
     * differs form the original value, the pristine attribute of the {@link Project} is set to {@code false}.
     *
     * @param designPatternTemplatePath the new complete filename
     */
    public void setDesignPatternTemplatePath(String designPatternTemplatePath) {
        if (different(this.designPatternTemplatePath, designPatternTemplatePath)) {
            this.designPatternTemplatePath = designPatternTemplatePath;
            this.pristine = false;
        }
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
     * Sets the maximum allowed number of missing edges that are allowed when the input is analysed. When the new value
     * differs form the original value, the pristine attribute of the {@link Project} is set to {@code false}.
     *
     * @param maxMissingEdges the new maximum
     */
    public void setMaxMissingEdges(int maxMissingEdges) {
        if (this.maxMissingEdges != maxMissingEdges) {
            this.maxMissingEdges = maxMissingEdges;
            this.pristine = false;
        }
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
     * Returns whether this {@link Project} is pristine: no changes were made since its creation.
     *
     * @return {@code true} if no changes were made to this project, or {@code false} of any changes were made.
     */
    public boolean isPristine() {
        return pristine;
    }

    /**
     * Determins is two string values are different.
     *
     * @param a first string
     * @param b second string
     * @return {@code true} if a is different from b
     */
    private boolean different(String a, String b) {
        if (a == null && b != null) {
            return (b != null);
        }
        return !a.equals(b);
    }

}
