package com.example.java_board.api.user;

import com.example.java_board.api.user.dto.request.CreateUser;
import com.example.java_board.api.user.mapper.UserMapper;
import com.example.java_board.repository.user.User;
import com.example.java_board.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public User signUp(CreateUser createUser) {
        User user = userMapper.toEntity(createUser);
        userRepository.save(user);
        return user;
    }

}
