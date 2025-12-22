package model;

public enum GiorniModel {
	Lunedi, Martedi, Mercoledi, Giovedi, Venerdi, Sabato, Domenica;

	/**
	 * Restituisce il nome del giorno corrispondente. PRECONDIZIONE: giorno compreso
	 * tra 1 e 7 POSTCONDIZIONE: il risultato è uno dei nomi definiti in Giorni
	 */
	public static String getNomeGiorno(int giorno) {
		if (giorno < 1 || giorno > 7) {
			throw new IllegalArgumentException("Il giorno deve essere compreso tra 1 e 7");
		}
		String result = GiorniModel.values()[giorno - 1].name();
		assert result != null && !result.isBlank() : "Postcondizione violata in getNomeGiorno";
		return result;
	}
}
