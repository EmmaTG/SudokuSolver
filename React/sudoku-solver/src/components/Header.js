import React from 'react';

export function Header(props) {
return <React.Fragment>
        <h1 className="container text-3xl font-bold underline">Sudoku</h1>
        <h4> Number of empty positions: {props.emptyPositions}</h4>
        </React.Fragment>
}