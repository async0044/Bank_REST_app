package com.example.bankcards.util;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.CardService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class TestDatabase {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private CardService cardService;

    @PostConstruct
    public void init() {

        if(!userRepository.existsById(1L)) {

            User user = new User();
            user.setUsername("admin606050");
            user.setPassword(passwordEncoder.encode("password606050"));
            user.setFirstName("Admin");
            user.setLastName("Admin");
            user.setEmail("email606050@gmail.com");

            Set<Role> roles = new HashSet<>();
            roles.add(Role.USER);
            roles.add(Role.ADMIN);
            user.setRoles(roles);
            user.setAddedOn(LocalDateTime.now());

            userRepository.save(user);
        }
    }

}
