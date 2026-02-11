package kr.kiomn2.bigtraffic.infrastructure.auth.repository;

import kr.kiomn2.bigtraffic.domain.auth.entity.BlacklistedToken;
import kr.kiomn2.bigtraffic.domain.auth.repository.BlacklistedTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class BlacklistedTokenRepositoryImpl implements BlacklistedTokenRepository {

    private final BlacklistedTokenJpaRepository jpaRepository;

    @Override
    public boolean existsByToken(String token) {
        return jpaRepository.existsByToken(token);
    }

    @Override
    public boolean existsByTokenAndExpirationDateAfter(String token, LocalDateTime now) {
        return jpaRepository.existsByTokenAndExpirationDateAfter(token, now);
    }

    @Override
    public BlacklistedToken save(BlacklistedToken token) {
        return jpaRepository.save(token);
    }

    @Override
    public void deleteExpiredTokens(LocalDateTime now) {
        jpaRepository.deleteExpiredTokens(now);
    }
}
