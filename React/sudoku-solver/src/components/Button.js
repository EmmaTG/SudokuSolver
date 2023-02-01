export function Button(props) {
    if (!props.render){
        return (
            <button className="bg-white hover:bg-gray-100 text-gray-800 font-semibold py-1 px-1 border-gray-600 rounded shadow"
            name="props.label" onClick={props.onClick}>{props.image } {props.label}</button>
        );
    }
    return null;
}