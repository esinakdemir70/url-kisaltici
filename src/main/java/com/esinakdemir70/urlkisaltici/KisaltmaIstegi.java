package com.esinakdemir70.urlkisaltici;

import jakarta.validation.constraints.NotBlank;

/**
 * POST /api/kisalt isteğinin gövdesi: {"url": "..."}
 */
public record KisaltmaIstegi(
		@NotBlank(message = "Geçerli bir URL giriniz (http veya https ile başlamalı).")
		@GecerliUrl
		String url) {
}
