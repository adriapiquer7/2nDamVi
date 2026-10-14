package patroDao;

import static patroDao.Correccio.comprova;
import static patroDao.Correccio.comprovaNoCanvi;
import static patroDao.Correccio.crida;
import static patroDao.Correccio.executa;
import static patroDao.Correccio.haImpres;
import static patroDao.Correccio.item;
import static patroDao.Correccio.itemManual;
import static patroDao.Correccio.titol;
import static patroDao.DadesProva.id;
import static patroDao.DadesProva.numCompletades;
import static patroDao.DadesProva.numCompletadors;
import static patroDao.DadesProva.numParticipants;
import static patroDao.DadesProva.partidaActiva;
import static patroDao.DadesProva.rang;
import static patroDao.DadesProva.trofeus;
import static patroDao.DadesProva.vida;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import modelDades.Brawler;
import modelDades.Classe;
import modelDades.Partida;
import modelDades.PartidaAmistosa;
import modelDades.PartidaRanked;
import modelDades.Rang;

/**
 * =====================================================================================
 *  MAIN D'AUTOCORRECCIÓ - Examen Hibernate Brawl Stars. NO L'HEU DE MODIFICAR.
 * =====================================================================================
 *
 * Com fer-lo servir:
 *  1. Perquè compili, heu de tenir el model complet (constructors, getters i setters de l'enunciat)
 *     i la classe BrawlerDao amb els 4 mètodes. Si encara no heu fet algun mètode,
 *     creeu-lo buit (return null;) i el Main el marcarà com a ERROR.
 *  2. Executeu-lo tantes vegades com vulgueu: abans de cada prova es reinicien les dades
 *     (vegeu DadesProva, on hi ha la taula amb totes les dades inicials).
 *  3. Cada comprovació mostra [OK] o [ERROR] amb el valor esperat i l'obtingut.
 *     Al codi, el comentari "Hauria de tornar" us diu què s'espera abans d'executar-ho.
 *  4. Al final hi ha un SIMULACRE DE PUNTUACIÓ aproximat: no és la nota de l'examen.
 */
public class Main {

	static BrawlerDao bDao;

	public static void main(String[] args) {
		rubrica();
		try {
			bDao = new BrawlerDao();
			DadesProva.reiniciar();
		} catch (Throwable e) {
			System.out.println("No s'ha pogut crear el DAO o carregar les dades de prova:");
			e.printStackTrace(System.out);
			System.out.println("Reviseu hibernate.cfg.xml, SessionManager i les anotacions del model.");
		}

		if (bDao != null) {
			provesModel();
			provesHerencia();
			provesToString();
			provesEstructuraDao();
			provesIntercanviTrofeus();
			provesTorneig();
			provesRepartirRecompensa();
			provesCompletarPartides();
		}

		Correccio.simulacre();
		try {
			SessionManager.getSessionFactory().close();
		} catch (Throwable e) {
			// res
		}
	}

	private static void rubrica() {
		itemManual(1, "Excepcions, transaccions, commits i rollbacks", 1);
		item(2, "Classes correctes (anotacions, constructors i setters)", 1);
		item(3, "Herència: Partida abstracta i SINGLE_TABLE", 0.5);
		item(4, "Interfícies i classes DAO", 1);
		item(5, "toString des de Brawler sense recursivitat", 0.5);
		item(6, "Cost de vida en assignar una partida activa", 0.5);
		item(7, "intercanviTrofeus: validació i transferència", 0.5);
		item(8, "intercanviTrofeus: retorna l'array actualitzat", 0.5);
		item(9, "torneig: rondes, dany i intercanvi de trofeus", 0.5);
		item(10, "torneig: aturada per mort, empats i guanyador", 0.5);
		item(11, "torneig: retorna l'array actualitzat", 0.5);
		item(12, "completarPartides: només brawlers amb partida activa", 0.5);
		item(13, "completarPartides: llistes i partida activa", 0.5);
		item(14, "completarPartides: Ranked/Amistosa, trofeus, rangs, vida", 0.5);
		item(15, "completarPartides: retorna la llista actualitzada", 0.5);
		item(16, "repartirRecompensa: multiplicador de rang", 0.25);
		item(17, "repartirRecompensa: canvi de rang", 0.25);
		item(18, "repartirRecompensa: retorna la llista actualitzada", 0.5);
	}

	/* =====================================================================
	 * MODEL: constructors, setters amb control i relacions als dos costats
	 * ===================================================================== */

	private static void provesModel() {
		titol("MODEL: constructors i setters");

		// Les dades de prova s'han carregat amb els vostres setters
		// Hauria de tornar: 8 brawlers a la BD
		comprova(2, "Hi ha 8 brawlers a la BD", 8L, () -> DadesProva.numBrawlers());

		// Hauria de tornar: Supervivència té 3 participants (Colt, Bull, Frank) i Atrapagemmes 1 completador (Poco)
		comprova(2, "Supervivència té 3 participants a la BD", 3L, () -> numParticipants("Supervivència"));
		comprova(2, "Atrapagemmes té 1 completador a la BD", 1L, () -> numCompletadors("Atrapagemmes"));

		// Constructor: només el nom. Hauria de tornar: BRONZE, 0 trofeus, 10 de vida
		comprova(2, "new Brawler(nom) comença a BRONZE", Rang.BRONZE, () -> new Brawler("Prova").getRang());
		comprova(2, "new Brawler(nom) comença amb 0 trofeus", 0, () -> new Brawler("Prova").getTrofeus());
		comprova(2, "new Brawler(nom) comença amb 10 de vida", 10.0, () -> new Brawler("Prova").getVida());

		// Límits. Hauria de tornar: trofeus 0 (no negatius), vida 10 (màxim) i vida 0 (mínim)
		comprova(2, "setTrofeus(-5) deixa 0 trofeus", 0, () -> {
			Brawler b = new Brawler("Prova");
			b.setTrofeus(-5);
			return b.getTrofeus();
		});
		comprova(2, "setVida(15) deixa 10 de vida", 10.0, () -> {
			Brawler b = new Brawler("Prova");
			b.setVida(15);
			return b.getVida();
		});
		comprova(2, "setVida(-3) deixa 0 de vida", 0.0, () -> {
			Brawler b = new Brawler("Prova");
			b.setVida(-3);
			return b.getVida();
		});

		// setClasse: el brawler també apareix a la llista de la classe
		comprova(2, "setClasse afegeix el brawler a classe.getBrawlers()", () -> {
			Brawler b = new Brawler("Prova");
			Classe c = new Classe("ClasseProva", "Habilitat", 1.0);
			b.setClasse(c);
			return c.getBrawlers().contains(b);
		});

		// Una partida completada no pot ser la partida activa. Hauria de tornar: partida activa null
		comprova(2, "No es pot posar com a activa una partida ja completada", () -> {
			Brawler b = new Brawler("Prova");
			Partida p = new PartidaRanked("PartidaProva", "Descripció", 10, 1.0);
			b.afegirPartidaCompletada(p);
			b.setPartidaActiva(p);
			return b.getPartidaActiva() == null;
		});

		// Completar la partida activa: deixa de ser activa, surt dels participants i entra als completadors
		comprova(2, "afegirPartidaCompletada de la partida activa la treu d'activa", () -> {
			Brawler b = new Brawler("Prova");
			Partida p = new PartidaRanked("PartidaProva", "Descripció", 10, 1.0);
			b.setPartidaActiva(p);
			b.afegirPartidaCompletada(p);
			return b.getPartidaActiva() == null && !p.getParticipants().contains(b)
					&& p.getCompletadors().contains(b) && b.getPartidesCompletades().contains(p);
		});

		titol("MODEL: cost de vida");
		// Hauria de tornar: 10 - 3.5 = 6.5 i el brawler als participants
		comprova(6, "setPartidaActiva aplica el cost de vida (10 - 3.5)", 6.5, () -> {
			Brawler b = new Brawler("Prova");
			b.setPartidaActiva(new PartidaRanked("PartidaProva", "Descripció", 10, 3.5));
			return b.getVida();
		});
		comprova(6, "setPartidaActiva afegeix el brawler als participants", () -> {
			Brawler b = new Brawler("Prova");
			Partida p = new PartidaRanked("PartidaProva", "Descripció", 10, 3.5);
			b.setPartidaActiva(p);
			return p.getParticipants().contains(b);
		});
		// Hauria de tornar: 0 (la vida no baixa de zero: 2 - 3.5)
		comprova(6, "El cost de vida no deixa la vida en negatiu (2 - 3.5)", 0.0, () -> {
			Brawler b = new Brawler("Prova");
			b.setVida(2);
			b.setPartidaActiva(new PartidaRanked("PartidaProva", "Descripció", 10, 3.5));
			return b.getVida();
		});
	}

	/* =====================================================================
	 * HERÈNCIA
	 * ===================================================================== */

	private static void provesHerencia() {
		titol("HERÈNCIA");
		comprova(3, "Partida és abstracta", () -> Modifier.isAbstract(Partida.class.getModifiers()));
		comprova(3, "PartidaRanked i PartidaAmistosa hereten de Partida",
				() -> PartidaRanked.class.getSuperclass() == Partida.class
						&& PartidaAmistosa.class.getSuperclass() == Partida.class);
		comprova(3, "Estratègia SINGLE_TABLE", InheritanceType.SINGLE_TABLE,
				() -> Partida.class.getAnnotation(Inheritance.class).strategy());
		comprova(3, "Columna discriminadora anomenada \"tipus\"", "tipus",
				() -> Partida.class.getAnnotation(DiscriminatorColumn.class).name());
		comprova(3, "Les subclasses no tenen atributs propis",
				() -> senseAtributs(PartidaRanked.class) && senseAtributs(PartidaAmistosa.class));
		comprova(3, "Partida no té cap atribut Java \"tipus\"", () -> Arrays.stream(Partida.class.getDeclaredFields())
				.noneMatch(f -> f.getName().equalsIgnoreCase("tipus")));
	}

	private static boolean senseAtributs(Class<?> classe) {
		return Arrays.stream(classe.getDeclaredFields()).allMatch(f -> Modifier.isStatic(f.getModifiers()));
	}

	/* =====================================================================
	 * toString
	 * ===================================================================== */

	private static void provesToString() {
		titol("toString (brawler retornat pel DAO, fora de la sessió)");
		reiniciar();
		Brawler shelly = crida(() -> bDao.get(id("Shelly")));
		String text = crida(() -> shelly.toString());
		System.out.println("\t" + text);
		// Hauria de mostrar: nom, classe, partida activa i completades, amb el tipus de cada partida
		comprova(5, "toString sense excepcions (LazyInitialization, StackOverflow...)", () -> text != null);
		comprova(5, "Mostra la classe (Tanc)", () -> text.contains("Tanc"));
		comprova(5, "Mostra la partida activa (Atrapagemmes)", () -> text.contains("Atrapagemmes"));
		comprova(5, "Mostra les partides completades (Futbrawl)", () -> text.contains("Futbrawl"));
		comprova(5, "Mostra el tipus de cada partida (Ranked i Amistosa)",
				() -> text.toLowerCase().contains("ranked") && text.toLowerCase().contains("amistosa"));
	}

	/* =====================================================================
	 * ESTRUCTURA DEL DAO
	 * ===================================================================== */

	private static void provesEstructuraDao() {
		titol("ESTRUCTURA DEL DAO");
		comprova(4, "IBrawlerDao és una interfície que hereta de IGenericDao", () -> {
			Class<?> i = Class.forName("patroDao.IBrawlerDao");
			return i.isInterface() && Arrays.asList(i.getInterfaces()).contains(Class.forName("patroDao.IGenericDao"));
		});
		comprova(4, "BrawlerDao hereta de GenericDao", "GenericDao",
				() -> BrawlerDao.class.getSuperclass().getSimpleName());
		comprova(4, "BrawlerDao implementa IBrawlerDao",
				() -> Arrays.asList(BrawlerDao.class.getInterfaces()).contains(Class.forName("patroDao.IBrawlerDao")));
		comprova(4, "IBrawlerDao declara els 4 mètodes de l'enunciat", () -> {
			Class<?> i = Class.forName("patroDao.IBrawlerDao");
			Method torneig = i.getMethod("torneig", int.class, int.class, int.class);
			Method intercanvi = i.getMethod("intercanviTrofeus", int.class, int.class, int.class);
			Method repartir = i.getMethod("repartirRecompensa", int.class);
			Method completar = i.getMethod("completarPartides");
			return torneig.getReturnType() == Brawler[].class && intercanvi.getReturnType() == Brawler[].class
					&& List.class.isAssignableFrom(repartir.getReturnType())
					&& List.class.isAssignableFrom(completar.getReturnType());
		});
		// Penalització de l'enunciat: no es puntua aquí, només s'avisa
		for (String nom : List.of("ClasseDao", "PartidaDao", "PartidaRankedDao", "PartidaAmistosaDao")) {
			try {
				Class.forName("patroDao." + nom);
				System.out.println("\t[AVÍS] Existeix " + nom + ": fer DAO per a totes les classes té penalització d'1 punt");
			} catch (ClassNotFoundException e) {
				// correcte: no n'hi ha
			}
		}
	}

	/* =====================================================================
	 * intercanviTrofeus(int idGuanyador, int idPerdedor, int trofeus)
	 * ===================================================================== */

	private static void provesIntercanviTrofeus() {
		titol("intercanviTrofeus");

		// A) Shelly (40) rep 15 trofeus de Colt (80)
		reiniciar();
		Brawler[] r = crida(() -> bDao.intercanviTrofeus(id("Shelly"), id("Colt"), 15));
		// Hauria de tornar: [Shelly amb 55 trofeus, Colt amb 65 trofeus]
		boolean funciona = comprova(7, "A la BD, Shelly té 40 + 15 = 55 trofeus", 55, () -> trofeus("Shelly"));
		funciona |= comprova(7, "A la BD, Colt té 80 - 15 = 65 trofeus", 65, () -> trofeus("Colt"));
		comprova(8, "Retorna un array de 2 brawlers", 2, () -> r.length);
		comprova(8, "El primer és el guanyador (Shelly) amb 55 trofeus", "Shelly 55",
				() -> r[0].getNom() + " " + r[0].getTrofeus());
		comprova(8, "El segon és el perdedor (Colt) amb 65 trofeus", "Colt 65",
				() -> r[1].getNom() + " " + r[1].getTrofeus());

		// B) Poco només té 20 trofeus i n'hauria de donar 50 a Piper: falla i no canvia res
		reiniciar();
		Brawler[] r2 = crida(() -> bDao.intercanviTrofeus(id("Piper"), id("Poco"), 50));
		// Hauria de tornar: [Piper amb 20, Poco amb 20] (sense canvis) i avisar per pantalla
		comprovaNoCanvi(funciona, 7, "Sense prou trofeus: Piper continua amb 20", 20, () -> trofeus("Piper"));
		comprovaNoCanvi(funciona, 7, "Sense prou trofeus: Poco continua amb 20", 20, () -> trofeus("Poco"));
		comprova(8, "Sense prou trofeus, retorna els dos brawlers sense canvis", "Piper 20 / Poco 20",
				() -> r2[0].getNom() + " " + r2[0].getTrofeus() + " / " + r2[1].getNom() + " " + r2[1].getTrofeus());

		// C) Un brawler amb ell mateix: no canvia res
		reiniciar();
		executa(() -> bDao.intercanviTrofeus(id("Colt"), id("Colt"), 10));
		// Hauria de tornar: Colt continua amb 80
		comprovaNoCanvi(funciona, 7, "Amb ell mateix no canvia res (Colt 80)", 80, () -> trofeus("Colt"));
	}

	/* =====================================================================
	 * torneig(int idBrawler1, int idBrawler2, int rondes)
	 * ===================================================================== */

	private static void provesTorneig() {
		titol("torneig");

		// A) Shelly (Tanc, dany 2, vida 8) contra Piper (Tirador, dany 3, vida 9), 5 rondes
		//    Ronda 1: Shelly 5 - Piper 7 | Ronda 2: 2 - 5 | Ronda 3: 0 - 3 -> Shelly mor i s'atura
		//    Piper guanya i s'emporta la meitat dels trofeus de Shelly: 40 / 2 = 20
		reiniciar();
		Brawler[] a = crida(() -> bDao.torneig(id("Shelly"), id("Piper"), 5));
		// Hauria de tornar: [Shelly vida 0 i 20 trofeus, Piper vida 3 i 40 trofeus]
		boolean funciona = comprova(9, "Shelly s'ha quedat sense vida", 0.0, () -> vida("Shelly"));
		comprova(10, "S'atura quan Shelly mor: Piper es queda amb 3 de vida", 3.0, () -> vida("Piper"));
		comprova(9, "Piper rep la meitat dels trofeus de Shelly: 20 + 20 = 40", 40, () -> trofeus("Piper"));
		comprova(9, "Shelly perd la meitat dels trofeus: 40 - 20 = 20", 20, () -> trofeus("Shelly"));
		comprova(11, "Retorna Shelly i Piper actualitzats", "Shelly 0.0 20 / Piper 3.0 40",
				() -> a[0].getNom() + " " + a[0].getVida() + " " + a[0].getTrofeus() + " / "
						+ a[1].getNom() + " " + a[1].getVida() + " " + a[1].getTrofeus());

		// B) Colt (Tirador, dany 3, vida 6.5) contra Bull (Tanc, dany 2, vida 6.5), 2 rondes
		//    Ronda 1: Colt 4.5 - Bull 3.5 | Ronda 2: 2.5 - 0.5 -> Colt guanya 2-0
		//    Colt s'emporta la meitat dels trofeus de Bull: 10 / 2 = 5
		reiniciar();
		Brawler[] b = crida(() -> bDao.torneig(id("Colt"), id("Bull"), 2));
		// Hauria de tornar: [Colt vida 2.5 i 85 trofeus, Bull vida 0.5 i 5 trofeus]
		funciona |= comprova(9, "Dany per ronda: Colt acaba amb 2.5 de vida", 2.5, () -> vida("Colt"));
		funciona |= comprova(9, "Dany per ronda: Bull acaba amb 0.5 de vida", 0.5, () -> vida("Bull"));
		comprova(10, "Colt guanya per rondes i té 80 + 5 = 85 trofeus", 85, () -> trofeus("Colt"));
		comprova(10, "Bull perd i té 10 - 5 = 5 trofeus", 5, () -> trofeus("Bull"));
		comprova(11, "Retorna Colt i Bull actualitzats", "Colt 2.5 85 / Bull 0.5 5",
				() -> b[0].getNom() + " " + b[0].getVida() + " " + b[0].getTrofeus() + " / "
						+ b[1].getNom() + " " + b[1].getVida() + " " + b[1].getTrofeus());

		// C) Colt (dany 3, vida 6.5) contra Poco (Suport, dany 1, vida 10), 2 rondes
		//    Ronda 1: Colt 5.5 - Poco 7 (Poco) | Ronda 2: 4.5 - 4 (Colt) -> 1-1: EMPAT, no hi ha intercanvi
		reiniciar();
		executa(() -> bDao.torneig(id("Colt"), id("Poco"), 2));
		// Hauria de tornar: vides 4.5 i 4.0, trofeus sense canvis (80 i 20)
		comprova(9, "Les vides baixen (Colt 4.5, Poco 4.0)", "4.5 4.0", () -> vida("Colt") + " " + vida("Poco"));
		comprovaNoCanvi(funciona, 10, "Empat 1-1: Colt conserva els 80 trofeus", 80, () -> trofeus("Colt"));
		comprovaNoCanvi(funciona, 10, "Empat 1-1: Poco conserva els 20 trofeus", 20, () -> trofeus("Poco"));

		// D) Bull contra Frank: tots dos Tanc amb 6.5 de vida -> totes les rondes empatades
		reiniciar();
		executa(() -> bDao.torneig(id("Bull"), id("Frank"), 2));
		// Hauria de tornar: tots dos amb 2.5 de vida i els trofeus sense canvis (10 i 85)
		comprovaNoCanvi(funciona, 10, "Rondes empatades: no hi ha intercanvi (Bull 10, Frank 85)", "10 85",
				() -> trofeus("Bull") + " " + trofeus("Frank"));

		// E) Un brawler contra ell mateix: no canvia res
		reiniciar();
		executa(() -> bDao.torneig(id("Colt"), id("Colt"), 3));
		comprovaNoCanvi(funciona, 10, "Contra ell mateix no canvia res (Colt 6.5 de vida)", 6.5, () -> vida("Colt"));
	}

	/* =====================================================================
	 * repartirRecompensa(int trofeus)
	 * ===================================================================== */

	private static void provesRepartirRecompensa() {
		titol("repartirRecompensa(15)");
		reiniciar();
		List<Brawler> llista = crida(() -> bDao.repartirRecompensa(15));
		// Hauria de tornar els 8 brawlers. Multiplicadors: BRONZE x1, PLATA x2, OR x3, DIAMANT x4
		boolean funciona = comprova(16, "Shelly (BRONZE): 40 + 15 = 55", 55, () -> trofeus("Shelly"));
		funciona |= comprova(16, "Piper (OR): 20 + 45 = 65", 65, () -> trofeus("Piper"));
		funciona |= comprova(16, "Poco (BRONZE): 20 + 15 = 35", 35, () -> trofeus("Poco"));
		funciona |= comprova(16, "Mortis (DIAMANT): 90 + 60 = 150", 150, () -> trofeus("Mortis"));
		// Canvi de rang: més de 100 trofeus -> puja un rang i els trofeus a 0
		comprova(17, "Colt (PLATA): 80 + 30 = 110 -> puja a OR", Rang.OR, () -> rang("Colt"));
		comprova(17, "Colt es queda amb 0 trofeus", 0, () -> trofeus("Colt"));
		comprovaNoCanvi(funciona, 17, "Frank: 85 + 15 = 100 justos -> NO puja (cal superar 100)", Rang.BRONZE,
				() -> rang("Frank"));
		comprovaNoCanvi(funciona, 17, "Mortis ja és DIAMANT: es queda DIAMANT", Rang.DIAMANT, () -> rang("Mortis"));
		comprova(18, "Retorna els 8 brawlers", 8, () -> llista.size());
		comprova(18, "La llista retornada té Colt a OR amb 0 trofeus", "OR 0", () -> {
			Brawler colt = buscar(llista, "Colt");
			return colt.getRang() + " " + colt.getTrofeus();
		});
		comprova(18, "La llista retornada té Mortis amb 150 trofeus", 150, () -> buscar(llista, "Mortis").getTrofeus());
	}

	/* =====================================================================
	 * completarPartides()
	 * ===================================================================== */

	private static void provesCompletarPartides() {
		titol("completarPartides");
		reiniciar();
		List<Brawler> llista = crida(() -> bDao.completarPartides());

		// Consulta: hauria de tornar els 7 brawlers que tenien partida activa (tots menys Poco)
		comprova(12, "Retorna 7 brawlers", 7, () -> llista.size());
		comprova(12, "Poco no hi és (no tenia partida activa)", () -> buscarOpcional(llista, "Poco") == null);

		// Llistes: ningú té partida activa, Shelly té 2 completades, Supervivència ja no té participants
		boolean funciona = comprova(13, "Cap brawler té partida activa", 0L, () -> DadesProva.numBrawlersAmbPartidaActiva());
		comprova(13, "Shelly té 2 partides completades (Futbrawl i Atrapagemmes)", 2L, () -> numCompletades("Shelly"));
		comprova(13, "Supervivència: 0 participants", 0L, () -> numParticipants("Supervivència"));
		comprova(13, "Supervivència: 3 completadors (Colt, Bull, Frank)", 3L, () -> numCompletadors("Supervivència"));
		comprova(13, "Atrapagemmes: 2 completadors (Poco i Shelly)", 2L, () -> numCompletadors("Atrapagemmes"));
		comprova(13, "La partida activa de Shelly és null", null, () -> partidaActiva("Shelly"));

		// Trofeus, rangs i vida
		funciona |= comprova(14, "Shelly (Ranked +30): 40 + 30 = 70", 70, () -> trofeus("Shelly"));
		comprova(14, "Colt (Ranked +60): 140 -> puja a OR amb 0 trofeus", "OR 0",
				() -> rang("Colt") + " " + trofeus("Colt"));
		comprova(14, "Frank (Ranked +60): 145 -> puja a PLATA amb 0 trofeus", "PLATA 0",
				() -> rang("Frank") + " " + trofeus("Frank"));
		comprova(14, "Mortis (DIAMANT, 130 trofeus): +5 de vida -> 7.0", 7.0, () -> vida("Mortis"));
		comprova(14, "Mortis continua DIAMANT amb 130 trofeus", "DIAMANT 130",
				() -> rang("Mortis") + " " + trofeus("Mortis"));
		comprova(14, "El Primo (DIAMANT amb 10 de vida): imprimeix BRAWLER SUPREM", () -> haImpres("BRAWLER SUPREM"));
		comprovaNoCanvi(funciona, 14, "Piper (Amistosa): no rep trofeus, continua amb 20", 20, () -> trofeus("Piper"));
		comprovaNoCanvi(funciona, 14, "El Primo es queda amb 10 de vida", 10.0, () -> vida("El Primo"));
		comprovaNoCanvi(funciona, 12, "Poco (sense partida activa) no canvia: 20 trofeus", 20, () -> trofeus("Poco"));

		// Valors retornats
		comprova(15, "La llista retornada té Shelly amb 70 trofeus", 70, () -> buscar(llista, "Shelly").getTrofeus());
		comprova(15, "La llista retornada té Frank a PLATA", Rang.PLATA, () -> buscar(llista, "Frank").getRang());
		comprova(15, "La llista retornada té Mortis amb 7.0 de vida", 7.0, () -> buscar(llista, "Mortis").getVida());
		comprova(15, "A la llista retornada, Bull ja no té partida activa",
				() -> buscar(llista, "Bull").getPartidaActiva() == null);
	}

	/* =====================================================================
	 * Auxiliars
	 * ===================================================================== */

	// Torna a crear les dades de prova. Si falla (p. ex. per un error als setters), ho mostra i continua
	private static void reiniciar() {
		executa(DadesProva::reiniciar);
	}

	private static Brawler buscar(List<Brawler> llista, String nom) {
		Brawler b = buscarOpcional(llista, nom);
		if (b == null) {
			throw new IllegalStateException(nom + " no és a la llista retornada");
		}
		return b;
	}

	private static Brawler buscarOpcional(List<Brawler> llista, String nom) {
		for (Brawler b : llista) {
			if (b != null && nom.equals(b.getNom())) {
				return b;
			}
		}
		return null;
	}
}
