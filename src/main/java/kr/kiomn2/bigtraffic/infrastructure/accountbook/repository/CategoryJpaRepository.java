package kr.kiomn2.bigtraffic.infrastructure.accountbook.repository;

import kr.kiomn2.bigtraffic.domain.accountbook.entity.Category;
import kr.kiomn2.bigtraffic.domain.accountbook.vo.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryJpaRepository extends JpaRepository<Category, Long> {

    /**
     * 사용자 ID로 카테고리 목록 조회
     */
    List<Category> findByUserId(Long userId);

    /**
     * 사용자 ID와 활성 상태로 카테고리 목록 조회
     */
    List<Category> findByUserIdAndIsActive(Long userId, Boolean isActive);

    /**
     * 사용자 ID와 거래 유형으로 카테고리 목록 조회
     */
    List<Category> findByUserIdAndType(Long userId, TransactionType type);

    /**
     * 사용자 ID와 거래 유형, 활성 상태로 카테고리 목록 조회
     */
    List<Category> findByUserIdAndTypeAndIsActive(Long userId, TransactionType type, Boolean isActive);

    /**
     * 사용자 ID와 카테고리 ID로 조회
     */
    Optional<Category> findByIdAndUserId(Long id, Long userId);

    /**
     * 기본 카테고리 조회
     */
    List<Category> findByUserIdAndIsDefaultTrue(Long userId);
}
