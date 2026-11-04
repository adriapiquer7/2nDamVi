package SpringROA.SpringROA.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import SpringROA.SpringROA.model.Hexagon;
import SpringROA.SpringROA.model.Partida;

public interface HexagonRepository extends JpaRepository<Hexagon, Integer> {

	List<Hexagon> findByPartidaOrderByCodi(Partida partida);

	List<Hexagon> findByPartidaAndNumero(Partida partida, Integer numero);
}
