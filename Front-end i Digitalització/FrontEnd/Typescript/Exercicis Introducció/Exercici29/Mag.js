import { personatge } from "./Personatge.js";
export class mag extends personatge {
    _poderMagic;
    constructor(nom, poderMagic) {
        super(nom);
        this._poderMagic = poderMagic;
    }
    get poderMagic() {
        return this._poderMagic;
    }
    set poderMagic(value) {
        this._poderMagic = value;
    }
    toString() {
        return super.toString() + ", Poder Magic: " + this._poderMagic;
    }
    _atacar() {
        return super._atacar() + ", Te un poder Magic de: " + this._poderMagic;
    }
}
//# sourceMappingURL=Mag.js.map