//package com.example.java_board.api.board.query;
//
//import com.example.java_board._core.config.QueryDSLConfig;
//import com.example.java_board._test_utils.fixture.*;
//import com.example.java_board.api.board.dto.request.SearchCondition;
//import com.example.java_board.api.board.dto.response.SelectBoard;
//import com.example.java_board.repository.board.Board;
//import com.example.java_board.repository.board.BoardRepository;
//import com.example.java_board.repository.board.BoardTag;
//import com.example.java_board.repository.tag.Tag;
//import com.example.java_board.repository.tag.TagRepository;
//import com.example.java_board.repository.user.User;
//import com.example.java_board.repository.user.UserRepository;
//import jakarta.transaction.Transactional;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.context.annotation.Import;
//import java.util.*;
//import static org.assertj.core.api.Assertions.assertThat;
//
///*
//1. 쿼리 레파지토리 클래스 정의 방식 차이
//public class BoardQueryRepositoryImpl implements BoardQueryRepositoryCustom {}
//public class BoardQueryRepository {}  - 현재
//
//*/
//
//// #TODO 테스트 컨테이너 메소드마다 뜨는거 바꾸기
//// fixture 분리 기준 - boardTag이런건 fixture 따로 클래스 만들지 말고,
//
//@DataJpaTest
//@Import({BoardQueryRepository_origin.class, QueryDSLConfig.class})
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // main의 application 속성이 아닌 테스트 설정을 참고하도록 함
//public class BoardQueryRepositoryTest {
//
//    @Autowired
//    BoardQueryRepository_origin boardQueryRepository;
//
//    @Autowired
//    UserRepository userRepository;
//
//    @Autowired
//    BoardRepository boardRepository;
//
//    @Autowired
//    TagRepository tagRepository;
//
//
//    @AfterEach
//    void tearDown() {
//        // entityManager.clear(); 영속성 컨텍스트를 비우는 것일 뿐, 데이터베이스에 저장된 데이터를 삭제하거나 롤백하지는 않음
////
////        boardRepository.deleteAllInBatch();
////        tagRepository.deleteAllInBatch();
////        userRepository.deleteAllInBatch();
//
//        boardRepository.deleteAll();
//        tagRepository.deleteAll();
//        userRepository.deleteAll();
//
//    }
//
//
//
//    @DisplayName("findPage")
//    @Nested
//    class findPage{
//        @Transactional
//        @Test
//        void success(){
//            // Given
//            // users
//            User user = userRepository.save(UserFixture.user("user1@gmail.com"));
//
//            // boards
//            Board board1  = boardRepository.save(BoardFixture.board("title1","content1",user));
//            Board board2  = boardRepository.save(BoardFixture.board("title2","content2",user));
//
//            // pageable request
//            Pageable pageable = PageableFixture.pageable(0,5,Sort.by("id").descending());
//
//            // When
//            Page<SelectBoard> result = boardQueryRepository.findPage(pageable);
//
//            // Then
//            // 전체 검증
//            assertThat(result.getTotalElements()).isEqualTo(2);
//            List<SelectBoard> content = result.getContent();
//            assertThat(content).hasSize(2);
//
//            // 개별 검증(id순 내림차순)
//            assertThat(content.get(0).title()).isEqualTo(board2.getTitle());
//            assertThat(content.get(0).content()).isEqualTo(board2.getContent());
//
//            assertThat(content.get(1).title()).isEqualTo(board1.getTitle());
//            assertThat(content.get(1).content()).isEqualTo(board1.getContent());
//        }
//
//////        @Test
//////        void fail_NoPageData(){
//////            // Given
//////            Pageable pageable = PageableFixture.pageable(5,5,Sort.by("id").descending());
//////
//////            // When
//////            Page<SelectBoard> result = boardQueryRepository.findPage(pageable);
//////
//////            // Then
//////            // 전체 검증
//////            assertThat(result.getTotalElements()).isEqualTo(0);
//////        }
//    }
//
//    @DisplayName("findBoardById")
//    @Nested
//    class findBoardById{
//
//        @Test
//        void success(){
//            // Given
//            User user = userRepository.save(UserFixture.user("user1@gmail.com"));
//
//            // boards
//            Board board1  = boardRepository.save(BoardFixture.board("title1","content1",user));
//            Board board2  = boardRepository.save(BoardFixture.board("title2","content2",user));
//
//            // When
//            // id값을 지정해주면 안됨(boardID = 1L;) -> db에서 값을 지웠더라도 id값이 누적되어 올라가므로
//            Optional<SelectBoard> result = boardQueryRepository.findBoardById(board1.getId());
//
//            // Then
//            assertThat(result).isPresent();
//            assertThat(result.get()).isNotNull();
//            assertThat(result.get().title()).isEqualTo(board1.getTitle());
//            assertThat(result.get().content()).isEqualTo(board1.getContent());
//        }
//
//        @Test
//        void fail_notFoundBoard(){
//            // Given
//            // user
//            User user = userRepository.save(UserFixture.user("user1@gmail.com"));
//            // board
//            Board board1  = boardRepository.save(BoardFixture.board("title1","content1",user));
//
//            long boardId = 10L;
//
//            // When
//            Optional<SelectBoard> result = boardQueryRepository.findBoardById(boardId);
//
//            // Then
//            assertThat(result).isEmpty();
//        }
//    }
//
//    @DisplayName("findPageByCondition")
//    @Nested
//    class findPageByCondition{
//        @Test
//        void success_ByContent1(){
//            // Given
//            User user = userRepository.save(UserFixture.user("user1@gmail.com"));
//
//            // boards
//            Board board1  = boardRepository.save(BoardFixture.board("aaa","111",user));
//            Board board2  = boardRepository.save(BoardFixture.board("bbb","222",user));
//
//            SearchCondition searchCondition = SearchConditionFixture.contentCondition("2");
//            Pageable pageable = PageableFixture.defaultPageable();
//
//            // When
//            Page<SelectBoard> result = boardQueryRepository.findPageByCondition(searchCondition, pageable);
//
//            // Then
//            assertThat(result).isNotNull();
//            assertThat(result.getTotalElements()).isEqualTo(1);
//            List<SelectBoard> content = result.getContent();
//            assertThat(content).hasSize(1);
//            assertThat(content.get(0).content()).contains("2");
//        }
//
//        @Test
//        void success_byContent2(){
//            // Given
//            User user = userRepository.save(UserFixture.user("user1@gmail.com"));
//
//            // boards
//            Board board1  = boardRepository.save(BoardFixture.board("aaa","ccc",user));
//            Board board2  = boardRepository.save(BoardFixture.board("bbb","ccc123",user));
//
//            String keyword = "cc";
//            SearchCondition searchCondition = SearchConditionFixture.contentCondition(keyword);
//            Pageable pageable = PageableFixture.defaultPageable();
//
//            // When
//            Page<SelectBoard> result = boardQueryRepository.findPageByCondition(searchCondition, pageable);
//
//            // Then
//            assertThat(result).isNotNull();
//            assertThat(result.getTotalElements()).isEqualTo(2);
//            List<SelectBoard> content = result.getContent();
//            assertThat(content).hasSize(2);
//            for (int i = 0; i < result.getTotalElements(); i++){
//                assertThat(content.get(i).content()).contains(keyword);
//            }
//        }
//
//        @Test
//        void success_byTag1(){
//            // Given
//            // user
//            User user = userRepository.save(UserFixture.user("user1@gmail.com"));
//
//            // boards
//            Board board1  = boardRepository.save(BoardFixture.board("aaa","ccc", user));
//            Board board2  = boardRepository.save(BoardFixture.board("bbb","ccc123", user));
//
//            // Tags
//            List<Tag> tags = new ArrayList<>();
//            tags.add(TagFixture.tag("java"));
//            tags.add(TagFixture.tag("python"));
//            tags.add(TagFixture.tag("c++"));
//            tagRepository.saveAll(tags);
//
//            BoardTag boardTag1 = TagFixture.boardTag(board1, tags.get(0)); // java
//            BoardTag boardTag1_1 = TagFixture.boardTag(board1, tags.get(1)); // python
//
//            Set<BoardTag> boardTagSet = new HashSet<>(List.of(boardTag1,boardTag1_1));
//            board1.updateTags(boardTagSet);
//
//            String keyword = "java";
//            SearchCondition searchCondition = SearchConditionFixture.tagCondition(keyword);
//            Pageable pageable = PageableFixture.defaultPageable();
//
//            // When
//            Page<SelectBoard> result = boardQueryRepository.findPageByCondition(searchCondition, pageable);
//
//            // Then
//            assertThat(result).isNotNull();
//            assertThat(result.getTotalElements()).isEqualTo(1);
//            List<SelectBoard> content = result.getContent();
//            assertThat(content).hasSize(1);
//            assertThat(content.get(0).tags()).contains(keyword);
//        }
//
//        @Test
//        void success_byTag2(){
//            // Given
//            // user
//            User user = userRepository.save(UserFixture.user("user1@gmail.com"));
//
//            // boards
//            Board board1  = boardRepository.save(BoardFixture.board("aaa","ccc", user));
//
//            // tag
//            Tag tag = TagFixture.tag("java");
//            tagRepository.save(tag);
//
//            BoardTag boardTag1 = TagFixture.boardTag(board1, tag); // java
//
//            Set<BoardTag> boardTagSet = new HashSet<>(List.of(boardTag1));
//            board1.updateTags(boardTagSet);
//
//            SearchCondition searchCondition = SearchConditionFixture.tagCondition("no tag");
//            Pageable pageable = PageableFixture.defaultPageable();
//
//            // When
//            Page<SelectBoard> result = boardQueryRepository.findPageByCondition(searchCondition, pageable);
//
//            // Then
//            assertThat(result).isNotNull();
//            assertThat(result.getTotalElements()).isEqualTo(0);
//            List<SelectBoard> content = result.getContent();
//            assertThat(content).hasSize(0);
//        }
//
//        @Test
//        void fail_notFoundByContent(){
//            // Given
//            // user
//            User user = userRepository.save(UserFixture.user("user1@gmail.com"));
//
//            // boards
//            Board board1  = boardRepository.save(BoardFixture.board("aaa","ccc", user));
//
//            SearchCondition searchCondition = SearchConditionFixture.contentCondition("empty");
//            Pageable pageable = PageableFixture.defaultPageable();
//
//            // When
//            Page<SelectBoard> result = boardQueryRepository.findPageByCondition(searchCondition, pageable);
//
//            // Then
//            assertThat(result).isNotNull();
//            assertThat(result.getTotalElements()).isEqualTo(0);
//            List<SelectBoard> content = result.getContent();
//            assertThat(content).hasSize(0);
//        }
//    }
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//}
//
