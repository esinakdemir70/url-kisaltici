package com.esinakdemir70.urlkisaltici;

/**
 * İstenen özel kod başka bir bağlantı tarafından kullanılıyorsa fırlatılır;
 * HataYoneticisi bunu 409'a çevirir.
 */
public class KodKullaniliyorException extends RuntimeException {

	public KodKullaniliyorException(String kod) {
		super("Kısa kod zaten kullanılıyor: " + kod);
	}
}
