package kr.kiomn2.bigtraffic.application.auth.service;

import kr.kiomn2.bigtraffic.domain.auth.entity.User;
import kr.kiomn2.bigtraffic.domain.auth.exception.DuplicateEmailException;
import kr.kiomn2.bigtraffic.domain.auth.exception.InvalidPasswordException;
import kr.kiomn2.bigtraffic.domain.auth.exception.UserNotFoundException;
import kr.kiomn2.bigtraffic.infrastructure.auth.repository.UserRepository;
import kr.kiomn2.bigtraffic.infrastructure.auth.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    /**
     * 회원가입
     */
    @Transactional
    public String register(String email, String username, String password) {
        // 이메일 중복 확인
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateEmailException();
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(password);

        // 유저 생성
        User user = User.builder()
                .email(email)
                .username(username)
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
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            User user = (User) authentication.getPrincipal();

            // 최근 로그인 날짜 업데이트
            user.setLastLoginDate(java.time.LocalDateTime.now());
            userRepository.save(user);

            // JWT 토큰 생성 및 반환
            return jwtTokenProvider.createToken(email);
        } catch (AuthenticationException e) {
            throw new InvalidPasswordException();
        }
    }

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