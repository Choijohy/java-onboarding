package com.example.java_board.api.user.mapper;

import com.example.java_board.api.user.dto.request.CreateUser;
import com.example.java_board.repository.user.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(CreateUser createUser) {
        return new User(createUser.email(), createUser.password());
    }
}
