package SpringROA.SpringROA.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import SpringROA.SpringROA.dto.PartidaPeticio;
import SpringROA.SpringROA.dto.PartidaResposta;
import SpringROA.SpringROA.dto.TaulerHexagonResposta;
import SpringROA.SpringROA.dto.TiradaResposta;
import SpringROA.SpringROA.service.PartidaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/partida")
public class PartidaController {

	@Autowired
	private PartidaService service;

	// POST /partida
	@PostMapping
	public ResponseEntity<PartidaResposta> iniciar(@Valid @RequestBody PartidaPeticio peticio) {
		return service.iniciarPartida(peticio);
	}

	// GET /partida/{idPartida}/tauler
	@GetMapping(path = "/{idPartida}/tauler")
	public ResponseEntity<List<TaulerHexagonResposta>> tauler(@PathVariable int idPartida) {
		return service.tauler(idPartida);
	}

	// POST /partida/{idPartida}/daus
	@PostMapping(path = "/{idPartida}/daus")
	public ResponseEntity<TiradaResposta> tirarDaus(@PathVariable int idPartida) {
		return service.tirarDaus(idPartida);
	}
}
