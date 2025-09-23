# Sistema di candidatura per una offerta di lavoro
## Introduzione
In questo documento si descrive il funzionamento di un sistema di candidatura per una offerta di lavoro in ambito IT. Il candidato ha la possibilità di candidarsi ad una offerta di lavoro tra quelle proposte e visualizzare un messaggio che conferma che sia avvenuta correttamente la sua candidatura.
## Architettura
L'applicazione "TrovaLavoro" utilizza Spring Boot, un framework Java che semplifica lo sviluppo di applicazioni stand-alone.
L'applicazione utilizza OAuth2 per l'autenticazione.

L'OAuth2 è uno standard che consente di autorizzare un'app o un servizio ad accedere ad un altro senza divulgare informazioni private come la password.
Lo vediamo spesso nelle pagine di login di siti web o applicazioni in cui ci viene chiesto di utilizzare le credenziali proprie del sito o di poter fare un login tramite google, facebook ecc..
Nel nostro caso specifico, abbiamo utilizzato Facebook come metodo di autenticazione.

## Tecnologie
Le tecnologie utilizzate per questo caso di studio sono:
- Linguaggio: JAVA (JDK 17)
- Framework: Spring Boot 3.0.0
- Thymeleaf
- HTML
- Javascript

## Funzionamento
- Homepage: L'utente accede alla homepage mediante l'url "localhost:8080/login"
- Autenticazione: L'utente clicca sul pulsante "Accedi con facebook" per avviare il flusso di autenticazione di OAuth2
- Redirect al Provider FB: L'utente viene reindirizzato sulla pagina di login di Facebook.
- Autenticazione su FB: L'utente inserisce mail e password per l'accesso. Riceverà una notifica fb sulla sua app che confermerà la sua identità e l'attendibilità della postazione da cui si fa richiesta.
- Recupero informazioni: Una volta autenticato, l'utente vedrà il proprio nome visualizzato sulla pagina delle candidature (dato recuperato dal Provider)
- Dashboard offerte di lavoro: Visualizzazione della pagina con le offerte di lavoro
- Candidatura: L'utente si candiderà ad una delle offerte proposte mediante il pulsante "Candidati"
- Candidatura effettuata: Cliccando il pulsante "Candidati" apparirà un messaggio per confermare all'utente (indicando nome e cognome) che la candidatura è andata a buon fine e che riceverà un riscontro alla mail recuperata dal provider fb.
- Logout da facebook: L'utente avrà la possibilità di uscire dalla pagina e ritornare alla pagina iniziale di login

## Struttura del Progetto
Il progetto presenta la seguente struttura:
- springboot-facebook-login
  - src\main
    - java\com\example
      - TrovaLavoroApplication.java
      - controller
        - MainController.java
      - security
        - SecurityConfig.java
    - resources
      - templates
        - login.html
        - user.html
      - application.yml
  - target
    - classes
      - com\example
        - controller
          - MainController.class
        - security
          - SecurityConfig.class
        - TrovaLavoroApplication.class
      - templates
        - login.html
        - user.html
      - application.yml
    - test-classes
  - pom.xml
 
## Configurazione
Le dipendenze usate dall'OAuth2 sono indicate nel pom.xml:

```javascript
<project xmlns="http://maven.apache.org/POM/4.0.0" 
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
                             http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.example</groupId>
  <artifactId>springboot-facebook-login</artifactId>
  <version>0.0.1-SNAPSHOT</version>
  <packaging>jar</packaging>
  <name>springboot-facebook-login</name>
  <description>Spring Boot Facebook OAuth2 Login Project</description>

  <parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.0.0</version>
    <relativePath/>
  </parent>

  <properties>
    <java.version>17</java.version>
  </properties>

  <dependencies>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-oauth2-client</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-thymeleaf</artifactId>
    </dependency>
  </dependencies>

  <build>
    <plugins>
      <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
      </plugin>
    </plugins>
  </build>
</project>
```

La configurazione specifica per Facebook come provider OAuth2 è presente nel file application.yml:

```javascript
spring:
  security:
    oauth2:
      client:
        registration:
          facebook:
            client-id: 1110584913697828
            client-secret: 5e2bfccdc764d7cc9357b251d4b67287
            redirect-uri: "http://localhost:8080/login/oauth2/code/facebook"
            scope:
              - email
              - public_profile
        provider:
          facebook:
            authorization-uri: https://www.facebook.com/dialog/oauth
            token-uri: https://graph.facebook.com/oauth/access_token
            user-info-uri: https://graph.facebook.com/me?fields=id,name,email,picture
            user-name-attribute: id
server:
  port: 8080
```

La classe SecurityConfig configura la sicurezza di Spring Security per un’applicazione Spring Boot che usa OAuth2 login:

```javascript
package com.example.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
          .authorizeHttpRequests(auth -> auth
              .requestMatchers("/login", "/css/**").permitAll()
              .anyRequest().authenticated()
          )
          .oauth2Login(oauth2 -> oauth2
              .loginPage("/login")
              .defaultSuccessUrl("/user", true)
              .failureUrl("/login?error=true")
          )
          .logout(logout -> logout
              .logoutSuccessUrl("/login?logout=true")
          );
        return http.build();
    }
}
```

La classe TrovoLavoroApplication.java è la classe principale che avvia l'applicazione Spring Boot:

```javascript
package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TrovaLavoroApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrovaLavoroApplication.class, args);
    }
}
```

La classe MainController.java gestisce le richieste presenti nelle due pagine principali: login.html e user.html

```javascript
package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import java.util.Map;

@Controller
public class MainController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/user")
    public String user(Model model, OAuth2AuthenticationToken authentication) {
        OAuth2User user = authentication.getPrincipal();
        model.addAttribute("name", user.getAttribute("name"));
        model.addAttribute("email", user.getAttribute("email"));
        model.addAttribute("id", user.getAttribute("id"));
        model.addAttribute("picture", ((Map) user.getAttribute("picture")).get("data"));
        return "user";
    }
}
```

@Controller indica che è un controller MVC di Spring: riceve richieste HTTP e restituisce viste (pagine HTML tramite Thymeleaf)

Il metodo login() gestisce le richieste GET a /login e restituisce la stringa "login". A questo punto Spring cercherà un template login.html nella cartella resources/templates mostrandola.

Il metodo user() gestisce le richieste GET a /user e riceve:

  - Model model: per passare i dati alla vista (pagina HTML)
  
  - OAuth2AuthenticationToken authentication: contiene le informazioni sull'utente autenticato tramite OAuth2 (Facebook)
  
All'interno del metodo abbiamo:

  - OAuth2User user = authentication.getPrincipal(); : che recupera l'utente autenticato
  
  - Aggiunge a Model alcuni attributi presi dal profilo dell'utente OAuth2:
  
    - model.addAttribute("name", user.getAttribute("name"));
    
    - model.addAttribute("email", user.getAttribute("email"));
    
  così in Thymeleaf può usare ${name}, ${email}.
  
Restituisce "user", quindi Spring mostrerà la pagina user.html

## File in HTML con Thymeleaf
Thymeleaf è un motore di template Java, cioè una libreria che permette di collegare i dati del backend (nel nostro caso Spring Boot) con il fontend HTML.
Fa da "ponte" tra Java e HTML, prendendo i dati dall'applicazione e inserendoli in HTML in modo dinamico. 
Un esempio lo riportiamo di seguito dal nostro file user.html:

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>User Info</title>
</head>
<body>
    <h2>Benvenuto, <span th:text="${name}">Nome</span>!</h2>
    <form th:action="@{/logout}" method="post">
        <button type="submit">Logout</button>
    </form>
```
Quando la pagina sarà renderizzata, ${name} sarà sostituito con il nome completo presente su Facebook (es. "Benvenuto Doriana Tedone!")

login.html è la homepage che permette al candidato di fare il login al portale con le offerte di lavoro, tramite facebook:

```html
<!DOCTYPE html>
<html lang="it" xmlns:th="http://www.thymeleaf.org">
<head>
  <meta charset="utf-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <title>Accedi</title>
  <style>
    :root{
      --bg: #f6f7fb;        /* sfondo neutro chiaro */
      --card-bg: #ffffff;
      --text: #111827;
      --muted: #6b7280;
      --border: #e5e7eb;
      --primary: #1877f2;   /* Facebook */
      --primary-600: #166fe0;
      --radius: 12px;
    }
    * { box-sizing: border-box; }
    html, body { height: 100%; }
    body{
      margin: 0;
      background: var(--bg);
      color: var(--text);
      font: 500 16px/1.5 system-ui, -apple-system, "Segoe UI", Roboto, Ubuntu, Cantarell, "Helvetica Neue", Arial, "Noto Sans";
      display: grid;
      place-items: center;
      padding: 24px;
    }
    .card{
      width: 100%;
      max-width: 420px;
      background: var(--card-bg);
      border: 1px solid var(--border);
      border-radius: var(--radius);
      padding: 24px;
      box-shadow: 0 8px 24px rgba(0,0,0,.05);
    }
    .title{ margin: 0 0 6px; font-size: 24px; }
    .subtitle{ margin: 0 0 16px; color: var(--muted); font-weight: 400; font-size: 15px; }
    .btn{
      display: inline-block;
      width: 100%;
      padding: 12px 14px;
      border-radius: 10px;
      text-align: center;
      text-decoration: none;
      font-weight: 600;
      color: #fff;
      background: var(--primary);
      border: 1px solid transparent;
      transition: background .15s ease, transform .06s ease;
    }
    .btn:hover{ background: var(--primary-600); }
    .btn:active{ transform: translateY(1px); }
    .meta{
      margin-top: 12px;
      color: var(--muted);
      font-size: 12px;
      text-align: center;
    }
    .alerts{ margin-top: 14px; display: grid; gap: 8px; }
    .alert{
      padding: 10px 12px; border-radius: 8px; border: 1px solid var(--border); font-size: 14px;
      background: #f9fafb; color: #111827;
    }
    @media (prefers-color-scheme: dark){
      :root{
        --bg: #0f1115;
        --card-bg: #17191f;
        --text: #e5e7eb;
        --muted: #9aa3b2;
        --border: #2a2f37;
      }
      .alert{ background:#1f232b; color:#e5e7eb; }
    }
  </style>
</head>
<body>
  <main class="card" role="main" aria-labelledby="login-title">
    <h1 id="login-title" class="title">Benvenuto/a 👋</h1>
    <p class="subtitle">Accedi per continuare. Puoi usare il tuo account Facebook.</p>

    <a class="btn" th:href="@{/oauth2/authorization/facebook}" role="button" aria-label="Accedi con Facebook">
      Accedi con Facebook
    </a>

    <p class="meta">
      Continuando accetti i nostri <a href="#" style="color:inherit;text-decoration:underline;">Termini</a> e la
      <a href="#" style="color:inherit;text-decoration:underline;">Privacy Policy</a>.
    </p>

  </main>
</body>
</html>
```

user.html gestisce invece la dashboard con tutte le offerte di lavoro disponibili, a cui è possibile candidarsi:

```html
[...]
<body>
<header class="container">
    <h1>Annunci di Lavoro</h1>
    <p>In questa pagina sono disponibili annunci di lavoro con dettagli espandibili.</p>
    <nav aria-label="Sezioni">
        <a href="#recenti">Recenti</a> · <a href="#fulltime">Full-time</a> · <a href="#remote">Remote</a>
    </nav>

    <!-- NEW: box messaggio statico che verrà mostrato al click su "Candidati" -->
    <p id="apply-message" role="status" aria-live="polite">
        Candidatura ricevuta correttamente!
        <strong><span th:text="${name}">Nome</span></strong>
        riceverai al più presto un riscontro alla mail <strong><span th:text="${email}"></span></strong>!
    </p>
    <!-- Area in cui verrà mostrato il benvenuto all'utente loggato -->
    <!-- <div id="user-welcome" aria-live="polite"></div> -->
</header>

<main class="container">
    <section id="recenti" aria-labelledby="h-recenti">
        <h2 id="h-recenti" class="muted">Annunci recenti</h2>
        <div class="grid">

            <!-- Job 1 -->
            <article class="card" itemscope itemtype="https://schema.org/JobPosting">
                <div class="row">
                    <h2 itemprop="title">Frontend Engineer</h2>
                    <span class="badge" itemprop="employmentType">Full-time</span>
                </div>
                <p class="muted"><span itemprop="hiringOrganization" itemscope itemtype="https://schema.org/Organization">
                    <span itemprop="name">PixelWorks</span></span> ·
                    <span itemprop="jobLocation" itemscope itemtype="https://schema.org/Place"><span itemprop="addressLocality">Milano</span>, <span itemprop="addressCountry">IT</span></span></p>
                <p class="salary">
                  <span itemprop="baseSalary" itemscope itemtype="https://schema.org/MonetaryAmount">
                    <meta itemprop="currency" content="EUR"/>
                    <span itemprop="minValue" content="38000">38.000</span> –
                    <span itemprop="maxValue" content="52000">52.000</span> €
                  </span>
                </p>
                <p class="muted">Pubblicato:
                    <time itemprop="datePosted" datetime="2025-08-21">21 ago 2025</time>
                </p>
                <p class="tags"><span class="tag">React</span><span class="tag">TypeScript</span><span class="tag">Tailwind</span></p>
                <details>
                    <summary>Dettagli &amp; candidatura</summary>
                    <p itemprop="description">Sviluppo UI moderne, design system e performance.</p>
                    <p><a href="#" aria-label="Candidati a Frontend Engineer">Candidati</a></p>
                </details>
            </article>

            <!-- Job 2 -->
            <article class="card" itemscope itemtype="https://schema.org/JobPosting">
                <div class="row">
                    <h2 itemprop="title">Backend Developer</h2>
                    <span class="badge" itemprop="employmentType">Full-time</span>
                </div>
                <p class="muted"><span itemprop="hiringOrganization" itemscope itemtype="https://schema.org/Organization">
                    <span itemprop="name">DataForge</span></span> ·
                    <span itemprop="jobLocation" itemscope itemtype="https://schema.org/Place"><span itemprop="addressLocality">Roma</span>, <span itemprop="addressCountry">IT</span></span></p>
                <p class="salary">
                  <span itemprop="baseSalary" itemscope itemtype="https://schema.org/MonetaryAmount">
                    <meta itemprop="currency" content="EUR"/>
                    <span itemprop="minValue" content="42000">42.000</span> –
                    <span itemprop="maxValue" content="60000">60.000</span> €
                  </span>
                </p>
                <p class="muted">Pubblicato:
                    <time itemprop="datePosted" datetime="2025-08-28">28 ago 2025</time>
                </p>
                <p class="tags"><span class="tag">Node.js</span><span class="tag">PostgreSQL</span><span class="tag">Docker</span></p>
                <details>
                    <summary>Dettagli &amp; candidatura</summary>
                    <p itemprop="description">API scalabili, microservizi e integrazioni.</p>
                    <p><a href="#">Candidati</a></p>
                </details>
            </article>

            <!-- Job 3 -->
            <article class="card" itemscope itemtype="https://schema.org/JobPosting">
                <div class="row">
                    <h2 itemprop="title">UX/UI Designer</h2>
                    <span class="badge" itemprop="employmentType">Contract</span>
                </div>
                <p class="muted"><span itemprop="hiringOrganization" itemscope itemtype="https://schema.org/Organization">
                    <span itemprop="name">Mosaic Studio</span></span> ·
                    <span itemprop="jobLocation" itemscope itemtype="https://schema.org/Place"><span itemprop="addressLocality">Torino</span>, <span itemprop="addressCountry">IT</span></span></p>
                <p class="salary">
                  <span itemprop="baseSalary" itemscope itemtype="https://schema.org/MonetaryAmount">
                    <meta itemprop="currency" content="EUR"/>
                    <span itemprop="minValue" content="28000">28.000</span> –
                    <span itemprop="maxValue" content="40000">40.000</span> €
                  </span>
                </p>
                <p class="muted">Pubblicato:
                    <time itemprop="datePosted" datetime="2025-08-26">26 ago 2025</time>
                </p>
                <p class="tags"><span class="tag">Figma</span><span class="tag">UX</span><span class="tag">UI</span></p>
                <details>
                    <summary>Dettagli &amp; candidatura</summary>
                    <p itemprop="description">Ricerca utenti, wireframe, prototipi.</p>
                    <p><a href="#">Candidati</a></p>
                </details>
            </article>

            <!-- Job 4 -->
            <article class="card" itemscope itemtype="https://schema.org/JobPosting">
                <div class="row">
                    <h2 itemprop="title">DevOps Engineer</h2>
                    <span class="badge" itemprop="employmentType">Full-time</span>
                </div>
                <p class="muted"><span itemprop="hiringOrganization" itemscope itemtype="https://schema.org/Organization">
                    <span itemprop="name">Cloudly</span></span> ·
                    <span itemprop="jobLocation" itemscope itemtype="https://schema.org/Place"><span itemprop="addressLocality">Remote</span>, <span itemprop="addressCountry">IT</span></span></p>
                <p class="salary">
                  <span itemprop="baseSalary" itemscope itemtype="https://schema.org/MonetaryAmount">
                    <meta itemprop="currency" content="EUR"/>
                    <span itemprop="minValue" content="50000">50.000</span> –
                    <span itemprop="maxValue" content="75000">75.000</span> €
                  </span>
                </p>
                <p class="muted">Pubblicato:
                    <time itemprop="datePosted" datetime="2025-08-31">31 ago 2025</time>
                </p>
                <p class="tags"><span class="tag">AWS</span><span class="tag">Docker</span><span class="tag">Kubernetes</span></p>
                <details>
                    <summary>Dettagli &amp; candidatura</summary>
                    <p itemprop="description">CI/CD, IaC e osservabilità.</p>
                    <p><a href="#">Candidati</a></p>
                </details>
            </article>

            <!-- Job 5 -->
            <article class="card" itemscope itemtype="https://schema.org/JobPosting">
                <div class="row">
                    <h2 itemprop="title">Data Analyst</h2>
                    <span class="badge" itemprop="employmentType">Part-time</span>
                </div>
                <p class="muted"><span itemprop="hiringOrganization" itemscope itemtype="https://schema.org/Organization">
                    <span itemprop="name">InsightLab</span></span> ·
                    <span itemprop="jobLocation" itemscope itemtype="https://schema.org/Place"><span itemprop="addressLocality">Bologna</span>, <span itemprop="addressCountry">IT</span></span></p>
                <p class="salary">
                  <span itemprop="baseSalary" itemscope itemtype="https://schema.org/MonetaryAmount">
                    <meta itemprop="currency" content="EUR"/>
                    <span itemprop="minValue" content="25000">25.000</span> –
                    <span itemprop="maxValue" content="32000">32.000</span> €
                  </span>
                </p>
                <p class="muted">Pubblicato:
                    <time itemprop="datePosted" datetime="2025-08-30">30 ago 2025</time>
                </p>
                <p class="tags"><span class="tag">SQL</span><span class="tag">Python</span><span class="tag">PowerBI</span></p>
                <details>
                    <summary>Dettagli &amp; candidatura</summary>
                    <p itemprop="description">Reportistica, dashboard e analisi KPI.</p>
                    <p><a href="#">Candidati</a></p>
                </details>
            </article>

            <!-- Job 6 -->
            <article class="card" itemscope itemtype="https://schema.org/JobPosting">
                <div class="row">
                    <h2 itemprop="title">Project Manager</h2>
                    <span class="badge" itemprop="employmentType">Contract</span>
                </div>
                <p class="muted"><span itemprop="hiringOrganization" itemscope itemtype="https://schema.org/Organization">
                    <span itemprop="name">BridgeOps</span></span> ·
                    <span itemprop="jobLocation" itemscope itemtype="https://schema.org/Place"><span itemprop="addressLocality">Verona</span>, <span itemprop="addressCountry">IT</span></span>
                </p>
                <p class="salary">
                  <span itemprop="baseSalary" itemscope itemtype="https://schema.org/MonetaryAmount">
                    <meta itemprop="currency" content="EUR"/>
                    <span itemprop="minValue" content="30000">30.000</span> –
                    <span itemprop="maxValue" content="45000">45.000</span> €
                  </span>
                </p>
                <p class="muted">Pubblicato:
                    <time itemprop="datePosted" datetime="2025-08-24">24 ago 2025</time>
                </p>
                <p class="tags"><span class="tag">Agile</span><span class="tag">Scrum</span><span class="tag">Stakeholders</span></p>
                <details>
                    <summary>Dettagli &amp; candidatura</summary>
                    <p itemprop="description">Pianificazione, sprint e comunicazione con clienti.</p>
                    <p><a href="#">Candidati</a></p>
                </details>
            </article>

        </div>
    </section>
    [...]
```

## Conclusione
Utilizzando l'OAuth2 nell'applicativo TrovaLavoro, si elimina la necessità per l'applicazione di gestire le credenziali degli utenti, affidando questo compito a provider sicuri e affidabili come facebook.
Si evita anche la creazione di nuove credenziali, solo per quel sito specifico, perchè si da la possibilità all'utente di utilizzare credenziali già esistenti per facebook. 
