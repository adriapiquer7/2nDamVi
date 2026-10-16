var cercle = document.getElementById("cercle");
var posicio = 0;
function moureDreta() {
    posicio -= 50;
    if (-(window.screen.width / 2) >= posicio) {
        posicio += 50;
    }
    else {
        cercle.style.transform = "translateX(".concat(posicio, "px)");
    }
}
function moureEsquerra() {
    posicio += 50;
    if ((window.screen.width / 2) <= posicio) {
        posicio -= 50;
    }
    else {
        cercle.style.transform = "translateX(".concat(posicio, "px)");
    }
}
function colorAleatori() {
    var num1 = random(255);
    var num2 = random(255);
    var num3 = random(255);
    cercle.style.backgroundColor = "rgb(".concat(num1, ", ").concat(num2, ", ").concat(num3, ")");
}
function random(max) {
    return Math.floor(Math.random() * (max + 1));
}
