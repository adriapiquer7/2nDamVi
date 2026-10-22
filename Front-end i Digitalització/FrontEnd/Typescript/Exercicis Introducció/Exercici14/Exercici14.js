"use strict";
function ActColor() {
    let mostraColor = document.getElementById("mostraColor");
    let vermell = document.getElementById("vermell").valueAsNumber;
    let verd = document.getElementById("verd").valueAsNumber;
    let blau = document.getElementById("blau").valueAsNumber;
    let arrayTupla = [vermell, verd, blau];
    mostraColor.innerText = `rgb(${arrayTupla[0]},${arrayTupla[1]},${arrayTupla[2]})`;
    mostraColor.style.backgroundColor = `rgb(${arrayTupla[0]},${arrayTupla[1]},${arrayTupla[2]})`;
}
//# sourceMappingURL=Exercici14.js.map