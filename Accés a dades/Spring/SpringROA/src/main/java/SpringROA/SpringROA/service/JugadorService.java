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

	/** Comprova que la partida és activa i que és el torn del jugador. Retorna la partida. */
	private static Partida validarTorn(Jugador j) {
		Partida p = j.getPartida();
		if (p.getEstat() == EstatPartida.FINALITZADA) {
			throw new JocException("La partida ja ha finalitzat");
		}
		if (p.getActiu() == null || !p.getActiu().getId().equals(j.getId())) {
			throw new JocException("No és el torn de " + j.getNom());
		}
		if (p.getEstat() == EstatPartida.EN_CURS) {
			if (!p.isDausTirats()) {
				throw new JocException("Cal tirar els daus abans de fer altres accions");
			}
			if (p.isLladrePendent()) {
				throw new JocException("Cal moure el lladre abans de continuar");
			}
		}
		return p;
	}

	/** Descompta els recursos del cost, o llança error si no n'hi ha prou */
	private static void pagar(Jugador j, Map<TipusRecurs, Integer> cost) {
		for (Map.Entry<TipusRecurs, Integer> e : cost.entrySet()) {
			if (j.getQuantitat(e.getKey()) < e.getValue()) {
				throw new JocException("Recursos insuficients: falten " + e.getKey());
			}
		}
		for (Map.Entry<TipusRecurs, Integer> e : cost.entrySet()) {
			j.afegirRecurs(e.getKey(), -e.getValue());
		}
	}

	private static boolean existeixAssentament(Session s, Vertex v) {
		return s.createQuery("SELECT COUNT(x) FROM Assentament x WHERE x.vertex = :v", Long.class)
				.setParameter("v", v).getSingleResult() > 0;
	}

	/** Regla de la distància: cap vèrtex adjacent pot tenir un assentament */
	private static boolean hiHaAssentamentAdjacent(Session s, Vertex v) {
		return s.createQuery("SELECT COUNT(x) FROM Assentament x WHERE "
				+ "x.vertex IN (SELECT ar.vertexB FROM Aresta ar WHERE ar.vertexA = :v) OR "
				+ "x.vertex IN (SELECT ar.vertexA FROM Aresta ar WHERE ar.vertexB = :v)", Long.class)
				.setParameter("v", v).getSingleResult() > 0;
	}

	private static long comptarCarreteresAresta(Session s, Aresta a) {
		return s.createQuery("SELECT COUNT(c) FROM Carretera c WHERE c.aresta = :a", Long.class)
				.setParameter("a", a).getSingleResult();
	}

	/** Nombre de construccions del jugador a un vèrtex (assentaments) */
	private static long assentamentsPropis(Session s, Jugador j, Vertex v) {
		return s.createQuery("SELECT COUNT(x) FROM Assentament x WHERE x.propietari = :j AND x.vertex = :v", Long.class)
				.setParameter("j", j).setParameter("v", v).getSingleResult();
	}

	/** Nombre de carreteres del jugador que toquen un vèrtex */
	private static long carreteresPropiesAmb(Session s, Jugador j, Vertex v) {
		return s.createQuery("SELECT COUNT(c) FROM Carretera c WHERE c.propietari = :j "
				+ "AND (c.aresta.vertexA = :v OR c.aresta.vertexB = :v)", Long.class)
				.setParameter("j", j).setParameter("v", v).getSingleResult();
	}

	/** Connexió normal: una aresta toca un assentament o una carretera del mateix jugador */
	private static boolean connectadaAmbPropi(Session s, Jugador j, Aresta a) {
		for (Vertex v : List.of(a.getVertexA(), a.getVertexB())) {
			if (assentamentsPropis(s, j, v) > 0 || carreteresPropiesAmb(s, j, v) > 0) {
				return true;
			}
		}
		return false;
	}

	/** En preparació: la carretera ha de tocar un poblat propi que encara no tingui cap carretera */
	private static boolean tocaPoblatSenseCarreteraPreparacio(Session s, Jugador j, Aresta a) {
		List<Vertex> lliures = s.createQuery("SELECT x.vertex FROM Assentament x WHERE x.propietari = :j "
				+ "AND NOT EXISTS (SELECT c FROM Carretera c WHERE c.propietari = :j "
				+ "AND (c.aresta.vertexA = x.vertex OR c.aresta.vertexB = x.vertex))", Vertex.class)
				.setParameter("j", j).getResultList();
		return lliures.stream().anyMatch(v -> v.getId().equals(a.getVertexA().getId())
				|| v.getId().equals(a.getVertexB().getId()));
	}

	private static boolean connectadaAmbCarreteraPropia(Session s, Jugador j, Vertex v) {
		return s.createQuery("SELECT COUNT(c) FROM Carretera c WHERE c.propietari = :j "
				+ "AND (c.aresta.vertexA = :v OR c.aresta.vertexB = :v)", Long.class)
				.setParameter("j", j).setParameter("v", v).getSingleResult() > 0;
	}

	/** Quan el jugador col·loca el seu segon poblat de la preparació, rep 1 recurs per hexàgon adjacent */
	private static void repartirRecursInicials(Session s, Jugador j, Vertex v) {
		List<Hexagon> hexagons = s.createQuery("SELECT hv.hexagon FROM HexagonVertex hv WHERE hv.vertex = :v",
				Hexagon.class).setParameter("v", v).getResultList();
		for (Hexagon h : hexagons) {
			TipusRecurs recurs = h.getTerreny().getRecurs();
			if (recurs != null) {
				j.afegirRecurs(recurs, 1);
			}
		}
	}

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

	/** Substitueix un poblat propi per una ciutat */
	public String millorarACiutat(int idJugador, int idVertex) {
		return executar(s -> {
			Jugador j = Consultes.carregarJugador(s, idJugador);
			Partida p = validarTorn(j);
			if (p.getEstat() != EstatPartida.EN_CURS) {
				throw new JocException("Només es pot millorar durant la partida (EN_CURS)");
			}

			Vertex v = s.get(Vertex.class, idVertex);
			if (v == null || !v.getPartida().getId().equals(p.getId())) {
				throw new JocException("El vèrtex " + idVertex + " no pertany a aquesta partida");
			}
			List<Assentament> ass = s.createQuery("FROM Assentament x WHERE x.vertex = :v", Assentament.class)
					.setParameter("v", v).getResultList();
			if (ass.isEmpty() || !(ass.get(0) instanceof Poblat)
					|| !ass.get(0).getPropietari().getId().equals(j.getId())) {
				throw new JocException("No tens cap poblat teu al vèrtex " + idVertex);
			}
			if (Consultes.comptar(s, "Ciutat", j) >= LIMIT_CIUTATS) {
				throw new JocException("Límit de " + LIMIT_CIUTATS + " ciutats assolit");
			}

			pagar(j, COST_CIUTAT);
			s.remove(ass.get(0));
			s.flush(); // el vèrtex té un únic assentament: cal esborrar abans d'inserir
			s.persist(new Ciutat(j, v));
			Consultes.comprovarVictoria(s, p);
			return "Poblat millorat a ciutat al vèrtex " + idVertex;
		});
	}

	/** Canvia 4 recursos del tipus dona per 1 del tipus rep */
	public String comerciarBanca(int idJugador, TipusRecurs dona, TipusRecurs rep) {
		return executar(s -> {
			Jugador j = Consultes.carregarJugador(s, idJugador);
			Partida p = validarTorn(j);
			if (p.getEstat() != EstatPartida.EN_CURS) {
				throw new JocException("El comerç només és possible durant la partida (EN_CURS)");
			}
			if (dona == null || rep == null || dona == rep) {
				throw new JocException("Els recursos de comerç han de ser diferents");
			}
			if (j.getQuantitat(dona) < 4) {
				throw new JocException("Necessites 4 recursos de tipus " + dona + " per comerciar");
			}
			j.afegirRecurs(dona, -4);
			j.afegirRecurs(rep, 1);
			return "Has canviat 4 " + dona + " per 1 " + rep;
		});
	}

	public int calcularPunts(int idJugador) {
		return executar(s -> Consultes.calcularPunts(s, Consultes.carregarJugador(s, idJugador)));
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

	private static Jugador jugadorPerOrdre(Session s, Partida p, int ordre) {
		return s.createQuery("FROM Jugador j WHERE j.partida = :p AND j.ordreTorn = :o", Jugador.class)
				.setParameter("p", p).setParameter("o", ordre).getSingleResult();
	}

	/**
	 * Vèrtexs on el jugador pot construir un poblat ara mateix: lliures, que compleixin
	 * la regla de la distància i (fora de preparació) connectats a una carretera pròpia.
	 * Tot es resol amb una única consulta JPQL.
	 */
	public List<Vertex> vertexsDisponibles(int idJugador) {
		return executar(s -> {
			Jugador j = Consultes.carregarJugador(s, idJugador);
			Partida p = j.getPartida();
			if (p.getEstat() == EstatPartida.FINALITZADA) {
				return new ArrayList<Vertex>();
			}
			boolean preparacio = p.getEstat() == EstatPartida.PREPARACIO;
			if (!preparacio && Consultes.comptar(s, "Poblat", j) >= LIMIT_POBLATS) {
				return new ArrayList<Vertex>();
			}

			String hql = "SELECT v FROM Vertex v WHERE v.partida = :p "
					+ "AND NOT EXISTS (SELECT x FROM Assentament x WHERE x.vertex = v) "
					+ "AND NOT EXISTS (SELECT ar FROM Aresta ar WHERE "
					+ "(ar.vertexA = v AND EXISTS (SELECT y FROM Assentament y WHERE y.vertex = ar.vertexB)) OR "
					+ "(ar.vertexB = v AND EXISTS (SELECT z FROM Assentament z WHERE z.vertex = ar.vertexA)))";
			if (!preparacio) {
				hql += " AND EXISTS (SELECT c FROM Carretera c WHERE c.propietari = :j "
						+ "AND (c.aresta.vertexA = v OR c.aresta.vertexB = v))";
			}
			hql += " ORDER BY v.codi";

			Query<Vertex> consulta = s.createQuery(hql, Vertex.class);
			consulta.setParameter("p", p);
			if (!preparacio) {
				consulta.setParameter("j", j);
			}
			List<Vertex> resultat = consulta.getResultList();
			return resultat;
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
