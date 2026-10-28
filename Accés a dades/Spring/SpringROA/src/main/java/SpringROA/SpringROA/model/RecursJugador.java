package SpringROA.SpringROA.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "RecursJugador", uniqueConstraints = @UniqueConstraint(columnNames = { "jugador_id", "tipus" }))
public class RecursJugador {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "jugador_id", nullable = false)
	private Jugador jugador;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "tipus", nullable = false, length = 7)
	private TipusRecurs tipus;

	@NotNull
	@Min(value = 0, message = "La quantitat no pot ser negativa")
	@Column(name = "quantitat", nullable = false)
	private Integer quantitat;

	public RecursJugador() {
	}

	public RecursJugador(Jugador jugador, TipusRecurs tipus) {
		this.jugador = jugador;
		this.tipus = tipus;
		this.quantitat = 0;
	}

	public Integer getId() { return id; }
	public TipusRecurs getTipus() { return tipus; }
	public Integer getQuantitat() { return quantitat; }
	public void setQuantitat(Integer quantitat) { this.quantitat = quantitat; }

	@Override
	public String toString() {
		return "RecursJugador [tipus=" + tipus + ", quantitat=" + quantitat + "]";
	}
}
