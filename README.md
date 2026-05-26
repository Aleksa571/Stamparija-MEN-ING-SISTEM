# Štamparija MEN-ING SISTEM DOO – Završni projekat

**Predmet:** Internet softverske arhitekture
**Tip projekta:** RESTful web servis (Spring Boot backend + React frontend)
**Tema:** Web aplikacija za štampariju MEN-ING SISTEM DOO, Smederevo

---

## Sadržaj

- [O projektu](#o-projektu)
- [Tehnologije](#tehnologije)
- [Pokretanje](#pokretanje)
- [Test nalozi](#test-nalozi)
- [Bodovne stavke](#bodovne-stavke)
- [Struktura projekta](#struktura-projekta)
- [API dokumentacija](#api-dokumentacija)
- [Baza podataka](#baza-podataka)
- [Sigurnost](#sigurnost)
- [Autor](#autor)

---

## O projektu

Web aplikacija za štampariju **MEN-ING SISTEM DOO** iz Smedereva. Štamparija se bavi proizvodnjom:

- Kartonskih kutija za roštilj
- Srebrnih kutija za pomfrit i toplu hranu
- Kutija za kolače (sa providnim prozorom)
- Podmetača za piće
- Kalendara
- Blokovske robe (otpremnice, računi, reversi)

Sajt omogućava:

- Pregled kategorija, proizvoda i blog objava (javno)
- Registraciju i prijavu korisnika
- Direktno poručivanje proizvoda (bez korpe) sa izborom dostave
- Praćenje statusa porudžbina
- Admin panel sa CRUD operacijama nad svim resursima

---

## Tehnologije

### Backend
- **Java 21** + **Spring Boot 3.3.4**
- **Spring Web** (REST)
- **Spring Data JPA** + **Hibernate**
- **Spring Security** + **JWT (jjwt 0.12.6)** + Refresh token
- **Validation API** (Jakarta Bean Validation)
- **Lombok** (smanjuje boilerplate)
- **H2** (dev profil, in-memory) / **MySQL** (prod profil)
- **Maven** (sa wrapperom `mvnw`)

### Frontend
- **React 19** + **Vite 8**
- **React Router 7** (rute)
- **Axios** (sa interceptorom za auto-refresh JWT)
- **React Bootstrap 5** + **Bootstrap Icons**

---

## Pokretanje

### Preduslovi
- **Java JDK 17+** (testirano sa JDK 21)
- **Node.js 18+** (testirano sa Node 22)
- Opcionalno: **MySQL 8** (ako se koristi `prod` profil)

### 1) Backend

```bash
cd backend
.\mvnw.cmd spring-boot:run    # Windows
./mvnw spring-boot:run        # Linux/Mac
```

Backend se podiže na **http://localhost:8080**. Po default-u koristi **H2** in-memory bazu (`dev` profil), pa nije potrebno instalirati MySQL.

**H2 konzola** dostupna na: http://localhost:8080/h2-console (JDBC URL: `jdbc:h2:mem:stamparija`)

Za MySQL profil:

```bash
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=prod
```

Konfiguracija MySQL-a je u `backend/src/main/resources/application-prod.properties`.

### 2) Frontend

```bash
cd frontend
npm install
npm run dev
```

Frontend se podiže na **http://localhost:5173**.

---

## Test nalozi

Aplikacija pri prvom pokretanju automatski kreira sledeće naloge (`DataSeeder.java`):

| Username | Lozinka | Role | Opis |
|----------|---------|------|------|
| `admin` | `admin123` | ROLE_ADMIN + ROLE_USER | Pun pristup admin panelu |
| `kupac` | `kupac123` | ROLE_USER | Običan korisnik, može da poručuje |

Takođe se kreira **6 kategorija**, **13 proizvoda** i **3 blog objave**.

---

## Bodovne stavke (30 poena)

| # | Stavka | Poeni | Implementacija |
|---|--------|-------|----------------|
| 1 | **[BE]** Postavljanje arhitekture | 5 | Spring Boot 3, slojevita arhitektura (controller → service → repository), DTO sloj, globalni exception handler, ModelMapper, profili (dev/prod), CORS, statički resursi |
| 2 | **[BE + FE]** CRUD za 1 tabelu | 5 | Implementirane su CRUD operacije za **sve** entitete (Categories, Products, Orders, BlogPosts, Users), zajedno sa frontend stranicama u admin panelu |
| 3 | **[BE]** OneToMany i ManyToMany relacije | 5 | `User ⟷ Role` (ManyToMany kroz `user_roles` tabelu), `Category → Product` (OneToMany), `User → Order` (OneToMany), `Order → OrderItem` (OneToMany), `Order ⟷ Product` (ManyToMany kroz `OrderItem`), `User → BlogPost` (OneToMany) |
| 4 | **[BE]** Autentifikacija + Autorizacija + JWT | 4 | Spring Security, BCrypt enkripcija, `JwtTokenProvider` (HS256), `JwtAuthenticationFilter`, `@PreAuthorize("hasRole('ADMIN')")` na admin endpoint-ima, `EntryPoint` za 401 odgovore |
| 5 | **[FE]** Autentifikacija + Autorizacija | 4 | Login/Register stranice, `AuthContext`, `ProtectedRoute` komponenta, zaštita admin ruta, axios interceptor automatski šalje Bearer token |
| 6 | **[BE + FE]** Auto-refresh tokena | 4 | Backend: `/api/auth/refresh` endpoint, `RefreshToken` entitet sa expiration. Frontend: axios response interceptor automatski hvata 401, poziva refresh, ponavlja originalni zahtev, sve transparentno za korisnika |
| 7 | **[DOC]** Postman dokumentacija | 3 | Kompletna kolekcija u `postman/stamparija-api.postman_collection.json` sa primerima odgovora |
| | **UKUPNO** | **30** | |

---

## Struktura projekta

```
ZavrsniISA/
├── backend/                              Spring Boot aplikacija
│   ├── src/main/java/rs/meningsistem/stamparija/
│   │   ├── StamparijaApplication.java
│   │   ├── config/                       AppConfig, SecurityConfig, WebConfig, DataSeeder
│   │   ├── controller/                   REST kontroleri
│   │   ├── dto/                          DTO klase (auth/, request/, response/)
│   │   ├── exception/                    Custom exceptioni + GlobalExceptionHandler
│   │   ├── model/                        JPA entiteti + enums
│   │   ├── repository/                   Spring Data JPA repozitorijumi
│   │   ├── security/                     UserPrincipal, UserDetailsService, JWT
│   │   └── service/                      Servisi + impl
│   ├── src/main/resources/
│   │   ├── application.properties
│   │   ├── application-dev.properties
│   │   └── application-prod.properties
│   ├── uploads/                          Statičke slike proizvoda
│   ├── pom.xml
│   └── mvnw / mvnw.cmd                   Maven wrapper
├── frontend/                             React + Vite aplikacija
│   ├── src/
│   │   ├── api/                          axios klijent + interceptor + servisi
│   │   ├── auth/ProtectedRoute.jsx
│   │   ├── components/layout/            Navbar, Footer, FeatureStrip
│   │   ├── context/                      AuthContext, ToastContext
│   │   ├── pages/                        Sve stranice (Public + User)
│   │   │   └── admin/                    Admin panel stranice
│   │   ├── utils/format.js
│   │   ├── App.jsx
│   │   ├── index.css
│   │   └── main.jsx
│   ├── public/
│   ├── index.html
│   ├── vite.config.js
│   └── package.json
├── postman/
│   └── stamparija-api.postman_collection.json    Postman kolekcija
├── slike/                                Originalne slike proizvoda (kopirane u backend/uploads)
├── .gitignore
└── README.md
```

---

## API dokumentacija

Backend izlaže RESTful API na bazi `http://localhost:8080`.

### Javni endpoint-i (bez autentifikacije)

| Metoda | URL | Opis |
|--------|-----|------|
| `POST` | `/api/auth/login` | Prijava (vraća JWT + refresh token) |
| `POST` | `/api/auth/register` | Registracija novog korisnika |
| `POST` | `/api/auth/refresh` | Osvežavanje access tokena |
| `GET`  | `/api/categories` | Lista svih kategorija |
| `GET`  | `/api/categories/{id}` | Pojedinačna kategorija |
| `GET`  | `/api/products` | Lista proizvoda (query: `categoryId`, `search`) |
| `GET`  | `/api/products/{id}` | Pojedinačni proizvod |
| `GET`  | `/api/blog` | Lista blog objava |
| `GET`  | `/api/blog/{id}` | Pojedinačna objava |
| `GET`  | `/uploads/{filename}` | Statičke slike |

### Autentifikovani (bilo koji korisnik)

| Metoda | URL | Opis |
|--------|-----|------|
| `GET`    | `/api/auth/me` | Trenutni korisnik |
| `POST`   | `/api/auth/logout` | Odjava (briše refresh token) |
| `POST`   | `/api/orders` | Kreiraj porudžbinu |
| `GET`    | `/api/orders/my` | Moje porudžbine |
| `GET`    | `/api/orders/{id}` | Pojedinačna porudžbina (samo svoja ili ako je admin) |

### Admin only (`@PreAuthorize("hasRole('ADMIN')")`)

| Metoda | URL | Opis |
|--------|-----|------|
| `POST/PUT/DELETE` | `/api/categories[/{id}]` | CRUD kategorija |
| `POST/PUT/DELETE` | `/api/products[/{id}]` | CRUD proizvoda |
| `POST/PUT/DELETE` | `/api/blog[/{id}]` | CRUD blog objava |
| `GET` | `/api/orders` | Sve porudžbine |
| `PATCH` | `/api/orders/{id}/status` | Promena statusa porudžbine |
| `DELETE` | `/api/orders/{id}` | Brisanje porudžbine |
| `GET/PATCH/DELETE` | `/api/users[/{id}]` | Upravljanje korisnicima |

### Postman kolekcija

Postman kolekcija sa svim endpoint-ima i sačuvanim primerima odgovora nalazi se u:

```
postman/stamparija-api.postman_collection.json
```

**Kako koristiti**:
1. Importujte kolekciju u Postman
2. Pokrenite backend (`http://localhost:8080`)
3. Kliknite na **Auth → Login (Admin)** – token se automatski sačuva u variable
4. Ostali zahtevi automatski koriste sačuvani token

---

## Baza podataka

### Tabele

1. **users** – korisnički nalozi
2. **roles** – role (ROLE_ADMIN, ROLE_USER)
3. **user_roles** – ManyToMany veza između users i roles
4. **categories** – kategorije proizvoda
5. **products** – proizvodi
6. **orders** – porudžbine
7. **order_items** – stavke porudžbine (ManyToMany Order ↔ Product)
8. **blog_posts** – blog objave
9. **refresh_tokens** – refresh tokeni za JWT

### Relacije

```
User ⟷ Role           ManyToMany (kroz user_roles)
User → Order          OneToMany
User → BlogPost       OneToMany
User → RefreshToken   OneToOne
Category → Product    OneToMany
Order → OrderItem     OneToMany
Product → OrderItem   OneToMany
Order ⟷ Product       ManyToMany (kroz OrderItem)
```

---

## Sigurnost

### JWT konfiguracija
- **Algoritam:** HS256 (HMAC-SHA256)
- **Access token:** 15 minuta (`app.jwt.access-token-expiration-ms=900000`)
- **Refresh token:** 7 dana (`app.jwt.refresh-token-expiration-ms=604800000`)

### Auto-refresh flow

```
1. Klijent salje zahtev sa Bearer access tokenom
2. Backend vrati 401 (token istekao)
3. Axios response interceptor uhvati 401
4. Salje POST /api/auth/refresh sa refresh tokenom
5. Backend vrati novi access + refresh token
6. Interceptor sačuva nove tokene
7. Originalni zahtev se ponavlja sa novim tokenom
8. Korisniku je sve transparentno
```

### Lozinke
- Hashirane sa BCrypt (Spring Security `BCryptPasswordEncoder`)
- Minimum 6 karaktera (validacija)

---

## Predaja projekta

Po instrukcijama za završni ispit:

- ✅ GitHub repozitorijum (BE + FE u jednom repo-u)
- ✅ Postman kolekcija sa primerima odgovora (`postman/stamparija-api.postman_collection.json`)
- ✅ Najmanje 5 tabela (imamo 9)
- ✅ Sve bodovne stavke implementirane

**Privatni nalog za pristup:** dodati `bpapaz@singimail.rs` kao kolaboratora ako je repo privatan.

---

## Autor

Završni projekat iz predmeta **Internet softverske arhitekture**
Tema: Web aplikacija za štampariju **MEN-ING SISTEM DOO**, Smederevo
Godina: 2026
