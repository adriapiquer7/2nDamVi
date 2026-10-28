package SpringROA.SpringROA.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

@Entity
@Table(name = "Partida")
public class Partida {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "estat", nullable = false, length = 11)
	private EstatPartida estat;

	@NotNull(message = "La data d'inici és obligatòria")
	@PastOrPresent(message = "La data d'inici no pot ser futura")
	@Column(name = "dataInici", nullable = false)
	private LocalDate dataInici;

	@NotNull
	@Min(value = 1, message = "La ronda ha de ser com a mínim 1")
	@Column(name = "ronda", nullable = false)
	private Integer ronda;

	@NotNull
	@Min(value = 0, message = "La tirada ha de ser com a mínim 0")
	@Max(value = 12, message = "La tirada ha de ser com a màxim 12")
	@Column(name = "ultimaTirada", nullable = false)
	private Integer ultimaTirada;

	@Column(name = "dausTirats", nullable = false)
	private boolean dausTirats;

	@Column(name = "lladrePendent", nullable = false)
	private boolean lladrePendent;

	@NotNull
	@Min(value = 2, message = "Els punts per guanyar han de ser com a mínim 2")
	@Max(value = 50, message = "Els punts per guanyar han de ser com a màxim 50")
	@Column(name = "puntsPerGuanyar", nullable = false)
	private Integer puntsPerGuanyar;

	// Relacions que no tenen valor quan es crea la partida: nullable
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lladre_id")
	private Hexagon lladre;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "actiu_id")
	private Jugador actiu;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "guanyador_id")
	private Jugador guanyador;

	// Relacions inverses (no guarden res a la taula de Partida)
	@OneToMany(mappedBy = "partida")
	private List<Jugador> jugadors = new ArrayList<>();

	@OneToMany(mappedBy = "partida")
	private List<Hexagon> tauler = new ArrayList<>();

	@OneToMany(mappedBy = "partida")
	private List<CartaDesenvolupament> pila = new ArrayList<>();

	public Partida() {
	}

	public Partida(int puntsPerGuanyar) {
		this.estat = EstatPartida.PREPARACIO;
		this.dataInici = LocalDate.now();
		this.ronda = 1;
		this.ultimaTirada = 0;
		this.dausTirats = false;
		this.lladrePendent = false;
		this.puntsPerGuanyar = puntsPerGuanyar;
	}

	public Integer getId() { return id; }

	public EstatPartida getEstat() { return estat; }
	public void setEstat(EstatPartida estat) { this.estat = estat; }

	public LocalDate getDataInici() { return dataInici; }

	public Integer getRonda() { return ronda; }
	public void setRonda(Integer ronda) { this.ronda = ronda; }

	public Integer getUltimaTirada() { return ultimaTirada; }
	public void setUltimaTirada(Integer ultimaTirada) { this.ultimaTirada = ultimaTirada; }

	public boolean isDausTirats() { return dausTirats; }
	public void setDausTirats(boolean dausTirats) { this.dausTirats = dausTirats; }

	public boolean isLladrePendent() { return lladrePendent; }
	public void setLladrePendent(boolean lladrePendent) { this.lladrePendent = lladrePendent; }

	public Integer getPuntsPerGuanyar() { return puntsPerGuanyar; }

	public Hexagon getLladre() { return lladre; }
	public void setLladre(Hexagon lladre) { this.lladre = lladre; }

	public Jugador getActiu() { return actiu; }
	public void setActiu(Jugador actiu) { this.actiu = actiu; }

	public Jugador getGuanyador() { return guanyador; }
	public void setGuanyador(Jugador guanyador) { this.guanyador = guanyador; }

	public List<Jugador> getJugadors() { return jugadors; }
	public List<Hexagon> getTauler() { return tauler; }

	@Override
	public String toString() {
		return "Partida [id=" + id + ", estat=" + estat + ", ronda=" + ronda + "]";
	}
}
