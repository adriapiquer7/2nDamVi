package SpringROA.SpringROA.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Service;

import SpringROA.SpringROA.GenericDao;
import SpringROA.SpringROA.exception.JocException;
import SpringROA.SpringROA.model.Aresta;
import SpringROA.SpringROA.model.Assentament;
import SpringROA.SpringROA.model.Carretera;
import SpringROA.SpringROA.model.Ciutat;
import SpringROA.SpringROA.model.EstatPartida;
import SpringROA.SpringROA.model.Hexagon;
import SpringROA.SpringROA.model.Jugador;
import SpringROA.SpringROA.model.Partida;
import SpringROA.SpringROA.model.Poblat;
import SpringROA.SpringROA.model.RecursJugador;
import SpringROA.SpringROA.model.TipusRecurs;
import SpringROA.SpringROA.model.Vertex;

@Service
public class JugadorService extends GenericDao<Jugador, Integer> {

	static final int LIMIT_CARRETERES = 15;
	static final int LIMIT_POBLATS = 5;
	static final int LIMIT_CIUTATS = 4;

	static final Map<TipusRecurs, Integer> COST_CARRETERA = Map.of(TipusRecurs.FUSTA, 1, TipusRecurs.MAO, 1);
	static final Map<TipusRecurs, Integer> COST_POBLAT = Map.of(TipusRecurs.FUSTA, 1, TipusRecurs.MAO, 1,
			TipusRecurs.LLANA, 1, TipusRecurs.BLAT, 1);
	static final Map<TipusRecurs, Integer> COST_CIUTAT = Map.of(TipusRecurs.BLAT, 2, TipusRecurs.MINERAL, 3);

	/** Construeix una carretera. A preparació no es paga ni es comprova la connexió normal. */
	public String construirCarretera(int idJugador, int idAresta) {
		return executar(s -> {
			Jugador j = Consultes.carregarJugador(s, idJugador);
			Partida p = validarTorn(j);

			Aresta a = s.get(Aresta.class, idAresta);
			if (a == null || !a.getPartida().getId().equals(p.getId())) {
				throw new JocException("L'aresta " + idAresta + " no pertany a aquesta partida");
			}
			if (comptarCarreteresAresta(s, a) > 0) {
				throw new JocException("L'aresta " + idAresta + " ja està ocupada");
			}
			if (Consultes.comptar(s, "Carretera", j) >= LIMIT_CARRETERES) {
				throw new JocException("Límit de " + LIMIT_CARRETERES + " carreteres assolit");
			}

			if (p.getEstat() == EstatPartida.PREPARACIO) {
				if (Consultes.comptar(s, "Carretera", j) >= p.getRonda()) {
					throw new JocException("Ja has col·locat la carretera d'aquesta ronda");
				}
				if (!tocaPoblatSenseCarreteraPreparacio(s, j, a)) {
					throw new JocException("En preparació la carretera ha de tocar el teu poblat nou");
				}
			} else {
				if (!connectadaAmbPropi(s, j, a)) {
					throw new JocException("La carretera no connecta amb cap construcció o carretera teva");
				}
				pagar(j, COST_CARRETERA);
			}

			s.persist(new Carretera(j, a));
			return "Carretera construïda a l'aresta " + idAresta;
		});
	}

	/** Construeix un poblat. Durant la preparació no es paga ni cal connexió. */
	public String construirPoblat(int idJugador, int idVertex) {
		return executar(s -> {
			Jugador j = Consultes.carregarJugador(s, idJugador);
			Partida p = validarTorn(j);

			Vertex v = s.get(Vertex.class, idVertex);
			if (v == null || !v.getPartida().getId().equals(p.getId())) {
				throw new JocException("El vèrtex " + idVertex + " no pertany a aquesta partida");
			}
			if (existeixAssentament(s, v)) {
				throw new JocException("El vèrtex " + idVertex + " ja està ocupat");
			}
			if (hiHaAssentamentAdjacent(s, v)) {
				throw new JocException("Regla de la distància: hi ha un poblat o ciutat en un vèrtex adjacent");
			}

			boolean preparacio = p.getEstat() == EstatPartida.PREPARACIO;
			if (preparacio) {
				if (Consultes.comptar(s, "Poblat", j) >= p.getRonda()) {
					throw new JocException("Ja has col·locat el poblat d'aquesta ronda");
				}
			} else if (!connectadaAmbCarreteraPropia(s, j, v)) {
				throw new JocException("El vèrtex no està connectat amb cap carretera teva");
			}
			if (Consultes.comptar(s, "Poblat", j) >= LIMIT_POBLATS) {
				throw new JocException("Límit de " + LIMIT_POBLATS + " poblats assolit");
			}
			if (!preparacio) {
				pagar(j, COST_POBLAT);
			}

			s.persist(new Poblat(j, v));
			if (preparacio && Consultes.comptar(s, "Poblat", j) == 2) {
				repartirRecursInicials(s, j, v);
			}
			Consultes.comprovarVictoria(s, p);
			return "Poblat construït al vèrtex " + idVertex;
		});
	}

	/** Passa el torn del jugador actiu al següent (seguint l'ordre de serp a la preparació) */
	public String passarTorn(int idJugador) {
		return executar(s -> {
			Jugador j = Consultes.carregarJugador(s, idJugador);
			Partida p = j.getPartida();
			if (p.getEstat() == EstatPartida.FINALITZADA) {
				throw new JocException("La partida ja ha finalitzat");
			}
			if (p.getActiu() == null || !p.getActiu().getId().equals(j.getId())) {
				throw new JocException("No és el torn de " + j.getNom());
			}
			int numJugadors = Consultes.jugadorsOrdenats(s, p).size();
			int ordre = j.getOrdreTorn();

			if (p.getEstat() == EstatPartida.PREPARACIO) {
				int ronda = p.getRonda();
				if (Consultes.comptar(s, "Poblat", j) < ronda || Consultes.comptar(s, "Carretera", j) < ronda) {
					throw new JocException("Has de col·locar un poblat i una carretera en aquest torn");
				}
				Jugador seguent;
				if (ronda == 1) {
					if (ordre < numJugadors) {
						seguent = jugadorPerOrdre(s, p, ordre + 1);
					} else {
						// L'últim de la primera ronda juga també el primer de la segona
						p.setRonda(2);
						seguent = j;
					}
				} else {
					if (ordre > 1) {
						seguent = jugadorPerOrdre(s, p, ordre - 1);
					} else {
						// Acaba la preparació: comença la partida
						p.setEstat(EstatPartida.EN_CURS);
						p.setRonda(1);
						seguent = jugadorPerOrdre(s, p, 1);
					}
				}
				p.setActiu(seguent);
				return "Torn passat. Ara juga " + seguent.getNom();
			}

			// EN_CURS
			if (!p.isDausTirats()) {
				throw new JocException("Has de tirar els daus abans de passar torn");
			}
			if (p.isLladrePendent()) {
				throw new JocException("Cal moure el lladre abans de passar torn");
			}
			Jugador seguent;
			if (ordre < numJugadors) {
				seguent = jugadorPerOrdre(s, p, ordre + 1);
			} else {
				seguent = jugadorPerOrdre(s, p, 1);
				p.setRonda(p.getRonda() + 1);
			}
			p.setActiu(seguent);
			p.setDausTirats(false);
			return "Torn passat. Ara juga " + seguent.getNom() + " (ronda " + p.getRonda() + ")";
		});
	}

	/** Mostra l'inventari de recursos del jugador */
	public void mostrarRecursos(int idJugador) {
		executar(s -> {
			Jugador j = Consultes.carregarJugador(s, idJugador);
			System.out.println("Recursos de " + j.getNom() + ":");
			List<RecursJugador> recursos = s.createQuery(
					"FROM RecursJugador r WHERE r.jugador = :j ORDER BY r.tipus", RecursJugador.class)
					.setParameter("j", j).getResultList();
			for (RecursJugador r : recursos) {
				System.out.printf("  %-8s %d%n", r.getTipus(), r.getQuantitat());
			}
			System.out.println("  Total: " + j.getTotalRecursos());
			return null;
		});
	}
}
