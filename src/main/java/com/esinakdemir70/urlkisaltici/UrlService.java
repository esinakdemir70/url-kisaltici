package com.esinakdemir70.urlkisaltici;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

/**
 * Kısa kod -> orijinal URL eşlemesini bellekte tutar.
 * Uygulama kapanınca kayıtlar silinir (kalıcı saklama ayrı bir görevdir: #5).
 */
@Service
public class UrlService {

	private final Map<String, String> linkler = new ConcurrentHashMap<>();
	private final KodUretici kodUretici;

	public UrlService(KodUretici kodUretici) {
		this.kodUretici = kodUretici;
	}

	/**
	 * URL için yeni bir kısa kod üretir ve kaydeder.
	 * Üretilen kod zaten kullanılıyorsa putIfAbsent null dışında bir değer döner
	 * ve döngü yeni bir kod dener.
	 */
	public String kisalt(String url) {
		String kod;
		do {
			kod = kodUretici.uret();
		} while (linkler.putIfAbsent(kod, url) != null);
		return kod;
	}
}
