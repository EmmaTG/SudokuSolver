package sudokuSolver.Model;

import java.util.*;
import java.util.function.ToIntFunction;

public class Sudoku {

    private int emptyPos;
    private List<List<Cell>> puzzleGrid;
    private List<List<Integer>> unsolvedGrid;
    private List<Integer> numbers = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9));
    private final Map<List<Integer>, Integer> boxes = new HashMap<>();
    private Map<Integer, List<Cell>> boxToCell = new HashMap<>();
    private static boolean createMode=false;


    public Sudoku() {
        this.emptyPos=81;
        this.puzzleGrid = new ArrayList<>();
        this.unsolvedGrid = new ArrayList<>();
        createBox();
        for (int i=0; i<9; i++) {
            unsolvedGrid.add(new ArrayList<>());
            List<Cell> cellColumns = new ArrayList<>();
            for (int j = 0; j < 9; j++) {
                int value = 0;
                unsolvedGrid.get(i).add(value);
                List<Integer> cellIndex = Arrays.asList(i,j);
                Cell newCell = new Cell(i,j,boxes.get(cellIndex),value);
                List<Cell> cellList = boxToCell.getOrDefault(boxes.get(cellIndex),new ArrayList<>());
                cellList.add(newCell);
                boxToCell.put(boxes.get(cellIndex),cellList);
                cellColumns.add(newCell);
            }
            this.puzzleGrid.add(cellColumns);
        }
        initializeMarkups();
    }

    private void initializeMarkups(){
        for (List<Cell> lst: this.puzzleGrid){
            for (Cell c : lst){
                c.setMarkUps(numbers);
            }
        }
    }

    public boolean solve(Map<List<Integer>, Integer> filledValues){
        createMode = false;
        int row,col,val;
        for (Map.Entry<List<Integer>,Integer> entry : filledValues.entrySet()){
            row = entry.getKey().get(0);
            col = entry.getKey().get(1);
            val = entry.getValue();
            this.setCellValue(row,col,val);
        }
        this.placeFinding();

        System.out.println(toString());
        int count = 1;
        while(this.candidateCheck() && count<20){
            count++;
            System.out.println(toString());
        }

        return this.simpleSolve();
    }

    public Map<List<Integer>, Integer> solvedValues(){
        List<Cell> lst;
        List<Integer> unsLst;
        int row,col,val;
        Cell c;
        Map<List<Integer>, Integer> values = new HashMap<>();
        for (int i=0; i<this.puzzleGrid.size();i++){
            lst = this.puzzleGrid.get(i);
            unsLst = this.unsolvedGrid.get(i);
            for (int j=0;j<lst.size();j++){
                if (unsLst.get(j)==0){
                    c = lst.get(j);
                    row = c.getRow();
                    col = c.getColumn();
                    val = c.getValue();
                    values.put(Arrays.asList(row,col),val);
                }
            }
        }
        return values;
    }


    private boolean numberAllowed(int row, int column,int number){
        return getCell(row, column).getMarkUps().contains(number);
    }

    private Cell getCell(int row, int column){
        return puzzleGrid.get(row).get(column);
    }

    public void setCellValue(int row, int column, int value){
        Cell cell = getCell(row,column);
        List<Integer> unsolvedRow = unsolvedGrid.get(row);
        if (value>0 && value<10) {
            if (cell.getMarkUps().contains(value)){
                cell.setValue(value);
                unsolvedRow.set(column,value);
                emptyPos--;
                return;
            }
            if (cell.getValue()!= value){
            System.out.println(String.format("Error with value %d in row %d and column %d",value,row,column));

            }

        } else {
            System.out.println("Number must be between 1 and 9");
            cell.setValue(0);
        }
    }

    private List<Cell> getRow(int row){
        return puzzleGrid.get(row);
    }

    private List<Cell> getColumn(int column){
        List<Cell> columnList = new ArrayList<>();
        for (int i=0;i<9;i++){
            columnList.add(getCell(i,column));
        }
        return columnList;
    }

    private List<Cell> getBox(int box){
        return boxToCell.get(box);
    }

    public int getEmptyPos() {
        return emptyPos;
    }

    private void placeFinding(){
        for (List<Cell> lst: this.puzzleGrid){
            for (Cell c : lst){
                setCellMarkups(c);
            }
        }
    }

    private void setCellMarkups(Cell c){
        List<Integer> markup = new ArrayList<>();
        if (c.getValue()!=0){
            c.setMarkUps(markup);
            return;
        }
        for (int num : numbers){
            if (sudokuCondition(num,c)){
                markup.add(num);
            }
        }
        if (!createMode) {
            if (markup.size() == 1) {
                c.setValue(markup.get(0));
                emptyPos--;
                markup.clear();
//            System.out.println("Automatic markup added at row: " + c.getRow() + " and column: " + c.getColumn());
                placeFinding();
            }
        }
        c.setMarkUps(markup);
    }

    private boolean candidateCheck(){
        if (emptyPos>0) {
            boolean updated = false;
            int row, column, box, value;
            for (int i = 0; i < 9; i++) {
                List<Integer> checkRow = checkRow(i);
                if (!checkRow.isEmpty()) {
                    row = i;
                    column = checkRow.get(0);
                    value = checkRow.get(1);
                    box = getCell(row, column).getBox();
                    puzzleGrid.get(row).get(column).setValue(value);
                    emptyPos--;
                    updateSelectMarkers(row, column, box);
//                stringCheckResult(row,column,value);
                    updated = true;
                }
                List<Integer> checkColumn = checkColumn(i);
                if (!checkColumn.isEmpty()) {
                    row = checkColumn.get(0);
                    column = i;
                    value = checkColumn.get(1);
                    box = getCell(row, column).getBox();
                    puzzleGrid.get(row).get(column).setValue(value);
                    emptyPos--;
                    updateSelectMarkers(row, column, box);
//                stringCheckResult(row,column,value);
                    updated = true;
                }
                List<Integer> checkBoxes = checkBoxes(i);
                if (!checkBoxes.isEmpty()) {
                    row = checkBoxes.get(0);
                    column = checkBoxes.get(1);
                    value = checkBoxes.get(2);
                    box = i + 1;
                    puzzleGrid.get(row).get(column).setValue(value);
                    emptyPos--;
                    updateSelectMarkers(row, column, box);
//                stringCheckResult(row,column,value);
                    updated = true;
                }
            }
            return updated;
        }
        System.out.println("Sudoku solved!!");
        return false;
    }


    private void updateSelectMarkers(int row, int col, int box){
        updateRowMarkups(row);
        updateColumnMarkups(col);
        updateBoxesMarkups(box);
    }

    private void updateRowMarkups(int row){
        for (Cell c : getRow(row)){
            setCellMarkups(c);
        }
    }

    private void updateColumnMarkups(int column){
        for (Cell c : getColumn(column)){
            setCellMarkups(c);
        }
    }

    private void updateBoxesMarkups(int box){
        for (Cell c : getBox(box)){
            setCellMarkups(c);
        }
    }

    private List<Integer> checkRow(int number){
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

    private List<Integer> checkColumn(int number){
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

    private List<Integer> checkBoxes(int number){
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

    private boolean simpleSolve() {
//        System.out.println("Simple solving algorithm");

        List<Cell> allCells = new ArrayList<>();
        Cell c;

        // Create flattened list of all cells with no entry
        this.puzzleGrid.forEach((lst) -> lst.stream()
                .filter(cell -> cell.getValue() == 0)
                .forEach(allCells::add));

        //Iterate through cells in list using a simple trial and error algorithm.
        ListIterator<Cell> iterator = allCells.listIterator();
        int startVal = 0,count = 0;
        boolean valueFound;
        while (count < 50000 && iterator.hasNext()) {
            count++;
            valueFound = false;
            c = iterator.next();
            for (int i = startVal + 1; i < 10; i++) {
                if (sudokuCondition(i, c)) {
                    c.setValue(i);
                    valueFound = true;
                    break;
                }
            }
            if (!valueFound) {
                if (iterator.hasPrevious()) {
                    c.setValue(0);
                    emptyPos++;
                    iterator.previous();
                    c = iterator.previous();
                    startVal = c.getValue();
                } else {
                    break;
                }
            } else {
                emptyPos--;
                startVal = 0;
            }
        }
//        System.out.println(count);
        return emptyPos==0;
    }

    public List<List<Integer>> createSudoku() {
        createMode = true;
        this.emptyPos = 81;
        this.puzzleGrid = new ArrayList<>();
        this.unsolvedGrid = new ArrayList<>();
        createBox();
        for (int i = 0; i < 9; i++) {
            unsolvedGrid.add(new ArrayList<>());
            List<Cell> cellColumns = new ArrayList<>();
            for (int j = 0; j < 9; j++) {
                int value = 0;
                unsolvedGrid.get(i).add(value);
                List<Integer> cellIndex = Arrays.asList(i, j);
                Cell newCell = new Cell(i, j, boxes.get(cellIndex), value);
                List<Cell> cellList = boxToCell.getOrDefault(boxes.get(cellIndex), new ArrayList<>());
                cellList.add(newCell);
                boxToCell.put(boxes.get(cellIndex), cellList);
                cellColumns.add(newCell);
            }
            this.puzzleGrid.add(cellColumns);
        }
        initializeMarkups();

        fillBoxes();
        simpleSolve();

        removeCells();
        System.out.println(toString());

        List<List<Integer>> values = new ArrayList<>();
        this.puzzleGrid.forEach(lst -> {
            List<Integer> rows = new ArrayList<>();
            lst.forEach(el -> rows.add(el.getValue()));
            values.add(rows);
        });
        this.unsolvedGrid = values;

        return values;
    }

    private void removeCells(){
        int k = 20;
        int count = k;
        Random rand = new Random();
        while (count>0) {
            int row = rand.nextInt(9);
            int col = rand.nextInt(9);
            Cell c = getCell(row, col);
            if (c.getValue() != 0) {
//                System.out.println("row: " + row+ ", col: " + col);
                c.setValue(0);
                updateSelectMarkers(row,col,c.getBox());
                emptyPos++;
                count--;
            }
        }

    }

    private void fillBoxes(){
        Random rand = new Random();
        for (int boxNo : Arrays.asList(1,5,9)) {
            List<Cell> boxCells = boxList(boxNo);
            for (Cell c : boxCells) {
                int markupsSize = c.getMarkUps().size();
                int random = rand.nextInt(markupsSize);
                c.setValue(c.getMarkUps().get(random));
                updateSelectMarkers(c.getRow(), c.getColumn(), c.getBox());
                emptyPos--;
            }
        }
    }

    private List<Cell> boxList(int i){
        List<Cell> topLeftBox = new ArrayList<>();
        this.puzzleGrid.forEach(lst ->
                lst.stream()
                        .filter(c -> c.getBox() == i)
                        .forEach(topLeftBox::add));
        topLeftBox.sort((c1, c2) -> Integer.compare(c2.getMarkUps().size(),c1.getMarkUps().size()));
        return topLeftBox;
    }




    private boolean sudokuCondition(int number, Cell cell){
        // true if number could go there
        // false if number not allowed to be there
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
//        for (List<Cell> lst : this.puzzleGrid){
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


    public int getCellValue(int row, int column){
        return getCell(row,column).getValue();
    }

    public void resetEmptyPos() {
        this.emptyPos = 81;
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


    private void stringCheckResult(int row, int col,int value){
        System.out.println("Check Row result:");
        System.out.println("Row: " + row + " Column: " + col);
        System.out.println("Value: " + value);
    }
    public List<Integer> getMarkups(int row, int column){
        return getCell(row,column).getMarkUps();
    }
}
