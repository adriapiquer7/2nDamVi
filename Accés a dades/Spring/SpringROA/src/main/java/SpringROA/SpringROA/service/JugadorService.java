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

<<<<<<< Updated upstream
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
=======

	@Transactional(readOnly = true)
	public ResponseEntity<JugadorRecursosResposta> recursos(int idJugador) {
		return ResponseEntity.ok(JugadorRecursosResposta.de(carregarJugador(idJugador)));
	}

	@Transactional
	public ResponseEntity<String> construir(int idJugador, String tipus, int posicio) {
		Jugador j = carregarJugador(idJugador);
		String resultat = switch (tipus.trim().toUpperCase()) {
			case "CARRETERA" -> construirCarretera(j, posicio);
			case "POBLAT" -> construirPoblat(j, posicio);
			case "CIUTAT" -> millorarACiutat(j, posicio);
			default -> throw new JocException(
					"Tipus de construcció no vàlid: " + tipus + " (ha de ser CARRETERA, POBLAT o CIUTAT)",
					HttpStatus.BAD_REQUEST);
		};
		return ResponseEntity.status(HttpStatus.CREATED).body(resultat);
	}

	@Transactional
	public ResponseEntity<String> passar(int idJugador) {
		Jugador j = carregarJugador(idJugador);
		Partida p = j.getPartida();
		if (p.getEstat() == EstatPartida.FINALITZADA) {
			throw new JocException("La partida ja ha finalitzat");
		}
		if (p.getActiu() == null || !p.getActiu().getId().equals(j.getId())) {
			throw new JocException("No és el torn de " + j.getNom());
		}
		int numJugadors = jugadorRepository.findByPartidaOrderByOrdreTorn(p).size();
		int ordre = j.getOrdreTorn();

		if (p.getEstat() == EstatPartida.PREPARACIO) {
			int ronda = p.getRonda();
			if (poblatRepository.countByPropietari(j) < ronda || carreteraRepository.countByPropietari(j) < ronda) {
				throw new JocException("Has de col·locar un poblat i una carretera en aquest torn");
			}
			Jugador seguent;
			if (ronda == 1) {
				if (ordre < numJugadors) {
					seguent = jugadorPerOrdre(p, ordre + 1);
				} else {
					p.setRonda(2);
					seguent = j;
				}
			} else {
				if (ordre > 1) {
					seguent = jugadorPerOrdre(p, ordre - 1);
				} else {
					p.setEstat(EstatPartida.EN_CURS);
					p.setRonda(1);
					seguent = jugadorPerOrdre(p, 1);
>>>>>>> Stashed changes
				}
				pagar(j, COST_CARRETERA);
			}

<<<<<<< Updated upstream
			s.persist(new Carretera(j, a));
			return "Carretera construïda a l'aresta " + idAresta;
		});
	}

	/** Construeix un poblat. Durant la preparació no es paga ni cal connexió. */
	public String construirPoblat(int idJugador, int idVertex) {
		return executar(s -> {
			Jugador j = Consultes.carregarJugador(s, idJugador);
			Partida p = validarTorn(j);
=======
		if (!p.isDausTirats()) {
			throw new JocException("Has de tirar els daus abans de passar torn");
		}
		if (p.isLladrePendent()) {
			throw new JocException("Cal moure el lladre abans de passar torn");
		}
		Jugador seguent;
		if (ordre < numJugadors) {
			seguent = jugadorPerOrdre(p, ordre + 1);
		} else {
			seguent = jugadorPerOrdre(p, 1);
			p.setRonda(p.getRonda() + 1);
		}
		p.setActiu(seguent);
		p.setDausTirats(false);
		return ResponseEntity.ok("Torn passat. Ara juga " + seguent.getNom() + " (ronda " + p.getRonda() + ")");
	}

	private String construirCarretera(Jugador j, int idAresta) {
		Partida p = validarTorn(j);

		Aresta a = arestaRepository.findById(idAresta)
				.orElseThrow(() -> new JocException("L'aresta " + idAresta + " no existeix", HttpStatus.NOT_FOUND));
		if (!a.getPartida().getId().equals(p.getId())) {
			throw new JocException("L'aresta " + idAresta + " pertany a una altra partida");
		}
		if (carreteraRepository.countByAresta(a) > 0) {
			throw new JocException("L'aresta " + idAresta + " ja està ocupada");
		}
		long carreteres = carreteraRepository.countByPropietari(j);
		if (carreteres >= LIMIT_CARRETERES) {
			throw new JocException("Límit de " + LIMIT_CARRETERES + " carreteres assolit");
		}

		if (p.getEstat() == EstatPartida.PREPARACIO) {
			if (carreteres >= p.getRonda()) {
				throw new JocException("Ja has col·locat la carretera d'aquesta ronda");
			}
			if (!tocaPoblatSenseCarreteraPreparacio(j, a)) {
				throw new JocException("En preparació la carretera ha de tocar el teu poblat nou");
			}
		} else {
			if (!connectadaAmbPropi(j, a)) {
				throw new JocException("La carretera no està connectada amb cap construcció o carretera teva");
			}
			pagar(j, COST_CARRETERA);
		}

		carreteraRepository.save(new Carretera(j, a));
		return "Carretera construïda a l'aresta " + idAresta;
	}

	private String construirPoblat(Jugador j, int idVertex) {
		Partida p = validarTorn(j);
>>>>>>> Stashed changes

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

<<<<<<< Updated upstream
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
=======
		poblatRepository.save(new Poblat(j, v));
		if (preparacio && poblats + 1 == 2) {
			repartirRecursInicials(j, v);
		}
		comprovarVictoria(p);
		return "Poblat construït al vèrtex " + idVertex;
	}

	private String millorarACiutat(Jugador j, int idVertex) {
		Partida p = validarTorn(j);
		if (p.getEstat() != EstatPartida.EN_CURS) {
			throw new JocException("Només es pot millorar durant la partida (EN_CURS)");
		}
>>>>>>> Stashed changes

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

<<<<<<< Updated upstream
			// EN_CURS
=======
		pagar(j, COST_CIUTAT);
		poblatRepository.delete(poblat);
		poblatRepository.flush();
		ciutatRepository.save(new Ciutat(j, v));
		comprovarVictoria(p);
		return "Poblat millorat a ciutat al vèrtex " + idVertex;
	}

	private Jugador carregarJugador(int idJugador) {
		return jugadorRepository.findById(idJugador)
				.orElseThrow(() -> new JocException("No existeix el jugador " + idJugador, HttpStatus.NOT_FOUND));
	}

	private Vertex carregarVertex(int idVertex, Partida p) {
		Vertex v = vertexRepository.findById(idVertex)
				.orElseThrow(() -> new JocException("El vèrtex " + idVertex + " no existeix", HttpStatus.NOT_FOUND));
		if (!v.getPartida().getId().equals(p.getId())) {
			throw new JocException("El vèrtex " + idVertex + " pertany a una altra partida");
		}
		return v;
	}

	private Jugador jugadorPerOrdre(Partida p, int ordre) {
		return jugadorRepository.findByPartidaAndOrdreTorn(p, ordre).orElseThrow(
				() -> new JocException("No existeix el jugador amb ordre " + ordre, HttpStatus.INTERNAL_SERVER_ERROR));
	}

	private Partida validarTorn(Jugador j) {
		Partida p = j.getPartida();
		if (p.getEstat() == EstatPartida.FINALITZADA) {
			throw new JocException("La partida ja ha finalitzat");
		}
		if (p.getActiu() == null || !p.getActiu().getId().equals(j.getId())) {
			throw new JocException("No és el torn de " + j.getNom());
		}
		if (p.getEstat() == EstatPartida.EN_CURS) {
>>>>>>> Stashed changes
			if (!p.isDausTirats()) {
				throw new JocException("Has de tirar els daus abans de passar torn");
			}
			if (p.isLladrePendent()) {
				throw new JocException("Cal moure el lladre abans de passar torn");
			}
<<<<<<< Updated upstream
			Jugador seguent;
			if (ordre < numJugadors) {
				seguent = jugadorPerOrdre(s, p, ordre + 1);
			} else {
				seguent = jugadorPerOrdre(s, p, 1);
				p.setRonda(p.getRonda() + 1);
=======
		}
		return p;
	}

	private void pagar(Jugador j, Map<TipusRecurs, Integer> cost) {
		for (Map.Entry<TipusRecurs, Integer> e : cost.entrySet()) {
			if (j.getQuantitat(e.getKey()) < e.getValue()) {
				throw new JocException("Recursos insuficients: falten " + e.getKey());
>>>>>>> Stashed changes
			}
			p.setActiu(seguent);
			p.setDausTirats(false);
			return "Torn passat. Ara juga " + seguent.getNom() + " (ronda " + p.getRonda() + ")";
		});
	}

<<<<<<< Updated upstream
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
=======
	private boolean connectadaAmbPropi(Jugador j, Aresta a) {
		for (Vertex v : List.of(a.getVertexA(), a.getVertexB())) {
			if (assentamentRepository.countByPropietariAndVertex(j, v) > 0
					|| carreteraRepository.carreteresPropiesAmb(j, v) > 0) {
				return true;
			}
		}
		return false;
	}

	private boolean tocaPoblatSenseCarreteraPreparacio(Jugador j, Aresta a) {
		List<Vertex> lliures = assentamentRepository.vertexsSenseCarretera(j);
		return lliures.stream().anyMatch(v -> v.getId().equals(a.getVertexA().getId())
				|| v.getId().equals(a.getVertexB().getId()));
	}

	private void repartirRecursInicials(Jugador j, Vertex v) {
		for (Hexagon h : hexagonVertexRepository.hexagonsDelVertex(v)) {
			TipusRecurs recurs = h.getTerreny().getRecurs();
			if (recurs != null) {
				j.afegirRecurs(recurs, 1);
			}
		}
	}

	private int calcularPunts(Jugador j) {
		return (int) (poblatRepository.countByPropietari(j) + 2 * ciutatRepository.countByPropietari(j));
	}

	private void comprovarVictoria(Partida p) {
		if (p.getEstat() != EstatPartida.EN_CURS) {
			return;
		}
		for (Jugador j : jugadorRepository.findByPartidaOrderByOrdreTorn(p)) {
			if (calcularPunts(j) >= p.getPuntsPerGuanyar()) {
				p.setEstat(EstatPartida.FINALITZADA);
				p.setGuanyador(j);
				return;
			}
		}
>>>>>>> Stashed changes
	}
}
