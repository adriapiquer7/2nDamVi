type vehicles = {
    fabricant : string,
    model: string,
    carrega ?: number
    tipus : string
}

let p : HTMLParagraphElement = document.getElementById("resultat") as HTMLParagraphElement
let arrayVehicles: vehicles[] = []
function mostrar(){
    p.innerText = "Mostrant les dades"
    for (const vehicle of arrayVehicles) {
        if (vehicle.tipus === "bicicleta"){
            if (vehicle.carrega!==0){
                p.innerText+= `\nBicibleta del fabricant: ${vehicle.fabricant}, model: ${vehicle.model} i temps de carrega: ${vehicle.carrega}`
            }
            else{
                p.innerText+= `\nBicibleta del fabricant: ${vehicle.fabricant}, model: ${vehicle.model}`
            }
        }
        else{
            p.innerText+= `\nPatinet del fabricant: ${vehicle.fabricant}, model: ${vehicle.model} i temps de carrega: ${vehicle.carrega}`
        }
    }
}
function guardar(){
    let fabricant : HTMLInputElement =document.getElementById("fabricant") as HTMLInputElement
    let model : HTMLInputElement = document.getElementById("model") as HTMLInputElement
    let tipus : HTMLSelectElement = document.getElementById("tipus") as HTMLSelectElement
    let carrega : HTMLInputElement = document.getElementById("carrega") as HTMLInputElement

    if(tipus.selectedIndex===0){
        if (carrega.valueAsNumber!==0){
            let bici:vehicles ={
                fabricant : fabricant.value,
                model : model.value,
                carrega : carrega.valueAsNumber,
                tipus: "bicicleta"
            }
            arrayVehicles.push(bici)
        }
        else{
            let bici:vehicles ={
                fabricant : fabricant.value,
                model : model.value,
                tipus: "bicicleta"
            }
            arrayVehicles.push(bici)
        }
    }
    else{
        let pati : vehicles = {
            fabricant: fabricant.value,
            model: model.value,
            carrega: carrega.valueAsNumber,
            tipus: "patinet"
        }
        arrayVehicles.push(pati)
    }
}