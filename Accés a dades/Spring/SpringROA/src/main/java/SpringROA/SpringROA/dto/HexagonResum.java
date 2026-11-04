package SpringROA.SpringROA.dto;

import SpringROA.SpringROA.model.Hexagon;
import SpringROA.SpringROA.model.TipusTerreny;

public record HexagonResum(Integer id, Integer codi, TipusTerreny terreny, Integer numero, boolean lladre) {

	public static HexagonResum de(Hexagon h, boolean lladre) {
		return new HexagonResum(h.getId(), h.getCodi(), h.getTerreny(), h.getNumero(), lladre);
	}
}
