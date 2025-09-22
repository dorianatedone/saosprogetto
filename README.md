# Sistema di candidatura per una offerta di lavoro
# Introduzione
In questo documento si descrive il funzionamento di un sistema di candidatura per una offerta di lavoro in ambito IT. Il candidato ha la possibilità di candidarsi ad una offerta di lavoro tra quelle proposte e visualizzare un messaggio che conferma che sia avvenuta correttamente la sua candidatura.
# Architettura
L'applicazione "TrovaLavoro" utilizza Spring Boot, un framework Java che semplifica lo sviluppo di applicazioni stand-alone.
L'applicazione utilizza OAuth2 per l'autenticazione.

L'OAuth2 è uno standard che consente di autorizzare un'app o un servizio ad accedere ad un altro senza divulgare informazioni private come la password.
Lo vediamo spesso nelle pagine di login di siti web o applicazioni in cui ci viene chiesto di utilizzare le credenziali proprie del sito o di poter fare un login tramite google, facebook ecc..
Nel nostro caso specifico, abbiamo utilizzato Facebook come metodo di autenticazione.

# Tecnologie
Le tecnologie utilizzate per questo caso di studio sono:
- Linguaggio: JAVA (JDK 17)
- Framework: Spring Boot 3.0.0
- Thymeleaf
- HTML

# Funzionamento
- Homepage: L'utente accede alla homepage mediante l'url "localhost:8080/login"
- Autenticazione: L'utente clicca sul pulsante "Accedi con facebook" per avviare il flusso di autenticazione di OAuth2
- Redirect al Provider FB: L'utente viene reindirizzato sulla pagina di login di Facebook.
- Autenticazione su FB: L'utente inserisce mail e password per l'accesso. Riceverà una notifica fb sulla sua app che confermerà la sua identità e l'attendibilità della postazione da cui si fa richiesta.
- Recupero informazioni: Una volta autenticato, l'utente vedrà il proprio nome visualizzato sulla pagina delle candidature (dato recuperato dal Provider)
- Dashboard offerte di lavoro: Visualizzazione della pagina con le offerte di lavoro
- Candidatura: L'utente si candiderà ad una delle offerte proposte mediante il pulsante "Candidati"
- Candidatura effettuata: Cliccando il pulsante "Candidati" apparirà un messaggio per confermare all'utente (indicando nome e cognome) che la candidatura è andata a buon fine e che riceverà un riscontro alla mail recuperata dal provider fb.
- Logout da facebook: L'utente avrà la possibilità di uscire dalla pagina e ritornare alla pagina iniziale di login

# Struttura del Progetto
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
 
# Configurazione
Le dipendenze usate dall'OAuth2 sono indicate nel pom.xml




