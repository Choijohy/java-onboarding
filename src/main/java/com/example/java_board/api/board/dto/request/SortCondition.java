package com.example.java_board.api.board.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record SortCondition (
    @NotEmpty
    @Size(max=3)
    List<@Valid SortType> sortTypes
){
}
