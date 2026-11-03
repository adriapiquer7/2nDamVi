package SpringROA.SpringROA.dto;

import java.util.EnumMap;
import java.util.Map;

import SpringROA.SpringROA.model.ColorJugador;
import SpringROA.SpringROA.model.Jugador;
import SpringROA.SpringROA.model.TipusRecurs;

/** Resposta de GET /jugador/{id}/recursos: el jugador amb el seu inventari */
public record JugadorRecursosResposta(Integer id, String nom, ColorJugador color, Integer ordreTorn,
		Integer idPartida, Map<TipusRecurs, Integer> recursos, int totalRecursos) {

	/** Es crida DINS d'un mètode @Transactional (l'inventari és LAZY) */
	public static JugadorRecursosResposta de(Jugador j) {
		Map<TipusRecurs, Integer> recursos = new EnumMap<>(TipusRecurs.class);
		for (TipusRecurs t : TipusRecurs.values()) {
			recursos.put(t, j.getQuantitat(t));
		}
		return new JugadorRecursosResposta(j.getId(), j.getNom(), j.getColor(), j.getOrdreTorn(),
				j.getPartida().getId(), recursos, j.getTotalRecursos());
	}
}
