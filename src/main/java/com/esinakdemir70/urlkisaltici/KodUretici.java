package com.esinakdemir70.urlkisaltici;

import java.security.SecureRandom;

import org.springframework.stereotype.Component;

/**
 * Kısa bağlantılar için rastgele, 6 karakterli alfanümerik kod üretir.
 */
@Component
public class KodUretici {

	static final String KARAKTERLER = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	static final int KOD_UZUNLUGU = 6;

	private final SecureRandom random = new SecureRandom();

	public String uret() {
		StringBuilder kod = new StringBuilder(KOD_UZUNLUGU);
		for (int i = 0; i < KOD_UZUNLUGU; i++) {
			kod.append(KARAKTERLER.charAt(random.nextInt(KARAKTERLER.length())));
		}
		return kod.toString();
	}
}
