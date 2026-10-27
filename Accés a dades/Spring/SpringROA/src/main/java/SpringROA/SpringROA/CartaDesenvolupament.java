package SpringROA.SpringROA;

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
import jakarta.validation.constraints.NotNull;

/** Carta de desenvolupament (part avançada). Mentre és a la pila, jugador és null. */
@Entity
@Table(name = "CartaDesenvolupament")
public class CartaDesenvolupament {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "partida_id", nullable = false)
	private Partida partida;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "jugador_id")
	private Jugador jugador;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "tipus", nullable = false, length = 13)
	private TipusCarta tipus;

	@Column(name = "usada", nullable = false)
	private boolean usada;

	@Column(name = "rondaCompra")
	private Integer rondaCompra;

	public CartaDesenvolupament() {
	}

	public CartaDesenvolupament(Partida partida, TipusCarta tipus) {
		this.partida = partida;
		this.tipus = tipus;
		this.usada = false;
	}

	public Integer getId() { return id; }
	public TipusCarta getTipus() { return tipus; }
	public boolean isUsada() { return usada; }
	public void setUsada(boolean usada) { this.usada = usada; }
	public Integer getRondaCompra() { return rondaCompra; }
	public void setRondaCompra(Integer rondaCompra) { this.rondaCompra = rondaCompra; }
	public Jugador getJugador() { return jugador; }
	public void setJugador(Jugador jugador) { this.jugador = jugador; }
}
