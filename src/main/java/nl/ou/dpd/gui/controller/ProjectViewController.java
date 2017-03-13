package nl.ou.dpd.gui.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import nl.ou.dpd.domain.Solution;
import nl.ou.dpd.gui.model.Model;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * A {@link Controller} for the project view of the application.
 *
 * @author Martin de Boer
 */
public class ProjectViewController extends Controller {

//    @FXML
//    private MenuController menuController;

    @FXML
    private TextField systemFileTextField;

    @FXML
    private TextField templateFileTextField;

    @FXML
    private Button analyseButton;

    @FXML
    private ComboBox<String> maxMissingEdgesComboBox;

    @FXML
    private TreeView<String> designPatternTreeView;

    @FXML
    private ScrollPane feedbackDetailsPane;

    private Map<String, Solution> feedbackMap;

    /**
     * Constructs a {@link ProjectViewController} with a reference to the specified {@link Model}.
     *
     * @param model the model of the MVC pattern
     */
    public ProjectViewController(Model model) {
        super(model);
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
        designPatternTreeView.setOnMouseClicked(createMouseHandler());
    }

    /**
     * Select a "system under consideration" file from disk.
     *
     * @param event is ignored
     */
    @FXML
    protected void chooseSystemFile(ActionEvent event) {
        String path = getModel().chooseSystemFile();
        if (path != null) {
            systemFileTextField.setText(path);
        }
    }

    /**
     * Select a dsisng pattern template file from disk.
     *
     * @param event is ignored
     */
    @FXML
    protected void chooseTemplateFile(ActionEvent event) {
        String path = getModel().chooseTemplateFile();
        if (path != null) {
            templateFileTextField.setText(path);
        }
        final boolean templateFileEmpty = templateFileTextField == null || templateFileTextField.getText().isEmpty();
        final boolean systemFileEmpty = systemFileTextField == null || systemFileTextField.getText().isEmpty();
        analyseButton.setDisable(templateFileEmpty || systemFileEmpty);
    }

    /**
     * Starts the analysis of the system under consideration, and processes the feedback data.
     */
    @FXML
    protected void analyse() {
        final int maxMissingEdges = Integer.parseInt(this.maxMissingEdgesComboBox.getValue());
        final Map<String, List<Solution>> result = getModel().analyse(maxMissingEdges);

        final TreeItem<String> treeRoot = new TreeItem<>("Design patterns");
        treeRoot.setExpanded(true);

        designPatternTreeView.setRoot(treeRoot);

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

    private void showFeedbackDetails(String key) {
        Solution solution = feedbackMap.get(key);
        if (solution != null) {
            // TODO
            feedbackDetailsPane.setContent(new Text("Name: " + key + " - pattern: " + solution.getDesignPatternName()));
        }
    }

}

