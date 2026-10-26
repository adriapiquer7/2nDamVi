package ProjecteHibernate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

/**
 * Carrega la topologia del tauler des dels CSV de la carpeta "csv" (a l'arrel del projecte).
 *
 * Format assumit (una capçalera a cada fitxer, separador ';' o ','):
 *   hexagons.csv       id;q;r
 *   vertexs.csv        id;x;y
 *   hexagon_vertex.csv hexagon;vertex;posicio   (ids del CSV)
 *   arestes.csv        id;vertexA;vertexB       (ids del CSV)
 * Cal revisar-ho contra els fitxers reals.
 */
final class TopologiaCSV {

	static final String DIRECTORI = "csv";

	private TopologiaCSV() {
	}

	static List<Hexagon> carregar(Session s, Partida p, List<TipusTerreny> terrenys, List<Integer> numeros) {
		List<String[]> filesHex = llegir("hexagons.csv");
		if (filesHex.size() != terrenys.size()) {
			throw new JocException("S'esperaven " + terrenys.size() + " hexàgons i n'hi ha " + filesHex.size());
		}

		Map<Integer, Hexagon> hexagonsPerCodi = new HashMap<>();
		Map<Integer, Vertex> vertexsPerCodi = new HashMap<>();
		List<Hexagon> hexagons = new ArrayList<>();

		int indexTerreny = 0;
		int indexNumero = 0;
		for (String[] c : filesHex) {
			TipusTerreny terreny = terrenys.get(indexTerreny++);
			// El desert no té número
			Integer numero = terreny == TipusTerreny.DESERT ? null : numeros.get(indexNumero++);
			Hexagon h = new Hexagon(p, parse(c[0]), terreny, numero, parse(c[1]), parse(c[2]));
			s.persist(h);
			hexagonsPerCodi.put(h.getCodi(), h);
			hexagons.add(h);
		}

		for (String[] c : llegir("vertexs.csv")) {
			Vertex v = new Vertex(p, parse(c[0]));
			s.persist(v);
			vertexsPerCodi.put(v.getCodi(), v);
		}

		for (String[] c : llegir("hexagon_vertex.csv")) {
			Hexagon h = buscar(hexagonsPerCodi, parse(c[0]), "hexàgon");
			Vertex v = buscar(vertexsPerCodi, parse(c[1]), "vèrtex");
			s.persist(new HexagonVertex(h, v, parse(c[2])));
		}

		for (String[] c : llegir("arestes.csv")) {
			Vertex a = buscar(vertexsPerCodi, parse(c[1]), "vèrtex");
			Vertex b = buscar(vertexsPerCodi, parse(c[2]), "vèrtex");
			s.persist(new Aresta(p, parse(c[0]), a, b));
		}

		return hexagons;
	}

	private static <T> T buscar(Map<Integer, T> mapa, int codi, String tipus) {
		T valor = mapa.get(codi);
		if (valor == null) {
			throw new JocException("El CSV fa referència a un " + tipus + " amb codi " + codi + " que no existeix");
		}
		return valor;
	}

	/** Llegeix un CSV i retorna les files (sense la capçalera) com a arrays de text */
	private static List<String[]> llegir(String fitxer) {
		Path path = Path.of(DIRECTORI, fitxer);
		try {
			List<String> linies = Files.readAllLines(path);
			List<String[]> files = new ArrayList<>();
			for (int i = 1; i < linies.size(); i++) {
				String linia = linies.get(i).trim();
				if (linia.isEmpty()) {
					continue;
				}
				String separador = linia.contains(";") ? ";" : ",";
				files.add(Arrays.stream(linia.split(separador)).map(String::trim).toArray(String[]::new));
			}
			return files;
		} catch (IOException e) {
			throw new JocException("No es pot llegir el fitxer " + path.toAbsolutePath() + ": " + e.getMessage());
		}
	}

	private static int parse(String text) {
		return Integer.parseInt(text.trim());
	}
}
