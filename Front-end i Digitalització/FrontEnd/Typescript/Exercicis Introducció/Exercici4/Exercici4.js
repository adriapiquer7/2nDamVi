function canviarOrdre() {
    var txtArea = document.getElementById("txtArea");
    var resultat = document.getElementById("resultat");
    var arrayParaules = [];
    var texto = txtArea.value.split(" ");
    texto.reverse();
    for (var i = 0; i < texto.length; i++) {
        resultat.innerText += texto[i];
    }
}
