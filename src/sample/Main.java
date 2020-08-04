package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
//        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
//        primaryStage.setTitle("Hello World");
//        primaryStage.setScene(new Scene(root, 300, 275));
//        primaryStage.show();
    }


    public static void main(String[] args) {
//        launch(args);
        Sudoku sudoku = new Sudoku();
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
        System.out.println(sudoku.toString());
//        System.out.println(sudoku.getMarkups(8,4));
//        System.out.println(sudoku.numberAllowed(1,1,8));
//        System.out.println(sudoku.getPuzzleGrid().get(0).get(8).toString());
        sudoku.check();
    }

}
