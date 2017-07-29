package nl.ou.dpd.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import nl.ou.dpd.exception.DesignPatternDetectorException;
import nl.ou.dpd.gui.model.Model;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.maven.model.Contributor;
import org.apache.maven.model.Developer;
import org.apache.maven.model.Organization;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;

import java.io.FileReader;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * A {@link Controller} for the about view of the application.
 *
 * @author Martin de Boer
 */
public class AboutViewController extends Controller {

    private static final Logger LOGGER = LogManager.getLogger(AboutViewController.class);

    @FXML
    private Label applicationNameLabel;
    @FXML
    private Label applicationVersionLabel;

    @FXML
    private Label aboutDescriptionLabel;
    @FXML
    private GridPane aboutApplicationGridPane;

    @FXML
    private Label aboutContributersSubtitle;
    @FXML
    private GridPane aboutContributersGridPane;

    private String description;
    private String inceptionYear;
    private String applicationName;
    private String applicationVersion;
    private Organization organisation;
    private List<Developer> developers;
    private List<Contributor> contributors;

    /**
     * Constructs a {@link AboutViewController} with the specified {@link Model}.
     *
     * @param model the model of the MVC pattern
     */
    public AboutViewController(Model model) {
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
        this.getApplicationInformation();

        applicationNameLabel.setText(this.applicationName);
        applicationVersionLabel.setText(String.format("Version: %s (%s)", applicationVersion, inceptionYear));
        aboutDescriptionLabel.setText(tidy(description));

        showContributersInformation();
    }

    private void showContributersInformation() {
        aboutContributersSubtitle.setText("Contributors");

        int row = 0;
        for (Developer developer : developers) {
            aboutContributersGridPane.addRow(
                    row++,
                    new Text(developer.getName()),
                    new Text(":"),
                    new Text(StringUtils.join(developer.getRoles(), ", "))
            );
        }
        for (Contributor contributor : contributors) {
            aboutContributersGridPane.addRow(
                    row++,
                    new Text(contributor.getName()),
                    new Text(":"),
                    new Text(StringUtils.join(contributor.getRoles(), ", "))
            );
        }
        aboutContributersGridPane.addRow(
                row++,
                new Text("Organisation"),
                new Text(":"),
                new Text(organisation.getName()));
        aboutContributersGridPane.addRow(
                row,
                new Text(""),
                new Text(""),
                new Text(organisation.getUrl()));
    }

    private void getApplicationInformation() {
        try {
            final MavenXpp3Reader xpp3Reader = new MavenXpp3Reader();
            final FileReader fileReader = new FileReader("pom.xml");
            final org.apache.maven.model.Model mavenModel = xpp3Reader.read(fileReader);

            developers = mavenModel.getDevelopers();
            description = mavenModel.getDescription();
            contributors = mavenModel.getContributors();
            inceptionYear = mavenModel.getInceptionYear();
            organisation = mavenModel.getOrganization();
            applicationName = mavenModel.getName();
            applicationVersion = mavenModel.getVersion();
        } catch (Exception e) {
            final String msg = "Unable to open resource 'pom.xml'.";
            LOGGER.error(msg, e);
            throw new DesignPatternDetectorException(msg, e);
        }
    }

    private String tidy(String description) {
        description = description.replaceAll("\n", " ");
        while(description.contains("  ")) {
            description = description.replaceAll("  ", " ");
        }
        return description;
    }
}

