package SpringROA.SpringROA.dto;

import SpringROA.SpringROA.model.ColorJugador;
import SpringROA.SpringROA.model.Jugador;

public record JugadorResum(Integer id, String nom, ColorJugador color, Integer ordreTorn) {

	public static JugadorResum de(Jugador j) {
		return new JugadorResum(j.getId(), j.getNom(), j.getColor(), j.getOrdreTorn());
	}
}
