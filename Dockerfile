# Etapa 1: Construcción del JAR
FROM maven:3.8.6-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Ejecución de la aplicación
# Usa una imagen de base de OpenJDK
FROM openjdk:17

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el JAR generado en la primera etapa de construcción al contenedor
COPY --from=build /app/target/iam-backend-0.0.1-SNAPSHOT.jar app.jar

# Expone el puerto en el que se ejecuta tu aplicación (por ejemplo, 8083)
EXPOSE 8083

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]

