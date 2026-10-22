function canviColor() {
    var quadrat = document.getElementById("quadrat");
    if (quadrat.classList.contains("classe1")) {
        quadrat.classList.remove("classe1");
    }
    else {
        quadrat.classList.add("classe1");
    }
}
