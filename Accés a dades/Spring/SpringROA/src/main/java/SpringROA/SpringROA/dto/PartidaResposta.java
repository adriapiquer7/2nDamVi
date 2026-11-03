package SpringROA.SpringROA.dto;

import java.util.ArrayList;
import java.util.List;

import SpringROA.SpringROA.model.EstatPartida;
import SpringROA.SpringROA.model.Hexagon;
import SpringROA.SpringROA.model.Jugador;
import SpringROA.SpringROA.model.Partida;

/** Resposta de POST /partida: la partida amb els jugadors i els hexàgons */
public record PartidaResposta(Integer id, EstatPartida estat, Integer ronda, Integer puntsPerGuanyar,
		Integer idJugadorActiu, List<JugadorResum> jugadors, List<HexagonResum> hexagons) {

	/** Es crida DINS d'un mètode @Transactional */
	public static PartidaResposta de(Partida p, List<Jugador> jugadors, List<Hexagon> hexagons) {
		Integer idLladre = p.getLladre() == null ? null : p.getLladre().getId();
		List<JugadorResum> js = new ArrayList<>();
		for (Jugador j : jugadors) {
			js.add(JugadorResum.de(j));
		}
		List<HexagonResum> hs = new ArrayList<>();
		for (Hexagon h : hexagons) {
			hs.add(HexagonResum.de(h, h.getId().equals(idLladre)));
		}
		Integer idActiu = p.getActiu() == null ? null : p.getActiu().getId();
		return new PartidaResposta(p.getId(), p.getEstat(), p.getRonda(), p.getPuntsPerGuanyar(), idActiu, js, hs);
	}
}
