package com.example.java_board;

import com.example.java_board.repository.board.BoardRepository;

import com.example.java_board.api.board.BoardService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class JavaBoardApplicationTests {

    @Autowired
    private BoardRepository boardRepository;
    private BoardService boardService;


//    @Test
//    void testGetAllBoardList() {
//        List<Board> all = this.boardRepository.findAll();
//        assertEquals(2, all.size());
//
//        Board board = all.get(0);
//        assertEquals("이것은 테스트 내용입니다.", board.getContent());
//    }
//
//    @Test
//    void testGetBoardById() {
//        // 해당하는 글이 존재하는 경우
//        Optional<Board> result1 = this.boardRepository.findById(1);
//        assertTrue(result1.isPresent());
//        Board board = result1.get();
//        assertEquals("이것은 테스트 내용입니다.", board.getContent());
//
//        // 해당하는 글이 없는 경우
//        Optional<Board> result2 = this.boardRepository.findById(3);
//        assertTrue(result2.isEmpty(), "Expected result to be empty");
//
//    }

}
