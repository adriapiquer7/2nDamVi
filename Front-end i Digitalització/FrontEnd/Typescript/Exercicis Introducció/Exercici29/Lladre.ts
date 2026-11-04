import {personatge} from "./Personatge.js";
export class lladre extends personatge{
    private _boti : number;

    constructor(nom:string, boti:number) {
        super(nom);
        this._boti=boti;
    }
    get boti(): number {
        return this._boti;
    }
    set boti(value: number) {
        this._boti = value;
    }
    toString(): string {
        return super.toString()+", Boti: "+this._boti;
    }
    public override _atacar(): string {
        return super._atacar()+", Te un boti de: "+this._boti;
    }
}