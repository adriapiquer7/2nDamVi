package SpringROA.SpringROA.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CIUTAT")
public class Ciutat extends Assentament {

	public Ciutat() {
	}

	public Ciutat(Jugador propietari, Vertex vertex) {
		super(propietari, vertex);
	}
}
