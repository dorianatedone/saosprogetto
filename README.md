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






