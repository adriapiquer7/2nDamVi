import os

import certifi
from pymongo import MongoClient, UpdateMany
from pymongo.errors import OperationFailure, PyMongoError

# ---------------------------------------------------------------------------
# CONFIGURACIÓ
# ---------------------------------------------------------------------------

# Cadena de connexió al clúster d'Atlas.
# Bona pràctica: no deixar usuari i contrasenya al codi. A PyCharm es pot definir
# la variable d'entorn MONGODB_URI a Run > Edit Configurations > Environment variables.
# Si no existeix, s'utilitza la URI per defecte.
URI = os.getenv(
    "MONGODB_URI",
    "mongodb+srv://usuari:super3@inicimongo.xjxq0.mongodb.net/"
    "?retryWrites=true&w=majority&appName=IniciMongo",
)

NOM_BD = "llibreria_examen"  # Base de dades d'exemple d'Atlas
NOM_COLLECCIO = "llibres"  # Col·lecció de pel·lícules


# ---------------------------------------------------------------------------
# FUNCIONS AUXILIARS
# ---------------------------------------------------------------------------

def titol(text):
    """Imprimeix un títol de secció per separar la sortida a la consola."""
    print("\n" + "=" * 70)
    print(text)
    print("=" * 70)


def mostra(cursor, limit=5):
    """
    Mostra per pantalla els primers 'limit' documents d'un cursor o llista.
    Evita imprimir milers de documents quan la consulta retorna molts resultats.
    """
    documents = list(cursor)
    print(f"-> {len(documents)} document(s) trobat(s)")
    for doc in documents[:limit]:
        print("  ", doc)
    if len(documents) > limit:
        print(f"   ... (es mostren només els {limit} primers)")


# ---------------------------------------------------------------------------
# CONNEXIÓ
# ---------------------------------------------------------------------------

def connectar():
    """
    Crea la connexió amb el clúster d'Atlas.
    - tls=True i tlsCAFile=certifi.where() eviten problemes de certificats SSL.
    - serverSelectionTimeoutMS=30000: temps màxim (30 s) per trobar el servidor.
    """
    client = MongoClient(
        URI,
        tls=True,
        tlsCAFile=certifi.where(),
        serverSelectionTimeoutMS=30000,
    )
    # Per comprovar que la connexió funciona, llistem les bases de dades del clúster
    titol("CONNEXIÓ")
    print("Bases de dades:", client.list_database_names())
    return client


def seleccionar_colleccio(client):
    """Selecciona la base de dades i la col·lecció, i en mostra informació bàsica."""
    db = client[NOM_BD]
    print("Col·leccions de", NOM_BD, ":", db.list_collection_names())

    col = db[NOM_COLLECCIO]
    # count_documents({}) amb filtre buit compta tots els documents
    print("Documents a", NOM_COLLECCIO, ":", col.count_documents({}))
    return col


# ---------------------------------------------------------------------------
# OPERACIONS CRUD
# ---------------------------------------------------------------------------

def create(col):
    """CREATE: inserir documents."""
    titol("CREATE (Crear)")

    # Inserir un document: insert_one retorna l'_id generat automàticament
    nou_doc = {
        "title": "Pel·lícula Exemple",
        "any": 2024,
        "puntuacio": 8.5,
    }
    resultat = col.insert_one(nou_doc)
    print(f"ID inserit: {resultat.inserted_id}")

    # Inserir múltiples documents: insert_many rep una llista de diccionaris
    multiples_docs = [
        {"title": "Pel·lícula 1", "any": 2024},
        {"title": "Pel·lícula 2", "any": 2024},
    ]
    resultat = col.insert_many(multiples_docs)
    print(f"IDs inserits: {resultat.inserted_ids}")


def read(col):
    """READ: consultar documents."""
    titol("READ (Llegir)")

    # find_one retorna el primer document que compleix el filtre (o None)
    print("find_one:")
    doc = col.find_one({"any": 2024})
    print("  ", doc)

    # find retorna un cursor amb tots els documents que compleixen el filtre
    print("find (múltiples documents):")
    mostra(col.find({"any": 2024}))

    # Projecció: 1 = mostrar el camp, 0 = ocultar-lo (_id surt per defecte)
    print("find amb projecció (només title i puntuacio):")
    mostra(col.find(
        {"any": 2024},
        {"title": 1, "puntuacio": 1, "_id": 0},
    ))


def update(col):
    """UPDATE: modificar documents."""
    titol("UPDATE (Actualitzar)")

    # update_one: modifica el primer document que coincideix amb el filtre.
    # $set assigna un valor (crea el camp si no existeix)
    res = col.update_one(
        {"title": "Pel·lícula Exemple"},  # filtre
        {"$set": {"puntuacio": 9.0}},  # actualització
    )
    print(f"update_one -> coincidents: {res.matched_count}, modificats: {res.modified_count}")

    # update_many: modifica tots els documents que coincideixen
    res = col.update_many(
        {"any": 2024},  # filtre
        {"$set": {"actualitzat": True}},  # actualització
    )
    print(f"update_many -> coincidents: {res.matched_count}, modificats: {res.modified_count}")

    # Camps niats: la notació amb punt crea/modifica un subdocument
    col.update_one(
        {"title": "Pel·lícula Exemple"},
        {"$set": {"detalls.genere": ["Acció"]}},
    )

    # $unset elimina un camp del document
    # (al notebook el filtre era "titol", però el camp es diu "title")
    col.update_one(
        {"title": "Pel·lícula 1"},
        {"$unset": {"actualitzat": ""}},
    )

    print("Document després de les actualitzacions:")
    print("  ", col.find_one({"title": "Pel·lícula Exemple"}))


# ---------------------------------------------------------------------------
# CONSULTES AVANÇADES
# ---------------------------------------------------------------------------

def operadors_comparacio(col):
    """Operadors de comparació: $gt, $lt, $gte, $ne, $in."""
    titol("OPERADORS DE COMPARACIÓ")

    print("$gt (major que 8.4):")
    mostra(col.find({"puntuacio": {"$gt": 8.4}}))

    print("$lt (menor que 5.0):")
    mostra(col.find({"puntuacio": {"$lt": 5.0}}))

    print("$gte (major o igual que 8.0):")
    mostra(col.find({"puntuacio": {"$gte": 8.0}}))

    # Aquesta consulta retorna milers de documents: limitem amb .limit()
    print("$ne (year diferent de 2014), limitat a 5:")
    mostra(col.find({"year": {"$ne": 2014}}, {"title": 1, "year": 1}).limit(5))

    # $in: el camp conté algun dels valors de la llista (com un OR)
    print("$in (gènere Horror o Sci-Fi), limitat a 5:")
    mostra(col.find({"genres": {"$in": ["Horror", "Sci-Fi"]}}, {"title": 1, "genres": 1}).limit(5))


def operadors_logics(col):
    """Operadors lògics: $and, $or."""
    titol("OPERADORS LÒGICS")

    # $and: s'han de complir totes les condicions
    print("$and (puntuacio > 8.0 I any = 2024):")
    mostra(col.find({
        "$and": [
            {"puntuacio": {"$gt": 8.0}},
            {"any": 2024},
        ]
    }))

    # $or: n'hi ha prou que se'n compleixi una
    print("$or (puntuacio > 9.0 O any = 2024):")
    mostra(col.find({
        "$or": [
            {"puntuacio": {"$gt": 9.0}},
            {"any": 2024},
        ]
    }))


def ordenacio_i_limits(col):
    """Ordenació amb sort() i limitació de resultats amb limit()."""
    titol("ORDENACIÓ I LÍMITS")

    # sort("camp", 1)  -> ascendent
    # sort("camp", -1) -> descendent
    print("Les 3 pel·lícules més recents:")
    mostra(col.find({}, {"title": 1, "year": 1, "_id": 0}).sort("year", -1).limit(3))


def agregacio(col):
    """Pipeline d'agregació: durada mitjana de les pel·lícules per any."""
    titol("AGREGACIÓ")

    pipeline = [
        # $group agrupa per any i calcula la mitjana del camp runtime
        {"$group": {
            "_id": "$year",
            "durada_mitjana": {"$avg": "$runtime"},
        }},
        # $sort ordena el resultat per any (ascendent)
        {"$sort": {"_id": 1}},
    ]
    resultats = col.aggregate(pipeline)
    mostra(resultats, limit=10)


# ---------------------------------------------------------------------------
# OPTIMITZACIÓ DE CONSULTES
# ---------------------------------------------------------------------------

def indexs(col):
    """Consulta i creació d'índexs."""
    titol("ÍNDEXS")

    print("Índexs existents:")
    for nom, info in col.index_information().items():
        print("  ", nom, "->", info["key"])

    # Índex simple sobre un camp
    col.create_index("any")

    # Índex compost: any ascendent i puntuacio descendent
    col.create_index([("any", 1), ("puntuacio", -1)])

    # Una col·lecció només pot tenir UN índex de text.
    # sample_mflix.movies ja en té un, per tant aquesta instrucció dona error:
    # caldria esborrar l'existent (col.drop_index(nom)) i crear-ne un de nou.
    try:
        col.create_index([("title", "text")])
    except OperationFailure as e:
        print("No s'ha pogut crear l'índex de text (ja n'hi ha un):", e.code)


def cerca_text(col):
    """Cerca de text (requereix un índex de text a la col·lecció)."""
    titol("CERCA DE TEXT")

    print('Cerca "exemple":')
    mostra(col.find({"$text": {"$search": "exemple"}}))

    print('Cerca "Pel·licula" amb any = 2024:')
    mostra(col.find({
        "$text": {"$search": "Pel·licula"},
        "any": 2024,
    }))


# ---------------------------------------------------------------------------
# EXEMPLES D'ÚS COMÚ
# ---------------------------------------------------------------------------

def consultes_habituals(col):
    """Consultes freqüents: $exists, $regex, comptar per categoria."""
    titol("CONSULTES HABITUALS")

    # $exists: False -> documents que NO tenen el camp
    print("Documents sense el camp 'plot', limitat a 5:")
    mostra(col.find({"plot": {"$exists": False}}, {"title": 1}).limit(5))

    # $regex: cerca per patró; "$options": "i" ignora majúscules/minúscules
    print("Títols que contenen 'space':")
    mostra(col.find({"title": {"$regex": "space", "$options": "i"}}, {"title": 1, "_id": 0}))

    # Comptar documents que compleixen un filtre
    print("Pel·lícules de terror:", col.count_documents({"genres": "Horror"}))


def gestio_errors(col):
    """Exemple de gestió d'errors amb try-except."""
    titol("GESTIÓ D'ERRORS")

    # Al notebook la variable 'document' no estava definida; aquí la creem
    document = {"title": "Pel·lícula amb errors controlats", "any": 2024}
    try:
        col.insert_one(document)
        print("Document inserit correctament")
    except PyMongoError as e:
        print(f"Error en inserir el document: {e}")


# ---------------------------------------------------------------------------
# DELETE (al final, per netejar les dades de prova)
# ---------------------------------------------------------------------------

def delete(col):
    """DELETE: esborrar els documents de prova inserits."""
    titol("DELETE (Esborrar)")

    # delete_one: esborra el primer document que coincideix
    res = col.delete_one({"title": "Pel·lícula Exemple"})
    print(f"delete_one -> esborrats: {res.deleted_count}")

    # delete_many: esborra tots els que coincideixen
    # Compte: el camp "any" només el tenen els nostres documents de prova
    # (les pel·lícules originals fan servir "year")
    res = col.delete_many({"any": 2024})
    print(f"delete_many -> esborrats: {res.deleted_count}")

    # ATENCIÓ: col.drop() esborraria TOTA la col·lecció. No l'utilitzis aquí!

def marcar_classics_extensos(col,pagines_minimes, any_limit):
    res = col.update_many(
        {"$and": [{"pagines": {"$gte": pagines_minimes}},{"any_publicacio": {"$lte" : any_limit}}]},  # filtre
        {"$set": {"etiqueta": "Clàssic extens"}},  # actualització
    )
    print(f"Documents modificats: {res.modified_count}")

def eliminar_llibres_sense_format(col, nom_format):
    res = col.delete_many(col.find({"formats": {"$ne":[nom_format]}},{"titol": 1}))
    print(f"Documents eliminats: {res.deleted_count}")

def llibres_titol_amb_article(col):
    res = col.find({"$or": [{"titol": {"$regex": "El"}},{"titol": {"$regex": "La"}},{"titol": {"$regex" : "Els"}},{"titol": {"regex" : "Las"}}, {"titol" : {"regex" : "L"}}]}, {"titol": 1, "autor": 1, "any_publicacio": 1}).sort("any_publicacio", 1)

    if(col.find({}, {"titol": 1, "autor": 1, "any_publicacio": 1})):
        mostra(res)

    print("No s'han trobat llibres amb titol que comenci per article")

