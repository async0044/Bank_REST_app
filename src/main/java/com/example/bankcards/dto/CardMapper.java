package com.example.bankcards.dto;

import com.example.bankcards.entity.Card;
import com.example.bankcards.util.CardStatus;

import java.time.LocalDateTime;

public class CardMapper {
    public static CardResponse cardToDtoHide(Card card) {
        return new CardResponse(
                card.getId(),
                "****-****-****-" + card.getCardNumber().substring(card.getCardNumber().length() - 4),
                card.getUserId(),
                card.getOwnerFirstname(),
                card.getOwnerLastname(),
                card.getAddedOn(),
                card.getCardStatus(),
                card.getBalance()
        );
    }

    public static CardResponse cardToDtoUnhide(Card card) {
        return new CardResponse(
                card.getId(),
                card.getCardNumber(),
                card.getUserId(),
                card.getOwnerFirstname(),
                card.getOwnerLastname(),
                card.getAddedOn(),
                card.getCardStatus(),
                card.getBalance()
        );
    }
}
