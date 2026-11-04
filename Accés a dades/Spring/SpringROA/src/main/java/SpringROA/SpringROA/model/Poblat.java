package SpringROA.SpringROA.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("POBLAT")
public class Poblat extends Assentament {

	public Poblat() {
	}

	public Poblat(Jugador propietari, Vertex vertex) {
		super(propietari, vertex);
	}
}
