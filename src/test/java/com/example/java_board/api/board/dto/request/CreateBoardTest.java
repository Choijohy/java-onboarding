package com.example.java_board.api.board.dto.request;

import com.example.java_board._test_utils.fixture.BoardFixture;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateBoardTest {
    private static Validator validator;

    // validator 초기화
    // 테스트 클래스에서 처음 1번만 실행(초기화 작업)  cf. @BeforeEach
    @BeforeAll
    static void setUpValidator(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void create_success(){
        // Given
        CreateBoard createBoard = BoardFixture.createBoard();

        // When
        Set<ConstraintViolation<CreateBoard>> violations = validator.validate(createBoard);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    void create_failedWhenNoUserId(){
        //Given
        CreateBoard createBoard = new CreateBoard(null, "title", "content", List.of("tag1","tag2"));

        // When
        Set<ConstraintViolation<CreateBoard>> violations = validator.validate(createBoard);

        // Then
        assertThat(violations).hasSize(1);
        ConstraintViolation<CreateBoard> violation = violations.iterator().next();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("userId");
        assertThat(violation.getMessage()).isEqualTo("널이어서는 안됩니다");
    }

    @Test
    void create_failWhenNoTitle(){
        // Given
        CreateBoard createBoard = new CreateBoard(1L, null, "content", List.of("tag1","tag2"));

        // Then
        Set<ConstraintViolation<CreateBoard>> violations = validator.validate(createBoard);

        // When
        assertThat(violations).hasSize(1);
        ConstraintViolation<CreateBoard> violation = violations.iterator().next();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("title");
        assertThat(violation.getMessage()).isEqualTo("공백일 수 없습니다");
    }

    @Test
    void create_failedWhenInvalidTitle(){
        // Given
        CreateBoard createBoard = new CreateBoard(1L, "", "content", List.of("tag1","tag2"));

        // Then
        Set<ConstraintViolation<CreateBoard>> violations = validator.validate(createBoard);

        // When
        assertThat(violations).hasSize(1);
        ConstraintViolation<CreateBoard> violation = violations.iterator().next();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("title");
        assertThat(violation.getMessage()).isEqualTo("공백일 수 없습니다");
    }

    @Test
    void create_failWhenNoContent(){
        // Given
        CreateBoard createBoard = new CreateBoard(1L, "title", null, List.of("tag1","tag2"));

        // Then
        Set<ConstraintViolation<CreateBoard>> violations = validator.validate(createBoard);

        // When
        assertThat(violations).hasSize(1);
        ConstraintViolation<CreateBoard> violation = violations.iterator().next();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("content");
        assertThat(violation.getMessage()).isEqualTo("널이어서는 안됩니다");
    }

    @Test
    void create_failWhenInvalidContent(){
        // Given
        CreateBoard createBoard = new CreateBoard(1L, "title", " ", List.of("tag1","tag2"));

        // Then
        Set<ConstraintViolation<CreateBoard>> violations = validator.validate(createBoard);

        // When
        assertThat(violations).hasSize(1);
        ConstraintViolation<CreateBoard> violation = violations.iterator().next();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("content");
        assertThat(violation.getMessage()).isEqualTo("공백일 수 없습니다");
    }

    @Test
    void create_successWhenNoTag(){
        // Given
        CreateBoard createBoard = new CreateBoard(1L, "title", "content", null);

        // Then
        Set<ConstraintViolation<CreateBoard>> violations = validator.validate(createBoard);

        // When
        assertThat(violations).isEmpty();
    }

    @Test
    void create_successWhenEmptyTag(){
        // Given
        CreateBoard createBoard = new CreateBoard(1L, "title", "content", List.of());

        // Then
        Set<ConstraintViolation<CreateBoard>> violations = validator.validate(createBoard);

        // When
        assertThat(violations).isEmpty();
    }

}
