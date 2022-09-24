## Special Team BackEnd

### 

- Spring Boot 2.7.4
- Spring Data MongoDB

### 시작하기

> 도커가 설치되어있어야합니다.

1. docker-compose 실행

```bash

cd special-docker-compose

docker-compose up -d

```

2. 빌드 (IDE 사용 시 SpecialApplication을 실행시켜주시면 됩니다. ) 

```
./gradlew clean build -x test
```

3. 실행

```
java -jar build/libs/special-0.0.1-SNAPSHOT.jar
```

