"use strict";
function canviarOrdre() {
    let txtArea = document.getElementById("txtArea");
    let resultat = document.getElementById("resultat");
    let arrayParaules = [];
    let texto = txtArea.value.split(" ");
    texto.reverse();
    for (let i = 0; i < texto.length; i++) {
        resultat.innerText += texto[i];
    }
}
//# sourceMappingURL=Exercici4.js.map