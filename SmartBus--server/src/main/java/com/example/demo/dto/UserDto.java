package com.example.demo.dto;

import com.example.demo.model.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

    private Long id;

    private String IMEI_id;

    public User toEntity() {
        User user = User.builder()
                .id(id)
                .IMEI_id(IMEI_id)
                .build();

        return user;
    }
}
