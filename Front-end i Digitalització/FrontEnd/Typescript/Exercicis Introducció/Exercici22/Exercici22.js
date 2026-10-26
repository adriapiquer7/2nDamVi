export class Videojoc {
    _titol;
    _genere;
    _plataforma;
    _preu;
    constructor(titol, genere, plataforma, preu) {
        this._titol = titol;
        this._genere = genere;
        this._plataforma = plataforma;
        this._preu = preu;
    }
    get titol() {
        return this._titol;
    }
    set titol(value) {
        this._titol = value;
    }
    get genere() {
        return this._genere;
    }
    set genere(value) {
        this._genere = value;
    }
    get plataforma() {
        return this._plataforma;
    }
    set plataforma(value) {
        this._plataforma = value;
    }
    get preu() {
        return this._preu;
    }
    set preu(value) {
        this._preu = value;
    }
    toString() {
        console.log("Titol: " + this._titol + ", Genere: " + this._genere + ", Plataforma: " + this._plataforma + ", Preu: " + this._preu + "€");
    }
}
//# sourceMappingURL=Exercici22.js.map