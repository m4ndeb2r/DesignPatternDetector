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
import nl.ou.dpd.domain.matching.Feedback;
import nl.ou.dpd.domain.matching.FeedbackType;
import nl.ou.dpd.domain.matching.PatternInspector;
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

    private static final String FEEDBACK = "Feedback";

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
    private Label feedbackTitle;

    @FXML
    private Label feedbackSubtitle;

    @FXML
    private Label matchedClassesLabel;

    @FXML
    private GridPane matchedClassesGridPane;

    @FXML
    private Label nodesFeedbackSubtitle;

    @FXML
    private GridPane nodesFeedbackGridPane;

    @FXML
    private Label relationsFeedbackSubtitle;

    @FXML
    private GridPane relationsFeedbackGridPane;

    private Map<String, Solution> solutionMap;
    private Map<String, Feedback> feedbackMap;

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
        Map<String, PatternInspector.MatchingResult> result;
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

        clearData();

        int patternCount = 0;
        for (String patternName : result.keySet()) {
            final List<Solution> solutions = result.get(patternName).getSolutions();
            final Feedback feedback = result.get(patternName).getFeedback();
            final TreeItem<String> patternRoot = new TreeItem<>(makePatternRootName(patternName, solutions));

            treeRoot.getChildren().add(patternRoot);
            patternCount += addSolutionsToTree(solutions, patternRoot);
            addFeedbackToTree(feedback, patternRoot, patternName);
        }

        treeRoot.setValue(treeRoot.getValue() + " (" + patternCount + ")");

        // Sort by patterns by name
        treeRoot.getChildren().sort(Comparator.comparing(t -> t.getValue()));

        // Enable clear button
        clearButton.setDisable(false);
    }

    private void addFeedbackToTree(Feedback feedback, TreeItem<String> patternRoot, String patternName) {
        final TreeItem<String> feedbackItem = new TreeItem<>("Feedback " + patternName);
        patternRoot.getChildren().add(feedbackItem);
        feedbackMap.put(patternName, feedback);
    }

    private int addSolutionsToTree(List<Solution> solutions, TreeItem<String> patternRoot) {
        int i = 0;
        boolean multipleSolutions = solutions.size() > 1;
        for (Solution solution : solutions) {
            String dpn = solution.getDesignPatternName();
            ++i;
            if (multipleSolutions) {
                dpn += String.format("-%d", i);
                final TreeItem<String> patternItem = new TreeItem<>(dpn);
                patternRoot.getChildren().add(patternItem);
            }
            solutionMap.put(dpn, solution);
        }
        return i;
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
            return String.format("%s (%d)", patternName, solutions.size());
        }
        return patternName;
    }

    /**
     * Creates an {@link EventHandler} to handle mouse clicks in the {@link TreeView} containing the detected patterns.
     * This handler shows solution or feedback details in the scroll pane, depending on the item that is clicked in the
     * tree view.
     *
     * @return a mouseclick handler
     */
    private EventHandler<MouseEvent> createMouseHandler() {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(final MouseEvent event) {
                final Node node = event.getPickResult().getIntersectedNode();
                if (node instanceof Text) {
                    final String key = ((Text) node).getText();
                    if (key.startsWith(FEEDBACK)) {
                        showFeedbackDetails(key.substring(FEEDBACK.length() + 1));
                    } else {
                        showSolutionDetails(key);
                    }
                }
            }
        };
    }

    private void showFeedbackDetails(final String key) {
        clearDetails();

        // Hide/display components
        matchedClassesGridPane.setManaged(false);
        matchedClassesLabel.setManaged(false);
        nodesFeedbackSubtitle.setManaged(true);
        nodesFeedbackGridPane.setManaged(true);
        relationsFeedbackSubtitle.setManaged(true);
        relationsFeedbackGridPane.setManaged(true);

        feedbackTitle.setText("Analysis feedback");
        feedbackSubtitle.setText(String.format("Design pattern: %s", key));

        final Feedback feedback = feedbackMap.get(key);

        nodesFeedbackSubtitle.setText("Class/interface feedback");
        int row = 0;
        for (nl.ou.dpd.domain.node.Node node : feedback.getNodeSet()) {
            int col = 0;
            nodesFeedbackGridPane.add(new Text(node.getName()), col, row++);
            for(String s : feedback.getFeedbackMessages(node, FeedbackType.INFO)) {
                nodesFeedbackGridPane.add(new Text(s), col, row++);
            }
            for(String s : feedback.getFeedbackMessages(node, FeedbackType.MATCH)) {
                nodesFeedbackGridPane.add(new Text(s), col, row++);
            }
            for(String s : feedback.getFeedbackMessages(node, FeedbackType.MISMATCH)) {
                nodesFeedbackGridPane.add(new Text(s), col, row++);
            }
            for(String s : feedback.getFeedbackMessages(node, FeedbackType.NOT_ANALYSED)) {
                nodesFeedbackGridPane.add(new Text(s), col, row++);
            }
        }

        relationsFeedbackSubtitle.setText("Relation feedback");
        row = 0;
        for (nl.ou.dpd.domain.relation.Relation relation : feedback.getRelationSet()) {
            int col = 0;
            relationsFeedbackGridPane.add(new Text(relation.getName()), col, row++);
            for(String s : feedback.getFeedbackMessages(relation, FeedbackType.INFO)) {
                relationsFeedbackGridPane.add(new Text(s), col, row++);
            }
            for(String s : feedback.getFeedbackMessages(relation, FeedbackType.MATCH)) {
                relationsFeedbackGridPane.add(new Text(s), col, row++);
            }
            for(String s : feedback.getFeedbackMessages(relation, FeedbackType.MISMATCH)) {
                relationsFeedbackGridPane.add(new Text(s), col, row++);
            }
            for(String s : feedback.getFeedbackMessages(relation, FeedbackType.NOT_ANALYSED)) {
                relationsFeedbackGridPane.add(new Text(s), col, row++);
            }
        }

        // TODO: this has to go.... (temporary)
        System.out.println("\nNode feedback:");
        feedback.getNodeSet().iterator().forEachRemaining(node -> {
            System.out.println("\n\tNode: " + node.getName());
            feedback.getFeedbackMessages(node, FeedbackType.INFO).forEach(s -> System.out.println("\t- " + s));
            feedback.getFeedbackMessages(node, FeedbackType.MATCH).forEach(s -> System.out.println("\t- " + s));
            feedback.getFeedbackMessages(node, FeedbackType.MISMATCH).forEach(s -> System.out.println("\t- " + s));
            feedback.getFeedbackMessages(node, FeedbackType.NOT_ANALYSED).forEach(s -> System.out.println("\t- " + s));
        });
        System.out.println("\nRelation feedback:");
        feedback.getRelationSet().iterator().forEachRemaining(r -> {
            System.out.println("\n\tRelation: " + r.getName());
            feedback.getFeedbackMessages(r, FeedbackType.INFO).forEach(s -> System.out.println("\t- " + s));
            feedback.getFeedbackMessages(r, FeedbackType.MATCH).forEach(s -> System.out.println("\t- " + s));
            feedback.getFeedbackMessages(r, FeedbackType.MISMATCH).forEach(s -> System.out.println("\t- " + s));
            feedback.getFeedbackMessages(r, FeedbackType.NOT_ANALYSED).forEach(s -> System.out.println("\t- " + s));
        });

    }

    private void showSolutionDetails(final String key) {
        clearDetails();

        // Hide/display components
        nodesFeedbackSubtitle.setManaged(false);
        nodesFeedbackGridPane.setManaged(false);
        relationsFeedbackSubtitle.setManaged(false);
        relationsFeedbackGridPane.setManaged(false);
        matchedClassesLabel.setManaged(true);
        matchedClassesGridPane.setManaged(true);

        final Solution solution = solutionMap.get(key);
        if (solution != null) {
            feedbackTitle.setText(String.format("Solution: %s", key));
            feedbackSubtitle.setText(getPatternLabelText(solution));

            // Show matching nodes
            matchedClassesLabel.setText("Matched classes");
            int row = 0;
            final List<String[]> matchingNodeNames = solution.getMatchingNodeNames();
            matchedClassesGridPane.add(new Text("Design pattern class"), 0, row);
            matchedClassesGridPane.add(new Text("System class"), 2, row);
            matchedClassesGridPane.getChildren().get(0).setStyle("-fx-font-weight: 600");
            matchedClassesGridPane.getChildren().get(1).setStyle("-fx-font-weight: 600");
            for (String[] nodeNames : matchingNodeNames) {
                int col = 0;
                row++;
                matchedClassesGridPane.add(new Text(nodeNames[1]), col++, row);
                matchedClassesGridPane.add(new Text("-->"), col++, row);
                matchedClassesGridPane.add(new Text(nodeNames[0]), col, row);
            }
        }
    }

    private String getPatternLabelText(Solution solution) {
        final String designPatternName = solution.getDesignPatternName();
        final String patternFamilyName = solution.getPatternFamilyName();
        if (designPatternName.equals(patternFamilyName)) {
            return String.format("Design pattern: %s", designPatternName);
        } else {
            return String.format("Design pattern: %s (%s)", designPatternName, patternFamilyName);
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
            systemFileTextField.setText(project.getSystemUnderConsiderationFilePath());
            templateFileTextField.setText(project.getDesignPatternFilePath());

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
        clearData();
        clearTreeView(feedbackTreeView);
        clearDetails();
        clearButton.setDisable(true);
    }

    private void clearData() {
        solutionMap = new HashMap<>();
        feedbackMap = new HashMap<>();
    }

    private void clearDetails() {
        feedbackSubtitle.setText(null);
        feedbackTitle.setText(null);

        matchedClassesLabel.setText(null);
        clearGridPane(matchedClassesGridPane);

        nodesFeedbackSubtitle.setText(null);
        clearGridPane(nodesFeedbackGridPane);

        relationsFeedbackSubtitle.setText(null);
        clearGridPane(relationsFeedbackGridPane);
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

