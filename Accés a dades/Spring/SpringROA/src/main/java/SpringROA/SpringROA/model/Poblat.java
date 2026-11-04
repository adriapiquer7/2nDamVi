package SpringROA.SpringROA.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/** Poblat: 1 punt de victòria i produeix 1 recurs. */
@Entity
@DiscriminatorValue("POBLAT")
public class Poblat extends Assentament {

	public Poblat() {
	}

	public Poblat(Jugador propietari, Vertex vertex) {
		super(propietari, vertex);
	}
}
