type dic = Record<string, string>
let diccionari: dic = {}

let inputNomRenec: HTMLInputElement = document.getElementById("renec") as HTMLInputElement
let inputDescRenec: HTMLInputElement = document.getElementById("descripcio") as HTMLInputElement
let p: HTMLParagraphElement = document.getElementById("resultat") as HTMLParagraphElement

function Mostrar() {
    p.innerText = ""
    for (const clave in diccionari) {
        p.innerText += clave + ":" + diccionari[clave] + "\n"
    }
}

function Guardar() {
    diccionari[inputNomRenec.value] = inputDescRenec.value;
}
