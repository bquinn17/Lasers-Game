package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;
import java.util.Scanner;

public class LasersModel extends Observable {

    private final static char EMPTY = '.';

    private final static char PILLAR = 'X';

    private final static char BEAM = '*';

    private final static char LASER = 'L';

    private final static char HORI_DIVIDE = '-';

    private final static char VERT_DIVIDE = '|';

    private int rows;
    private int columns;

    private String message;

    private char[][] grid;
    private ArrayList<Integer> nums;
    private ArrayList<Integer> badCoords;
    private File file;

    /**
     * creates a new safe model from the given safe file
     * @param filename name of the safe file
     */
    public LasersModel(String filename) {
        File file = new File(filename);
        this.file = file;

        makeGrid(file, filename);

    }
    public void setFile(File file){this.file = file;}

    public void resetGrid(){
        makeGrid(file, "");
    }

    public void makeGrid(File file, String filename){
        try {
            Scanner in = new Scanner(file);

            int row = in.nextInt();
            int col = in.nextInt();

            in.nextLine();

            char [][] grid = new char[row][col];

            for (int i = 0; i < row; i++) {
                String[] tokens = in.nextLine().split(" ");
                for (int j = 0; j < col; j++) {
                    grid[i][j] = tokens[j].charAt(0);
                }
            }
            createGrid(row, col, grid);
            in.close();

        } catch (FileNotFoundException fnfe){
            System.out.println(filename + " (The system cannot find the file specified)");
            System.exit(2);
        }
    }

    /**
     * Constructs a new safe object with the given parameters
     * @param rows number of rows in the safe
     * @param cols number of columns in the safe
     * @param grid 2d array representing the initial layout of the safe
     */
    private void createGrid(int rows, int cols, char[][] grid){
        this.rows = rows;
        this.columns = cols;
        this.grid = grid;
        Integer [] array = new Integer[] {0,1,2,3,4};
        this.nums = new ArrayList<Integer>(Arrays.asList(array));
        array = new Integer[] {-1,-1};
        this.badCoords = new ArrayList<Integer>(Arrays.asList(array));
    }

    /**
     * Tries to add a laser. If it can it will add it and its beams, but if it can't it will say error adding laser at
     * that coordinate. Also will check to see if the coordinates are valid.
     * @param row number of rows in the safe
     * @param col number of columns in the safe
     */
    public void addLaser(int row, int col){
        if (row >= rows || row < 0 || col >= columns || col < 0){
            message = "Error adding laser at: ("+ row +", "+ col +")";
            announceChange();
            return;
        }
        if(grid[row][col] == EMPTY || grid[row][col] == BEAM){
            grid[row][col] = LASER;
            int bRow = row;
            int bCol = col;
            while(++bRow < rows && (grid[bRow][bCol] == EMPTY || grid[bRow][bCol] == BEAM)){
                grid[bRow][bCol] = BEAM;
            }
            bRow = row;
            bCol = col;
            while(++bCol < columns && (grid[bRow][bCol] == EMPTY || grid[bRow][bCol] == BEAM)){
                grid[bRow][bCol] = BEAM;
            }
            bRow = row;
            bCol = col;
            while(--bRow >= 0 && (grid[bRow][bCol] == EMPTY || grid[bRow][bCol] == BEAM)){
                grid[bRow][bCol] = BEAM;
            }
            bRow = row;
            bCol = col;
            while(--bCol >= 0 && (grid[bRow][bCol] == EMPTY || grid[bRow][bCol] == BEAM)){
                grid[bRow][bCol] = BEAM;
            }
            message = "Laser added at: ("+ row +", "+ col +")";
            announceChange();

        }
        else{
            message = "Error adding laser at: ("+ row +", "+ col +")";
            announceChange();
        }
    }

    /**
     * Tries to remove a laser. If it can it will remove it and all of its beams, but if it can't it will say error
     * removing laser at that coordinate. Also will check to see if the coordinates are valid. Lastly calls fix lasers.
     * @param row number of rows in the safe
     * @param col number of columns in the safe
     */
    public void removeLaser(int row, int col){
        if (row >= rows || row < 0 || col >= columns || col < 0){
            message = "Error removing laser at: ("+ row +", "+ col +")";
            announceChange();
            return;
        }

        if(grid[row][col] == LASER){
            grid[row][col] = EMPTY;
            int bRow = row;
            int bCol = col;
            while(++bRow < rows && grid[bRow][bCol] == BEAM){
                grid[bRow][bCol] = EMPTY;
            }
            bRow = row;
            bCol = col;
            while(++bCol < columns && grid[bRow][bCol] == BEAM){
                grid[bRow][bCol] = EMPTY;
            }
            bRow = row;
            bCol = col;
            while(--bRow >= 0 && grid[bRow][bCol] == BEAM){
                grid[bRow][bCol] = EMPTY;
            }
            bRow = row;
            bCol = col;
            while(--bCol >= 0 && grid[bRow][bCol] == BEAM){
                grid[bRow][bCol] = EMPTY;
            }
            fixLasers();
            message = "Laser removed at: ("+ row +", "+ col +")";
            announceChange();

        }
        else{
            message = "Error removing laser at: ("+ row +", "+ col +")";
            announceChange();
        }
    }

    /**
     * Makes sure all laser beams are correct.
     */
    private void fixLasers(){
        for (int j = 0; j < rows; j++) {
            for (int i = 0; i < columns; i++) {
                if (grid[j][i] == LASER) {
                    int bRow = j;
                    int bCol = i;
                    while(++bRow < rows){
                        if (grid[bRow][bCol] == EMPTY){
                            grid[bRow][bCol] = BEAM;
                        } else if (grid[bRow][bCol] != BEAM) {
                            break;
                        }
                    }
                    bRow = j;
                    bCol = i;
                    while(++bCol < columns){
                        if (grid[bRow][bCol] == EMPTY){
                            grid[bRow][bCol] = BEAM;
                        } else if (grid[bRow][bCol] != BEAM) {
                            break;
                        }
                    }
                    bRow = j;
                    bCol = i;
                    while(--bRow >= 0){
                        if (grid[bRow][bCol] == EMPTY){
                            grid[bRow][bCol] = BEAM;
                        } else if (grid[bRow][bCol] != BEAM) {
                            break;
                        }
                    }
                    bRow = j;
                    bCol = i;
                    while(--bCol >= 0){
                        if (grid[bRow][bCol] == EMPTY){
                            grid[bRow][bCol] = BEAM;
                        } else if (grid[bRow][bCol] != BEAM) {
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * A utility method that indicates the model has changed and
     * notifies observers
     */
    private void announceChange() {
        setChanged();
        notifyObservers();
    }

    @Override
    public String toString() {
        String str = "  ";
        for (int i = 0; i < (columns); i++) {
            str += i + " ";
        }
        str += "\n  ";
        for (int i = 0; i < (columns +(columns -1)); i++) {
            str += HORI_DIVIDE;
        }
        for (int j = 0; j < rows; j++) {
            str += "\n" + j + VERT_DIVIDE;
            for (int i = 0; i < columns; i++) {
                str += grid[j][i] + " ";
            }
            str = str.substring(0, str.length()-1);
        }
        return str;
    }

    /**
     * The verify command displays a status message that indicates whether the safe is valid or not. In order to be
     * valid, none of the rules of the safe may be violated. Each tile that is not a pillar must have either a laser or
     * beam covering it. Each pillar that requires a certain number of neighboring lasers must add up exactly. If two
     * or more lasers are in sight of each other, in the cardinal directions, it is invalid.
     * @return whether it is valid or not
     */
    public boolean verify(){
        int rowLasers = 0;
        ArrayList<Integer> colLasers= new ArrayList<>();
        for (int i = 0; i < columns; i++) {
            colLasers.add(i,0);
        }

        for (int j = 0; j < rows; j++) {
            for (int i = 0; i < columns; i++) {
                if (grid[j][i] == EMPTY) {
                    badLasers(j,i);
                    return false;
                } else if (is_pillar(grid[j][i])) {
                    colLasers.set(i, 0);
                    rowLasers = 0;
                    if (!check_pillar(grid[j][i], j, i)) {
                        badLasers(j,i);
                        return false;
                    }
                } else if (grid[j][i] == LASER) {
                    if (colLasers.get(i) == 1) {
                        badLasers(j,i);
                        return false;
                    }
                    colLasers.set(i, 1);
                    rowLasers++;
                    if (rowLasers > 1) {
                        badLasers(j,i);
                        return false;
                    }
                } else if (grid[j][i] == PILLAR) {
                    rowLasers = 0;
                    colLasers.set(i, 0);
                }
            }
            rowLasers = 0;
        }
        badCoords.set(0,-1);
        badCoords.set(1,-1);
        return true;
    }

    private void badLasers(int j, int i){
        message = "Error verifying at : (" + j + " , " + i + ")";
        badCoords.set(0,j);
        badCoords.set(1,i);
        announceChange();
    }

    /**
     * checks to see if the position is a number, meaning that it is a pillar
     * @param ch the character a certain position
     * @return whether or not it is a pillar
     */
    private boolean is_pillar(char ch){
        String str = String.valueOf(ch);
        try {
            nums.contains(Integer.parseInt(str));
        } catch (NumberFormatException ex){
            return false;
        }
        return true;
    }


    /**
     * Sees if each Pillar has the correct amount of lasers
     * @param ch number of lasers it should have
     * @param j row of pillar to check
     * @param i column of pillar to check
     * @return wheather it is correct or not.
     */
    private boolean check_pillar(char ch, int j, int i){
        int num = Integer.parseInt(String.valueOf(ch));
        int count = 0;
        if(j + 1 < rows && grid[j+1][i] == LASER){
            count++;
        }
        if(j - 1 >= 0 && grid[j-1][i] == LASER){
            count++;
        }
        if(i + 1 < columns && grid[j][i+1] == LASER){
            count++;
        }
        if(i - 1 >= 0 &&grid[j][i-1] == LASER){
            count++;
        }
        return count == num;
    }

    /**
     * Allows access to columns
     * @return columns
     */
    public int getColumns() {
        return columns;
    }

    /**
     * Allows access to rows
     * @return rows
     */
    public int getRows() {
        return rows;
    }

    public ArrayList<Integer> getBadCoords(){
        return this.badCoords;
    }

    public String getMessage() {
        return message;
    }

    public char getGridAtPos(int row, int col) {
        return grid[col][row];
    }

    public char getGridAtPosFlipped(int row, int col) {
        return grid[row][col];
    }

    public char[][] getGrid(){return this.grid;}
}
