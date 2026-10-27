package SpringROA.SpringROA;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/** Ciutat: substitueix un poblat propi. 2 punts de victòria i produeix 2 recursos. */
@Entity
@DiscriminatorValue("CIUTAT")
public class Ciutat extends Assentament {

	public Ciutat() {
	}

	public Ciutat(Jugador propietari, Vertex vertex) {
		super(propietari, vertex);
	}
}
