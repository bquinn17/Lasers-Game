package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;
import java.util.Scanner;

public class LasersModel extends Observable {

    private final static char EMPTY = '.';

    public final static char PILLAR = 'X';

    private final static char BEAM = '*';

    private final static char LASER = 'L';

    private final static char HORI_DIVIDE = '-';

    private final static char VERT_DIVIDE = '|';

    private int rows;
    private int columns;
    private char[][] grid;
    private ArrayList<Integer> nums;

    /**
     * creates a new safe model from the given safe file
     * @param filename name of the safe file
     */
    public LasersModel(String filename)  {
        try {
            File file = new File(filename);
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
            announceChange();

        } catch (FileNotFoundException fnfe){
            System.out.println(filename + " (The system cannot find the file specified)");
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
    }

    /**
     * Tries to add a laser. If it can it will add it and its beams, but if it can't it will say error adding laser at
     * that coordinate. Also will check to see if the coordinates are valid.
     * @param row number of rows in the safe
     * @param col number of columns in the safe
     */
    void addLaser(int row, int col){
        if (row >= rows || row < 0){
            System.out.println("Error adding laser at: ("+ row +", "+ col +")");
            return;
        }
        if (col >= columns || col < 0){
            System.out.println("Error adding laser at: ("+ row +", "+ col +")");
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
            System.out.println("Laser added at: ("+ row +", "+ col +")");

        }
        else{
            System.out.println("Error adding laser at: ("+ row +", "+ col +")");
        }
    }

    /**
     * Tries to remove a laser. If it can it will remove it and all of its beams, but if it can't it will say error
     * removing laser at that coordinate. Also will check to see if the coordinates are valid. Lastly calls fix lasers.
     * @param row number of rows in the safe
     * @param col number of columns in the safe
     */
    void removeLaser(int row, int col){
        if (row >= rows || row < 0){
            System.out.println("Error removing laser at: ("+ row +", "+ col +")");
            return;
        }
        if (col >= columns || col < 0){
            System.out.println("Error removing laser at: ("+ row +", "+ col +")");
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
            System.out.println("Laser removed at: ("+ row +", "+ col +")");

        }
        else{
            System.out.println("Error removing laser at: ("+ row +", "+ col +")");
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
}
