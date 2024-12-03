package com.example.java_board.api.user;


import com.example.java_board.api.user.dto.request.CreateUser;
import com.example.java_board._core.dto.ApiResponse;
import com.example.java_board.repository.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<ApiResponse<User>> signUp(@RequestBody CreateUser request) {
        User user = userService.signUp(request);
        return ApiResponse.of(HttpStatus.CREATED, user);
    }

}
