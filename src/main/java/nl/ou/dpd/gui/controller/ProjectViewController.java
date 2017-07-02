package nl.ou.dpd.gui.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import nl.ou.dpd.domain.matching.Solution;
import nl.ou.dpd.gui.model.Model;
import nl.ou.dpd.gui.model.Project;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

/**
 * A {@link Controller} for the project view of the application.
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
    private Button clearButton;

    @FXML
    private Button analyseButton;

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

    /**
     * Constructs a {@link ProjectViewController} with the specified {@link Model}.
     *
     * @param model the model of the MVC pattern
     */
    public ProjectViewController(Model model) {
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
     * Let the model select a design pattern template file from disk, and store it in the currently open
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
     * Clears the feedback on the screen.
     */
    @FXML
    protected void clear() {
        clearFeedback();
    }

    /**
     * Starts the analysis of the system under consideration, and processes the feedback data.
     */
    @FXML
    protected void analyse() {
        Map<String, List<Solution>> result;
        try {
            result = getModel().analyse();
        } catch (Exception e) {
            LOGGER.error("Error during analysis: ", e);

            // Show error to the user.
            Alert alert = new CustomAlert(Alert.AlertType.ERROR);
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
            final TreeItem<String> patternRoot = new TreeItem<>(makePatternRootName(patternName, solutions));
            treeRoot.getChildren().add(patternRoot);

            int i = 0;
            boolean multipleSolutions = solutions.size() > 1;
            for (Solution solution : solutions) {
                String dpn = solution.getDesignPatternName();
                ++i;
                if (multipleSolutions) {
                    dpn += (i);
                    final TreeItem<String> patternItem = new TreeItem<>(dpn);
                    patternRoot.getChildren().add(patternItem);
                }
                feedbackMap.put(dpn, solution);
            }

            patternCount += i;
        }

        treeRoot.setValue(treeRoot.getValue() + " (" + patternCount + ")");

        // Sort by patterns by name
        treeRoot.getChildren().sort(Comparator.comparing(t -> t.getValue()));

        // Enable clear button
        clearButton.setDisable(false);
    }

    /**
     * Generates the name of a pattern root string, based on the {@code patternName} and the size of the
     * {@link solutions}. If there is only one solution, the {@code patternName} is returned; if there are several
     * solutions, the {@code patternName} is suffixed with the number of solutions between parenthesis.
     *
     * @param patternName the pattern name
     * @param solutions   the solutions found for a pattern
     * @return the name of the pattern followed by the number of solutions between parenthesis, or just the name of the
     * pattern (when there is just 1 solution).
     */
    private String makePatternRootName(String patternName, List<Solution> solutions) {
        if (solutions.size() > 1) {
            return patternName + " (" + solutions.size() + ")";
        }
        return patternName;
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

            // Show matching nodes
            feedbackMatchedClassesLabel.setText("Matched classes");
            clearGridPane(feedbackMatchedClassesGridPane);
            int row = 0;
            final List<String[]> matchingNodeNames = solution.getMatchingNodeNames();
            feedbackMatchedClassesGridPane.add(new Text("Design pattern class"), 0, row);
            feedbackMatchedClassesGridPane.add(new Text("System class"), 2, row);
            feedbackMatchedClassesGridPane.getChildren().get(0).setStyle("-fx-font-weight: 600");
            feedbackMatchedClassesGridPane.getChildren().get(1).setStyle("-fx-font-weight: 600");
            for (String[] nodeNames : matchingNodeNames) {
                int col = 0;
                row++;
                feedbackMatchedClassesGridPane.add(new Text(nodeNames[0]), col++, row);
                feedbackMatchedClassesGridPane.add(new Text("-->"), col++, row);
                feedbackMatchedClassesGridPane.add(new Text(nodeNames[1]), col, row);
            }

            // TODO: feedback here....

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
            analyseButton.setDisable(true);
        }
        clearFeedback();
    }

    private void clearFeedback() {
        // Clear data
        feedbackMap = new HashMap<>();

        // Clear treeview
        clearTreeView(feedbackTreeView);

        // Clear details
        feedbackPatternLabel.setText(null);
        feedbackPatternNameLabel.setText(null);

        feedbackMatchedClassesLabel.setText(null);
        clearGridPane(feedbackMatchedClassesGridPane);

        feedbackSuperfluousEdgesLabel.setText(null);
        clearGridPane(feedbackSuperfluousEdgesGridPane);

        feedbackMissingEdgesLabel.setText(null);
        clearGridPane(feedbackMissingEdgesGridPane);

        // Disable clear button
        clearButton.setDisable(true);
    }

    private void clearTreeView(final TreeView<String> treeView) {
        final TreeItem<String> root = treeView.getRoot();
        if (root != null) {
            final ObservableList<TreeItem<String>> children = root.getChildren();
            if (children != null && children.size() > 0) {
                children.clear();
            }
            treeView.setRoot(null);
        }
    }

    private void clearGridPane(final GridPane gridPane) {
        final ObservableList<Node> children = gridPane.getChildren();
        if (children != null && children.size() != 0) {
            children.clear();
        }
    }

}

