package com.example.java_board.api.user.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class SelectUser {
        private long id;
        private String email;

        @QueryProjection
        public SelectUser(long id, String email) {
                this.id = id;
                this.email = email;
        }
}
