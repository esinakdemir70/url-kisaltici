package com.esinakdemir70.urlkisaltici;

/**
 * İstenen kısa kod kayıtlı değilse fırlatılır; HataYoneticisi bunu 404'e çevirir.
 */
public class BaglantiBulunamadiException extends RuntimeException {

	public BaglantiBulunamadiException(String kod) {
		super("Kısa kod bulunamadı: " + kod);
	}
}
