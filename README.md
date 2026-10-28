# Order Management API

Un API REST dezvoltat in **Spring Boot** si **Java 17** pentru gestionarea eficienta a comenzilor, incluzand validari, gestionarea statusurilor si tratarea centralizata a erorilor.

---

## Tehnologii Utilizate
* **Java 17**
* **Spring Boot**
* **Maven** (pentru managementul dependentelor si build)
* **Spring Data JPA & Hibernate**
* **H2 Database** (baza de date in memorie pentru persistenta si teste)
* **Jakarta Validation** (pentru validarea datelor primite)

---

## Requirements
Inainte de a rula aplicatia, asigura-te ca ai instalate pe sistemul tau:
* **JDK 17** sau o versiune compatibila.
* **Maven**

---

## Instructiuni de Utilizare

### 1. Clonarea sau descarcarea proiectului
Deschide un terminal in folderul radacina al proiectului (acolo unde se afla fisierul `pom.xml`).

### 2. Compilarea aplicatiei
Pentru a compila proiectul si a verifica daca exista erori de sintaxa sau dependente lipsa, ruleaza:
mvn clean compile

### 3. Rularea aplicatiei
mvn spring-boot:run

### 3. Rularea testelor automate
mvn test

### 4. Baza de date
Pentru vizualizarea bazei de date se va folosi urmatorul link: http://localhost:8080/h2-console/login.jsp  
Se vor insera datele de logare din `src/main/resources/application.properties`  
Initial, baza de date este populata cu data din fisierul `src/main/resources/data.sql` pentru a avea date de test cand apelam endpointurile din Postman

## Endpoints Principale
Aplicatia expune urmatoarele rute principale sub prefixul `/api/orders`:

* **`POST /orders`** - Crearea unei comenzi noi (valideaza datele primite).
* **`GET /orders/{id}`** - Preluarea unei comenzi dupa ID (returneaza 404 Not Found daca nu exista).
* **`GET /orders`** - Listarea comenzilor cu suport pentru paginare si filtrare optionala dupa status.
* **`PATCH /orders/{id}/status`** - Actualizarea statusului unei comenzi (blocheaza modificarea si returneaza 400 Bad Request daca comanda este deja intr-un status final).
* **`GET /orders/total`** - Calcularea sumei totale pentru comenzile filtrate dupa status.