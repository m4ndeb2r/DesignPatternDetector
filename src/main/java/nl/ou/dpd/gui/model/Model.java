package nl.ou.dpd.gui.model;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import nl.ou.dpd.domain.DesignPattern;
import nl.ou.dpd.domain.SystemUnderConsideration;
import nl.ou.dpd.domain.matching.PatternInspector;
import nl.ou.dpd.domain.matching.Solution;
import nl.ou.dpd.gui.controller.ControllerFactoryCreator;
import nl.ou.dpd.parsing.argoxmi.ArgoUMLParser;
import nl.ou.dpd.parsing.pattern.PatternsParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;


/**
 * Manages control flow of views.
 *
 * @author Martin de Boer
 */
public class Model extends Observable {

    private static final Logger LOGGER = LogManager.getLogger(Model.class);

    private static final String MAINVIEW_FXML = "fxml/mainview.fxml";
    private static final String PROJECTVIEW_FXML = "fxml/projectview.fxml";

    private final RetentionFileChooser fileChooser;
    private Scene scene;
    private Callback<Class<?>, Object> controllerFactory;
    private Project openProject = null;

    /**
     * Constructor expecting a {@link Scene} as input parameter.
     *
     * @param scene the {@link Scene} to load views onto.
     */
    public Model(Scene scene) {
        this.scene = scene;
        this.controllerFactory = ControllerFactoryCreator.createControllerFactory(this);
        this.fileChooser = new RetentionFileChooser(new FileChooser());
    }

    /**
     * Shows the main view of the application and notifies the {@link java.util.Observer}s.
     */
    public void showMainView() {
        openProject = null;
        showView(MAINVIEW_FXML);
        setChangedAndNotifyObservers();
    }

    /**
     * Shows the project view of the application and notifies the {@link java.util.Observer}s.
     */
    public void newProject() {
        openProject = new Project();
        showView(PROJECTVIEW_FXML);
        setChangedAndNotifyObservers();
    }

    /**
     * Lets the user pick a {@link Project} {@link File} from the system, opens that {@link Project} and notifies the
     * {@link java.util.Observer}s.
     *
     * @throws FileNotFoundException when the project file does not exits.
     */
    public void openProject() throws FileNotFoundException {
        this.openProject(this.chooseFile("Project files (*.dpd)", "*.dpd"));
    }

    /**
     * Opens an existing {@link Project}, based on the contents of the specified {@link File} and notifies the
     * {@link java.util.Observer}s.
     *
     * @param projectFile a project file containing {@link Project} information
     * @throws FileNotFoundException when the project file does not exits.
     */
    public void openProject(File projectFile) throws FileNotFoundException {
        if (projectFile != null) {
            openProject = new Project(projectFile);
        }
        if (hasOpenProject()) {
            showView(PROJECTVIEW_FXML);
        } else {
            showView(MAINVIEW_FXML);
        }
        setChangedAndNotifyObservers();
    }

    /**
     * Saves the current project.
     *
     * @return {@code true} if the project was successfully saved, or {@code false} otherwise.
     */
    public boolean saveProject() {
        if (openProject.getProjectFile() == null) {
            return saveProjectAs();
        }
        return openProject.save();
    }

    /**
     * Saves the current project as ...  and notifies the {@link java.util.Observer}s.
     *
     * @return {@code true} if the project was successfully saved, or {@code false} otherwise.
     */
    public boolean saveProjectAs() {
        final FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Project files (*.dpd)", "*.dpd");

        fileChooser.setTitle("Save Project");
        fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().add(filter);

        final File file = fileChooser.showSaveDialog(scene.getWindow());
        if (file != null && openProject.save(file)) {
            setChangedAndNotifyObservers();
            return true;
        }
        return false;
    }

    /**
     * Closes the current project and notifies the {@link java.util.Observer}s.
     */
    public void closeProject() {
        openProject = null;
        showView(MAINVIEW_FXML);
        setChangedAndNotifyObservers();
    }

    /**
     * Determines if the project can be closed without loosing any changes to it.
     *
     * @return {@code true} if the project can be closed without loosing changes, or {@code false} otherwise.
     */
    public boolean canCloseProjectWithoutDataLoss() {
        return openProject != null && openProject.isPristine();
    }

    /**
     * Determines whether there is a project that is currently open.
     *
     * @return {@code true} if a project is currently open, or (@code false} otherwise.
     */
    public boolean hasOpenProject() {
        return openProject != null;
    }

    /**
     * Returns the location of the {@link File} of the currently open {@link Project}, or {@code null} if no project
     * is currently open, or when the {@link Project} was not yet saved to a {@link File}.
     *
     * @return the path of the project {@link File} or {@code null} if no such file exists or no {@link Project} is
     * currently open.
     */
    public String getOpenProjectFilePath() {
        if (this.openProject != null && this.openProject.getProjectFile() != null) {
            return this.openProject.getProjectFile().getPath();
        } else {
            return null;
        }
    }

    /**
     * Sets the max numnber of allowed edges missing for the currently opened project.
     *
     * @param m the new number of max allowed missing edges
     */
    public void setMaxMissingEdges(int m) {
        if (openProject != null) {
            openProject.setMaxMissingEdges(m);
            setChangedAndNotifyObservers();
        }
    }

    /**
     * Opens a dialog to choose a file with extension {@code *.xmi}. Notifies {@link java.util.Observer}s when a
     * file was chosen.
     */
    public void chooseSystemFile() {
        File chosenFile = this.chooseFile("ArgoUML export files (*.xmi)", "*.xmi");
        if (chosenFile != null
                && chosenFile.getPath() != null
                && !chosenFile.getPath().equals(openProject.getSystemUnderConsiderationPath())) {
            openProject.setSystemUnderConsiderationPath(chosenFile.getPath());
            setChangedAndNotifyObservers();
        }
    }

    /**
     * Opens a dialog to choose a file with extension {@code *.xml}. Notifies {@link java.util.Observer}s when a
     * file was chosen.
     */
    public void chooseTemplateFile() {
        File chosenFile = this.chooseFile("XML template files (*.xml)", "*.xml");
        if (chosenFile != null
                && chosenFile.getPath() != null
                && !chosenFile.getPath().equals(openProject.getDesignPatternTemplatePath())) {
            openProject.setDesignPatternTemplatePath(chosenFile.getPath());
            setChangedAndNotifyObservers();
        }
    }

    /**
     * Parses the specified input files, and attempts to detect design patterns defined in the template file, in the
     * "system under consideration" file. The results are gathered in a {@link Map} containing {@link List}s of
     * {@link Solution}s as values, and the name of the pattern as key.
     *
     * @param maxMissingEdges the maximum allowed number of missing edges.
     * @return a {@link Map} containing the gathered results
     */
    public Map<String, List<Solution>> analyse(int maxMissingEdges) {
        // Parse the input files
        final SystemUnderConsideration system = new ArgoUMLParser().parse(openProject.getSystemUnderConsiderationPath());
        final List<DesignPattern> designPatterns = new PatternsParser().parse(openProject.getDesignPatternTemplatePath());

        Map<String, List<Solution>> assembledMatchResults = new HashMap<>();
        designPatterns.forEach(pattern -> {
            final PatternInspector patternInspector = new PatternInspector(system, pattern);
            List<Solution> solutions = patternInspector.getSolutions();
            if (solutions.size() > 0) {
                assembledMatchResults.put(pattern.getName(), solutions);
            }
        });

        return assembledMatchResults;
    }

    private File chooseFile(String filterDescription, String... filterExtension) {
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(filterDescription, filterExtension);
        fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().add(extFilter);
        return fileChooser.showOpenDialog(scene.getWindow());
    }

    private void showView(final String resource) {
        try {
            final URL location = getClass().getClassLoader().getResource(resource);
            final FXMLLoader loader = new FXMLLoader(location);
            loader.setControllerFactory(controllerFactory);
            scene.setRoot((Parent) loader.load());
        } catch (Exception e) {
            LOGGER.error("Unable to open resource " + resource + ".", e);
        }
    }

    private void setChangedAndNotifyObservers() {
        setChanged();
        notifyObservers(openProject);
    }

}
