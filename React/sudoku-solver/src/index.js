import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import reportWebVitals from './reportWebVitals';

const oneToNine = [1,2,3,4,5,6,7,8,9];

function Header() {
return <h1>Sudoku Solver</h1>;
}

function handleCreate() {
console.log("Create");
}

function handleSolve() {
console.log("Solve");
}

function handleClearAll() {
console.log("Clear All");
}

function Buttons() {
return (
<div>
    <button name="Create" onClick={x=>handleCreate()}> Create </button>
    <button name="Solve" onClick={x=>handleSolve()}> Solve </button>
    <button name="Create" onClick={x=>handleClearAll()}> Clear All </button>
</div>
);
}

function cellToRow(num) {
    return Math.floor(num/9);
}

function cellToCol(num) {
    return Math.floor(num%9);
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

    constructor(props) {
        super(props);
        this.state = {
            row: cellToRow(props.number),
            col: cellToCol(props.number),
            box: cellToBox(props.number),
            value: null,
            markups: oneToNine,
        };
    }

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
            onInput={(e) => this.props.onChange(e,this.props.number)}
            >
            </input>
        );
    }
}

class Board extends React.Component {

    renderSquare(i) {
    return(
        <Cell number={i}
        onChange={(e) => this.props.onChange(e, i)}
        />
    );
    }

    renderRow(startVal){
        return (<div className="board-row">
              {this.renderSquare(startVal)}
              {this.renderSquare(startVal+1)}
              {this.renderSquare(startVal+2)}
              {this.renderSquare(startVal+3)}
              {this.renderSquare(startVal+4)}
              {this.renderSquare(startVal+5)}
              {this.renderSquare(startVal+6)}
              {this.renderSquare(startVal+7)}
              {this.renderSquare(startVal+8)}
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
        this.state = {
            emptyPositions: 81,
            columnMarkups: this.initialMarkups(),
            rowMarkups: this.initialMarkups(),
            boxMarkups: this.initialMarkups(),
        };

//        for (var i=0; i<81; i++){
//          this.board
//        }
    }


    initialMarkups(){
        let markupMap = new Map()
        for (var i = 1; i <= 9; i++){
            markupMap.set(i,oneToNine);
        }
        return markupMap;
    }

    create_sudoku() {
        //TODO:: After solving sudoku, values must removed to create one.
        // 1. fill boxes 0, 4 and 8 with number 1-9
        // 2. simplySolve the sudoku
        // 3. remove a certain number of positions
        // 4. display sudoku
    }

    solve_sudoku() {
        //TODO:: Implement simple solve algorithm
        // First create flat list of all Cells where value = null;
        this.state.board.forEach()
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
                    <Header/>
                    <Buttons/>
                    <Board
                        cells={this.board}
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
