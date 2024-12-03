package com.example.java_board.api.board;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

import com.example.java_board._test_utils.ControllerTest;
import com.example.java_board._test_utils.fixture.CommentFixture;
import com.example.java_board.api.board.dto.request.CreateBoard;
import com.example.java_board._test_utils.fixture.BoardFixture;
import com.example.java_board.api.board.dto.request.CreateComment;
import com.example.java_board.api.board.dto.request.UpdateBoard;
import com.example.java_board.api.board.dto.request.UpdateComment;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@WebMvcTest(BoardController.class)
class BoardControllerTest extends ControllerTest {


    @MockBean
    BoardService boardService;

    @MockBean
    CommentService commentService;

    @Test
    void createBoard() throws Exception {
        CreateBoard createBoard = BoardFixture.createBoard();

        this.mockMvc.perform(post("/board")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBoard))
                )
                .andExpect(status().is2xxSuccessful())
                .andDo(document("create-board"));
    }

    @Test
    void updateBoard() throws Exception {
        long boardId = 1L;
        UpdateBoard updateBoard = BoardFixture.updateBoard();

        this.mockMvc.perform(put("/board/{boardId}", boardId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateBoard))
                )
                .andExpect(status().is2xxSuccessful())
                .andDo(document("update-board"));
    }


    @Test
    void deleteBoard() throws Exception {
        long boardId = 1L;

        this.mockMvc.perform(delete("/board/{boardId}", boardId))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("delete-board"));
    }

    @Test
    void like() throws Exception {
        long boardId = 1L;
        long userId = 1L;

        this.mockMvc.perform(post("/board/{boardId}/like", boardId)
                        .param("userId", String.valueOf(userId))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andDo(document("board-like"));
    }

    @Test
    void createComment() throws Exception {
        long boardId = 1L;
        CreateComment createComment = CommentFixture.createComment();

        this.mockMvc.perform(post("/board/{boardId}/comment", boardId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createComment))
                )
                .andExpect(status().is2xxSuccessful())
                .andDo(document("create-comment"));
    }

    @Test
    void updateComment() throws Exception {
        long boardId = 1L;
        long commentId = 1L;
        UpdateComment updateComment = CommentFixture.updateComment();

        this.mockMvc.perform(put("/board/{boardId}/comment/{commentId}", boardId, commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateComment))
                )
                .andExpect(status().is2xxSuccessful())
                .andDo(document("update-comment"));
    }

    @Test
    void deleteComment() throws Exception {
        long boardId = 1L;
        long commentId = 1L;

        this.mockMvc.perform(delete("/board/{boardId}/comment/{commentId}", boardId, commentId))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("delete-comment"));
    }

    @Test
    void commentLike() throws Exception {
        long boardId = 1L;
        long commentId = 1L;
        long userId = 1L;

        this.mockMvc.perform(post("/board/{boardId}/comment/{commentId}/like", boardId, commentId)
                        .param("userId", String.valueOf(userId))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andDo(document("board-like"));
    }
}