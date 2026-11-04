package SpringROA.SpringROA.service;

import java.util.List;

import jakarta.persistence.EntityManager;
import SpringROA.SpringROA.exception.JocException;
import SpringROA.SpringROA.model.EstatPartida;
import SpringROA.SpringROA.model.Jugador;
import SpringROA.SpringROA.model.Partida;

/** Consultes JPQL i regles de puntuació compartides pels DAO. */
final class Consultes {

	private Consultes() {
	}

	static Partida carregarPartida(EntityManager em, int idPartida) {
		Partida p = em.find(Partida.class, idPartida);
		if (p == null) {
			throw new JocException("No existeix la partida " + idPartida);
		}
		return p;
	}

	static Jugador carregarJugador(EntityManager em, int idJugador) {
		Jugador j = em.find(Jugador.class, idJugador);
		if (j == null) {
			throw new JocException("No existeix el jugador " + idJugador);
		}
		return j;
	}

	static List<Jugador> jugadorsOrdenats(EntityManager em, Partida p) {
		return em.createQuery("FROM Jugador j WHERE j.partida = :p ORDER BY j.ordreTorn", Jugador.class)
				.setParameter("p", p).getResultList();
	}

	/** Nombre de construccions d'una classe (Poblat, Ciutat, Carretera) propietat del jugador */
	static long comptar(EntityManager em, String entitat, Jugador j) {
		return em.createQuery("SELECT COUNT(c) FROM " + entitat + " c WHERE c.propietari = :j", Long.class)
				.setParameter("j", j).getSingleResult();
	}

	/** Punts de victòria: 1 per poblat, 2 per ciutat */
	static int calcularPunts(EntityManager em, Jugador j) {
		return (int) (comptar(em, "Poblat", j) + 2 * comptar(em, "Ciutat", j));
	}

	/** Si algun jugador arriba als punts per guanyar, la partida acaba */
	static void comprovarVictoria(EntityManager em, Partida p) {
		if (p.getEstat() != EstatPartida.EN_CURS) {
			return;
		}
		for (Jugador j : jugadorsOrdenats(em, p)) {
			if (calcularPunts(em, j) >= p.getPuntsPerGuanyar()) {
				p.setEstat(EstatPartida.FINALITZADA);
				p.setGuanyador(j);
				return;
			}
		}
	}
}
