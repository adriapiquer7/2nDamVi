function canviColor(){
    let inputColor : HTMLSelectElement = document.getElementById("inputColors") as HTMLSelectElement
    let resultat : HTMLParagraphElement = document.getElementById("resultat") as HTMLParagraphElement
    let body: HTMLBodyElement = document.body as HTMLBodyElement

    body.style.backgroundColor = inputColor.value
    resultat.innerText="S'ha posat el color "+ inputColor.value;
}