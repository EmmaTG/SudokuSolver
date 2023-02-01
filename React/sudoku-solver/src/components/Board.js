import React from 'react';
import {Cell} from './Cell.js';

export function Board(props) {
        return (
            <React.Fragment>
                {renderRow(0, props)}
                {renderRow(9, props)}
                {renderRow(18, props)}
                {renderRow(27, props)}
                {renderRow(36, props)}
                {renderRow(45, props)}
                {renderRow(54, props)}
                {renderRow(63, props)}
                {renderRow(72, props)}
            </React.Fragment>
        );
}

function renderCell(i,props) {
return (<Cell details = {props.cells[i]}
              mode = {props.mode}
              onChange={(e) => props.onChange(e, i)}
              />);
}

function renderRow(startVal,props){
    return (<div className="board-row">
        {renderCell(startVal,props)}
        {renderCell(startVal+1,props)}
        {renderCell(startVal+2,props)}
        {renderCell(startVal+3,props)}
        {renderCell(startVal+4,props)}
        {renderCell(startVal+5,props)}
        {renderCell(startVal+6,props)}
        {renderCell(startVal+7,props)}
        {renderCell(startVal+8,props)}
        </div>
    );
}