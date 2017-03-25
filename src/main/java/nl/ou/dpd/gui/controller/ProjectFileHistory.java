package nl.ou.dpd.gui.controller;

import nl.ou.dpd.gui.model.Project;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
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

    private List<File> projectFiles = new ArrayList<>();

    /**
     * Returns the most recently stored {@link Project} {@link File}s.
     *
     * @return a {@link List} of {@link Files}s.
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
}
