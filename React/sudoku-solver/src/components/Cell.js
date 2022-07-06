import React from 'react';
import {numberToLocation} from '../Utilities/utilities.js';

export class Cell extends React.Component {

           render () {
               let fontStyle = '';
               const {row, col, box} = numberToLocation(+this.props.details.cellIndex);

               if (this.props.details.start){
                   fontStyle = 'font-bold bg-slate-300';
               }
               else if (this.props.mode === "CHECK" && !this.props.details.correct){
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

               let classN = "border-2 w-10 h-15 "+ fontStyle;
               return (
                   <input
                   className={classN}
                   type="text"
                   key="this.props.details.cellIndex"
                   value={this.props.details.value}
                   style={{background: fontStyle}}
                   readOnly={this.props.details.start || this.props.mode==='SOLVED'}
                   onInput={(e) => this.props.onChange(e,this.props.details.cellIndex)}
                   >
                   </input>
               );
           }
       }