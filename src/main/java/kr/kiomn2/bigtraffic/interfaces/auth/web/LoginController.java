package kr.kiomn2.bigtraffic.interfaces.auth.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/user-info")
    public String userInfo() {
        return "user-info";
    }

    @GetMapping("/api/auth/kakao/callback")
    public String kakaoCallback() {
        return "kakao-callback";
    }
}
