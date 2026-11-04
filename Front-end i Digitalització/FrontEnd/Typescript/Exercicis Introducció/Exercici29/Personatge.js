export class personatge {
    _nom;
    _atac;
    _puntsDeVida;
    constructor(nom) {
        this._nom = nom;
        this._atac = Math.floor(Math.random() * 11);
        this._puntsDeVida = Math.floor(Math.random() * 51);
    }
    get nom() {
        return this._nom;
    }
    set nom(value) {
        this._nom = value;
    }
    get atac() {
        return this._atac;
    }
    set atac(value) {
        this._atac = value;
    }
    get puntsDeVida() {
        return this._puntsDeVida;
    }
    set puntsDeVida(value) {
        this._puntsDeVida = value;
    }
    toString() {
        return ("Nom: " + this._nom + ", Atac: " + this._atac + ", Punts de Vida: " + this._puntsDeVida);
    }
    _atacar() {
        return this._nom + " ha atacat amb un atac basic!";
    }
}
//# sourceMappingURL=Personatge.js.map