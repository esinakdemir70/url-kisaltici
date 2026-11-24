package com.esinakdemir70.urlkisaltici;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Hataları kullanıcıya {"hata": "..."} biçiminde, Türkçe mesajla döner.
 */
@RestControllerAdvice
public class HataYoneticisi {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Map<String, String> dogrulamaHatasi(MethodArgumentNotValidException e) {
		String mesaj = e.getBindingResult().getFieldErrors().stream()
				.findFirst()
				.map(hata -> hata.getDefaultMessage())
				.orElse("İstek geçersiz.");
		return Map.of("hata", mesaj);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Map<String, String> okunamayanIstek() {
		return Map.of("hata", "İstek gövdesi okunamadı. Örnek: {\"url\": \"https://ornek.com\"}");
	}
}
