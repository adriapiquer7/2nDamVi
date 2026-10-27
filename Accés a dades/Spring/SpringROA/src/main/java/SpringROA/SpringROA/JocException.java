package SpringROA.SpringROA;

/**
 * Error de regla de joc (torn incorrecte, recursos insuficients, etc.).
 * El missatge és el que es mostra a l'usuari. Quan es llença dins d'una
 * transacció, Hibernate fa rollback i no queda cap canvi a mitges.
 */
public class JocException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public JocException(String missatge) {
		super(missatge);
	}
}
