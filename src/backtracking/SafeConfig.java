package backtracking;

import model.LasersModel;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

    public SafeConfig(String filename) {
        this.model = new LasersModel(filename);
        this.grid = model.getGrid();
        pillars = new ArrayList<>();
        getPillars();
        this.pillars.sort((pillar1, pillar2) -> pillar1.getNumber() - pillar2.getNumber());
        this.isRip = false;
        solveFours();
        this.grid = model.getGrid();
        System.out.println(model);
    }

    private SafeConfig(SafeConfig config){
        this.model = new LasersModel(config.model);
        this.grid = this.model.getGrid();
        this.pillars = config.pillars;
        this.isRip = config.isRip;
    }

    @Override
    public Collection<Configuration> getSuccessors() {
        ArrayList<Configuration> successors = new ArrayList<>();
        if(pillars.get(pillars.size()-1).getNumber() == 0){
            Pillar pillar = pillars.get(pillars.size() - 1);
            int count = countAround(pillar);
            if (count < pillar.getNumber()){
                return successors; //not enough spots for lasers, invalid successor
            } else if (count == pillar.getNumber()){ //fill the rest in with lasers
                fillLasers(pillar);
                successors.add(this);
                return successors;
            } else { //when count is greater than the number on the pillar
                ArrayList<Configuration> children = generateChildren(pillar);
                successors.addAll(children);
                return successors; //all possibilities of laser placement for given pillar
            }
        } else {
            model.verify();
            int row = model.getBadCoords().get(0);
            int col = model.getBadCoords().get(1);
            if(model.getGridAtPos(row,col) != '.'){ //If verify does not break on white space, this config is invalid
                return successors;
            }
            //If the white space is next to a numbered pillar, this that pillar is already full
            if (row + 1 < model.getRows() && model.is_pillar(model.getGridAtPos(row + 1, col))) {
                successors.add(this);
                return successors;
            }
            if (row - 1 > 0 && model.is_pillar(model.getGridAtPos(row + 1, col))) {
                successors.add(this);
                return successors;
            }
            if (col + 1 < model.getColumns() && model.is_pillar(model.getGridAtPos(row + 1, col))) {
                successors.add(this);
                return successors;
            }
            if (col - 1 > 0 && model.is_pillar(model.getGridAtPos(row + 1, col))) {
                successors.add(this);
                return successors;
            }
            SafeConfig kid = new SafeConfig(this);
            successors.add(kid);
            successors.add(this);
            return successors;
        }
    }

    private ArrayList<Configuration> generateChildren(Pillar pillar) {
        ArrayList<int[]> canAdd = new ArrayList<>();
        //A list of all of the coordinates that a laser can be added
        //this could be up, down, left, or right
        int[]coords = pillar.getCoords();
        int row = coords[0]; int col = coords[1];
        if(row + 1 < model.getRows() && model.getGridAtPos(row +1,col) == '.'){
            int[] good = {row+1,col};
            canAdd.add(good);
        }
        if(row - 1 > 0 && model.getGridAtPos(row -1,col) == '.'){
            int[] good = {row+1,col};
            canAdd.add(good);
        }
        if(col + 1 < model.getColumns() && model.getGridAtPos(row ,col+1) == '.'){
            int[] good = {row+1,col};
            canAdd.add(good);
        }
        if(col - 1 > 0 && model.getGridAtPos(row ,col-1) == '.'){
            int[] good = {row+1,col};
            canAdd.add(good);
        }

        ArrayList<Configuration> kids = new ArrayList<>(); //full list of successors
        int number = pillar.getNumber();
        while (canAdd.size() > number){ //adds n number of lasers each time, then removes the front position
            SafeConfig kid = new SafeConfig(this); //create a new configuration using the copy constructor
            int i = 0;
            while (i < number){ //puts a laser in the first n positions
                kid.model.addLaser(canAdd.get(i)[0],canAdd.get(i)[1]);
                i++;
            }
            canAdd.remove(0); //removes the first coordinate in order to step forward one in the coordinate list
            kids.add(kid); //adds to the list of successors
        }
        return kids;
    }

    private int countAround(Pillar pillar) {
        int[]coords = pillar.getCoords();
        int row = coords[0]; int col = coords[1];
        int count = 0;
        if(row + 1 < model.getRows() && model.getGridAtPos(row +1,col) == '.'){ count++;}
        if(row - 1 > 0 && model.getGridAtPos(row -1,col) == '.'){ count++;}
        if(col + 1 < model.getColumns() && model.getGridAtPos(row ,col+1) == '.'){ count++;}
        if(col - 1 > 0 && model.getGridAtPos(row ,col-1) == '.'){ count++;}
        return count;
    }

    private int fillLasers(Pillar pillar){
        int count = pillar.getNumber();
        int[]coords = pillars.get(pillars.size() - 1).getCoords();
        pillars.remove(pillars.size() - 1);
        int row = coords[0]; int col = coords[1];
        if (count > 0 && model.addLaser(row+1,col)) { count--; }
        if (count > 0 && model.addLaser(row-1,col)) { count--; }
        if (count > 0 && model.addLaser(row,col-1)) { count--; }
        if (count > 0 && model.addLaser(row,col+1)) { count--; }
        if(count > 0){ this.isRip = true; }
        return count;
    }

    private void solveFours(){
        int count = 0;
        while (this.pillars.get(pillars.size() - 1).getNumber() == 4){
            int[]coords = pillars.get(pillars.size() - 1).getCoords();
            pillars.remove(pillars.size() - 1);
            int row = coords[0]; int col = coords[1];
            if(model.addLaser(row+1,col)) { count++; }
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
        if(!model.verify() && pillars.size() > 0) {
            for (int i = 0; pillars.get(i).getNumber() == 0; i++) {
                int[]coords = pillars.get(pillars.size() - i).getCoords();
                int row = coords[0]; int col = coords[1];
                if(row + 1 < model.getRows() && model.getGridAtPos(row +1,col) == 'L'){
                    return false;
                } if(row - 1 > 0 && model.getGridAtPos(row-1,col) == 'L'){
                    return false;
                } if(col - 1 > 0 && model.getGridAtPos(row,col-1) == 'L'){
                    return false;
                } if(col + 1 < model.getColumns() && model.getGridAtPos(row,col+1) == 'L'){
                    return false;
                }
            }
            // TODO
        }
        return true;
    }

    @Override
    public boolean isGoal() {
        return model.verify();
    }

    public boolean getIsRip(){
        return this.isRip;
    }

    public char[][] getGrid(){
        return model.getGrid();
    }


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