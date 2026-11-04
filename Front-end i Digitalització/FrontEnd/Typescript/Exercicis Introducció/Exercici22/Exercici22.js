"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.Videojoc = void 0;
var Videojoc = /** @class */ (function () {
    function Videojoc(titol, genere, plataforma, preu) {
        this._titol = titol;
        this._genere = genere;
        this._plataforma = plataforma;
        this._preu = preu;
    }
    Object.defineProperty(Videojoc.prototype, "titol", {
        get: function () {
            return this._titol;
        },
        set: function (value) {
            this._titol = value;
        },
        enumerable: false,
        configurable: true
    });
    Object.defineProperty(Videojoc.prototype, "genere", {
        get: function () {
            return this._genere;
        },
        set: function (value) {
            this._genere = value;
        },
        enumerable: false,
        configurable: true
    });
    Object.defineProperty(Videojoc.prototype, "plataforma", {
        get: function () {
            return this._plataforma;
        },
        set: function (value) {
            this._plataforma = value;
        },
        enumerable: false,
        configurable: true
    });
    Object.defineProperty(Videojoc.prototype, "preu", {
        get: function () {
            return this._preu;
        },
        set: function (value) {
            this._preu = value;
        },
        enumerable: false,
        configurable: true
    });
    Videojoc.prototype.toString = function () {
        return ("Titol: " + this._titol + ", Genere: " + this._genere + ", Plataforma: " + this._plataforma + ", Preu: " + this._preu + "€");
    };
    return Videojoc;
}());
exports.Videojoc = Videojoc;
