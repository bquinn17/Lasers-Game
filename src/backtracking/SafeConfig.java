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

    public boolean getIsRip(){
        return this.isRip;
    }

    private void solveFours(){
        int count = 0;
        for (int i = 1; this.pillars.get(pillars.size() - i).getNumber() == 4; i++){
            int[]coords = pillars.get(pillars.size() - i).getCoords();
            pillars.remove(pillars.size() - i);
            int row = coords[0]; int col = coords[1];
            System.out.println(coords[0] + " , "+ coords[1]);
            if(row + 1 < model.getRows() && model.getGridAtPos(row +1,col) == '.'){
                System.out.println("test");
                model.addLaser(row+1,col);
                count++;
            } if(row - 1 > 0 && model.getGridAtPos(row-1,col) == '.'){
                model.addLaser(row - 1,col);
                count++;
            } if(col - 1 > 0 && model.getGridAtPos(row,col-1) == '.'){
                model.addLaser(row,col - 1);
                count++;
            } if(col + 1 < model.getColumns() && model.getGridAtPos(row,col+1) == '.'){
                model.addLaser(row,col + 1);
                count++;
            }
            if(count < 4){
                this.isRip = true;
                break;
            }
            count = 0;
        }
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
}
