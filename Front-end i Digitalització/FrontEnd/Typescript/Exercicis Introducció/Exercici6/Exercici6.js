"use strict";
function canviColor() {
    let inputColor = document.getElementById("inputColors");
    let resultat = document.getElementById("resultat");
    let body = document.body;
    body.style.backgroundColor = inputColor.value;
    resultat.innerText = "S'ha posat el color " + inputColor.value;
}
//# sourceMappingURL=Exercici6.js.map