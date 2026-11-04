PRÀCTICA HIBERNATE - CATAN (versió base)

Integrants: (posa aquí els noms del grup)

Estat
- Entitats amb herència JOINED per a Construccio (Assentament, Poblat, Ciutat, Carretera).
- DAO: GenericDao (save/get/delete/list) + PartidaDAO i JugadorDAO.
- Mètodes de la part base: IniciarPartida, MostrarTauler, MostrarRecursos, MostrarPartida,
  TirarDaus, ConstruirCarretera, ConstruirPoblat, MillorarACiutat, ComerciarBanca,
  CalcularPunts, PassarTorn, ComprovarVictoria, VertexsDisponibles.
- Pendent (part avançada): cartes de desenvolupament, lladre (MoureLladre), ExercitMesGran.
- Consultes @NamedQuery: pendent d'afegir.

Assumpcions
- Els CSV han d'estar a la carpeta "csv" de l'arrel del projecte. Format: capçalera a la
  primera línia i separador ';' o ','.
  hexagons.csv: id;q;r   vertexs.csv: id;x;y   hexagon_vertex.csv: hexagon;vertex;posicio
  arestes.csv: id;vertexA;vertexB
- El valor de codi de cada hexàgon, vèrtex i aresta és l'id del CSV.
- El color del jugador depèn de l'ordre de torn: VERMELL=1, BLAU=2, BLANC=3, TARONJA=4.
- MAO s'escriu sense accent (el nom del enum).

Execució
- Configura usuari i contrasenya a src/main/resources/daoHibernate.cfg.xml.
- Executa ProjecteHibernate.Main. Cada execució recrea les taules (hbm2ddl = create).

Proves: veure el document de proves.
