package modelDades;

import java.util.HashSet;
import java.util.Set;

/*
 * Completa la classe segons l'enunciat: anotacions, constructors, getters, setters
 * (amb els controls que demana l'enunciat), hashCode, equals i toString.
 * NO canvieu els noms ni els tipus dels atributs: el Main i DadesProva els fan servir.
 */
public class Brawler {

	private Integer id;
	private String nom;
	private Rang rang;
	private int trofeus;
	private double vida;

	private Classe classe;
	private Partida partidaActiva;
	private Set<Partida> partidesCompletades = new HashSet<>();
}
