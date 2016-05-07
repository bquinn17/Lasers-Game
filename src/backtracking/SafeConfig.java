package backtracking;

import java.util.ArrayList;
import java.util.Collection;

import model.LasersModel;
/**
 * The class represents a single configuration of a safe.  It is
 * used by the backtracker to generate successors, check for
 * validity, and eventually find the goal.
 *
 * This class is given to you here, but it will undoubtedly need to
 * communicate with the model.  You are free to move it into the model
 * package and/or incorporate it into another class.
 *
 * @author Sean Strout @ RIT CS
 * @author Bryan Quinn
 * @author Chris Cassidy
 * */
public class SafeConfig implements Configuration {

    private LasersModel model;

    public SafeConfig(String filename) {
        this.model = new LasersModel(filename);
        // TODO
    }

    @Override
    public Collection<Configuration> getSuccessors() {
        // TODO
        return null;
    }

    @Override
    public boolean isValid() {
        if(!model.verify()) {
            ArrayList<Integer> coords = model.getBadCoords();
            if (model.getGridAtPos(coords.get(0), coords.get(1)) == '.') {
                // TODO
            }
        }
        return true;
    }

    @Override
    public boolean isGoal() {
        return model.verify();
    }
}
