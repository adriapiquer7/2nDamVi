function canviColor() {
    var inputColor = document.getElementById("inputColors");
    var resultat = document.getElementById("resultat");
    var body = document.body;
    body.style.backgroundColor = inputColor.value;
    resultat.innerText = "S'ha posat el color " + inputColor.value;
}
