package sudokuSolver.Controller;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import sudokuSolver.Model.Sudoku;
import sudokuSolver.View.SudokuView;

import java.util.*;

public class Controller {

    private SudokuView sudokuView;
    private Sudoku sudoku;
    private int emptyPositions;

    public Controller(SudokuView sudokuView) {
        this.sudokuView = sudokuView;
        init();
    }

    private void init(){
        this.sudoku = new Sudoku();
        this.emptyPositions=this.sudoku.getEmptyPos();
        sudokuView.clearAllFields();
        sudokuView.getSolveButton().setText("Solve");
        sudokuView.disableTextAreas(false);
        setEventHandlers();

    }

    private void setEventHandlers(){

        // Set event handler for the "Clear All" button
        Button clearButton = sudokuView.getClearAllbutton();
        clearButton.setDisable(false);
        clearButton.setOnAction((e) -> init());

        // Set event handler for the "Create Sudoku" button
        Button createButton = sudokuView.getCreatebutton();
        createButton.setDisable(false);
        createButton.setOnAction(e -> {
            init();
            createSudoku();
//            init();
        });

        // Set event handler for the "Solve" button
        Button solveButton = sudokuView.getSolveButton();
        solveButton.setOnAction((e) -> {
            boolean solved = this.sudoku.solve(getValues());
            if (solved) {
                Map<List<Integer>, Integer> values = this.sudoku.solvedValues();
                sudokuView.addSolvedValues(values);
                sudokuView.disableTextAreas(true);
                solveButton.setText("Reset");
                clearButton.setDisable(true);
                solveButton.setOnAction((e2) -> init());
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Unsolvable!");
                alert.setHeaderText("Sudoku could not be solved.");
                alert.setContentText("Please re-check numbers");
                alert.show();
                this.sudoku = new Sudoku();
            }

        });
    }

    private Map<List<Integer>, Integer> getValues(){
        return sudokuView.getCellvalues();
    }

    private void createSudoku(){
        List<List<Integer>> createdSudoku = sudoku.createSudoku();
        sudokuView.displaySudoku(createdSudoku);
    }

    public void updateString(){
        int changeInValues = (emptyPositions-sudoku.getEmptyPos());
        System.out.println(changeInValues + (changeInValues==1 ? " value added" :  " values added"));
        System.out.println("Empty positions remaining: " + sudoku.getEmptyPos());
        emptyPositions = sudoku.getEmptyPos();
    }

}
