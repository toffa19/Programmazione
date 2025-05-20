# Piattaforma Interattiva per l’Apprendimento della Programmazione Java

**Autori:** Matteo Nanni, Christian Toffalori  
**Data:** 17/05/2025

Un’applicazione desktop realizzata con JavaFX e Maven che guida l’utente (studente) attraverso esercizi a risposta multipla suddivisi per macro-temi e livelli di difficoltà, adottando l’architettura **MVVM** (Model-View-ViewModel).

---

## 🚀 Caratteristiche principali

- **Autenticazione & Registrazione**: gestione degli utenti con ruoli **student** ed **editor**.  
- **Dashboard Interattiva**: panoramica dei progressi, streak giornalieri, percentuale di completamento e classifica (solo studenti).  
- **10 Macro-Temi**, ciascuno con 9 esercizi (3 facili, 3 medi, 3 difficili).  
- **Feedback Immediato**: risposta corretta/sbagliata con conteggio tentativi.  
- **Cronometro**: misura il tempo di completamento, conserva il miglior punteggio e il minor tempo.  
- **Persistenza Locale**: dati salvati su file JSON (`users.json`, `exercises.json`).

---

## 📋 Requisiti

- **Java 17** o superiore  
- **Maven 3.6+**  
- **Internet** per scaricare le dipendenze Maven (una tantum)

---

## ⚙️ Installazione & Avvio

1. **Clona il repository**
   ```bash
   git clone https://github.com/<tuo-username>/programmazione-javafx.git
   cd programmazione-javafx
## Apri con IntelliJ IDEA

1. **File > Open** e seleziona la cartella del progetto.  
2. Verifica che venga riconosciuto come progetto Maven.

---

## Configura SDK e JavaFX

- **Project SDK:** Java 17  
- **Project language level:** 17  
- Dependencies Maven importate automaticamente.

---

## Crea la configurazione di Run

1. **Run > Edit Configurations > + (Maven)**  
2. **Name:** Run JavaFX  
3. **Command line:** `javafx:run`

---

## Esegui l’applicazione

- Seleziona la configurazione **Run JavaFX** e clicca ▶️ **Run**

---

## 🏗️ Architettura del progetto
com.progetto
├─ model/ # Entità di dominio (User, Exercise, QuestionResult, Progress)
├─ repository/ # Lettura/scrittura JSON (UsersWrapper, ExercisesWrapper)
├─ util/ # Utility generali (SessionManager, Config)
├─ viewmodel/ # Logica di interazione (HomeVM, ExercisesVM, PathVM, …)
├─ view/ # FXML + Controller (Login, Home, Path, Topic, Exercises, Profile…)
└─ assets/ # CSS, immagini, risorse

- **MVVM:** separazione netta fra Model, View (.fxml) e ViewModel.  
- **Maven** gestisce le dipendenze (`jackson-databind`, `javafx-controls`, …) e il ciclo di build.

---

## 📂 Gestione dati

- `src/data/users.json`: anagrafica, credenziali, progressi e loginDates degli utenti.  
- `src/data/exercises.json`: elenchi di esercizi organizzati per macro-tema e difficoltà.  
- I repository (`UserRepository`, `ExerciseRepository`) serializzano/deserializzano tramite Jackson.

---

## 🤝 Contribuire

1. Fork del progetto  
2. Crea un branch: `git checkout -b feature/nome-feature`  
3. Apporta modifiche e fai commit: `git commit -am 'Aggiunta nuova feature'`  
4. Push sul branch: `git push origin feature/nome-feature`  
5. Apri una Pull Request su GitHub

---

## 📄 License

Distribuito sotto licenza MIT. Vedi `LICENSE` per i dettagli.
