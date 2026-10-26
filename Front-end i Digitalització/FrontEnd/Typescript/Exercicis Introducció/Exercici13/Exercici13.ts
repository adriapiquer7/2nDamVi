type patinet = {
    fabricant : string,
    model: string,
    carrega : number
}
type bicicleta = {
    fabricant : string,
    model: string
}

type vehicles = patinet | bicicleta

let p : HTMLParagraphElement = document.getElementById("resultat") as HTMLParagraphElement
let arrayVehicles: vehicles[] = []
function mostrar(){
    p.innerText = "Mostrant les dades"
    for (let i = 0; i < arrayVehicles.length; i++) {
        let vehicle : vehicles = arrayVehicles[i];
        if("carrega" in vehicle){
            p.innerText="Fabricant:"+vehicle.fabricant+", Model: "+vehicle.model+", Temps de carrega: "+vehicle.carrega
            p.innerHTML="<br>"
        }
        else{
            p.innerText="Fabricant:"+vehicle.fabricant+", Model: "+vehicle.model
            p.innerHTML="<br>"
        }
    }
}
function guardar(){
    let fabricant : HTMLInputElement =document.getElementById("fabricant") as HTMLInputElement
    let model : HTMLInputElement = document.getElementById("model") as HTMLInputElement
    let tipus : HTMLSelectElement = document.getElementById("tipus") as HTMLSelectElement
    let carrega : HTMLInputElement = document.getElementById("carrega") as HTMLInputElement

    if(tipus.selectedIndex===0){
        let bici:bicicleta ={
            fabricant : fabricant.value,
            model : model.value
        }
        arrayVehicles.push(bici)
    }
    else{
        let pati : patinet = {
            fabricant: fabricant.value,
            model: model.value,
            carrega: carrega.valueAsNumber
        }
        arrayVehicles.push(pati)
    }
    p.innerText="S'han guardat les dades"
}