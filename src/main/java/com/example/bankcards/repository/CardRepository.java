package com.example.bankcards.repository;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    Optional<Card> findById(Long id);
    List<Card> findAll();

    @Query(value = """
        SELECT card FROM Card card WHERE card.id = :cardId AND card.user.id = :userId
        """)
    Optional<Card> findByCardIdAndUserId(@Param("cardId") Long cardId, @Param("userId") Long userId);

    @Query(value = """
        SELECT * FROM cards WHERE user_id = :userId
        """, nativeQuery = true)
    List<Card> findCardsByUserId(Long userId);

    @Query(value = """
        SELECT COUNT (*) > 0 FROM cards WHERE user_id = :userId
        """, nativeQuery = true)
    boolean existsByUserId(Long userId);

    @Query(value = """
        SELECT COUNT (*) > 0 FROM cards WHERE card_number = :cardNumber
        """, nativeQuery = true)
    boolean existsByCardNumber(String cardNumber);

    @Query(value = """
        DELETE FROM cards WHERE id = :id
        RETURNING *
        """, nativeQuery = true)
    Optional<Card> deleteCardById(@Param("id") Long id);

    @Query(value = """
        SELECT card.balance FROM Card card WHERE card.id = :id
        """)
    Optional<Double> checkBalanceById(@Param("id") Long id);

    @Query(value = """
        SELECT EXISTS(SELECT 1 FROM cards WHERE id = :cardId AND user_id = :userId)
        """, nativeQuery = true)
    boolean existByIdandUserId(@Param("cardId") Long cardId, @Param("userId") Long userId);

    @Query(value = """
        SELECT * FROM cards WHERE user_id = :userId
        """, nativeQuery = true)
    Page<Card> getByUserId(Long userId, Pageable pageable);

    @Query(value = "SELECT * FROM cards card WHERE card.user_id = :userId AND card.balance BETWEEN :minBalance AND :maxBalance"
        , countQuery = "SELECT count(*) FROM cards card WHERE card.user_id = :userId AND card.balance BETWEEN :minBalance AND :maxBalance"
        , nativeQuery = true)
    Page<Card> getByUserIdAndBalance(Long userId, Double minBalance, Double maxBalance, Pageable pageable);
}
