package SpringROA.SpringROA.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "Vertex", uniqueConstraints = @UniqueConstraint(columnNames = { "partida_id", "codi" }))
public class Vertex {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	/** Id que té el vèrtex al CSV */
	@NotNull
	@Column(name = "codi", nullable = false)
	private Integer codi;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "partida_id", nullable = false)
	private Partida partida;

	public Vertex() {
	}

	public Vertex(Partida partida, Integer codi) {
		this.partida = partida;
		this.codi = codi;
	}

	public Integer getId() { return id; }
	public Integer getCodi() { return codi; }
	public Partida getPartida() { return partida; }

	@Override
	public String toString() {
		return "Vertex [id=" + id + ", codi=" + codi + "]";
	}
}
