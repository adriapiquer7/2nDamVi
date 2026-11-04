import { guerrer } from "./Guerrer.js";
import { mag } from "./Mag.js";
import { lladre } from "./Lladre.js";
let botoJugador1 = document.getElementById("guardarJugador1");
let botoJugador2 = document.getElementById("guardarJugador2");
let botoJugar = document.getElementById("jugar");
let resultat = document.getElementById("resultat");
let inputNom = document.getElementById("nomJugador");
let arrayJugadors = [];
botoJugador1.onclick = crearJugador1;
botoJugador2.onclick = crearJugador2;
botoJugar.onclick = jugar;
function crearJugador1() {
    let numRandom = Math.floor(Math.random() * 3);
    switch (numRandom) {
        case 0:
            let guerrer1 = new guerrer(inputNom.value, "Cuchillo");
            arrayJugadors.push(guerrer1);
            resultat.innerText = "S'ha creat el guerrer: " + guerrer1.toString();
            break;
        case 1:
            let mag1 = new mag(inputNom.value, "Foc");
            arrayJugadors.push(mag1);
            resultat.innerText = "S'ha creat el mag: " + mag1.toString();
            break;
        default:
            let lladre1 = new lladre(inputNom.value, Math.floor(Math.random() * 11));
            arrayJugadors.push(lladre1);
            resultat.innerText = "S'ha creat el lladre: " + lladre1.toString();
            break;
    }
}
function crearJugador2() {
    let numRandom = Math.floor(Math.random() * 3);
    switch (numRandom) {
        case 0:
            let guerrer2 = new guerrer(inputNom.value, "Cuchillo");
            arrayJugadors.push(guerrer2);
            resultat.innerText = "S'ha creat el guerrer: " + guerrer2.toString();
            break;
        case 1:
            let mag2 = new mag(inputNom.value, "Foc");
            arrayJugadors.push(mag2);
            resultat.innerText = "S'ha creat el mag: " + mag2.toString();
            break;
        default:
            let lladre2 = new lladre(inputNom.value, Math.floor(Math.random() * 11));
            arrayJugadors.push(lladre2);
            resultat.innerText = "S'ha creat el lladre: " + lladre2.toString();
            break;
    }
}
function jugar() {
    let torn = Math.floor(Math.random() * 2);
    if (arrayJugadors.length == 2) {
        while (arrayJugadors[0].puntsDeVida > 0 && arrayJugadors[1].puntsDeVida > 0) {
            switch (torn) {
                case 0:
                    let ataca = Math.floor(Math.random() * 2);
                    switch (ataca) {
                        case 0:
                            resultat.innerText += "\n" + arrayJugadors[0].nom + ", ataca a " + arrayJugadors[1].nom + ", amb el seu atac i li fa " + arrayJugadors[0].atac + " de mal," + arrayJugadors[1].nom + ", te:" + (arrayJugadors[1].puntsDeVida - arrayJugadors[0].atac) + "Punts de vida!";
                            arrayJugadors[1].puntsDeVida -= arrayJugadors[0].atac;
                            break;
                        default:
                            resultat.innerText += "\nEl personatge " + arrayJugadors[0].nom + " no ataca!";
                            break;
                    }
                    torn = 1;
                    break;
                default:
                    let ataca2 = Math.floor(Math.random() * 2);
                    switch (ataca2) {
                        case 0:
                            resultat.innerText += "\n" + arrayJugadors[1].nom + ", ataca a " + arrayJugadors[0].nom + ", amb el seu atac i li fa " + arrayJugadors[1].atac + " de mal," + arrayJugadors[0].nom + ", te:" + (arrayJugadors[0].puntsDeVida - arrayJugadors[1].atac) + "Punts de vida!";
                            arrayJugadors[0].puntsDeVida -= arrayJugadors[1].atac;
                            break;
                        default:
                            resultat.innerText += "\nEl personatge " + arrayJugadors[1].nom + " no ataca!";
                            break;
                    }
                    torn = 0;
                    break;
            }
        }
        if (arrayJugadors[0].puntsDeVida <= 0) {
            resultat.innerText += "\nHa guanyat el jugador:" + arrayJugadors[1].nom;
        }
        else if (arrayJugadors[1].puntsDeVida <= 0) {
            resultat.innerText += "\nHa guanyat el jugador:" + arrayJugadors[0].nom;
        }
    }
}
//# sourceMappingURL=MainExercici29.js.map