package com.example.bankcards.service;

import com.example.bankcards.dto.*;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.BalanceException;
import com.example.bankcards.exception.SomethingNotFoundException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.CardStatus;
import com.example.bankcards.util.CardUtils;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    CardRepository cardRepository;
    @Autowired
    CardUtils cardUtils;



    public List<CardResponse> findCardsAsList() {
        return cardRepository.findAll().stream().map(card -> CardMapper.cardToDtoUnhide(card)).collect(Collectors.toList());
    }



    public PageCardResponse<CardResponse> findCardsByCurrentUserId(int page, int size, Double minBalance, Double maxBalance) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Card> cardPage;

        if (minBalance != null || maxBalance != null) {
            cardPage = cardRepository.getByUserIdAndBalance(cardUtils.getCurrentUserId(), minBalance, maxBalance, pageable);
        } else {
            cardPage = cardRepository.getByUserId(cardUtils.getCurrentUserId(), pageable);
        }

        List<CardResponse> cardResponses = cardPage.getContent().stream().map(c -> CardMapper.cardToDtoUnhide(c)).collect(Collectors.toList());

        return new PageCardResponse<>(
                cardResponses,
                page,
                cardPage.getTotalPages(),
                cardPage.getTotalElements(),
                size);
    }



    public CardResponse createCardByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new SomethingNotFoundException("User with ID \"" + userId + "\" not found. ", this.getClass().getName() + ".createCardByUserId()"));
        Card card = new Card(
                user,
                cardUtils.generateCardNumber(),
                user.getFirstName(),
                user.getLastName(),
                LocalDateTime.now(),
                CardStatus.ACTIVE,
                0.0
        );
        return CardMapper.cardToDtoUnhide(cardRepository.save(card));
    }


    @Transactional
    public CardResponse blockCardById(Long id) {
        Card card = cardRepository.findById(id).orElseThrow(() -> new SomethingNotFoundException("Card with ID \"" + id + "\" not found. ", this.getClass().getName() + ".blockCardById()"));
        card.setCardStatus(CardStatus.BLOCKED);
        return CardMapper.cardToDtoUnhide(cardRepository.save(card));
    }


    @Transactional
    public CardResponse activateCardById(Long id) {
        Card card = cardRepository.findById(id).orElseThrow(() -> new SomethingNotFoundException("Card with ID \"" + id + "\" not found. ", this.getClass().getName() + ".blockCardById()"));
        card.setCardStatus(CardStatus.ACTIVE);
        return CardMapper.cardToDtoUnhide(cardRepository.save(card));
    }



    public CardResponse deleteCardById(Long id) {
        return CardMapper.cardToDtoUnhide(cardRepository.deleteCardById(id).orElseThrow(() -> new SomethingNotFoundException("Card with ID \"" + id + "\" not found. ", this.getClass().getName() + ".deleteCardById()")));
    }



    public List<CardResponse> findCardsByCurrentUserId() {
        return cardRepository.findCardsByUserId(cardUtils.getCurrentUserId()).stream().map(card -> CardMapper.cardToDtoUnhide(card)).collect(Collectors.toList());
    }


    @Transactional
    public CardResponse cardBlockingRequestById(Long cardId) {
        Card card = cardRepository.findByCardIdAndUserId(cardId, cardUtils.getCurrentUserId()).orElseThrow(() -> new SomethingNotFoundException("Card with ID \"" + cardId + "\" not found. ", this.getClass().getName() + ".cardBlockingRequestById()"));
        card.setCardStatus(CardStatus.BLOCKINGREQUEST);
        return CardMapper.cardToDtoUnhide(cardRepository.save(card));
    }



    public Double checkBalanceByCardId(Long cardId) {
        if (!cardRepository.existByIdandUserId(cardId, cardUtils.getCurrentUserId())) throw new SomethingNotFoundException("Card with ID \"" + cardId + "\" not found. ", this.getClass().getName() + ".checkBalanceByCardId()");
        return cardRepository.checkBalanceById(cardId).get();
    }


    @Transactional
    public TransferResponse cardToCardTransfer(TransferRequest transferRequest) {
        Card sourceCard = cardRepository.findByCardIdAndUserId(transferRequest.sourceCardId(), cardUtils.getCurrentUserId()).orElseThrow(() -> new SomethingNotFoundException("Card with ID \"" + transferRequest.sourceCardId() + "\" not found. ", this.getClass().getName() + ".cardToCardTransfer()"));
        Card targetCard = cardRepository.findByCardIdAndUserId(transferRequest.targetCardId(), cardUtils.getCurrentUserId()).orElseThrow(() -> new SomethingNotFoundException("Card with ID \"" + transferRequest.targetCardId() + "\" not found. ", this.getClass().getName() + ".cardToCardTransfer()"));
        Double checkTransfer = sourceCard.getBalance() + targetCard.getBalance();
        if (sourceCard.getBalance() >= transferRequest.amount()) {
            sourceCard.setBalance(sourceCard.getBalance() - transferRequest.amount());
            targetCard.setBalance(targetCard.getBalance() + transferRequest.amount());
        }   else throw new BalanceException("Card with ID " + sourceCard.getId() + " balance not enough.", this.getClass().getName() + ".cardToCardTransfer()");
        if (checkTransfer == sourceCard.getBalance() + targetCard.getBalance()) {
            cardRepository.save(sourceCard);
            cardRepository.save(targetCard);
            return new TransferResponse(sourceCard.getBalance(), targetCard.getBalance());
        } else throw new ServiceException("Transfer opeartion cancelled");
    }
}
