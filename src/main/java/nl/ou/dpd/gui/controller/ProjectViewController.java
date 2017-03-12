package nl.ou.dpd.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import nl.ou.dpd.domain.Solution;
import nl.ou.dpd.gui.model.Model;

import java.net.URL;
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

    /**
     * Constructs a {@link ProjectViewController} with a reference to the specified {@link Model}.
     *
     * @param model the model of the MVC pattern
     */
    public ProjectViewController(Model model) {
        super(model);
    }

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  <tt>null</tt> if the location is not known.
     * @param resources The resources used to localize the root object, or <tt>null</tt> if
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
     *
     * TODO: store the matched classes, and edge information in an attribute to show when an item in the TreeView is clicked
     */
    @FXML
    protected void analyse() {
        final int maxMissingEdges = Integer.parseInt(this.maxMissingEdgesComboBox.getValue());
        final Map<String, List<Solution>> result = getModel().analyse(maxMissingEdges);

        designPatternTreeView.setRoot(new TreeItem<>("Design patterns"));
        final TreeItem<String> treeRoot = designPatternTreeView.getRoot();

        int patternCount = 0;
        for (String patternName : result.keySet()) {
            final List<Solution> solutions = result.get(patternName);
            final TreeItem<String> patternRoot = new TreeItem<>(patternName + " (" + solutions.size() + ")");
            treeRoot.getChildren().add(patternRoot);

            int i = 0;
            for(Solution solution : solutions) {
                final TreeItem<String> patternItem = new TreeItem<>(solution.getDesignPatternName() + (++i));
                patternItem.getChildren().add(new TreeItem("Classes"));
                if(solution.getSuperfluousEdges().size() > 0) {
                    patternItem.getChildren().add(new TreeItem("Superfluous Edges"));
                }
                if(solution.getMissingEdges().size() > 0) {
                    patternItem.getChildren().add(new TreeItem("Missing Edges"));
                }
                patternRoot.getChildren().add(patternItem);
            }

            patternCount += i;
        }

        treeRoot.setValue(treeRoot.getValue() + " (" + patternCount + ")");
    }

}

