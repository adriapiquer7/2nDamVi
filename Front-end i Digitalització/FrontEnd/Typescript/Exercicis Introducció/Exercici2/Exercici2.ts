function calcular(){
    let inputNumero : HTMLInputElement = document.getElementById("inputNumero") as HTMLInputElement
    let text : HTMLParagraphElement = document.getElementById("resultat") as HTMLParagraphElement
    let mesura : HTMLSelectElement = document.getElementById("mesures") as HTMLSelectElement
    let num : number = inputNumero.valueAsNumber;
    let mesuraTriada : number = mesura.options.selectedIndex
    let numCambiat : number = 0;


    switch (mesuraTriada){
        case 0:
            numCambiat=Math.round((num/1.013)*100)/100
            text.innerText= "S'han passat de "+num+" bars a "+numCambiat+" atmosferes";
            break
        case 1:
            numCambiat=Math.round((num*1.013)*100)/100
            text.innerText= "S'han passat de "+num+" atmosferes a "+numCambiat+" bars";
            break
    }
}