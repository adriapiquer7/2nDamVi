package SpringROA.SpringROA.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import SpringROA.SpringROA.model.Hexagon;
import SpringROA.SpringROA.model.HexagonVertex;
import SpringROA.SpringROA.model.Vertex;

public interface HexagonVertexRepository extends JpaRepository<HexagonVertex, Integer> {

	List<HexagonVertex> findByHexagonOrderByPosicio(Hexagon hexagon);

	@Query("SELECT hv.hexagon FROM HexagonVertex hv WHERE hv.vertex = :v")
	List<Hexagon> hexagonsDelVertex(@Param("v") Vertex vertex);
}
