var inputX = document.getElementById("x");
var inputY = document.getElementById("y");
var resultat = document.getElementById("resultat");
function calcular() {
    var puntsXY = {
        x: inputX.valueAsNumber,
        y: inputY.valueAsNumber
    };
    var valorCalcul = Math.sqrt(Math.pow((0 - puntsXY.x), 2) + Math.pow((0 - puntsXY.y), 2));
    resultat.innerText = "Distancia des de l'origen: " + valorCalcul.toFixed(2);
}
function mostrar() {
    resultat.innerText = "";
    var estudiant1 = {
        id: 1,
        nom: "Pepe",
        correu: "correu@gmail.com"
    };
    var estudiant2 = {
        id: 2,
        nom: "Pepa",
        correu: "correuPepa@gmail.com"
    };
    var arrayTupla = [estudiant1, estudiant2];
    for (var _i = 0, arrayTupla_1 = arrayTupla; _i < arrayTupla_1.length; _i++) {
        var estudiant = arrayTupla_1[_i];
        resultat.innerText += "ESTUDIANT" + estudiant.id + "\nID Estudiant: " + estudiant.id + ",\nNom Estudiant: " + estudiant.nom + ",\nCorreu Estudiant: " + estudiant.correu + "\n";
    }
}
