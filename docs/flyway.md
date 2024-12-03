# Flyway

## 배경

데이터베이스 마이그레이션 툴로서, DB 변경사항을 추적하고, 업데이트나 롤백을 쉽게 할 수 있도록 도와줍니다 .

JPA Hibernate ddl-auto 옵션을 활용할 수 있지만, 변경사항(버전) 관리가 되지 않기에 소스코드 상에서 관리해줍니다.

## 설정

### build.gradle 추가
```kotlin
implementation("org.flywaydb:flyway-core")
implementation("org.flywaydb:flyway-mysql")
```

### sql 작성
/resources/db/migration 아래에 .sql 파일 작성

파일 작성 시에는, 관련 파일(Entity 코드)와 sql 쿼리문이 모두 변경된 채로 동작 해야합니다.

(예시) V2.0__create_table.sql
```sql
create table tag
(
    id       bigint auto_increment
        primary key,
    name varchar(255)
);
```

## 네이밍 규칙
**{prefix}{version}__{description}.sql** 방식으로 작성되어야 합니다.

(예시) V2.0__create_table.sql

- **prefix**: 동작 방식
  - **V**(Version): 버전 마이그레이션
  - **U**(Undo): 실행 취소
  - **R**(Repeatable): 반복 가능한 마이그레이션
    - {version} 불필요(버전과 상관없이 반복되기 때문)
- **version**: 버전 번호
  - 낮은 숫자부터 확인함 (만약 이미 더 높은 버전의 상태라면, 낮은 버전 실행하지 않음)
  - 정수 외의 소수 및 날짜도 가능
- **separator**: 구분자
  - '__'로, '_'가 두개 임에 주의(하나일 경우, 스킵됨)
- **description**: 설명
  - 해당 버전 설명 작성
  - '_'로 이어서 작성 가능 