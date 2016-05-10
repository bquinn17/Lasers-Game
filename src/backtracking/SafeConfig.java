package backtracking;

import model.LasersModel;
import java.util.ArrayList;
import java.util.Collection;

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
    private char[][] grid;
    private ArrayList<Pillar> pillars;
    private boolean isRip;
    private int currRow;
    private int currCol;

    /**
     * Initializes all the variables for the backtracking
     * @param filename the file that it is reading in from the command line.
     */
    public SafeConfig(String filename) {
        this.model = new LasersModel(filename);
        this.grid = model.getGrid();
        pillars = new ArrayList<>();
        getPillars();
        this.pillars.sort((pillar1, pillar2) -> pillar1.getNumber() - pillar2.getNumber());
        this.isRip = false;
        this.grid = model.getGrid();
        currCol = 0;
        currRow = 0;
    }

    /**
     * Initializes all the variables for the current configuration. Copy constructor.
     * @param config the current configuration of the safe
     */
    private SafeConfig(SafeConfig config){
        this.model = new LasersModel(config.model);
        this.grid = this.model.getGrid();
        this.pillars = new ArrayList<>(config.pillars);
        this.isRip = config.isRip;
        this.currCol = config.currCol;
        this.currRow = config.currRow;
    }

    @Override
    public Collection<Configuration> getSuccessors() {
        //Step 1: Fill in all the lasers for the 4 pillars
        //Step 2: Fill around the rest of the pillars, generating multiple
            //successors when necessary
        //Step 3: Fill in the rest of the empty positions with lasers using
            //a naive approach
        if(pillars.get(pillars.size()-1).getNumber() == 4) {solveFours();}
        ArrayList<Configuration> successors = new ArrayList<>();
        if(pillars.size() != 0 && pillars.get(pillars.size()-1).getNumber() != 0){ //if there are still non 0 pillars
            //left in the list of pillars...
            Pillar pillar = pillars.get(pillars.size() - 1);
            pillars.remove(pillars.size()-1);
            int count = countAround(pillar);
            if (count < pillar.getNumber()){
                return successors; //not enough spots for lasers, invalid successor
            } else if (count == pillar.getNumber()){ //fill the rest in with lasers
                fillLasers(pillar);
                successors.add(this);
                return successors;
            } else { //when count is greater than the number on the pillar
                ArrayList<Configuration> children = generateChildren(pillar);
                System.out.println("Children: ");
                children.forEach(System.out::println);
                successors.addAll(children);
                return successors; //all possibilities of laser placement for given pillar
            }
        } else { //begin naive approach
            while (model.getGridAtPos(currRow,currRow) != '.'){ //step forward until the next open spot
                stepForward();
            }
            //If the white space is next to a numbered pillar, this that pillar is already full
            if (currRow + 1 < model.getRows() && model.is_pillar(model.getGridAtPos(currRow + 1, currRow))) {
                successors.add(this);
                return successors;
            }
            if (currRow - 1 > 0 && model.is_pillar(model.getGridAtPos(currRow + 1, currRow))) {
                successors.add(this);
                return successors;
            }
            if (currRow + 1 < model.getColumns() && model.is_pillar(model.getGridAtPos(currRow + 1, currRow))) {
                successors.add(this);
                return successors;
            }
            if (currRow - 1 > 0 && model.is_pillar(model.getGridAtPos(currRow + 1, currRow))) {
                successors.add(this);
                return successors;
            }
            //return two children, one with a laser added in the spot, one without
            SafeConfig kid = new SafeConfig(this);
            successors.add(kid);
            successors.add(this);
            stepForward(); //move forward one spot
            return successors;
        }
    }

    /**
     * move to the next position in the safe.
     */
    private void stepForward(){
        this.currRow++;
        if(this.currRow == model.getRows()){
            currRow = 0;
            currCol++;
        }
    }

    /**
     * Generates all possible configurations of placing lasers around the piller.
     * @param pillar the pillar that it is looking for configurations
     * @return a list of possible children
     */
    private ArrayList<Configuration> generateChildren(Pillar pillar) {
        ArrayList<int[]> canAdd = new ArrayList<>();
        //A list of all of the coordinates that a laser can be added
        //this could be up, down, left, or right
        if(pillars.size() == 1){
            boolean stop = true;
        }
        int[]coords = pillar.getCoords();
        int row = coords[0]; int col = coords[1];
        if(row + 1 < model.getRows() && model.getGridAtPos(row +1,col) == '.'){
            int[] good = {row+1,col};
            canAdd.add(good);
        }
        if(row - 1 >= 0 && model.getGridAtPos(row -1,col) == '.'){
            int[] good = {row-1,col};
            canAdd.add(good);
        }
        if(col + 1 < model.getColumns() && model.getGridAtPos(row ,col+1) == '.'){
            int[] good = {row,col+1};
            canAdd.add(good);
        }
        if(col - 1 >= 0 && model.getGridAtPos(row ,col-1) == '.'){
            int[] good = {row,col-1};
            canAdd.add(good);
        }
        System.out.println("Can Add: " + canAdd.size());

        int i;
        int index = 0;
        int number = pillar.getNumber();
        ArrayList<Configuration> kids = new ArrayList<>(); //full list of successors
        if(number == 2){ //if the number is two start by making lasers on every other spot
            while(index+2 < canAdd.size()){
                SafeConfig kid = new SafeConfig(this); //create a new configuration using the copy constructor
                i = index;
                while (i < canAdd.size()){ //puts a laser in the first n positions
                    kid.model.addLaser(canAdd.get(i)[0],canAdd.get(i)[1]);
                    i+=2;
                }
                index++;
                kids.add(kid);
            }
        }
        index = 0;
        while (index < canAdd.size()){ //adds n number of lasers each time, then removes the front position
            SafeConfig kid = new SafeConfig(this); //create a new configuration using the copy constructor
            i = index;
            int endIndex = (number + index) % canAdd.size(); //can loop around to the beginning
            while (i != endIndex){ //puts a laser in the next n positions
                kid.model.addLaser(canAdd.get(i)[0],canAdd.get(i)[1]);
                i++;
                if(i == canAdd.size()) i = 0;
            }
            index++; //index moves one step forward
            kids.add(kid); //adds to the list of successors
        }
        System.out.println("Kids: " + kids.size());
        return kids;
    }

    /**
     * looks for every possible position of the laser around the laser.
     * @param pillar the piller it is checking around
     * @return how many possible locations there are
     */
    private int countAround(Pillar pillar) {
        int[]coords = pillar.getCoords();
        int row = coords[0]; int col = coords[1];
        int count = 0;
        if(row + 1 < model.getRows() && model.getGridAtPos(row +1,col) == '.'){ count++;}
        if(row - 1 >= 0 && model.getGridAtPos(row -1,col) == '.'){ count++;}
        if(col + 1 < model.getColumns() && model.getGridAtPos(row ,col+1) == '.'){ count++;}
        if(col - 1 >= 0 && model.getGridAtPos(row ,col-1) == '.'){ count++;}
        return count;
    }

    /**
     * puts lasers around the pillar
     * @param pillar the pillar to put lasers around
     * @return teh amount of laser it placed
     */
    private int fillLasers(Pillar pillar){
        int count = pillar.getNumber();
        int[]coords = pillars.get(pillars.size() - 1).getCoords();
        pillars.remove(pillars.size() - 1);
        int row = coords[0]; int col = coords[1];
        if (count > 0 && model.getGridAtPos(row,col) != '*' && model.addLaser(row+1,col)) { count--; }
        if (count > 0 && model.getGridAtPos(row,col) != '*' && model.addLaser(row-1,col)) { count--; }
        if (count > 0 && model.getGridAtPos(row,col) != '*' && model.addLaser(row,col-1)) { count--; }
        if (count > 0 && model.getGridAtPos(row,col) != '*' && model.addLaser(row,col+1)) { count--; }
        if(count > 0){ this.isRip = true; }
        return count;
    }

    /**
     * puts lasers around the pillars that require four lasers.
     */
    private void solveFours(){
        int count = 0;
        while (this.pillars.get(pillars.size() - 1).getNumber() == 4){
            int[]coords = pillars.get(pillars.size() - 1).getCoords();
            pillars.remove(pillars.size() - 1);
            int row = coords[0]; int col = coords[1];
            if (model.addLaser(row+1,col)) { count++; }
            if (model.addLaser(row-1,col)) { count++; }
            if (model.addLaser(row,col-1)) { count++; }
            if (model.addLaser(row,col+1)) { count++; }
            if(count < 4){
                this.isRip = true;
                break;
            }
            count = 0;
        }
    }

    @Override
    public boolean isValid() {
        boolean finished = model.verify();
        if (currCol == model.getColumns() && currRow == model.getRows()){
            return finished; //if we are at the last position, then the board is either the goal or wrong
        }

        if(pillars.get(pillars.size()-1).getNumber() == 0 && !finished) {
            for (int i = 0; i < pillars.size() && pillars.get(i).getNumber() == 0; i++) {
                int[]coords = pillars.get(i).getCoords();
                int row = coords[0]; int col = coords[1];
                if(row + 1 < model.getRows() && model.getGridAtPos(row +1,col) == 'L'){
                    return false;
                } if(row - 1 >= 0 && model.getGridAtPos(row-1,col) == 'L'){
                    return false;
                } if(col - 1 >= 0 && model.getGridAtPos(row,col-1) == 'L'){
                    return false;
                } if(col + 1 < model.getColumns() && model.getGridAtPos(row,col+1) == 'L'){
                    return false;
                }
            }
            // TODO ??
        }
        return true;
    }

    @Override
    public boolean isGoal() {
        return model.verify();
    }

    @Override
    public boolean getIsRip(){
        return this.isRip;
    }

    /**
     * returns the current grid.
     */
    public char[][] getGrid(){
        return model.getGrid();
    }


    /**
     * find all pillars in a grid.
     */
    private void getPillars(){
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (model.is_pillar(grid[i][j])){
                    int number = Integer.parseInt(String.valueOf(grid[i][j]));
                    Pillar pillar = new Pillar(i,j,number);
                    this.pillars.add(pillar);
                }
            }

        }
    }

    @Override
    public String toString(){
        return model.toString();
    }
}