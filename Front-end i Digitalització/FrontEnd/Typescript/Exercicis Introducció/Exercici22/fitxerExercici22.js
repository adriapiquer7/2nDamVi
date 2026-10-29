"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var Exercici22_js_1 = require("./Exercici22.js");
var arrayTupla = [];
var inputTitol = document.getElementById("titol");
var inputGenere = document.getElementById("genere");
var inputPlataforma = document.getElementById("plataforma");
var inputPreu = document.getElementById("preu");
var botoCrear = document.getElementById("crearNouJoc");
var botoMostrar = document.getElementById("mostrarJoc");
var p = document.getElementById("resultat");
botoCrear.onclick = crear;
botoMostrar.onclick = mostrar;
function crear() {
    var videojoc1 = new Exercici22_js_1.Videojoc(inputTitol.value, inputGenere.value, inputPlataforma.value, inputPreu.valueAsNumber);
    arrayTupla.push(videojoc1);
}
function mostrar() {
    for (var i = 0; i < arrayTupla.length; i++) {
        p.innerText += arrayTupla[i].toString() + "\n";
    }
}
