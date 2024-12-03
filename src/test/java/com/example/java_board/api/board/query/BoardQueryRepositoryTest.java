package com.example.java_board.api.board.query;

import com.example.java_board._core.config.QueryDSLConfig;
import com.example.java_board._test_utils.fixture.BoardFixture;
import com.example.java_board._test_utils.fixture.PageableFixture;
import com.example.java_board._test_utils.fixture.TagFixture;
import com.example.java_board._test_utils.fixture.UserFixture;
import com.example.java_board.api.board.dto.request.SearchCondition;
import com.example.java_board.api.board.dto.request.SearchType;
import com.example.java_board.api.board.dto.request.SortCondition;
import com.example.java_board.api.board.dto.request.SortType;
import com.example.java_board.api.board.dto.response.SelectBoard;
import com.example.java_board.api.board.exception.NotFoundBoardException;
import com.example.java_board.repository.board.Board;
import com.example.java_board.repository.board.BoardRepository;
import com.example.java_board.repository.tag.TagRepository;
import com.example.java_board.repository.user.User;
import com.example.java_board.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@DataJpaTest // JPA 관련 테스트 설정만 로드
@Import({BoardQueryRepository.class, QueryDSLConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BoardQueryRepositoryTest {

    @Autowired
    BoardQueryRepository boardQueryRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    TagRepository tagRepository;

    @AfterEach
    void tearDown() {
        boardRepository.deleteAll();
        tagRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Nested
    class findBoardById{

        @Transactional
        @Test
        void success(){
            // Given
            User user = userRepository.save(UserFixture.user());
            Board board = boardRepository.save(BoardFixture.board("title","content",user));

            // When
            SelectBoard result = boardQueryRepository.getBoardById(board.getId());

            // Then
            assertThat(result.id()).isEqualTo(board.getId());
            assertThat(result.title()).isEqualTo(board.getTitle());
            assertThat(result.content()).isEqualTo(board.getContent());
        }

        @Transactional
        @Test
        void fail_boardNotFoundException(){
            // Given
            User user = userRepository.save(UserFixture.user());
            Board board = boardRepository.save(BoardFixture.board("title","content",user));

            // When
            // Then
            assertThrows(NotFoundBoardException.class, ()->{
                boardQueryRepository.getBoardById(board.getId()+1L);
            });
        }
    }

    @Nested
    class findPage{

        @Transactional
        @Test
        void success(){
            // Given
            User user = userRepository.save(UserFixture.user());
            Board board1 = boardRepository.save(BoardFixture.board("title1","1",user));
            Board board2 = boardRepository.save(BoardFixture.board("title2","2",user));
            // pageable sorting은 적용 안되므로 무시(디폴트: id desc)
            Pageable pageable = PageableFixture.pageable(0,10,Sort.by("id").descending());

            // When
            Page<SelectBoard> result = boardQueryRepository.findPage(null, null, pageable);

            // Then
            assertThat(result.getTotalElements()).isEqualTo(2);
            List<SelectBoard> content = result.getContent();
            assertThat(content).hasSize(2);

            assertThat(content.get(0).title()).isEqualTo(board2.getTitle());
            assertThat(content.get(0).content()).isEqualTo(board2.getContent());
            assertThat(content.get(1).title()).isEqualTo(board1.getTitle());
            assertThat(content.get(1).content()).isEqualTo(board1.getContent());
        }

        @Transactional
        @Test
        void success_findBySearchCondition(){
            // Given
            User user = userRepository.save(UserFixture.user());
            Board board1 = boardRepository.save(BoardFixture.board("title1","1",user));
            Board board2 = boardRepository.save(BoardFixture.board("title2","2",user));
            // pageable sorting은 적용 안되므로 무시(디폴트: id desc)
            Pageable pageable = PageableFixture.pageable(0,10,Sort.by("id").descending());
            SearchCondition searchCondition = new SearchCondition(SearchType.TITLE, "1");

            // When
            Page<SelectBoard> result = boardQueryRepository.findPage(searchCondition, null, pageable);


            // Then
            assertThat(result.getTotalElements()).isEqualTo(1);
            List<SelectBoard> content = result.getContent();
            assertThat(content).hasSize(1);

            assertThat(content.get(0).title()).isEqualTo(board1.getTitle());
            assertThat(content.get(0).content()).isEqualTo(board1.getContent());
        }

        @Transactional
        @Test
        void success_findBySortCondition(){
            // Given
            User user = userRepository.save(UserFixture.user());
            Board board1 = boardRepository.save(BoardFixture.board("title1","1",user));
            Board board2 = boardRepository.save(BoardFixture.board("title2","2",user));
            // pageable sorting은 적용 안되므로 무시(디폴트: id desc)
            Pageable pageable = PageableFixture.pageable(0,10,Sort.by("id").descending());
            SortCondition sortCondition = new SortCondition(Collections.singletonList(SortType.OLD)); // ???

            // When
            Page<SelectBoard> result = boardQueryRepository.findPage(null, sortCondition, pageable);


            // Then
            assertThat(result.getTotalElements()).isEqualTo(2);
            List<SelectBoard> content = result.getContent();
            assertThat(content).hasSize(2);

            assertThat(content.get(0).title()).isEqualTo(board1.getTitle());
            assertThat(content.get(0).content()).isEqualTo(board1.getContent());
            assertThat(content.get(1).title()).isEqualTo(board2.getTitle());
            assertThat(content.get(1).content()).isEqualTo(board2.getContent());
        }

    }


}
