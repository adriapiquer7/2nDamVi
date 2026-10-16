"use strict";
function calcular() {
    let inputNumero = document.getElementById("inputNumero");
    let text = document.getElementById("resultat");
    let mesura = document.getElementById("mesures");
    let num = inputNumero.valueAsNumber;
    let mesuraTriada = mesura.options.selectedIndex;
    let numCambiat = 0;
    switch (mesuraTriada) {
        case 0:
            numCambiat = Math.round((num / 1.013) * 100) / 100;
            text.innerText = "S'han passat de " + num + " bars a " + numCambiat + " atmosferes";
            break;
        case 1:
            numCambiat = Math.round((num * 1.013) * 100) / 100;
            text.innerText = "S'han passat de " + num + " atmosferes a " + numCambiat + " bars";
            break;
    }
}
//# sourceMappingURL=Exercici2.js.map