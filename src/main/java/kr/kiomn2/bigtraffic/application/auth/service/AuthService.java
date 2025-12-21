package kr.kiomn2.bigtraffic.application.auth.service;

import kr.kiomn2.bigtraffic.domain.auth.entity.User;
import kr.kiomn2.bigtraffic.domain.auth.exception.UserNotFoundException;
import kr.kiomn2.bigtraffic.infrastructure.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    /**
     * 회원탈퇴
     */
    @Transactional
    public void withdrawal(String email) {
        log.info("회원탈퇴 처리 시작 - email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("회원탈퇴 실패 - 사용자를 찾을 수 없음, email: {}", email);
                    return new UserNotFoundException();
                });

        log.info("사용자 삭제 중 - userId: {}, email: {}", user.getId(), email);
        userRepository.delete(user);
        log.info("회원탈퇴 처리 완료 - email: {}", email);
    }
}