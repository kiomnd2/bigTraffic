package kr.kiomn2.bigtraffic.domain.auth.repository;

import kr.kiomn2.bigtraffic.domain.auth.entity.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<User> findByProviderAndProviderId(String provider, String providerId);

    User save(User user);

    void delete(User user);
}
