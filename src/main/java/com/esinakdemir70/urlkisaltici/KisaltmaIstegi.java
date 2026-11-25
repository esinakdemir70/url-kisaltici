package com.esinakdemir70.urlkisaltici;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * POST /api/kisalt isteğinin gövdesi: {"url": "...", "ozelKod": "..."}
 * ozelKod isteğe bağlıdır; gönderilmezse rastgele bir kod üretilir.
 */
public record KisaltmaIstegi(
		@NotBlank(message = "Geçerli bir URL giriniz (http veya https ile başlamalı).")
		@GecerliUrl
		String url,

		@Pattern(regexp = "[a-zA-Z0-9-]{3,30}",
				message = "Özel kod 3-30 karakter olmalı ve yalnızca harf, rakam ve tire (-) içermelidir.")
		String ozelKod) {
}
