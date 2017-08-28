package nl.ou.dpd.gui.model;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import nl.ou.dpd.DesignPatternDetector;
import nl.ou.dpd.domain.DesignPattern;
import nl.ou.dpd.domain.SystemUnderConsideration;
import nl.ou.dpd.domain.matching.PatternInspector;
import nl.ou.dpd.exception.DesignPatternDetectorException;
import nl.ou.dpd.parsing.ArgoUMLParser;
import nl.ou.dpd.parsing.ParserFactory;
import nl.ou.dpd.parsing.PatternsParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
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

    private static final String HELPVIEW_FXML = "fxml/helpview.fxml";
    private static final String MAINVIEW_FXML = "fxml/mainview.fxml";
    private static final String ABOUTVIEW_FXML = "fxml/aboutview.fxml";
    private static final String PROJECTVIEW_FXML = "fxml/projectview.fxml";

    private static final String NO_CONTROLLER_ERR_MSG = "Initialization error. No controller factory was set in model.";
    private static final String UNABLE_TO_OPEN_RESOURCE_MSG = "Unable to open resource '%s'.";

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
        this.fileChooser = new RetentionFileChooser(new FileChooser());
    }

    /**
     * Sets the controller factory for this model. A controller factory is {@link Callback} object that is used
     * by the {@link FXMLLoader} to determine the controller object when loading a view.
     *
     * @param controllerFactory the {@link Callback} object used for determining the appropriate controller(s)
     */
    public void setControllerFactory(Callback<Class<?>, Object> controllerFactory) {
        this.controllerFactory = controllerFactory;
    }

    /**
     * Shows the main view of the application and notifies the {@link java.util.Observer}s.
     */
    public void showMainView() {
        openProject = null;
        showView(MAINVIEW_FXML);
        setChangedAndNotifyObservers();
    }

    public void showAboutWindow() {
        showWindow(ABOUTVIEW_FXML, "About");
    }

    public void showHelpWindow() {
        showWindow(HELPVIEW_FXML, "Help");
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
        if (openProject.save()) {
            setChangedAndNotifyObservers();
            return true;
        }
        return false;
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
     * Opens a dialog to choose a file with extension {@code *.xmi}. Notifies {@link java.util.Observer}s when a
     * file was chosen.
     */
    public void chooseSystemFile() {
        File chosenFile = this.chooseFile("ArgoUML export files (*.xmi)", "*.xmi");
        if (chosenFile != null
                && chosenFile.getPath() != null
                && !chosenFile.getPath().equals(openProject.getSystemUnderConsiderationFilePath())) {
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
                && !chosenFile.getPath().equals(openProject.getDesignPatternFilePath())) {
            openProject.setDesignPatternTemplatePath(chosenFile.getPath());
            setChangedAndNotifyObservers();
        }
    }

    /**
     * Parses the specified input files, and attempts to detect design patterns defined in the template file, in the
     * "system under consideration" file. The results are gathered in a {@link Map} containing {@link List}s of
     * {@link PatternInspector.MatchingResult}s as values, and the name of the pattern as key.
     *
     * @return a {@link Map} containing the gathered results
     */
    public Map<String, PatternInspector.MatchingResult> analyse() {
        // Parse the xmi input file
        final ArgoUMLParser argoUMLParser = ParserFactory.createArgoUMLParser();
        final SystemUnderConsideration system = argoUMLParser.parse(openProject.getSystemUnderConsiderationFilePath());

        // Parse the xml input file
        final PatternsParser patternsParser = ParserFactory.createPatternParser();
        final List<DesignPattern> designPatterns = patternsParser.parse(openProject.getDesignPatternFilePath());

        // Analyse the system under consideration
        final Map<String, PatternInspector.MatchingResult> assembledMatchResults = new HashMap<>();
        designPatterns.forEach(pattern -> {
            final PatternInspector patternInspector = new PatternInspector(system, pattern);
            final String patternName = pattern.getName();
            final String patternFamilyName = pattern.getFamily();
            String key = patternName;
            if (!patternName.equals(patternFamilyName)) {
                key = String.format("%s (%s)", patternName, patternFamilyName);
            }
            assembledMatchResults.put(key, patternInspector.getMatchingResult());
        });

        return assembledMatchResults;
    }

    private File chooseFile(String filterDescription, String... filterExtension) {
        final FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(filterDescription, filterExtension);
        fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().add(extFilter);
        return fileChooser.showOpenDialog(scene.getWindow());
    }

    private void showView(final String resource) {
        final Parent parent = loadParentFromResource(resource);
        scene.setRoot(parent);
    }

    private void showWindow(final String resource, String title) {
        final Parent parent = loadParentFromResource(resource);
        final Scene scene = new Scene(parent);
        scene.getStylesheets().add(DesignPatternDetector.class.getResource(DesignPatternDetector.STYLE_SHEET).toExternalForm());

        final Stage stage = new Stage();
        stage.setScene(scene);

        // Get the app icon
        final InputStream iconResource = DesignPatternDetector.class.getResourceAsStream(DesignPatternDetector.ICON_NAME);
        if (iconResource != null) {
            stage.getIcons().add(new Image(iconResource));
        }
        stage.setTitle(title);
        stage.initStyle(StageStyle.UTILITY);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setWidth(575.0);
        stage.setHeight(700.0);

        stage.show();
    }

    private Parent loadParentFromResource(String resource) {
        if (controllerFactory == null) {
            throw new DesignPatternDetectorException(NO_CONTROLLER_ERR_MSG);
        }
        try {
            final URL location = getClass().getClassLoader().getResource(resource);
            final FXMLLoader loader = new FXMLLoader(location);
            loader.setControllerFactory(controllerFactory);
            return (Parent) loader.load();
        } catch (Exception e) {
            final String msg = String.format(UNABLE_TO_OPEN_RESOURCE_MSG, resource);
            LOGGER.error(msg, e);
            throw new DesignPatternDetectorException(msg, e);
        }
    }

    private void setChangedAndNotifyObservers() {
        setChanged();
        notifyObservers(openProject);
    }

}
