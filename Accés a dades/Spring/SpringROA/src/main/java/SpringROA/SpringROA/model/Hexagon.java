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
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "Hexagon", uniqueConstraints = @UniqueConstraint(columnNames = { "partida_id", "codi" }))
public class Hexagon {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	/** Id que té l'hexàgon al CSV */
	@NotNull
	@Column(name = "codi", nullable = false)
	private Integer codi;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "terreny", nullable = false, length = 10)
	private TipusTerreny terreny;

	/** Null al desert */
	@Min(value = 2, message = "El número mínim és 2")
	@Max(value = 12, message = "El número màxim és 12")
	@Column(name = "numero")
	private Integer numero;

	/** Coordenades axials (informatives). Poden ser negatives. */
	@NotNull
	@Column(name = "q", nullable = false)
	private Integer q;

	@NotNull
	@Column(name = "r", nullable = false)
	private Integer r;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "partida_id", nullable = false)
	private Partida partida;

	public Hexagon() {
	}

	public Hexagon(Partida partida, Integer codi, TipusTerreny terreny, Integer numero, Integer q, Integer r) {
		this.partida = partida;
		this.codi = codi;
		this.terreny = terreny;
		this.numero = numero;
		this.q = q;
		this.r = r;
	}

	public Integer getId() { return id; }
	public Integer getCodi() { return codi; }
	public TipusTerreny getTerreny() { return terreny; }
	public void setTerreny(TipusTerreny terreny) { this.terreny = terreny; }
	public Integer getNumero() { return numero; }
	public void setNumero(Integer numero) { this.numero = numero; }
	public Integer getQ() { return q; }
	public Integer getR() { return r; }
	public Partida getPartida() { return partida; }

	@Override
	public String toString() {
		return "Hexagon [id=" + id + ", codi=" + codi + ", terreny=" + terreny + ", numero=" + numero + "]";
	}
}
