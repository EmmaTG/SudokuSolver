package sudokuSolver.View;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SudokuView {

    private List<TextArea> cells = new ArrayList<>();
    private Button solveButton;
    private Button clearAllbutton;
    private GridPane gridPane;
    private BorderPane borderPane;

    public SudokuView() {
        this.solveButton = new Button("Solve");
        this.solveButton.setMaxWidth(Double.MAX_VALUE);
        this.clearAllbutton = new Button("Clear all");
        this.clearAllbutton.setMaxWidth(Double.MAX_VALUE);
        this.gridPane = new GridPane();
        this.borderPane = new BorderPane();
        HBox row;
        VBox box;
        int cellCount = 0;
        for (int i=0;i<81;i=i+9){
            row = new HBox();
            box = new VBox();
            for (int count=i;count<i+9;count++){
                TextArea cell = getCell();
                cellCount++;
                cell.setText(String.valueOf(cellCount));
                this.cells.add(cell);
                row.getChildren().add(cell);
            }
            box.getChildren().add(row);
            gridPane.add(box,0,gridPane.getRowCount());
        }
        this.borderPane.setCenter(this.gridPane);

        VBox buttonBox = new VBox(solveButton,clearAllbutton);
        buttonBox.setAlignment(Pos.CENTER);
        this.borderPane.setRight(buttonBox);
    }

    public List<TextArea> getCells() {
        return cells;
    }

    public Button getSolveButton() {
        return solveButton;
    }

    public Button getClearAllbutton() {
        return clearAllbutton;
    }

    public GridPane getGridPane() {
        return gridPane;
    }

    public BorderPane getBorderPane() {
        return borderPane;
    }

    private TextArea getCell(){
        TextArea cell = new TextArea();
        cell.setMaxSize(50,50);
        return cell;
    }
}
