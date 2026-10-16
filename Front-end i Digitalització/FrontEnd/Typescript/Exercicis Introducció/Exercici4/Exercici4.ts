function canviarOrdre(){
    let txtArea : HTMLTextAreaElement = document.getElementById("txtArea") as HTMLTextAreaElement
    let resultat :HTMLParagraphElement = document.getElementById("resultat") as HTMLParagraphElement
    let arrayParaules: string[] = [];

    let texto = txtArea.value.split(" ")
    texto.reverse()
    for (let i = 0; i < texto.length; i++) {
        resultat.innerText+= texto[i];
    }
}