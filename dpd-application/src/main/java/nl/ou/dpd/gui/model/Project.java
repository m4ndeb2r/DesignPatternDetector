package nl.ou.dpd.gui.model;

import nl.ou.dpd.exception.DesignPatternDetectorException;
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

    private static final String DPD_SYSTEM_FILE = "dpd.system.file";
    private static final String DPD_PATTERNS_FILE = "dpd.patterns.file";

    private String name;
    private String systemUnderConsiderationPath;
    private String designPatternFilePath;
    private File projectFile;
    private boolean pristine;

    /**
     * Creates a new {@link Project} with default attributes. Use this method when a new project is created from
     * scratch (not loaded from a file project). The newly created project is not yet linked to a file.
     */
    public Project() {
        this.name = "[New project]";
        this.pristine = false;
        this.projectFile = null;
        this.designPatternFilePath = null;
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
            if (line.startsWith(DPD_SYSTEM_FILE)) {
                this.setSystemUnderConsiderationPath(line.substring(line.indexOf(":") + 1).trim());
            }
            if (line.startsWith(DPD_PATTERNS_FILE)) {
                this.setDesignPatternTemplatePath(line.substring(line.indexOf(":") + 1).trim());
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
    public boolean save(final File file) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(file));
            if (this.systemUnderConsiderationPath != null) {
                out.write(DPD_SYSTEM_FILE + ":" + this.systemUnderConsiderationPath);
                out.newLine();
            }
            if (this.designPatternFilePath != null) {
                out.write(DPD_PATTERNS_FILE + ":" + this.designPatternFilePath);
                out.newLine();
            }
            out.newLine();

            // Succesfully written to file. Change project name and file.
            this.setName(file.getName());
            this.projectFile = file;
            this.pristine = true;
        } catch (Exception ex) {
            LOGGER.error("Saving project failed.", ex);
            throw new DesignPatternDetectorException("Saving project failed.", ex);

        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
        LOGGER.info("Project " + file.getName() + " saved successfully to file " + file.getPath() + ".");
        return true;
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
    public String getSystemUnderConsiderationFilePath() {
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
     * Gets the filename of the XML-imput file containing the design patterns.
     *
     * @return the complete filename
     */
    public String getDesignPatternFilePath() {
        return designPatternFilePath;
    }

    /**
     * Sets the filename of the XML-imput file containing the design pattern. When the new value differs from the
     * original value, the pristine attribute of the {@link Project} is set to {@code false}.
     *
     * @param designPatternFilePath the new complete filename
     */
    public void setDesignPatternTemplatePath(String designPatternFilePath) {
        if (different(this.designPatternFilePath, designPatternFilePath)) {
            this.designPatternFilePath = designPatternFilePath;
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
