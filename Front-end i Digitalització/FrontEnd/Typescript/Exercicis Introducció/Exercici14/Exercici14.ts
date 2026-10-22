function ActColor(){
    let mostraColor: HTMLDivElement = document.getElementById("mostraColor") as HTMLDivElement

    let vermell : number = (document.getElementById("vermell") as HTMLInputElement).valueAsNumber
    let verd : number = (document.getElementById("verd") as HTMLInputElement).valueAsNumber
    let blau : number = (document.getElementById("blau") as HTMLInputElement).valueAsNumber
    let arrayTupla : [number, number, number] = [vermell, verd, blau]

    mostraColor.innerText = `rgb(${arrayTupla[0]},${arrayTupla[1]},${arrayTupla[2]})`
    mostraColor.style.backgroundColor = `rgb(${arrayTupla[0]},${arrayTupla[1]},${arrayTupla[2]})`

}