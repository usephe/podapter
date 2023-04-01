package com.audiophileproject.usermanagement.repos;

import com.audiophileproject.usermanagement.models.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    public Optional<RefreshToken> findById(Long id);
    public Optional<RefreshToken> findByToken(String token);

//    @Query(value = "select * from RefreshToken where User = :user and expired = false")
//    public Optional<RefreshToken> findNotExpiredByUser(User user);

}
