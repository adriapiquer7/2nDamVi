package SpringROA.SpringROA.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import SpringROA.SpringROA.model.Jugador;
import SpringROA.SpringROA.model.Partida;

public interface JugadorRepository extends JpaRepository<Jugador, Integer> {

	List<Jugador> findByPartidaOrderByOrdreTorn(Partida partida);

	Optional<Jugador> findByPartidaAndOrdreTorn(Partida partida, Integer ordreTorn);
}
