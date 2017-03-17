package nl.ou.dpd.gui.controller;

import javafx.util.Callback;
import nl.ou.dpd.gui.model.Model;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;

/**
 * Tests the {@link ControllerFactoryCreator} class.
 *
 * @author Martin de Boer
 */
@RunWith(MockitoJUnitRunner.class)
public class ControllerFactoryCreatorTest {

    @Mock
    private Model model;

    @Mock
    private Model otherModel;

    /**
     * Exception rule.
     */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Tests the {@link ControllerFactoryCreator#createControllerFactory(Model)} method.
     */
    @Test
    public void testCreateControllerFactory() {
        final Callback<Class<?>, Object> controllerFactory = ControllerFactoryCreator.createControllerFactory(model);
        final Controller controller = (ProjectViewController)controllerFactory.call(ProjectViewController.class);
        assertThat(controller.getModel(), is(model));
        assertThat(controller.getModel(), not(is(otherModel)));

        final Callback<Class<?>, Object> otherControllerFactory = ControllerFactoryCreator.createControllerFactory(otherModel);
        final Controller otherController = (ProjectViewController)otherControllerFactory.call(ProjectViewController.class);
        assertThat(otherController.getModel(), is(otherModel));
        assertThat(otherController.getModel(), not(is(model)));
    }

    /**
     * Tests the exception handling of the {@link ControllerFactoryCreator#createControllerFactory(Model)} method.
     */
    @Test
    public void testCreateControllerFactoryException() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Unable to create controller factory.");
        thrown.expectCause(is(NoSuchMethodException.class));
        ControllerFactoryCreator.createControllerFactory(model).call(String.class);
    }
}
