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

    /**
     * sets the the variables for the location the pill
     * @param row the row that the pillar is in
     * @param col the col that the pillar is in
     * @param number the pillars number of lasers it can have
     */
    public Pillar(int row, int col, int number){
        this.row = row;
        this.col = col;
        this.number = number;
    }

    /**
     * the coordinates of the pillar
     * @return the coordinate of the pillar
     */
    public int[] getCoords(){
        return new int[]{this.row, this.col};
    }

    /**
     * the number of lasers that can be around the laser
     * @return the number
     */
    public int getNumber() {
        return this.number;
    }
}
