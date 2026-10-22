"use strict";
let p = document.getElementById("resultat");
let arrayVehicles = [];
function mostrar() {
    p.innerText = "Mostrant les dades";
    for (const vehicle of arrayVehicles) {
        if (vehicle.tipus === "bicicleta") {
            if (vehicle.carrega !== 0) {
                p.innerText += `\nBicibleta del fabricant: ${vehicle.fabricant}, model: ${vehicle.model} i temps de carrega: ${vehicle.carrega}`;
            }
            else {
                p.innerText += `\nBicibleta del fabricant: ${vehicle.fabricant}, model: ${vehicle.model}`;
            }
        }
        else {
            p.innerText += `\nPatinet del fabricant: ${vehicle.fabricant}, model: ${vehicle.model} i temps de carrega: ${vehicle.carrega}`;
        }
    }
}
function guardar() {
    let fabricant = document.getElementById("fabricant");
    let model = document.getElementById("model");
    let tipus = document.getElementById("tipus");
    let carrega = document.getElementById("carrega");
    if (tipus.selectedIndex === 0) {
        if (carrega.valueAsNumber !== 0) {
            let bici = {
                fabricant: fabricant.value,
                model: model.value,
                carrega: carrega.valueAsNumber,
                tipus: "bicicleta"
            };
            arrayVehicles.push(bici);
        }
        else {
            let bici = {
                fabricant: fabricant.value,
                model: model.value,
                tipus: "bicicleta"
            };
            arrayVehicles.push(bici);
        }
    }
    else {
        let pati = {
            fabricant: fabricant.value,
            model: model.value,
            carrega: carrega.valueAsNumber,
            tipus: "patinet"
        };
        arrayVehicles.push(pati);
    }
}
//# sourceMappingURL=Exercici13.js.map