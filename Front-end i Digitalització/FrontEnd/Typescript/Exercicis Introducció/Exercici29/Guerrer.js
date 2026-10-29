import { personatge } from "./Personatge.js";
export class guerrer extends personatge {
    _arma;
    constructor(nom, arma) {
        super(nom);
        this._arma = arma;
    }
    get arma() {
        return this._arma;
    }
    set arma(value) {
        this._arma = value;
    }
    toString() {
        return super.toString() + ", Arma: " + this._arma;
    }
    _atacar() {
        return super._atacar() + ", Te un arma de poder: " + this._arma;
    }
}
//# sourceMappingURL=Guerrer.js.map