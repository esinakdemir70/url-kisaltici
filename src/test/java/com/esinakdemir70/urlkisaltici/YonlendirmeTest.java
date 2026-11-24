package com.esinakdemir70.urlkisaltici;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
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
class YonlendirmeTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void varOlanKodIcin302VeOrijinalUrlDoner() throws Exception {
		String orijinalUrl = "https://www.ornek.com/cok/uzun/adres";
		String yanit = mockMvc.perform(post("/api/kisalt")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"url\": \"" + orijinalUrl + "\"}"))
				.andExpect(status().isCreated())
				.andReturn().getResponse().getContentAsString();
		String kod = JsonPath.read(yanit, "$.kisaKod");

		mockMvc.perform(get("/" + kod))
				.andExpect(status().isFound())
				.andExpect(header().string("Location", orijinalUrl));
	}

	@Test
	void olmayanKodIcin404VeTurkceHataDoner() throws Exception {
		mockMvc.perform(get("/Yok123"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.hata").value("Bilerek yanlis yazilmis mesaj."));
	}
}
