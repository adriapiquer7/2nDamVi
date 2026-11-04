import {personatge} from "./Personatge.js";
export class mag extends personatge{
    private _poderMagic : string;

    constructor(nom:string, poderMagic:string) {
        super(nom);
        this._poderMagic=poderMagic;
    }
    get poderMagic(): string {
        return this._poderMagic;
    }
    set poderMagic(value: string) {
        this._poderMagic = value;
    }
    toString(): string {
        return super.toString()+", Poder Magic: "+this._poderMagic;
    }
    public override _atacar(): string {
        return super._atacar()+", Te un poder Magic de: "+this._poderMagic;
    }
}