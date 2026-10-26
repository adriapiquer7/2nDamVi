import {Videojoc} from "./Exercici22.js";
let arrayTupla:Videojoc[] = [];
let inputTitol : HTMLInputElement = document.getElementById("titol") as HTMLInputElement
let inputGenere : HTMLInputElement = document.getElementById("genere") as HTMLInputElement
let inputPlataforma : HTMLInputElement = document.getElementById("plataforma") as HTMLInputElement
let inputPreu : HTMLInputElement = document.getElementById("preu") as HTMLInputElement
let botoCrear :HTMLButtonElement = document.getElementById("crearNouJoc") as HTMLButtonElement
let botoMostrar : HTMLButtonElement = document.getElementById("mostrarJoc") as HTMLButtonElement
let p : HTMLParagraphElement = document.getElementById("resultat") as HTMLParagraphElement

botoCrear.onclick = crear;
botoMostrar.onclick= mostrar;

function crear(){
    let videojoc1 : Videojoc = new Videojoc(inputTitol.value, inputGenere.value, inputPlataforma.value, inputPreu.valueAsNumber)
    arrayTupla.push(videojoc1)
}
function mostrar(){
    for (let i:number = 0; i <arrayTupla.length ; i++) {
        p.innerText+=arrayTupla[i].toString()+"\n"
    }
}