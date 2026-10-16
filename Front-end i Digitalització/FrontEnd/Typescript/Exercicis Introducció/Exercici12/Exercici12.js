"use strict";
let inputX = document.getElementById("x");
let inputY = document.getElementById("y");
let resultat = document.getElementById("resultat");
function calcular() {
    let puntsXY = {
        x: inputX.valueAsNumber,
        y: inputY.valueAsNumber
    };
    let valorCalcul = Math.sqrt((0 - puntsXY.x) ** 2 + (0 - puntsXY.y) ** 2);
    resultat.innerText = "Distancia des de l'origen: " + valorCalcul.toFixed(2);
}
function mostrar() {
    resultat.innerText = "";
    let estudiant1 = {
        id: 1,
        nom: "Pepe",
        correu: "correu@gmail.com"
    };
    let estudiant2 = {
        id: 2,
        nom: "Pepa",
        correu: "correuPepa@gmail.com"
    };
    let arrayTupla = [estudiant1, estudiant2];
    for (const estudiant of arrayTupla) {
        resultat.innerText += "ESTUDIANT" + estudiant.id + "\nID Estudiant: " + estudiant.id + ",\nNom Estudiant: " + estudiant.nom + ",\nCorreu Estudiant: " + estudiant.correu + "\n";
    }
}
//# sourceMappingURL=Exercici12.js.map