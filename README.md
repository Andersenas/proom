# Build
1. Install latest [JDK](https://jdk.java.net/22/)
2. Install latest [maven](https://maven.apache.org/download.cgi)
3. `mvn clean install`
4. `docker build -f .\server.dockerfile . -t proom-server:latest`
5. ` docker run -p 8080:8080 proom-server:latest --rm`