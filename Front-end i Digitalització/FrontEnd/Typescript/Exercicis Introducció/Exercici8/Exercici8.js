"use strict";
let cercle = document.getElementById("cercle");
let posicio = 0;
function moureDreta() {
    posicio -= 50;
    if (-(window.screen.width / 2) >= posicio) {
        posicio += 50;
    }
    else {
        cercle.style.transform = `translateX(${posicio}px)`;
    }
}
function moureEsquerra() {
    posicio += 50;
    if ((window.screen.width / 2) <= posicio) {
        posicio -= 50;
    }
    else {
        cercle.style.transform = `translateX(${posicio}px)`;
    }
}
function colorAleatori() {
    let num1 = random(255);
    let num2 = random(255);
    let num3 = random(255);
    cercle.style.backgroundColor = `rgb(${num1}, ${num2}, ${num3})`;
}
function random(max) {
    return Math.floor(Math.random() * (max + 1));
}
//# sourceMappingURL=Exercici8.js.map