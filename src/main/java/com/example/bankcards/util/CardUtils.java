package com.example.bankcards.util;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.AuthException;
import com.example.bankcards.exception.SomethingNotFoundException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class CardUtils {

    @Autowired
    CardRepository cardRepository;
    @Autowired
    private UserRepository userRepository;

    public String generateCardNumber() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        String cardNumber = "";
        int counter = 0;

        do {
            cardNumber = String.format("%04d-%04d-%04d-%04d",
                    random.nextInt(10000),
                    random.nextInt(10000),
                    random.nextInt(10000),
                    random.nextInt(10000));
            counter++;
            System.out.println(cardNumber);
            System.out.println(cardRepository.existsByCardNumber(cardNumber));
            System.out.println(counter);
        } while (cardRepository.existsByCardNumber(cardNumber) && counter < 5);

        return cardNumber;
    }

    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails)
                return ((UserDetailsImpl) principal).getId();

            if (principal instanceof String)
                return userRepository.findByUsername((String) principal).orElseThrow(() -> new SomethingNotFoundException("User authentication not found.", this.getClass().getName() + ".getCurrentUserId()")).getId();
        }
        throw new AuthException("User not authenticated", this.getClass().getName() + ".getCurrentUserId()");
    }
}
