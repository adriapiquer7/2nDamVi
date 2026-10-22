function canviColor(){
    let quadrat :HTMLDivElement = document.getElementById("quadrat") as HTMLDivElement
    if (quadrat.classList.contains("classe1")){
        quadrat.classList.remove("classe1")
    }
    else{
        quadrat.classList.add("classe1")
    }
}