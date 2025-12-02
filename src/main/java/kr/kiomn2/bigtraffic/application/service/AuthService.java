package kr.kiomn2.bigtraffic.application.service;

import kr.kiomn2.bigtraffic.domain.entity.User;
import kr.kiomn2.bigtraffic.domain.exception.DuplicateEmailException;
import kr.kiomn2.bigtraffic.domain.exception.InvalidPasswordException;
import kr.kiomn2.bigtraffic.domain.exception.UserNotFoundException;
import kr.kiomn2.bigtraffic.infrastructure.repository.UserRepository;
import kr.kiomn2.bigtraffic.infrastructure.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 회원가입
     */
    @Transactional
    public String register(String email, String password) {
        // 이메일 중복 확인
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateEmailException();
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(password);

        // 유저 생성
        User user = User.builder()
                .email(email)
                .password(encodedPassword)
                .build();

        userRepository.save(user);

        // JWT 토큰 생성 및 반환
        return jwtTokenProvider.createToken(email);
    }

    /**
     * 로그인
     */
    @Transactional
    public String login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidPasswordException();
        }

        // 최근 로그인 날짜 업데이트
        user.setLastLoginDate(java.time.LocalDateTime.now());
        userRepository.save(user);

        // JWT 토큰 생성 및 반환
        return jwtTokenProvider.createToken(email);
    }
}