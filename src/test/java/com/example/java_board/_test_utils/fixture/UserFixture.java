package com.example.java_board._test_utils.fixture;

import com.example.java_board.api.user.dto.request.CreateUser;
import com.example.java_board.api.user.dto.response.SelectUser;
import com.example.java_board.repository.user.User;

public class UserFixture {

    public static User user() {
        return new User("email", "password");
    }

    public static User user(String email) {
        return new User(email, "password");
    }

    public static CreateUser createUser() {
        return new CreateUser("email", "password");
    }

    public static SelectUser selectUser() { return new SelectUser(1L, "email");}
}
