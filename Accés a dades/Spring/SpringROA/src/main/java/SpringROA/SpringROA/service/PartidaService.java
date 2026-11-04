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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import SpringROA.SpringROA.dto.ConstruccioResposta;
import SpringROA.SpringROA.dto.GuanyResposta;
import SpringROA.SpringROA.dto.PartidaPeticio;
import SpringROA.SpringROA.dto.PartidaResposta;
import SpringROA.SpringROA.dto.TaulerHexagonResposta;
import SpringROA.SpringROA.dto.TiradaResposta;
import SpringROA.SpringROA.exception.JocException;
import SpringROA.SpringROA.model.Assentament;
import SpringROA.SpringROA.model.Ciutat;
import SpringROA.SpringROA.model.ColorJugador;
import SpringROA.SpringROA.model.EstatPartida;
import SpringROA.SpringROA.model.Hexagon;
import SpringROA.SpringROA.model.Jugador;
import SpringROA.SpringROA.model.Partida;
import SpringROA.SpringROA.model.TipusRecurs;
import SpringROA.SpringROA.model.TipusTerreny;
import SpringROA.SpringROA.repository.AssentamentRepository;
import SpringROA.SpringROA.repository.HexagonRepository;
import SpringROA.SpringROA.repository.JugadorRepository;
import SpringROA.SpringROA.repository.PartidaRepository;
import jakarta.persistence.EntityManager;

/** Servei fort de la partida: iniciar, mostrar el tauler i tirar els daus (abans, PartidaDAO) */
@Service
public class PartidaService {

	private static final int PUNTS_PER_DEFECTE = 10;
	private final Random random = new Random();

	@Autowired
	private PartidaRepository partidaRepository;
	@Autowired
	private JugadorRepository jugadorRepository;
	@Autowired
	private HexagonRepository hexagonRepository;
	@Autowired
	private AssentamentRepository assentamentRepository;
	// Només per a TopologiaCSV (càrrega de la topologia des dels CSV)
	@Autowired
	private EntityManager entityManager;

	/** Carrega una partida o llança un 404 */
	private Partida carregarPartida(int idPartida) {
		return partidaRepository.findById(idPartida)
				.orElseThrow(() -> new JocException("No existeix la partida " + idPartida, HttpStatus.NOT_FOUND));
	}

	/** Crea una partida nova: jugadors, tauler, terrenys, números i lladre al desert */
	@Transactional
	public ResponseEntity<PartidaResposta> iniciarPartida(PartidaPeticio peticio) {
		// 1 · Comprovar abans de modificar res
		List<String> nomsJugadors = peticio.nomsJugadors();
		if (nomsJugadors == null || nomsJugadors.size() < 2 || nomsJugadors.size() > 4) {
			throw new JocException("Hi ha d'haver entre 2 i 4 jugadors", HttpStatus.BAD_REQUEST);
		}
		List<String> noms = nomsJugadors.stream().map(n -> n == null ? "" : n.trim()).toList();
		if (noms.stream().anyMatch(String::isEmpty) || new HashSet<>(noms).size() != noms.size()) {
			throw new JocException("Els noms dels jugadors han de ser no buits i diferents", HttpStatus.BAD_REQUEST);
		}
		int punts = peticio.puntsPerGuanyar() == null ? PUNTS_PER_DEFECTE : peticio.puntsPerGuanyar();
		if (punts < 2 || punts > 50) {
			throw new JocException("Els punts per guanyar han d'estar entre 2 i 50", HttpStatus.BAD_REQUEST);
		}

		// 2 · Modificar
		Partida partida = partidaRepository.save(new Partida(punts));

		// Ordre de torn aleatori; el color depèn de l'ordre de torn
		List<String> barrejats = new ArrayList<>(noms);
		Collections.shuffle(barrejats, random);
		ColorJugador[] colors = ColorJugador.values();
		List<Jugador> jugadors = new ArrayList<>();
		for (int i = 0; i < barrejats.size(); i++) {
			jugadors.add(jugadorRepository.save(new Jugador(partida, barrejats.get(i), colors[i], i + 1)));
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
		List<Hexagon> hexagons = TopologiaCSV.carregar(entityManager, partida, terrenys, numeros);

		// El lladre comença al desert
		Hexagon desert = hexagons.stream()
				.filter(h -> h.getTerreny() == TipusTerreny.DESERT)
				.findFirst()
				.orElseThrow();
		partida.setLladre(desert);

		// 4 · Retornar el DTO, construït aquí dins
		return ResponseEntity.status(HttpStatus.CREATED).body(PartidaResposta.de(partida, jugadors, hexagons));
	}

	private static void afegir(List<TipusTerreny> llista, TipusTerreny terreny, int quantitat) {
		for (int i = 0; i < quantitat; i++) {
			llista.add(terreny);
		}
	}

	/** Hexàgons de la partida amb terreny, número, lladre i les construccions dels seus vèrtexs */
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
			}
			resposta.add(new TaulerHexagonResposta(h.getId(), h.getCodi(), h.getTerreny(), h.getNumero(),
					h.getId().equals(idLladre), construccions));
		}
		return ResponseEntity.ok(resposta);
	}

	/**
	 * Tira els daus i fa la producció. Per cada hexàgon amb el número (sense lladre),
	 * cada poblat dona 1 recurs i cada ciutat 2 al propietari.
	 */
	@Transactional
	public ResponseEntity<TiradaResposta> tirarDaus(int idPartida) {
		// 1 · Comprovar
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

		// 2 · Modificar
		int d1 = random.nextInt(6) + 1;
		int d2 = random.nextInt(6) + 1;
		int total = d1 + d2;
		p.setUltimaTirada(total);
		p.setDausTirats(true);

		// Els guanys es guarden per id de jugador (una sola entrada per jugador)
		Map<Integer, Jugador> jugadors = new LinkedHashMap<>();
		Map<Integer, Map<TipusRecurs, Integer>> guanys = new LinkedHashMap<>();

		// Amb un 7 no hi ha producció (la part avançada del lladre no és en aquesta versió)
		if (total != 7) {
			for (Hexagon h : hexagonRepository.findByPartidaAndNumero(p, total)) {
				if (p.getLladre() != null && p.getLladre().getId().equals(h.getId())) {
					continue;
				}
				TipusRecurs recurs = h.getTerreny().getRecurs();
				if (recurs == null) {
					continue;
				}
				for (Assentament a : assentamentRepository.assentamentsDelHexagon(h)) {
					int quantitat = a instanceof Ciutat ? 2 : 1;
					Jugador propietari = a.getPropietari();
					jugadors.putIfAbsent(propietari.getId(), propietari);
					guanys.computeIfAbsent(propietari.getId(), k -> new EnumMap<>(TipusRecurs.class))
							.merge(recurs, quantitat, Integer::sum);
				}
			}
		}

		List<GuanyResposta> resum = new ArrayList<>();
		for (Map.Entry<Integer, Map<TipusRecurs, Integer>> e : guanys.entrySet()) {
			Jugador j = jugadors.get(e.getKey());
			for (Map.Entry<TipusRecurs, Integer> r : e.getValue().entrySet()) {
				j.afegirRecurs(r.getKey(), r.getValue());
			}
			resum.add(new GuanyResposta(j.getNom(), e.getValue()));
		}

		// 4 · Retornar el DTO
		return ResponseEntity.ok(new TiradaResposta(d1, d2, total, resum));
	}
}
