package kr.kiomn2.bigtraffic.infrastructure.auth.repository;

import kr.kiomn2.bigtraffic.domain.auth.entity.BlacklistedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface BlacklistedTokenRepository extends JpaRepository<BlacklistedToken, Long> {

    /**
     * 토큰이 블랙리스트에 존재하는지 확인
     */
    boolean existsByToken(String token);

    /**
     * 토큰이 블랙리스트에 존재하고 아직 만료되지 않았는지 확인
     */
    boolean existsByTokenAndExpirationDateAfter(String token, LocalDateTime now);

    /**
     * 만료된 토큰 삭제 (정리 작업용)
     */
    @Modifying
    @Query("DELETE FROM BlacklistedToken b WHERE b.expirationDate < :now")
    void deleteExpiredTokens(LocalDateTime now);
}
