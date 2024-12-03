package com.example.java_board.api.user;

import static org.mockito.Mockito.*;

import com.example.java_board.api.user.dto.request.CreateUser;
import com.example.java_board.api.user.mapper.UserMapper;
import com.example.java_board._test_utils.fixture.UserFixture;
import com.example.java_board.repository.user.User;
import com.example.java_board.repository.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    UserMapper userMapper;

    @InjectMocks
    UserService userService;

    @Test
    void signUp() {
        // given
        User user = UserFixture.user();
        CreateUser createUser = UserFixture.createUser();
        when(userMapper.toEntity(createUser)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        // when
        userService.signUp(createUser);

        // then
        verify(userMapper).toEntity(createUser);
        verify(userRepository).save(user);
    }
}