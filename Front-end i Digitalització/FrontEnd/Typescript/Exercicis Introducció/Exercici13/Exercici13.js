"use strict";
let p = document.getElementById("resultat");
let arrayVehicles = [];
function mostrar() {
    p.innerText = "Mostrant les dades";
    for (let i = 0; i < arrayVehicles.length; i++) {
        let vehicle = arrayVehicles[i];
        if ("carrega" in vehicle) {
            p.innerText = "Fabricant:" + vehicle.fabricant + ", Model: " + vehicle.model + ", Temps de carrega: " + vehicle.carrega;
            p.innerHTML = "<br>";
        }
        else {
            p.innerText = "Fabricant:" + vehicle.fabricant + ", Model: " + vehicle.model;
            p.innerHTML = "<br>";
        }
    }
}
function guardar() {
    let fabricant = document.getElementById("fabricant");
    let model = document.getElementById("model");
    let tipus = document.getElementById("tipus");
    let carrega = document.getElementById("carrega");
    if (tipus.selectedIndex === 0) {
        let bici = {
            fabricant: fabricant.value,
            model: model.value
        };
        arrayVehicles.push(bici);
    }
    else {
        let pati = {
            fabricant: fabricant.value,
            model: model.value,
            carrega: carrega.valueAsNumber
        };
        arrayVehicles.push(pati);
    }
    p.innerText = "S'han guardat les dades";
}
//# sourceMappingURL=Exercici13.js.map