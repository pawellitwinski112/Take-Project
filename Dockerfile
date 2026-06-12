# ETAP 1: Budowanie (Kompilacja)
FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app

# Kopiujemy plik konfiguracyjny Mavena
COPY pom.xml .
# Pobieramy zależności (Dzięki temu Docker je scache'uje i kolejne budowania będą szybsze)
RUN mvn dependency:go-offline

# Kopiujemy cały kod źródłowy
COPY src ./src
# Budujemy aplikację do pliku .jar (pomijamy testy dla przyspieszenia)
RUN mvn clean package -DskipTests

# ETAP 2: Uruchomienie (Lekkie środowisko)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Kopiujemy gotowy, skompilowany plik .jar z ETAPU 1 (z kontenera o nazwie 'builder')
COPY --from=builder /app/target/*.jar app.jar

# Informujemy, że aplikacja będzie nasłuchiwać na porcie 8080
EXPOSE 8080

# Komenda startowa
ENTRYPOINT ["java", "-jar", "app.jar"]