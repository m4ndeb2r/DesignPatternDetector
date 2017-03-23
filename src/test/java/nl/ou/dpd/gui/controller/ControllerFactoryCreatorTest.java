package nl.ou.dpd.gui.controller;

import javafx.util.Callback;
import nl.ou.dpd.gui.model.Model;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.TestCase.assertFalse;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertTrue;

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
     * Tests the {@link ControllerFactoryCreator#createControllerFactory(Model)} method for the
     * {@link ProjectViewController}.
     */
    @Test
    public void testCreateProjectViewControllerFactory() {
        final Callback<Class<?>, Object> controllerFactory = ControllerFactoryCreator.createControllerFactory(model);
        final Controller controller = (ProjectViewController) controllerFactory.call(ProjectViewController.class);
        assertThat(controller.getModel(), is(model));
        assertThat(controller.getModel(), not(is(otherModel)));

        // The factory always returns the same controller instance
        final Controller controller2 = (ProjectViewController) controllerFactory.call(ProjectViewController.class);
        assertTrue(controller == controller2);

        // Any factory always returns the same controller instance when the same model is provided
        final Callback<Class<?>, Object> controllerFactory2 = ControllerFactoryCreator.createControllerFactory(model);
        final Controller controller3 = (ProjectViewController) controllerFactory2.call(ProjectViewController.class);
        assertTrue(controller2== controller3);

        // A factory for another model always returns a new controller instance (because a different model is provided)
        final Callback<Class<?>, Object> controllerFactory3 = ControllerFactoryCreator.createControllerFactory(otherModel);
        final Controller controller4 = (ProjectViewController) controllerFactory3.call(ProjectViewController.class);
        assertFalse(controller3 == controller4);
        assertThat(controller4.getModel(), is(otherModel));
        assertThat(controller4.getModel(), not(is(model)));
    }

    /**
     * Tests the {@link ControllerFactoryCreator#createControllerFactory(Model)} method for the
     * {@link MainViewController}.
     */
    @Test
    public void testCreateMainViewControllerFactory() {
        final Callback<Class<?>, Object> controllerFactory = ControllerFactoryCreator.createControllerFactory(model);
        final Controller controller = (MainViewController) controllerFactory.call(MainViewController.class);
        assertThat(controller.getModel(), is(model));
        assertThat(controller.getModel(), not(is(otherModel)));

        // The factory always returns the same controller instance
        final Controller controller2 = (MainViewController) controllerFactory.call(MainViewController.class);
        assertTrue(controller == controller2);

        // Any factory always returns the same controller instance when the same model is provided
        final Callback<Class<?>, Object> controllerFactory2 = ControllerFactoryCreator.createControllerFactory(model);
        final Controller controller3 = (MainViewController) controllerFactory2.call(MainViewController.class);
        assertTrue(controller2== controller3);

        // A factory for another model always returns a new controller instance (because a different model is provided)
        final Callback<Class<?>, Object> controllerFactory3 = ControllerFactoryCreator.createControllerFactory(otherModel);
        final Controller controller4 = (MainViewController) controllerFactory3.call(MainViewController.class);
        assertFalse(controller3 == controller4);
        assertThat(controller4.getModel(), is(otherModel));
        assertThat(controller4.getModel(), not(is(model)));
    }

    /**
     * Tests the {@link ControllerFactoryCreator#createControllerFactory(Model)} method for the
     * {@link MenuController}.
     */
    @Test
    public void testCreateMenuControllerControllerFactory() {
        final Callback<Class<?>, Object> controllerFactory = ControllerFactoryCreator.createControllerFactory(model);
        final Controller controller = (MenuController) controllerFactory.call(MenuController.class);
        assertThat(controller.getModel(), is(model));
        assertThat(controller.getModel(), not(is(otherModel)));

        // The factory always returns the same controller instance
        final Controller controller2 = (MenuController) controllerFactory.call(MenuController.class);
        assertTrue(controller == controller2);

        // Any factory always returns the same controller instance when the same model is provided
        final Callback<Class<?>, Object> controllerFactory2 = ControllerFactoryCreator.createControllerFactory(model);
        final Controller controller3 = (MenuController) controllerFactory2.call(MenuController.class);
        assertTrue(controller2== controller3);

        // A factory for another model always returns a new controller instance (because a different model is provided)
        final Callback<Class<?>, Object> controllerFactory3 = ControllerFactoryCreator.createControllerFactory(otherModel);
        final Controller controller4 = (MenuController) controllerFactory3.call(MenuController.class);
        assertFalse(controller3 == controller4);
        assertThat(controller4.getModel(), is(otherModel));
        assertThat(controller4.getModel(), not(is(model)));
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
