package sudokuSolver;

import sudokuSolver.Model.Sudoku;

public class Main{


    public static void main(String[] args) {
        Sudoku sudoku = new Sudoku();
        sudoku.createSudoku();
    }


    public static Sudoku easySudoku(){
        Sudoku sudoku = new Sudoku();
        // Very Easy sudoku SOLVED
        sudoku.setCellValue(0,3,9);
        sudoku.setCellValue(0,8,3);

        sudoku.setCellValue(1,0,3);
        sudoku.setCellValue(1,1,4);
        sudoku.setCellValue(1,4,1);
        sudoku.setCellValue(1,6,6);

        sudoku.setCellValue(2,1,5);
        sudoku.setCellValue(2,2,6);
        sudoku.setCellValue(2,3,4);
        sudoku.setCellValue(2,8,8);

        sudoku.setCellValue(3,0,1);
        sudoku.setCellValue(3,1,3);
        sudoku.setCellValue(3,2,2);
        sudoku.setCellValue(3,3,6);
        sudoku.setCellValue(3,4,5);
        sudoku.setCellValue(3,5,8);

        sudoku.setCellValue(4,1,9);
        sudoku.setCellValue(4,3,7);
        sudoku.setCellValue(4,4,4);
        sudoku.setCellValue(4,5,3);
        sudoku.setCellValue(4,7,6);

        sudoku.setCellValue(5,1,6);
        sudoku.setCellValue(5,2,4);
        sudoku.setCellValue(5,3,2);
        sudoku.setCellValue(5,4,9);
        sudoku.setCellValue(5,5,1);
        sudoku.setCellValue(5,6,8);

        sudoku.setCellValue(6,1,2);
        sudoku.setCellValue(6,4,8);
        sudoku.setCellValue(6,6,3);
        sudoku.setCellValue(6,7,1);
        sudoku.setCellValue(6,8,9);

        sudoku.setCellValue(7,4,2);
        sudoku.setCellValue(7,7,8);

        sudoku.setCellValue(8,1,8);
        sudoku.setCellValue(8,5,9);
        sudoku.setCellValue(8,6,4);
        sudoku.setCellValue(8,7,5);
        return sudoku;
    }

    public static Sudoku mediumSudoku(){
        Sudoku sudoku = new Sudoku();
        // Medium sudoku SOLVED
        sudoku.setCellValue(0,1,1);
        sudoku.setCellValue(0,2,5);
        sudoku.setCellValue(0,4,4);
        sudoku.setCellValue(0,6,6);
        sudoku.setCellValue(0,8,7);

        sudoku.setCellValue(1,6,2);
        sudoku.setCellValue(1,8,4);

        sudoku.setCellValue(2,1,2);
        sudoku.setCellValue(2,7,1);
        sudoku.setCellValue(2,8,3);

        sudoku.setCellValue(3,2,8);
        sudoku.setCellValue(3,4,3);
        sudoku.setCellValue(3,6,1);
        sudoku.setCellValue(3,7,6);

        sudoku.setCellValue(4,5,5);

        sudoku.setCellValue(5,1,6);
        sudoku.setCellValue(5,2,4);
        sudoku.setCellValue(5,3,2);
        sudoku.setCellValue(5,8,8);

        sudoku.setCellValue(6,0,8);
        sudoku.setCellValue(6,2,1);
        sudoku.setCellValue(6,5,6);
        sudoku.setCellValue(6,7,4);
        sudoku.setCellValue(6,8,5);

        sudoku.setCellValue(7,3,8);
        sudoku.setCellValue(7,6,7);

        sudoku.setCellValue(8,0,2);
        sudoku.setCellValue(8,4,7);
        sudoku.setCellValue(8,5,3);
        sudoku.setCellValue(8,8,1);
        return sudoku;

    }

    public static Sudoku hardSudoku(){
        Sudoku sudoku = new Sudoku();
        // Hard sudoku UNSOLVED
        sudoku.setCellValue(0,0,4);
        sudoku.setCellValue(0,3,9);
        sudoku.setCellValue(0,6,8);
        sudoku.setCellValue(0,7,6);

        sudoku.setCellValue(1,5,7);
        sudoku.setCellValue(1,6,2);

        sudoku.setCellValue(2,1,3);
        sudoku.setCellValue(2,3,2);
        sudoku.setCellValue(2,4,5);

        sudoku.setCellValue(3,1,1);
        sudoku.setCellValue(3,2,7);
        sudoku.setCellValue(3,5,5);
        sudoku.setCellValue(3,7,2);
        sudoku.setCellValue(3,8,9);

        sudoku.setCellValue(4,1,9);
        sudoku.setCellValue(4,5,6);

        sudoku.setCellValue(5,1,6);
        sudoku.setCellValue(5,7,7);

        sudoku.setCellValue(6,3,4);
        sudoku.setCellValue(6,6,5);

        sudoku.setCellValue(7,0,7);
        sudoku.setCellValue(7,6,9);

        sudoku.setCellValue(8,4,6);
        sudoku.setCellValue(8,5,2);
        sudoku.setCellValue(8,6,4);
        return sudoku;
    }

    public static Sudoku expertSudoku(){
        Sudoku sudoku = new Sudoku();
        // Expert sudoku UNSOLVED
        sudoku.setCellValue(0,2,6);
        sudoku.setCellValue(0,5,8);
        sudoku.setCellValue(0,6,5);

        sudoku.setCellValue(1,4,7);
        sudoku.setCellValue(1,6,6);
        sudoku.setCellValue(1,7,1);
        sudoku.setCellValue(1,8,3);

        sudoku.setCellValue(2,8,9);

        sudoku.setCellValue(3,4,9);
        sudoku.setCellValue(3,8,1);

        sudoku.setCellValue(4,2,1);
        sudoku.setCellValue(4,6,8);

        sudoku.setCellValue(5,0,4);
        sudoku.setCellValue(5,3,5);
        sudoku.setCellValue(5,4,3);

        sudoku.setCellValue(6,0,1);
        sudoku.setCellValue(6,2,7);
        sudoku.setCellValue(6,4,5);
        sudoku.setCellValue(6,5,3);

        sudoku.setCellValue(7,1,5);
        sudoku.setCellValue(7,4,6);
        sudoku.setCellValue(7,5,4);

        sudoku.setCellValue(8,0,3);
        sudoku.setCellValue(8,3,1);
        sudoku.setCellValue(8,7,6);
        return sudoku;
    }
}
