function hora() {
    let inputHora: HTMLInputElement = document.getElementById("inputHora") as HTMLInputElement
    let inputMinuts: HTMLInputElement = document.getElementById("inputMinuts") as HTMLInputElement
    let inputSegons: HTMLInputElement = document.getElementById("inputSegons") as HTMLInputElement
    let resultat: HTMLParagraphElement = document.getElementById("resultat") as HTMLParagraphElement

    let hora = inputHora.valueAsNumber
    let min = inputMinuts.valueAsNumber
    let seg = inputSegons.valueAsNumber

    if(hora>24) {
        hora=Math.trunc(hora%24)
    }
    if(min>60) {
        hora=Math.trunc(hora+(min/60))
        min=min%60
    }
    if (seg>60) {
        min=Math.trunc(min+(seg/60))
        seg=seg%60
    }

    let arrayTupla : readonly [number, number, number] = [hora, min, seg];

    resultat.innerText = arrayTupla[0].toString().padStart(2,"0")+":"+arrayTupla[1].toString().padStart(2,"0")+":"+arrayTupla[2].toString().padStart(2,"0")
}