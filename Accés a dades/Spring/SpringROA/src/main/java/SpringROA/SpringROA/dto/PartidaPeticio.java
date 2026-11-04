package SpringROA.SpringROA.dto;

import java.util.List;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** JSON d'entrada de POST /partida: {"nomsJugadors": ["Anna","Bernat"], "puntsPerGuanyar": 10} */
public record PartidaPeticio(
		@NotNull(message = "Cal indicar la llista de jugadors")
		@Size(min = 2, max = 4, message = "Hi ha d'haver entre 2 i 4 jugadors")
		List<@NotBlank(message = "El nom d'un jugador no pot estar buit") String> nomsJugadors,

		@Min(value = 2, message = "Els punts per guanyar han de ser com a mínim 2")
		@Max(value = 50, message = "Els punts per guanyar han de ser com a màxim 50")
		Integer puntsPerGuanyar) {
}
