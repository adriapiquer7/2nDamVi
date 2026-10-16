"use strict";
const xOrigen = 0;
const yOrigen = 0;
let inputX = document.getElementById("x");
let inputY = document.getElementById("y");
let resultat = document.getElementById("resultat");
function calcular() {
    let valorCalcul = Math.sqrt((xOrigen - inputX.valueAsNumber) ** 2 + (yOrigen - inputY.valueAsNumber) ** 2);
    resultat.innerText = "Distancia des de l'origen: " + valorCalcul.toFixed(2);
}
function mostrar() {
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