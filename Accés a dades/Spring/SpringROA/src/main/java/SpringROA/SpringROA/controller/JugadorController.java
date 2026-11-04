package SpringROA.SpringROA.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import SpringROA.SpringROA.dto.JugadorRecursosResposta;
import SpringROA.SpringROA.service.JugadorService;

@RestController
@RequestMapping(path = "/jugador")
public class JugadorController {

	@Autowired
	private JugadorService service;

	// GET /jugador/{idJugador}/recursos
	@GetMapping(path = "/{idJugador}/recursos")
	public ResponseEntity<JugadorRecursosResposta> recursos(@PathVariable int idJugador) {
		return service.recursos(idJugador);
	}

	// POST /jugador/{idJugador}/construir?tipus=POBLAT&posicio=31
	@PostMapping(path = "/{idJugador}/construir")
	public ResponseEntity<String> construir(@PathVariable int idJugador, @RequestParam String tipus,
			@RequestParam int posicio) {
		return service.construir(idJugador, tipus, posicio);
	}

	// POST /jugador/{idJugador}/passar
	@PostMapping(path = "/{idJugador}/passar")
	public ResponseEntity<String> passar(@PathVariable int idJugador) {
		return service.passar(idJugador);
	}
}
