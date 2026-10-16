function validar(){
    let text : HTMLParagraphElement = document.getElementById("resultat") as HTMLParagraphElement
    let inputNumero : HTMLInputElement = document.getElementById("inputNumero") as HTMLInputElement
    let numAleatori : number = Math.floor(Math.random()*100)+1;

    let num : number = inputNumero.valueAsNumber;
    let txt : String ="";
    if (num>numAleatori || num<1)
    {
        txt="El numero esta fora del rang"
    }else{
        txt="El numero esta dintre del rang"
    }

    text.innerText= txt+" i es el numero: "+num+", i el random: "+numAleatori
}