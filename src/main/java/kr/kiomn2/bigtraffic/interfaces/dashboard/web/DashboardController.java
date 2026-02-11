package kr.kiomn2.bigtraffic.interfaces.dashboard.web;

import kr.kiomn2.bigtraffic.application.dashboard.facade.DashboardFacade;
import kr.kiomn2.bigtraffic.domain.auth.entity.User;
import kr.kiomn2.bigtraffic.interfaces.dashboard.dto.response.DashboardResponse;
import kr.kiomn2.bigtraffic.interfaces.finance.dto.response.BankAccountResponse;
import kr.kiomn2.bigtraffic.interfaces.finance.dto.response.CardResponse;
import kr.kiomn2.bigtraffic.interfaces.finance.mapper.BankAccountMapper;
import kr.kiomn2.bigtraffic.interfaces.finance.mapper.CardMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 대시보드 웹 컨트롤러
 */
@Slf4j
@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardFacade dashboardFacade;
    private final BankAccountMapper bankAccountMapper;
    private final CardMapper cardMapper;

    /**
     * 대시보드 메인 페이지
     *
     * @param user 인증된 사용자
     * @param model 뷰 모델
     * @return 대시보드 템플릿
     */
    @GetMapping
    public String dashboard(@AuthenticationPrincipal User user, Model model) {
        log.info("대시보드 페이지 접근 - userId: {}, email: {}", user.getId(), user.getEmail());

        try {
            DashboardFacade.DashboardData data = dashboardFacade.getDashboard(user);

            List<BankAccountResponse> accountResponses = data.getAccounts().stream()
                    .map(bankAccountMapper::toResponse)
                    .toList();

            List<CardResponse> cardResponses = data.getCards().stream()
                    .map(cardMapper::toResponse)
                    .toList();

            DashboardResponse dashboard = DashboardResponse.of(
                    data.getTotalAssets(),
                    data.getTotalBalance(),
                    accountResponses,
                    cardResponses,
                    data.getUsername(),
                    data.getEmail()
            );

            model.addAttribute("totalAssets", dashboard.getTotalAssets());
            model.addAttribute("totalBalance", dashboard.getTotalBalance());
            model.addAttribute("accountCount", dashboard.getAccountCount());
            model.addAttribute("cardCount", dashboard.getCardCount());
            model.addAttribute("accounts", dashboard.getAccounts());
            model.addAttribute("cards", dashboard.getCards());
            model.addAttribute("username", dashboard.getUsername());
            model.addAttribute("email", dashboard.getEmail());
            model.addAttribute("isAdmin", user.isAdmin());

            log.info("대시보드 페이지 렌더링 완료 - userId: {}", user.getId());
            return "dashboard/dashboard";

        } catch (Exception e) {
            log.error("대시보드 페이지 로드 실패 - userId: {}, email: {}", user.getId(), user.getEmail(), e);
            model.addAttribute("errorMessage", "대시보드 정보를 불러오는데 실패했습니다.");
            return "error";
        }
    }
}
