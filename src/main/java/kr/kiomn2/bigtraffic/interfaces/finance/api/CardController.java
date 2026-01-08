package kr.kiomn2.bigtraffic.interfaces.finance.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.kiomn2.bigtraffic.application.finance.command.CreateCardCommand;
import kr.kiomn2.bigtraffic.application.finance.command.DeleteCardCommand;
import kr.kiomn2.bigtraffic.application.finance.command.SetDefaultCardCommand;
import kr.kiomn2.bigtraffic.application.finance.command.UpdateCardCommand;
import kr.kiomn2.bigtraffic.application.finance.query.GetCardQuery;
import kr.kiomn2.bigtraffic.application.finance.query.GetCardsQuery;
import kr.kiomn2.bigtraffic.application.finance.service.CardService;
import kr.kiomn2.bigtraffic.domain.finance.vo.CardType;
import kr.kiomn2.bigtraffic.interfaces.finance.dto.request.CardCreateRequest;
import kr.kiomn2.bigtraffic.interfaces.finance.dto.request.CardUpdateRequest;
import kr.kiomn2.bigtraffic.interfaces.finance.dto.response.CardListResponse;
import kr.kiomn2.bigtraffic.interfaces.finance.dto.response.CardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Card", description = "카드 관리 API")
@RestController
@RequestMapping("/api/v1/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @Operation(summary = "카드 등록", description = "새로운 카드를 등록합니다.")
    @PostMapping
    public ResponseEntity<CardResponse> createCard(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CardCreateRequest request) {

        Long userId = Long.parseLong(userDetails.getUsername());

        CreateCardCommand command = CreateCardCommand.from(userId, request);
        CardResponse response = cardService.createCard(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "카드 목록 조회", description = "사용자의 카드 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<CardListResponse> getCards(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) CardType cardType,
            @RequestParam(required = false) Boolean isActive) {

        Long userId = Long.parseLong(userDetails.getUsername());

        GetCardsQuery query = new GetCardsQuery(userId, cardType, isActive);
        CardListResponse response = cardService.getCards(query);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "카드 상세 조회", description = "특정 카드의 상세 정보를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<CardResponse> getCard(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {

        Long userId = Long.parseLong(userDetails.getUsername());

        GetCardQuery query = new GetCardQuery(userId, id);
        CardResponse response = cardService.getCard(query);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "카드 수정", description = "카드 정보를 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<CardResponse> updateCard(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id,
            @Valid @RequestBody CardUpdateRequest request) {

        Long userId = Long.parseLong(userDetails.getUsername());

        UpdateCardCommand command = UpdateCardCommand.from(userId, id, request);
        CardResponse response = cardService.updateCard(command);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "카드 삭제", description = "카드를 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {

        Long userId = Long.parseLong(userDetails.getUsername());

        DeleteCardCommand command = new DeleteCardCommand(userId, id);
        cardService.deleteCard(command);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "기본 카드 설정", description = "특정 카드를 기본 카드로 설정합니다.")
    @PatchMapping("/{id}/default")
    public ResponseEntity<CardResponse> setDefaultCard(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {

        Long userId = Long.parseLong(userDetails.getUsername());

        SetDefaultCardCommand command = new SetDefaultCardCommand(userId, id);
        CardResponse response = cardService.setDefaultCard(command);

        return ResponseEntity.ok(response);
    }
}
