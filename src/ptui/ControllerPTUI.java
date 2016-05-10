package ptui;

import model.LasersModel;

import java.io.*;
import java.util.Scanner;

/**
 * This class represents the controller portion of the plain text UI.
 * It takes the model from the view (LasersPTUI) so that it can perform
 * the operations that are input in the run method.
 *
 * @author Sean Strout @ RIT CS
 * @author Bryan Quinn
 * @author Chris Cassidy
 */
public class ControllerPTUI {
    /**
     * The UI's connection to the model
     */
    private LasersModel model;

    /**
     * Construct the PTUI.  Create the model and initialize the view.
     *
     * @param model The laser model
     */
    public ControllerPTUI(LasersModel model) {
        this.model = model;
    }

    /**
     * Run the main loop.  This is the entry point for the controller
     *
     * @param inputFile The name of the input command file, if specified
     */
    public void run(String inputFile) {
        if (inputFile != null) {
            try {
                Scanner in = new Scanner(new File(inputFile));
                while (in.hasNextLine()) {
                    String[] str = in.nextLine().split(" ");
                    System.out.print(">");
                    for (int i = 0; i < str.length; i++) {
                        System.out.print(" " + str[i]);
                    }
                    System.out.println("");
                    handle_command(str);
                }
            } catch (FileNotFoundException ex) {
                System.out.println(inputFile + " (The system cannot find the file specified)");
            }
        }
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            boolean go = true;
            while (go) {
                System.out.print("> ");
                String input = br.readLine();
                input = input.replaceAll("^\\s+", "");
                input = input.replaceAll("\\s+$", "");
                String[] str = input.split(" ");
                go = handle_command(str);
            }
            System.out.println("");
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    /**
     * Handels all the input and directs them to the correct method to be called.
     *
     * @param str teh input of the user
     * @return will quit the program.
     */
    private boolean handle_command(String[] str) {
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
                    LasersPTUI.printHelp();
                    break;
                case 'q':
                    return false;
                case 'r':
                    if (str.length != 3) {
                        System.out.println("Incorrect coordinates");
                    } else {
                        model.removeLaser(Integer.parseInt(str[1]), Integer.parseInt(str[2]));
                        model.notifyObservers();
                        System.out.println(this.model);
                    }
                    break;
                case 'v':
                    if (model.verify()) {
                        System.out.println("Safe is fully verified!");
                        model.notifyObservers();
                        System.out.println(this.model);
                    } else {
                        model.notifyObservers();
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
