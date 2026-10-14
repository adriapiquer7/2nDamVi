package patroDao;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/*
 * =====================================================================================
 *  EINA D'AUTOCORRECCIÓ. NO L'HEU DE MODIFICAR.
 * =====================================================================================
 * Fa les comprovacions del Main i calcula un SIMULACRE de puntuació.
 *
 *  - Cada comprovació mostra [OK] o [ERROR] amb el valor esperat i l'obtingut.
 *  - Si el vostre codi llança una excepció, es mostra i el Main continua amb la resta.
 *  - Al final es mostra una taula amb els ítems de la rúbrica que es poden comprovar
 *    executant el codi. És una puntuació APROXIMADA: no és la nota de l'examen.
 */
public final class Correccio {

	private Correccio() {
	}

	private static final double TOLERANCIA = 0.001;

	// Ítems de la rúbrica: número -> ítem
	private static final Map<Integer, Item> ITEMS = new LinkedHashMap<>();

	// Sortida per pantalla de l'última crida feta amb crida(...)
	private static String ultimaSortida = "";

	// Com un Supplier, però pot llançar qualsevol excepció (també les comprovades)
	@FunctionalInterface
	public interface Valor<T> {
		T get() throws Exception;
	}

	private static class Item {
		final int num;
		final String descripcio;
		final double pes;
		final boolean automatic;
		int ok;
		int total;

		Item(int num, String descripcio, double pes, boolean automatic) {
			this.num = num;
			this.descripcio = descripcio;
			this.pes = pes;
			this.automatic = automatic;
		}
	}

	/* =====================================================================
	 * Definició de la rúbrica
	 * ===================================================================== */

	// Ítem que es comprova executant el codi
	public static void item(int num, String descripcio, double pes) {
		ITEMS.put(num, new Item(num, descripcio, pes, true));
	}

	// Ítem que NO es pot comprovar automàticament (el revisa la professora)
	public static void itemManual(int num, String descripcio, double pes) {
		ITEMS.put(num, new Item(num, descripcio, pes, false));
	}

	/* =====================================================================
	 * Crides al vostre codi
	 * ===================================================================== */

	/*
	 * Executa una crida al vostre codi. Si llança qualsevol excepció (o error), la mostra
	 * i retorna null perquè el Main pugui continuar. La sortida per pantalla de la crida
	 * es mostra igualment i es guarda per comprovar els missatges (vegeu haImprès).
	 */
	public static <T> T crida(Valor<T> crida) {
		PrintStream original = System.out;
		ByteArrayOutputStream copia = new ByteArrayOutputStream();
		System.setOut(new PrintStream(new Duplicador(original, copia), true, StandardCharsets.UTF_8));
		try {
			return crida.get();
		} catch (Throwable e) {
			original.println("\t[EXCEPCIÓ] " + descriu(e));
			return null;
		} finally {
			System.out.flush();
			System.setOut(original);
			ultimaSortida = copia.toString(StandardCharsets.UTF_8);
		}
	}

	// Per a crides que no retornen res (o quan no ens interessa el que retornen)
	public static void executa(Runnable crida) {
		crida(() -> {
			crida.run();
			return null;
		});
	}

	// La crida anterior ha imprès aquest text?
	public static boolean haImpres(String text) {
		return ultimaSortida.contains(text);
	}

	/* =====================================================================
	 * Comprovacions
	 * ===================================================================== */

	/*
	 * Compara el valor esperat amb l'obtingut. L'obtingut es calcula DINS d'un try:
	 * si llança una excepció (NullPointerException perquè el vostre mètode ha retornat null,
	 * LazyInitializationException...), la comprovació compta com a ERROR i el Main continua.
	 */
	public static boolean comprova(int item, String que, Object esperat, Valor<Object> obtingut) {
		Object valor;
		try {
			valor = obtingut.get();
		} catch (Throwable e) {
			registra(item, false);
			System.out.println("\t[ERROR] " + que + " -> esperat " + format(esperat) + ", però ha fallat: " + descriu(e));
			return false;
		}
		boolean correcte = iguals(esperat, valor);
		registra(item, correcte);
		if (correcte) {
			System.out.println("\t[OK]    " + que + " (" + format(valor) + ")");
		} else {
			System.out.println("\t[ERROR] " + que + " -> esperat " + format(esperat) + ", obtingut " + format(valor));
		}
		return correcte;
	}

	// Comprovació de sí o no
	public static boolean comprova(int item, String que, Valor<Boolean> condicio) {
		return comprova(item, que, true, () -> condicio.get());
	}

	/*
	 * Comprovacions del tipus "en aquest cas NO ha de canviar res".
	 * Un mètode buit tampoc canvia res: per això només puntuen si la funció ja fa
	 * els canvis bàsics (funciona = alguna de les comprovacions principals ha anat bé).
	 */
	public static boolean comprovaNoCanvi(boolean funciona, int item, String que, Object esperat, Valor<Object> obtingut) {
		if (!funciona) {
			registra(item, false);
			System.out.println("\t[ERROR] " + que + " -> no puntua: la funció encara no fa els canvis bàsics");
			return false;
		}
		return comprova(item, que, esperat, obtingut);
	}

	public static boolean comprovaNoCanvi(boolean funciona, int item, String que, Valor<Boolean> condicio) {
		return comprovaNoCanvi(funciona, item, que, true, () -> condicio.get());
	}

	private static void registra(int num, boolean correcte) {
		Item it = ITEMS.get(num);
		if (it == null) {
			throw new IllegalArgumentException("Ítem de la rúbrica inexistent: " + num);
		}
		it.total++;
		if (correcte) {
			it.ok++;
		}
	}

	private static boolean iguals(Object esperat, Object obtingut) {
		if (esperat instanceof Number e && obtingut instanceof Number o) {
			return Math.abs(e.doubleValue() - o.doubleValue()) < TOLERANCIA;
		}
		// Enums i textos: comparem el nom (així RANG.OR == "OR")
		if (esperat != null && obtingut != null) {
			return esperat.toString().equals(obtingut.toString());
		}
		return Objects.equals(esperat, obtingut);
	}

	/* =====================================================================
	 * Simulacre de puntuació
	 * ===================================================================== */

	public static void simulacre() {
		System.out.println();
		System.out.println("====================================================================================");
		System.out.println(" SIMULACRE DE PUNTUACIÓ");
		System.out.println("====================================================================================");
		System.out.println(" AVÍS: puntuació APROXIMADA calculada automàticament. NO és la nota de l'examen");
		System.out.println(" i NO substitueix la correcció de la professora.");
		System.out.println(" Només avalua una part de l'examen: el que es pot comprovar executant el codi");
		System.out.println(" (valors a la base de dades, valors retornats i missatges per pantalla).");
		System.out.println(" No valora, entre d'altres: la gestió de transaccions i excepcions, que es tornin a");
		System.out.println(" consultar les dades a la BD després del commit, la reutilització de mètodes,");
		System.out.println(" la qualitat del codi ni les penalitzacions de l'enunciat.");
		System.out.println("------------------------------------------------------------------------------------");
		System.out.printf(" %3s  %-58s %7s %13s%n", "#", "Ítem", "Proves", "Punts");
		double total = 0;
		double maxim = 0;
		double manual = 0;
		for (Item it : ITEMS.values()) {
			String desc = it.descripcio.length() > 58 ? it.descripcio.substring(0, 57) + "." : it.descripcio;
			if (!it.automatic) {
				manual += it.pes;
				System.out.printf(" %3d  %-58s %7s %13s%n", it.num, desc, "-", "docent/" + num(it.pes));
				continue;
			}
			double punts = it.total == 0 ? 0 : it.pes * it.ok / it.total;
			total += punts;
			maxim += it.pes;
			System.out.printf(" %3d  %-58s %7s %13s%n", it.num, desc, it.ok + "/" + it.total,
					num(punts) + " / " + num(it.pes));
		}
		System.out.println("------------------------------------------------------------------------------------");
		System.out.println(" Total comprovat automàticament: " + num(total) + " / " + num(maxim));
		System.out.println(" Pendent de revisió docent: " + num(manual) + " punts (ítems marcats \"docent\")");
		System.out.println(" Recordeu: és un simulacre orientatiu. La nota la posa la professora.");
		System.out.println("====================================================================================");
	}

	/* =====================================================================
	 * Auxiliars
	 * ===================================================================== */

	public static void titol(String text) {
		System.out.println("\n========== " + text + " ==========");
	}

	private static String num(double d) {
		return String.format(Locale.of("ca", "ES"), "%.2f", d);
	}

	private static String format(Object o) {
		if (o instanceof Double d) {
			return String.format(Locale.ROOT, "%.1f", d);
		}
		return String.valueOf(o);
	}

	private static String descriu(Throwable e) {
		Throwable arrel = e;
		while (arrel.getCause() != null && arrel.getCause() != arrel) {
			arrel = arrel.getCause();
		}
		String text = e.getClass().getSimpleName() + (e.getMessage() == null ? "" : ": " + e.getMessage());
		if (arrel != e) {
			text += " (causa: " + arrel.getClass().getSimpleName()
					+ (arrel.getMessage() == null ? "" : ": " + arrel.getMessage()) + ")";
		}
		return text.length() > 250 ? text.substring(0, 250) + "..." : text;
	}

	// Escriu alhora a la consola i a la còpia (per poder comprovar els missatges)
	private static class Duplicador extends OutputStream {
		private final OutputStream a;
		private final OutputStream b;

		Duplicador(OutputStream a, OutputStream b) {
			this.a = a;
			this.b = b;
		}

		@Override
		public void write(int c) throws java.io.IOException {
			a.write(c);
			b.write(c);
		}

		@Override
		public void write(byte[] buf, int off, int len) throws java.io.IOException {
			a.write(buf, off, len);
			b.write(buf, off, len);
		}

		@Override
		public void flush() throws java.io.IOException {
			a.flush();
			b.flush();
		}
	}
}
