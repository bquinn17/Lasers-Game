package ptui;

import model.LasersModel;

import java.util.Observable;
import java.util.Observer;

/**
 * This class represents the view portion of the plain text UI.  It
 * is initialized first, followed by the controller (ControllerPTUI).
 * You should create the model here, and then implement the update method.
 *
 * @author Sean Strout @ RIT CS
 * @author Bryan Quinn
 * @author Chris Cassidy
 * */
public class LasersPTUI implements Observer {
    /** The UI's connection to the model */
    private LasersModel model;
    public ControllerPTUI controller;

    /**
     * Construct the PTUI.  Create the model and initialize the view.
     * @param filename the safe file name
     */
    public LasersPTUI(String filename) {
        //try {
            this.model = new LasersModel(filename);
        /*} catch (FileNotFoundException fnfe) {
            System.out.println(fnfe.getMessage());
            System.exit(-1);
        }*/
        this.model.addObserver(this);
        controller = new ControllerPTUI(this.model);

    }

    public LasersModel getModel() { return this.model; }

    static void printHelp(){
        System.out.println(
                "a|add r c: Add laser to (r,c) \n" +
                        "d|display: Display safe \n" +
                        "h|help: Print this help message \n" +
                        "q|quit: Exit program \n" +
                        "r|remove r c: Remove laser from (r,c) \n" +
                        "v|verify: Verify safe correctness");
    }

    @Override
    public void update(Observable o, Object arg) {
        if(arg != null){
            System.out.println(arg);

        }
        System.out.println("test");
        System.out.println(this.model);
    }
}
