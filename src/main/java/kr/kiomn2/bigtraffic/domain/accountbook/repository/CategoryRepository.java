package kr.kiomn2.bigtraffic.domain.accountbook.repository;

import kr.kiomn2.bigtraffic.domain.accountbook.entity.Category;
import kr.kiomn2.bigtraffic.domain.accountbook.vo.TransactionType;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {

    List<Category> findByUserId(Long userId);

    List<Category> findByUserIdAndIsActive(Long userId, Boolean isActive);

    List<Category> findByUserIdAndType(Long userId, TransactionType type);

    List<Category> findByUserIdAndTypeAndIsActive(Long userId, TransactionType type, Boolean isActive);

    Optional<Category> findByIdAndUserId(Long id, Long userId);

    Category save(Category category);

    List<Category> saveAll(List<Category> categories);
}
