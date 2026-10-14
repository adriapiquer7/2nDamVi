package patroDao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import modelDades.Brawler;
import modelDades.Classe;
import modelDades.Partida;
import modelDades.PartidaAmistosa;
import modelDades.PartidaRanked;
import modelDades.Rang;

/*
 * =====================================================================================
 *  DADES DE PROVA. NO L'HEU DE MODIFICAR.
 * =====================================================================================
 * - reiniciar(): esborra totes les dades i torna a crear les de la taula de sota.
 *   El Main el crida abans de provar cada funció: així un error en una funció
 *   no afecta les proves de les altres.
 * - La resta de mètodes consulten la BD DIRECTAMENT (sense passar pel vostre DAO)
 *   per comprovar què ha quedat desat de veritat.
 *
 * Fa servir els vostres constructors i setters: Classe(nom, habilitat, dany),
 * PartidaRanked/PartidaAmistosa(titol, descripcio, recompensaTrofeus, costVida), Brawler(nom),
 * setClasse, setPartidaActiva, afegirPartidaCompletada, setRang, setTrofeus i setVida.
 *
 * DADES (després de reiniciar):
 *
 *   Classes:  Tanc (dany 2.0), Tirador (3.0), Assassí (4.5), Suport (1.0)
 *
 *   Partides: Atrapagemmes  RANKED    recompensa 30, cost de vida 2.0
 *             Supervivència RANKED    recompensa 60, cost de vida 3.5
 *             Atracament    RANKED    recompensa 40, cost de vida 1.0
 *             Futbrawl      AMISTOSA  recompensa 20, cost de vida 1.0
 *
 *   Brawler   Classe   Rang     Trofeus  Vida  Partida activa   Completades
 *   Shelly    Tanc     BRONZE   40       8.0   Atrapagemmes     Futbrawl
 *   Colt      Tirador  PLATA    80       6.5   Supervivència    -
 *   Piper     Tirador  OR       20       9.0   Futbrawl         -
 *   Mortis    Assassí  DIAMANT  90       2.0   Atracament       -
 *   El Primo  Tanc     DIAMANT  95       10.0  Atracament       -
 *   Poco      Suport   BRONZE   20       10.0  -                Atrapagemmes
 *   Bull      Tanc     BRONZE   10       6.5   Supervivència    -
 *   Frank     Tanc     BRONZE   85       6.5   Supervivència    -
 *
 * La vida de la taula ja té descomptat el cost de la partida activa. Per si el vostre
 * setPartidaActiva encara no l'aplica bé, aquí la fixem al final amb setVida:
 * així les proves de les funcions del DAO no depenen d'aquest setter.
 */
public final class DadesProva {

	private DadesProva() {
	}

	private static SessionFactory sf() {
		return SessionManager.getSessionFactory();
	}

	/* =====================================================================
	 * reiniciar
	 * ===================================================================== */

	public static void reiniciar() {
		try (Session session = sf().openSession()) {
			Transaction tx = session.beginTransaction();
			try {
				// Esborrat massiu (HQL): primer els brawlers (Hibernate buida també la taula de completades)
				session.createMutationQuery("DELETE FROM Brawler").executeUpdate();
				session.createMutationQuery("DELETE FROM Partida").executeUpdate();
				session.createMutationQuery("DELETE FROM Classe").executeUpdate();
				crear(session);
				tx.commit();
			} catch (RuntimeException e) {
				if (tx.isActive()) {
					tx.rollback();
				}
				throw e;
			}
		}
	}

	/*
	 * Cada entitat es desa (persist) ABANS d'enllaçar-la amb les altres: així ja té id
	 * i els vostres equals/hashCode per id funcionen dins els Set.
	 */
	private static void crear(Session session) {
		Classe tanc = new Classe("Tanc", "Puny de ferro", 2.0);
		Classe tirador = new Classe("Tirador", "Tret llarg", 3.0);
		Classe assassi = new Classe("Assassí", "Atac per l'esquena", 4.5);
		Classe suport = new Classe("Suport", "Curació", 1.0);
		for (Classe c : List.of(tanc, tirador, assassi, suport)) {
			session.persist(c);
		}

		Partida gemmes = new PartidaRanked("Atrapagemmes", "Recull 10 gemmes i aguanta-les", 30, 2.0);
		Partida supervivencia = new PartidaRanked("Supervivència", "L'últim que queda dempeus guanya", 60, 3.5);
		Partida atracament = new PartidaRanked("Atracament", "Destrueix la caixa forta rival", 40, 1.0);
		Partida futbrawl = new PartidaAmistosa("Futbrawl", "Partit de futbol per practicar", 20, 1.0);
		for (Partida p : List.of(gemmes, supervivencia, atracament, futbrawl)) {
			session.persist(p);
		}

		Brawler shelly = brawler(session, "Shelly", tanc, Rang.BRONZE, 40);
		shelly.afegirPartidaCompletada(futbrawl);
		shelly.setPartidaActiva(gemmes);
		shelly.setVida(8.0);

		Brawler colt = brawler(session, "Colt", tirador, Rang.PLATA, 80);
		colt.setPartidaActiva(supervivencia);
		colt.setVida(6.5);

		Brawler piper = brawler(session, "Piper", tirador, Rang.OR, 20);
		piper.setPartidaActiva(futbrawl);
		piper.setVida(9.0);

		Brawler mortis = brawler(session, "Mortis", assassi, Rang.DIAMANT, 90);
		mortis.setPartidaActiva(atracament);
		mortis.setVida(2.0);

		Brawler primo = brawler(session, "El Primo", tanc, Rang.DIAMANT, 95);
		primo.setPartidaActiva(atracament);
		primo.setVida(10.0);

		Brawler poco = brawler(session, "Poco", suport, Rang.BRONZE, 20);
		poco.afegirPartidaCompletada(gemmes);
		poco.setVida(10.0);

		Brawler bull = brawler(session, "Bull", tanc, Rang.BRONZE, 10);
		bull.setPartidaActiva(supervivencia);
		bull.setVida(6.5);

		Brawler frank = brawler(session, "Frank", tanc, Rang.BRONZE, 85);
		frank.setPartidaActiva(supervivencia);
		frank.setVida(6.5);
		// Els canvis fets després del persist es desen al commit (els objectes són gestionats)
	}

	private static Brawler brawler(Session session, String nom, Classe classe, Rang rang, int trofeus) {
		Brawler b = new Brawler(nom);
		b.setClasse(classe);
		b.setRang(rang);
		b.setTrofeus(trofeus);
		session.persist(b);
		return b;
	}

	/* =====================================================================
	 * Consultes de comprovació (directes a la BD)
	 * ===================================================================== */

	public static int id(String nom) {
		return valor("SELECT b.id FROM Brawler b WHERE b.nom = :p", nom, Integer.class);
	}

	public static int trofeus(String nom) {
		return valor("SELECT b.trofeus FROM Brawler b WHERE b.nom = :p", nom, Integer.class);
	}

	public static double vida(String nom) {
		return valor("SELECT b.vida FROM Brawler b WHERE b.nom = :p", nom, Double.class);
	}

	public static Rang rang(String nom) {
		return valor("SELECT b.rang FROM Brawler b WHERE b.nom = :p", nom, Rang.class);
	}

	// Títol de la partida activa (null si no en té)
	public static String partidaActiva(String nom) {
		return valor("SELECT p.titol FROM Brawler b LEFT JOIN b.partidaActiva p WHERE b.nom = :p", nom, String.class);
	}

	public static long numCompletades(String nom) {
		return valor("SELECT COUNT(p) FROM Brawler b JOIN b.partidesCompletades p WHERE b.nom = :p", nom, Long.class);
	}

	public static long numParticipants(String titol) {
		return valor("SELECT COUNT(b) FROM Partida p JOIN p.participants b WHERE p.titol = :p", titol, Long.class);
	}

	public static long numCompletadors(String titol) {
		return valor("SELECT COUNT(b) FROM Partida p JOIN p.completadors b WHERE p.titol = :p", titol, Long.class);
	}

	public static long numBrawlersAmbPartidaActiva() {
		try (Session session = sf().openSession()) {
			return session.createQuery("SELECT COUNT(b) FROM Brawler b WHERE b.partidaActiva IS NOT NULL", Long.class)
					.getSingleResult();
		}
	}

	public static long numBrawlers() {
		try (Session session = sf().openSession()) {
			return session.createQuery("SELECT COUNT(b) FROM Brawler b", Long.class).getSingleResult();
		}
	}

	private static <T> T valor(String hql, String parametre, Class<T> tipus) {
		try (Session session = sf().openSession()) {
			return session.createQuery(hql, tipus).setParameter("p", parametre).getSingleResult();
		}
	}
}
