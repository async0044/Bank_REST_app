package com.example.bankcards.dto;

import com.example.bankcards.entity.User;
import java.util.stream.Collectors;

public class UserMapper {

    static public UserResponse userToDto(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRoles(),
                user.getAddedOn(),
                user.getCards().stream().map(card -> CardMapper.cardToDtoHide(card)).collect(Collectors.toSet()));
    }
}
