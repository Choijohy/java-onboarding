package com.example.java_board._test_utils.fixture;

import com.example.java_board.repository.board.Board;
import com.example.java_board.repository.board.BoardTag;
import com.example.java_board.repository.tag.Tag;

import java.util.List;
import java.util.Set;

public class TagFixture {

    public static Tag tag() { return new Tag("tag1");}

    public static Tag tag(String keyword){ return new Tag(keyword);}

    public static BoardTag boardTag(Board board, Tag tag){
        return new BoardTag(board, tag);
    }
}
