"use strict";
function hora() {
    let inputHora = document.getElementById("inputHora");
    let inputMinuts = document.getElementById("inputMinuts");
    let inputSegons = document.getElementById("inputSegons");
    let resultat = document.getElementById("resultat");
    let hora = inputHora.valueAsNumber;
    let min = inputMinuts.valueAsNumber;
    let seg = inputSegons.valueAsNumber;
    if (hora > 24) {
        hora = Math.trunc(hora % 24);
    }
    if (min > 60) {
        hora = Math.trunc(hora + (min / 60));
        min = min % 60;
    }
    if (seg > 60) {
        min = Math.trunc(min + (seg / 60));
        seg = seg % 60;
    }
    let arrayTupla = [hora, min, seg];
    resultat.innerText = arrayTupla[0].toString().padStart(2, "0") + ":" + arrayTupla[1].toString().padStart(2, "0") + ":" + arrayTupla[2].toString().padStart(2, "0");
}
//# sourceMappingURL=Exercici5.js.map