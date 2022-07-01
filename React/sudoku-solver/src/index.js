import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import reportWebVitals from './reportWebVitals';

const oneToNine = [1,2,3,4,5,6,7,8,9];

function Header(props) {
return <React.Fragment>
        <h1></h1>
        <h4> Number of empty positions: {props.emptyPositions}</h4>
        </React.Fragment>
}

function Button(props) {
return (
    <button name="props.label" onClick={props.onClick}> {props.label} </button>
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

    render () {
        const fontStyle =this.props.details.start ? '#D3D3D3': '';
        return (
            <input
            type="text"
            size="2"
            key="this.props.number"
            value={this.props.details.value}
            style={{background: fontStyle}}
            readOnly={this.props.details.start}
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
        this.handleChange = this.handleChange.bind(this);
        this.originalBoard = null;
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
                                   value: "",
                                   start: false,
                               })
                           })
        })
    }


    initialMarkups(){
        let markupMap = Array(9).fill(oneToNine.slice());
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

    updateMarkers(cell, newBoard) {
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

        // 1. fill boxes 0, 4 and 8 with number 1-9
        [0,4,8].forEach( (x) => {
            var cells = boxNumToCells(x);
            for(const c of cells) {
                let cell = newBoard[c];
                const markupSize = cell.markups.length;
                const randomNumber = Math.floor(Math.random() * markupSize);
                cell.value = cell.markups[randomNumber];
                this.updateMarkers(cell,newBoard);
            }
        });

        let stateObj = {
            board: newBoard
            };
        // 2. simplySolve the sudoku
        this.simplySolve(stateObj);
        // 3. remove a certain number of positions
        this.removeCells(32,stateObj);
        this.originalBoard = stateObj.board.map(x => {return {row: x.row,
                                                             col: x.col,
                                                             box: x.box,
                                                             markups: x.markups.slice(),
                                                             value: x.value,
                                                             start: x.value !== ""}
                                                     });
        // 4. display sudoku
        this.setState(stateObj);
    }

    simplySolve(stateValues) {
        let emptyCells = [];
        let newBoard = stateValues.board

        // Create array of empty cells
        newBoard.forEach(x => {
            if (!x.value){
                emptyCells.push(x);
            }
        });

        let valueFound = false;
        let c;
        let startVal = 0;
        c = emptyCells[0];
        let idx = 0;
        while (idx !== emptyCells.length){
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
                if (idx !== 0) { // And its not the first cell
                    c.value = "";
                    idx--;
                    c = emptyCells[idx]
                    startVal = c.value;
                } else {
                    break // Break: while (idx != emptyCells.length)
                }
            } else { // If a value has been found, move onto the next cell
                idx++;
                c = emptyCells[idx]
                startVal = 0;
            }
        }
    }

    removeCells(numCells, stateObj) {
        let newBoard = stateObj.board
        let emptyPos = newBoard.reduce(this.countEmptyPositions,0);

         console.log(numCells);
         while (emptyPos < numCells) {
                const randomNumberRow = Math.floor(Math.random() * 5);
                const randomNumberCol = Math.floor(Math.random() * 5);
                let rowNum;
                let colNum;
                for (var i = 0 ; i < 4 ;i++) { // To ensure symmetrical sudoku
                    switch(i){
                        case 1:
                            rowNum = 8-randomNumberRow;
                            colNum = randomNumberCol
                            break;
                        case 2:
                            rowNum = randomNumberRow
                            colNum = 8-randomNumberCol;
                            break;
                        case 3:
                            rowNum = 8-randomNumberRow
                            colNum = 8-randomNumberCol;
                            break;
                        default:
                            rowNum = randomNumberRow
                            colNum = randomNumberCol;
                    }
                    const cellNumber = rowNum*9 + colNum
                    let removedCell = newBoard[cellNumber];
                    if (removedCell.value !== "") {
                        removedCell.value = "";
                        removedCell.markups = oneToNine.slice();
                        this.updateMarkers(removedCell, newBoard);
                        emptyPos++;
                    }
                    if (randomNumberRow === 4 && randomNumberCol === 4  )  {
                        numCells += 3;
                        break; //Break: for (var i = 0 ; i < 4 ;i++)
                    }
                }
            }

        //TODO: Create indicator in state.board object to distinguish between starting number and number filled in my user
        newBoard.forEach(x => {if (x.value !== ""){
                                    x.start = true;
                                    }
                                });
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
    }

    handleChange(e,num){
        const result = e.target.value.replace(/\D/g, '');
        if (result && result <= 9 && result>0){
            let newBoard = this.state.board.slice();
            newBoard[num].value = result;
            this.setState({board:newBoard});
        }
    }

    restart_sudoku(){
        this.setState({board: this.originalBoard});
    }

    reset(){
        let newState = this.resetBoard();

        this.columnMarkups = this.initialMarkups();
        this.rowMarkups = this.initialMarkups();
        this.boxMarkups = this.initialMarkups();

        this.setState(newState);
    }

    countEmptyPositions(total,cell) {
        return total + (cell.value === "" ? 1 : 0);
    }

    render() {
        let emptyCellPositions = this.state.board.reduce(this.countEmptyPositions, 0);
        return (
        <React.Fragment>
                    <Header emptyPositions={emptyCellPositions}/>
                    <Button label="Create Sudoku" onClick={()=>this.create_sudoku()} />
                    <Button label="Restart current game" onClick={()=>this.restart_sudoku()} />
                    <Button label="Clear all" onClick={()=>this.reset()} />
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
