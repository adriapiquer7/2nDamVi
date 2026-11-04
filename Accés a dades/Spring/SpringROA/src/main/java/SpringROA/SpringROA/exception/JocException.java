package SpringROA.SpringROA.exception;

import org.springframework.http.HttpStatus;

/**
 * Error de regla de joc (torn incorrecte, recursos insuficients, etc.).
 * El missatge es mostra a l'usuari i el codi d'estat HTTP el tria qui la llança
 * (per defecte 409). Com és una RuntimeException, dins d'un @Transactional
 * fa rollback automàticament i no queda cap canvi a mitges.
 */
public class JocException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private final HttpStatus status;

	public JocException(String missatge) {
		this(missatge, HttpStatus.CONFLICT);
	}

	public JocException(String missatge, HttpStatus status) {
		super(missatge);
		this.status = status;
	}

	public HttpStatus getStatus() {
		return status;
	}
}
