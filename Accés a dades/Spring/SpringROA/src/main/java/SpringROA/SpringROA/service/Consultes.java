package SpringROA.SpringROA.service;

import java.util.List;

import org.hibernate.Session;

import SpringROA.SpringROA.exception.JocException;
import SpringROA.SpringROA.model.EstatPartida;
import SpringROA.SpringROA.model.Jugador;
import SpringROA.SpringROA.model.Partida;

/** Consultes JPQL i regles de puntuació compartides pels DAO. */
final class Consultes {

	private Consultes() {
	}

	static Partida carregarPartida(Session s, int idPartida) {
		Partida p = s.get(Partida.class, idPartida);
		if (p == null) {
			throw new JocException("No existeix la partida " + idPartida);
		}
		return p;
	}

	static Jugador carregarJugador(Session s, int idJugador) {
		Jugador j = s.get(Jugador.class, idJugador);
		if (j == null) {
			throw new JocException("No existeix el jugador " + idJugador);
		}
		return j;
	}

	static List<Jugador> jugadorsOrdenats(Session s, Partida p) {
		return s.createQuery("FROM Jugador j WHERE j.partida = :p ORDER BY j.ordreTorn", Jugador.class)
				.setParameter("p", p).getResultList();
	}

	/** Nombre de construccions d'una classe (Poblat, Ciutat, Carretera) propietat del jugador */
	static long comptar(Session s, String entitat, Jugador j) {
		return s.createQuery("SELECT COUNT(c) FROM " + entitat + " c WHERE c.propietari = :j", Long.class)
				.setParameter("j", j).getSingleResult();
	}

	/** Punts de victòria: 1 per poblat, 2 per ciutat */
	static int calcularPunts(Session s, Jugador j) {
		return (int) (comptar(s, "Poblat", j) + 2 * comptar(s, "Ciutat", j));
	}

	/** Si algun jugador arriba als punts per guanyar, la partida acaba */
	static void comprovarVictoria(Session s, Partida p) {
		if (p.getEstat() != EstatPartida.EN_CURS) {
			return;
		}
		for (Jugador j : jugadorsOrdenats(s, p)) {
			if (calcularPunts(s, j) >= p.getPuntsPerGuanyar()) {
				p.setEstat(EstatPartida.FINALITZADA);
				p.setGuanyador(j);
				return;
			}
		}
	}
}
