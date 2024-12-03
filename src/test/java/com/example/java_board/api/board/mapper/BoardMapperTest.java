package com.example.java_board.api.board.mapper;

import com.example.java_board.repository.user.UserRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BoardMapperTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    BoardMapper boardMapper;

//    @Test
//    void toEntity() {
//        User user = UserFixture.user();
//        CreateBoard createBoard = BoardFixture.createBoard(user);
//        boardMapper.toEntity(createBoard);
//        // TODO 문성하 findById 의 throw 때문에 매번 검사해야한다. Impl 을 만들자.
//    }
}