package SpringROA.SpringROA.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;

@Entity
public abstract class Assentament extends Construccio {

	@NotNull
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vertex_id", nullable = true, unique = true)
	private Vertex vertex;

	protected Assentament() {
	}

	protected Assentament(Jugador propietari, Vertex vertex) {
		super(propietari);
		this.vertex = vertex;
	}

	public Vertex getVertex() { return vertex; }
}
