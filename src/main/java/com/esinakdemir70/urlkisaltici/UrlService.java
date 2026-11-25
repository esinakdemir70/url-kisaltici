package com.esinakdemir70.urlkisaltici;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

/**
 * Kısa kod -> orijinal URL eşlemesini bellekte tutar.
 * Uygulama kapanınca kayıtlar silinir (kalıcı saklama ayrı bir görevdir: #5).
 */
@Service
public class UrlService {

	private final Map<String, String> linkler = new ConcurrentHashMap<>();
	private final Map<String, Long> tiklanmalar = new ConcurrentHashMap<>();
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

	/**
	 * URL'yi kullanıcının seçtiği kodla kaydeder.
	 * Kod zaten kullanılıyorsa KodKullaniliyorException fırlatır.
	 */
	public String ozelKodlaKisalt(String ozelKod, String url) {
		if (linkler.putIfAbsent(ozelKod, url) != null) {
			throw new KodKullaniliyorException(ozelKod);
		}
		return ozelKod;
	}

	/**
	 * Kısa koda karşılık gelen orijinal URL'yi döner; kod yoksa boş Optional döner.
	 */
	public Optional<String> bul(String kod) {
		return Optional.ofNullable(linkler.get(kod));
	}

	/**
	 * Kısa bağlantının tıklanma sayısını bir artırır.
	 * ConcurrentHashMap.merge atomiktir; aynı anda gelen tıklamalar kaybolmaz.
	 */
	public void tiklanmaArtir(String kod) {
		tiklanmalar.merge(kod, 1L, Long::sum);
	}

	/**
	 * Kısa bağlantının kaç kez açıldığını döner; hiç açılmadıysa 0 döner.
	 */
	public long tiklanmaSayisi(String kod) {
		return tiklanmalar.getOrDefault(kod, 0L);
	}
}
