package com.esinakdemir70.urlkisaltici;

/**
 * GET /api/istatistik/{kod} yanıtı.
 */
public record IstatistikYaniti(String kisaKod, String orijinalUrl, long tiklanma) {
}
