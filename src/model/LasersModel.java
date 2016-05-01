package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Observable;
import java.util.Scanner;

public class LasersModel extends Observable {

    public LasersModel(String filename)  {
        // TODO

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

            //this.safe = new Safe(row, col, grid);

            in.close();
        } catch (FileNotFoundException fnfe){
            System.out.println(filename + " (The system cannot find the file specified)");
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
}
