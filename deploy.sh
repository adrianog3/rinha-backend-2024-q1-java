docker-compose down -v

./mvnw package -Dmaven.test.skip=true

docker build -t rinha-backend-2024-q1:latest -f docker/Dockerfile.jvm .

docker-compose up -d
