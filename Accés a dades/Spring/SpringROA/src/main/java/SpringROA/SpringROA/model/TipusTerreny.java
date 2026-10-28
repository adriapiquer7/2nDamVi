package SpringROA.SpringROA.model;

public enum TipusTerreny {
	BOSC(TipusRecurs.FUSTA),
	TURONS(TipusRecurs.MAO),
	PASTURES(TipusRecurs.LLANA),
	CAMPS(TipusRecurs.BLAT),
	MUNTANYES(TipusRecurs.MINERAL),
	DESERT(null);

	private final TipusRecurs recurs;

	TipusTerreny(TipusRecurs recurs) {
		this.recurs = recurs;
	}

	/** Recurs que produeix el terreny, o null pel desert. */
	public TipusRecurs getRecurs() {
		return recurs;
	}
}
