package kr.kiomn2.bigtraffic.infrastructure.accountbook.repository;

import kr.kiomn2.bigtraffic.domain.accountbook.entity.Category;
import kr.kiomn2.bigtraffic.domain.accountbook.repository.CategoryRepository;
import kr.kiomn2.bigtraffic.domain.accountbook.vo.TransactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {

    private final CategoryJpaRepository jpaRepository;

    @Override
    public List<Category> findByUserId(Long userId) {
        return jpaRepository.findByUserId(userId);
    }

    @Override
    public List<Category> findByUserIdAndIsActive(Long userId, Boolean isActive) {
        return jpaRepository.findByUserIdAndIsActive(userId, isActive);
    }

    @Override
    public List<Category> findByUserIdAndType(Long userId, TransactionType type) {
        return jpaRepository.findByUserIdAndType(userId, type);
    }

    @Override
    public List<Category> findByUserIdAndTypeAndIsActive(Long userId, TransactionType type, Boolean isActive) {
        return jpaRepository.findByUserIdAndTypeAndIsActive(userId, type, isActive);
    }

    @Override
    public Optional<Category> findByIdAndUserId(Long id, Long userId) {
        return jpaRepository.findByIdAndUserId(id, userId);
    }

    @Override
    public Category save(Category category) {
        return jpaRepository.save(category);
    }

    @Override
    public List<Category> saveAll(List<Category> categories) {
        return jpaRepository.saveAll(categories);
    }
}
