package com.esinakdemir70.urlkisaltici;

import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

@SpringBootTest
@AutoConfigureMockMvc
class UrlControllerTest {

	private static final String HATA_MESAJI = "Geçerli bir URL giriniz (http veya https ile başlamalı).";

	@Autowired
	private MockMvc mockMvc;

	@Test
	void gecerliUrlKisaltilinca201VeAltiKarakterliKodDoner() throws Exception {
		mockMvc.perform(post("/api/kisalt")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"url\": \"https://www.ornek.com/cok/uzun/adres\"}"))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.kisaKod").value(matchesPattern("[a-zA-Z0-9]{6}")))
				.andExpect(jsonPath("$.kisaUrl").value(matchesPattern("http://localhost/[a-zA-Z0-9]{6}")))
				.andExpect(jsonPath("$.orijinalUrl").value("https://www.ornek.com/cok/uzun/adres"));
	}

	@ParameterizedTest
	@ValueSource(strings = { "merhaba", "", "   ", "ftp://x.com", "http://" })
	void gecersizUrlIcin400VeTurkceHataDoner(String url) throws Exception {
		mockMvc.perform(post("/api/kisalt")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"url\": \"" + url + "\"}"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.hata").value(HATA_MESAJI));
	}

	@Test
	void urlAlaniYoksa400Doner() throws Exception {
		mockMvc.perform(post("/api/kisalt")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{}"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.hata").value(HATA_MESAJI));
	}

	@Test
	void bozukJsonIcin400Doner() throws Exception {
		mockMvc.perform(post("/api/kisalt")
						.contentType(MediaType.APPLICATION_JSON)
						.content("bu json degil"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.hata").exists());
	}
}
