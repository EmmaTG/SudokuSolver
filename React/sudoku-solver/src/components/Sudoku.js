import React from 'react';
import '../index.css';

import {BsFillPencilFill} from 'react-icons/bs';

import {Header} from './Header.js';
import {Button} from './Button.js';
import {Board} from './Board.js';
import {numberToLocation, getCellsFromRow, getCellsFromCol, getCellsFromBox,oneToNine} from '../Utilities/utilities.js';

export class Sudoku extends React.Component {

    constructor(props) {
        super(props);
        this.state = {board: Array(81).fill(null).map((x,num) => {
                                        return ({
                                            cellIndex: num,
                                            markups: oneToNine.slice(),
                                            value: "",
                                            start: false,
                                            correct: 1,
                                        })
                                    }),
                     mode: "INPUT"
                 };
        this.handleChange = this.handleChange.bind(this);
        this.create_sudoku = this.create_sudoku.bind(this);
        this.restart_sudoku = this.restart_sudoku.bind(this);
        this.reset = this.reset.bind(this);
        this.handleShowSolution = this.handleShowSolution.bind(this);
        this.check = this.check.bind(this);
        this.resetVariables();
    }

    resetVariables(){
        this.originalBoard = null;
        this.solutionBoard = null;

        this.columnMarkups = this.initialMarkups();
        this.rowMarkups = this.initialMarkups();
        this.boxMarkups = this.initialMarkups();

    }

    initialMarkups(){
        let markupMap = Array(9).fill(oneToNine.slice());
        return markupMap;
    }

    getEmptyPositions(stateBoard){
        return stateBoard.reduce(this.countEmptyPositions,0);
    }

    /*
    Get affected cells in by the addition of a value current cell curCell
    */
    getAffectedCells(curCell){
        const {row, col, box} = numberToLocation(curCell.cellIndex);

        let affectedCellNums = getCellsFromRow(row);
        affectedCellNums = affectedCellNums.concat(getCellsFromCol(col));
        affectedCellNums = affectedCellNums.concat(getCellsFromBox(box));

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
        const {row, col, box} = numberToLocation(cell.cellIndex);

        idxLocation = this.boxMarkups[box].indexOf(cell.value);
        this.boxMarkups[box].splice(idxLocation,1);

        idxLocation = this.rowMarkups[row].indexOf(cell.value);
        this.rowMarkups[row].splice(idxLocation,1);

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
        newBoard.forEach(x => {x.value = ''; x.start = 0; x.correct = 1;});

        // 1. fill boxes 0, 4 and 8 with number 1-9
        [0,4,8].forEach( (x) => {
            var cells = getCellsFromBox(x);
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
        this.solutionBoard = JSON.parse(JSON.stringify(stateObj.board));

        // 3. remove a certain number of positions
        this.removeCells(42,stateObj);
        this.originalBoard = JSON.parse(JSON.stringify(stateObj.board));
        this.originalBoard.forEach(x => x.start = x.value !== "");
        this.solutionBoard.forEach((x,num) => x.start = this.originalBoard[num].start);
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
        let emptyPos = this.getEmptyPositions(newBoard);

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
                        break; //Break: for (var i = 0 ; i < 4 ;i++)
                    }
                }
            }

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

    handleChange(e,num){
        let boardMode = "INPUT";
        const result = e.target.value.replace(/\D/g, '');
        if ( (result && result <= 9 && result>0) || e.target.value === ''){
            let newBoard = this.state.board.slice();
            newBoard[num].value = result;
            if (this.solutionBoard){
               newBoard[num].correct = (+result === this.solutionBoard[num].value);
            }
            const emptyPositions = this.getEmptyPositions(newBoard);
            if (emptyPositions === 0){
                const numberIncorrect = newBoard.reduce((total,cell) => total + (cell.correct ? 0 : 1), 0)
                if (numberIncorrect === 0){
                    boardMode= "SOLVED";
                } else {
                    alert("You have " + numberIncorrect + " incorrect entries!!");
                }
            }
            this.setState({board:newBoard,mode:boardMode});
        }
    }

    handleShowSolution(){
    if (this.solutionBoard){
        this.setState({board:this.solutionBoard,mode:'SOLVED'})
    } else {
        // TODO: Set up candidate check solution method (To be run before simply solve)
//        let newBoard = JSON.parse(JSON.stringify(this.state.board));
//        this.simplySolve(newBoard);
//        this.setState({board:newBoard,mode:'SOLVED'})
    }
    }

    restart_sudoku(){
        this.setState({board: JSON.parse(JSON.stringify(this.originalBoard))});
    }

    reset(){
        let newBoard = JSON.parse(JSON.stringify(this.state.board));
        newBoard.forEach(x => {x.value = ''; x.start = 0; x.correct=1;})
        this.resetVariables();

        this.setState({board: newBoard, mode:'INPUT'});
    }

    check(){
        let currentBoard = this.state.board.slice();
        for (let i = 0; i < currentBoard.length; i++){
            let cell = currentBoard[i];
            if (!cell.start && cell.value !== ""){
                cell.correct = (+cell.value === this.solutionBoard[i].value);
            }
        }
        this.setState({board: currentBoard, mode:"CHECK"})
    }

    countEmptyPositions(total,cell) {
        return total + (cell.value === "" ? 1 : 0);
    }

    render() {
        let emptyCellPositions = this.getEmptyPositions(this.state.board);
        return (
        <div className="container mx-auto mt-3 font-thin">
                    <Header emptyPositions={emptyCellPositions}/>
                    <Button label="Create Sudoku" onClick={()=>this.create_sudoku()} image={<BsFillPencilFill className="inline-block "/>}/>
                    <Button label="Restart current game" onClick={()=>this.restart_sudoku()} render={!this.originalBoard} />
                    <Button label="Clear all" onClick={()=>this.reset()} render={emptyCellPositions>=81} />
                    <Board
                        cells={this.state.board}
                        mode={this.state.mode}
                        onChange={(e,num) => this.handleChange(e,num)}/>
                    <Button label="Show solution" onClick={()=>this.handleShowSolution()} render={!this.solutionBoard} />
                    <Button label="Check" onClick={()=>this.check()} render={!this.solutionBoard}  />
        </div>
                )
    }
}