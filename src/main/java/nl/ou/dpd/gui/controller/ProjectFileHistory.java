package nl.ou.dpd.gui.controller;

import nl.ou.dpd.DesignPatternDetector;
import nl.ou.dpd.gui.model.Project;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Remembers the most recently used {@link Project} {@link File}s, so they can, of example, be shown in the File menu
 * of the application. This class is a singleton that is implemented as an enum with one
 *
 * @author Martin de Boer
 */
public enum ProjectFileHistory {

    /**
     * The single instance of this enum.
     */
    INSTANCE;

    private static final int MAX_LEN = 5;
    private static final String _DPD_MRU = "/.dpdmru";
    private static final Logger LOGGER = LogManager.getLogger(ProjectFileHistory.class);

    private List<File> projectFiles = new ArrayList<>();

    /**
     * Returns the most recently stored {@link Project} {@link File}s.
     *
     * @return a {@link List} of {@link File}s.
     */
    public List<File> getProjectFiles() {
        return projectFiles;
    }

    /**
     * Add a {@link File} to the list of most recently used {@link Project} {@link File}s. Also, the non-existant files
     * are taken from the list.
     *
     * @param newProjectFile the {@link File} to add on top of the list.
     * @return the new list with the newly added item on top
     */
    public List<File> addProjectFile(File newProjectFile) {
        // Remove old nonexistant files
        this.purgeProjectFiles();

        if (newProjectFile.exists()) {
            final List<File> newFiles = new ArrayList<>();

            // Prevent doubles
            projectFiles.remove(newProjectFile);

            // Add on top
            newFiles.add(newProjectFile);

            // Add the previous ones after that (MAX_LEN -1 files at most)
            for (int i = 1; i < MAX_LEN && i < this.projectFiles.size() + 1; i++) {
                newFiles.add(this.projectFiles.get(i - 1));
            }

            // Remember the new list.
            this.projectFiles = newFiles;
        }

        return this.projectFiles;
    }

    /**
     * Removes {@link File}s from the list that do not exist on the system anymore.
     */
    public void purgeProjectFiles() {
        this.projectFiles = this.projectFiles
                .stream()
                .filter(file -> file.exists())
                .collect(Collectors.toList());
    }

    /**
     * Reads the previously saved most recently used projects from the {@link File} {@code .dpdmru}.
     */
    public void restore() {
        try {
            final File projectHistoryFile = getProjectHistoryFile();
            if (projectHistoryFile.exists()) {
                final Scanner sc = new Scanner(projectHistoryFile);
                while (sc.hasNextLine()) {
                    this.projectFiles.add(new File(sc.nextLine()));
                }
            }
            // Remove nonexistant project files
            purgeProjectFiles();
        } catch (Exception ex) {
            LOGGER.warn("Restoring project history failed.", ex);
        }
    }

    /**
     * Stores most recently used projects to file.
     */
    public void store() {
        BufferedWriter out = null;
        try {
            final File projectHistoryFile = getProjectHistoryFile();
            if (!projectHistoryFile.exists()) {
                LOGGER.debug("Creating project history file: " + projectHistoryFile.getPath() + ".");
                projectHistoryFile.createNewFile();
            }
            out = new BufferedWriter(new FileWriter(projectHistoryFile));
            int count = 0;
            for (final File projFile : this.projectFiles) {
                out.write(projFile.getPath());
                out.newLine();
                count++;
            }
            LOGGER.debug("Project history ("
                    + count
                    + " projects) saved to "
                    + projectHistoryFile.getPath()
                    + ".");
        } catch (Exception ex) {
            LOGGER.warn("Saving project history failed.", ex);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private File getProjectHistoryFile() throws UnsupportedEncodingException, URISyntaxException {
        final CodeSource codeSource = DesignPatternDetector.class.getProtectionDomain().getCodeSource();
        final File jarFile = new File(codeSource.getLocation().toURI().getPath());
        final String jarDir = jarFile.getParentFile().getPath();
        final String projHistPath = jarDir + _DPD_MRU;
        final String decodedProjHistPath = URLDecoder.decode(projHistPath, "UTF-8");
        return new File(decodedProjHistPath);
    }
}
