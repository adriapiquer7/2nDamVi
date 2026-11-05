import { Videojoc } from "./Exercici22.js";
let arrayTupla = [];
let inputTitol = document.getElementById("titol");
let inputGenere = document.getElementById("genere");
let inputPlataforma = document.getElementById("plataforma");
let inputPreu = document.getElementById("preu");
let botoCrear = document.getElementById("crearNouJoc");
let botoMostrar = document.getElementById("mostrarJoc");
let p = document.getElementById("resultat");
botoCrear.onclick = crear;
botoMostrar.onclick = mostrar;
function crear() {
    let videojoc1 = new Videojoc(inputTitol.value, inputGenere.value, inputPlataforma.value, inputPreu.valueAsNumber);
    arrayTupla.push(videojoc1);
}
function mostrar() {
    for (let i = 0; i < arrayTupla.length; i++) {
        p.innerText += arrayTupla[i].toString() + "\n";
    }
}
//# sourceMappingURL=fitxerExercici22.js.map