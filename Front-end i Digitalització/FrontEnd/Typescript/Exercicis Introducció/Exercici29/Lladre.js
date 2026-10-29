import { personatge } from "./Personatge.js";
export class lladre extends personatge {
    _boti;
    constructor(nom, boti) {
        super(nom);
        this._boti = boti;
    }
    get boti() {
        return this._boti;
    }
    set boti(value) {
        this._boti = value;
    }
    toString() {
        return super.toString() + ", Boti: " + this._boti;
    }
    _atacar() {
        return super._atacar() + ", Te un boti de: " + this._boti;
    }
}
//# sourceMappingURL=Lladre.js.map