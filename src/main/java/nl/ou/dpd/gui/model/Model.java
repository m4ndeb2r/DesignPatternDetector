package nl.ou.dpd.gui.model;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import nl.ou.dpd.data.argoxmi.ArgoXMIParser;
import nl.ou.dpd.data.template.TemplatesParser;
import nl.ou.dpd.domain.DesignPattern;
import nl.ou.dpd.domain.Matcher;
import nl.ou.dpd.domain.Solution;
import nl.ou.dpd.domain.SystemUnderConsideration;
import nl.ou.dpd.gui.controller.ControllerFactoryCreator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Manages control flow of views.
 *
 * @author Martin de Boer
 */
public class Model {

    private static final Logger LOGGER = LogManager.getLogger(Model.class);

    private static final String MAINVIEW_FXML = "fxml/mainview.fxml";
    private static final String PROJECTVIEW_FXML = "fxml/projectview.fxml";

    private Scene scene;
    private Callback<Class<?>, Object> controllerFactory;

    // TODO: replace with the actual open project
    private Object openProject = null;

    private File systemFile;
    private File templateFile;

    private final Matcher matcher;


    /**
     * Constructor expecting a {@link Scene} as input parameter.
     *
     * @param scene the {@link Scene} to load views onto.
     */
    public Model(Scene scene) {
        this.scene = scene;
        this.controllerFactory = ControllerFactoryCreator.createControllerFactory(this);
        this.matcher = new Matcher();
    }

    /**
     * Shows the main view of the application.
     */
    public void showMainView() {
        openProject = null;
        showView(MAINVIEW_FXML);
    }

    /**
     * Shows the project view of the application.
     */
    public void newProject() {
        openProject = "DUMMY";
        showView(PROJECTVIEW_FXML);
    }

    /**
     * Opens an existing project.
     */
    public void openProject() {
        openProject = "DUMMY";
        showView(PROJECTVIEW_FXML);
    }

    /**
     * Saves the current project.
     */
    public void saveProject() {
    }

    /**
     * Saves the current project as ...
     */
    public void saveProjectAs() {
    }

    /**
     * Closes the current project.
     */
    public void closeProject() {
        openProject = null;
        showView(MAINVIEW_FXML);
    }

    /**
     * Determins whether there is a project that is currently open.
     *
     * @return {@code true} if a project is currently open, or (@code false} otherwise.
     */
    public boolean hasOpenProject() {
        return openProject != null;
    }

    /**
     * Opens a dialog to choose a file with extension {@code *.xmi}.
     *
     * @return a string representation of the file path, or {@code null} if no file was selected.
     */
    public String chooseSystemFile() {
        systemFile = this.chooseFile("ArgoUML export files (*.xmi)", "*.xmi");
        if (systemFile == null) {
            return null;
        } else {
            return systemFile.getPath();
        }
    }

    /**
     * Opens a dialog to choose a file with extension {@code *.xml}.
     *
     * @return a string representation of the file path, or {@code null} if no file was selected.
     */
    public String chooseTemplateFile() {
        templateFile = this.chooseFile("XML template files (*.xml)", "*.xml");
        if (templateFile == null) {
            return null;
        } else {
            return templateFile.getPath();
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
        final SystemUnderConsideration system = new ArgoXMIParser().parse(systemFile.getAbsolutePath());
        final List<DesignPattern> designPatterns = new TemplatesParser().parse(templateFile.getAbsolutePath());

        Map<String, List<Solution>> assembledMatchResults = new HashMap<>();
        designPatterns.forEach(pattern -> {
            List<Solution> solutions = matcher.match(pattern, system, maxMissingEdges).getSolutions();
            if (solutions.size() > 0) {
                assembledMatchResults.put(pattern.getName(), solutions);
            }
        });

        return assembledMatchResults;
    }

    private File chooseFile(String filterDescription, String... filterExtension) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(filterDescription, filterExtension);
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

}
