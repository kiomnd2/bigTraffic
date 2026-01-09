package kr.kiomn2.bigtraffic.interfaces.accountbook.api;

import kr.kiomn2.bigtraffic.application.accountbook.command.CreateCategoryCommand;
import kr.kiomn2.bigtraffic.application.accountbook.command.DeleteCategoryCommand;
import kr.kiomn2.bigtraffic.application.accountbook.command.UpdateCategoryCommand;
import kr.kiomn2.bigtraffic.application.accountbook.query.GetCategoriesQuery;
import kr.kiomn2.bigtraffic.application.accountbook.query.GetCategoryQuery;
import kr.kiomn2.bigtraffic.application.accountbook.service.CategoryService;
import kr.kiomn2.bigtraffic.domain.accountbook.vo.TransactionType;
import kr.kiomn2.bigtraffic.domain.auth.entity.User;
import kr.kiomn2.bigtraffic.interfaces.accountbook.dto.request.CategoryCreateRequest;
import kr.kiomn2.bigtraffic.interfaces.accountbook.dto.request.CategoryUpdateRequest;
import kr.kiomn2.bigtraffic.interfaces.accountbook.dto.response.CategoryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * 가계부 카테고리 API 컨트롤러
 * 카테고리 등록/수정/삭제는 관리자만 가능
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * 관리자 권한 체크
     */
    private void checkAdminRole(User user) {
        if (!user.isAdmin()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "관리자만 접근할 수 있습니다.");
        }
    }

    /**
     * 카테고리 생성 (관리자 전용)
     */
    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(
            @AuthenticationPrincipal User user,
            @RequestBody CategoryCreateRequest request
    ) {
        checkAdminRole(user);
        CreateCategoryCommand command = CreateCategoryCommand.from(user.getId(), request);
        CategoryResponse response = categoryService.createCategory(command);
        return ResponseEntity.ok(response);
    }

    /**
     * 카테고리 목록 조회
     */
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getCategories(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) TransactionType type,
            @RequestParam(required = false) Boolean isActive
    ) {
        GetCategoriesQuery query = new GetCategoriesQuery(user.getId(), type, isActive);
        List<CategoryResponse> categories = categoryService.getCategories(query);
        return ResponseEntity.ok(categories);
    }

    /**
     * 카테고리 상세 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategory(
            @AuthenticationPrincipal User user,
            @PathVariable Long id
    ) {
        GetCategoryQuery query = new GetCategoryQuery(user.getId(), id);
        CategoryResponse category = categoryService.getCategory(query);
        return ResponseEntity.ok(category);
    }

    /**
     * 카테고리 수정 (관리자 전용)
     */
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @AuthenticationPrincipal User user,
            @PathVariable Long id,
            @RequestBody CategoryUpdateRequest request
    ) {
        checkAdminRole(user);
        UpdateCategoryCommand command = UpdateCategoryCommand.from(user.getId(), id, request);
        CategoryResponse response = categoryService.updateCategory(command);
        return ResponseEntity.ok(response);
    }

    /**
     * 카테고리 삭제 (관리자 전용, 비활성화)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(
            @AuthenticationPrincipal User user,
            @PathVariable Long id
    ) {
        checkAdminRole(user);
        DeleteCategoryCommand command = new DeleteCategoryCommand(user.getId(), id);
        categoryService.deleteCategory(command);
        return ResponseEntity.noContent().build();
    }
}
