package ProjecteHibernate;

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
@Table(name = "Aresta", uniqueConstraints = @UniqueConstraint(columnNames = { "partida_id", "codi" }))
public class Aresta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	/** Id que té l'aresta al CSV */
	@NotNull
	@Column(name = "codi", nullable = false)
	private Integer codi;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "partida_id", nullable = false)
	private Partida partida;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "vertexA_id", nullable = false)
	private Vertex vertexA;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "vertexB_id", nullable = false)
	private Vertex vertexB;

	public Aresta() {
	}

	public Aresta(Partida partida, Integer codi, Vertex vertexA, Vertex vertexB) {
		this.partida = partida;
		this.codi = codi;
		this.vertexA = vertexA;
		this.vertexB = vertexB;
	}

	public Integer getId() { return id; }
	public Integer getCodi() { return codi; }
	public Partida getPartida() { return partida; }
	public Vertex getVertexA() { return vertexA; }
	public Vertex getVertexB() { return vertexB; }

	@Override
	public String toString() {
		return "Aresta [id=" + id + ", codi=" + codi + "]";
	}
}
