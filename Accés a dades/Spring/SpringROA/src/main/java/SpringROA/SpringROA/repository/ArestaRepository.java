package SpringROA.SpringROA.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import SpringROA.SpringROA.model.Aresta;

public interface ArestaRepository extends JpaRepository<Aresta, Integer> {

	@Query("SELECT ar.id FROM Aresta ar WHERE ar.vertexA.id = :v OR ar.vertexB.id = :v")
	List<Integer> idsAdjacents(@Param("v") int idVertex);
}