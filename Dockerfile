# Temel imaj
FROM openjdk:17-jdk-slim

# Uygulama için çalışma dizini oluştur
WORKDIR /app

# build klasöründeki jar dosyasını konteynıra kopyala
COPY target/LottoFun-0.0.1-SNAPSHOT.jar app.jar

# Uygulamanın çalıştırılacağı komut
ENTRYPOINT ["java", "-jar", "app.jar"]