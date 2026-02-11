package kr.kiomn2.bigtraffic.domain.accountbook.service;

import kr.kiomn2.bigtraffic.domain.accountbook.command.CreateCategoryCommand;
import kr.kiomn2.bigtraffic.domain.accountbook.command.DeleteCategoryCommand;
import kr.kiomn2.bigtraffic.domain.accountbook.command.UpdateCategoryCommand;
import kr.kiomn2.bigtraffic.domain.accountbook.entity.Category;
import kr.kiomn2.bigtraffic.domain.accountbook.query.GetCategoriesQuery;
import kr.kiomn2.bigtraffic.domain.accountbook.query.GetCategoryQuery;
import kr.kiomn2.bigtraffic.domain.accountbook.repository.CategoryRepository;
import kr.kiomn2.bigtraffic.domain.accountbook.vo.TransactionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public Category createCategory(CreateCategoryCommand command) {
        Category category = Category.createCustom(command);
        Category saved = categoryRepository.save(category);

        log.info("카테고리 생성 완료 - categoryId: {}, name: {}", saved.getId(), saved.getName());
        return saved;
    }

    public List<Category> getCategories(GetCategoriesQuery query) {
        if (query.getType() != null && query.getIsActive() != null) {
            return categoryRepository.findByUserIdAndTypeAndIsActive(
                    query.getUserId(), query.getType(), query.getIsActive()
            );
        } else if (query.getType() != null) {
            return categoryRepository.findByUserIdAndType(query.getUserId(), query.getType());
        } else if (query.getIsActive() != null) {
            return categoryRepository.findByUserIdAndIsActive(query.getUserId(), query.getIsActive());
        } else {
            return categoryRepository.findByUserId(query.getUserId());
        }
    }

    public Category getCategory(GetCategoryQuery query) {
        return categoryRepository.findByIdAndUserId(query.getCategoryId(), query.getUserId())
                .orElseThrow(() -> new RuntimeException("카테고리를 찾을 수 없습니다."));
    }

    @Transactional
    public Category updateCategory(UpdateCategoryCommand command) {
        Category category = categoryRepository.findByIdAndUserId(command.getCategoryId(), command.getUserId())
                .orElseThrow(() -> new RuntimeException("카테고리를 찾을 수 없습니다."));

        category.updateInfo(command);
        log.info("카테고리 수정 완료 - categoryId: {}", command.getCategoryId());

        return category;
    }

    @Transactional
    public void deleteCategory(DeleteCategoryCommand command) {
        Category category = categoryRepository.findByIdAndUserId(command.getCategoryId(), command.getUserId())
                .orElseThrow(() -> new RuntimeException("카테고리를 찾을 수 없습니다."));

        category.deactivate();
        log.info("카테고리 비활성화 완료 - categoryId: {}", command.getCategoryId());
    }

    @Transactional
    public void createDefaultCategories(Long userId) {
        log.info("기본 카테고리 생성 시작 - userId: {}", userId);

        List<Category> defaultCategories = new ArrayList<>();

        defaultCategories.add(Category.createDefault(userId, "급여", TransactionType.INCOME, "#4CAF50"));
        defaultCategories.add(Category.createDefault(userId, "부수입", TransactionType.INCOME, "#8BC34A"));
        defaultCategories.add(Category.createDefault(userId, "용돈", TransactionType.INCOME, "#CDDC39"));
        defaultCategories.add(Category.createDefault(userId, "기타 수입", TransactionType.INCOME, "#FFC107"));

        defaultCategories.add(Category.createDefault(userId, "식비", TransactionType.EXPENSE, "#F44336"));
        defaultCategories.add(Category.createDefault(userId, "교통비", TransactionType.EXPENSE, "#E91E63"));
        defaultCategories.add(Category.createDefault(userId, "주거비", TransactionType.EXPENSE, "#9C27B0"));
        defaultCategories.add(Category.createDefault(userId, "통신비", TransactionType.EXPENSE, "#673AB7"));
        defaultCategories.add(Category.createDefault(userId, "의료비", TransactionType.EXPENSE, "#3F51B5"));
        defaultCategories.add(Category.createDefault(userId, "문화/여가", TransactionType.EXPENSE, "#2196F3"));
        defaultCategories.add(Category.createDefault(userId, "쇼핑", TransactionType.EXPENSE, "#00BCD4"));
        defaultCategories.add(Category.createDefault(userId, "교육비", TransactionType.EXPENSE, "#009688"));
        defaultCategories.add(Category.createDefault(userId, "기타 지출", TransactionType.EXPENSE, "#795548"));

        categoryRepository.saveAll(defaultCategories);
        log.info("기본 카테고리 생성 완료 - userId: {}, 카테고리 수: {}", userId, defaultCategories.size());
    }
}
