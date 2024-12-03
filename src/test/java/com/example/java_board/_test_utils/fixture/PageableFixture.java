package com.example.java_board._test_utils.fixture;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PathVariable;


public class PageableFixture {
    public static Pageable defaultPageable(){
        return PageRequest.of(0,5, Sort.by("id").descending());
    }

    public static Pageable pageable(int page, int size, Sort sort){
        return PageRequest.of(page, size, sort);
    }

}
