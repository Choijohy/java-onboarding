# QueryDSL

## 배경

동적 쿼리 툴로서, Hibernate Query Language(HQL)를 동적으로 사용할 수 있도록 도와줍니다.

JPQL을 사용할 수도 있지만, type-safe 하게 사용할 수 있도록 도와줍니다.

## 설정

### build.gradle 추가
spring boot 3.3.0 기준, querydsl 5.0.0 버전 사용
```kotlin
implementation ("com.querydsl:querydsl-jpa:5.0.0:jakarta")
annotationProcessor("com.querydsl:querydsl-apt:5.0.0:jakarta")
annotationProcessor("jakarta.annotation:jakarta.annotation-api")
annotationProcessor("jakarta.persistence:jakarta.persistence-api")
```

### QClass 생성
1. compileJava를 통해서 QClass 생성 가능
    ```shell
    ./gradlew compileJava
    ```
2. clean을 통해서 QClass 제거 가능
    ```shell
    ./gradlew clean
    ```
**QClass는 build/generated/sources/annotationProcessor 아래에 생성됩니다.**
> IntelliJ IDEA 기준, Gradle 툴 창에서 Tasks-other-compileJava 및 Tasks-build-clean 으로 실행 가능

## 사용
자세한 사용법은 찾아보시면 있습니다. 다만, 이 프로젝트에서는 다음과 같이 사용합니다.
- 각 라우터(기능)마다, QueryDSL용 Repository 클래스를 생성해서 사용합니다. (ex. [BoardQueryRepository](../src/main/java/com/example/java_board/api/board/query/BoardQueryRepository.java))
- 공통 쿼리의 경우, 특히 join 및 where 관련 절을 묶어서 사용합니다.
- select절의 경우, 항상 DTO로 변환해서 사용합니다.

