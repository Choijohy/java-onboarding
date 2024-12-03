package com.example.java_board.repository.tag;

import com.example.java_board.repository._common.IdentifiableEntity;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag extends IdentifiableEntity {
    private String name;

    public Tag(String name) {
        this.name = name;
    }
}
