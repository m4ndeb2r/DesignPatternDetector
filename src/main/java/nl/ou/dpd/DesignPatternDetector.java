/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.ou.dpd;

import nl.ou.dpd.argoxmi.ArgoXMI;
import nl.ou.dpd.fourtuples.FourTupleArray;
import nl.ou.dpd.fourtuples.template.Templates;

import java.util.ArrayList;

/**
 * The main class of the Design Pattern Detector application.
 *
 * @author E.M. van Doorn
 * @author Martin de Boer
 */
public final class DesignPatternDetector {

    private String templateFileName, xmiFileName;
    private int maxMissingEdges;

    /**
     * The main method of the application. Reads the command line arguments and starts the {@link DesignPatternDetector}
     * application. Usage:
     * <pre>
     *     java -jar DesignPatternDetector [-t] [<template-file>] [-x] [<xmi-file>] [-n] [<max-missing-edges>]
     * </pre>
     * When no arguments are provided, the application will assume defaults, respectively: "templates.xml", "input.xmi",
     * and 0. If the specified (or defaulted) files do not exist, an error will occur.
     *
     * @param args the command line arguments. The arguments are optional, but come in pairs (name/value). If a name
     *             argument (flag) is provided, then a corresponding value argument is expected as well. When a name
     *             argument (flag) "-t" is provided, the following argument is presumed to be the name of the template
     *             file containing the design patterns specification. When a name argument "-x" is provided, the
     *             following argument is presumed to be the xmi-file representing the "system under consideration". And
     *             finalliy, when a name argument "-n" is provided, the following argument is presumed to be the maximum
     *             number of missing edges allowed.
     */
    public static void main(String[] args) {
        new DesignPatternDetector().run(args);
    }

    /**
     * Constructor without arguments. This constructor is package scoped for testing purposes.
     */
    DesignPatternDetector() {
        templateFileName = "templates.xml";
        xmiFileName = "input.xmi";
        maxMissingEdges = 0;
    }

    private void run(String[] args) {
        System.out.println("Current directory: " + System.getProperty("user.dir"));

        parseArgs(args);

        final ArgoXMI inputProcessor = new ArgoXMI(xmiFileName);
        final Templates templates = new Templates(templateFileName);

        final FourTupleArray fta = inputProcessor.getFourtuples();
        final ArrayList<FourTupleArray> dps = templates.parse();

        // Find a match for each design pattern in dsp
        dps.forEach(dp -> dp.match(fta, maxMissingEdges));
    }

    private void parseArgs(String[] args) {
        // Every flag should be followd by a value
        if (args.length > 6 || args.length % 2 == 1) {
            foutmeldingExit();
        }

        for (int i = 0; i < args.length; i += 2) {
            if (args[i].equals("-t")) {
                templateFileName = args[i + 1];
            } else if (args[i].equals("-x")) {
                xmiFileName = args[i + 1];
            } else if (args[i].equals("-n")) {
                maxMissingEdges = Integer.parseInt(args[i + 1]);
            } else {
                foutmeldingExit();
            }
        }
    }

    private void foutmeldingExit() {
        System.out.println("Correct is: java -t templateFile -x xmiFile -n maxNumberOfMissingEdges");
        System.out.println("Default values for templateFile and xmiFile are templates.xml, input.xmi and 0");

        System.exit(1);
    }
}
