package com.example.demo.service;

import com.example.demo.dto.UserDto;
import com.example.demo.model.User;
import com.example.demo.repos.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserService {

    private UserRepository userRepository;

    public void createUser(UserDto userDto) {
        System.out.println(userDto.getId());
        System.out.println(userDto.getIMEI_id());
        User user = userDto.toEntity();

        userRepository.save(user);

    }
}
