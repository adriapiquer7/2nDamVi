package SpringROA.SpringROA.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import SpringROA.SpringROA.exception.JocException;
import SpringROA.SpringROA.model.Assentament;
import SpringROA.SpringROA.model.Ciutat;
import SpringROA.SpringROA.model.ColorJugador;
import SpringROA.SpringROA.model.EstatPartida;
import SpringROA.SpringROA.model.Hexagon;
import SpringROA.SpringROA.model.HexagonVertex;
import SpringROA.SpringROA.model.Jugador;
import SpringROA.SpringROA.model.Partida;
import SpringROA.SpringROA.model.TipusRecurs;
import SpringROA.SpringROA.model.TipusTerreny;
import SpringROA.SpringROA.model.Vertex;
import SpringROA.SpringROA.repository.ArestaRepository;
import SpringROA.SpringROA.repository.PartidaRepository;
import jakarta.persistence.EntityManager;

@Service
public class PartidaService{
	
	@Autowired
	private PartidaRepository partidaRepository;
	@Autowired
<<<<<<< Updated upstream
	private EntityManager entityManager;
	@Autowired
	private ArestaRepository arestaRepository;
	private static final int PUNTS_PER_DEFECTE = 10;
	private final Random random = new Random();
=======
	private JugadorRepository jugadorRepository;
	@Autowired
	private HexagonRepository hexagonRepository;
	@Autowired
	private AssentamentRepository assentamentRepository;
	@Autowired
	private EntityManager entityManager;

	private Partida carregarPartida(int idPartida) {
		return partidaRepository.findById(idPartida)
				.orElseThrow(() -> new JocException("No existeix la partida " + idPartida, HttpStatus.NOT_FOUND));
	}
>>>>>>> Stashed changes

	@Transactional
<<<<<<< Updated upstream
	public int iniciarPartida(List<String> nomsJugadors, Integer puntsPerGuanyar) {
=======
	public ResponseEntity<PartidaResposta> iniciarPartida(PartidaPeticio peticio) {
		List<String> nomsJugadors = peticio.nomsJugadors();
>>>>>>> Stashed changes
		if (nomsJugadors == null || nomsJugadors.size() < 2 || nomsJugadors.size() > 4) {
			throw new JocException("Hi ha d'haver entre 2 i 4 jugadors");
		}
		List<String> noms = nomsJugadors.stream().map(String::trim).toList();
		if (noms.stream().anyMatch(String::isEmpty) || new HashSet<>(noms).size() != noms.size()) {
			throw new JocException("Els noms dels jugadors han de ser no buits i diferents");
		}
		int punts = puntsPerGuanyar == null ? PUNTS_PER_DEFECTE : puntsPerGuanyar;

<<<<<<< Updated upstream
		return executar(s -> {
			Partida partida = new Partida(punts);
			s.persist(partida);

			// Ordre de torn aleatori; el color depèn de l'ordre de torn
			List<String> barrejats = new ArrayList<>(noms);
			Collections.shuffle(barrejats, random);
			ColorJugador[] colors = ColorJugador.values();
			List<Jugador> jugadors = new ArrayList<>();
			for (int i = 0; i < barrejats.size(); i++) {
				Jugador j = new Jugador(partida, barrejats.get(i), colors[i], i + 1);
				s.persist(j);
				jugadors.add(j);
=======
		Partida partida = partidaRepository.save(new Partida(punts));

		List<String> barrejats = new ArrayList<>(noms);
		Collections.shuffle(barrejats, random);
		ColorJugador[] colors = ColorJugador.values();
		List<Jugador> jugadors = new ArrayList<>();
		for (int i = 0; i < barrejats.size(); i++) {
			jugadors.add(jugadorRepository.save(new Jugador(partida, barrejats.get(i), colors[i], i + 1)));
		}
		partida.setActiu(jugadors.get(0));

		List<TipusTerreny> terrenys = new ArrayList<>();
		afegir(terrenys, TipusTerreny.BOSC, 4);
		afegir(terrenys, TipusTerreny.PASTURES, 4);
		afegir(terrenys, TipusTerreny.CAMPS, 4);
		afegir(terrenys, TipusTerreny.TURONS, 3);
		afegir(terrenys, TipusTerreny.MUNTANYES, 3);
		afegir(terrenys, TipusTerreny.DESERT, 1);
		Collections.shuffle(terrenys, random);

		List<Integer> numeros = new ArrayList<>(List.of(2, 3, 3, 4, 4, 5, 5, 6, 6, 8, 8, 9, 9, 10, 10, 11, 11, 12));
		Collections.shuffle(numeros, random);

		List<Hexagon> hexagons = TopologiaCSV.carregar(entityManager, partida, terrenys, numeros);

		Hexagon desert = hexagons.stream()
				.filter(h -> h.getTerreny() == TipusTerreny.DESERT)
				.findFirst()
				.orElseThrow();
		partida.setLladre(desert);

		return ResponseEntity.status(HttpStatus.CREATED).body(PartidaResposta.de(partida, jugadors, hexagons));
	}

	private static void afegir(List<TipusTerreny> llista, TipusTerreny terreny, int quantitat) {
		for (int i = 0; i < quantitat; i++) {
			llista.add(terreny);
		}
	}

	@Transactional(readOnly = true)
	public ResponseEntity<List<TaulerHexagonResposta>> tauler(int idPartida) {
		Partida p = carregarPartida(idPartida);
		Integer idLladre = p.getLladre() == null ? null : p.getLladre().getId();

		List<TaulerHexagonResposta> resposta = new ArrayList<>();
		for (Hexagon h : hexagonRepository.findByPartidaOrderByCodi(p)) {
			List<ConstruccioResposta> construccions = new ArrayList<>();
			for (Assentament a : assentamentRepository.assentamentsDelHexagon(h)) {
				String tipus = a instanceof Ciutat ? "CIUTAT" : "POBLAT";
				construccions.add(new ConstruccioResposta(a.getVertex().getId(), tipus, a.getPropietari().getNom()));
>>>>>>> Stashed changes
			}
			partida.setActiu(jugadors.get(0));

			// Terrenys i números assignats aleatòriament
			List<TipusTerreny> terrenys = new ArrayList<>();
			afegir(terrenys, TipusTerreny.BOSC, 4);
			afegir(terrenys, TipusTerreny.PASTURES, 4);
			afegir(terrenys, TipusTerreny.CAMPS, 4);
			afegir(terrenys, TipusTerreny.TURONS, 3);
			afegir(terrenys, TipusTerreny.MUNTANYES, 3);
			afegir(terrenys, TipusTerreny.DESERT, 1);
			Collections.shuffle(terrenys, random);

			List<Integer> numeros = new ArrayList<>(List.of(2, 3, 3, 4, 4, 5, 5, 6, 6, 8, 8, 9, 9, 10, 10, 11, 11, 12));
			Collections.shuffle(numeros, random);

			// Topologia des dels CSV
			List<Hexagon> hexagons = TopologiaCSV.carregar(s, partida, terrenys, numeros);

			// El lladre comença al desert
			Hexagon desert = hexagons.stream()
					.filter(h -> h.getTerreny() == TipusTerreny.DESERT)
					.findFirst()
					.orElseThrow();
			partida.setLladre(desert);

			return partida.getId();
		});
	}

	/** Mostra hexàgons, número, lladre i construccions de cada vèrtex */
	@Transactional
	public void mostrarTauler(int idPartida) {
		executar(s -> {
			Partida p = Consultes.carregarPartida(s, idPartida);
			List<Hexagon> hexagons = s.createQuery("FROM Hexagon h WHERE h.partida = :p ORDER BY h.codi", Hexagon.class)
					.setParameter("p", p).getResultList();

			System.out.println("=== TAULER (partida " + idPartida + ") ===");
			for (Hexagon h : hexagons) {
				boolean hiLladre = p.getLladre() != null && p.getLladre().getId().equals(h.getId());
				System.out.printf("Hexàgon %d (codi %d) | %s | número %s%s%n", h.getId(), h.getCodi(),
						h.getTerreny(), h.getNumero() == null ? "-" : h.getNumero(), hiLladre ? " | LLADRE" : "");

				List<HexagonVertex> hvs = s.createQuery(
						"FROM HexagonVertex hv WHERE hv.hexagon = :h ORDER BY hv.posicio", HexagonVertex.class)
						.setParameter("h", h).getResultList();
				for (HexagonVertex hv : hvs) {
					Vertex v = hv.getVertex();
					List<Assentament> ass = s.createQuery("FROM Assentament a WHERE a.vertex = :v", Assentament.class)
							.setParameter("v", v).getResultList();
					String info = "lliure";
					if (!ass.isEmpty()) {
						Assentament a = ass.get(0);
						String tipus = a instanceof Ciutat ? "ciutat" : "poblat";
						info = tipus + " de " + a.getPropietari().getNom();
					}
					System.out.printf("   vèrtex %d (posició %d): %s%n", v.getId(), hv.getPosicio(), info);
				}
			}
			return null;
		});
	}

	@Transactional
<<<<<<< Updated upstream
	public String tirarDaus(int idPartida) {
		return executar(s -> {
			Partida p = Consultes.carregarPartida(s, idPartida);
			if (p.getEstat() != EstatPartida.EN_CURS) {
				throw new JocException("Els daus només es tiren durant la partida (EN_CURS)");
			}
			if (p.isDausTirats()) {
				throw new JocException("El jugador actiu ja ha tirat els daus en aquest torn");
			}
			if (p.isLladrePendent()) {
				throw new JocException("Cal moure el lladre abans de continuar");
			}

			int d1 = random.nextInt(6) + 1;
			int d2 = random.nextInt(6) + 1;
			int total = d1 + d2;
			p.setUltimaTirada(total);
			p.setDausTirats(true);

			StringBuilder sb = new StringBuilder("Tirada: " + d1 + " + " + d2 + " = " + total + "\n");
			if (total == 7) {
				// La part avançada (lladre) no és en aquesta versió
				return sb.append("Surt 7: no hi ha producció.").toString();
			}

			Map<Jugador, Map<TipusRecurs, Integer>> guanys = new LinkedHashMap<>();
			List<Hexagon> hexagons = s.createQuery("FROM Hexagon h WHERE h.partida = :p AND h.numero = :n", Hexagon.class)
					.setParameter("p", p).setParameter("n", total).getResultList();

			for (Hexagon h : hexagons) {
=======
	public ResponseEntity<TiradaResposta> tirarDaus(int idPartida) {
		Partida p = carregarPartida(idPartida);
		if (p.getEstat() != EstatPartida.EN_CURS) {
			throw new JocException("Els daus només es tiren durant la partida (EN_CURS)");
		}
		if (p.isDausTirats()) {
			throw new JocException("El jugador actiu ja ha tirat els daus en aquest torn");
		}
		if (p.isLladrePendent()) {
			throw new JocException("Cal moure el lladre abans de continuar");
		}

		int d1 = random.nextInt(6) + 1;
		int d2 = random.nextInt(6) + 1;
		int total = d1 + d2;
		p.setUltimaTirada(total);
		p.setDausTirats(true);

		Map<Integer, Jugador> jugadors = new LinkedHashMap<>();
		Map<Integer, Map<TipusRecurs, Integer>> guanys = new LinkedHashMap<>();

		if (total != 7) {
			for (Hexagon h : hexagonRepository.findByPartidaAndNumero(p, total)) {
>>>>>>> Stashed changes
				if (p.getLladre() != null && p.getLladre().getId().equals(h.getId())) {
					continue;
				}
				TipusRecurs recurs = h.getTerreny().getRecurs();
				if (recurs == null) {
					continue;
				}
				List<Assentament> ass = s.createQuery(
						"FROM Assentament a WHERE a.vertex IN (SELECT hv.vertex FROM HexagonVertex hv WHERE hv.hexagon = :h)",
						Assentament.class).setParameter("h", h).getResultList();
				for (Assentament a : ass) {
					int quantitat = a instanceof Ciutat ? 2 : 1;
					guanys.computeIfAbsent(a.getPropietari(), k -> new EnumMap<>(TipusRecurs.class))
							.merge(recurs, quantitat, Integer::sum);
				}
			}

			for (Map.Entry<Jugador, Map<TipusRecurs, Integer>> e : guanys.entrySet()) {
				StringBuilder linia = new StringBuilder();
				for (Map.Entry<TipusRecurs, Integer> r : e.getValue().entrySet()) {
					e.getKey().afegirRecurs(r.getKey(), r.getValue());
					linia.append(" +").append(r.getValue()).append(' ').append(r.getKey());
				}
				sb.append(e.getKey().getNom()).append(':').append(linia).append('\n');
			}
<<<<<<< Updated upstream
			if (guanys.isEmpty()) {
				sb.append("Ningú no rep recursos.\n");
			}
			return sb.toString().trim();
		});
=======
			resum.add(new GuanyResposta(j.getNom(), e.getValue()));
		}

		return ResponseEntity.ok(new TiradaResposta(d1, d2, total, resum));
>>>>>>> Stashed changes
	}
}
