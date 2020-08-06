package sudokuSolver.View;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.*;
import java.util.function.UnaryOperator;

public class SudokuView {

    private List<TextField> cells = new ArrayList<>();
    private Button solveButton;
    private Button clearAllbutton;
    private GridPane gridPane;
    private BorderPane borderPane;
    private static final String ORIGINAL_STYLE = "-fx-text-fill: black; -fx-font-size:20; -fx-text-alignment: center";
    private static final String CHANGED_STYLE = "-fx-text-fill: red; -fx-font-size:20; -fx-font-weight: bold; " +
            "-fx-text-alignment: center";

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
                TextField cell = getCell();
                cell.setText(String.valueOf(cellCount));
                cellCount++;
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

    public void clearAllFields(){
        for (TextField textArea : this.cells){
            textArea.clear();
            textArea.setStyle(ORIGINAL_STYLE);
        }
    }

    public void disableTextAreas(boolean disable){
        for (TextField textArea : this.cells){
            textArea.setDisable(disable);
        }
    }

    public void addSolvedValues(Map<List<Integer>, Integer> values){
        int row,col,val,idx;
        for (Map.Entry<List<Integer>,Integer> entry :values.entrySet()){
            row = entry.getKey().get(0);
            col = entry.getKey().get(1);
            idx = row*9 + col;
            val = entry.getValue();
            TextField textarea = this.cells.get(idx);
            textarea.setText(String.valueOf(val));
            textarea.setStyle(CHANGED_STYLE);
        }

    }

    public Button getSolveButton() {
        return solveButton;
    }

    public Button getClearAllbutton() {
        return clearAllbutton;
    }

    public BorderPane getBorderPane() {
        return borderPane;
    }

    public Map<List<Integer>, Integer> getCellvalues() {
        Map<List<Integer>, Integer> locAndValues= new HashMap<>();
        this.cells.forEach(el -> {
            if (!el.getText().isEmpty()) {
                int val = Integer.valueOf(el.getText());
                int idx = this.cells.indexOf(el);
                int row = (int) Math.floor(idx / 9.0);
                int col = idx % 9;
                locAndValues.put(Arrays.asList(row, col), val);
            }
        });
        return locAndValues;
    }

    private TextField getCell(){
        TextField cell = new TextField();
        cell.setMaxSize(50,50);
        cell.setStyle(ORIGINAL_STYLE);
        cell.setAlignment(Pos.CENTER);

        // TextFormater to ensure only one digit (0-9) is added
        UnaryOperator<TextFormatter.Change> integerFilter = change ->
                (change.getText().matches("([1-9])?") && change.getControlNewText().length()<2) ? change : null;
        cell.setTextFormatter(new TextFormatter<String>(integerFilter));

        return cell;
    }


    public List<TextField> getCells() {
        return cells;
    }

    public GridPane getGridPane() {
        return gridPane;
    }
}
