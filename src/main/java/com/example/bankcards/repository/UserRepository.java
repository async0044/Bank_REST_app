package com.example.bankcards.repository;

import com.example.bankcards.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = """
        SELECT COUNT (*) > 0 FROM users WHERE username = :username
        """, nativeQuery = true)
    boolean existByUsername(@Param("username") String username);

    @Query(value = """
        SELECT COUNT (*) > 0 FROM users WHERE email = :email
        """,nativeQuery = true)
    boolean existByEmail(@Param("email") String email);

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);

    @Query(value = """
        SELECT * FROM users
        """, nativeQuery = true)
    List<User> getAllUsers();

    @Query(value = """
        DELETE FROM users WHERE id = :id
        RETURNING *
        """, nativeQuery = true)
    Optional<User> deleteUserById(@Param("id") Long id);


}
