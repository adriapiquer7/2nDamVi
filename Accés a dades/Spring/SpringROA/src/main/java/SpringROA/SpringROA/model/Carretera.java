package SpringROA.SpringROA.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;

@Entity
@DiscriminatorValue("CARRETERA")
public class Carretera extends Construccio {

	@NotNull
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "aresta_id", nullable = true, unique = true)
	private Aresta aresta;

	public Carretera() {
	}

	public Carretera(Jugador propietari, Aresta aresta) {
		super(propietari);
		this.aresta = aresta;
	}

	public Aresta getAresta() { return aresta; }
}
