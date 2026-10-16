type estudiant1 = {
    id:number,
    nom: string,
    correu:string
}
type estudiant2 = {
    x:number,
    y:number
}
const xOrigen:number = 0
const yOrigen:number=0
let inputX : HTMLInputElement =document.getElementById("x") as HTMLInputElement
let inputY : HTMLInputElement =document.getElementById("y") as HTMLInputElement
let resultat : HTMLParagraphElement = document.getElementById("resultat") as HTMLParagraphElement
function calcular(){
    let valorCalcul:number=Math.sqrt((xOrigen-inputX.valueAsNumber)**2+(yOrigen-inputY.valueAsNumber)**2)
    resultat.innerText="Distancia des de l'origen: "+valorCalcul.toFixed(2)
}
function mostrar(){
    let estudiant1 : estudiant1 = {
        id:1,
        nom:"Pepe",
        correu:"correu@gmail.com"
    }
    let estudiant2 : estudiant1 = {
        id:2,
        nom:"Pepa",
        correu:"correuPepa@gmail.com"
    }
    let arrayTupla :readonly [estudiant1, estudiant1] = [estudiant1, estudiant2]
    for (const estudiant of arrayTupla) {
        resultat.innerText+="ESTUDIANT"+estudiant.id+"\nID Estudiant: "+estudiant.id+",\nNom Estudiant: "+estudiant.nom+",\nCorreu Estudiant: "+estudiant.correu+"\n"
    }
}