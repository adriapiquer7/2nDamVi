package SpringROA.SpringROA.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import SpringROA.SpringROA.model.Ciutat;
import SpringROA.SpringROA.model.Jugador;

public interface CiutatRepository extends JpaRepository<Ciutat, Integer> {

	long countByPropietari(Jugador propietari);
}
