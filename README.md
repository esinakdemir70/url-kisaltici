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

Rastgele üretilen kısa kod her zaman 6 karakterlidir ve yalnızca harf ve rakamlardan oluşur. Kendi kodunuzu seçmek için Özellik 4'e bakın.

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

## Özellik 3: Tıklanma sayısı (#3) nasıl denenir

Kısa bağlantı her açıldığında (her `302` yönlendirmesinde) tıklanma sayısı bir artar. İstatistik isteğinin kendisi sayıyı artırmaz.

1. Özellik 1'deki gibi bir kısa bağlantı oluşturun ve dönen kodu not edin (ör. `V0lCSL`).
2. Bağlantıyı tarayıcıda 3 kez açın veya komut satırından 3 kez isteyin:

   ```bash
   curl -s -o /dev/null http://localhost:8080/V0lCSL          # Windows: curl.exe -s -o NUL http://localhost:8080/V0lCSL
   ```

3. İstatistiği görüntüleyin:

   ```bash
   curl http://localhost:8080/api/istatistik/V0lCSL           # Windows: curl.exe http://localhost:8080/api/istatistik/V0lCSL
   ```

   Beklenen yanıt (`200 OK`):

   ```json
   {"kisaKod":"V0lCSL","orijinalUrl":"https://github.com/esinakdemir70/url-kisaltici","tiklanma":3}
   ```

Olmayan bir kod için (`/api/istatistik/Yok123`) `404` ve `{"hata":"Bu kısa bağlantı bulunamadı."}` döner.

## Özellik 4: Özel kısa kod (#4) nasıl denenir

İstekte `ozelKod` alanı gönderilirse rastgele kod yerine bu kod kullanılır. Özel kod 3-30 karakter olmalı ve yalnızca harf, rakam ve tire (`-`) içermelidir.

Linux / macOS:

```bash
curl -i -X POST http://localhost:8080/api/kisalt \
  -H "Content-Type: application/json" \
  -d '{"url": "https://github.com/esinakdemir70/url-kisaltici", "ozelKod": "devops-odev"}'
```

Windows PowerShell:

```powershell
curl.exe -i -X POST http://localhost:8080/api/kisalt -H "Content-Type: application/json" -d '{\"url\": \"https://github.com/esinakdemir70/url-kisaltici\", \"ozelKod\": \"devops-odev\"}'
```

Beklenen yanıt (`201 Created`):

```
HTTP/1.1 201
Location: http://localhost:8080/devops-odev
Content-Type: application/json

{"kisaKod":"devops-odev","kisaUrl":"http://localhost:8080/devops-odev","orijinalUrl":"https://github.com/esinakdemir70/url-kisaltici"}
```

Artık `http://localhost:8080/devops-odev` adresi asıl siteye yönlendirir.

Aynı komutu ikinci kez çalıştırın. Kod zaten kullanıldığı için `409 Conflict` döner:

```
HTTP/1.1 409
Content-Type: application/json

{"hata":"Bu kısa kod zaten kullanılıyor. Lütfen başka bir kod seçin."}
```

Kurala uymayan bir kod (ör. `"ozelKod": "a b"`) gönderilirse `400` ve `{"hata":"Özel kod 3-30 karakter olmalı ve yalnızca harf, rakam ve tire (-) içermelidir."}` döner.

## API özeti

| Yöntem | Yol | İstek gövdesi | Başarılı yanıt | Hata yanıtları |
|---|---|---|---|---|
| `POST` | `/api/kisalt` | `{"url": "https://...", "ozelKod": "..."}` (`ozelKod` isteğe bağlı) | `201 Created` + `{"kisaKod", "kisaUrl", "orijinalUrl"}` | `400` geçersiz URL, geçersiz özel kod veya bozuk JSON; `409` özel kod zaten kullanılıyor |
| `GET` | `/{kod}` (3-30 karakter: harf, rakam, tire) | - | `302 Found` + `Location: <orijinal URL>` | `404` kod bulunamadı |
| `GET` | `/api/istatistik/{kod}` | - | `200 OK` + `{"kisaKod", "orijinalUrl", "tiklanma"}` | `404` kod bulunamadı |

Hata yanıtları her zaman `{"hata": "<Türkçe mesaj>"}` biçimindedir.

## Geliştirme süreci

Her görev aynı akışla ilerledi:

1. Görev panoda bir **issue** olarak açıldı (açıklama + kabul kriterleri) ve **Yapılacak** sütununa kondu.
2. Çalışmaya başlanınca kart **Devam Ediyor** sütununa taşındı ve `main`'den görev numarasını içeren bir **branch** açıldı (ör. `feature/1-url-kisaltma`).
3. Değişiklikler küçük commit'ler hâlinde yapıldı. Her commit mesajı görev numarasını içerir (ör. `#1`). Böylece commit'ler issue sayfasında da görünür.
4. Branch için bir **pull request** açıldı. PR açıklamasında yapılanlar, nasıl deneneceği ve `Closes #<no>` yer aldı. GitHub Actions her PR'da testleri otomatik çalıştırdı.
5. Testler ve elle yapılan denemeler kabul kriterlerini doğrulayınca PR `main`'e birleştirildi (merge commit ile, commit geçmişi korunarak).
6. Issue'ya doğrulamanın nasıl yapıldığını anlatan bir yorum eklendi, kabul kriterleri işaretlendi ve kart **Tamamlandı** sütununa taşındı.

Tamamlanan görevler ve pull request'ler:

| Görev | Branch | Pull request |
|---|---|---|
| [#1 Uzun URL'yi kısaltma](https://github.com/esinakdemir70/url-kisaltici/issues/1) | `feature/1-url-kisaltma` | [#8](https://github.com/esinakdemir70/url-kisaltici/pull/8) |
| [#2 Kısa bağlantıdan asıl adrese yönlendirme](https://github.com/esinakdemir70/url-kisaltici/issues/2) | `feature/2-yonlendirme` | [#9](https://github.com/esinakdemir70/url-kisaltici/pull/9) |
| [#6 GitHub Actions ile otomatik derleme ve test](https://github.com/esinakdemir70/url-kisaltici/issues/6) | `feature/6-ci` | [#10](https://github.com/esinakdemir70/url-kisaltici/pull/10) |
| [#3 Tıklanma sayısını gösterme](https://github.com/esinakdemir70/url-kisaltici/issues/3) | `feature/3-tiklanma` | [#13](https://github.com/esinakdemir70/url-kisaltici/pull/13) |
| [#4 Özel kısa kod seçme](https://github.com/esinakdemir70/url-kisaltici/issues/4) | `feature/4-ozel-kod` | [#14](https://github.com/esinakdemir70/url-kisaltici/pull/14) |

[#11](https://github.com/esinakdemir70/url-kisaltici/pull/11) numaralı PR, #6'nın "test bozulunca iş akışı kırmızı olur" kabul kriterini göstermek için bilerek bozulmuş bir testle açılmış ve birleştirilmeden kapatılmıştır.

Sonraki görevler (#5 kalıcı saklama, #7 Docker) panoda **Yapılacak** sütunundadır.
