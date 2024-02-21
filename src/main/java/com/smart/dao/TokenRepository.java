package com.smart.dao;


import com.smart.model.PasswordResetToken;
import com.smart.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TokenRepository extends JpaRepository<PasswordResetToken,Integer> {
    PasswordResetToken findByToken(String token);

    @Transactional
    void deleteByUser(User user);

    User getByUser(User user);
}
