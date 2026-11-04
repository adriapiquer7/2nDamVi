package SpringROA.SpringROA.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Jugador", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "partida_id", "nom" }),
		@UniqueConstraint(columnNames = { "partida_id", "color" }),
		@UniqueConstraint(columnNames = { "partida_id", "ordreTorn" }) })
public class Jugador {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@NotBlank(message = "El nom és obligatori")
	@Size(max = 50, message = "El nom no pot superar els {max} caràcters")
	@Column(name = "nom", nullable = false, length = 50)
	private String nom;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "color", nullable = false, length = 7)
	private ColorJugador color;

	@NotNull
	@Min(value = 1, message = "L'ordre de torn va de 1 a 4")
	@Max(value = 4, message = "L'ordre de torn va de 1 a 4")
	@Column(name = "ordreTorn", nullable = false)
	private Integer ordreTorn;

	@NotNull
	@Min(value = 0, message = "Els cavallers jugats no poden ser negatius")
	@Column(name = "cavallersJugats", nullable = false)
	private Integer cavallersJugats;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "partida_id", nullable = false)
	private Partida partida;

	/** Inventari: 5 files RecursJugador, creades en crear el jugador */
	@OneToMany(mappedBy = "jugador", cascade = CascadeType.ALL)
	private List<RecursJugador> inventari = new ArrayList<>();

	/** Poblats, ciutats i carreteres propietat del jugador */
	@OneToMany(mappedBy = "propietari")
	private List<Construccio> construccions = new ArrayList<>();

	/** Cartes de desenvolupament a la mà (part avançada) */
	@OneToMany(mappedBy = "jugador")
	private List<CartaDesenvolupament> ma = new ArrayList<>();

	public Jugador() {
	}

	public Jugador(Partida partida, String nom, ColorJugador color, int ordreTorn) {
		this.partida = partida;
		this.nom = nom;
		this.color = color;
		this.ordreTorn = ordreTorn;
		this.cavallersJugats = 0;
		for (TipusRecurs t : TipusRecurs.values()) {
			inventari.add(new RecursJugador(this, t));
		}
	}

	public Integer getId() { return id; }
	public String getNom() { return nom; }
	public ColorJugador getColor() { return color; }
	public Integer getOrdreTorn() { return ordreTorn; }
	public Integer getCavallersJugats() { return cavallersJugats; }
	public Partida getPartida() { return partida; }

	/** Retorna la fila de l'inventari per a un tipus de recurs */
	public RecursJugador getRecurs(TipusRecurs tipus) {
		for (RecursJugador r : inventari) {
			if (r.getTipus() == tipus) {
				return r;
			}
		}
		throw new IllegalStateException("Falta la fila de recurs " + tipus + " del jugador " + nom);
	}

	public int getQuantitat(TipusRecurs tipus) {
		return getRecurs(tipus).getQuantitat();
	}

	/** Suma (o resta si és negatiu) recursos del tipus indicat */
	public void afegirRecurs(TipusRecurs tipus, int quantitat) {
		RecursJugador r = getRecurs(tipus);
		r.setQuantitat(r.getQuantitat() + quantitat);
	}

	public int getTotalRecursos() {
		int total = 0;
		for (RecursJugador r : inventari) {
			total += r.getQuantitat();
		}
		return total;
	}

	@Override
	public String toString() {
		return "Jugador [id=" + id + ", nom=" + nom + ", color=" + color + ", ordreTorn=" + ordreTorn + "]";
	}
}
