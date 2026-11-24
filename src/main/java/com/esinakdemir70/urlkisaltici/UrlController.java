package com.esinakdemir70.urlkisaltici;

import java.net.URI;

import org.springframework.http.ResponseEntity;
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
		String kod = urlService.kisalt(url);

		// Kısa adres, isteğin geldiği sunucu adresine göre oluşturulur (localhost sabit değil).
		URI kisaUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/{kod}")
				.buildAndExpand(kod)
				.toUri();

		return ResponseEntity.created(kisaUrl)
				.body(new KisaltmaYaniti(kod, kisaUrl.toString(), url));
	}
}
