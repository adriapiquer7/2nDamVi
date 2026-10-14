package modelDades;

import java.util.HashSet;
import java.util.Set;

/*
 * Completa la classe segons l'enunciat: anotacions, constructors, getters, setters
 * (amb els controls que demana l'enunciat), hashCode, equals i toString.
 * NO canvieu els noms ni els tipus dels atributs: el Main i DadesProva els fan servir.
 */
public class Partida {

	private Integer id;
	private String titol;
	private String descripcio;
	private int recompensaTrofeus;
	private double costVida;

	private Set<Brawler> participants = new HashSet<>();
	private Set<Brawler> completadors = new HashSet<>();
}
