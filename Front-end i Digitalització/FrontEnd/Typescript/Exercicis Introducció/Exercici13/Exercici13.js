var p = document.getElementById("resultat");
var arrayVehicles = [];
function mostrar() {
    p.innerText = "Mostrant les dades";
    for (var i = 0; i < arrayVehicles.length; i++) {
        var vehicle = arrayVehicles[i];
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
    var fabricant = document.getElementById("fabricant");
    var model = document.getElementById("model");
    var tipus = document.getElementById("tipus");
    var carrega = document.getElementById("carrega");
    if (tipus.selectedIndex === 0) {
        var bici = {
            fabricant: fabricant.value,
            model: model.value
        };
        arrayVehicles.push(bici);
    }
    else {
        var pati = {
            fabricant: fabricant.value,
            model: model.value,
            carrega: carrega.valueAsNumber
        };
        arrayVehicles.push(pati);
    }
    p.innerText = "S'han guardat les dades";
}
