package ptui;

import model.LasersModel;

import java.io.FileNotFoundException;
import java.util.Observable;
import java.util.Observer;

/**
 * This class represents the view portion of the plain text UI.  It
 * is initialized first, followed by the controller (ControllerPTUI).
 * You should create the model here, and then implement the update method.
 *
 * @author Sean Strout @ RIT CS
 * @author YOUR NAME HERE
 */
public class LasersPTUI implements Observer {
    /** The UI's connection to the model */
    private LasersModel model;

    /**
     * Construct the PTUI.  Create the model and initialize the view.
     * @param filename the safe file name
     * @throws FileNotFoundException if file not found
     */
    public LasersPTUI(String filename) throws FileNotFoundException {
        //try {
            this.model = new LasersModel(filename);
        /*} catch (FileNotFoundException fnfe) {
            System.out.println(fnfe.getMessage());
            System.exit(-1);
        }*/
        this.model.addObserver(this);
    }

    public LasersModel getModel() { return this.model; }

    void printHelp(){
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

        String[] str = (String[]) arg;

        if (str.length > 0 && str[0].length() > 0) {
            switch (str[0].charAt(0)) {
                case 'a':
                    if (str.length != 3) {
                        System.out.println("Incorrect coordinates");
                    } else {
                        model.addLaser(Integer.parseInt(str[1]), Integer.parseInt(str[2]));
                        System.out.println(this.model);
                    }
                    break;
                case 'd':
                    System.out.println(this.model);
                    break;
                case 'h':
                    printHelp();
                    break;
                case 'q':
                    return false;
                case 'r':
                    if (str.length != 3) {
                        System.out.println("Incorrect coordinates");
                    } else {
                        model.removeLaser(Integer.parseInt(str[1]), Integer.parseInt(str[2]));
                        System.out.println(this.model);
                    }
                    break;
                case 'v':
                    if (model.verify()) {
                        System.out.println("Safe is fully verified!");
                        System.out.println(this.model);
                    } else {
                        System.out.println(this.model);
                    }
                    break;
                default:
                    System.out.println("Unrecognized command: " + str[0]);
            }
        }
        return true;
    }
}
