package sample;

import java.util.*;
import java.util.function.BiConsumer;

public class Sudoku {

    private List<List<Cell>> puzzleGrid;
    private List<Integer> numbers = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6,7,8,9));
    private final Map<List<Integer>, Integer> boxes = new HashMap<>();
    private Map<Integer, List<Cell>> boxToCell = new HashMap<>();


    public Sudoku() {
        this.puzzleGrid = new ArrayList<>();
        createBox();
        for (int i=0; i<9; i++) {
            List<Cell> cellColumns = new ArrayList<>();
            for (int j = 0; j < 9; j++) {
                List<Integer> cellIndex = Arrays.asList(i,j);
                int value = 0;
                Cell newCell = new Cell(i,j,boxes.get(cellIndex),value);
                List<Cell> cellList = boxToCell.getOrDefault(boxes.get(cellIndex),new ArrayList<>());
                cellList.add(newCell);
                boxToCell.put(boxes.get(cellIndex),cellList);
                cellColumns.add(newCell);
            }
            this.puzzleGrid.add(cellColumns);
        }
        setMarkups();
    }

    public boolean numberAllowed(int row, int column,int number){
        return getCell(row, column).getMarkUps().contains(number);
    }

    public Cell getCell(int row, int column){
        return puzzleGrid.get(row).get(column);
    }

    public int getCellValue(int row, int column){
        return getCell(row,column).getValue();
    }

    public void setCellValue(int row, int column, int value){
        Cell cell = getCell(row,column);
        if (value>0 && value<10) {
            if (cell.getMarkUps().contains(value)){
                cell.setValue(value);
                setMarkups();
                return;
            }
            System.out.println("Value cannot be added");

        } else {
            System.out.println("Number must be between 1 and 9");
            cell.setValue(0);
        }
    }

    public List<Cell> getRow(int row){
        return puzzleGrid.get(row);
    }

    public List<Cell> getColumn(int column){
        List<Cell> columnList = new ArrayList<>();
        for (int i=0;i<9;i++){
            columnList.add(getCell(i,column));
        }
        return columnList;
    }

    public List<Cell> getBox(int box){
        return boxToCell.get(box);
    }

    private void setMarkups(){
        for (List<Cell> lst: this.puzzleGrid){
            for (Cell c : lst){
                setCellMarkups(c);
            }
        }
    }

    private void setCellMarkups(Cell c){
        List<Integer> markup = new ArrayList<Integer>();
        if (c.getValue()!=0){
            c.setMarkUps(markup);
            return;
        }
        for (int num : numbers){
            if (sudokuCondition(num,c)){
                markup.add(num);
            }
        }
        if (markup.size()==1){
            c.setValue(markup.get(0));
            markup.clear();
            System.out.println("Automatic markup added at row: " + c.getRow() + " and column: " + c.getColumn());
            setMarkups();
        }

        c.setMarkUps(markup);
    }

    public void check(){
        for (int i=0;i<9;i++) {
            List<Integer> checkRow = checkRow(i);
            if (!checkRow.isEmpty()){
                System.out.println("Check Row result:");
                System.out.println("Row: " + i + " Column: " + checkRow.get(0));
                System.out.println("Value: " + checkRow.get(1));
                System.out.println();
            }
            List<Integer> checkColumn = checkColumn(i);
            if (!checkColumn.isEmpty()){
                System.out.println("Check Column result:");
                System.out.println("Row: " + checkColumn.get(0) + " Column: " + i);
                System.out.println("Value: " + checkColumn.get(1));
                System.out.println();
            }
            List<Integer> checkBoxes = checkBoxes(i);
            if (!checkBoxes.isEmpty()){
                System.out.println("Check Boxes result:");
                System.out.println("Row: " + checkBoxes.get(0) + " Column: " + checkBoxes.get(1));
                System.out.println("Value: " + checkBoxes.get(2));
                System.out.println();
            }
        }
    }
    public List<Integer> checkRow(int number){
        List<Cell> listOfRows;
        List<Integer> occurAndCol;
        Map<Integer,List<Integer>> occurences = new HashMap<>();
        List<Integer> numAndLoc;
        listOfRows = getRow(number);
        for (int i=0;i<9;i++) {
            for (Cell c : listOfRows) {
                if (numberAllowed(c.getRow(), c.getColumn(), i)) {
                    occurAndCol =  occurences.getOrDefault(i, new ArrayList<>(Arrays.asList(0,c.getColumn())));
                    occurAndCol.set(0,occurAndCol.get(0)+1);
                    occurences.put(i, occurAndCol);
                }
            }
        }
        numAndLoc = new ArrayList<>();
        for (Map.Entry<Integer,List<Integer>> entry: occurences.entrySet()){
            if (entry.getValue().get(0)==1){
                numAndLoc.add(entry.getValue().get(1)); // column location
                numAndLoc.add(entry.getKey()); // value to be added
            }
        }
        return numAndLoc;
    }

    public List<Integer> checkColumn(int number){
        List<Cell> listOfColumns;
        List<Integer> occurAndRow;
        Map<Integer,List<Integer>> occurences = new HashMap<>();
        List<Integer> numAndLoc;

        listOfColumns = getColumn(number);
        for (int i=0;i<9;i++) {
            for (Cell c : listOfColumns) {
                if (numberAllowed(c.getRow(), c.getColumn(), i)) {
                    occurAndRow =  occurences.getOrDefault(i, new ArrayList<>(Arrays.asList(0,c.getRow())));
                    occurAndRow.set(0,occurAndRow.get(0)+1);
                    occurences.put(i, occurAndRow);
                }
            }
        }
        numAndLoc = new ArrayList<>();
        for (Map.Entry<Integer,List<Integer>> entry: occurences.entrySet()){
            if (entry.getValue().get(0)==1){
                numAndLoc.add(entry.getValue().get(1)); // row location
                numAndLoc.add(entry.getKey()); // value to be added
            }
        }
        return numAndLoc;
    }

    public List<Integer> checkBoxes(int number){
        List<Cell> listOfBoxes;
        List<Integer> occurAndPos;
        Map<Integer,List<Integer>> occurences = new HashMap<>();
        List<Integer> numAndLoc;

            listOfBoxes = getBox(number+1);

        for (int i=0;i<9;i++) {
            for (Cell c : listOfBoxes) {
                if (numberAllowed(c.getRow(), c.getColumn(), i)) {
                    occurAndPos =  occurences.getOrDefault(i, new ArrayList<>(Arrays.asList(0,c.getRow(),c.getColumn())));
                    occurAndPos.set(0,occurAndPos.get(0)+1);
                    occurences.put(i, occurAndPos);
                }
            }
        }
        numAndLoc = new ArrayList<>();
        for (Map.Entry<Integer,List<Integer>> entry: occurences.entrySet()){
            if (entry.getValue().get(0)==1){
                numAndLoc.add(entry.getValue().get(1)); // row location
                numAndLoc.add(entry.getValue().get(2)); // column location
                numAndLoc.add(entry.getKey()); // value to be added
            }
        }
        return numAndLoc;
    }

    public List<Integer> getMarkups(int row, int column){
        return getCell(row,column).getMarkUps();
    }

    private boolean sudokuCondition(int number, Cell cell){
        return (sudokuConditionBox(number,cell) &&
                sudokuConditionColumn(number,cell) &&
                sudokuConditionRow(number,cell));
    }

    private boolean sudokuConditionBox(int number, Cell cell){
        List<Cell> listOfCells = getBox(cell.getBox());
        return checkForValue(number,listOfCells);
    }

    private boolean sudokuConditionColumn(int number, Cell cell){
        List<Cell> listOfCells = getColumn(cell.getColumn());
        return checkForValue(number,listOfCells);
    }

    private boolean sudokuConditionRow(int number, Cell cell){
        List<Cell> listOfCells = getRow(cell.getRow());
        return checkForValue(number,listOfCells);
    }

    private boolean checkForValue(int number, List<Cell> listOfCells){
        for (Cell c: listOfCells){
            if (c.getValue()==number){
                return false;
            }
        }
        return true;
    }

    public List<List<Cell>> getPuzzleGrid() {
        return puzzleGrid;
    }

    public List<Integer> getNumbers() {
        return numbers;
    }

    public Map<List<Integer>, Integer> getBoxes() {
        return boxes;
    }

    public Map<Integer, List<Cell>> getBoxToCell() {
        return boxToCell;
    }

    private void createBox(){
        for (int k = 0; k < 9; k= k+3) {
            for (int firstIndex=k;firstIndex<k+3;firstIndex++){
                int count = 0;
                for (int j = 0; j < 9; j++) {
                    List<Integer> cellList = new ArrayList<>();
                    if (count < 3) {
                        cellList.add(firstIndex);
                        cellList.add(j);
                        boxes.put(cellList, k+1);
                    } else if (count < 6) {
                        cellList.add(firstIndex);
                        cellList.add(j);
                        boxes.put(cellList, k+2);
                    } else if (count < 9) {
                        cellList.add(firstIndex);
                        cellList.add(j);
                        boxes.put(cellList, k+3);
                    }
                    count++;
                }
            }
        }
    }

    @Override
    public String toString() {
        String hLine = "\t-----------------------------------\n";
        StringBuilder sb = new StringBuilder();
        sb.append("\t0\t1\t2\t3\t4\t5\t6\t7\t8\n");
        sb.append(hLine);
        int count =0;
        int horizonLine =0;
        for (List<Cell> lst : this.puzzleGrid){
            sb.append(count);
            sb.append("\t|");
            int vertLine =0;
            for (Cell c: lst){
                sb.append(c.getValue());
                vertLine++;
                if (vertLine==3){
                    sb.append("|");
                    vertLine = 0;
                }
                sb.append("\t");
            }

            sb.append("\n");
            count++;
            horizonLine++;
            if (horizonLine==3){
                sb.append(hLine);
                horizonLine = 0;
            }
        }
        return sb.toString();
    }
}
