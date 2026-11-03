package SpringROA.SpringROA.dto;

import java.util.List;

import SpringROA.SpringROA.model.TipusTerreny;

/** Un hexàgon del tauler amb les construccions als seus vèrtexs (GET /partida/{id}/tauler) */
public record TaulerHexagonResposta(Integer id, Integer codi, TipusTerreny terreny, Integer numero, boolean lladre,
		List<ConstruccioResposta> construccions) {
}
