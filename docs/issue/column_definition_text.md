# `columnDefinition = "TEXT"`

### 문제

Board Entity 정의 중 `content` 에 관핸서 해당 칼럼을 `Text` 로 지정하려고 하였으나 아래의 문제가 발생하였습니다.

- Mariadb 에서는 `TEXT` 타입을 지원.
- H2 Database 에서는 `TEXT` 타입을 지원하지 않음.

Board:

```java
...

@Column(nullable = false, columnDefinition = "TEXT")
private String content;

//create table "board" (
//        "id" bigint generated by default as identity,
//        "user_id" bigint,
//        "content" "TEXT" not null,
//        "title" varchar(255) not null,
//        primary key ("id")
//)" via JDBC [Domain "TEXT" not found;]    <-- here

...
```

### 해결 방법

해당 문제를 해결하기 위해 `jakarta.persistence.Lob` 어노테이션을 사용하였습니다.

```java
...

@Lob
@Column(nullable = false)
private String content;

...
```

Javadoc:
> Specifies that a persistent property or field should be persisted as a large object to a
> database-supported large object type.
> Portable applications should use the Lob annotation when mapping to a database Lob type. The Lob
> annotation may be used in conjunction with the Basic annotation or the ElementCollection
> annotation
> when the element collection value is of basic type. A Lob may be either a binary or character
> type.
> The Lob type is inferred from the type of the persistent field or property, and except for string
> and character-based types defaults to Blob.

> Specifies that a persistent property or field should be persisted as a large object to a
> database-supported large object type.\
> 영구 속성 또는 필드가 데이터베이스에서 지원하는 큰 객체 유형에 큰 객체로 유지되도록 지정합니다.

> A Lob may be either a binary or character type.\
> Lob은 이진 또는 문자 유형일 수 있습니다.

실제 데이터베이스 칼럼의 타입을 확인해 보았습니다.

H2 Database:

```text
create table "board" (
    "id" bigint generated by ådefault as identity,
    "user_id" bigint,
    "title" varchar(255) not null,
    "content" clob not null,
    primary key ("id")
)
```

MariaDB:

```text
create table `board` (
    `id` bigint not null auto_increment,
    `user_id` bigint,
    `title` varchar(255) not null,
    `content` tinytext not null,
    primary key (`id`)
) engine=InnoDB
```

H2 Database에서는 `CLOB`, MariaDB 에서는 `TINYTEXT`로 인식되는 것을 확인할 수 있습니다. 하지만 몇가지 아쉬운 부분이 있습니다.

- `TINYTEXT`는 조금 아쉬울 수 있습니다. `TEXT`, `LONGTEXT` 를 사용하고 싶을 수 있습니다.
- 운영디비가 메인이 되어야할 것 같습니다.
    - 예를 들어 `columnDefinition = TEXT` 라고 지정을 하고, Test시 사용하는 H2 Database에서 이를 다른
      타입을 바꾸는 것이 좋아 보입니다.
- `@Lob` 은 `CLOB` 으로 자동할당 됩니다. `BLOB` 등의 타입이 필요한 경우 제약이 발생합니다.

~~따라서 저는 `columnDefinition` 옵션을 통해 메인 데이터베이스에 맞는 칼럼 속성을 명시하고, 테스트를 수행하는 H2 Database에서 이를 회피해가면 좋을 것
같습니다.~~

안되는 것 같아서 다른 방법을 찾아보겠습니다. 첫번째로 `columnDefinition` 을 핸들링할 수 없었습니다. 두번째로는 하다보니 `columnDefinition` 사용이
옳바른가? 입니다.
`columnDefinition`을 통해 작성한 칼럼 타입은 별도의 검사없이 String 타입으로 지정이 가능합니다. 만약 `columnDefinition="wrong"`으로
작성해도 동작이 됩니다. 위 두가지 이유로 `@Lob`을 다시 활용해 보겠습니다.

`@Lob`을 사용하는 경우 H2 Database에서는 `CLOB` 타입으로 알맞게 정의됩니다. 하지만 Mariadb 에서는 `TINYTEXT` 인 것이
문제였는데요, `Dialect`을 정의해서 변경할 수 있습니다.

### CustomMariaDBDialect 정의

`@Lob` 어노테이션이 활성화된 경우 Mariadb 에서 `TINYTEXT`가 아닌 `TEXT`가 되길 원합니다. ddltype을 재정의 해줍니다.

```java
public class CustomMariaDBDialect extends MariaDBDialect {

    public CustomMariaDBDialect() {
        super();
    }

    @Override
    protected void registerColumnTypes(TypeContributions typeContributions, ServiceRegistry serviceRegistry) {
        super.registerColumnTypes(typeContributions, serviceRegistry);
        final DdlTypeRegistry ddlTypeRegistry = typeContributions.getTypeConfiguration().getDdlTypeRegistry();

        ddlTypeRegistry.addDescriptor(new DdlTypeImpl(Types.CLOB, "TEXT", this));
        ddlTypeRegistry.addDescriptor(new DdlTypeImpl(Types.BLOB, "BLOB", this));
    }

}

// Board.java
...

@Lob
@Column(nullable = false)
private String content;

...
```

아래 결과를 통해 `Types.CLOB` 인 경우 `TEXT` Type 칼럼을 갖을 수 있습니다. (BLOB은 `byte[]` 형식)

```text
create table `board` (
    `id` bigint not null auto_increment,
    `user_id` bigint,
    `title` varchar(255) not null,
    `content` TEXT not null,
    primary key (`id`)
) engine=InnoDB
```

### `content` 칼럼은 꼭 LOB 타입이어야 할까?

문제는 해결했지만 다시 돌아와서 `content` 필드가 `TEXT` 타입일 필요가 있냐는 궁금증이 생겼습니다. `VARCHAR`의 길이를 늘려서 사용해도 될거 같거든요.

**VARCHAR**

- 장점:
    - 성능: VARCHAR는 일반적으로 CLOB보다 성능이 좋습니다. 특히 짧은 문자열 데이터를 처리할 때 더 빠르게 작동합니다.
    - 메모리 사용: VARCHAR는 필요한 메모리만 사용하므로, 데이터베이스의 메모리 사용을 최적화할 수 있습니다.
    - 인덱싱: VARCHAR 필드는 인덱스를 더 효율적으로 사용할 수 있어 검색 성능이 향상될 수 있습니다.
- 단점:
    - 길이 제한: VARCHAR의 길이는 데이터베이스에 따라 제한됩니다. 예를 들어, MySQL에서 VARCHAR의 최대 길이는 65,535바이트입니다.

**CLOB**

- 장점:
    - 대용량 데이터 저장: CLOB은 매우 큰 텍스트 데이터를 저장할 수 있습니다(최대 4GB).
    - 유연성: 데이터의 크기가 매우 가변적일 때 유연하게 사용할 수 있습니다.
- 단점:
    - 성능: CLOB은 VARCHAR보다 성능이 낮을 수 있으며, 특히 작은 데이터를 처리할 때 비효율적일 수 있습니다.
    - 복잡성: CLOB 데이터를 처리하는 데 더 많은 메모리와 CPU 리소스를 사용할 수 있습니다.

## Unnecessary stubbings detected.

Mocking을 활용한 테스트 도중 발생한 에러 메세지 입니다.

```text
Unnecessary stubbings detected.
Clean & maintainable test code requires zero unnecessary code.
Following stubbings are unnecessary (click to navigate to relevant line of code):
```

### 문제

boardService.like 메서드는 아래와 같습니다.

```java

@Transactional
public void like(long id, long userId) {
    Board board = boardRepository.findById(id).orElseThrow(NotFoundBoardException::new);
    User user = userRepository.findById(userId).orElseThrow(NotFoundUserException::new);
    board.like(user);
    boardRepository.save(board);
}
```

문제가 발생한 테스트 코드는 `NotFoundBoardException` 을 확인하기 위한 테스트 코드였는데요, `NotFoundBoardException` 가
발생한다면 `like` 메서드의 첫번째 줄에서 메서드가 종료됩니다. 아래는 테스트 코드 입니다.

```java

@Test
void like_NotFoundBoardException() {
    // given
    long userId = 1L;
    long boardId = 1L;
    User user = UserFixture.user();
    Board board = spy(BoardFixture.board(user));
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(boardRepository.findById(boardId)).thenReturn(Optional.empty());

    // when & then
    assertThrows(NotFoundBoardException.class, () -> boardService.like(userId, boardId));

    verify(boardRepository).findById(boardId);
    verify(userRepository, never()).findById(userId);
    verify(board, never()).like(user);
    verify(boardRepository, never()).save(board);
}
```

테스트 코드를 보면 `boardRepository.findById` 와 함께 `userRepository.findById` 도 모킹을 하는데요, 첫번째 줄에서 메서드가 종료되기
떄문에 **`userRepository.findById` 에 대한 모킹은 불필요하다는 에러가 발생한 것**이었습니다.

### 해결 방법

1. 불필요한 Mocking을 제거한다.
2. 불필요한 Mocking인 것을 인지하고 있다고 명시한다.

저는 `userRepository.findById` 를 호출하지 않았다는 것을 test code 에 명시하고 싶었습니다. 따라서 2번 방법으로 해결하려고 합니다.

```java
// AS-IS
when(userRepository.findById(userId)).thenReturn(Optional.of(user));
// TO-BE
lenient().when(userRepository.findById(userId)).thenReturn(Optional.of(user));
```

수정된 전체 코드

```java
@Test
void like_NotFoundBoardException() {
    // given
    long userId = 1L;
    long boardId = 1L;
    User user = UserFixture.user();
    Board board = spy(BoardFixture.board(user));
    lenient().when(userRepository.findById(userId)).thenReturn(Optional.of(user)); // exception 발생으로 인해 이 메소드는 실행되지 않는다.
    when(boardRepository.findById(boardId)).thenReturn(Optional.empty());

    // when & then
    assertThrows(NotFoundBoardException.class, () -> boardService.like(userId, boardId));

    verify(boardRepository).findById(boardId);
    verify(userRepository, never()).findById(userId);
    verify(board, never()).like(user);
    verify(boardRepository, never()).save(board);
}
```