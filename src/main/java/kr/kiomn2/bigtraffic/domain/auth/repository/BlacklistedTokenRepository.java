package kr.kiomn2.bigtraffic.domain.auth.repository;

import kr.kiomn2.bigtraffic.domain.auth.entity.BlacklistedToken;

import java.time.LocalDateTime;

public interface BlacklistedTokenRepository {

    boolean existsByToken(String token);

    boolean existsByTokenAndExpirationDateAfter(String token, LocalDateTime now);

    BlacklistedToken save(BlacklistedToken token);

    void deleteExpiredTokens(LocalDateTime now);
}
