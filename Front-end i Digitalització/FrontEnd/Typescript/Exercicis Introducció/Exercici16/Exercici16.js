var diccionari = {};
var inputNomRenec = document.getElementById("renec");
var inputDescRenec = document.getElementById("descripcio");
var p = document.getElementById("resultat");
function Mostrar() {
    p.innerText = "";
    for (var clave in diccionari) {
        p.innerText += clave + ":" + diccionari[clave] + "\n";
    }
}
function Guardar() {
    diccionari[inputNomRenec.value] = inputDescRenec.value;
}
