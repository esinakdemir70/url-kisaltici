package com.esinakdemir70.urlkisaltici;

import java.net.URI;
import java.net.URISyntaxException;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class GecerliUrlValidator implements ConstraintValidator<GecerliUrl, String> {

	@Override
	public boolean isValid(String deger, ConstraintValidatorContext context) {
		if (deger == null || deger.isBlank()) {
			return true; // Boş değer kontrolünü @NotBlank yapar.
		}
		try {
			URI uri = new URI(deger.trim());
			String sema = uri.getScheme();
			boolean semaUygun = "http".equalsIgnoreCase(sema) || "https".equalsIgnoreCase(sema);
			boolean hostVar = uri.getHost() != null && !uri.getHost().isBlank();
			return semaUygun && hostVar;
		} catch (URISyntaxException e) {
			return false;
		}
	}
}
