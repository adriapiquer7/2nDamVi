export class Videojoc{
    private _titol : string;
    private _genere : string;
    private _plataforma :string;
    private _preu : number;

    constructor(titol:string, genere:string, plataforma:string, preu:number) {
        this._titol=titol;
        this._genere=genere;
        this._plataforma=plataforma;
        this._preu=preu;
    }
    get titol(): string {
        return this._titol;
    }

    set titol(value: string) {
        this._titol = value;
    }
    get genere(): string {
        return this._genere;
    }

    set genere(value: string) {
        this._genere = value;
    }
    get plataforma(): string {
        return this._plataforma;
    }

    set plataforma(value: string) {
        this._plataforma = value;
    }
    get preu(): number {
        return this._preu;
    }

    set preu(value: number) {
        this._preu = value;
    }
    toString(){
        return ("Titol: " + this._titol + ", Genere: " + this._genere + ", Plataforma: " + this._plataforma + ", Preu: " + this._preu+"€");
    }
}
