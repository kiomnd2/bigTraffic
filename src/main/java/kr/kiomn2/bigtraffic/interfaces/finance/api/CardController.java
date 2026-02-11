package kr.kiomn2.bigtraffic.interfaces.finance.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.kiomn2.bigtraffic.domain.finance.command.*;
import kr.kiomn2.bigtraffic.domain.finance.entity.Card;
import kr.kiomn2.bigtraffic.domain.finance.query.GetCardQuery;
import kr.kiomn2.bigtraffic.domain.finance.query.GetCardsQuery;
import kr.kiomn2.bigtraffic.domain.finance.service.CardService;
import kr.kiomn2.bigtraffic.domain.finance.vo.CardType;
import kr.kiomn2.bigtraffic.interfaces.finance.dto.request.CardCreateRequest;
import kr.kiomn2.bigtraffic.interfaces.finance.dto.request.CardUpdateRequest;
import kr.kiomn2.bigtraffic.interfaces.finance.dto.response.CardListResponse;
import kr.kiomn2.bigtraffic.interfaces.finance.dto.response.CardResponse;
import kr.kiomn2.bigtraffic.interfaces.finance.mapper.CardMapper;
import kr.kiomn2.bigtraffic.domain.auth.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Card", description = "카드 관리 API")
@RestController
@RequestMapping("/api/v1/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;
    private final CardMapper cardMapper;

    @Operation(summary = "카드 등록", description = "새로운 카드를 등록합니다.")
    @PostMapping
    public ResponseEntity<CardResponse> createCard(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody CardCreateRequest request) {

        Long userId = user.getId();

        CreateCardCommand command = new CreateCardCommand(
                userId, request.getCardName(), request.getCardCompany(),
                request.getCardNumber(), request.getCardType(),
                request.getBalance(), request.getCreditLimit(),
                request.getBillingDay(), request.getColor(), request.getMemo()
        );
        Card card = cardService.createCard(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(cardMapper.toResponse(card));
    }

    @Operation(summary = "카드 목록 조회", description = "사용자의 카드 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<CardListResponse> getCards(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) CardType cardType,
            @RequestParam(required = false) Boolean isActive) {

        Long userId = user.getId();

        GetCardsQuery query = new GetCardsQuery(userId, cardType, isActive);
        List<Card> cards = cardService.getCards(query);

        return ResponseEntity.ok(cardMapper.toListResponse(cards));
    }

    @Operation(summary = "카드 상세 조회", description = "특정 카드의 상세 정보를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<CardResponse> getCard(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {

        Long userId = user.getId();

        GetCardQuery query = new GetCardQuery(userId, id);
        Card card = cardService.getCard(query);

        return ResponseEntity.ok(cardMapper.toDetailResponse(card));
    }

    @Operation(summary = "카드 수정", description = "카드 정보를 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<CardResponse> updateCard(
            @AuthenticationPrincipal User user,
            @PathVariable Long id,
            @Valid @RequestBody CardUpdateRequest request) {

        Long userId = user.getId();

        UpdateCardCommand command = new UpdateCardCommand(
                userId, id, request.getCardName(), request.getCardCompany(),
                request.getCardType(), request.getBalance(),
                request.getCreditLimit(), request.getIsActive(),
                request.getColor(), request.getMemo()
        );
        Card card = cardService.updateCard(command);
        return ResponseEntity.ok(cardMapper.toResponse(card));
    }

    @Operation(summary = "카드 삭제", description = "카드를 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {

        Long userId = user.getId();

        DeleteCardCommand command = new DeleteCardCommand(userId, id);
        cardService.deleteCard(command);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "기본 카드 설정", description = "특정 카드를 기본 카드로 설정합니다.")
    @PatchMapping("/{id}/default")
    public ResponseEntity<CardResponse> setDefaultCard(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {

        Long userId = user.getId();

        SetDefaultCardCommand command = new SetDefaultCardCommand(userId, id);
        Card card = cardService.setDefaultCard(command);

        return ResponseEntity.ok(cardMapper.toResponse(card));
    }
}
