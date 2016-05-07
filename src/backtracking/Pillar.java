package backtracking;

/**
 * Represents an pillar and keeps track of its neighbors
 *
 * @author Bryan Quinn
 */
public class Pillar {
    private int row;
    private int col;
    private int number;

    public Pillar(int row, int col, int number){
        this.row = row;
        this.col = col;
        this.number = number;
    }

    public int[] getCoords(){
        return new int[]{this.row, this.col};
    }

    public int getNumber() {
        return this.number;
    }
}
