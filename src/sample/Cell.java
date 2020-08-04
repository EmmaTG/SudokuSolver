package sample;

import java.util.ArrayList;
import java.util.List;

public class Cell {

    private int row;
    private int column;
    private int box;
    private int value;
    private List<Integer> markUps = new ArrayList<>();

    public Cell(int row, int column, int box, int value) {
        this.row = row;
        this.column = column;
        this.box = box;
        this.value = value;
    }

    public void setMarkUps(List<Integer> markUps) {
        this.markUps = markUps;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public int getBox() {
        return box;
    }

    public int getValue() {
        return value;
    }

    public List<Integer> getMarkUps() {
        return markUps;
    }

    public void setValue(int value) {
        if (this.getValue()== value){
            System.out.println("Replacing " + this.value + " with " + value);
        }
        this.value = value;
    }

    @Override
    public String toString() {
        return "Cell{" +
                "row=" + row +
                ", column=" + column +
                ", box=" + box +
                ", \nvalue=" + value +
                ", \nmarkUps=" + markUps +
                '}';
    }
}
