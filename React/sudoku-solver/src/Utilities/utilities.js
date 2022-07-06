export const oneToNine = [1,2,3,4,5,6,7,8,9];

export function numberToLocation(num){
    const rowNum = cellToRow(num);
    const colNum = cellToCol(num);
    const boxNum = cellToBox(num);
    return {row: rowNum, col: colNum, box: boxNum};
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

export function getCellsFromBox(boxNum){

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

export function getCellsFromRow(row) {
    let arr =   [0,1,2,3,4,5,6,7,8].map(x => (x+9*(row)));
    return arr;
}

function cellToCol(num) {
    return Math.floor( (num)%9);
}

export function getCellsFromCol(col) {
    return [0,1,2,3,4,5,6,7,8].map(x => x*9+col);
}