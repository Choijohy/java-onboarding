# [JPA] 게시판 'Java_Board'

JPA를 황용하여 기본적인 게시판의 CRUD를 구현합니다.\
부가 기능으로는 댓글, 좋아요 기능을 포함합니다.

## 기술 스택

#### Language

- Language: Java 17(OpenJDK 17)

#### Framework

- Spring Boot 3.3.0

#### Package Manager

- Gradle Kotlin

#### DataBase

- MariaDB 11.4

#### Library

| Name                                               | Version | Description |
|----------------------------------------------------|---------|-------------|
| [**Lombok**](https://pub.dev/packages/flutter_svg) | 1.18.20 | 설명          |
| [**Flyway**](https://documentation.red-gate.com/fd/) | 10.10.0 | DB 버전 관리    |

***

## 실행

```shell
cd ./
docker compose up -d
./gradlew bootRun
```

### release 실행

```shell
java -jar \
  -DSERVER_PORT=8088 \
  -DDATABASE_HOST=localhost \
  -DDATABASE_PORT=53306 \
  -DDATABASE_NAME=on-boarding-java \
  -DDATABASE_USERNAME=wedatalab \
  -DDATABASE_PASSWORD=wedatalab \
  -Dspring.profiles.active=release \
  ./java_board.jar
```

## 기능

### JPA

- User, Board, Comment Entity 구현
- Board 좋아요 기능 구현 `@OneToMany` `@ManyToOne` 사용
- Comment 좋아요 기능 구현 `@ManyToMany` 사용

### Flyway

순서대로 마이그레이션 파일을 작성해 주세요.

- JPA ddl-auto option `validate` 으로 설정
- Init Schema 작성
- Tag 테이블 추가
- Tag init 데이터 추가 ("java study", "python study")
- Board 에 Tag 릴레이션 추가 (@ManyToMany)
- Tag 이름 변경 마이그레이션 파일 작성
    - java study -> JAVA_STUDY
    - python study -> PYTHON_STUDY

```java
@Entity
class Tag {
    private Long id;
    private String name;
}
```

### QueryDSL

- Board 페이지 조회 구현
- Board 검색 구현 (content, tag name, user name)