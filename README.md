[![CI](https://github.com/esinakdemir70/url-kisaltici/actions/workflows/ci.yml/badge.svg)](https://github.com/esinakdemir70/url-kisaltici/actions/workflows/ci.yml)

# URL Kısaltıcı

## Proje hakkında

Uzun ve karmaşık web adreslerini mesajlarda, sunumlarda veya sosyal medyada paylaşmak zordur ve bu adresler okunaksız görünür. Bu proje, uzun bir URL'yi kısa ve paylaşılabilir bir bağlantıya dönüştüren basit bir web servisidir. Kısa bağlantı açıldığında kullanıcı asıl adrese yönlendirilir.

Linklerini sade biçimde paylaşmak isteyen öğrenciler, öğrenci kulüpleri ve küçük ekipler için geliştirilmiştir.

## İş takip panosu

Görevler GitHub Projects üzerinde takip edilir: [URL Kısaltıcı - İş Takibi](https://github.com/users/esinakdemir70/projects/1)

Panoda üç sütun vardır: **Yapılacak**, **Devam Ediyor**, **Tamamlandı**. Her kart bir issue'dur ve açıklama ile kabul kriterleri içerir.

## Kullanılan teknolojiler

- Java 17
- Spring Boot 3.5 (Web, Validation)
- Maven (Maven Wrapper ile)
- JUnit 5 ve MockMvc
- GitHub Actions (her push ve pull request'te otomatik derleme ve test)

## Gereksinimler

- JDK 17 veya üstü
- Git

Maven kurmanıza gerek yoktur. Proje içindeki Maven Wrapper (`mvnw`, `mvnw.cmd`) doğru Maven sürümünü ilk çalıştırmada kendisi indirir.

## Çalıştırma adımları

Linux / macOS:

```bash
git clone https://github.com/esinakdemir70/url-kisaltici.git
cd url-kisaltici
./mvnw spring-boot:run
```

Windows (PowerShell):

```powershell
git clone https://github.com/esinakdemir70/url-kisaltici.git
cd url-kisaltici
.\mvnw.cmd spring-boot:run
```

Uygulama `http://localhost:8080` adresinde açılır. Durdurmak için terminalde `Ctrl+C` tuşlarına basın.

Testleri çalıştırmak için:

```bash
./mvnw test          # Windows: .\mvnw.cmd test
```

> Not: Bağlantılar bellekte tutulur. Uygulama kapatıldığında oluşturulan kısa bağlantılar silinir.
