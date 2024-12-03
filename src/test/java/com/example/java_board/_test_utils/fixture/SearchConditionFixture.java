package com.example.java_board._test_utils.fixture;

import com.example.java_board.api.board.dto.request.SearchCondition;
import com.example.java_board.api.board.dto.request.SearchType;

public class SearchConditionFixture {
    public static SearchCondition contentCondition(String keyword){
        return new SearchCondition(SearchType.CONTENT, keyword);
    }

    public static SearchCondition tagCondition(String keyword) {
        return new SearchCondition(SearchType.TAG, keyword);
    }

    public static SearchCondition userCondition(String keyword) {
        return new SearchCondition(SearchType.USER, keyword);
    }
}
