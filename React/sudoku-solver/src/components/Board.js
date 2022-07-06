import React from 'react';

import {Cell} from './Cell.js';

export class Board extends React.Component {

    renderCell(i) {
    return (<Cell details = {this.props.cells[i]}
                  mode = {this.props.mode}
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