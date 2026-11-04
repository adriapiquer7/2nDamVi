package SpringROA.SpringROA.dto;

import java.util.List;

/** Resposta de POST /partida/{id}/daus: la tirada i els recursos rebuts per cada jugador */
public record TiradaResposta(Integer dau1, Integer dau2, Integer total, List<GuanyResposta> guanys) {
}
