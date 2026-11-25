package com.esinakdemir70.urlkisaltici;

import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
class OzelKodTest {

	private static final String ORIJINAL_URL = "https://www.ornek.com/cok/uzun/adres";

	@Autowired
	private MockMvc mockMvc;

	private ResultActions ozelKodlaKisalt(String ozelKod) throws Exception {
		return mockMvc.perform(post("/api/kisalt")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"url\": \"" + ORIJINAL_URL + "\", \"ozelKod\": \"" + ozelKod + "\"}"));
	}

	@Test
	void ozelKodGonderilinceBaglantiBuKodlaOlusturulur() throws Exception {
		ozelKodlaKisalt("devops-odev")
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.kisaKod").value("devops-odev"))
				.andExpect(jsonPath("$.kisaUrl").value("http://localhost/devops-odev"))
				.andExpect(jsonPath("$.orijinalUrl").value(ORIJINAL_URL));

		mockMvc.perform(get("/devops-odev"))
				.andExpect(status().isFound())
				.andExpect(header().string("Location", ORIJINAL_URL));
	}

	@Test
	void kodZatenKullaniliyorsa409Doner() throws Exception {
		ozelKodlaKisalt("cakisan-kod").andExpect(status().isCreated());

		ozelKodlaKisalt("cakisan-kod")
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.hata").value("Bu kısa kod zaten kullanılıyor. Lütfen başka bir kod seçin."));
	}

	@ParameterizedTest
	@ValueSource(strings = { "ab", "bosluk var", "egik/cizgi", "nokta.li", "turkce-ç", "",
			"bu-kod-otuz-karakterden-daha-uzun" })
	void gecersizOzelKodIcin400Doner(String ozelKod) throws Exception {
		ozelKodlaKisalt(ozelKod)
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.hata")
						.value("Özel kod 3-30 karakter olmalı ve yalnızca harf, rakam ve tire (-) içermelidir."));
	}

	@Test
	void ozelKodGonderilmezseRastgeleKodUretilir() throws Exception {
		mockMvc.perform(post("/api/kisalt")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"url\": \"" + ORIJINAL_URL + "\"}"))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.kisaKod").value(matchesPattern("[a-zA-Z0-9]{6}")));
	}

	@Test
	void faviconIstegiYonlendirmeyeDusmez() throws Exception {
		mockMvc.perform(get("/favicon.ico"))
				.andExpect(status().isNotFound());
	}
}
