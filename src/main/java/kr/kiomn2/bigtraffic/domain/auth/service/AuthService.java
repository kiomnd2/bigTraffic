package kr.kiomn2.bigtraffic.domain.auth.service;

import kr.kiomn2.bigtraffic.domain.auth.command.WithdrawalCommand;
import kr.kiomn2.bigtraffic.domain.auth.entity.User;
import kr.kiomn2.bigtraffic.domain.auth.exception.UserNotFoundException;
import kr.kiomn2.bigtraffic.domain.auth.query.GetCurrentUserQuery;
import kr.kiomn2.bigtraffic.domain.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    @Transactional
    public void withdrawal(WithdrawalCommand command) {
        log.info("회원탈퇴 처리 시작 - email: {}", command.getEmail());

        try {
            User user = userRepository.findByEmail(command.getEmail())
                    .orElseThrow(() -> {
                        log.error("회원탈퇴 실패 - 사용자를 찾을 수 없음, email: {}", command.getEmail());
                        return new UserNotFoundException();
                    });

            log.info("사용자 삭제 중 - userId: {}, email: {}", user.getId(), command.getEmail());
            userRepository.delete(user);
            log.info("회원탈퇴 처리 완료 - email: {}", command.getEmail());

        } catch (UserNotFoundException e) {
            log.error("=== 회원탈퇴 실패 (UserNotFoundException) ===");
            log.error("Email: {}", command.getEmail());
            log.error("StackTrace: ", e);
            log.error("===============================================");
            throw e;
        } catch (Exception e) {
            log.error("=== 회원탈퇴 처리 중 예상치 못한 오류 발생 ===");
            log.error("Email: {}", command.getEmail());
            log.error("Exception Type: {}", e.getClass().getName());
            log.error("Message: {}", e.getMessage());
            log.error("StackTrace: ", e);
            log.error("===============================================");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public User getCurrentUser(GetCurrentUserQuery query) {
        log.info("사용자 정보 조회 요청 - email: {}", query.getEmail());

        try {
            User user = userRepository.findByEmail(query.getEmail())
                    .orElseThrow(() -> {
                        log.error("사용자를 찾을 수 없음 - email: {}", query.getEmail());
                        return new UserNotFoundException();
                    });

            log.info("사용자 정보 조회 성공 - userId: {}, email: {}", user.getId(), query.getEmail());
            return user;

        } catch (UserNotFoundException e) {
            log.error("=== 사용자 정보 조회 실패 (UserNotFoundException) ===");
            log.error("Email: {}", query.getEmail());
            log.error("StackTrace: ", e);
            log.error("===================================================");
            throw e;
        } catch (Exception e) {
            log.error("=== 사용자 정보 조회 중 예상치 못한 오류 발생 ===");
            log.error("Email: {}", query.getEmail());
            log.error("Exception Type: {}", e.getClass().getName());
            log.error("Message: {}", e.getMessage());
            log.error("StackTrace: ", e);
            log.error("===================================================");
            throw e;
        }
    }
}
