package kr.kiomn2.bigtraffic.application.auth.service;

import kr.kiomn2.bigtraffic.domain.auth.entity.User;
import kr.kiomn2.bigtraffic.domain.auth.exception.UserNotFoundException;
import kr.kiomn2.bigtraffic.infrastructure.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    /**
     * 회원탈퇴
     */
    @Transactional
    public void withdrawal(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        userRepository.delete(user);
    }
}