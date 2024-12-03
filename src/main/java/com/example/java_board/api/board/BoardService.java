package com.example.java_board.api.board;

import com.example.java_board.api.board.dto.request.*;
import com.example.java_board.api.board.dto.response.SelectBoard;
import com.example.java_board.api.board.exception.NotFoundBoardException;
import com.example.java_board.api.board.mapper.BoardMapper;
import com.example.java_board.api.board.query.BoardQueryRepository;
import com.example.java_board.api.user.exception.NotFoundUserException;
import com.example.java_board.repository.board.Board;
import com.example.java_board.repository.board.BoardRepository;
import com.example.java_board.repository.board.BoardTag;
import com.example.java_board.repository.tag.Tag;
import com.example.java_board.repository.tag.TagRepository;
import com.example.java_board.repository.user.User;
import com.example.java_board.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final TagRepository tagRepository;
    private final BoardMapper boardMapper;
    private final BoardQueryRepository boardQueryRepository;

    @Transactional
    public void create(CreateBoard createBoard) {
        Board board = boardMapper.toEntity(createBoard);
        Set<BoardTag> tags = createBoard.tags().stream()
                .map(this::findOrCreateTag)
                .map(tag -> new BoardTag(board, tag))
                .collect(Collectors.toSet());

        board.updateTags(tags);
        boardRepository.save(board);
    }

    @Transactional
    public void update(long id, UpdateBoard updateBoard) {
        Board board = boardRepository.findById(id).orElseThrow(NotFoundBoardException::new);
        Set<BoardTag> tags = updateBoard.tags().stream()
                .map(this::findOrCreateTag)
                .map(tag -> new BoardTag(board, tag))
                .collect(Collectors.toSet());

        board.update(updateBoard);
        board.updateTags(tags);
        boardRepository.save(board);
    }

    @Transactional
    public void delete(long id) {
        if (!boardRepository.existsById(id)) throw new NotFoundBoardException();
        boardRepository.deleteById(id);
    }

    @Transactional
    public void like(long id, long userId) {
        Board board = boardRepository.findById(id).orElseThrow(NotFoundBoardException::new);
        User user = userRepository.findById(userId).orElseThrow(NotFoundUserException::new);
        board.like(user);
        boardRepository.save(board);
    }

    public Optional<SelectBoard> findBoardById(long id) { return boardQueryRepository.findBoardById(id);}
    public Page<SelectBoard> findBoards(Pageable pageable) {
        return boardQueryRepository.findPage(pageable);
    }

    public Page<SelectBoard> findBoardsByCondition(SearchCondition searchCondition, Pageable pageable) {
        return boardQueryRepository.findPageByCondition(searchCondition, pageable);
    }

    private Tag findOrCreateTag(String tagName) {
        return tagRepository.findTagByName(tagName)
                .orElseGet(() -> tagRepository.save(new Tag(tagName)));
    }

    @Transactional(readOnly = true)
    public Page<SelectBoard> findSortedBoards(SortCondition sortCondition, Pageable pageable){
        return boardQueryRepository.findSortedBoards(sortCondition, pageable);
    }
}