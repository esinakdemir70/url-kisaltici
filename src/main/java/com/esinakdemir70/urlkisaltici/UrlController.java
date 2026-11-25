package com.esinakdemir70.urlkisaltici;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;

@RestController
public class UrlController {

	private final UrlService urlService;

	public UrlController(UrlService urlService) {
		this.urlService = urlService;
	}

	@PostMapping("/api/kisalt")
	public ResponseEntity<KisaltmaYaniti> kisalt(@Valid @RequestBody KisaltmaIstegi istek) {
		String url = istek.url().trim();
		String kod = (istek.ozelKod() != null)
				? urlService.ozelKodlaKisalt(istek.ozelKod(), url)
				: urlService.kisalt(url);

		// Kısa adres, isteğin geldiği sunucu adresine göre oluşturulur (localhost sabit değil).
		URI kisaUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/{kod}")
				.buildAndExpand(kod)
				.toUri();

		return ResponseEntity.created(kisaUrl)
				.body(new KisaltmaYaniti(kod, kisaUrl.toString(), url));
	}

	/**
	 * Kısa bağlantıyı orijinal adrese yönlendirir (302 Found).
	 * Desen 3-30 karakterlik harf, rakam ve tireden oluşan kodları kabul eder
	 * (rastgele kodlar ve özel kodlar). Nokta ve eğik çizgi içermediği için
	 * /api/** ve /favicon.ico gibi yollar bu metoda düşmez.
	 */
	@GetMapping("/{kod:[a-zA-Z0-9-]{3,30}}")
	public ResponseEntity<Void> yonlendir(@PathVariable String kod) {
		String url = urlService.bul(kod)
				.orElseThrow(() -> new BaglantiBulunamadiException(kod));
		urlService.tiklanmaArtir(kod);
		return ResponseEntity.status(HttpStatus.FOUND)
				.location(URI.create(url))
				.build();
	}

	/**
	 * Kısa bağlantının kaç kez açıldığını gösterir.
	 */
	@GetMapping("/api/istatistik/{kod}")
	public IstatistikYaniti istatistik(@PathVariable String kod) {
		String url = urlService.bul(kod)
				.orElseThrow(() -> new BaglantiBulunamadiException(kod));
		return new IstatistikYaniti(kod, url, urlService.tiklanmaSayisi(kod));
	}
}
