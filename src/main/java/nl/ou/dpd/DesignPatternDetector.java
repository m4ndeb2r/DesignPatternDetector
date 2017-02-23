/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.ou.dpd;

import nl.ou.dpd.argoxmi.ArgoXMI;
import nl.ou.dpd.fourtuples.DetectPatterns;
import nl.ou.dpd.fourtuples.template.Templates;

/**
 *
 * @author E.M. van Doorn
 */
public class DesignPatternDetector {

    private String templateFileName, xmiFileName;
    private int maxMissingEdges;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        DesignPatternDetector prg;

        prg = new DesignPatternDetector();
        prg.run(args);
    }

    DesignPatternDetector() {
        templateFileName = "templates.xml";
        xmiFileName = "input.xmi";
        maxMissingEdges = 0;
    }

    private void run(String[] args) {
        ArgoXMI inputProcessor;
        Templates templates;
        DetectPatterns detector;

        parseArgs(args);

        System.out.println("Current directory: " + System.getProperty("user.dir"));

        inputProcessor = new ArgoXMI(xmiFileName);
        templates = new Templates(templateFileName);

        detector = new DetectPatterns();
        detector.detectDP(inputProcessor.getFourtuples(),
                templates.parse(), maxMissingEdges);

    }

    public void parseArgs(String[] args) {
        if (args.length > 6 || args.length % 2 == 1)
            // Every flag should be followd by a value
        {
            foutmeldingExit();
        }

        for (int i = 0; i < args.length; i += 2)
        {
            if (args[i].equals("-t"))
            {
                templateFileName = args[i + 1];
            } else if (args[i].equals("-x"))
            {
                xmiFileName = args[i + 1];
            } else if (args[i].equals("-n"))
            {
                maxMissingEdges = Integer.parseInt(args[i + 1]);
            } else
            {
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
