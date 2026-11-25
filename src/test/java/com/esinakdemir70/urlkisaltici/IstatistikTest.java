package com.esinakdemir70.urlkisaltici;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.jayway.jsonpath.JsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class IstatistikTest {

	private static final String ORIJINAL_URL = "https://www.ornek.com/cok/uzun/adres";

	@Autowired
	private MockMvc mockMvc;

	private String kisalt() throws Exception {
		String yanit = mockMvc.perform(post("/api/kisalt")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"url\": \"" + ORIJINAL_URL + "\"}"))
				.andExpect(status().isCreated())
				.andReturn().getResponse().getContentAsString();
		return JsonPath.read(yanit, "$.kisaKod");
	}

	@Test
	void yeniBaglantininTiklanmaSayisiSifirdir() throws Exception {
		String kod = kisalt();

		mockMvc.perform(get("/api/istatistik/" + kod))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.kisaKod").value(kod))
				.andExpect(jsonPath("$.orijinalUrl").value(ORIJINAL_URL))
				.andExpect(jsonPath("$.tiklanma").value(0));
	}

	@Test
	void ucKezAcilanBaglantininTiklanmaSayisiUctur() throws Exception {
		String kod = kisalt();
		for (int i = 0; i < 3; i++) {
			mockMvc.perform(get("/" + kod)).andExpect(status().isFound());
		}

		mockMvc.perform(get("/api/istatistik/" + kod))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.tiklanma").value(3));
	}

	@Test
	void istatistikIstegiTiklanmaSayisiniArtirmaz() throws Exception {
		String kod = kisalt();
		mockMvc.perform(get("/" + kod));
		mockMvc.perform(get("/api/istatistik/" + kod));

		mockMvc.perform(get("/api/istatistik/" + kod))
				.andExpect(jsonPath("$.tiklanma").value(1));
	}

	@Test
	void olmayanKodIcinIstatistik404Doner() throws Exception {
		mockMvc.perform(get("/api/istatistik/Yok123"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.hata").value("Bu kısa bağlantı bulunamadı."));
	}
}
