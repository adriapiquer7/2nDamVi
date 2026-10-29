import {personatge} from "./Personatge.js";
export class guerrer extends personatge{
    private _arma : string;

    constructor(nom:string, arma:string) {
        super(nom);
        this._arma=arma;
    }
    get arma(): string {
        return this._arma;
    }
    set arma(value: string) {
        this._arma = value;
    }
    toString(): string {
        return super.toString()+", Arma: "+this._arma;
    }
    public override _atacar(): string {
        return super._atacar()+", Te un arma de poder: "+this._arma;
    }
}
