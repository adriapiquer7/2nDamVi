"use strict";
function validar() {
    let text = document.getElementById("resultat");
    let inputNumero = document.getElementById("inputNumero");
    let numAleatori = Math.floor(Math.random() * 100) + 1;
    let num = inputNumero.valueAsNumber;
    let txt = "";
    if (num > numAleatori || num < 1) {
        txt = "El numero esta fora del rang";
    }
    else {
        txt = "El numero esta dintre del rang";
    }
    text.innerText = txt + " i es el numero: " + num + ", i el random: " + numAleatori;
}
//# sourceMappingURL=Exercici1.js.map