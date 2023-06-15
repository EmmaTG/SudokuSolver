import React from 'react';
import {numberToLocation} from '../Utilities/utilities.js';

export function Cell(props) {

    let fontStyle = '';
    const {row, col} = numberToLocation(+props.details.cellIndex);

    if (props.details.start){
       fontStyle = 'font-bold bg-slate-300';
    }
    else if (props.mode === "CHECK" && !props.details.correct){
       fontStyle = 'bg-red-300';
    }
    if ((col+1)%3 === 0) {
       fontStyle += " border-r-2 border-r-black ";
    }
    if ((row+1)%3 === 0){
       fontStyle += " border-b-2 border-b-black ";
    }
    if (col === 0){
       fontStyle += " border-l-2 border-l-black ";
    }
    if (row === 0){
       fontStyle += " border-t-2 border-t-black ";
    }

    let classN = "border-l-2 border-b-2 "+ fontStyle;
    return (
       <input
       className={classN}
       type="text"
       key="this.props.details.cellIndex"
       value={props.details.value}
       style={{background: fontStyle, width:"4vw", height:"4vw", fontSize:"3vw", textAlign:'center'}}
       readOnly={props.details.start || props.mode==='SOLVED'}
       onInput={(e) => props.onChange(e,props.details.cellIndex)}
       >
       </input>
    );
}