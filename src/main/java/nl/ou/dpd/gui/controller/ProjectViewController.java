package nl.ou.dpd.gui.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import nl.ou.dpd.domain.Clazz;
import nl.ou.dpd.domain.Edge;
import nl.ou.dpd.domain.MatchedClasses;
import nl.ou.dpd.domain.Solution;
import nl.ou.dpd.gui.model.Model;
import nl.ou.dpd.gui.model.Project;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * A {@link Controller} for the project view of the application.
 * <p>
 * TODO: this should not be a singleton, but there should also not be multiple instance of this class!! Fix it in the ControllerFactoryCreator if possible.
 *
 * @author Martin de Boer
 */
public class ProjectViewController extends Controller implements Observer {

    private static final Logger LOGGER = LogManager.getLogger(ProjectViewController.class);

    @FXML
    private Label projectNameLabel;

    @FXML
    private TextField systemFileTextField;

    @FXML
    private TextField templateFileTextField;

    @FXML
    private Button analyseButton;

    @FXML
    private ComboBox<String> maxMissingEdgesComboBox;

    @FXML
    private TreeView<String> feedbackTreeView;

    @FXML
    private Label feedbackPatternNameLabel;

    @FXML
    private Label feedbackPatternLabel;

    @FXML
    private Label feedbackMatchedClassesLabel;

    @FXML
    private GridPane feedbackMatchedClassesGridPane;

    @FXML
    private Label feedbackSuperfluousEdgesLabel;

    @FXML
    private GridPane feedbackSuperfluousEdgesGridPane;

    @FXML
    private Label feedbackMissingEdgesLabel;

    @FXML
    private GridPane feedbackMissingEdgesGridPane;

    private Map<String, Solution> feedbackMap;

    // Singleton
    private static ProjectViewController instance = null;

    /**
     * Returns the single instance of this class, or creates it if it does not exist.
     *
     * @param model the {@link Model} this controller updates
     * @return the singleton instance of the {@link ProjectViewController}.
     */
    public static ProjectViewController getInstance(Model model) {
        if (instance == null) {
            instance = new ProjectViewController(model);
        }
        return instance;
    }

    /**
     * A private constructor because the {@link ProjectViewController} is a singleton.
     *
     * @param model the model of the MVC pattern
     */
    private ProjectViewController(Model model) {
        super(model);
        model.addObserver(this);
    }

    /**
     * Called to initialize a controller after its root element has been completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  <tt>null</tt> if the location is not known.
     * @param resources The resources used to localize the root object, or <tt>null</tt> if
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        feedbackTreeView.setOnMouseClicked(createMouseHandler());
    }

    /**
     * Notifies the model the value of the max missing edges is updated.
     *
     * @param event is ignored
     */
    @FXML
    protected void maxMissingEdgesChanged(ActionEvent event) {
        getModel().setMaxMissingEdges(Integer.parseInt(maxMissingEdgesComboBox.getValue()));
    }

    /**
     * Select a "system under consideration" file from disk, and store it in the currently open
     * {@link Project}. The {@link Model} will notify its {@link Observer}s (among which this
     * {@link ProjectViewController}).
     *
     * @param event is ignored
     */
    @FXML
    protected void chooseSystemFile(ActionEvent event) {
        getModel().chooseSystemFile();
    }

    /**
     * Let the model select a desing pattern template file from disk, and store it in the currently open
     * {@link Project}. The {@link Model} will notify its {@link Observer}s (among which this
     * {@link ProjectViewController}).
     *
     * @param event is ignored
     */
    @FXML
    protected void chooseTemplateFile(ActionEvent event) {
        getModel().chooseTemplateFile();
    }

    /**
     * Starts the analysis of the system under consideration, and processes the feedback data.
     */
    @FXML
    protected void analyse() {
        final int maxMissingEdges = Integer.parseInt(this.maxMissingEdgesComboBox.getValue());

        Map<String, List<Solution>> result = null;
        try {
            result = getModel().analyse(maxMissingEdges);
        } catch (Exception e) {
            LOGGER.error("Error during analysis: ", e);

            // Show error to the user.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not analyse input data");
            alert.setContentText(e.getMessage());
            alert.showAndWait();

            return;
        }

        final TreeItem<String> treeRoot = new TreeItem<>("Design patterns");
        treeRoot.setExpanded(true);

        feedbackTreeView.setRoot(treeRoot);

        feedbackMap = new HashMap<>();
        int patternCount = 0;
        for (String patternName : result.keySet()) {
            final List<Solution> solutions = result.get(patternName);
            final TreeItem<String> patternRoot = new TreeItem<>(patternName + " (" + solutions.size() + ")");
            treeRoot.getChildren().add(patternRoot);

            int i = 0;
            boolean postFix = solutions.size() > 1;
            for (Solution solution : solutions) {
                String dpn = solution.getDesignPatternName();
                ++i;
                if (postFix) {
                    dpn += (i);
                }
                final TreeItem<String> patternItem = new TreeItem<>(dpn);
                patternRoot.getChildren().add(patternItem);
                feedbackMap.put(dpn, solution);
            }

            patternCount += i;
        }

        treeRoot.setValue(treeRoot.getValue() + " (" + patternCount + ")");
    }

    /**
     * Creates an {@link EventHandler} to handle mouse clicks in the {@link TreeView} containing the detected patterns.
     * This handler shows feedback details in the scroll pane, for any the pattern that is clicked in the tree view.
     *
     * @return a mouseclick handler
     */
    private EventHandler<MouseEvent> createMouseHandler() {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(final MouseEvent event) {
                final Node node = event.getPickResult().getIntersectedNode();
                if (node instanceof Text) {
                    showFeedbackDetails(((Text) node).getText());
                }
            }
        };
    }

    private void showFeedbackDetails(final String key) {
        Solution solution = feedbackMap.get(key);
        if (solution != null) {
            feedbackPatternNameLabel.setText("Feedback information for " + key);
            feedbackPatternLabel.setText("Design pattern: " + solution.getDesignPatternName());

            // Show matching classes
            feedbackMatchedClassesLabel.setText("Matched classes");
            clearGridPane(feedbackMatchedClassesGridPane);
            int row = 0;
            final MatchedClasses matchedClasses = solution.getMatchedClasses();
            feedbackMatchedClassesGridPane.add(new Text("System class"), 0, row);
            feedbackMatchedClassesGridPane.getChildren().get(0).setStyle("-fx-font-weight: 600");
            feedbackMatchedClassesGridPane.add(new Text("Design pattern class"), 2, row);
            feedbackMatchedClassesGridPane.getChildren().get(1).setStyle("-fx-font-weight: 600");
            for (Clazz cls : matchedClasses.getBoundSystemClassesSorted()) {
                int col = 0;
                row++;
                feedbackMatchedClassesGridPane.add(new Text(cls.getName()), col++, row);
                feedbackMatchedClassesGridPane.add(new Text("-->"), col++, row);
                feedbackMatchedClassesGridPane.add(new Text(matchedClasses.get(cls).getName()), col, row);
            }

            // Show superfluous edges
            feedbackSuperfluousEdgesLabel.setText("Edges that do not belong to the design pattern");
            clearGridPane(feedbackSuperfluousEdgesGridPane);
            row = 0;
            final Set<Edge> superfluousEdges = solution.getSuperfluousEdges();
            if (superfluousEdges.size() == 0) {
                feedbackSuperfluousEdgesGridPane.add(new Text("No superfluous edges found."), 0, row);
            }
            for (Edge edge : superfluousEdges) {
                int col = 0;
                feedbackSuperfluousEdgesGridPane.add(new Text(edge.getClass1().getName()), col++, row);
                feedbackSuperfluousEdgesGridPane.add(new Text("-->"), col++, row);
                feedbackSuperfluousEdgesGridPane.add(new Text(edge.getClass2().getName()), col, row);
                row++;
            }

            // Show missing edges
            feedbackMissingEdgesLabel.setText("Missing edges");
            clearGridPane(feedbackMissingEdgesGridPane);
            row = 0;
            final Set<Edge> missingEdges = solution.getMissingEdges();
            if (missingEdges.size() == 0) {
                feedbackMissingEdgesGridPane.add(new Text("No missing edges found."), 0, row);
            }
            for (Edge edge : missingEdges) {
                int col = 0;
                feedbackMissingEdgesGridPane.add(new Text(edge.getClass1().getName()), col++, row);
                feedbackMissingEdgesGridPane.add(new Text("-->"), col++, row);
                feedbackMissingEdgesGridPane.add(new Text(edge.getClass2().getName()), col, row);
                row++;
            }

        }
    }

    /**
     * This method is called when an {@link Observable} calls the {@code notifyObservers} method. At that moment this
     * {@link ProjectViewController}, being an instance of the {@link Observer} interface, will be updated.
     *
     * @param o   the observable object
     * @param arg a {@link Project} that is opened by the user (or {@code null} if none is currently opened.
     */
    @Override
    public void update(Observable o, Object arg) {
        if (arg != null) {
            // Synchronise fields with the open project
            final Project project = (Project) arg;
            if (project.isPristine()) {
                projectNameLabel.setText(project.getName());
            } else {
                projectNameLabel.setText(project.getName() + " *");
            }
            systemFileTextField.setText(project.getSystemUnderConsiderationPath());
            templateFileTextField.setText(project.getDesignPatternTemplatePath());
            this.maxMissingEdgesComboBox.setValue(Integer.toString(project.getMaxMissingEdges()));

            // Enable/disable the analyse button
            final boolean templateFileEmpty = templateFileTextField == null
                    || templateFileTextField.getText() == null
                    || templateFileTextField.getText().isEmpty();
            final boolean systemFileEmpty = systemFileTextField == null
                    || systemFileTextField.getText() == null
                    || systemFileTextField.getText().isEmpty();
            analyseButton.setDisable(templateFileEmpty || systemFileEmpty);
        } else {
            // Whipe the fields' values
            projectNameLabel.setText(null);
            systemFileTextField.setText(null);
            templateFileTextField.setText(null);
            this.maxMissingEdgesComboBox.setValue("0");
            analyseButton.setDisable(true);
        }
    }

    private void clearGridPane(final GridPane gridPane) {
        final ObservableList<Node> children = gridPane.getChildren();
        if (children != null && children.size() != 0) {
            children.clear();
        }
    }

}

