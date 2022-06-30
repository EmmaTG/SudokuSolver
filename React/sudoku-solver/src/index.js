import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import reportWebVitals from './reportWebVitals';

const oneToNine = [1,2,3,4,5,6,7,8,9];

function Header(props) {
return <React.Fragment>
        <h1>Sudoku Solver</h1>
        <h4> Number of empty positions: {props.emptyPositions}</h4>
        </React.Fragment>
}

function Button(props) {
return (
<div>
    <button name="props.label" onClick={props.onClick}> {props.label} </button>
</div>
);
}

function boxNumToCells(boxNum){

    let cells = Array(9).fill(null);

    let rowStart;
    if (boxNum < 3){
        rowStart = 0;
    } else if (boxNum < 6){
        rowStart = 3;
    } else {
        rowStart = 6;
    }

    let colStart;
    if (boxNum === 0 || boxNum === 3  || boxNum === 6 ){
        colStart = 0;
    } else if (boxNum === 1 || boxNum === 4 || boxNum === 7 ){
        colStart = 3;
    } else {
        colStart = 6;
    }

    let counter = 0;
    for (let r = rowStart; r<(rowStart+3); r++){
        for (let c = colStart; c<(colStart+3); c++){
            let idx = (r*9)+c;
            cells[counter] = idx;
            counter++;
        }
    }
    return cells;
}

function cellToRow(num) {
    return Math.floor((num)/9);
}

function rowToCell(row) {
    let arr =   [0,1,2,3,4,5,6,7,8].map(x => (x+9*(row)));
    return arr;
}

function cellToCol(num) {
    return Math.floor( (num)%9);
}

function colToCell(col) {
    return [0,1,2,3,4,5,6,7,8].map(x => x*9+col);
}

function cellToBox(num) {
    const row = cellToRow(num);
    const col = cellToCol(num);
    if (row<3){
        if (col<3)
            {return 0;}
        else if (col<6)
            {return 1;}
        else
            {return 2;}
    }
    else if (row <6) {
        if (col<3)
            {return 3;}
        else if (col<6)
            {return 4;}
        else
            {return 5;}
    }
    else {
        if (col<3)
            {return 6;}
        else if (col<6)
            {return 7;}
        else
            {return 8;}
    }

}

class Cell extends React.Component {

    onlyNumberKey(evt) {
        console.log("Only ASCII character in that range allowed");
        var ASCIICode = (evt.which) ? evt.which : evt.keyCode
        if (ASCIICode > 31 && (ASCIICode < 48 || ASCIICode > 57))
            return false;
        return true;
    }

    render () {
        return (
            <input
            type="text"
            size="2"
            key="this.props.number"
            value={this.props.details.value}
            onInput={(e) => this.props.onChange(e,this.props.number)}
            >
            </input>
        );
    }
}

class Board extends React.Component {

    renderCell(i) {
    return (<Cell number={i+1}
                  details = {this.props.cells[i]}
                  onChange={(e) => this.props.onChange(e, i)}
                  />);
    }

    renderRow(startVal){
        return (<div className="board-row">
              {this.renderCell(startVal)}
              {this.renderCell(startVal+1)}
              {this.renderCell(startVal+2)}
              {this.renderCell(startVal+3)}
              {this.renderCell(startVal+4)}
              {this.renderCell(startVal+5)}
              {this.renderCell(startVal+6)}
              {this.renderCell(startVal+7)}
              {this.renderCell(startVal+8)}
            </div>
            );
    }

    render () {
        return (
            <React.Fragment>
                {this.renderRow(0)}
                {this.renderRow(9)}
                {this.renderRow(18)}
                {this.renderRow(27)}
                {this.renderRow(36)}
                {this.renderRow(45)}
                {this.renderRow(54)}
                {this.renderRow(63)}
                {this.renderRow(72)}
            </React.Fragment>
        );
    }
}

class Sudoku extends React.Component {

    constructor(props) {
        super(props);
        this.state = this.resetBoard();

    }

    resetBoard(){
        this.columnMarkups = this.initialMarkups();
        this.rowMarkups = this.initialMarkups();
        this.boxMarkups = this.initialMarkups();

        return ({
            board: Array(81).fill(null).map((x,num) => {
                               return ({
                                   row: cellToRow(num),
                                   col: cellToCol(num),
                                   box: cellToBox(num),
                                   markups: oneToNine.slice(),
                                   value: ""
                               })
                           }),
            emptyPositions: 81,
        })
    }


    initialMarkups(){
        let markupMap = Array(9).fill(oneToNine);
        return markupMap;
    }

    /*
    Get affected cells in by the addition of a value current cell curCell
    */
    getAffectedCells(curCell){
        const boxNum = curCell.box;
        const row = curCell.row;
        const col = curCell.col;

        let affectedCellNums = rowToCell(row);
        affectedCellNums = affectedCellNums.concat(colToCell(col));
        affectedCellNums = affectedCellNums.concat(boxNumToCells(boxNum));

        affectedCellNums = affectedCellNums.filter((x,idx) => {return affectedCellNums.indexOf(x) === idx})

        let affectedCells = [];
        let newBoard = this.state.board.slice();
        for (const c of affectedCellNums) {
            affectedCells.push(newBoard[c]);
        }

        return affectedCells;
    }

    updateMarkers(cell) {
        // Get values of cells in box, get values of col cells, get values of row cells
        // if value, remove value from cell markup

        let idxLocation;
        const boxNum = cell.box;
        idxLocation = this.boxMarkups[boxNum].indexOf(cell.value);
        this.boxMarkups[boxNum].splice(idxLocation,1);

        const row = cell.row;
        idxLocation = this.rowMarkups[row].indexOf(cell.value);
        this.rowMarkups[row].splice(idxLocation,1);

        const col = cell.col;
        idxLocation = this.columnMarkups[col].indexOf(cell.value);
        this.columnMarkups[col].splice(idxLocation,1);

        let affectingCells = this.getAffectedCells(cell);


        let newBoard = this.state.board.slice();
        for (let c of affectingCells) {
            const idx = c.markups.indexOf(cell.value);
            if (idx !== -1){
                c.markups.splice(idx,1);
            }
        }

        this.setState({board: newBoard});

    }

    create_sudoku() {
        let newBoard = this.state.board.slice();
        let newEmptyCells = this.state.emptyPositions;

        //TODO:: After solving sudoku, values must removed to create one.
        // 1. fill boxes 0, 4 and 8 with number 1-9
        [0,4,8].forEach( (x) => {
            var cells = boxNumToCells(x);
            for(const c of cells) {
                let cell = newBoard[c];
                const markupSize = cell.markups.length;
                const randomNumber = Math.floor(Math.random() * markupSize);
                cell.value = cell.markups[randomNumber];
                newEmptyCells--;
                this.updateMarkers(cell);
            }
        });

        let stateObj = {
            board: newBoard,
            emptyPositions: newEmptyCells
            };
        // 2. simplySolve the sudoku
        console.log(stateObj);
        this.simplySolve(stateObj);
        console.log(stateObj);
        // 3. remove a certain number of positions
        this.removeCells(10,stateObj);
        console.log(stateObj);
        // 4. display sudoku
    }

    simplySolve(stateValues) {
        let emptyCells = [];
        let newBoard = stateValues.board
        let emptyPos = stateValues.emptyPositions;

        // Create array of empty cells
        newBoard.forEach(x => {
            if (!x.value){
                emptyCells.push(x);
            }
        });

        let valueFound = false;
        let c;
        let startVal = 0;
        const cellIterator = [1,2,3,4,5,6,7,8,9,10].values();
        var cellStack = [];
        c = emptyCells[0];
        let idx = 0;
        while (idx != emptyCells.length){
            // Find a value between 1 and 9 to fill this cell
            for (let i = startVal + 1 ; i < 10 ; i++){
                valueFound = this.sudokuCondition(c,i);
                if (valueFound){
                    c.value = i;
                    break; // Break: for (let i = startVal + 1 ; i < 10 ; i++)
                }
            }
            // If no value can be put in this position
            if(!valueFound) {
                if (idx != 0) { // And its not the first cell
                    c.value = "";
                    emptyPos++;
                    idx--;
                    c = emptyCells[idx]
                    startVal = c.value;
                } else {
                    break // Break: while (idx != emptyCells.length)
                }
            } else { // If a value has been found, move onto the next cell
                idx++;
                emptyPos--;
                c = emptyCells[idx]
                startVal = 0;
            }
        }
        stateValues.emptyPositions = emptyPos;
    }

    removeCells(numCells, stateObj) {
        let newBoard = stateObj.board
        let emptyPos = stateObj.emptyPositions;

         let count = numCells;
         while (count > 0) {
            const randomNumberRow = Math.floor(Math.random() * 9);
            const randomNumberCol = Math.floor(Math.random() * 9);
            const cellNumber = randomNumberRow*9 + randomNumberCol
            let removedCell = newBoard[cellNumber];
            removedCell.value = "";
            removedCell.markups = oneToNine.slice();
            this.updateMarkers(removedCell);
            emptyPos++;
            count--;
            }
         stateObj.emptyPositions = emptyPos;
    }

    sudokuCondition(cell,value) {
        let cells = this.getAffectedCells(cell).filter(c => c.value);
        // Check if affected cells contain value you want to add
        for (const c of cells){
            if (c.value === value){
                return false;
            }
        }
        return true;
    }

    solve_sudoku() {
        //TODO:: Implement simple solve algorithm
        // First create flat list of all Cells where value = null;
//        this.state.board.forEach()
    }

    handleChange(e,num){
    //TODO: Only allow numeric values
//        const result = e.target.value.replace(/\D/g, '');
//        setValue(result);
        console.log("Handling box number " + num);
        console.log(e.target.value);
    }

    render() {
        return (
        <React.Fragment>
                    <Header emptyPositions={this.state.emptyPositions}/>
                    <Button label="Create Sudoku" onClick={()=>this.create_sudoku()} />
                    <Board
                        cells={this.state.board}
                        onChange={(e,num) => this.handleChange(e,num)}/>
        </React.Fragment>
                )
    }
}

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(<Sudoku />);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
