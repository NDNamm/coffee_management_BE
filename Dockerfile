# ---------- Stage 1: Build with Maven ----------
FROM maven:3.9.6-amazoncorretto-17 AS build

# Copy tất cả source code vào image
WORKDIR /app
COPY pom.xml .
COPY src ./src



# Tạo file jar (skip test nếu muốn build nhanh hơn)
RUN mvn clean package -DskipTests

# ---------- Stage 2: Run app ----------
FROM amazoncorretto:17.0.15


# Copy file jar từ stage build
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Mở port (tuỳ vào Spring Boot app)
EXPOSE 8080

# Lệnh chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]
