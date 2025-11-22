package com.example.bankcards.entity;

import com.example.bankcards.util.CardStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "cards")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String cardNumber;
    private String ownerFirstname;
    private String ownerLastname;
    private LocalDateTime addedOn;
    @Enumerated(EnumType.STRING)
    private CardStatus status;
    private Double balance;

    public Card() {
    }

    public Card(User user, String cardNumber, String ownerFirstname, String ownerLastname, LocalDateTime addedOn, CardStatus status, Double balance) {
        this.user = user;
        this.cardNumber = cardNumber;
        this.ownerFirstname = ownerFirstname;
        this.ownerLastname = ownerLastname;
        this.addedOn = addedOn;
        this.status = status;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public Long getUserId() {
        return user.getId();
    }

    public String getOwnerFirstname() {
        return ownerFirstname;
    }

    public String getOwnerLastname() {
        return ownerLastname;
    }

    public LocalDateTime getAddedOn() {
        return addedOn;
    }

    public CardStatus getCardStatus() {
        return status;
    }

    public Double getBalance() {
        return balance;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setOwnerFirstname(String ownerFirstname) {
        this.ownerFirstname = ownerFirstname;
    }

    public void setOwnerLastname(String ownerLastname) {
        this.ownerLastname = ownerLastname;
    }

    public void setAddedOn(LocalDateTime addedOn) {
        this.addedOn = addedOn;
    }

    public void setCardStatus(CardStatus status) {
        this.status = status;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
