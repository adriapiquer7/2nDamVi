function ActColor() {
    var mostraColor = document.getElementById("mostraColor");
    var vermell = document.getElementById("vermell").valueAsNumber;
    var verd = document.getElementById("verd").valueAsNumber;
    var blau = document.getElementById("blau").valueAsNumber;
    var arrayTupla = [vermell, verd, blau];
    mostraColor.innerText = "rgb(".concat(arrayTupla[0], ",").concat(arrayTupla[1], ",").concat(arrayTupla[2], ")");
    mostraColor.style.backgroundColor = "rgb(".concat(arrayTupla[0], ",").concat(arrayTupla[1], ",").concat(arrayTupla[2], ")");
}
