package SpringROA.SpringROA;

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
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/** Relació Hexagon-Vertex amb la posició (0-5) del vèrtex dins l'hexàgon. */
@Entity
@Table(name = "HexagonVertex", uniqueConstraints = @UniqueConstraint(columnNames = { "hexagon_id", "posicio" }))
public class HexagonVertex {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "hexagon_id", nullable = false)
	private Hexagon hexagon;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "vertex_id", nullable = false)
	private Vertex vertex;

	@NotNull
	@Min(value = 0, message = "La posició ha d'estar entre 0 i 5")
	@Max(value = 5, message = "La posició ha d'estar entre 0 i 5")
	@Column(name = "posicio", nullable = false)
	private Integer posicio;

	public HexagonVertex() {
	}

	public HexagonVertex(Hexagon hexagon, Vertex vertex, Integer posicio) {
		this.hexagon = hexagon;
		this.vertex = vertex;
		this.posicio = posicio;
	}

	public Integer getId() { return id; }
	public Hexagon getHexagon() { return hexagon; }
	public Vertex getVertex() { return vertex; }
	public Integer getPosicio() { return posicio; }
}
