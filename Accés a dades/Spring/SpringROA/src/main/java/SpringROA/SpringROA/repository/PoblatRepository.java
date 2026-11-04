package SpringROA.SpringROA.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import SpringROA.SpringROA.model.Jugador;
import SpringROA.SpringROA.model.Poblat;

public interface PoblatRepository extends JpaRepository<Poblat, Integer> {

	long countByPropietari(Jugador propietari);
}
