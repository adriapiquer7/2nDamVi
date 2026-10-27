package SpringROA.SpringROA;

import java.util.List;

public class Main {

	public static void main(String[] args) {
		PartidaDAO partidaDAO = new PartidaDAO();
		JugadorDAO jugadorDAO = new JugadorDAO();

		try {
			int idPartida = partidaDAO.iniciarPartida(List.of("Anna", "Bernat", "Carla"), 10);
			System.out.println("Partida creada amb id " + idPartida);
			partidaDAO.mostrarTauler(idPartida);

			// Preparació (automàtica): cada jugador col·loca un poblat i una carretera
			int intents = 0;
			while (partidaDAO.estat(idPartida) == EstatPartida.PREPARACIO && intents++ < 50) {
				int idActiu = partidaDAO.idJugadorActiu(idPartida);
				int vertex = jugadorDAO.vertexsDisponibles(idActiu).get(0).getId();
				System.out.println(jugadorDAO.construirPoblat(idActiu, vertex));
				colocarCarretera(jugadorDAO, partidaDAO, idActiu, vertex);
				System.out.println(jugadorDAO.passarTorn(idActiu));
			}
			partidaDAO.mostrarPartida(idPartida);

			// Partida en curs: unes quantes rondes de tirades i torns
			for (int i = 0; i < 4; i++) {
				int idActiu = partidaDAO.idJugadorActiu(idPartida);
				System.out.println(partidaDAO.tirarDaus(idPartida));
				jugadorDAO.mostrarRecursos(idActiu);
				System.out.println(jugadorDAO.passarTorn(idActiu));
			}
			partidaDAO.mostrarPartida(idPartida);
			partidaDAO.mostrarTauler(idPartida);
		} catch (JocException e) {
			System.out.println("Error de joc: " + e.getMessage());
		}
	}

	private static void colocarCarretera(JugadorDAO jd, PartidaDAO pd, int idJugador, int idVertex) {
		for (int idAresta : pd.arestesAdjacents(idVertex)) {
			try {
				System.out.println(jd.construirCarretera(idJugador, idAresta));
				return;
			} catch (JocException e) {
				// aresta ocupada o no vàlida: provem la següent
			}
		}
		System.out.println("No s'ha pogut col·locar cap carretera");
	}
}
