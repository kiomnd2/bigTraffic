package kr.kiomn2.bigtraffic.infrastructure.finance.repository;

import kr.kiomn2.bigtraffic.domain.finance.entity.Card;
import kr.kiomn2.bigtraffic.domain.finance.vo.CardType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardJpaRepository extends JpaRepository<Card, Long> {

    List<Card> findByUserId(Long userId);

    List<Card> findByUserIdAndIsActive(Long userId, Boolean isActive);

    List<Card> findByUserIdAndCardType(Long userId, CardType cardType);

    Optional<Card> findByIdAndUserId(Long id, Long userId);

    Optional<Card> findByUserIdAndIsDefault(Long userId, Boolean isDefault);

    @Modifying
    @Query("UPDATE Card c SET c.isDefault = false WHERE c.userId = :userId AND c.id != :cardId")
    void unsetDefaultForOtherCards(@Param("userId") Long userId, @Param("cardId") Long cardId);

    boolean existsByUserIdAndIsDefault(Long userId, Boolean isDefault);

    long countByUserId(Long userId);
}
