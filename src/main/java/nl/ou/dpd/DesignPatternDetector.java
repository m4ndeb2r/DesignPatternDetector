package nl.ou.dpd;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import nl.ou.dpd.gui.model.Model;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The main class of the Design Pattern Detector application.
 *
 * @author E.M. van Doorn
 * @author Martin de Boer
 */
public final class DesignPatternDetector extends Application {

    private static final Logger LOGGER = LogManager.getLogger(DesignPatternDetector.class);

    private static final String APP_TITLE = "Design Pattern Detector";

    /**
     * The main method of the application. Starts a GUI.
     *
     * @param args these arguments are omitted
     */
    public static void main(String[] args) {
        try {
            LOGGER.info("Application DesignPatternDetector started.");
            launch(args);
        } catch (Throwable t) {
            LOGGER.error("An unexpected error occurred.", t);
        } finally {
            LOGGER.info("Application DesignPatternDetector stopped.");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(final Stage primaryStage) throws Exception {
        final Scene scene = new Scene(new StackPane());
        final Model model = new Model(scene);

        model.showMainView();

        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.setTitle(APP_TITLE);
        primaryStage.show();
    }


//    private static final String USAGE_TXT =
//            "\nUsage: \n\tjava -t templateFile -x xmiFile -n maxNumberOfMissingEdges." +
//                    "\n\tDefault values for templateFile and xmiFile are templates.xml, input.xmi and 0";
//
//
//    private String templateFileName, xmiFileName;
//    private int maxMissingEdges;
//
//    /**
//     * Constructor without arguments. This constructor is package scoped for testing purposes.
//     */
//    DesignPatternDetector() {
//        templateFileName = "templates.xml";
//        xmiFileName = "input.xmi";
//        maxMissingEdges = 0;
//    }
//
//
//    /**
//     * The main method of the application. Reads the command line arguments and starts the {@link DesignPatternDetector}
//     * application. Usage:
//     * <pre>
//     *     java -jar DesignPatternDetector [-t] [<template-file>] [-x] [<xmi-file>] [-n] [<max-missing-edges>]
//     * </pre>
//     * When no arguments are provided, the application will assume defaults, respectively: "templates.xml", "input.xmi",
//     * and 0. If the specified (or defaulted) files do not exist, an error will occur.
//     *
//     * @param args the command line arguments. The arguments are optional, but come in pairs (name/value). If a name
//     *             argument (flag) is provided, then a corresponding value argument is expected as well. When a name
//     *             argument (flag) "-t" is provided, the following argument is presumed to be the name of the template
//     *             file containing the design patterns specification. When a name argument "-x" is provided, the
//     *             following argument is presumed to be the xmi-file representing the "system under consideration". And
//     *             finalliy, when a name argument "-n" is provided, the following argument is presumed to be the maximum
//     *             number of missing edges allowed.
//     */
//    public static void main(String[] args) {
//        new DesignPatternDetector().run(args);
//    }
//
//    private void run(String[] args) {
//        try {
//            LOGGER.info("Application DesignPatternDetector started.");
//            LOGGER.debug("Current directory: " + System.getProperty("user.dir"));
//
//            // TODO: remove console output. This is still here for backbward compatibility
//            System.out.println("Current directory: " + System.getProperty("user.dir"));
//
//            // Parse the arguments
//            parseArgs(args);
//
//            // Parse the input files
//            final SystemUnderConsideration system = new ArgoXMIParser().parse(xmiFileName);
//            final List<DesignPattern> designPatterns = new TemplatesParser().parse(templateFileName);
//
//            // Find a match for each design pattern in dsp
//            final Matcher matcher = new Matcher();
//            // TODO: the show() method is called tmeporarily for backaward compatibility
//            designPatterns.forEach(pattern -> matcher.match(pattern, system, maxMissingEdges).show());
//
//        } catch (Throwable t) {
//            // Acknowledge the user of the unrecoverable error situation
//            LOGGER.error("An unexpected error occurred. Exiting...", t);
//
//            // Do not call System.ext(). It is a bad habit.
//            throw (t);
//        }
//    }
//
//    private void parseArgs(String[] args) {
//        // Every flag should be followed by a value
//        if (args.length > 6 || args.length % 2 == 1) {
//            final String msg = "Illegal number of parameters: " + args.length + ". " + USAGE_TXT;
//            LOGGER.error(msg);
//            throw new IllegalArgumentException(msg);
//        }
//
//        for (int i = 0; i < args.length; i += 2) {
//            if (args[i].equals("-t")) {
//                templateFileName = args[i + 1];
//            } else if (args[i].equals("-x")) {
//                xmiFileName = args[i + 1];
//            } else if (args[i].equals("-n")) {
//                maxMissingEdges = Integer.parseInt(args[i + 1]);
//            } else {
//                final String msg = "Incorrect parameter: " + args[i] + ". " + USAGE_TXT;
//                LOGGER.error(msg);
//                throw new IllegalArgumentException(msg);
//            }
//        }
//    }
}
