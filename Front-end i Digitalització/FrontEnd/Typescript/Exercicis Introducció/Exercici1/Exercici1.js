function validar() {
    var text = document.getElementById("resultat");
    var inputNumero = document.getElementById("inputNumero");
    var numAleatori = Math.floor(Math.random() * 100) + 1;
    var num = inputNumero.valueAsNumber;
    var txt = "";
    if (num > numAleatori || num < 1) {
        txt = "El numero esta fora del rang";
    }
    else {
        txt = "El numero esta dintre del rang";
    }
    text.innerText = txt + " i es el numero: " + num + ", i el random: " + numAleatori;
}
