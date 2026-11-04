package SpringROA.SpringROA.dto;

import java.util.Map;

import SpringROA.SpringROA.model.TipusRecurs;

/** Recursos que ha rebut un jugador en una tirada */
public record GuanyResposta(String jugador, Map<TipusRecurs, Integer> recursos) {
}
