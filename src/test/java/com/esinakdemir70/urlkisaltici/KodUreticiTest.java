package com.esinakdemir70.urlkisaltici;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class KodUreticiTest {

	private final KodUretici kodUretici = new KodUretici();

	@Test
	void uretilenKodAltiKarakterliVeAlfanumeriktir() {
		for (int i = 0; i < 1000; i++) {
			String kod = kodUretici.uret();
			assertThat(kod).hasSize(6).matches("[a-zA-Z0-9]{6}");
		}
	}
}
