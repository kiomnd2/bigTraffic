package kr.kiomn2.bigtraffic.application.accountbook.service;

import kr.kiomn2.bigtraffic.application.accountbook.command.CreateCategoryCommand;
import kr.kiomn2.bigtraffic.application.accountbook.command.DeleteCategoryCommand;
import kr.kiomn2.bigtraffic.application.accountbook.command.UpdateCategoryCommand;
import kr.kiomn2.bigtraffic.application.accountbook.query.GetCategoriesQuery;
import kr.kiomn2.bigtraffic.application.accountbook.query.GetCategoryQuery;
import kr.kiomn2.bigtraffic.domain.accountbook.entity.Category;
import kr.kiomn2.bigtraffic.infrastructure.accountbook.repository.CategoryRepository;
import kr.kiomn2.bigtraffic.interfaces.accountbook.dto.response.CategoryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 카테고리 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * 카테고리 생성
     */
    @Transactional
    public CategoryResponse createCategory(CreateCategoryCommand command) {
        Category category = Category.createCustom(
                command.getUserId(),
                command.getName(),
                command.getType(),
                command.getColor(),
                command.getIcon()
        );
        Category saved = categoryRepository.save(category);

        log.info("카테고리 생성 완료 - categoryId: {}, name: {}", saved.getId(), saved.getName());
        return CategoryResponse.from(saved);
    }

    /**
     * 사용자의 카테고리 목록 조회
     */
    public List<CategoryResponse> getCategories(GetCategoriesQuery query) {
        List<Category> categories;

        if (query.getType() != null && query.getIsActive() != null) {
            categories = categoryRepository.findByUserIdAndTypeAndIsActive(
                    query.getUserId(), query.getType(), query.getIsActive()
            );
        } else if (query.getType() != null) {
            categories = categoryRepository.findByUserIdAndType(query.getUserId(), query.getType());
        } else if (query.getIsActive() != null) {
            categories = categoryRepository.findByUserIdAndIsActive(query.getUserId(), query.getIsActive());
        } else {
            categories = categoryRepository.findByUserId(query.getUserId());
        }

        return categories.stream()
                .map(CategoryResponse::from)
                .toList();
    }

    /**
     * 카테고리 상세 조회
     */
    public CategoryResponse getCategory(GetCategoryQuery query) {
        Category category = categoryRepository.findByIdAndUserId(query.getCategoryId(), query.getUserId())
                .orElseThrow(() -> new RuntimeException("카테고리를 찾을 수 없습니다."));

        return CategoryResponse.from(category);
    }

    /**
     * 카테고리 수정
     */
    @Transactional
    public CategoryResponse updateCategory(UpdateCategoryCommand command) {
        Category category = categoryRepository.findByIdAndUserId(command.getCategoryId(), command.getUserId())
                .orElseThrow(() -> new RuntimeException("카테고리를 찾을 수 없습니다."));

        category.updateInfo(command.getName(), command.getColor(), command.getIcon());
        log.info("카테고리 수정 완료 - categoryId: {}", command.getCategoryId());

        return CategoryResponse.from(category);
    }

    /**
     * 카테고리 삭제 (비활성화)
     */
    @Transactional
    public void deleteCategory(DeleteCategoryCommand command) {
        Category category = categoryRepository.findByIdAndUserId(command.getCategoryId(), command.getUserId())
                .orElseThrow(() -> new RuntimeException("카테고리를 찾을 수 없습니다."));

        category.deactivate();
        log.info("카테고리 비활성화 완료 - categoryId: {}", command.getCategoryId());
    }
}
