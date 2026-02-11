package kr.kiomn2.bigtraffic.interfaces.auth.mapper;

import kr.kiomn2.bigtraffic.domain.auth.entity.User;
import kr.kiomn2.bigtraffic.interfaces.auth.dto.UserInfoResponse;
import org.springframework.stereotype.Component;

@Component
public class AuthMapper {

    public UserInfoResponse toResponse(User user) {
        return UserInfoResponse.from(user);
    }
}
