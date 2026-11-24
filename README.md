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

## Özellik 1: URL kısaltma (#1) nasıl denenir

Uygulama çalışırken ikinci bir terminal açın.

Linux / macOS:

```bash
curl -i -X POST http://localhost:8080/api/kisalt \
  -H "Content-Type: application/json" \
  -d '{"url": "https://github.com/esinakdemir70/url-kisaltici"}'
```

Windows PowerShell:

```powershell
curl.exe -i -X POST http://localhost:8080/api/kisalt -H "Content-Type: application/json" -d '{\"url\": \"https://github.com/esinakdemir70/url-kisaltici\"}'
```

> PowerShell'de `curl`, `Invoke-WebRequest` komutunun takma adıdır. Bu yüzden `curl.exe` yazılmalıdır. Windows PowerShell 5.1, JSON içindeki çift tırnakları `curl.exe`'ye aktarırken siler. Bu nedenle tırnaklar `\"` biçiminde kaçırılmıştır. PowerShell 7.3 veya üstünü kullanıyorsanız kaçırmaya gerek yoktur: `-d '{"url": "https://github.com/esinakdemir70/url-kisaltici"}'`

Beklenen yanıt (`201 Created`, kod her seferinde farklıdır):

```
HTTP/1.1 201
Location: http://localhost:8080/v8uqf0
Content-Type: application/json

{"kisaKod":"v8uqf0","kisaUrl":"http://localhost:8080/v8uqf0","orijinalUrl":"https://github.com/esinakdemir70/url-kisaltici"}
```

Kısa kod her zaman 6 karakterlidir ve yalnızca harf ve rakamlardan oluşur.

Geçersiz URL denemesi:

```bash
# Linux / macOS
curl -i -X POST http://localhost:8080/api/kisalt \
  -H "Content-Type: application/json" \
  -d '{"url": "merhaba"}'
```

```powershell
# Windows PowerShell
curl.exe -i -X POST http://localhost:8080/api/kisalt -H "Content-Type: application/json" -d '{\"url\": \"merhaba\"}'
```

Beklenen yanıt (`400 Bad Request`):

```
HTTP/1.1 400
Content-Type: application/json

{"hata":"Geçerli bir URL giriniz (http veya https ile başlamalı)."}
```

Boş değer (`""`) veya `ftp://x.com` gibi `http`/`https` ile başlamayan adresler de aynı hatayı döner.

## Özellik 2: Yönlendirme (#2) nasıl denenir

1. Özellik 1'deki istekten dönen `kisaUrl` değerini (ör. `http://localhost:8080/v8uqf0`) tarayıcının adres çubuğuna yapıştırın. Tarayıcı asıl adresi açar.

2. Yönlendirmeyi komut satırında görmek için kendi kodunuzu yazarak:

   ```bash
   curl -i http://localhost:8080/v8uqf0          # Windows: curl.exe -i http://localhost:8080/v8uqf0
   ```

   Beklenen yanıt (`302 Found`):

   ```
   HTTP/1.1 302
   Location: https://github.com/esinakdemir70/url-kisaltici
   Content-Length: 0
   ```

3. Olmayan bir kod denemesi:

   ```bash
   curl -i http://localhost:8080/Yok123          # Windows: curl.exe -i http://localhost:8080/Yok123
   ```

   Beklenen yanıt (`404 Not Found`):

   ```
   HTTP/1.1 404
   Content-Type: application/json

   {"hata":"Bu kısa bağlantı bulunamadı."}
   ```
