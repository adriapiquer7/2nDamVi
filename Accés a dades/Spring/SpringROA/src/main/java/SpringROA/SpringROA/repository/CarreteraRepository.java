package SpringROA.SpringROA.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import SpringROA.SpringROA.model.Aresta;
import SpringROA.SpringROA.model.Carretera;
import SpringROA.SpringROA.model.Jugador;
import SpringROA.SpringROA.model.Vertex;

public interface CarreteraRepository extends JpaRepository<Carretera, Integer> {

	long countByPropietari(Jugador propietari);

	long countByAresta(Aresta aresta);

	@Query("SELECT COUNT(c) FROM Carretera c WHERE c.propietari = :j "
			+ "AND (c.aresta.vertexA = :v OR c.aresta.vertexB = :v)")
	long carreteresPropiesAmb(@Param("j") Jugador jugador, @Param("v") Vertex vertex);
}
