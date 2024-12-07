package com.we_assignment.service;

import com.we_assignment.dto.request.UserRequestDto;
import com.we_assignment.dto.response.UserResponseDto;
import com.we_assignment.entity.User;
import com.we_assignment.enums.Role;
import com.we_assignment.exception.member.MemberNullPointerException;
import com.we_assignment.repository.jpa.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public UserResponseDto getUserResponseDtoById(UUID id) {
        return userToUserResponseDto(getUserById(id));
    }

    public User getUserById(UUID id) {
        return userRepository.findById(id).orElseThrow(MemberNullPointerException::new);
    }

    public UserResponseDto userToUserResponseDto(User user) {
        return new UserResponseDto(user.getUsername(), user.getEmail());
    }

    public void signUp(UserRequestDto.Create userRequestDto) {
        User user = convertCreateDtoToUser(userRequestDto);
        userRepository.save(user);
        log.info("유저 생성: {}", user.getEmail());
    }

    public User convertCreateDtoToUser(UserRequestDto.Create userRequestDto) {
        return User.builder()
                .username(userRequestDto.getUsername())
                .password(userRequestDto.getPassword())
                .email(userRequestDto.getEmail())
                .role(Role.USER)
                .build();
    }

    public User convertUpdateDtoToUser(UserRequestDto.Update userRequestDto) {
        return User.builder()
                .username(userRequestDto.getUsername())
                .password(userRequestDto.getPassword())
                .email(userRequestDto.getEmail())
                .role(Role.USER)
                .build();
    }

    public void updateUser(UserRequestDto.Update userRequestDto) {
        User user = convertUpdateDtoToUser(userRequestDto);
        userRepository.save(user);
        log.info("유저 수정 : {}", user.getEmail());
    }

    public void deleteUser(User user) {
        user.delete();
        userRepository.save(user);
        log.info("유저 삭제 : {}", user.getEmail());
    }

}
