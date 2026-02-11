package kr.kiomn2.bigtraffic.interfaces.accountbook.api;

import kr.kiomn2.bigtraffic.domain.accountbook.command.CreateCategoryCommand;
import kr.kiomn2.bigtraffic.domain.accountbook.command.DeleteCategoryCommand;
import kr.kiomn2.bigtraffic.domain.accountbook.command.UpdateCategoryCommand;
import kr.kiomn2.bigtraffic.domain.accountbook.entity.Category;
import kr.kiomn2.bigtraffic.domain.accountbook.query.GetCategoriesQuery;
import kr.kiomn2.bigtraffic.domain.accountbook.query.GetCategoryQuery;
import kr.kiomn2.bigtraffic.domain.accountbook.service.CategoryService;
import kr.kiomn2.bigtraffic.domain.accountbook.vo.TransactionType;
import kr.kiomn2.bigtraffic.domain.auth.entity.User;
import kr.kiomn2.bigtraffic.interfaces.accountbook.dto.request.CategoryCreateRequest;
import kr.kiomn2.bigtraffic.interfaces.accountbook.dto.request.CategoryUpdateRequest;
import kr.kiomn2.bigtraffic.interfaces.accountbook.dto.response.CategoryResponse;
import kr.kiomn2.bigtraffic.interfaces.accountbook.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(
            @AuthenticationPrincipal User user,
            @RequestBody CategoryCreateRequest request
    ) {
        CreateCategoryCommand command = new CreateCategoryCommand(
                user.getId(), request.getName(), request.getType(),
                request.getColor(), request.getIcon()
        );
        Category category = categoryService.createCategory(command);
        return ResponseEntity.ok(categoryMapper.toResponse(category));
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getCategories(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) TransactionType type,
            @RequestParam(required = false) Boolean isActive
    ) {
        GetCategoriesQuery query = new GetCategoriesQuery(user.getId(), type, isActive);
        List<Category> categories = categoryService.getCategories(query);
        return ResponseEntity.ok(categoryMapper.toResponseList(categories));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategory(
            @AuthenticationPrincipal User user,
            @PathVariable Long id
    ) {
        GetCategoryQuery query = new GetCategoryQuery(user.getId(), id);
        Category category = categoryService.getCategory(query);
        return ResponseEntity.ok(categoryMapper.toResponse(category));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @AuthenticationPrincipal User user,
            @PathVariable Long id,
            @RequestBody CategoryUpdateRequest request
    ) {
        UpdateCategoryCommand command = new UpdateCategoryCommand(
                user.getId(), id, request.getName(), request.getColor(), request.getIcon()
        );
        Category category = categoryService.updateCategory(command);
        return ResponseEntity.ok(categoryMapper.toResponse(category));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(
            @AuthenticationPrincipal User user,
            @PathVariable Long id
    ) {
        DeleteCategoryCommand command = new DeleteCategoryCommand(user.getId(), id);
        categoryService.deleteCategory(command);
        return ResponseEntity.noContent().build();
    }
}
