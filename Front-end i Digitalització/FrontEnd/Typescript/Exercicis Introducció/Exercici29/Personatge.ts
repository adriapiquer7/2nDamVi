export abstract class personatge{
    private _nom : string;
    private _atac : number;
    private _puntsDeVida : number;

    constructor(nom:string) {
        this._nom=nom
        this._atac = Math.floor(Math.random() * 11);
        this._puntsDeVida = Math.floor(Math.random() * 51);
    }
    get nom(): string {
        return this._nom;
    }

    set nom(value: string) {
        this._nom = value;
    }

    get atac(): number {
        return this._atac;
    }

    set atac(value: number) {
        this._atac = value;
    }

    get puntsDeVida(): number {
        return this._puntsDeVida;
    }

    set puntsDeVida(value: number) {
        this._puntsDeVida = value;
    }
    toString(){
        return ("Nom: " + this._nom + ", Atac: " + this._atac + ", Punts de Vida: " + this._puntsDeVida);
    }
    public _atacar() : string{
        return this._nom+" ha atacat amb un atac basic!"
    }
}
