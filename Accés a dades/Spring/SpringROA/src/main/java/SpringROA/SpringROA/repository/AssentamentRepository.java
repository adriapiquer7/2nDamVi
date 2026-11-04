package SpringROA.SpringROA.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import SpringROA.SpringROA.model.Assentament;
import SpringROA.SpringROA.model.Hexagon;
import SpringROA.SpringROA.model.Jugador;
import SpringROA.SpringROA.model.Vertex;

/** Poblats i ciutats (un vèrtex només pot tenir un assentament) */
public interface AssentamentRepository extends JpaRepository<Assentament, Integer> {

	Optional<Assentament> findByVertex(Vertex vertex);

	boolean existsByVertex(Vertex vertex);

	long countByPropietariAndVertex(Jugador propietari, Vertex vertex);

	/** Assentaments situats en algun dels vèrtexs d'un hexàgon */
	@Query("SELECT a FROM Assentament a WHERE a.vertex IN (SELECT hv.vertex FROM HexagonVertex hv WHERE hv.hexagon = :h)")
	List<Assentament> assentamentsDelHexagon(@Param("h") Hexagon hexagon);

	/** Regla de la distància: nombre d'assentaments als vèrtexs adjacents a un vèrtex */
	@Query("SELECT COUNT(x) FROM Assentament x WHERE "
			+ "x.vertex IN (SELECT ar.vertexB FROM Aresta ar WHERE ar.vertexA = :v) OR "
			+ "x.vertex IN (SELECT ar.vertexA FROM Aresta ar WHERE ar.vertexB = :v)")
	long assentamentsAdjacents(@Param("v") Vertex vertex);

	/** Vèrtexs amb un assentament del jugador que encara no tenen cap carretera seva */
	@Query("SELECT x.vertex FROM Assentament x WHERE x.propietari = :j "
			+ "AND NOT EXISTS (SELECT c FROM Carretera c WHERE c.propietari = :j "
			+ "AND (c.aresta.vertexA = x.vertex OR c.aresta.vertexB = x.vertex))")
	List<Vertex> vertexsSenseCarretera(@Param("j") Jugador jugador);
}
